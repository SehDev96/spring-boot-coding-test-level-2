package com.accenture.codingtest.springbootcodingtest.repository;

import com.accenture.codingtest.springbootcodingtest.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("select t from Task t where t.status='COMPLETED'")
    List<Task> findAllCompletedTasks();

    @Query("select t from Task t where t.status in ('NOT_STARTED,IN_PROGRESS,READY_FOR_TEST')")
    List<Task> findAllActiveTasks();

    @Query(value = "select t from app_task t where t.status='COMPLETED' and t.user_id= :user_id",nativeQuery = true)
    List<Task> findAllCompletedTasksByUsedId(UUID user_id);

    @Query(value = "select t from app_task t where t.status in ('NOT_STARTED,IN_PROGRESS,READY_FOR_TEST') and t.user_id= :user_id",nativeQuery = true)
    List<Task> findAllActiveTaskByUserId(UUID user_id);

    @Query(value = "select t from app_task t where t.status='COMPLETED' and t.project_id= :project_id",nativeQuery = true)
    List<Task> findAllCompletedTasksByProjectId(UUID project_id);

    @Query(value = "select t from app_task t where t.status in ('NOT_STARTED,IN_PROGRESS,READY_FOR_TEST') and t.project_id= :project_id",nativeQuery = true)
    List<Task> findAllActiveTaskByProjectId(UUID project_id);

    @Query(value = "select t from app_task t where t.project_id= :project_id",nativeQuery = true)
    List<Task> findAllByProject_id(UUID project_id);

    @Query(value = "select t from app_task t where t.user_id= :user_id",nativeQuery = true)
    List<Task> findAllByUser_id(UUID user_id);
}
