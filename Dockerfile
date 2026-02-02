# Multi-stage build para otimizar o tamanho da imagem

# Estágio 1: Build da aplicação
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copia apenas os arquivos de configuração do Maven primeiro (para cache)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Baixa as dependências (esta camada será cacheada)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila a aplicação
RUN ./mvnw clean package -DskipTests

# Estágio 2: Imagem de runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Cria usuário não-root para rodar a aplicação (segurança)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o JAR compilado do estágio anterior
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Configurações de JVM otimizadas
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Executa a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
