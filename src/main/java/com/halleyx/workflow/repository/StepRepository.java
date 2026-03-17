package com.halleyx.workflow.repository;
import com.halleyx.workflow.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface StepRepository extends JpaRepository<Step, UUID> {
    List<Step> findByWorkflowIdOrderBySequenceOrderAsc(UUID workflowId);
}