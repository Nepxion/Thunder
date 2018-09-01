# 介绍
Hessian在Web容器(例如Tomcat)里的部署

    1. 注意：hessian-server-context.xml里的port端口号必须和Web容器运行的端口号一致
    2. 在根目录下执行install-tomcat.bat里的语句
    3. 把thunder-test\thunder-test-[version]-release.war拷贝到Web容器的webapp目录下，并重命名成thunder.war
    4. 注意：在Web容器的webapp\WEB-INF\thunder目录(自动解压后)下，有若干个web.xml，您像启动哪种类型的服务，更名成web.xml，默认的web.xml指向hessian服务