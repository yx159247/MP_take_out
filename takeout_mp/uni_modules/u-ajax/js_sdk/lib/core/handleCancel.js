const CANCEL = Symbol('$$cancel')

function hasCancel(target) {
  return target === null || target === undefined
    ? false
    : Object.prototype.hasOwnProperty.call(target, CANCEL)
}

/**
 * 派发请求拒绝方法，处理发起请求前错误，取消执行请求，并防止进入响应拦截器
 * @param {*} reason 错误原因
 * @returns {Promise} 封装了 CANCEL 的失败对象
 */
export function dispatchCancel(reason) {
  return Promise.reject({ [CANCEL]: reason })
}

/**
 * 拦截失败对象
 * @param {Function} rejected 响应错误拦截器
 */
export function interceptCancel(rejected) {
  // 判断发起请求前是否发生错误，如果发生错误则不执行后面的响应错误拦截器
  return (
    rejected && (response => (hasCancel(response) ? Promise.reject(response) : rejected(response)))
  )
}

/**
 * 分离失败对象
 * @param {*} response 封装了 CANCEL 的失败对象
 */
export function detachCancel(error) {
  return Promise.reject(hasCancel(error) ? error[CANCEL] : error)
}
