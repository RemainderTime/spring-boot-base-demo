# Spring Boot 集成 Sa-Token 实现 RBAC 权限管理（生产级实战）

> 项目源码地址：[GitHub - xf-boot-base](https://github.com/XiongFengX/spring-boot-base-demo)  
> 分支切换指令：`git checkout sa-token-rbac` (请替换为你实际的分支名)

## 前言

随着项目从简单的 C 端应用扩展到 **B 端后台管理系统**，我们面临的核心痛点是如何高效实现 **RBAC（基于角色的访问控制）** 模型。
简单的 Token 登录已无法满足需求，而手动从零开发一套包含角色管理、菜单权限、按钮拦截的权限系统，不仅耗时耗力，还容易引入安全漏洞。

**为了解决这一痛点，本文将引入 Sa-Token 框架。**
Sa-Token 是一个轻量级 Java 权限认证框架，它不仅能完美接管基础的登录认证，更能以极简的代码实现复杂的权限控制。**本文将带你一步步通过 Sa-Token，构建一套标准的、生产级的后台权限体系。**

> **说明**：为了言简意赅，**文中只展示 Sa-Token 相关的核心功能代码**，不再罗列通用的基础设置（如全局异常处理、统一响应封装等）。无论您是从头新建项目，还是在已有架构上集成，这都是一份通用的标准参考方案。

---



## 二、准备环境

*   **JDK**: 17+
*   **Spring Boot**: 3.3.3
*   **MySQL**: 8.0+
*   **Redis**: 5.0+ (生产环境下 Sa-Token 强依赖 Redis 做数据持久化)

---

## 三、数据库设计 (RBAC 模型)

为了实现标准的 RBAC 权限控制，我们设计了以下五张核心表。这套设计支撑了“用户 -> 角色 -> 权限”的经典模型。

### 1. 核心表结构说明

*   **sys_user**: 用户表。存储用户的基本信息（账号、密码、昵称）。
*   **sys_role**: 角色表。定义系统中的角色（如：超级管理员、普通用户、运维人员）。
*   **sys_permission**: 权限（菜单）表。定义具体的资源权限标识（如 `user:add`, `user:delete`）。
*   **sys_user_role**: 用户-角色关联表。多对多关系，一个用户可以有多个角色。
*   **sys_role_permission**: 角色-权限关联表。多对多关系，一个角色可以拥有多个权限。

> **SQL 脚本**：项目完整源码中已包含 DDL 和初始化数据，请参考 `doc/sql` 目录。

---

## 四、Sa-Token 集成步骤

### 1. 引入依赖

除了核心 Starter，这里也引入了 Redis 集成插件。因为在生产环境（多实例部署）下，Session 通常需要是分布式的。

```xml
<!-- Sa-Token 核心依赖 -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-spring-boot3-starter</artifactId>
    <version>1.37.0</version>
</dependency>

<!-- Sa-Token 整合 Redis（使用 Jackson 序列化）-->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-redis-jackson</artifactId>
    <version>1.37.0</version>
</dependency>

<!-- Redis 连接池 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

### 2. 配置文件 (application.yml)

配置里有一个很有意思的决策点：`token-style`。

```yaml
sa-token: 
  token-name: Authorization
  timeout: 2592000 # 30天
  activity-timeout: -1 
  is-concurrent: true 
  is-read-header: true
  token-style: uuid # 重点关注这里
```

### 3. 聊聊关于 Token 风格的选择：JWT vs UUID

在很多教程里，大家习惯把 Token 和 JWT 划等号，甚至觉得“不用 JWT 就是不现代”。但在 Sa-Token 的使用场景下，这里配置为 `uuid`。

**我们先看看 JWT 的本质**：
JWT (Json Web Token) 设计的初衷是**去中心化**。它把用户信息（User ID、角色等）签个名直接放在 Token 字符串里。
*   **优势**：服务端不需要查库，拿到 Token 解密就知道你是谁。特别适合微服务之间透传用户信息，或者超大规模（亿级用户）不想查 Redis 的场景。
*   **劣势**：**无法撤销**。一旦发出去， unless 你换秘钥，否则没法让它提前失效。这就导致了“踢人下线”、“封号”这种功能在纯 JWT 模式下很难做。

**现在的尴尬现状（Redis + JWT）**：
为了解决 JWT 不能踢人的问题，很多方案变成了：*“用 JWT 并在 Redis 里存一个黑名单/白名单”*。
一旦引入了 Redis 查库，JWT **不查库** 的最大优势就没了。
既然都要查 Redis，那我为什么还要传一个几百字节长的 JWT 字符串？还要浪费 CPU 去验签？

**为什么选择 UUID 模式？**
*   **原理**：生成的 Token 就是一个简短的随机字符串。
*   **状态**：Redis 存 `Key: token`, `Value: UserInfo`。
*   **优势**：简单粗暴。想踢人？删 Redis Key 就行。Token 极短，省流量。
*   **适用性**：对于绝大多数企业级管理后台、App 后端，我们需要强管控能力（实时封号、踢人），UUID + Redis 是比较实用、高效的方案。

---

## 五、核心代码集成

### 1. 登录与注销

引入 Sa-Token 后，登录逻辑变得异常简单。框架会自动帮我们生成 Token、写入 Redis。

```java
// UserServiceImpl.java
public RetObj login(LoginInfoRes res) {
    // ... 账号密码校验逻辑 ...
    
    // 登录，Sa-Token 会自动处理 Session 和 Token
    StpUtil.login(user.getId());
    
    // 我们还可以把当前用户信息缓存起来，方便后续随时取用
    StpUtil.getSession().set("loginUser", loginUser);
    
    // ...
}
```

### 2. 权限接口实现 (`StpInterface`)

框架知道你登录了，但不知道你有什么权限。所以我们需要实现 `StpInterface` 接口，告诉 Sa-Token 如何去数据库查权限。

```java
@Component
public class StpInterfaceImpl implements StpInterface {
    
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回该用户的所有权限标识，例如 ["user:add", "role:list"]
        return sysPermissionMapper.getPermissionListByUserId(Long.valueOf(loginId.toString()));
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回该用户的所有角色标识，例如 ["admin"]
        return sysRoleMapper.getRoleListByUserId(Long.valueOf(loginId.toString()));
    }
}
```

---

## 六、拦截器设计的思考

在集成过程中，你可能会发现我的配置里注册了**两个**拦截器，这其实是为了兼容项目之前的一些老代码习惯。

```java
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private SaTokenContextInterceptor saTokenContextInterceptor;
    
    // 统一管理不需要拦截的路径（白名单）
    private static final String[] EXCLUDE_PATHS = { "/user/login", "/doc.html", ... };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. Sa-Token 核心拦截器 (负责鉴权)
        registry.addInterceptor(new SaInterceptor(handler -> {
            StpUtil.checkLogin(); // 开启注解鉴权
        })).addPathPatterns("/**").excludePathPatterns(EXCLUDE_PATHS);

        // 2. 上下文注入拦截器 (负责数据搬运)
        registry.addInterceptor(saTokenContextInterceptor)
                .addPathPatterns("/**").excludePathPatterns(EXCLUDE_PATHS);
    }
}
```

*   **为什么要两个？**
    `SaInterceptor` 负责“安检”，没登录直接拦回去。而 `SaTokenContextInterceptor` 是我自定义的，它负责在安检通过后，把 Sa-Token 里的用户信息拿出来放到 `ThreadLocal` 里，这样我原来的业务代码里 `SessionContext.get()` 依然能正常工作，不用大改业务逻辑。
*   **为什么要配置两遍路径？**
    Spring MVC 的拦截器是独立的。如果不配置排除路径，第二个拦截器可能会尝试在未登录状态下去获取用户信息，虽然不一定会报错，但逻辑上不严谨。提取一个 `EXCLUDE_PATHS` 常量是比较优雅的解法。

---

## 七、使用效果

现在，Controller 层的权限控制变得非常干净：

```java
// 必须具备 'super-admin' 角色
@SaCheckRole("super-admin")
@PostMapping("/admin/config")

// 必须具备 'user:add' 或 'user:update' 其中一个权限
@SaCheckPermission(value = {"user:add", "user:update"}, mode = SaMode.OR)
@PostMapping("/user/edit")

// 公开接口，忽略鉴权
@SaIgnore
@PostMapping("/public/api")
```

## 总结

这次升级主要是为了让鉴权层更标准化。Sa-Token 这种国产框架确实在易用性和功能丰富度上找到了很好的平衡。通过 `UUID + Redis` 的模式，我们既保证了高性能，又拥有了完整的会话控制能力，非常适合此类单体或集群架构的项目。
