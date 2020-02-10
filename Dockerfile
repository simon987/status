FROM ubuntu:19.10 as build

RUN apt update && apt install -y curl gnupg openjdk-13-jdk
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list &&\
     curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add &&\
      apt update && apt install -y sbt

WORKDIR /build/
COPY . /build
RUN sbt assembly

FROM openjdk
WORKDIR /app/
COPY --from=build /build/target/scala-2.13/status-assembly-0.1.jar /app/
COPY --from=build /build/src/main/webapp /app/src/main/webapp

RUN touch status.mv.db
ADD config-template.yml config.yml
ENTRYPOINT ["/usr/bin/java", "-jar", "status-assembly-0.1.jar"]
EXPOSE 8080