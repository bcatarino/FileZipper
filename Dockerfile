FROM adoptopenjdk/openjdk11
ARG JAR_FILE=target/FileZipper*.jar
COPY ${JAR_FILE} FileZipper.jar
ENTRYPOINT ["java","-jar","/FileZipper.jar"]