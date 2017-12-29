echo 'on'
echo '============================================================='
echo '$                                                           $'
echo '$                      Nepxion Thunder                      $'
echo '$                                                           $'
echo '$                                                           $'
echo '$                                                           $'
echo '$  Nepxion Technologies All Right Reserved                  $'
echo '$  Copyright(C) 2017                                        $'
echo '$                                                           $'
echo '============================================================='
echo '.'
echo 'off'

title=Nepxion Thunder
color=0a

PROJECT_NAME=thunder-spring-boot-docker-example
PROJECT_LIST=thunder-framework,thunder-test,${PROJECT_NAME}

DOCKER_HOST=tcp://localhost:2375
# DOCKER_CERT_PATH=/User/Neptune/.docker/machine/certs
IMAGE_NAME=thunder-spring-boot
MACHINE_IP=10.0.75.1
MACHINE_PORT=6010
CONTAINER_PORT=6010

if [ ! -d ${PROJECT_NAME}/target];then
rmdir /s/q ${PROJECT_NAME}/target
fi

# 执行相关模块的Maven Install
mvn clean install -DskipTests -pl ${PROJECT_LIST} -am

# 停止和删除Docker容器
docker stop ${IMAGE_NAME}
# docker kill ${IMAGE_NAME}
docker rm ${IMAGE_NAME}

# 删除Docker镜像
docker rmi ${IMAGE_NAME}

cd ${PROJECT_NAME}

# 安装Docker镜像
mvn package docker:build -DskipTests -DImageName=${IMAGE_NAME} -DExposePort=${CONTAINER_PORT} -DThunderHost=${MACHINE_IP} -DThunderPort=${MACHINE_PORT}

# 安装和启动Docker容器，并自动执行端口映射
docker run -i -t -p ${MACHINE_PORT}:${CONTAINER_PORT} --name ${IMAGE_NAME} ${IMAGE_NAME}:latest