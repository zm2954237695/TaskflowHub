package com.taskflowhub.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskflowhub.api.entity.ProjectEntity;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProjectMapper extends BaseMapper<ProjectEntity> {
    @Select("SELECT p.id, p.name, p.project_key, p.description, p.color, p.created_at, COUNT(t.id) AS task_count, COALESCE(SUM(CASE WHEN t.status='DONE' THEN 1 ELSE 0 END),0) AS completed_count FROM projects p LEFT JOIN tasks t ON t.project_id=p.id GROUP BY p.id,p.name,p.project_key,p.description,p.color,p.created_at ORDER BY p.id")
    List<ProjectSummaryRow> selectProjectSummaries();

    @Select("SELECT p.id, p.name, p.project_key, p.description, p.color, p.created_at, COUNT(t.id) AS task_count, COALESCE(SUM(CASE WHEN t.status='DONE' THEN 1 ELSE 0 END),0) AS completed_count FROM projects p LEFT JOIN tasks t ON t.project_id=p.id WHERE p.id=#{id} GROUP BY p.id,p.name,p.project_key,p.description,p.color,p.created_at")
    ProjectSummaryRow selectProjectSummaryById(@Param("id") long id);

    class ProjectSummaryRow {
        private Long id; private String name; private String projectKey; private String description; private String color; private Integer taskCount; private Integer completedCount; private java.time.LocalDateTime createdAt;
        public Long getId(){return id;} public void setId(Long v){id=v;} public String getName(){return name;} public void setName(String v){name=v;} public String getProjectKey(){return projectKey;} public void setProjectKey(String v){projectKey=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;} public String getColor(){return color;} public void setColor(String v){color=v;} public Integer getTaskCount(){return taskCount;} public void setTaskCount(Integer v){taskCount=v;} public Integer getCompletedCount(){return completedCount;} public void setCompletedCount(Integer v){completedCount=v;} public java.time.LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(java.time.LocalDateTime v){createdAt=v;}
    }
}
