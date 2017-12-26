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

@set DEPENDENCY_LIST=thunder-framework,thunder-test
@set PROJECT_NAME=thunder-spring-boot-docker-example
@set IMAGE_NAME=thunder-spring-boot
@set MACHINE_PORT=6010
@set CONTAINER_PORT=6010

if exist %PROJECT_NAME%\target rmdir /s/q %PROJECT_NAME%\target

@rem 执行相关模块的Maven Install
call mvn clean install -DskipTests -pl %DEPENDENCY_LIST% ,%PROJECT_NAME% -am

@rem 停止和删除Docker容器
call docker stop %IMAGE_NAME%
@rem call docker kill %IMAGE_NAME%
call docker rm %IMAGE_NAME%

@rem 删除Docker镜像
call docker rmi %IMAGE_NAME%

cd %PROJECT_NAME%

set DOCKER_HOST=tcp://localhost:2375
@rem set DOCKER_CERT_PATH=C:\Users\Neptune\.docker\machine\certs

@rem 安装Docker镜像
call mvn package docker:build -DskipTests

@rem 安装和启动Docker容器，并自动执行端口映射
call docker run -i -t -p %MACHINE_PORT%:%CONTAINER_PORT% --name %IMAGE_NAME% %IMAGE_NAME%:latest

pause