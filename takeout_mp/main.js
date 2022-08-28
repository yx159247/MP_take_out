import App from './App'
import uView from '@/uni_modules/uview-ui'
Vue.use(uView)
// #ifndef VUE3
import Vue from 'vue'
Vue.config.productionTip = false

// 封装弹窗的方法
uni.$showMsg = function(title = '数据请求失败' , duration = 1500,icon='none') {
	uni.showToast({
		title,
		duration,
		icon
	})
}

App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}


// #endif