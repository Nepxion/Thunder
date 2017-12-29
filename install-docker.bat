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

@set PROJECT_NAME=thunder-spring-boot-docker-example
@set PROJECT_LIST=thunder-framework,thunder-test,%PROJECT_NAME%

@set DOCKER_HOST=tcp://localhost:2375
@rem @set DOCKER_CERT_PATH=C:\Users\Neptune\.docker\machine\certs
@set IMAGE_NAME=thunder-spring-boot
@set MACHINE_IP=10.0.75.1
@set MACHINE_PORT=6010
@set CONTAINER_PORT=6010

if exist %PROJECT_NAME%\target rmdir /s/q %PROJECT_NAME%\target

@rem 执行相关模块的Maven Install
call mvn clean install -DskipTests -pl %PROJECT_LIST% -am

@rem 停止和删除Docker容器
call docker stop %IMAGE_NAME%
@rem call docker kill %IMAGE_NAME%
call docker rm %IMAGE_NAME%

@rem 删除Docker镜像
call docker rmi %IMAGE_NAME%

cd %PROJECT_NAME%

@rem 安装Docker镜像
call mvn package docker:build -DskipTests -DImageName=%IMAGE_NAME% -DExposePort=%CONTAINER_PORT% -DThunderHost=%MACHINE_IP% -DThunderPort=%MACHINE_PORT%

@rem 安装和启动Docker容器，并自动执行端口映射
call docker run -i -t -p %MACHINE_PORT%:%CONTAINER_PORT% --name %IMAGE_NAME% %IMAGE_NAME%:latest

pause