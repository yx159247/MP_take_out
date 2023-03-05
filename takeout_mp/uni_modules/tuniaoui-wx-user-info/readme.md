
# 使用说明

使用方法：

```
<template>
	<view class="login-page">
    <!-- 授权登录按钮 -->
    <view class="submit-btn"  @tap.stop="openAuthorizationModal">
      授权登录
    </view>
    
    
    <wx-user-info-modal
      v-model="showAuthorizationModal"
      @updated="updatedUserInfoEvent"
    ></wx-user-info-modal>
	</view>
</template>

<script>
  import WxUserInfoModal from '@/uni_modules/tuniaoui-wx-user-info/components/tuniaoui-wx-user-info/tuniaoui-wx-user-info.vue'
  
	export default {
    components: { WxUserInfoModal },
		data() {
			return {
				showAuthorizationModal: false
			}
		},
		onLoad() {

		},
		methods: {

      // 打开获取用户信息弹框
      openAuthorizationModal() {
        this.showAuthorizationModal = true
      },
      
      // 获取到的用户信息
      updatedUserInfoEvent(info) {
        console.log('获取到的用户信息', info)
      },
      
      
		}
	}
</script>

<style>
	.login-page {
    width: 100vw;
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  /* 授权按钮 */
  .submit-btn {
    width: 100%;
    background-color: #05C160;
    color: #FFFFFF;
    margin-top: 60rpx;
    border-radius: 10rpx;
    padding: 25rpx;
    font-size: 32rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 30rpx;
  }
</style>

```

参数说明：

| 参数       | 说明                                                 |
| :-------- | ---------------------------------------------------- |
| v-model   | 弹出、关闭设置用户信息弹框                               |

