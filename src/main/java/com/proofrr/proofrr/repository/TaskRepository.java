package com.proofrr.proofrr.repository;

import com.proofrr.proofrr.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndCategory_Id(Long userId, Long categoryId);
}
