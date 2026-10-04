package com.taskflowhub.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("sys_user")
public class UserEntity {
    @TableId(type = IdType.AUTO) private Long id;
    private String username;
    private String passwordHash;
    private String displayName;
    private String role;
    private String avatar;
    private Boolean enabled;
    private LocalDateTime createdAt;
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getUsername(){return username;} public void setUsername(String v){username=v;}
    public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
    public String getDisplayName(){return displayName;} public void setDisplayName(String v){displayName=v;}
    public String getRole(){return role;} public void setRole(String v){role=v;}
    public String getAvatar(){return avatar;} public void setAvatar(String v){avatar=v;}
    public Boolean getEnabled(){return enabled;} public void setEnabled(Boolean v){enabled=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
