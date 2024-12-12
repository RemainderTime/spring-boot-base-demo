## 拿来即用springboot基础脚手架

---
### 项目介绍

[![](https://img.shields.io/badge/-@remaindertime-FC5531?style=flat&logo=csdn&logoColor=FC5531&labelColor=424242)](https://blog.csdn.net/qq_39818325?type=blog)
[![GitHub Stars](https://img.shields.io/github/stars/RemainderTime/spring-boot-base-demo?style=social)](https://github.com/RemainderTime/spring-boot-base-demo)
![](https://img.shields.io/badge/jdk-1.8+-blue.svg)
![](https://img.shields.io/badge/springboot-3.3.3-{徽标颜色}.svg)   
![](https://img.shields.io/badge/springdoc-2.6.0-{徽标颜色}.svg)
![](https://img.shields.io/badge/elasticsearch-8.16.0-005571.svg)
![](https://img.shields.io/badge/redis-3.3.3-FF4438.svg)
---
> 这是一个基于 **Spring Boot 3.3.3** 的快速构建单体架构脚手架，旨在帮助开发者快速搭建高效、稳定的项目基础框架。项目集成了多种常用的技术组件与功能，涵盖从用户认证到数据加密、从全局异常处理到搜索引擎操作，适合个人学习与企业级单体应用开发。

### 集成技术与功能亮点

- 身份认证与授权（JWT）：基于 JWT 实现用户认证与授权，确保系统安全性。
- 数据加密（RSA）：提供 RSA 非对称加密支持，保障敏感数据安全。
- 持久层框架（MyBatis Plus）：简化数据库操作，提供高效的 CRUD 支持。
- 数据库（MySQL）：采用 MySQL 作为默认数据库，易于扩展和维护。
- 数据连接池（Hikari）：高性能数据源管理，优化数据库连接效率。
- 缓存（Redis）：支持分布式缓存，提升系统响应速度与并发能力。
- 接口文档（springdoc-openapi）：自动生成标准化 API 文档，便于调试与集成。
- 模板引擎（Thymeleaf）：支持动态页面渲染，提升前后端协同效率。
- 容器化支持（Docker）：内置 Dockerfile，轻松实现环境部署与迁移。
- 搜索引擎（Elasticsearch 8.x）：集成最新版本 Elasticsearch Java 客户端，提供高效的全文检索与复杂查询功能。
- 全局异常处理：统一管理异常，提升代码可维护性与调试效率。
- 拦截器支持：轻松实现请求拦截与权限控制。

### 项目优势
**全面适配 Spring Boot 3.x**
- 所有组件已全面升级为支持 Spring Boot 3.x 的最新版本。解决了开发者在版本升级中遇到的各种不兼容和适配问题，大大减少了升级带来的额外工作量，让项目开发更加顺畅。

**初学者友好**
- 提供清晰的代码结构与详细的配置说明，帮助初学者快速上手微服务与单体架构的开发实践。

**高扩展性**
- 丰富的功能集成，涵盖了开发中常见的场景，减少重复开发工作量，同时为定制化需求预留了扩展空间。

**稀缺的最新技术操作示例**
- 最新版本的 Elasticsearch 8.x 集成、Java 客户端操作示例和现代化 API 设计，让开发者能够轻松掌握分布式搜索引擎的使用。

### 版本更新 2024-10-12

---
1. springboot版本升级3.x
2. mybatis plus版本升级3.x
3. dynamic mybatis plus版本升级3.x
4. redis版本升级3.x以及配置优化
5. 替换swagger依赖支持spring boot3.x (knife4j->springdoc-openapi)
6. 新增请求头工具类
7. 参数校验异常捕获优化
8. 登录拦截器注册为spring容器管理 
9. 新增本地日志配置文件

---
如果这个项目对你有帮助，请随手点个 Star ⭐ 支持一下吧！🎉✨ 你的支持是我持续优化的动力！❤️

