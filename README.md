# Nepxion Thunder
![Total visits](https://komarev.com/ghpvc/?username=Nepxion&label=total%20visits&color=blue)  [![Total lines](https://tokei.rs/b1/github/Nepxion/Thunder?category=lines)](https://tokei.rs/b1/github/Nepxion/Thunder?category=lines)  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/Nepxion/Thunder/blob/master/LICENSE)  [![Maven Central](https://img.shields.io/maven-central/v/com.nepxion/thunder.svg?label=maven%20central)](https://search.maven.org/artifact/com.nepxion/thunder)  [![Javadocs](http://www.javadoc.io/badge/com.nepxion/thunder-framework.svg)](http://www.javadoc.io/doc/com.nepxion/thunder-framework)  [![Build Status](https://travis-ci.org/Nepxion/Thunder.svg?branch=master)](https://travis-ci.org/Nepxion/Thunder)  [![Codacy Badge](https://api.codacy.com/project/badge/Grade/a2c3078fc1464fdf961c0c276200e3e4)](https://www.codacy.com/project/HaojunRen/Thunder/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Nepxion/Thunder&amp;utm_campaign=Badge_Grade_Dashboard)  [![Stars](https://img.shields.io/github/stars/Nepxion/Thunder.svg?label=Stars&tyle=flat&logo=GitHub)](https://github.com/Nepxion/Thunder/stargazers)  [![Stars](https://gitee.com/Nepxion/Thunder/badge/star.svg)](https://gitee.com/nepxion/Thunder/stargazers)

<a href="https://github.com/Nepxion" tppabs="#" target="_blank"><img width="25" height="25" src="http://nepxion.gitee.io/discovery/docs/icon-doc/github.png"></a>&nbsp;  <a href="https://gitee.com/Nepxion" tppabs="#" target="_blank"><img width="25" height="25" src="http://nepxion.gitee.io/discovery/docs/icon-doc/gitee.png"></a>&nbsp;  <a href="https://search.maven.org/search?q=g:com.nepxion" tppabs="#" target="_blank"><img width="25" height="25" src="http://nepxion.gitee.io/discovery/docs/icon-doc/maven.png"></a>&nbsp;  <a href="http://nepxion.gitee.io/discovery/docs/contact-doc/wechat.jpg" tppabs="#" target="_blank"><img width="25" height="25" src="http://nepxion.gitee.io/discovery/docs/icon-doc/wechat.png"></a>&nbsp;  <a href="http://nepxion.gitee.io/discovery/docs/contact-doc/dingding.jpg" tppabs="#" target="_blank"><img width="25" height="25" src="http://nepxion.gitee.io/discovery/docs/icon-doc/dingding.png"></a>&nbsp;  <a href="http://nepxion.gitee.io/discovery/docs/contact-doc/gongzhonghao.jpg" tppabs="#" target="_blank"><img width="25" height="25" src="http://nepxion.gitee.io/discovery/docs/icon-doc/gongzhonghao.png"></a>&nbsp;  <a href="mailto:1394997@qq.com" tppabs="#"><img width="25" height="25" src="http://nepxion.gitee.io/discovery/docs/icon-doc/email.png"></a>

Nepxion Thunder是一款基于Netty + Hessian + Kafka + ActiveMQ + Tibco + Zookeeper(Curator Framework) + Redis + FST + Spring + Spring Web MVC + Spring Boot + Docker分布式RPC调用框架。架构思想主要是来自阿里巴巴的Dubbo框架，但比它更轻量级，零配置式实现部署

技术博客：[http://nepxion.iteye.com/](http://nepxion.iteye.com/)

容器部署：[https://github.com/Nepxion/Thunder/tree/master/thunder-spring-boot-docker-example](https://github.com/Nepxion/Thunder/tree/master/thunder-spring-boot-docker-example)

## 简介
- 和Dubbo功能的比较

  跟Dubbo相比，Thunder的增强功能包括
  - 支持Message Queue（消息队列中间件)，Dubbo只支持Netty等内存堆积消息的方式，Thunder不仅支持内存堆积，也支持MQ的硬盘文件堆积
  - 支持本地链式调用，全程无阻塞的调用方式，可省去业务端写Callback的步骤
  - 支持跨进程的调用链，服务端和调用端都支持软负载均衡
  - 支持远程配置和调优，业务端所有集群下的应用可共享一个配置文件，并且通过远程配置方式修改，也支持业务系统自身已经存在的参数化平台的配置方式接入
  - 支持丰富的监控手段，默认支持Redis，Splunk，自定义第三方WebService做监控
  - 支付丰富的服务治理手段，图形化，上下线宕机邮件或者短信通知，异常事件自定义捕捉
  - 支持序列化在网络传输层面的压缩（QuickLz)，在大数据量传输，通过压缩，对提高吞吐量（TPS)尤其有效
  - 支持Spring的简化配置，支持全局配置和局部配置的结合
  - 支持对微服务框架的整合（例如：Spring Boot)

  跟Dubbo相比，Thunder的未支持功能包括
  - 软负载均衡的算法，Thunder支持三种（轮询，随机，一致性哈希)，Dubbo还支持最少活跃度，还支持预热和权重计算
  - 访问规则，Thunder支持限流，密钥匹配，版本匹配，Dubbo相对更丰富，例如黑白名单，服务降级，网段隔离
