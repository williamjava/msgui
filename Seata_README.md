# 微服务架构实践之msgui
## 集成Seata(分布式事务解决方案)
1.下载Seata
* 下载地址：https://github.com/seata/seata/releases
2.数据库配置
* 创建seata数据库
3.Nacos配置
* Nacos中新建命名空间msgui-seata，专用于放置 seata 配置
* 增加Seata如下配置到Nacos
```
service.vgroupMapping.fsp_tx_msgui_group=default
store.mode=db
store.db.datasource=druid
store.db.dbType=mysql
store.db.driverClassName=com.mysql.cj.jdbc.Driver
store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true
store.db.user=root
store.db.password=root
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.lockTable=lock_table
store.db.queryLimit=100
store.db.maxWait=5000
```
4.服务配置并启动
* ./seata-server.bat -p 8091 -h 127.0.0.1 -m db

5.具体服务中增加seata依赖以及两个文件(file.conf和registry.conf)；使用Seata代理数据源(如：RepoStockApplication.java)

6.业务入口使用注解@GlobalTransactional