FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/invoice-unpack*.jar invoice-unpack.jar
CMD java ${JAVA_OPTS} -jar invoice-unpack.jar