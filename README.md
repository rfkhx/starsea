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

## 插件开发

参考示例项目[echo bot](https://github.com/rfkhx/starsea-echobot)，实现你的功能。

## 结构说明
### 消息循环

参见`top.ntutn.starsea.arch`包下的`Handler`和`Looper`类。参考了安卓上消息循环代码。

### 网络请求

难点：长轮询、图片上传。

### 插件

自定义类PluginClassLoader继承URLClassLoader，借助SPI服务发现。
