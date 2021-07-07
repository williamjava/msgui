# 微服务架构实践之msgui
## 集成RabbitMQ(rabbitMQ是一个在AMQP协议标准基础上完整的，可服用的企业消息系统。它遵循Mozilla Public License开源协议，采用 Erlang 实现的工业级的消息队列(MQ)服务器，Rabbit MQ 是建立在Erlang OTP平台上。)
1.安装Erlang
* 下载地址：https://www.erlang.org/downloads
* 设置环境变量，新建ERLANG_HOME
* 修改环境变量path，增加Erlang变量至path，%ERLANG_HOME%\bin
* 打开cmd命令框，输入erl

2.安装RabbitMQ
* 下载地址：http://www.rabbitmq.com/install-windows-manual.html
* 设置环境变量，新建RABBITMQ_SERVER
* 修改环境变量path，增加rabbitmq变量至path，%RABBITMQ_SERVER%\sbin;
* 打开cmd命令框，切换至sbin目录下，输入rabbitmqctl status查看是否安装成功
* 安装插件,rabbitmq-plugins.bat enable rabbitmq_management
* 安装插件如果不顺利，执行下面的步骤
```
将 C:\Users\Administrator\.erlang.cookie 同步至C:\Windows\System32\config\systemprofile\.erlang.cookie 
同时删除：C:\Users\Administrator\AppData\Roaming\RabbitMQ目录
输入命令：rabbitmq-plugins.bat enable rabbitmq_management ，出现下面信息表示插件安装成功：
```
* rabbitmq-server.bat
* 访问：http://localhost:15672 (默认用户名和密码：guest/guest)