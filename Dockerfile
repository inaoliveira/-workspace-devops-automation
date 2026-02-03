# Estágio 1: Build da aplicação
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copia apenas o pom.xml primeiro
COPY pom.xml .

# Baixa as dependências usando o mvn da imagem (não precisa de mvnw)
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila a aplicação usando mvn
RUN mvn clean package -DskipTests

# Estágio 2: Imagem de runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Segurança: Usuário não-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o JAR do estágio builder
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]