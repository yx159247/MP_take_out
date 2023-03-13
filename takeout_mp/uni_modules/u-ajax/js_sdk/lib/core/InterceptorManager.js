/**
 * 拦截器类
 */
export default class InterceptorManager {
  handlers = []

  use(fulfilled, rejected) {
    this.handlers.push({
      fulfilled,
      rejected
    })
    return this.handlers.length - 1
  }

  eject(id) {
    if (this.handlers[id]) {
      this.handlers[id] = null
    }
  }

  forEach(fn, reverse = false) {
    if (reverse) {
      for (let i = this.handlers.length - 1; i >= 0; i--) {
        this.handlers[i] !== null && fn(this.handlers[i])
      }
    } else {
      for (let i = 0, l = this.handlers.length; i < l; i++) {
        this.handlers[i] !== null && fn(this.handlers[i])
      }
    }
  }
}
