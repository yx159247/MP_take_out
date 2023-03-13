import Ajax from './lib/core/Ajax'
import Fetcher from './lib/adapters/Fetcher'

const ajax = Ajax()

ajax.create = function create(instanceConfig) {
  return Ajax(instanceConfig)
}

ajax.Fetcher = Fetcher

export { Fetcher }

export default ajax
