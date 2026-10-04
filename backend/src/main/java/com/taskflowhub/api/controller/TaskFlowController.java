package com.taskflowhub.api.controller;

import com.taskflowhub.api.entity.UserEntity;
import com.taskflowhub.api.mapper.UserMapper;
import com.taskflowhub.api.model.*;
import com.taskflowhub.api.security.JwtTokenService;
import com.taskflowhub.api.service.TaskFlowService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class TaskFlowController {
    private final TaskFlowService service;
    private final UserMapper users;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwt;

    public TaskFlowController(TaskFlowService service, UserMapper users, PasswordEncoder passwordEncoder, JwtTokenService jwt) {
        this.service = service;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwt = jwt;
    }

    @PostMapping("/auth/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        if (request.username() == null || request.username().isBlank() || request.password() == null || request.password().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "请输入账号和密码");
        }
        UserEntity user = users.findByUsername(request.username());
        System.out.println("LOGIN username=" + request.username() + ", userFound=" + (user != null) + ", hashLength=" + (user == null || user.getPasswordHash() == null ? 0 : user.getPasswordHash().length()) + ", matched=" + (user != null && user.getPasswordHash() != null && passwordEncoder.matches(request.password(), user.getPasswordHash())));
        if (user == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "账号或密码错误");
        }
        return Map.of("accessToken", jwt.create(user.getId(), user.getUsername(), user.getRole()), "expiresIn", 7200,
                "user", Map.of("id", user.getId(), "name", user.getDisplayName(), "username", user.getUsername(), "role", user.getRole()));
    }

    @PostMapping("/auth/register")
    public Map<String, Object> register(@RequestBody RegisterRequest request) {
        if (request.username() == null || !request.username().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "请输入有效的邮箱");
        if (request.password() == null || request.password().length() < 6)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密码至少需要 6 位");
        if (users.findByUsername(request.username()) != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "账号已存在");
        UserEntity user = new UserEntity();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setDisplayName(request.displayName() == null || request.displayName().isBlank() ? request.username().split("@")[0] : request.displayName());
        user.setRole("USER");
        user.setEnabled(true);
        users.insert(user);
        return Map.of("accessToken", jwt.create(user.getId(), user.getUsername(), user.getRole()), "expiresIn", 7200, "user", Map.of("id", user.getId(), "name", user.getDisplayName(), "username", user.getUsername(), "role", user.getRole()));
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
    public Project createProject(@RequestBody ProjectRequest request) {
        if (request.name() == null || request.name().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "项目名称不能为空");
        return service.createProject(request.name(), request.description(), request.color());
    }

    @GetMapping("/tasks")
    public Map<String, Object> tasks(@RequestParam(value = "projectId", required = false) Long projectId, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Task> items = service.tasks(projectId, status, keyword);
        return Map.of("items", items, "total", items.size());
    }

    @PostMapping("/projects/{projectId}/tasks")
    public Task createTask(@PathVariable("projectId") long projectId, @RequestBody TaskRequest request) {
        if (request.title() == null || request.title().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "任务标题不能为空");
        try {
            return service.createTask(projectId, request.title(), request.description(), request.priority(), request.assignee(), request.dueDate());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/tasks/{taskId}/status")
    public Task updateStatus(@PathVariable("taskId") long taskId, @RequestBody StatusRequest request) {
        try {
            return service.updateStatus(taskId, request.status());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    public record RegisterRequest(String username, String password, String displayName) {
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








