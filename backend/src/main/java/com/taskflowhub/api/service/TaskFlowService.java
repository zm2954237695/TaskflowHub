package com.taskflowhub.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taskflowhub.api.entity.ProjectEntity;
import com.taskflowhub.api.entity.TaskEntity;
import com.taskflowhub.api.mapper.ProjectMapper;
import com.taskflowhub.api.mapper.TaskMapper;
import com.taskflowhub.api.model.Project;
import com.taskflowhub.api.model.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class TaskFlowService {
    private static final Set<String> STATUSES = Set.of("TODO", "IN_PROGRESS", "REVIEW", "DONE", "PAUSED");
    private final ProjectMapper projects;
    private final TaskMapper tasks;

    public TaskFlowService(ProjectMapper projects, TaskMapper tasks) { this.projects = projects; this.tasks = tasks; }

    public List<Project> projects() { return projects.selectProjectSummaries(); }

    @Transactional
    public Project createProject(String name, String description, String color) {
        String raw = name.replaceAll("[^A-Za-z]", "").toUpperCase(Locale.ROOT);
        String key = raw.isBlank() ? "PROJ" : raw.substring(0, Math.min(4, raw.length()));
        ProjectEntity entity = new ProjectEntity(); entity.setName(name); entity.setProjectKey(key); entity.setDescription(description); entity.setColor(color == null ? "blue" : color);
        projects.insert(entity); return projects.selectProjectSummaryById(entity.getId());
    }

    public List<Task> tasks(Long projectId, String status, String keyword) { return tasks.selectTasks(projectId, status, keyword); }

    @Transactional
    public Task createTask(long projectId, String title, String description, String priority, String assignee, String dueDate) {
        if (projects.selectById(projectId) == null) throw new NoSuchElementException("项目不存在");
        TaskEntity entity = new TaskEntity(); entity.setProjectId(projectId); entity.setTitle(title); entity.setDescription(description); entity.setStatus("TODO"); entity.setPriority(priority == null ? "MEDIUM" : priority); entity.setAssignee(assignee == null ? "未分配" : assignee); entity.setDueDate(dueDate == null || dueDate.isBlank() ? null : LocalDate.parse(dueDate));
        tasks.insert(entity); return task(entity.getId());
    }

    @Transactional
    public Task updateStatus(long id, String status) {
        if (!STATUSES.contains(status)) throw new IllegalArgumentException("无效的任务状态");
        TaskEntity current = tasks.selectById(id); if (current == null) throw new NoSuchElementException("任务不存在");
        if ("TODO".equals(current.getStatus()) && "DONE".equals(status)) throw new IllegalArgumentException("待处理任务不能直接完成，请先进入进行中");
        TaskEntity update = new TaskEntity(); update.setId(id); update.setStatus(status); tasks.updateById(update); return task(id);
    }

    public Map<String, Object> dashboard() {
        long total = tasks.countAll(), done = tasks.countByStatus("DONE"), active = tasks.countByStatus("IN_PROGRESS") + tasks.countByStatus("REVIEW");
        return Map.of("totalTasks", total, "completedTasks", done, "activeTasks", active, "completionRate", total == 0 ? 0 : Math.round(done * 1000.0 / total) / 10.0, "myTasks", tasks.selectOpenTasksByAssignee("林小满"));
    }

    private Task task(long id) { TaskEntity e = tasks.selectById(id); if (e == null) throw new NoSuchElementException("任务不存在"); return toModel(e); }
    private Task toModel(TaskEntity e) { return new Task(e.getId(), e.getProjectId(), e.getTitle(), e.getDescription(), e.getStatus(), e.getPriority(), e.getAssignee(), e.getDueDate() == null ? null : e.getDueDate().toString(), e.getCreatedAt() == null ? null : e.getCreatedAt().toInstant(java.time.ZoneOffset.UTC)); }
}
