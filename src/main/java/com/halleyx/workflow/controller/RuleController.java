package com.halleyx.workflow.controller;

import com.halleyx.workflow.entity.Rule;
import com.halleyx.workflow.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/steps/{stepId}/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    @PostMapping
    public ResponseEntity<Rule> addRule(@PathVariable UUID stepId, @RequestBody Rule rule) {
        return ResponseEntity.ok(ruleService.createRule(stepId, rule));
    }

    @GetMapping
    public ResponseEntity<List<Rule>> getRules(@PathVariable UUID stepId) {
        return ResponseEntity.ok(ruleService.getRulesByStep(stepId));
    }
}