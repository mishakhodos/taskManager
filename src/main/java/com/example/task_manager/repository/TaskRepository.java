package com.example.task_manager.repository;

import com.example.task_manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> , JpaSpecificationExecutor<Task>, PagingAndSortingRepository<Task, UUID> {
}
