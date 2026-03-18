package com.halleyx.workflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "executions")
@Data
public class Execution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // ✅ RELATION WITH WORKFLOW (IMPORTANT FIX)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    private Integer workflowVersion;

    // Status: PENDING_ADMIN, PENDING_CEO, COMPLETED, REJECTED
    private String status = "PENDING";

    private UUID currentStepId;

    @Column(name = "current_approver_role")
    private String currentApproverRole;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> data;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> logs = new ArrayList<>();

    private Integer retries = 0;
    private String triggeredBy;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ✅ LOG HELPER
    public void addLog(String message) {
        if (this.logs == null) this.logs = new ArrayList<>();
        this.logs.add(LocalDateTime.now() + " - " + message);
    }

    // ✅ HELPER METHOD FOR DTO
    public String getWorkflowName() {
        return workflow != null ? workflow.getName() : "N/A";
    }

    // ✅ OPTIONAL FIX (if you have rule logic later)
    public String getRuleCondition() {
        return "N/A"; // or fetch from step/rule entity if exists
    }

    public void setWorkflowId(UUID id) {
    }
}