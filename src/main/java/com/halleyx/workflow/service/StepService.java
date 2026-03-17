package com.halleyx.workflow.service;

import com.halleyx.workflow.entity.Step;
import com.halleyx.workflow.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;

    @Transactional
    public Step createStep(UUID workflowId, Step step) {
        step.setWorkflowId(workflowId);
        return stepRepository.save(step);
    }

    public List<Step> getStepsByWorkflow(UUID workflowId) {
        return stepRepository.findByWorkflowIdOrderBySequenceOrderAsc(workflowId);
    }

    public void deleteStep(UUID stepId) {
        if (stepRepository.existsById(stepId)) {
            stepRepository.deleteById(stepId);
        } else {
            throw new RuntimeException("Step not found with id: " + stepId);
        }
    }
}