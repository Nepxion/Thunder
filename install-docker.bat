@echo on
@echo =============================================================
@echo $                                                           $
@echo $                      Nepxion Thunder                      $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Technologies All Right Reserved                  $
@echo $  Copyright(C) 2017                                        $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion Thunder
@color 0a

@echo Please ensure to config JAVA_HOME with Java 7
@set JAVA_HOME=E:\Tool\JDK-1.7.0
@echo Found JAVA_HOME=%JAVA_HOME%

call mvn clean install -DskipTests -pl thunder-framework,thunder-test,thunder-spring-boot-docker-example -am

cd thunder-spring-boot-docker-example

set DOCKER_HOST=tcp://localhost:2375
@rem set DOCKER_HOST=tcp://192.168.99.100:2376
@rem set DOCKER_CERT_PATH=C:\Users\Neptune\.docker\machine\certs

call mvn package docker:build -DskipTests
@rem call mvn package docker:build -DskipTests && java -jar target/thunder-spring-boot-docker-example-1.0.0.jar

pause