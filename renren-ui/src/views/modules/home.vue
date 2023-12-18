<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-home">
      <h3>项目介绍</h3>
      <ul>
        <li>本项目基于人人开源的renren-security进行后端开发，前端使用renren-ui作为实现renren-security后台管理前端功能</li>
        <li>前后端分离，通过token进行数据交互，可独立部署</li>
        <li>动态菜单，通过菜单管理统一管理访问路由</li>
        <li>支持动态权限修改，采用RBAC模型，前端菜单和后台权限实时更新。</li>
        <li>您的star是我坚持的动力，感谢大家的支持</li>
      </ul>
      <h3>技术介绍</h3>
      <ul>
        <li>前端： vue + vuex + vue-router + axios + vuetify + element + echarts + uni—app</li>
        <li>后端： SpringBoot  + docker + Shiro + Swagger + MyBatisPlus + Mysql + Redis + quartz</li>
        <li>其他： 微信小程序、使用Minio实现文件上传</li>
      </ul>
      <h3>如何反馈</h3>
      <ul>
        <li>有问题可以给我发邮件，我会尽力帮您解答！</li>
      </ul>
    </div>
  </el-card>
</template>
<script>
import Cookies from 'js-cookie'
import WebsocketHeartbeatJs from 'websocket-heartbeat-js';


export default {
  data() {
    return {

    }
  },
  watch: {

  },
  created() {

  },
  mounted() {

  },

  methods: {


  }
}
</script>

<style>
.mod-home {
  line-height: 1.5;
}
</style>
