import { forEach, isArray } from '../utils'

/**
 * 根据 baseURL 和 url 拼接
 * @param {string} baseURL 请求根地址
 * @param {string} relativeURL 请求参数地址
 * @returns {string} 拼接后的地址
 */
function combineURL(baseURL = '', relativeURL = '') {
  // 判断是否 http:// 或 https:// 开头
  if (/^https?:\/\//.test(relativeURL)) return relativeURL
  // 去除 baseURL 结尾斜杠，去除 relativeURL 开头斜杠，再判断拼接
  return relativeURL ? baseURL.replace(/\/+$/, '') + '/' + relativeURL.replace(/^\/+/, '') : baseURL
}

function encode(val) {
  return encodeURIComponent(val)
    .replace(/%3A/gi, ':')
    .replace(/%24/g, '$')
    .replace(/%2C/gi, ',')
    .replace(/%20/g, '+')
    .replace(/%5B/gi, '[')
    .replace(/%5D/gi, ']')
}

function querystring(url, params) {
  let query

  const parts = []
  forEach(params, (val, key) => {
    if (val === null || typeof val === 'undefined') return

    if (isArray(val)) key = key + '[]'
    else val = [val]

    forEach(val, v => {
      if (v !== null && typeof v === 'object') {
        v = JSON.stringify(v)
      }
      parts.push(encode(key) + '=' + encode(v))
    })
  })
  query = parts.join('&')

  if (query) {
    const hashmarkIndex = url.indexOf('#')
    hashmarkIndex !== -1 && (url = url.slice(0, hashmarkIndex))
    url += (url.indexOf('?') === -1 ? '?' : '&') + query
  }

  return url
}

/**
 * 根据 baseURL、url、params query 编译请求URL
 * @returns {string} 处理后的地址
 */
export default function buildURL({ baseURL, url: relativeURL, params, query } = {}) {
  let url = combineURL(baseURL, relativeURL)

  if (!params && !query) {
    return url
  }

  if (params) {
    if (/\/:/.test(url)) {
      // 是否是 params 参数地址，并对应设置
      forEach(params, (val, key) => {
        url = url.replace(`/:${key}`, `/${encode(String(val))}`)
      })
    } else if (!query) {
      // 兼容旧的 params 属性设置 query
      url = querystring(url, params)
    }
  }

  // 设置 query 参数
  if (query) {
    url = querystring(url, query)
  }

  return url
}
