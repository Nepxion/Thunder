FROM java

ENV JAR_NAME=thunder-spring-boot-docker-example-1.0.0.jar
ENV EXPOSE_PORT=6010
ENV THUNDER_HOST=10.0.75.1

ADD /${JAR_NAME} //

EXPOSE ${EXPOSE_PORT}

CMD ["./run.sh"]

ENTRYPOINT java -jar -DThunderHost=${THUNDER_HOST} /${JAR_NAME}