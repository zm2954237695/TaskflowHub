package com.taskflowhub.api.controller;

import com.taskflowhub.api.model.*;
import com.taskflowhub.api.service.TaskFlowService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class TaskFlowController {
    private final TaskFlowService service;

    public TaskFlowController(TaskFlowService service) {
        this.service = service;
    }

    @PostMapping("/auth/login")
    public Map<String, Object> login(@RequestBody LoginRequest r) {
        if (r.username() == null || r.username().isBlank() || r.password() == null || r.password().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "请输入账号和密码");
        return Map.of("accessToken", "demo-token", "expiresIn", 7200, "user", Map.of("id", 1, "name", "林小满", "username", r.username(), "role", "项目管理员"));
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return service.dashboard();
    }

    @GetMapping("/projects")
    public Map<String, Object> projects() {
        List<Project> items = service.projects();
        return Map.of("items", items, "total", items.size());
    }

    @PostMapping("/projects")
    public Project createProject(@RequestBody ProjectRequest r) {
        if (r.name() == null || r.name().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "项目名称不能为空");
        return service.createProject(r.name(), r.description(), r.color());
    }

    @GetMapping("/tasks")
    public Map<String, Object> tasks(@RequestParam(required = false) Long projectId, @RequestParam(required = false) String status, @RequestParam(required = false) String keyword) {
        List<Task> items = service.tasks(projectId, status, keyword);
        return Map.of("items", items, "total", items.size());
    }

    @PostMapping("/projects/{projectId}/tasks")
    public Task createTask(@PathVariable long projectId, @RequestBody TaskRequest r) {
        if (r.title() == null || r.title().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "任务标题不能为空");
        try {
            return service.createTask(projectId, r.title(), r.description(), r.priority(), r.assignee(), r.dueDate());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/tasks/{taskId}/status")
    public Task updateStatus(@PathVariable long taskId, @RequestBody StatusRequest r) {
        try {
            return service.updateStatus(taskId, r.status());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    public record LoginRequest(String username, String password) {
    }

    public record ProjectRequest(@NotBlank String name, String description, String color) {
    }

    public record TaskRequest(@NotBlank String title, String description, String priority, String assignee,
                              String dueDate) {
    }

    public record StatusRequest(@NotBlank String status) {
    }
}
