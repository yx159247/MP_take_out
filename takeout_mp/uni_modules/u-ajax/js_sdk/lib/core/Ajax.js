import InterceptorManager from './InterceptorManager'
import buildURL from '../helpers/buildURL'
import mergeConfig from '../helpers/mergeConfig'
import dispatchRequest from './dispatchRequest'
import { detachCancel, dispatchCancel, interceptCancel } from './handleCancel'
import defaults, { METHOD } from '../defaults'
import { forEach, merge } from '../utils'

export default function Ajax(defaultConfig) {
  // 挂载当前实例配置
  ajax.config = Object.freeze(
    typeof defaultConfig === 'object' ? merge(defaultConfig) : defaultConfig
  )

  // 挂载全局默认配置引用
  ajax.defaults = defaults

  // 挂载拦截器
  ajax.interceptors = {
    request: new InterceptorManager(),
    response: new InterceptorManager()
  }

  // 挂载获取实例请求地址方法
  ajax.getURL = async function getURL(config) {
    const combineConfig = await mergeConfig(defaults, defaultConfig, config)
    return buildURL(combineConfig).replace(/^\?/, '')
  }

  // 挂载对应的 method 方法
  forEach(METHOD, method => {
    ajax[method] = function methodAjax(url, data, config) {
      if (typeof url === 'string') return ajax(url, data, { ...config, method })
      return ajax({ ...url, method })
    }
  })

  function ajax(url, data, config) {
    const newConfig = typeof url === 'string' ? { ...config, url, data } : { ...url }

    // 声明 Promise 链
    const chain = [dispatchRequest, dispatchCancel]

    // 将请求拦截遍历添加到链前面
    ajax.interceptors.request.forEach(
      ({ fulfilled, rejected }) => chain.unshift(fulfilled, rejected),
      true
    )

    // 将响应拦截遍历添加到链后面
    ajax.interceptors.response.forEach(
      ({ fulfilled, rejected }) => chain.push(fulfilled, interceptCancel(rejected)),
      false
    )

    // 先执行获取 config 请求配置
    chain.unshift(config => mergeConfig(defaults, defaultConfig, config), undefined)

    // 处理发起请求前的错误数据
    chain.push(undefined, detachCancel)

    // 创建请求Promise，后面遍历链将链上方法传递到then回调
    let request = Promise.resolve(newConfig)
    while (chain.length) {
      request = request.then(chain.shift(), chain.shift())
    }

    return request
  }

  return ajax
}
