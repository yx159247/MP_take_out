export type AnyObject = { [x: string]: any }

export type Data = string | AnyObject | ArrayBuffer
export type Method = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'CONNECT' | 'HEAD' | 'OPTIONS' | 'TRACE'
export type DataType = 'json' | 'text' | 'html'
export type ResponseType = 'text' | 'arraybuffer'

export interface RequestTask {
  abort: () => void
  onHeadersReceived?: (listener: (header: any) => void) => void
  offHeadersReceived?: (listener: (header: any) => void) => void
}

export interface FetcherInstance<T = any> {
  resolve: (value: T) => void
  reject: (reason?: any) => void
  source: () => Promise<T>
  abort: () => Promise<void>
}

export interface FetcherConstructor {
  new <T = RequestTask>(): FetcherInstance<T>
}

export interface CustomConfig {}

export interface AjaxRequestConfig extends CustomConfig {
  baseURL?: string
  url?: string
  data?: Data
  query?: AnyObject
  params?: AnyObject
  header?: any
  method?: Method
  timeout?: number
  dataType?: DataType
  responseType?: ResponseType
  sslVerify?: boolean
  withCredentials?: boolean
  firstIpv4?: boolean
  fetcher?: FetcherInstance
  validateStatus?: ((statusCode?: number) => boolean) | null
  adapter?: (config: AjaxRequestConfig) => Promise<any>
}

export type AjaxConfigType =
  | AjaxRequestConfig
  | (() => AjaxRequestConfig)
  | (() => Promise<AjaxRequestConfig>)
  | void

export interface AjaxResponse<T = any> {
  data: T
  statusCode: number
  header: any
  config: AjaxRequestConfig
  errMsg: string
  cookies: string[]
}

export interface AjaxInterceptorManager<V> {
  use<T = V>(onFulfilled?: (value: V) => T | Promise<T>, onRejected?: (error: any) => any): number
  eject(id: number): void
}

export interface CustomResponse<T = any> {}

export type AjaxResult<T> = keyof CustomResponse extends never ? AjaxResponse<T> : CustomResponse<T>

export interface AjaxInvoke {
  <T = any, R = AjaxResult<T>>(config?: AjaxRequestConfig): Promise<R>
  <T = any, R = AjaxResult<T>>(url?: string, data?: Data, config?: AjaxRequestConfig): Promise<R>
}

export interface AjaxInstance<T extends AjaxConfigType> extends AjaxInvoke {
  get: AjaxInvoke
  post: AjaxInvoke
  put: AjaxInvoke
  delete: AjaxInvoke
  connect: AjaxInvoke
  head: AjaxInvoke
  options: AjaxInvoke
  trace: AjaxInvoke
  getURL(config?: AjaxConfigType): Promise<string>
  readonly defaults: AjaxRequestConfig
  readonly config: T
  interceptors: {
    request: AjaxInterceptorManager<AjaxRequestConfig>
    response: AjaxInterceptorManager<AjaxResponse>
  }
}

export interface AjaxStatic extends AjaxInstance<void> {
  create<T extends AjaxConfigType = void>(config?: T): AjaxInstance<T>
  Fetcher: FetcherConstructor
}

declare const Ajax: AjaxStatic
declare const Fetcher: FetcherConstructor

export { Fetcher }

export default Ajax
