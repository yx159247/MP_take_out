import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Cookies from 'js-cookie'
import zhCNLocale from 'element-ui/lib/locale/lang/zh-CN'
import zhCN from './zh-CN'

Vue.use(VueI18n)

export const messages = {
  'zh-CN': {
    '_lang': '简体中文',
    ...zhCN,
    ...zhCNLocale
  }
}

export default new VueI18n({
  locale: Cookies.get('language') || 'zh-CN',
  messages
})
