FROM ghcr.io/graalvm/native-image-community:17 AS build
WORKDIR /build

RUN microdnf install -y maven

COPY pom.xml .
COPY domain/pom.xml domain/pom.xml
COPY springframework/pom.xml springframework/pom.xml
COPY domain/src domain/src
COPY springframework/src springframework/src

# 1. builda e instala o domain no repositório local primeiro
RUN mvn -pl domain -am clean install -DskipTests

# 2. agora springframework encontra o domain normalmente
RUN mvn -pl springframework -am -Pnative native:compile -DskipTests

FROM debian:bookworm-slim
WORKDIR /app
EXPOSE 8082
COPY --from=build /build/springframework/target/springframework /app/app
ENTRYPOINT ["/app/app"]