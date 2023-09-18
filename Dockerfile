FROM amazoncorretto:17-alpine
COPY ./target/hello-app-1.0.jar hello-app-1.0.jar
EXPOSE 8888

RUN apk add curl

RUN curl -L https://github.com/aws-observability/aws-otel-java-instrumentation/releases/download/v1.28.1/aws-opentelemetry-agent.jar --output opentelemetry-javaagent-all.jar

ENTRYPOINT [ "java", "-javaagent:opentelemetry-javaagent-all.jar", "-jar", "/hello-app-1.0.jar" ]