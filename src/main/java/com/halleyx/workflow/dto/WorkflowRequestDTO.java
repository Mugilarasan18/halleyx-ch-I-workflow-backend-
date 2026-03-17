package com.halleyx.workflow.dto;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class WorkflowRequestDTO {
    private String name;
    private Boolean isActive = true;
    private Map<String, Object> inputSchema;
    private UUID startStepId;
}