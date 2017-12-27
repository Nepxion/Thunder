FROM java

ENV JAR_NAME=thunder-spring-boot-docker-example-1.0.0.jar
ENV EXPOSE_HOST=10.0.75.1
ENV EXPOSE_PORT=6666

ADD /${JAR_NAME} //

EXPOSE ${EXPOSE_PORT}

CMD ["./run.sh"]

ENTRYPOINT java -jar -DThunderHost=${EXPOSE_HOST} -DThunderPort=${EXPOSE_PORT} /${JAR_NAME}