FROM openjdk:8-jre-alpine3.9
COPY target/ftp-client-1.0.jar ./client.jar
CMD ["java", "-jar", "client.jar", "172.20.0.1", "5056"]