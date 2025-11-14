FROM maven:3.9-amazoncorretto-21 AS builder 

WORKDIR /springApp 

COPY pom.xml . 
COPY src ./src

RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests 

FROM amazoncorretto:21

WORKDIR /springApp 

COPY --from=builder /springApp/target/*.jar springApp.jar 

EXPOSE 8080 

ENTRYPOINT ["java", "-jar", "springApp.jar"]
