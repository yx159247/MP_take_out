import adapter from './adapters/http'
import { forEach } from './utils'

export const METHOD = ['get', 'post', 'put', 'delete', 'connect', 'head', 'options', 'trace']
export const HEADER = ['common', ...METHOD]

const defaults = {
  adapter,
  header: {},
  method: 'GET',
  validateStatus: statusCode => statusCode >= 200 && statusCode < 300
}

forEach(HEADER, h => (defaults.header[h] = {}))

export default defaults
