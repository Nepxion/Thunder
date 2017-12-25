# 注册Docker Hub账号

    1. Docker Hub注册账户，https://hub.docker.com/

## Win10 Docker部署
此方式最简单，强烈推荐，但该方式不支持Win7

    1 安装Docker
      1.1 在官网下载Docker
      1.2 安装Docker
      1.3 安装Kitematic

    2 登录Docker
      2.1 在Docker图标上右键菜单，点击进入Kitematic界面，并用Docker Hub账号登录登录(账户名在登录的时候必须是全部小写)

    3 开启Docker非认证模式
      3.1 在Docker图标上右键菜单，选Settings，进入设置界面，把“Expose daemon on tcp//localhost:2375 without TLS”打勾，见图1
      3.2 禁止Docker认证模式，例如，把“set DOCKER_CERT_PATH=...”注释掉，切记！

    4 修改配置
      4.1 把thunder-spring-boot-docker-example\src\main\docker\dockfile中-DThunderHost=XXX，修改为你机器上Docker宿主机的IP，例如10.0.75.1
          运行ipconfig命令，你可以看到一个Docker的虚拟网卡(DockerNAT)，其显示的IP即为Docker宿主机的IP
      4.2 把thunder-spring-boot-docker-example\src\main\resources\thunder-ext.properties中Zookeeper地址改成对应你本地网卡的IP
          运行ipconfig命令，可以获取本地网卡的IP

    5 安装镜像和容器
       5.1 在根目录下执行install-docker.bat里的语句(您可以打开看看，里面包含的命令)，一键创建镜像和容器。等待一段时间后，在当前Dos窗口直接模拟运行Spring Boot的可执行包，是为了验证镜像是否制作正确
       5.2 点击镜像(My Images)列表，出现thunder-spring-boot-docker-example镜像，则表示镜像安装成功。如果看不到，则可再次点击镜像(My Images)列表即可刷新最新镜像列表，见图2
       5.3 在所选容器的的Settings->Hostname/Ports界面，端口已经自动映射好了，点击SAVE(很重要)让端口映射生效，容器将自动重启

    6 验证容器
      6.1 运行Zookeeper
      6.2 在IDE里运行thunder-spring-boot-docker-example\...\NettyClientCommandLineApplication.java
      6.3 观察Docker控制台，如果有中文输出，表示Docker内部服务可以被外界访问了，一切成功！见图6

    6 更新镜像和容器
      以后镜像不需要把上述所有步骤重新做一下，只需要执行如下三步即可
      6.1 停止原先运行的容器(最好删除容器以及关联的镜像)
      6.2 执行4.1步骤
      6.3 执行5.1步骤

## 附录Kitematic操作

    1 安装容器
      1.1 命令行方式(推荐)
          1.1.1 在DOCKER CLI窗口执行下面语句，多个端口映射请用多个-p隔开，见图5
                docker run -d -p 127.0.0.1:6010:6010 --name thunder-spring-boot thunder-spring-boot:latest
          1.1.2 在所选容器的的Settings->Hostname/Ports界面，端口已经自动映射好了，点击SAVE(很重要)让端口映射生效，容器将自动重启

        1.2 界面方式
            5.2.1 在镜像(My Images)列表点“CREATE”，在容器(Containers)列表中将出现它，并且将自动启动，见图3
            5.2.2 容器(Containers)列表上面，点“NEW”，返回上级界面，继续安装其它镜像
            5.2.3 在所选容器的的Settings->Hostname/Ports界面，添加端口映射宿主机端口6010映射到容器端口6010(即Thunder的启动端口)，点击SAVE(很重要)让端口映射生效，容器将自动重启，见图4

    2 启动和停止容器
      2.1 点“START”，启动容器，见图3
      2.2 点“STOP”，停止容器
      2.3 点“RESTART”，重启容器

    3 删除镜像
      3.1 在镜像(My Images)列表，点“。。。”的按钮，然后再点“DELETE TAG”，即可删除镜像，见图2

    4 删除容器
      4.1 在容器(Containers)列表，把鼠标移上去，会出现“X”的按钮，点击它，即可删除容器，见图3 

图1
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/Docker1.jpg)

图2
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/Docker2.jpg)

图3
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/Docker3.jpg)

图4
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/Docker4.jpg)

图5
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/Docker5.jpg)

图6
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/Docker6.jpg)

## 附录附录Docker命令
注意：镜像版本的参数可能不是必需的

    1.1 创建容器
        docker create [镜像名]:[镜像版本] (创建容器)
        docker run -d -p [IP地址]:[宿主机端口]:[容器端口] --name [容器名] [镜像名]:[镜像版本] (创建后台型容器，运行在后台，创建后与终端无关，只有调用docker stop、docker kill命令才能使容器停止)
        docker run -i -t --rm -p [IP地址]:[宿主机端口]:[容器端口] --name [容器名] [镜像名]:[镜像版本] (创建交互型容器，运行在前台，容器中使用exit命令或者调用docker stop、docker kill命令，容器停止)
        -it:
        -i:标准输入给容器 
        -t:分配一个虚拟终端
        -d:以守护进程方式运行，容器在后台运行
        -p:端口映射
        --name:指定容器名
        --rm:退出时删除容器
    1.2 启动容器
        docker start [容器名]或者[容器ID] (启动容器)
        docker ----restart=always [容器名]或者[容器ID] (重启容器，不管发生什么错误始终重启) 
        docker ----restart=on-failure:[次数] [容器名]或者[容器ID] (重启容器，发生错误后重试x次) 
    1.3 停止容器
        docker stop [容器名]或者[容器ID] (停止容器)
        docker kill [容器名]或者[容器ID] (强制停止容器)
    1.4 删除容器
        docker rm [容器名]或者[容器ID] (删除容器)
    1.5 重命名容器
        docker rename [旧名称] [新名称] (重命名一个容器)
    1.6 查看容器
        docker ps (查看当前运行的容器)
        docker ps -a (查看所有容器，包括停止的容器)
        docker ps -l (查看最新创建的容器，只列出最后创建的容器)
        docker ps -n=x (查看最新创建的x个容器，-n=x选项，会列出最后创建的x个容器)
        docker top [容器名] (查看容器内部的进程)
        docker exec (在容器中运行一个进程)
        docker logs [容器名] (查看容器操作日志)
    1.7 创建镜像
        docker commit [镜像名]:[镜像版本] (创建镜像)
    1.8 获取镜像
        docker pull [镜像名]:[镜像版本] (获取镜像)
    1.9 搜索镜像
        docker search [镜像名]:[镜像版本] (获取镜像)
    1.10 查看镜像
        docker images (查看镜像列表)
        docker history [镜像名]:[镜像版本] (查看镜像的历史)
    1.11 删除镜像
        docker rmi [镜像名]:[镜像版本] (删除镜像)
    1.12 载入镜像
        docker load --input xxx.tar (导入镜像以及其相关的元数据信息，包括标签等)
    1.13 更多命令参考
        http://www.runoob.com/docker/docker-command-manual.html

## Win7 Docker部署
此方式较复杂

    1. 请参考http://nepxion.iteye.com/blog/2321596
    2. 因为thunder-spring-boot-docker-example模块已经重构过，故有些地方有出入，例如类名等，相信你能看得懂