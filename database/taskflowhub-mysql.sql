CREATE DATABASE IF NOT EXISTS taskflowhub DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE taskflowhub;

DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS projects;

CREATE TABLE projects (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  project_key VARCHAR(20) NOT NULL UNIQUE,
  description VARCHAR(500),
  color VARCHAR(24) NOT NULL DEFAULT 'blue',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE tasks (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  description VARCHAR(1000),
  status VARCHAR(20) NOT NULL DEFAULT 'TODO',
  priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
  assignee VARCHAR(80) NOT NULL DEFAULT '未分配',
  due_date DATE NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_tasks_project FOREIGN KEY (project_id) REFERENCES projects(id),
  CONSTRAINT chk_tasks_status CHECK (status IN ('TODO','IN_PROGRESS','REVIEW','DONE','PAUSED')),
  CONSTRAINT chk_tasks_priority CHECK (priority IN ('LOW','MEDIUM','HIGH')),
  INDEX idx_tasks_project_status (project_id, status),
  INDEX idx_tasks_assignee (assignee),
  INDEX idx_tasks_due_date (due_date)
) ENGINE=InnoDB;

INSERT INTO projects (id, name, project_key, description, color) VALUES
(1, '产品增长计划', 'GROW', '围绕新产品发布的增长与内容协作。', 'mint'),
(2, 'TaskFlow Hub', 'TFH', '任务协作系统的产品开发与交付。', 'violet');

INSERT INTO tasks (id, project_id, title, description, status, priority, assignee, due_date) VALUES
(1, 1, '梳理新用户 onboarding 流程', '确认首次使用路径和关键转化节点。', 'IN_PROGRESS', 'HIGH', '林小满', '2026-10-05'),
(2, 1, '准备发布页文案', '完成首屏、功能和 FAQ 文案。', 'TODO', 'MEDIUM', '周予安', '2026-10-08'),
(3, 1, '埋点方案评审', '和数据团队确认事件命名及口径。', 'REVIEW', 'HIGH', '陈默', '2026-10-03'),
(4, 2, '完成权限模型设计', '定义系统角色和项目角色的边界。', 'DONE', 'HIGH', '林小满', '2026-09-30'),
(5, 2, '任务列表交互稿', '确定筛选、分页和空状态。', 'IN_PROGRESS', 'MEDIUM', '周予安', '2026-10-06'),
(6, 2, '配置本地开发环境', '整理启动说明和环境变量。', 'DONE', 'LOW', '陈默', '2026-09-28'),
(7, 2, '补充接口测试', '覆盖登录、任务状态和越权访问。', 'TODO', 'MEDIUM', '林小满', '2026-10-12');

ALTER TABLE projects AUTO_INCREMENT = 3;
ALTER TABLE tasks AUTO_INCREMENT = 8;