- Netty是由JBOSS提供的一个Java开源框架，提供异步的、事件驱动的网络应用程序框架和工具，它是基于TCP，UDP协议的传输方式的NIO框架。在Thunder，实现异步/同步/广播的调用方式，多线程实现调用
- Hessian是轻量级的Remoting HTTP框架，提供同步的调用方式。它是基于二进制RPC协议。在Thunder，实现异步/同步/广播的调用方式，多线程实现调用
- Kafka是一种高吞吐量的分布式发布订阅消息系统，非JMS标准，是MQ里面性能最优化的。在Thunder，实现异步/同步/广播的调用方式，多线程实现调用
- ActiveMQ是由Apache出品，最流行的，能力强劲的开源消息总线。它支持JMS1.1和J2EE 1.4规范的 JMS Provider，支持二进制协议(openwire，amqp)，文本协议(stomp)，物联网协议(mqtt)，WebSocket(ws)五种协议，Spring无缝整合它到框架里面。在Thunder，实现异步/同步/广播的调用方式，多线程实现调用
- TIBCO(NASDAQ:TIBX)是一家有着20年历史的老牌中间件公司，致力于EAI企业应用集成产品和解决方案的领域。在Thunder，实现异步/同步/广播的调用方式，多线程实现调用。由于是商业软件，不提供开发包，请到该公司主页获取免费版开发包
- Zookeeper是分布式应用程序协调服务，是Google的Chubby一个开源的实现，是Hadoop和HBase的重要组件。它是一个为分布式应用提供一致性服务的软件，提供的功能包括：配置维护、名字服务、分布式同步、组服务等。Thunder利用Apache Curator Framework的衍生组件，实现对Zookeeper的调用
- Redis是一个Key-Value存储系统，异常快速的数据持久化，支持丰富的数据类型，良好的操作原子性，多实用的工具，可以在多个用例如缓存，消息，队列使用(Redis原生支持发布/订阅)，任何短暂的数据，应用程序，如Web应用程序会话，网页命中计数。Thunder利用它做发布/订阅功能，该功能是对Netty和Hessian的增强
- FST(Fast Serialization)和Kryo是实现Java快速对象序列化的开发包。序列化速度更快(2-10倍)、体积更小，而且兼容 JDK 原生的序列化，使用者可以任选1个，推荐FST
- Faster Jackson和Alibaba fastjson是实现的Json对对象和字符串的高速转换。使用者可以任选1个，推荐Faster Jackson
- Spring是轻量级的Java开发框架。Thunder利用Spring AOP技术实现面向切面的动态代理，通过命名空间的自定义标签解析FactoryBean
- Spring Web MVC，Thunder利用它实现和Hessian的整合
- Apache Core，Thunder利用它的异步NIO实现服务治理的数据传送
- Ebay Jetstream，Thunder利用它实现Web版的服务治理
- Google Guava EventBus，Thunder利用它实现事件驱动发布框架内部事件，解除耦合；发布外部事件，进行重试补偿，异常通知(邮件或短信通知)
- Splunk或Redis，Thunder利用它实现日志云管理
- Nepxion Swing Repository，Thunder利用它实现Java Desktop版的服务治理
- Java SPI，Thunder利用它实现相关扩展
- Jdeferred Promise，Thunder利用它实现链式调用
- Docker是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的容器中，然后发布到任何流行的Linux机器上，也可以实现虚拟化。容器是完全使用沙箱机制，相互之间不会有任何接口，Thunder利用它做容器

