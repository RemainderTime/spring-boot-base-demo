# 使用 JDK 17 轻量级运行环境
FROM eclipse-temurin:17-jre-alpine

# 作者信息
LABEL maintainer="xiongfeng"

# 切换工作目录
WORKDIR /root/java

# 将编译好的 jar 包复制到容器中，避免硬编码包名版本号
COPY target/*.jar app.jar

# 暴露端口 8089 (对齐 application.yml)
EXPOSE 8089

# 运行命令，加入垃圾回收和内存优化参数，以及时区与字符编码设置
ENTRYPOINT ["java", "-server", "-Xms512m", "-Xmx512m", "-Dfile.encoding=UTF-8", "-Duser.timezone=Asia/Shanghai", "-jar", "app.jar"]
