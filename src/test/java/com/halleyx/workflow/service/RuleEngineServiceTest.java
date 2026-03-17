package com.halleyx.workflow.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RuleEngineServiceTest {

    private RuleEngineService ruleEngineService;

    @BeforeEach
    void setUp() {
        ruleEngineService = new RuleEngineService();
    }

    @Test
    void testConditionPasses_WhenMatch() {
        // Given
        String condition = "#amount > 100 && #country == 'US'";
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("amount", 150);
        inputData.put("country", "US");

        // When
        boolean result = ruleEngineService.evaluateCondition(condition, inputData);

        // Then
        assertTrue(result, "Condition should pass because amount > 100 and country is US");
    }

    @Test
    void testConditionFails_WhenNoMatch() {
        // Given
        String condition = "#amount > 100 && #country == 'US'";
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("amount", 50); // Fails here
        inputData.put("country", "US");

        // When
        boolean result = ruleEngineService.evaluateCondition(condition, inputData);

        // Then
        assertFalse(result, "Condition should fail because amount is not > 100");
    }

    @Test
    void testDefaultCondition_AlwaysPasses() {
        // Given
        String condition = "DEFAULT";
        Map<String, Object> inputData = new HashMap<>();

        // When
        boolean result = ruleEngineService.evaluateCondition(condition, inputData);

        // Then
        assertTrue(result, "DEFAULT condition must always pass");
    }

    @Test
    void testNullOrEmptyCondition_AlwaysPasses() {
        assertTrue(ruleEngineService.evaluateCondition(null, new HashMap<>()));
        assertTrue(ruleEngineService.evaluateCondition("   ", new HashMap<>()));
    }

    @Test
    void testInvalidSpELSyntax_ReturnsFalseSafely() {
        // Given a completely broken rule syntax
        String invalidCondition = "#amount >>>-- 100 ERROR";
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("amount", 150);

        // When
        boolean result = ruleEngineService.evaluateCondition(invalidCondition, inputData);

        // Then it should catch the exception internally and return false
        assertFalse(result, "Invalid SpEL syntax should be caught and return false");
    }
}