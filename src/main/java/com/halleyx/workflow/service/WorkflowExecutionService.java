package com.halleyx.workflow.service;

import com.halleyx.workflow.entity.Execution;
import com.halleyx.workflow.entity.Rule;
import com.halleyx.workflow.entity.Step;
import com.halleyx.workflow.entity.Workflow;
import com.halleyx.workflow.repository.ExecutionRepository;
import com.halleyx.workflow.repository.RuleRepository;
import com.halleyx.workflow.repository.StepRepository;
import com.halleyx.workflow.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkflowExecutionService {

    private final WorkflowRepository workflowRepository;
    private final StepRepository stepRepository;
    private final RuleRepository ruleRepository;
    private final ExecutionRepository executionRepository;
    private final RuleEngineService ruleEngineService;

    /**
     * User dashboard-la "Submit" click pannum podhu idhu trigger aagum.
     * Request-a Admin approval list-ku anuppi "Pause" pannidhum.
     */
    @Transactional
    public Execution startExecution(UUID workflowId, Map<String, Object> inputData, String userId) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        if (!workflow.getIsActive()) {
            throw new RuntimeException("Workflow is not active");
        }

        Execution execution = new Execution();
        execution.setWorkflowId(workflow.getId());
        execution.setWorkflowVersion(workflow.getVersion());
        execution.setData(inputData);
        execution.setTriggeredBy(userId);
        execution.setStartedAt(LocalDateTime.now());

        execution.setStatus("PENDING_ADMIN");
        execution.setCurrentApproverRole("ADMIN");
        execution.setCurrentStepId(workflow.getStartStepId());
        execution.addLog("Request submitted. Waiting for ADMIN verification.");

        return executionRepository.save(execution);
    }


    @Transactional
    public Execution approveOrRejectExecution(UUID executionId, String role, boolean isApproved, String comments) {
        Execution execution = executionRepository.findById(executionId)
                .orElseThrow(() -> new RuntimeException("Execution not found"));

        if (isApproved) {
            execution.addLog(role + " APPROVED. Comments: " + comments);

            execution.setStatus("IN_PROGRESS");
            executionRepository.save(execution);

            processWorkflow(execution);
        } else {
            execution.setStatus("REJECTED");
            execution.setCurrentApproverRole(null);
            execution.setEndedAt(LocalDateTime.now());
            execution.addLog(role + " REJECTED by " + role + ". Comments: " + comments);
            executionRepository.save(execution);
        }
        return execution;
    }


    private void processWorkflow(Execution execution) {
        UUID currentStepId = execution.getCurrentStepId();
        Map<String, Object> inputData = execution.getData();

        while (currentStepId != null) {
            Step step = stepRepository.findById(currentStepId)
                    .orElseThrow(() -> new RuntimeException("Step not found"));

            String stepName = step.getName().toUpperCase();

            if (stepName.contains("ADMIN") || stepName.contains("MANAGER")) {
                execution.setStatus("PENDING_ADMIN");
                execution.setCurrentApproverRole("ADMIN");
                execution.addLog("Paused: Waiting for ADMIN approval at step: " + step.getName());
                executionRepository.save(execution);
                return;
            }
            else if (stepName.contains("CEO") || stepName.contains("DIRECTOR")) {
                execution.setStatus("PENDING_CEO");
                execution.setCurrentApproverRole("CEO");
                execution.addLog("Paused: Waiting for CEO approval at step: " + step.getName());
                executionRepository.save(execution);
                return;
            }

            List<Rule> rules = ruleRepository.findByStepIdOrderByPriorityAsc(currentStepId);
            UUID nextStepId = null;
            boolean ruleMatched = false;

            for (Rule rule : rules) {
                if (ruleEngineService.evaluateCondition(rule.getConditionExpression(), inputData)) {
                    execution.addLog("Matched Rule: " + rule.getConditionExpression());
                    nextStepId = rule.getNextStepId();
                    ruleMatched = true;
                    break;
                }
            }

            if (!ruleMatched && !rules.isEmpty()) {
                throw new RuntimeException("No rules matched for step: " + step.getName());
            }

            currentStepId = nextStepId;
            execution.setCurrentStepId(currentStepId);

            executionRepository.save(execution);
        }

        if (execution.getCurrentStepId() == null) {
            execution.setStatus("COMPLETED");
            execution.setCurrentApproverRole(null);
            execution.setEndedAt(LocalDateTime.now());
            execution.addLog("Workflow Completed Successfully.");
            executionRepository.save(execution);
        }
    }
}