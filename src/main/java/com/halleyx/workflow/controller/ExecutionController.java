package com.halleyx.workflow.controller;

import com.halleyx.workflow.entity.Execution;
import com.halleyx.workflow.repository.ExecutionRepository;
import com.halleyx.workflow.service.WorkflowExecutionService;
import com.halleyx.workflow.dto.ExecutionStartDTO;
import com.halleyx.workflow.dto.ExecutionResponseDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/executions")
@RequiredArgsConstructor
public class ExecutionController {

    private final WorkflowExecutionService executionService;
    private final ExecutionRepository executionRepository;

    // ✅ START WORKFLOW (UPDATED TO USE DTO)
    @PostMapping("/start/{workflowId}")
    public ResponseEntity<Execution> executeWorkflow(
            @PathVariable UUID workflowId,
            @RequestBody ExecutionStartDTO dto) {

        return ResponseEntity.ok(
                executionService.startExecution(
                        workflowId,
                        dto.getInputData(),
                        dto.getTriggeredBy()
                )
        );
    }

    // ✅ PENDING REQUESTS
    @GetMapping("/pending")
    public ResponseEntity<List<Execution>> getPendingRequests(@RequestParam String role) {
        String status = "PENDING_" + role.toUpperCase();
        return ResponseEntity.ok(
                executionRepository.findByStatusAndCurrentApproverRole(status, role.toUpperCase())
        );
    }

    // ✅ APPROVE / REJECT
    @PostMapping("/{executionId}/approve")
    public ResponseEntity<Execution> approveOrReject(
            @PathVariable UUID executionId,
            @RequestParam String role,
            @RequestParam boolean isApproved,
            @RequestParam(required = false) String comments) {

        return ResponseEntity.ok(
                executionService.approveOrRejectExecution(executionId, role, isApproved, comments)
        );
    }

    // ✅ USER HISTORY
    @GetMapping
    public ResponseEntity<List<Execution>> getByUser(@RequestParam String user) {
        return ResponseEntity.ok(
                executionRepository.findByTriggeredByOrderByStartedAtDesc(user)
        );
    }

    @GetMapping("/all-history")
    public ResponseEntity<List<ExecutionResponseDTO>> getAllHistory() {

        List<Execution> executions = executionRepository.findAllByOrderByStartedAtDesc();

        List<ExecutionResponseDTO> response = executions.stream().map(ex -> {
            ExecutionResponseDTO dto = new ExecutionResponseDTO();

            dto.setId(ex.getId());
            dto.setTriggeredBy(ex.getTriggeredBy());

            dto.setWorkflowName(
                    ex.getWorkflow() != null ? ex.getWorkflow().getName() : "N/A"
            );

            dto.setRuleCondition(ex.getRuleCondition());
            dto.setLogs(ex.getLogs());
            dto.setStatus(ex.getStatus());

            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }
}