package com.taskflowhub.api.service;

import com.taskflowhub.api.model.Project;
import com.taskflowhub.api.model.Task;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskFlowService {
    private final AtomicLong projectSeq = new AtomicLong(3), taskSeq = new AtomicLong(8);
    private final Map<Long, ProjectState> projects = new ConcurrentHashMap<>();
    private final Map<Long, TaskState> tasks = new ConcurrentHashMap<>();

    public TaskFlowService() {
        projects.put(1L, new ProjectState(1, "产品增长计划", "GROW", "围绕新产品发布的增长与内容协作。", "mint"));
        projects.put(2L, new ProjectState(2, "TaskFlow Hub", "TFH", "任务协作系统的产品开发与交付。", "violet"));
        add(new TaskState(1, 1, "梳理新用户 onboarding 流程", "确认首次使用路径和关键转化节点。", "IN_PROGRESS", "HIGH", "林小满", "2026-10-05"));
        add(new TaskState(2, 1, "准备发布页文案", "完成首屏、功能和 FAQ 文案。", "TODO", "MEDIUM", "周予安", "2026-10-08"));
        add(new TaskState(3, 1, "埋点方案评审", "和数据团队确认事件命名及口径。", "REVIEW", "HIGH", "陈默", "2026-10-03"));
        add(new TaskState(4, 2, "完成权限模型设计", "定义系统角色和项目角色的边界。", "DONE", "HIGH", "林小满", "2026-09-30"));
        add(new TaskState(5, 2, "任务列表交互稿", "确定筛选、分页和空状态。", "IN_PROGRESS", "MEDIUM", "周予安", "2026-10-06"));
        add(new TaskState(6, 2, "配置本地开发环境", "整理启动说明和环境变量。", "DONE", "LOW", "陈默", "2026-09-28"));
        add(new TaskState(7, 2, "补充接口测试", "覆盖登录、任务状态和越权访问。", "TODO", "MEDIUM", "林小满", "2026-10-12"));
    }

    private void add(TaskState t) {
        tasks.put(t.id, t);
    }

    public List<Project> projects() {
        return projects.values().stream().sorted(Comparator.comparingLong(p -> p.id)).map(this::project).toList();
    }

    public Project createProject(String name, String description, String color) {
        long id = projectSeq.getAndIncrement();
        String raw = name.replaceAll("[^A-Za-z]", "").toUpperCase(Locale.ROOT);
        String key = raw.isBlank() ? "PROJ" + id : raw.substring(0, Math.min(4, raw.length()));
        ProjectState p = new ProjectState(id, name, key, description, color == null ? "blue" : color);
        projects.put(id, p);
        return project(p);
    }

    public List<Task> tasks(Long projectId, String status, String keyword) {
        return tasks.values().stream().filter(t -> projectId == null || t.projectId == projectId).filter(t -> status == null || status.isBlank() || "ALL".equals(status) || t.status.equals(status)).filter(t -> keyword == null || keyword.isBlank() || t.title.contains(keyword) || t.description.contains(keyword)).sorted(Comparator.comparing((TaskState t) -> t.status).thenComparingLong(t -> t.id)).map(this::task).toList();
    }

    public Task createTask(long projectId, String title, String description, String priority, String assignee, String dueDate) {
        if (!projects.containsKey(projectId)) throw new NoSuchElementException("项目不存在");
        TaskState t = new TaskState(taskSeq.getAndIncrement(), projectId, title, description, "TODO", priority == null ? "MEDIUM" : priority, assignee == null ? "未分配" : assignee, dueDate);
        tasks.put(t.id, t);
        return task(t);
    }

    public Task updateStatus(long id, String status) {
        TaskState t = taskState(id);
        if (!Set.of("TODO", "IN_PROGRESS", "REVIEW", "DONE", "PAUSED").contains(status))
            throw new IllegalArgumentException("无效的任务状态");
        if ("TODO".equals(t.status) && "DONE".equals(status)) throw new IllegalArgumentException("待处理任务不能直接完成，请先进入进行中");
        t.status = status;
        return task(t);
    }

    public Map<String, Object> dashboard() {
        long total = tasks.size(), done = tasks.values().stream().filter(t -> "DONE".equals(t.status)).count(), active = tasks.values().stream().filter(t -> Set.of("IN_PROGRESS", "REVIEW").contains(t.status)).count();
        return Map.of("totalTasks", total, "completedTasks", done, "activeTasks", active, "completionRate", total == 0 ? 0 : Math.round(done * 1000.0 / total) / 10.0, "myTasks", tasks.values().stream().filter(t -> "林小满".equals(t.assignee) && !"DONE".equals(t.status)).map(this::task).toList());
    }

    private TaskState taskState(long id) {
        TaskState t = tasks.get(id);
        if (t == null) throw new NoSuchElementException("任务不存在");
        return t;
    }

    private Project project(ProjectState p) {
        int total = (int) tasks.values().stream().filter(t -> t.projectId == p.id).count(), done = (int) tasks.values().stream().filter(t -> t.projectId == p.id && "DONE".equals(t.status)).count();
        return new Project(p.id, p.name, p.key, p.description, p.color, total, done, p.createdAt);
    }

    private Task task(TaskState t) {
        return new Task(t.id, t.projectId, t.title, t.description, t.status, t.priority, t.assignee, t.dueDate, t.createdAt);
    }

    private static class ProjectState {
        final long id;
        final String name, key, description, color;
        final Instant createdAt = Instant.now();

        ProjectState(long id, String name, String key, String description, String color) {
            this.id = id;
            this.name = name;
            this.key = key;
            this.description = description;
            this.color = color;
        }
    }

    private static class TaskState {
        final long id, projectId;
        final String title, description, priority, assignee, dueDate;
        final Instant createdAt = Instant.now();
        String status;

        TaskState(long id, long projectId, String title, String description, String status, String priority, String assignee, String dueDate) {
            this.id = id;
            this.projectId = projectId;
            this.title = title;
            this.description = description;
            this.status = status;
            this.priority = priority;
            this.assignee = assignee;
            this.dueDate = dueDate;
        }
    }
}
