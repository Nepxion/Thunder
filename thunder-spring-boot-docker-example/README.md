# 注册Docker Hub账号
1. Docker Hub注册账户，https://hub.docker.com/

2. Docker Hub账号登录，账户名在登录的时候必须是全部小写

# 安装和部署Docker
1. Win10环境下(此方式最简单，强烈推荐，但不支持Win7)
   1.1 安装Docker
       1)在Docker官网下载并安装Docker
       2)安装Kitematic

   1.2 开启非TLS访问模式
       1)在Docker Settings中把“Expose daemon on tcp//localhost:2375 without TLS”打勾
       2)在docker.bat中把“set DOCKER_CERT_PATH=...”注释掉

   1.3 修改配置
       把thunder-spring-boot-docker-example\src\main\resources下的thunder-ext.properties中Zookeeper地址改成对应你本地的真实IP
       因为部署到Docker后，Docker中localhost并非对应Zookeeper的localhost地址

   1.4 部署Docker
       1)在thunder-spring-boot-docker-example上级目录下执行install.bat里的语句
       2)在thunder-spring-boot-docker-example目录下执行docker.bat里的语句(切记mvn package，别加clean)

   1.5 启动Docker
       在Kitematic界面上进行操作
       1)点“My Images”，在thunder-spring-boot-docker-example镜像上，点“CREATE”，在左边容器(Containers)列表中将出现它
       2)点“START”，“STOP”，“RESTART”等，可以进行“启动”，“停止”，“重启”该应用的操作
       3)左边容器(Containers)列表上面，点“NEW”，返回上级界面

2. Win7环境下(此方式较复杂)
   请参考http://nepxion.iteye.com/blog/2321596
   因为thunder-spring-boot-docker-example模块已经重构过，故有些地方有出入，例如类名等，相信你能看得懂