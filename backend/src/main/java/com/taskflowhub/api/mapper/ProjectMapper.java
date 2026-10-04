package com.taskflowhub.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskflowhub.api.entity.ProjectEntity;
import com.taskflowhub.api.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectMapper extends BaseMapper<ProjectEntity> {
    @Select("SELECT p.id, p.name, p.project_key AS `key`, p.description, p.color, p.created_at AS createdAt, " +
            "COUNT(t.id) AS taskCount, COALESCE(SUM(CASE WHEN t.status='DONE' THEN 1 ELSE 0 END),0) AS completedCount " +
            "FROM projects p LEFT JOIN tasks t ON t.project_id=p.id GROUP BY p.id,p.name,p.project_key,p.description,p.color,p.created_at ORDER BY p.id")
    List<Project> selectProjectSummaries();

    @Select("SELECT p.id, p.name, p.project_key AS `key`, p.description, p.color, p.created_at AS createdAt, " +
            "COUNT(t.id) AS taskCount, COALESCE(SUM(CASE WHEN t.status='DONE' THEN 1 ELSE 0 END),0) AS completedCount " +
            "FROM projects p LEFT JOIN tasks t ON t.project_id=p.id WHERE p.id=#{id} " +
            "GROUP BY p.id,p.name,p.project_key,p.description,p.color,p.created_at")
    Project selectProjectSummaryById(@Param("id") long id);
}
