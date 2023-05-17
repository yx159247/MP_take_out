<template>
  <transition name="el-fade-in-linear">
    <router-view />


  </transition>
</template>

<style>
  .el-table th.gutter{
    display: table-cell!important;
  }
</style>
<script>
import Cookies from 'js-cookie'
import { messages } from '@/i18n'
import audioFile from '@/assets/audio/OrderNotificationTone.mp3'
import silentAudio from '@/assets/audio/silent.mp3'

import th from "element-ui/src/locale/lang/th";
import WebsocketHeartbeatJs from 'websocket-heartbeat-js';
import el from "element-ui/src/locale/lang/el";
// let websocketHeartbeatJs = null
// websocketHeartbeatJs = new WebsocketHeartbeatJs({
//   url: `${window.SITE_CONFIG['webSocketURL']}/takeout/websocket?token=${Cookies.get('token')}`
// });
export default {
  data() {
    return {
      timeout: 10 * 1000, // 45秒一次心跳
      timeoutObj: null, // 心跳心跳倒计时
      serverTimeoutObj: null, // 心跳倒计时
      timeoutnum: null, // 断开 重连倒计时
      lockReconnect: false, // 防止
      websocket: null,
      audio : null,
      silentAudio : null,


    }
  },
  watch: {
    '$i18n.locale': 'i18nHandle'
  },
  created() {
    this.silentAudio = new Audio(silentAudio);
    this.audio = new Audio(audioFile);
    this.i18nHandle(this.$i18n.locale)
  },
  mounted() {
    this.initWebSocket();
  },

  methods: {
    play(){

    },
    initWebSocket() {
      // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
      let wsUrl = `${window.SITE_CONFIG['webSocketURL']}/takeout/websocket?token=${Cookies.get('token')}`
      this.websocket = new WebSocket(wsUrl);
      this.websocket.onopen = this.websocketonopen;
      this.websocket.onerror = () => {
        console.log('WebSocket连接发生错误');
        this.reconnect();
      };
      this.websocket.onmessage = this.setOnmessageMessage;
      this.websocket.onclose = this.websocketclose;
      // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      // window.onbeforeunload = that.onbeforeunload
    },
    async setOnmessageMessage(event) {
      console.log('收到服务端消息:', event.data);
      if (Cookies.get('token') === undefined) {
        this.reconnect()
        return
      }
      this.reset();
      // //发现消息进入    开始处理前端触发逻辑
      if (event.data === 'success' || event.data === 'heartbeat') {
        return
      }
      if (event.data === 'close'){
        this.websocket.close()
        return
      }
      this.$notify({
        title: '通知',
        message: event.data,
        type: 'success',
        duration: 0
      });
      this.silentAudio.play();
      setTimeout(() => {
        this.audio.play()
      }, 300)

    },
    start() {
      console.log('start');
      //清除延时器
      this.timeoutObj && clearTimeout(this.timeoutObj);
      this.serverTimeoutObj && clearTimeout(this.serverTimeoutObj);
      this.timeoutObj = setTimeout(() => {
        if (this.websocket && this.websocket.readyState === 1) {
          this.websocket.send('heartbeat');//发送消息，服务端返回信息，即表示连接良好，可以在socket的onmessage事件重置心跳机制函数
        } else {
          this.reconnect();
        }
        //定义一个延时器等待服务器响应，若超时，则关闭连接，重新请求server建立socket连接
        this.serverTimeoutObj = setTimeout(() => {
          this.websocket.close();
        }, this.timeout)
      }, this.timeout)
    },
    reset() { // 重置心跳
      // 清除时间
      clearTimeout(this.timeoutObj);
      clearTimeout(this.serverTimeoutObj);
      // 重启心跳
      this.start();
    },
    // 重新连接
    reconnect() {
      if (this.lockReconnect) return
      this.lockReconnect = true;
      //没连接上会一直重连，设置延迟避免请求过多
      this.timeoutnum && clearTimeout(this.timeoutnum);
      this.timeoutnum = setTimeout(() => {
        this.initWebSocket();
        this.lockReconnect = false;
      }, 5000)
    },
    websocketonopen() {
      //开启心跳
      this.start();
      console.log("WebSocket连接成功!!!"+new Date()+"----"+this.websocket.readyState);
    },
    websocketonerror(e) {
      console.log("WebSocket连接发生错误" + e);
    },
    websocketclose(e) {
      // this.websocket.close();
      // clearTimeout(this.timeoutObj);
      // clearTimeout(this.serverTimeoutObj);
      // console.log("WebSocket连接关闭");
      this.reconnect()
    },
    websocketsend(message) {
      this.websocket.send(message)
    },
    closeWebSocket() { // 关闭websocket
      this.websocket.close()
    },
    i18nHandle(val, oldVal) {
      Cookies.set('language', val)
      document.querySelector('html').setAttribute('lang', val)
      document.title = messages[val].brand.lg
      // 非登录页面，切换语言刷新页面
      if (this.$route.name !== 'login' && oldVal) {
        window.location.reload()
      }
    },
  }
}
</script>
