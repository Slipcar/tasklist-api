package com.supero.tasklist.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.supero.tasklist.model.TaskModel;
import com.supero.tasklist.repository.TaskRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1")
@Api(value = "Desafio Supero - Task List")
@CrossOrigin("*")
public class TaskController {

	@Autowired
	private TaskRepository taskRepository;

	@GetMapping("/tasklist")
	@ApiOperation(value = "Retorna Todas as Tasks Cadastradas")
	public List<TaskModel> allTasks() {
		return taskRepository.findAll();
	}

	@GetMapping("/tasklist/{id}")
	@ApiOperation(value = "Retorna a Task do ID Informado")
	public Optional<TaskModel> getTaskById(@PathVariable(value = "id") Long id) {
		return Optional.of(taskRepository.
				findById(id).
				orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
	}

	@PostMapping("/tasklist")
	@ApiOperation(value = "Salva uma Task")
	public TaskModel newTask(@RequestBody TaskModel taskModel) {
		return taskRepository.save(taskModel);
	}
	
	@PutMapping("/tasklist/{id}/edit")
	@ApiOperation(value = "Edita a Task do ID Informado")
	public TaskModel taskEdit(@PathVariable Long id, @RequestBody TaskModel taskModel) {
			TaskModel currentTask = taskRepository.findById(id).get();
			BeanUtils.copyProperties(taskModel, currentTask, "id");
			return taskRepository.save(currentTask);
	}
	
	@PatchMapping("/tasklist/{id}/done")
	@ApiOperation(value = "Marca Uma Task Como Concluída")
	public TaskModel taskDone(@PathVariable Long id) {
		return taskRepository.findById(id).map(task -> {
			task.setDone(true);
			task.setDoneDate(LocalDateTime.now());
			taskRepository.save(task);
			return task;
		}).orElse(null);
	}

	@DeleteMapping("/tasklist/{id}")
	@ApiOperation(value = "Deleta uma Task")
	public void deleteTask(@PathVariable Long id) {
		taskRepository.deleteById(id);
	}
}
