## 项目介绍

<p align=center>
  <a href="https://github.com/yx159247">
    <img src="https://avatars.githubusercontent.com/u/78630698?v=4" alt="David's Github" style="border-radius: 50%;height: 185px;width: 185px">
  </a>
</p>

<p align=center>
   基于Springboot + Vue 开发的前后端分离外卖点单系统
</p>

<p align="center">
   <a target="_blank" href="https://github.com/yx159247/MP_take_out">
      <img src="https://img.shields.io/hexpm/l/plug.svg"/>
      <img src="https://img.shields.io/badge/JDK-1.8+-green.svg"/>
      <img src="https://img.shields.io/badge/springboot-2.7.1-green"/>
      <img src="https://img.shields.io/badge/vue-2.6.11-green"/>
      <img src="https://img.shields.io/badge/mysql-8.0.29-green"/>
      <img src="https://img.shields.io/badge/mybatis--plus-3.5.2-green"/>
      <img src="https://img.shields.io/badge/redis-6.x-green"/>
      <img src="https://img.shields.io/badge/minio-8.3.7-green"/>
      <img src="https://img.shields.io/badge/quarz-2.3.2-green"/>
      <img src="https://img.shields.io/badge/shiro-1.9.0-green"/>
      <img src="https://img.shields.io/badge/easypoi-4.1.0-green"/>
      <img src="https://img.shields.io/badge/knife4j-2.0.9-green"/>
   </a>
</p>



