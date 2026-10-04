package com.taskflowhub.api.model;

import java.time.Instant;

public record Project(long id, String name, String key, String description, String color, int taskCount,
                      int completedCount, Instant createdAt) {
}
