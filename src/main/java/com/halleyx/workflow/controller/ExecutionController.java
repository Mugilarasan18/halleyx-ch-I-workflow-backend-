package com.halleyx.workflow.controller;

import com.halleyx.workflow.entity.Execution;
import com.halleyx.workflow.repository.ExecutionRepository;
import com.halleyx.workflow.service.WorkflowExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/executions")
@RequiredArgsConstructor
public class ExecutionController {

    private final WorkflowExecutionService executionService;
    private final ExecutionRepository executionRepository;

    @PostMapping("/start/{workflowId}")
    public ResponseEntity<Execution> executeWorkflow(@PathVariable UUID workflowId, @RequestBody Map<String, Object> payload) {
        String user = (String) payload.get("triggeredBy");
        Map<String, Object> data = (Map<String, Object>) payload.get("inputData");
        return ResponseEntity.ok(executionService.startExecution(workflowId, data, user));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Execution>> getPendingRequests(@RequestParam String role) {
        String status = "PENDING_" + role.toUpperCase();
        return ResponseEntity.ok(executionRepository.findByStatusAndCurrentApproverRole(status, role.toUpperCase()));
    }

    @PostMapping("/{executionId}/approve")
    public ResponseEntity<Execution> approveOrReject(@PathVariable UUID executionId, @RequestParam String role, @RequestParam boolean isApproved, @RequestParam(required = false) String comments) {
        return ResponseEntity.ok(executionService.approveOrRejectExecution(executionId, role, isApproved, comments));
    }

    @GetMapping
    public ResponseEntity<List<Execution>> getByUser(@RequestParam String user) {
        return ResponseEntity.ok(executionRepository.findByTriggeredByOrderByStartedAtDesc(user));
    }

    @GetMapping("/all-history")
    public ResponseEntity<List<Execution>> getAllHistory() {
        // CEO dashboard-la analytics table-kku moththa history-um anuprom
        return ResponseEntity.ok(executionRepository.findAllByOrderByStartedAtDesc());
    }
}