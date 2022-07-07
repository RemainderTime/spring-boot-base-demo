# java8运行环境
FROM openjdk:8-jdk-alpine
# 作者名称
MAINTAINER xiongfeng

# 切换工作目录
WORKDIR /root/java

# 添加demo-start-1.0.0.jar文件到docker环境内
#ADD xf-boot-base-1.0.1.jar /root/java/xf-boot-base-1.0.1.jar
COPY target/*.jar app.jar
# 暴露端口8080
EXPOSE 8080
# 运行命令
ENTRYPOINT ["java", "-server", "-Xms512m", "-Xmx512m", "-jar", "/root/java/xf-boot-base-1.0.1.jar"]