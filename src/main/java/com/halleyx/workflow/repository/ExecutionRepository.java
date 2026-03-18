package com.halleyx.workflow.repository;

import com.halleyx.workflow.entity.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ExecutionRepository extends JpaRepository<Execution, UUID> {
    List<Execution> findByTriggeredByOrderByStartedAtDesc(String triggeredBy);

    List<Execution> findByStatusAndCurrentApproverRole(String status, String role);

    List<Execution> findAllByOrderByStartedAtDesc();
}