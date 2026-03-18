package com.halleyx.workflow.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class ExecutionResponseDTO {

    private UUID id;
    private String triggeredBy;
    private String workflowName;
    private String ruleCondition;
    private List<String> logs;
    private String status;
}