package com.taskflowhub.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskflowhub.api.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    @Select("SELECT id, username, password_hash AS passwordHash, display_name AS displayName, role, avatar, enabled, created_at AS createdAt FROM sys_user WHERE username = #{username} AND enabled = 1 LIMIT 1")
    UserEntity findByUsername(@Param("username") String username);
}
