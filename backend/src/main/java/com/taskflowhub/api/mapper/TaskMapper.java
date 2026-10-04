package com.taskflowhub.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskflowhub.api.entity.TaskEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<TaskEntity> {
    @SelectProvider(type = TaskSqlProvider.class, method = "selectTasks")
    List<TaskEntity> selectTasks(@Param("projectId") Long projectId, @Param("status") String status, @Param("keyword") String keyword);

    @Select("SELECT COUNT(*) FROM tasks")
    long countAll();

    @Select("SELECT COUNT(*) FROM tasks WHERE status=#{status}")
    long countByStatus(@Param("status") String status);

    @Select("SELECT id, project_id, title, description, status, priority, assignee, due_date, created_at, updated_at FROM tasks WHERE assignee=#{assignee} AND status <> 'DONE' ORDER BY due_date, id")
    List<TaskEntity> selectOpenTasksByAssignee(@Param("assignee") String assignee);

    class TaskSqlProvider {
        public String selectTasks(java.util.Map<String, Object> params) {
            StringBuilder sql = new StringBuilder("SELECT id, project_id, title, description, status, priority, assignee, due_date, created_at, updated_at FROM tasks WHERE 1=1");
            if (params.get("projectId") != null) sql.append(" AND project_id = #{projectId}");
            String status = (String) params.get("status"), keyword = (String) params.get("keyword");
            if (status != null && !status.isBlank() && !"ALL".equals(status)) sql.append(" AND status = #{status}");
            if (keyword != null && !keyword.isBlank())
                sql.append(" AND (title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))");
            return sql.append(" ORDER BY FIELD(status, 'TODO','IN_PROGRESS','REVIEW','PAUSED','DONE'), id").toString();
        }
    }
}
