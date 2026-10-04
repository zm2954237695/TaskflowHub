# TaskFlowHub

TaskFlowHub 是一个面向创意与技术服务接单场景的前后端分离系统。前台负责展示服务、搜索筛选、提交需求；管理后台提供收入趋势、订单状态、动态流和订单录入。

## 目录

- `frontend`：Vue 3 + Vite + Pinia + Vue Router + Lucide Vue，响应式用户端与管理端。
- `backend`：Spring Boot 2.7 REST API，Java 8 可运行，内置 H2 内存数据库用于演示。
- `database/schema.sql`：MySQL 8 建表、索引关系和初始化数据。

## 本地运行

1. 启动后端：`cd backend`，执行 `mvn spring-boot:run`，API 地址为 `http://localhost:8080`。
2. 启动前端：另开终端进入 `frontend`，执行 `npm install`、`npm run dev`，打开 `http://localhost:5173`。
3. 前台首页为 `/`，管理工作台为 `/admin`。演示接口使用内存数据，重启后恢复；生产部署时将 `application.yml` 数据源改为 MySQL。

## 技术栈与亮点

- 前端采用 Vue 3 Composition API、Vite HMR、Pinia 状态管理、Vue Router 路由和 Lucide 图标，组件与样式均为可替换的模块化实现。
- 后端采用 Spring Boot 2.7、RESTful API、分层 Service/Controller、CORS、H2/MySQL 双数据源配置；Java 8 兼容当前环境，生产建议升级至 Java 21 + Spring Boot 3.x。
- 以“需求评估 - 透明协作 - 准时交付”为核心业务闭环，前台和后台共享订单模型。
- UI 使用响应式网格、状态色、趋势图、筛选与弹窗表单，移动端支持折叠侧栏和自适应卡片。
- SQL 使用 `utf8mb4`、外键约束、金额精度和审计时间字段，便于接入真实鉴权、支付与消息中心。

## 默认演示数据

后台直接打开即可查看演示数据。登录接口为 `POST /api/auth/login`，当前为演示 token，不校验密码；接入生产时可替换为 Spring Security + JWT。
