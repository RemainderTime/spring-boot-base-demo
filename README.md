# XF-Boot-Base (Spring Boot Base Demo)

<div align="center">

<img src="https://img.shields.io/badge/Spring_Boot-3.3.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot"/>
<img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"/>
<img src="https://img.shields.io/badge/Nacos-2.x-00C7D5?style=for-the-badge&logo=AlibabaCloud&logoColor=white" alt="Nacos"/>
<img src="https://img.shields.io/badge/MyBatis_Plus-3.5.8-00599C?style=for-the-badge&logo=Spring&logoColor=white" alt="MyBatis Plus"/>

<br/>
<br/>

[![GitHub stars](https://img.shields.io/github/stars/RemainderTime/spring-boot-base-demo?style=social&label=Stars)](https://github.com/RemainderTime/spring-boot-base-demo)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](./LICENSE)

---

### 「 为企业级开发而生的高效脚手架 」

<p align="center">
  <a href="#-关于项目">💎 关于项目</a> &nbsp;|&nbsp;
  <a href="#-核心亮点">⚡ 核心亮点</a> &nbsp;|&nbsp;
  <a href="#-生态全景">🌿 生态全景</a> &nbsp;|&nbsp;
  <a href="#-快速运行">🚀 快速运行</a> &nbsp;|&nbsp;
  <a href="#-项目日志">📅 项目日志</a>
</p>

</div>

<br/>

## � 关于项目

**XF-Boot-Base** 并非仅仅是一个简单的 "Hello World" 示例，而是一个经过精心打磨、具备生产级标准的 **Spring Boot 3.3** 全栈开发底座。

我们深入分析了企业单体应用到微服务架构演进过程中的痛点，构建了一套**模块化、可插拔、高扩展**的基础框架。从底层的 **JDK 17** 优化，到顶层的 API 接口规范；从**JWT 安全认证**的丝滑接入，到 **Docker 容器化**的一键部署。XF-Boot-Base 旨在消除重复造轮子的时间成本，让开发者能够专注于核心业务逻辑的实现。

无论你是想要快速验证想法的独立开发者，还是寻找稳健基石的架构师，这里都有你需要的最佳实践。

---

## ⚡ 核心亮点

<div align="center">

| 🚀 **前沿技术** | 🔐 **安全无忧** | 🐳 **云原生友好** |
| :--- | :--- | :--- |
| 紧跟 **Spring Boot 3.x** 生态，<br>基于 **Java 17 LTS** 构建，<br>享受最新技术红利。 | 深度整合 **JWT** 令牌认证<br>& **RSA** 非对称加密，<br>为数据安全保驾护航。 | 内置 **Dockerfile** 脚本，<br>支持 **Docker Compose** 编排，<br>部署快人一步。 |

| 💾 **数据增强** | 🔌 **微服务预装** | 🛠 **极致体验** |
| :--- | :--- | :--- |
| **MyBatis Plus** 强力驱动，<br>**Dynamic Datasource**<br>轻松驾驭多数据源场景。 | 原生集成 **Nacos**，<br>配置中心与注册中心开箱即用，<br>平滑过渡微服务。 | **SpringDoc (OpenAPI 3)**<br>自动生成精美文档，<br>调试开发得心应手。 |

</div>

---

## 🌿 生态全景

我们采用 **"核心 + 插件化"** 的分支管理策略，以 `master` 为稳定基石，通过不同分支满足多样化的业务需求。

| 🌳 分支标识 | 🎯 定位 | 📝 功能描述 | 🏭 最佳应用场景 |
| :--- | :--- | :--- | :--- |
| **`master`** | **核心底座** | 标准化脚手架，含 JWT/Nacos/Redis | 🚀 快速启动标准单体项目 |
| `feature/admin-auth-spring-security` | **安全堡垒** | Spring Security 官方方案 (RBAC) | 🏦 金融级、政府级后台系统 |
| `feature/admin-auth-sa-token` | **敏捷权限** | Sa-Token 轻量级权限控制 | ⚡ 中小型项目、国内快速开发 |
| `component/rocketmq-and-es` | **高并发** | RocketMQ 5.x + Elasticsearch 8.x | 📈 海量日志、搜索、高吞吐业务 |
| `feature/master-payment` | **商业变现** | 支付宝沙盒支付 (H5/App) | 💳 电商、会员订阅、SaaS 平台 |

> **💡 提示**: 所有分支均基于 `master` 演进，可根据项目规模灵活 `git merge` 所需功能模块。

---

## 🚀 快速运行

### 🛠️ 环境依赖
*   **JDK**: 17 +
*   **Maven**: 3.8 +
*   **MySQL**: 8.0 +
*   **Redis**: 5.0 +
*   **Nacos**: 2.x (可选)

### 🏃‍♂️ 启动步骤

> **Step 1: 获取源码**
> ```bash
> git clone https://github.com/RemainderTime/spring-boot-base-demo.git
> cd spring-boot-base-demo
> ```

> **Step 2: 数据准备**
> *   创建数据库 `xf_boot_base`
> *   修改 `src/main/resources/application.yml` 配好你的数据库账号密码
> *   *(注：实体类完善，表结构建议通过 JPA 或手动创建)*

> **Step 3: 启动服务**
>
> **方式 A: 本地 Maven 运行**
> ```bash
> mvn spring-boot:run
> ```
>
> **方式 B: Docker Compose 一键编排**
> ```bash
> cd src/main/resources/docker
> docker-compose -f boot-docker-compose.yml up -d
> ```

> **Step 4: 探索接口**
> 打开浏览器访问: `http://localhost:8080/doc.html`

---

## 📅 项目日志

### v1.0.1 (2024-10-12)
*   ⬆️ **内核升级**: Spring Boot 3.3.3 & MyBatis Plus 3.5.8
*   📝 **文档重构**: 全面拥抱 SpringDoc OpenAPI，弃用旧版 Swagger
*   🛡️ **安全加固**: 优化全局异常拦截与参数校验机制

---

## 📈 关注趋势

[![Star History Chart](https://api.star-history.com/svg?repos=RemainderTime/spring-boot-base-demo&type=Date)](https://star-history.com/#RemainderTime/spring-boot-base-demo&Date)

<div align="center">
<br/>

**喜欢这个项目？请点个 Star ⭐ 支持作者持续更新！**

</div>




