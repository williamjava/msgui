# 微服务架构实践之msgui
## SpringCloudAlibaba(Nacos + Sentinel + Seata) + Dubbo + 缓存(Redis) + 消息(RabbitMQ)
1.网关访问(msgui-gateway)
* http://localhost:10101/user/swagger-ui.html
* 增加token认证(如：bearer d8b94175-7c45-4239-a12b-7c2c83207137)

2.认证服务器，获取token(msgui-auth-server)
* http://localhost:10102/swagger-ui.html
* 调用login接口获取token(如：d8b94175-7c45-4239-a12b-7c2c83207137)

3.项目运行
* 启动Redis服务(指定配置文件通过命令行启动：./redis-server.exe redis.windows.conf)
* 启动Nacos(通过命令行以单机模式启动：./startup.cmd -m standalone)
* 启动RepoUserApplication
* 启动BusiUserProviderApplication
* 启动CtlUserApplication
* 启动AuthServerApplication
* 启动GatewayApplication

4.分布式链路追踪系统SkyWalking(零侵入)
* windows版本es：链接: https://pan.baidu.com/s/1j1-pds9nGoUuhy584DjUZg 提取码: meit
* windows版本skywalking：链接: https://pan.baidu.com/s/1UC99LsuqTCpCvOVKkkC7fg 提取码: smaa
* 启动es，运行bin目录下的elasticsearch.bat文件
* 启动skywalking，运行bin目录下的startup.bat文件
* 项目启动参数中增加如下参数：
```
-javaagent:C:\Users\admin\Desktop\apache-skywalking-apm-bin-es7\agent\skywalking-agent.jar
-Dskywalking.agent.service_name=msgui-gateway(服务名)
-Dskywalking.collector.backend_service=localhost:11800
```
* 访问：http://localhost:8090/ (端口可在skywalkiing文件目录下的webapp目录下，修改webapp.yml文件)