<template>
  <div v-loading.fullscreen.lock="loading" :element-loading-text="$t('loading')" :class="['aui-wrapper', { 'aui-sidebar--fold': $store.state.sidebarFold }]">
    <template v-if="!loading">
      <main-navbar />
      <main-sidebar />
      <div class="aui-content__wrapper">
        <main-content v-if="!$store.state.contentIsNeedRefresh" />
      </div>
    </template>
  </div>
</template>

<script>
import MainNavbar from './main-navbar'
import MainSidebar from './main-sidebar'
import MainContent from './main-content'
import debounce from 'lodash/debounce'
export default {
  provide () {
    return {
      // 刷新
      refresh () {
        this.$store.state.contentIsNeedRefresh = true
        this.$nextTick(() => {
          this.$store.state.contentIsNeedRefresh = false
        })
      }
    }
  },
  data () {
    return {
      loading: true
    }
  },
  components: {
    MainNavbar,
    MainSidebar,
    MainContent
  },
  watch: {
    $route: 'routeHandle'
  },
  created () {
    this.windowResizeHandle()
    this.routeHandle(this.$route)
    Promise.all([
      this.getUserInfo(),
      this.getPermissions()
    ]).then(() => {
      this.loading = false
    })
  },
  methods: {
    // 窗口改变大小
    windowResizeHandle () {
      this.$store.state.sidebarFold = document.documentElement['clientWidth'] <= 992 || false
      window.addEventListener('resize', debounce(() => {
        this.$store.state.sidebarFold = document.documentElement['clientWidth'] <= 992 || false
      }, 150))
    },
    // 路由, 监听
    routeHandle (route) {
      if (!route.meta.isTab) {
        return false
      }
      var tab = this.$store.state.contentTabs.filter(item => item.name === route.name)[0]
      if (!tab) {
        tab = {
          ...window.SITE_CONFIG['contentTabDefault'],
          ...route.meta,
          'name': route.name,
          'params': { ...route.params },
          'query': { ...route.query }
        }
        this.$store.state.contentTabs = this.$store.state.contentTabs.concat(tab)
      }
      this.$store.state.sidebarMenuActiveName = tab.menuId
      this.$store.state.contentTabsActiveName = tab.name
    },
    // 获取当前管理员信息
    getUserInfo () {
      return this.$http.get('/sys/user/info').then(({ data: res }) => {
        if (res.code !== 0) {
          return this.$message.error(res.msg)
        }
        this.$store.state.user.id = res.data.id
        this.$store.state.user.name = res.data.username
        this.$store.state.user.superAdmin = res.data.superAdmin
      }).catch(() => {})
    },
    // 获取权限
    getPermissions () {
      return this.$http.get('/sys/menu/permissions').then(({ data: res }) => {
        if (res.code !== 0) {
          return this.$message.error(res.msg)
        }
        window.SITE_CONFIG['permissions'] = res.data
      }).catch(() => {})
    }
  }
}
</script>
