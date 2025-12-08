package com.proofrr.proofrr.repository;

import com.proofrr.proofrr.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserId(Long userId);

    boolean existsByShareUuid(String shareUuid);

    Optional<Project> findByShareUuid(String shareUuid);
}