## 功能
-  框架进程可以既是服务方，又是调用方，两者为一体，互为Server和Client模式，只要在注册中心注册，无论是客户端还是服务端都将是负载均衡的节点。基于上述设计，支持多级以上的调用链方式：纯异步多级调用，纯同步多级调用，先异步后同步多级调用，先同步后异步多级调用，异步多层Callback调用
- 支持TCP NIO框架(Netty)，TCP MQ消息队列框架(Kafka，ActiveMQ，Tibco)，HTTP(Hessian)的传输方式，支持多线程调用
- 支持同步调用，异步调用Callback，广播通知方式(点对点的发布订阅模式)，同步调用和异步调用的超时机制
- 支持基于Spring的简单本地调用方式(显式调用)，达到远程调用的目的，RPC调用
- 支持服务方应用上下线调用方和服务的自动发现，不用重启调用方应用
- 支持心跳和自动重连机制
- 支持应用与注册中心Zookeeper重连机制
- 支持注册中心的负载均衡的时候，切换到不健康服务方的时候，继续切换功能，并提供尝试切换次数的设定
- 支持注册中心的负载均衡，一致性Hash(Consistent Hash)算法，权重轮循算法(Round Robin)，随机轮循算法(Random)
- 支持授权，基于密钥的服务安全访问，跟Hessian自带的安全认证结合在一起，采用双向密钥匹配方式
- 支持接口调用的版本Version控制，版本不匹配拒绝调用，采用双向版本匹配方式
- 支持限流，通过令牌刷新方式，可控制单位时间内接口被调用的次数
- 支持升级后，服务中心持久化对象不一致，版本判断，并重新创建
- 支持远程配置和调优，管理者可以通过远程配置工具，配置和调优众多分布式的服务提供方和调用方，当然它们既可以使用本地的配置，也可以使用远程配置。业务端可以通过SPI方式自定义远程存储，比如通过外部参数化平台接入的方式，进行存储
- 支持统计中心的服务依赖情况(SOA治理)。查看服务方接口所在的地址和端口，所走的协议，所属应用，所属组，以及它暴露的接口方法列表，查看其是否启动，可动态刷新；查看调用方的所调用的接口，接入的地址，所走的协议，所属应用，所属组，查看其是否接入，可动态刷新
- 支持监控中心监控所有的方法调用(耗时、次数、异常等)信息，结合Redis(哨兵模式和集群模式)或者Splunk做动态日志监控，也可以通过第三方WebService分布式接入，做数据统计，通过广播方式，所有接入进来的WebService都将获得统计数据
- 提供界面化的服务治理，包括分布式的服务方和调用的分布情况，上下线动态刷新，以及令牌控制，密钥控制，版本控制等功能
- 提供图形化的网络拓扑部署展现
- 提供图形化的调用链跟踪和异常分析，可基于集成式的Log文件，Redis缓存等数据源
- 支持五大通信中间件的性能优化，通过独立的配置文件实现，采用局部配置和全局配置的方式，局部配置优先于全局配置
- 支持MQ(消息队列)在同一个进程中为服务/调用指定不同的MQ服务器
- 支持MQ(消息队列)指定三种Connection或Session的缓存方式(SingleConnectionFactory，CachingConnectionFactory，PooledConnectionFactory)
- 支持MQ(消息队列)指定两种初始化方式(JNDI和非JNDI)
- 支持异步事件驱动发布框架发布事件，进行业务系统的重试补偿，接入框架的所有异常通知(邮件或短信通知)
- 支持链式调用
- 支持不同线程池选用的队列类型配置，线程池Reject五大模式的配置。采用多线程池隔离技术，当多个服务部署在同一个JVM，一个服务调用忙，不会影响其它服务调用，业务端可以视具体场景，决定是否要开启线程隔离
- 支持线程亲和性。添加配置affinityThread = true，启动时在命令行添加-Daffinity.reserved={cpu-mark-in-hex}，就可以指定进程使用哪个CPU

## 依赖
```xml
<dependency>
  <groupId>com.nepxion</groupId>
  <artifactId>thunder-framework</artifactId>
  <version>${thunder.version}</version>
</dependency>
```

## 请联系我
微信、钉钉、公众号和文档

![](http://nepxion.gitee.io/discovery/docs/contact-doc/wechat-1.jpg)![](http://nepxion.gitee.io/discovery/docs/contact-doc/dingding-1.jpg)![](http://nepxion.gitee.io/discovery/docs/contact-doc/gongzhonghao-1.jpg)![](http://nepxion.gitee.io/discovery/docs/contact-doc/document-1.jpg)

## Star走势图
[![Stargazers over time](https://starchart.cc/Nepxion/Thunder.svg)](https://starchart.cc/Nepxion/Thunder)