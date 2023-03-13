import { assign, forEach } from '../utils'

/**
 * 深度合并，且不合并 undefined 值
 * @param {object} obj1 前对象
 * @param {object} obj2 后对象
 * @returns {object} 合并后的对象
 */
function merge(obj1 = {}, obj2 = {}) {
  const obj = {}

  const objKeys = Object.keys({ ...obj1, ...obj2 })

  forEach(objKeys, prop => {
    if (obj2[prop] !== undefined) {
      obj[prop] = assign(obj1[prop], obj2[prop])
    } else if (obj1[prop] !== undefined) {
      obj[prop] = assign(undefined, obj1[prop])
    }
  })

  return obj
}

/**
 * 合并请求配置
 * @param  {...object|function} args 请求配置
 * @returns {object} 合并后的请求配置
 */
export default async function mergeConfig(...args) {
  let config = {}

  for (let i = 0, l = args.length; i < l; i++) {
    const current = typeof args[i] === 'function' ? await args[i]() : args[i]
    config = merge(config, current)
  }

  config.method = config.method.toUpperCase()

  return config
}
