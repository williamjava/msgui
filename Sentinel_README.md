# 微服务架构实践之msgui
## 集成Sentien(一个熔断限流的开源中间件)
1.环境准备
* 下载地址：https://github.com/alibaba/Sentinel/releases/download/1.8.2/sentinel-dashboard-1.8.2.jar
* 启动：java -Dserver.port=8070 -Dcsp.sentinel.dashboard.server=localhost:8070 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.0.jar
* 访问：http://localhost:8070 (默认用户名密码：sentinel/sentinel)

2.项目集成
* 增加如下Maven依赖
```
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```
* 访问对应的项目，Sentinel控制台才会出现对应的服务