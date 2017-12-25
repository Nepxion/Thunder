# 注册Docker Hub账号

    1. Docker Hub注册账户，https://hub.docker.com/

## Win10 Docker和Kitematic基本操作
此方式最简单，强烈推荐，但该方式不支持Win7，请注意带“重要”字样的步骤，哪位同学还有更方便的一键部署方式，请告知

    1 安装Docker
      1.1 在官网下载Docker
      1.2 安装Docker
      1.3 安装Kitematic

    2 开启非TLS访问模式
      2.1 重要：在Docker图标上右键菜单，选Settings，进入设置界面，把“Expose daemon on tcp//localhost:2375 without TLS”打勾，见图1
      2.2 禁止Docker认证模式，例如，把“set DOCKER_CERT_PATH=...”注释掉

    3 修改配置
      3.1 重要：把thunder-spring-boot-docker-example\src\main\docker\dockfile中-DThunderHost=XXX，修改为你机器上Docker宿主机的IP，默认为10.0.75.1
          运行ipconfig命令，你可以看到一个Docker的虚拟网卡(DockerNAT)，其显示的IP即为Docker宿主机的IP
      3.2 重要：把thunder-spring-boot-docker-example\src\main\resources\thunder-ext.properties中Zookeeper地址改成对应你本地网卡的IP
          运行ipconfig命令，可以获取本地网卡的IP

    4 安装镜像
       4.1 重要：在根目录下执行install-docker.bat里的语句。等待一段时间后，镜像制作上传成功，在最后的窗口直接模拟运行Spring Boot的可执行包，是为了验证镜像是否制作正确
       4.2 在Docker图标上右键菜单，点击进入Kitematic界面，并用Docker Hub账号登录登录(账户名在登录的时候必须是全部小写)
       4.3 点击镜像(My Images)列表，出现thunder-spring-boot-docker-example镜像，则表示镜像安装成功。如果看不到，则可再次点击镜像(My Images)列表即可刷新最新镜像列表，见图2

    5 安装容器
      5.1 命令行方式(推荐)
          5.1.1 重要：在DOCKER CLI窗口执行下面语句(任选一条)，多个端口映射请用多个-p隔开，见图5
                docker run -d -p 127.0.0.1:6010:6010 thunder-spring-boot-docker-example
                docker run -it --rm -p 127.0.0.1:6010:6010 thunder-spring-boot-docker-example
          5.1.2 重要：在所选容器的的Settings->Hostname/Ports界面，看到端口已经映射好了，点击SAVE(很重要)让端口映射生效，并重启容器

        5.2 界面方式
            5.2.1 在镜像(My Images)列表点“CREATE”，在容器(Containers)列表中将出现它，并且将自动启动，见图3
            5.2.2 容器(Containers)列表上面，点“NEW”，返回上级界面，继续安装其它镜像
            5.2.3 在所选容器的的Settings->Hostname/Ports界面，端口映射宿主机端口6010映射到容器端口6010(即Thunder的启动端口)，并点击SAVE(很重要)并重启容器，见图4

    6 启动和停止容器
      6.1 点“START”，启动容器，见图3
      6.2 点“STOP”，停止容器
      6.3 点“RESTART”，重启容器

    7 删除镜像
      7.1 在镜像(My Images)列表，点“。。。”的按钮，然后再点“DELETE TAG”，即可删除镜像，见图2

    8 删除容器
      8.1 在容器(Containers)列表，把鼠标移上去，会出现“X”的按钮，点击它，即可删除容器，见图3

    9 验证容器
      9.1 运行Zookeeper
      9.2 在IDE里运行thunder-spring-boot-docker-example\...\NettyClientCommandLineApplication.java
      9.3 观察Docker控制台，如果有中文输出，表示Docker内部服务可以被外界访问了，一切成功！见图6

    10 更新镜像和容器
       以后镜像不需要把上述所有步骤重新做一下，只需要执行如下四步即可
       10.1 停止原先运行的容器(最好删除容器以及关联的镜像)
       10.2 在根目录下执行install-docker.bat里的语句
       10.3 docker run -d -p 127.0.0.1:6010:6010 thunder-spring-boot-docker-example
       10.4 在所选容器的的Settings->Hostname/Ports界面，端口映射宿主机端口6010映射到容器端口6010(即Thunder的启动端口)，并点击SAVE(很重要)并重启容器

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

## Win7 Docker和Kitematic基本操作
此方式较复杂

   1. 请参考http://nepxion.iteye.com/blog/2321596
   2. 因为thunder-spring-boot-docker-example模块已经重构过，故有些地方有出入，例如类名等，相信你能看得懂