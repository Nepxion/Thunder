set DOCKER_HOST=tcp://192.168.99.100:2376

set DOCKER_CERT_PATH=C:\Users\Neptune\.docker\machine\certs

call mvn package && java -jar target/nepxion-thunder-spring-boot-docker-1.0.0.jar

call mvn clean package docker:build