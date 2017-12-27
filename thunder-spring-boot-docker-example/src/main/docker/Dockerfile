FROM java

ENV THUNDER_JAR=thunder-spring-boot-docker-example-1.0.0.jar
ENV THUNDER_HOST=10.0.75.1
ENV THUNDER_PORT=6010

ADD /${THUNDER_JAR} //

EXPOSE 8888

CMD ["./run.sh"]

ENTRYPOINT java -jar -DThunderHost=${THUNDER_HOST} -DThunderPort=${THUNDER_PORT} /${THUNDER_JAR}