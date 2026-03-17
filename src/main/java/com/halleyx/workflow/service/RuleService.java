package com.halleyx.workflow.service;

import com.halleyx.workflow.entity.Rule;
import com.halleyx.workflow.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RuleService {

    private final RuleRepository ruleRepository;

    @Transactional
    public Rule createRule(UUID stepId, Rule rule) {
        rule.setStepId(stepId);
        return ruleRepository.save(rule);
    }

    public List<Rule> getRulesByStep(UUID stepId) {
        return ruleRepository.findByStepIdOrderByPriorityAsc(stepId);
    }
}