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