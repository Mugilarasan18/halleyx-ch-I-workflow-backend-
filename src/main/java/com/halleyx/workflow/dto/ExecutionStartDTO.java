package com.halleyx.workflow.dto;

import lombok.Data;
import java.util.Map;
import java.util.UUID;

@Data
public class ExecutionStartDTO {
    private Map<String, Object> inputData;
    private String triggeredBy;
}