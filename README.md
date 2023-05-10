


## 项目介绍

<p align=center>
  <a href="https://github.com/yx159247">
    <img src="https://avatars.githubusercontent.com/u/78630698?v=4" alt="David's Github" style="border-radius: 50%;height: 185px;width: 185px">
  </a>
</p>


<p align=center>
   基于Springboot + Vue 开发的前后端分离外卖点单系统
  （2024届本科软件工程专业大学生在线找工作）
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

## 如何反馈

- 有问题可以扫下面的二维码添加我的微信(添加好友时，请备注来意，再帮忙点个star)，我会尽力帮您解答！

  ![WeChat](https://user-images.githubusercontent.com/78630698/187065150-536ccff2-c5e9-4a52-abea-87deed9047a4.jpg)	



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
├─renren-admin      美食元素后台管理后端服务
│    │
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
├─renren-api        美食元素小程序后端服务
│ 
├─renren-common     公共模块
├─renren-generator  代码生成器
│        └─resources 
│           ├─mapper   MyBatis文件
│           ├─template 代码生成器模板（可增加或修改相应模板）
│           ├─application.yml    全局配置文件
│           └─generator.properties   代码生成器，配置文件
│
├─renren-ui     美食元素后台管理Vue前端项目
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

| 开发工具                      | 说明               |
| ----------------------------- | ------------------ |
| IDEA                          | Java开发工具IDE    |
| WebStrom或者VS Code           | Vue开发工具IDE     |
| Navicat                       | MySQL远程连接工具  |
| Another Redis Desktop Manager | Redis远程连接工具  |
| FinalShell                    | Linux远程连接工具  |
| HBuilder X                    | 编译微信小程序工具 |

| 开发环境 | 版本   |
| -------- | ------ |
| JDK      | 1.8    |
| MySQL    | 8.0.29 |
| Redis    | 6.0.5  |
| Maven    | 3.8    |
| NodeJs   | 14.x.x |

## 项目截图
<img width="364" alt="image" src="https://user-images.githubusercontent.com/78630698/222748803-a563e436-6bc2-4918-803e-7eeac16c1a76.png">
<img width="363" alt="image" src="https://user-images.githubusercontent.com/78630698/222748583-3efaa4d9-0e05-4f60-822e-2adb7c793d1b.png">
<img width="363" alt="image" src="https://user-images.githubusercontent.com/78630698/222748959-b6c298c0-f12d-4cbc-a845-11fc399db9ad.png">
<img width="363" alt="image" src="https://user-images.githubusercontent.com/78630698/222749599-3f32fac3-0862-4d5d-bb66-3a32000d7042.png">
<img width="364" alt="image" src="https://user-images.githubusercontent.com/78630698/222749755-23105720-de43-4506-87db-cb1d45690cb6.png">
<img width="363" alt="image" src="https://user-images.githubusercontent.com/78630698/222749875-134fd198-bf7c-4bb0-b35b-4261ee6d4671.png">
<img width="362" alt="image" src="https://user-images.githubusercontent.com/78630698/222751313-ef78b4eb-9018-4281-b3d7-d8924b1428d8.png">

![image](https://user-images.githubusercontent.com/78630698/187065050-6382f8fd-7f62-4921-8474-aa438b519e0b.png)

![image](https://user-images.githubusercontent.com/78630698/187065081-ce95050e-39cd-4e3f-9e03-3e5fbcfa86e6.png)

## 快速开始

### 准备工作(IDE、小程序测试号、环境等)

#### 注册小程序测试账号

- 打开网址：https://developers.weixin.qq.com/miniprogram/dev/devtools/sandbox.html
- 按着步骤操作就可以了

#### 安装Minio(如果你用阿里云、腾讯云、七牛云可以忽略此步骤，直接看文件上传配置那一步)

- 虚拟机或者云服务器用docker拉取Minio镜像

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

#### 安装Nodejs环境(如果有请忽略)

- NodeJs版本不要安装太高，14.x最好
- 附上一个下载链接：https://nodejs.org/download/release/v14.21.3/node-v14.21.3-x64.msi
- 具体配置环境变量等请自行百度一下找一下教程，步骤很简单。

### 运行后台管理系统后端项目

- 通过git下载源码

- idea、eclipse需安装lombok插件，不然会提示找不到entity的get set方法

- 创建数据库takeout，数据库编码为UTF-8mb4

- 执行takeout_mysql8.sql文件，初始化数据

- 打开application-dev.yml文件，修改MySQL账号和密码、Redis的相关配置信息

- 在renren-api模块下，打开application.yml文件，修改微信小程序appid和secret配置

  ```
  weixin:
    appid:
    secret:
  ```

- 在takeout目录下，执行mvn clean install

- Eclipse、IDEA运行AdminApplication.java，则可启动项目【renren-admin】


### 运行后台管理系统前端项目

- 配置好Nodejs环境之后就可以用WebStorm或者VsCode打开renren-ui文件夹(后台管理前端源代码)

- 打开终端输入

  ```
  npm install
  ```

  等待安装依赖完成后，输入

  ```
  npm run serve
  ```

  等待编译完成后，会自动打开页面，或者访问 http://localhost:8001/
  - 账号密码：admin/admin
  
 ### 安装以及修改HBuilder X配置以及微信开发者工具配置

- 先去Dcloud官网下载Hubilder X 、以及微信开发者工具
- Hbuilder X：https://www.dcloud.io/hbuilderx.html?source=javascriptc.com
- 微信开发者工具：https://developers.weixin.qq.com/miniprogram/dev/devtools/nightly.html
- 然后安装打开Hbuilder X软件，打开takeout_mp文件夹(小程序的源代码)

- 打开微信开发者工具微信修改微信开发者工具的设置，将服务端口打开，不打开，HBuilder X在编译之后不会自动唤醒微信开发者工具

  ![image](https://user-images.githubusercontent.com/78630698/192106411-320ba427-52ab-480d-bd14-7ff5cfe1c744.png)

  

- 将图片中的AppId修改为你自己申请的小程序测试号AppId

  ![image](https://user-images.githubusercontent.com/78630698/192106120-5937b039-bd43-40dd-8561-b1bd8c8e1e67.png)	

### 启动微信小程序项目

- 完成了以上配置之后就可以启动小程序项目了

  ### ![image](https://user-images.githubusercontent.com/78630698/192106657-5cdf1224-7802-4016-8cea-333a81ef7bd2.png)

### 文件上传配置

- 在后台管理系统里直接更改配置
- ![image](https://user-images.githubusercontent.com/78630698/222681494-1f2862da-9e47-4d42-8684-12b73ed030b1.png)

## 注意事项

- 如果使用minio存储文件，需要先安装好minio
- 如果要运行小程序，需要先提前申请一个小程序开发测试账号

## 项目总结

这个作为新手入门项目是十分不错的，项目所用的技术栈覆盖的也比较广，适合初学者学习。做的不好的地方请大家见谅，有问题的或者有好的建议可以私聊联系我。
