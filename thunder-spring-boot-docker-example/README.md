在非TLS模式下工作
1. 在Docker Settings中把“Expose daemon on tcp//localhost:2375 without TLS”打勾
2. 在docker.bat中把“set DOCKER_CERT_PATH=...”注释掉

执行方式
1. 在thunder-spring-boot-docker-example上级目录下执行install.bat里的语句
2. 在thunder-spring-boot-docker-example目录下执行docker.bat里的语句(切记mvn package，别加clean)