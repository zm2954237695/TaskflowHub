package com.taskflowhub.api.model;

import java.time.Instant;

public record Task(long id, long projectId, String title, String description, String status, String priority,
                   String assignee, String dueDate, Instant createdAt) {
}
