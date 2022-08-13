# starsea 一个tg机器人框架

## 部署

1. 执行`./gradlew build`，编译项目
2. 在`build/distributions`下找到打包好的tar包xxx.tar，上传到你的服务器上
3. `tar -xvf xxx.tar`
4. `mv xxx starsea`
5. `mv starsea/config.json ./`，并编辑该文件，填入你的token
6. `mkdir plugin`，并将你获得的插件拷贝到plugin目录下
7. `chmod +x starsea/starsea-demand.sh`
8. `nohup starsea/starsea-demand.sh &`

`config.json` 字段：

- bot_token：你的tg机器人token
- super_pwd：超级管理员密码，目前用于机器人关机。

机器人目前提供了一个命令可以用于关闭机器人，即`/power [超级管理员密码]`。当然，如果你是通过demand启动的机器人还会再活过来……我只是为了方便重启这样设计，真的想停机不如直接用kill。

超级管理员密码未设置会在启动时随机生成一个UUID，并输出到日志。

## 插件开发

参考示例项目[echo bot](https://github.com/rfkhx/starsea-echobot)，实现你的功能。

## 结构说明
### 消息循环

参见`top.ntutn.starsea.arch`包下的`Handler`和`Looper`类。参考了安卓上消息循环代码。

### 网络请求

难点：长轮询、图片上传。

### 插件

自定义类PluginClassLoader继承URLClassLoader，借助SPI服务发现。

当前最新版本机器人支持的插件api版本：

| 插件版本  | 支持能力             | 变更内容        |
|-------|------------------|-------------|
| 1.0.0 | 基本文本消息           | 插件第一个版本     |
| 1.1.3 | 基本文本消息<br>基本图片消息 | 增加了基本图片消息支持 |

