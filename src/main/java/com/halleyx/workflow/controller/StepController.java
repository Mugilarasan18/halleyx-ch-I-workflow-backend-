package com.halleyx.workflow.controller;

import com.halleyx.workflow.entity.Step;
import com.halleyx.workflow.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflows/{workflowId}/steps")
@RequiredArgsConstructor
public class StepController {

    private final StepService stepService;

    @PostMapping
    public ResponseEntity<Step> addStep(@PathVariable UUID workflowId, @RequestBody Step step) {
        return ResponseEntity.ok(stepService.createStep(workflowId, step));
    }

    @GetMapping
    public ResponseEntity<List<Step>> getSteps(@PathVariable UUID workflowId) {
        return ResponseEntity.ok(stepService.getStepsByWorkflow(workflowId));
    }

    @DeleteMapping("/{stepId}")
    public ResponseEntity<Void> deleteStep(@PathVariable UUID stepId) {
        stepService.deleteStep(stepId);
        return ResponseEntity.noContent().build();
    }
}