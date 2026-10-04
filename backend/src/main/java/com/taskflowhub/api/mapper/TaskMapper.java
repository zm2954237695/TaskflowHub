package com.taskflowhub.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskflowhub.api.entity.TaskEntity;
import com.taskflowhub.api.model.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<TaskEntity> {
    @Select("<script>SELECT id, project_id AS projectId, title, description, status, priority, assignee, due_date AS dueDate, created_at AS createdAt FROM tasks WHERE 1=1 " +
            "<if test='projectId != null'>AND project_id=#{projectId} </if>" +
            "<if test='status != null and status != \'\' and status != \'ALL\''>AND status=#{status} </if>" +
            "<if test='keyword != null and keyword != \'\''>AND (title LIKE CONCAT('%',#{keyword},'%') OR description LIKE CONCAT('%',#{keyword},'%')) </if>" +
            "ORDER BY FIELD(status,'TODO','IN_PROGRESS','REVIEW','PAUSED','DONE'), id</script>")
    List<Task> selectTasks(@Param("projectId") Long projectId, @Param("status") String status, @Param("keyword") String keyword);

    @Select("SELECT COUNT(*) FROM tasks") long countAll();
    @Select("SELECT COUNT(*) FROM tasks WHERE status=#{status}") long countByStatus(@Param("status") String status);
    @Select("SELECT id, project_id AS projectId, title, description, status, priority, assignee, due_date AS dueDate, created_at AS createdAt FROM tasks WHERE assignee=#{assignee} AND status <> 'DONE' ORDER BY due_date, id")
    List<Task> selectOpenTasksByAssignee(@Param("assignee") String assignee);
}
