# 注册Docker Hub账号
1. Docker Hub注册账户，https://hub.docker.com/

# 安装和部署Docker
1. Win10环境下(此方式最简单，强烈推荐，但该方式不支持Win7)
   1.1 安装Docker
       1)在官网下载Docker
       2)安装Docker
       3)安装Kitematic

   1.2 开启非TLS访问模式
       1)在Kitematic的Docker Settings中把“Expose daemon on tcp//localhost:2375 without TLS”打勾
       2)禁止Docker认证，例如，把“set DOCKER_CERT_PATH=...”注释掉

   1.3 修改配置
       1)把thunder-spring-boot-docker-example\src\main\resources下的thunder-ext.properties中Zookeeper地址改成对应你本地的真实IP
         因为部署到Docker后，Docker中localhost并非对应Zookeeper的localhost地址

   1.4 安装镜像
       1)在根目录下执行install-docker.bat里的语句
       2)在Docker图标上右键菜单，点击进入Kitematic界面，并用Docker Hub账号登录登录(账户名在登录的时候必须是全部小写)
       3)点击镜像(My Images)列表，出现thunder-spring-boot-docker-example镜像，则表示镜像安装成功。如果看不到，则可再次点击镜像(My Images)列表即可刷新最新镜像列表

   1.5 安装容器
       1)在镜像(My Images)列表点“CREATE”，在容器(Containers)列表中将出现它
       2)容器(Containers)列表上面，点“NEW”，返回上级界面，继续安装其它镜像

   1.6 启动和停止容器
       1)点“START”，启动容器
       2)点“STOP”，停止容器
       3)点“RESTART”，重启容器

   1.7 删除镜像
       1)在镜像(My Images)列表，点“。。。”的按钮，然后再点“DELETE TAG”，即可删除镜像

   1.8 删除容器
       1)在容器(Containers)列表，把鼠标移上去，会出现“X”的按钮，点击它，即可删除容器

2. Win7环境下(此方式较复杂)
   请参考http://nepxion.iteye.com/blog/2321596

   因为thunder-spring-boot-docker-example模块已经重构过，故有些地方有出入，例如类名等，相信你能看得懂