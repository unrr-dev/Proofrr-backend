package com.proofrr.proofrr.repository;

import com.proofrr.proofrr.model.Project;
import com.proofrr.proofrr.model.ProjectVisitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectVisitorRepository extends JpaRepository<ProjectVisitor, Long> {
    Optional<ProjectVisitor> findByIdAndProject(Long id, Project project);
}