[项目说明](#项目说明)|[目录结构](#目录结构)|[项目特点](#项目特点)|[技术选型](#技术选型)|[开发环境](#开发环境)|[项目截图](#项目截图)|[快速开始](#快速开始)|[注意事项](#注意事项)|[项目总结](#项目总结)|[如何反馈](#如何反馈)

## 项目说明

- 采用SpringBoot、Shiro、MyBatis-Plus框架，开发的前后端分离外卖点单系统，采用Shiro进行权限管理，使用uni-app开发微信小程序，支持微信登录、Redis缓存、quartz定时、导出数据等功能。
- 提供了代码生成器，只需编写30%左右代码，其余的代码交给系统自动生成，可快速完成开发任务
- 后台地址：http://localhost:8001（账号密码：admin/admin）
- 后台接口文档地址：http://localhost:8080/api/doc.html
- 微信小程序接口文档地址：http://localhost:8081/api/doc.html

您的star是我坚持的动力，感谢大家的支持，欢迎提交pr共同改进项目。

## 目录结构

后台前端项目位于renren-ui下

小程序前端项目位于takeout_mp下

SQL文件位于根目录下的takeout_mysql8.sql，需要MYSQL8以上版本。

**ps：请先运行后端项目，再启动前端项目。**

```
take_out
│ 
├─renren-admin      管理后台
│    ├─db  数据库SQL脚本
│    │ 
│    ├─modules  模块
│    │    ├─job 定时任务
│    │    ├─log 日志管理
│    │    ├─oss 文件存储
│    │    ├─security 安全模块
│    │    ├─sys 系统管理(核心)
|    |    └─takeout 外卖业务模块(核心)
│    │ 
│    └─resources 
│        ├─mapper   MyBatis文件
│        ├─public  静态资源
│        └─application.yml   全局配置文件
│       
│ 
├─renren-api        外卖小程序API服务
│ 
├─renren-common     公共模块
├─renren-generator  代码生成器
│        └─resources 
│           ├─mapper   MyBatis文件
│           ├─template 代码生成器模板（可增加或修改相应模板）
│           ├─application.yml    全局配置文件
│           └─generator.properties   代码生成器，配置文件
│
├─renren-ui     后台管理VUe前端项目
├─takeout_mp    uniapp微信小程序项目
```



## 项目特点

- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，通过token进行数据交互
- 支持动态权限修改，采用RBAC模型，前端菜单和后台权限实时更新。
- 提供CrudService接口，对增删改查进行封装，代码更简洁
- 页面交互使用Vue2.x，极大的提高了开发效率
- 完善的部门管理及数据权限，通过注解实现数据权限的控制
- 完善的XSS防范及脚本过滤，彻底杜绝XSS攻击
- 完善的代码生成机制，可在线生成entity、xml、dao、service、vue、sql代码，减少70%以上的开发任务
- 引入quartz定时任务，可动态完成任务的添加、修改、删除、暂停、恢复及日志查看等功能
- 引入Hibernate Validator校验框架，轻松实现后端校验
- 引入云存储服务，已支持：七牛云、阿里云、腾讯云等
- 引入swagger文档支持，方便编写API接口文档
- 新增AOP注解实现日志管理。  
- 代码遵循阿里巴巴开发规范，利于开发者学习。

## 技术选型

- 核心框架：Spring Boot 2.7.1
- 安全框架：Apache Shiro 1.9
- 持久层框架：MyBatis 3.5
- 定时器：Quartz 2.3
- 数据库连接池：Druid 1.2
- 日志管理：Logback
- 页面交互：Vue2.x
- 微信小程序：uni-app

## 开发环境

| 开发工具                      | 说明              |
| ----------------------------- | ----------------- |
| IDEA                          | Java开发工具IDE   |
| WebStrom                      | Vue开发工具IDE    |
| Navicat                       | MySQL远程连接工具 |
| Another Redis Desktop Manager | Redis远程连接工具 |
| FinalShell                    | Linux远程连接工具 |

| 开发环境 | 版本   |
| -------- | ------ |
| JDK      | 1.8    |
| MySQL    | 8.0.29 |
| Redis    | 6.0.5  |
| Maven    | 3.8    |

## 项目截图

![image](https://user-images.githubusercontent.com/78630698/174472611-aca5b2e1-b48e-4af6-9ed6-710d46a79a0f.png)
![image](https://user-images.githubusercontent.com/78630698/174472624-1a0ee932-8320-4822-9d35-e047db6eddb3.png)
![image](https://user-images.githubusercontent.com/78630698/174472629-290d4791-683b-48c5-9a9f-4d361d843db1.png)
![image](https://user-images.githubusercontent.com/78630698/187065050-6382f8fd-7f62-4921-8474-aa438b519e0b.png)
![image](https://user-images.githubusercontent.com/78630698/187065081-ce95050e-39cd-4e3f-9e03-3e5fbcfa86e6.png)


## 快速开始

- 通过git下载源码

- idea、eclipse需安装lombok插件，不然会提示找不到entity的get set方法

- 创建数据库takeout，数据库编码为UTF-8mb4

- 执行takeout_mysql8.sql文件，初始化数据

- 修改application-dev.yml文件，更新MySQL账号和密码、Redis的相关配置信息

- 修改application-dev.yml文件，更新Minio配置（下面有安装Minio的教程）

- ```
  minio:
    # minio配置的地址，端口9000
    url: http://你安装minio的ip:9000
    # 账号
    accessKey: minio
    # 密码
    secretKey: minio123
    # MinIO桶名字
    bucketName: images
  ```

- 在renren-api模块下，修改application.yml文件，更新微信小程序配置

  ```
  weixin:
    getPhoneNumberUrl:
    getAccessTokenUrl:
    jscode2sessionUrl:
    appid:
    secret:
  ```

- 

- 在takeout目录下，执行mvn clean install

- Eclipse、IDEA运行AdminApplication.java，则可启动项目【renren-admin】

- 再启动前端项目

- 账号密码：admin/admin

#### 安装Minio

- 用docker拉取Minio镜像

  ```dockerfile
  docker pull minio/minio
  ```

  安装镜像

- ```dockerfile
   docker run \
  --name minio \
  -p 9000:9000  \
  -p 9090:9090  \
  -d --restart=always \
  -e "MINIO_ROOT_USER=minio" \
  -e "MINIO_ROOT_PASSWORD=minio123" \
  -v /opt/docker_minio/data:/data \
  -v /opt/docker_minio/config:/root/.minio \
  minio/minio server  /data --console-address ":9090" --address ":9000"
  ```

  

- 登录（这里的ip地址填自己的就可以了）

  页面地址 http://192.168.65.100:9090/login

- 创建一个名字为images的桶

  ![image](https://user-images.githubusercontent.com/78630698/183250823-02e45090-ca48-4241-afeb-f3cf9cce70e3.png)

  ![image](https://user-images.githubusercontent.com/78630698/183250838-be4dc053-5073-4415-9c23-657cdcf805f2.png)
  ![image](https://user-images.githubusercontent.com/78630698/183250865-e3e7d3ce-5fc5-4c42-9ecd-d0723d57f944.png)

- 更改Yml配置文件

  ![image](https://user-images.githubusercontent.com/78630698/183250736-99e50ba9-82fe-43d9-8674-c57ba933aa9a.png)

  ps：url改成你自己的，账号和密码如果之前安装docker镜像的时候没动就不用改，bucketName可以不用修改。

## 注意事项

- 如果使用minio存储文件，需要先安装好minio
- 如果要运行小程序，需要先提前申请一个小程序开发测试账号

## 项目总结

这个作为新手入门项目是十分不错的，项目所用的技术栈覆盖的也比较广，适合初学者学习。做的不好的地方请大家见谅，有问题的或者有好的建议可以私聊联系我。

## 如何反馈

- 有问题可以扫下面的二维码添加我的微信(添加好友时，请备注来意)，我会尽力帮您解答！
 ![WeChat](https://user-images.githubusercontent.com/78630698/187065150-536ccff2-c5e9-4a52-abea-87deed9047a4.jpg)


