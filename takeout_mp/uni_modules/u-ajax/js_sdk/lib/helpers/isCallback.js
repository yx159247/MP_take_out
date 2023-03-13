/**
 * 判断参数是否含有回调参数 success / fail / complete 之一
 * @param {string} field 参数的 Key 值字符串
 * @returns {boolean} 返回判断值
 */
export default function isCallback(field) {
  return ['success', 'fail', 'complete'].includes(field)
}
