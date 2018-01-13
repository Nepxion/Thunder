# 注册Docker Hub账号

    1. Docker Hub注册账户，https://hub.docker.com/

## Win10 Docker部署
此方式最简单，强烈推荐，但该方式不支持Win7

### 部署前准备工作

    1 安装Docker
      1.1 在官网下载Docker，https://www.docker.com/community-edition
          在Download Docker Community Edition栏下，选择相关的操作系统下的Docker客户端进行下载
      1.2 安装Docker
      1.3 安装Kitematic

    2 登录Docker
      2.1 在Docker图标上右键菜单，点击进入Kitematic界面，并用Docker Hub账号登录登录(账户名在登录的时候必须是全部小写)

    3 加速Docker(可选)
      3.1 Docker默认从国外的Mirror站点下载镜像，如果您觉得下载速度很慢，可以把它改成从阿里云的Mirro站点
          在Docker图标上右键菜单，选Settings->Daemon，见图1
          在Insecure registries里填入registry.mirrors.aliyuncs.com，
          在Registry mirros里填入https://pee6w651.mirror.aliyuncs.com

    4 开启Docker非认证模式
      4.1 在Docker图标上右键菜单，选Settings->General，把“Expose daemon on tcp//localhost:2375 without TLS”打勾，见图2
      4.2 禁止Docker认证模式，例如，把“DOCKER_CERT_PATH=...”注释掉

    5 Win10防火墙设置(注意)
      5.1 Win10防火墙有时候会把Docker服务的可执行程序误认为木马病毒，需要把它设置为允许的项目，切记，见图7

### 开始部署

    1 修改配置
      1.1 启动Thunder时候，需要把服务提供端的IP地址注册到Zookeeper上，以宿主机IP代替Docker IP注册上去
          把根目录脚本install-docker.bat或者install-docker.sh中的MACHINE_IP=XXX，修改为你机器上Docker宿主机的IP，例如10.0.75.1
          运行ipconfig命令，你可以看到一个Docker的虚拟网卡(DockerNAT)，其显示的IP即为Docker宿主机的IP
          相关环境变量介绍
          DOCKER_HOST=tcp://localhost:2375                        // Docker服务器的URL
          DOCKER_CERT_PATH=C:\Users\Neptune\.docker\machine\certs // 认证模式下的证书存放路径
          IMAGE_NAME=thunder-spring-boot                          // 镜像名称
          MACHINE_IP=10.0.75.1                                    // 容器宿主机IP，即Thunder要注册到Zookeeper的IP
          MACHINE_PORT=6010                                       // 容器宿主机端口，即Thunder要注册到Zookeeper的端口，同时也是映射到Docker容器的主机端口
          CONTAINER_PORT=6010                                     // 容器端口，即暴露端口，同时也是被容器宿主机映射的端口
      1.2 容器里服务要访问容器外服务，需要给容器里服务指定外部的IP地址(这里以Zookeeper为例)
          把thunder-spring-boot-docker-example\src\main\resources\thunder-ext.properties中Zookeeper地址改成对应你本地网卡的IP
          运行ipconfig命令，可以获取本地网卡的IP

    2 安装镜像和容器
      2.1 在根目录下执行脚本install-docker.bat或者install-docker.sh，一键部署镜像和容器。等待一段时间后，交互型容器创建并启动成功

    3 验证镜像和容器
      3.1 验证镜像和容器是否安装成功
          点击镜像(My Images)列表的Tab，刷新镜像和容器列表，出现thunder-spring-boot镜像和容器，两者安装成功，见图3
      3.2 验证容器里的服务是否被外部访问
          在IDE里运行thunder-spring-boot-docker-example\...\NettyClientCommandLineApplication.java，执行容器外的应用访问容器内的应用
          观察Docker控制台，如果有中文输出，表示Docker内部服务可以被外界访问了，一切成功！见图6

    4 更新镜像和容器
      4.1 以后更新不需要把上述所有步骤重新做一下，只需要把”5 安装镜像和容器“中的步骤执行一遍即可，达到一键部署目的

    5 部署容器到阿里云
      5.1 代码下载到阿里云，然后执行上述步骤即可(运行脚本install-docker.sh)

    6 推送镜像到阿里云
      6.1 本地推送镜像到阿里云，执行脚本install-image.bat或者install-image.sh，见图8

## 附录：Kitematic操作

    1 安装容器
      1.1 命令行方式(推荐)
          1.1.1 在Window Dos窗口或者DOCKER CLI窗口执行下面语句，多个端口映射请用多个-p隔开，千万不要在前面加localhost，否则映射不成功
                docker run -d -p 6010:6010 --name thunder-spring-boot thunder-spring-boot:latest
        1.2 界面方式
            5.2.1 在镜像(My Images)列表点“CREATE”，在容器(Containers)列表中将出现它，并且将自动启动，见图4
            5.2.2 容器(Containers)列表上面，点“NEW”，返回上级界面，继续安装其它镜像
            5.2.3 在所选容器的的Settings->Hostname/Ports界面，添加端口映射宿主机端口6010映射到容器端口6010(即Thunder的启动端口)，点击SAVE让端口映射生效，容器将自动重启，见图5

    2 启动和停止容器
      2.1 点“START”，启动容器
      2.2 点“STOP”，停止容器
      2.3 点“RESTART”，重启容器

    3 删除镜像
      3.1 在镜像(My Images)列表，点“。。。”的按钮，然后再点“DELETE TAG”，即可删除镜像

    4 删除容器
      4.1 在容器(Containers)列表，把鼠标移上去，会出现“X”的按钮，点击它，即可删除容器

图1
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/doc/Docker1.jpg)

图2
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/doc/Docker2.jpg)

图3
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/doc/Docker3.jpg)

图4
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/doc/Docker4.jpg)

图5
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/doc/Docker5.jpg)

图6
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/doc/Docker6.jpg)

图7
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/doc/Win10.jpg)

图8
![Alt text](https://github.com/Nepxion/Thunder/blob/master/thunder-spring-boot-docker-example/doc/Aliyun.jpg)

## 附录：Docker命令
注意：镜像版本的参数可能不是必需的

    1.1 创建容器
        docker create [镜像名]:[镜像版本] (创建容器)
        docker run -d -p [IP地址]:[宿主机端口]:[容器端口] --name [容器名] [镜像名]:[镜像版本] (创建后台型容器，运行在后台，创建后与终端无关，只有调用docker stop、docker kill命令才能使容器停止)
        docker run -i -t --rm -p [IP地址]:[宿主机端口]:[容器端口] --name [容器名] [镜像名]:[镜像版本] (创建交互型容器，运行在前台，容器中使用exit命令或者调用docker stop、docker kill命令才能使容器停止)
        -[IP地址]:如果Docker装在本地，千万不要在前面加localhost，否则映射不成功
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
        docker port [容器名] (查看容器端口映射)
        docker inspect [容器名]或者[容器ID] (查看容器的系统变量)
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