// ajax.js

// 引入 uni-ajax 模块
import ajax from '@/uni_modules/u-ajax'

// 创建请求实例
const instance = ajax.create({
  // 初始配置
  baseURL:  process.env.VUE_APP_BASE_URL //微信开发者工具用这个url
  // baseURL: 'http://192.168.109.200:8081/api' //真机调试用这个url
})

// 添加请求拦截器
instance.interceptors.request.use(
  config => {
    // 在发送请求前做些什么
	const token = uni.getStorageSync('token')
	token && (config.header['token'] = token)
    return config
  },
  error => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 添加响应拦截器
instance.interceptors.response.use(
  response => {
    // 对响应数据做些什么

    return response.data
  },
  error => {
    // 对响应错误做些什么
	
    return Promise.reject(error)
  }
)

// 导出 create 创建后的实例
export default instance
