package com.taskscheduler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskscheduler.model.Task;
import com.taskscheduler.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private GroqService groqService;

    public Task createTask(Task task) {
    if (task.getPriority() == null || task.getPriority().isEmpty()) {
        String priority = groqService.predictPriority(task.getTitle(), task.getDescription());
        task.setPriority(priority);
    }
    return taskRepository.save(task);
    }
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateStatus(int id, String status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public void deleteTask(int id) {
        taskRepository.deleteById(id);
    }
}