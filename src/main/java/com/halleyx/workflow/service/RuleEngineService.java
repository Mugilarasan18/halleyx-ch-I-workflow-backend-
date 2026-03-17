package com.halleyx.workflow.service;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RuleEngineService {

    private final ExpressionParser parser = new SpelExpressionParser();

    public boolean evaluateCondition(String condition, Map<String, Object> inputData) {
        if (condition == null || condition.trim().isEmpty() || "DEFAULT".equalsIgnoreCase(condition.trim())) {
            return true;
        }

        try {
            StandardEvaluationContext context = new StandardEvaluationContext();
            if (inputData != null) {
                inputData.forEach(context::setVariable);
            }
            Boolean result = parser.parseExpression(condition).getValue(context, Boolean.class);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            System.err.println("Rule Evaluation Failed: " + e.getMessage());
            return false; // Safely fail the rule if SpEL cannot parse it
        }
    }
}