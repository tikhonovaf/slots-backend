FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=target/slotsbe.jar
WORKDIR /opt/app
COPY ${JAR_FILE} slotsbe.jar
#ENTRYPOINT ["java","-jar","slotsbe.jar"]
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar slotsbe.jar"]