FROM java

ENV JAR_NAME=thunder-spring-boot-docker-example-1.0.0.jar
ENV THUNDER_HOST=10.0.75.1
ENV THUNDER_PORT=6010

ADD /${JAR_NAME} //

EXPOSE 8888

CMD ["./run.sh"]

ENTRYPOINT java -jar -DThunderHost=${THUNDER_HOST} -DThunderPort=${THUNDER_PORT} /${JAR_NAME}