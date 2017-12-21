FROM java
ADD /thunder-spring-boot-docker-example-1.0.0.jar //

EXPOSE 8888

CMD ["./run.sh"]

ENTRYPOINT ["java", "-jar", "-DThunderHost=10.0.75.1", "/thunder-spring-boot-docker-example-1.0.0.jar"]