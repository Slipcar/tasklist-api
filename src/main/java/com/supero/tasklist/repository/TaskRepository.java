package com.supero.tasklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supero.tasklist.model.TaskModel;

public interface TaskRepository extends JpaRepository<TaskModel, Long>{

}