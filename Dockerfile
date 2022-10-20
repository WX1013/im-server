FROM openjdk:8-jre
ENV LANG C.UTF-8
ENV LANG zh_CN.UTF-8
ENV LC_ALL zh_CN.UTF-8
ENV TZ=Asia/Shanghai
EXPOSE 8000 8999
COPY target/im-server-1.0.0.jar /app.jar
ENTRYPOINT ["java", "-Xmx512m", "-Djava.security.egd=file:/dev/./urandom", "-Ddruid.mysql.usePingMethod=false", "-jar", "/app.jar"]
