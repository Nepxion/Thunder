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

@set PROJECT_NAME=thunder-spring-boot-docker-example
@set IMAGE_NAME=thunder-spring-boot

@rem 删除target，有时候mvn会clean失败，需事先强制删除target
if exist %PROJECT_NAME%\target rmdir /s/q %PROJECT_NAME%\target

@rem 执行相关模块的install
call mvn clean install -DskipTests -pl thunder-framework,thunder-test,%PROJECT_NAME% -am

@rem 停止和删除容器
call docker stop %IMAGE_NAME%
@rem call docker kill %IMAGE_NAME%
call docker rm %IMAGE_NAME%

@rem 删除镜像
call docker rmi %IMAGE_NAME%

cd %PROJECT_NAME%

@rem 安装容器镜像
set DOCKER_HOST=tcp://localhost:2375
@rem set DOCKER_HOST=tcp://192.168.99.100:2376
@rem set DOCKER_CERT_PATH=C:\Users\Neptune\.docker\machine\certs

call mvn package docker:build -DskipTests
@rem call mvn package docker:build -DskipTests && java -jar target\%PROJECT_NAME%-1.0.0.jar

call docker run -i -t -p 127.0.0.1:6010:6010 --name %IMAGE_NAME% %IMAGE_NAME%:latest

pause