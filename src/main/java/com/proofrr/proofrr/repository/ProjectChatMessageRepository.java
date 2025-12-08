package com.proofrr.proofrr.repository;

import com.proofrr.proofrr.model.Project;
import com.proofrr.proofrr.model.ProjectChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectChatMessageRepository extends JpaRepository<ProjectChatMessage, Long> {
    List<ProjectChatMessage> findByProjectOrderByCreatedAtAsc(Project project);
}
