<template>
  <nav class="aui-navbar" :class="`aui-navbar--${$store.state.navbarLayoutType}`">
    <div class="aui-navbar__header">
      <h1 class="aui-navbar__brand" @click="$router.push({ name: 'home' })">
        <a class="aui-navbar__brand-lg" href="javascript:;">{{ $t('brand.lg') }}</a>
        <a class="aui-navbar__brand-mini" href="javascript:;">{{ $t('brand.mini') }}</a>
      </h1>
    </div>
    <div class="aui-navbar__body">
      <el-menu class="aui-navbar__menu mr-auto" mode="horizontal">
        <el-menu-item index="1" @click="$store.state.sidebarFold = !$store.state.sidebarFold">
          <svg class="icon-svg aui-navbar__icon-menu aui-navbar__icon-menu--switch" aria-hidden="true"><use xlink:href="#icon-outdent"></use></svg>
        </el-menu-item>
        <el-menu-item index="2" @click="refresh()">
          <svg class="icon-svg aui-navbar__icon-menu aui-navbar__icon-menu--refresh" aria-hidden="true"><use xlink:href="#icon-sync"></use></svg>
        </el-menu-item>
      </el-menu>
      <el-menu class="aui-navbar__menu" mode="horizontal">

<!--        <el-menu-item index="2">-->
<!--          <a href="https://gitee.com/renrenio/renren-security" target="_blank">-->
<!--            <svg class="icon-svg aui-navbar__icon-menu" aria-hidden="true"><use xlink:href="#github"></use></svg>-->
<!--          </a>-->
<!--        </el-menu-item>-->
        <el-menu-item index="4" @click="fullscreenHandle()">
          <svg class="icon-svg aui-navbar__icon-menu" aria-hidden="true"><use xlink:href="#icon-fullscreen"></use></svg>
        </el-menu-item>
        <el-menu-item index="5" class="aui-navbar__avatar">
          <el-dropdown placement="bottom" :show-timeout="0">
            <span class="el-dropdown-link">
              <img src="~@/assets/img/avatar.png">
              <span>{{ $store.state.user.name }}</span>
              <i class="el-icon-arrow-down"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="updatePasswordHandle()">{{ $t('updatePassword.title') }}</el-dropdown-item>
              <el-dropdown-item @click.native="logoutHandle()">{{ $t('logout') }}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
      </el-menu>
    </div>
    <!-- 弹窗, 修改密码 -->
    <update-password v-if="updatePasswordVisible" ref="updatePassword"></update-password>
  </nav>
</template>
<script>
import screenfull from 'screenfull'
import UpdatePassword from './main-navbar-update-password'
import { clearLoginInfo } from '@/utils'
export default {
  inject: ['refresh'],
  data () {
    return {
      updatePasswordVisible: false,
      messageTip: false
    }
  },
  components: {
    UpdatePassword
  },
  methods: {
    // 全屏
    fullscreenHandle () {
      if (!screenfull.enabled) {
        return this.$message({
          message: this.$t('fullscreen.prompt'),
          type: 'warning',
          duration: 500
        })
      }
      screenfull.toggle()
    },
    // 修改密码
    updatePasswordHandle () {
      this.updatePasswordVisible = true
      this.$nextTick(() => {
        this.$refs.updatePassword.init()
      })
    },
    // 退出
    logoutHandle () {
      this.$confirm(this.$t('prompt.info', { 'handle': this.$t('logout') }), this.$t('prompt.title'), {
        confirmButtonText: this.$t('confirm'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.$http.post('/logout').then(({ data: res }) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg)
          }
          clearLoginInfo()
          this.$router.push({ name: 'login' })
        }).catch(() => {})
      }).catch(() => {})
    }
  }
}
</script>
