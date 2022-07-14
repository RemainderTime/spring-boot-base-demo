# java8运行环境
FROM openjdk:8-jdk-alpine
# 作者名称
MAINTAINER xiongfeng

# 切换工作目录
WORKDIR /root/java

#1. coding自动化部署
COPY target/*.jar app.jar
# 暴露端口8080
#EXPOSE 8080
# 运行命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/urandom","-Dfile.encoding=UTF-8","-Duser.timezone=Asia/Shanghai","-XX:MaxDirectMemorySize=1024m","-XX:MetaspaceSize=256m","-XX:MaxMetaspaceSize=512m","-XX:MaxRAMPercentage=80.0","-jar","app.jar"]

#2. 手动部署项目到docker环境中
# 添加demo-start-1.0.0.jar文件到docker环境内
#ADD xf-boot-base-1.0.1.jar /root/java/xf-boot-base-1.0.1.jar
## 暴露端口8080
#EXPOSE 8080
## 运行命令
#ENTRYPOINT ["java", "-server", "-Xms512m", "-Xmx512m", "-jar", "/root/java/xf-boot-base-1.0.1.jar"]
