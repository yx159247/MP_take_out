export default function adapter(config) {
  return new Promise((resolve, reject) => {
    const requestTask = uni.request({
      ...config,
      complete: result => {
        // 根据状态码判断要执行的触发的状态
        const response = { config, ...result }
        !config.validateStatus || config.validateStatus(result.statusCode)
          ? resolve(response)
          : reject(response)
      }
    })

    config.fetcher?.resolve(requestTask)
  })
}
