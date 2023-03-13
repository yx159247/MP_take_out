<template>
	<view>
		<view class="nickname">
			<u-input
		placeholder="请输入昵称"
		clearable
		border="none"
		v-model="nickName"
		></u-input>
		</view>
		<view class="submit">
			<u-button  color="#feca50" type="warning" text="确定" @click="onsubmit"></u-button>
		</view>
				
	</view>
</template>

<script>

import {updateUserInfo} from'../../../api/sendCode.js'
	export default {
		data() {
			return {
				nickName : '',
			}
		},
		methods: {
			onsubmit(){
				let _this = this
				let httpData = {
					nickName : this.nickName
				}
				updateUserInfo(httpData).then(res =>{
					uni.showLoading({
							title: '加载中'
						})
					if (res.code === 0){
						let userInfo = wx.getStorageSync('userInfo')
						userInfo.nickName = _this.nickName
						wx.setStorageSync('userInfo',userInfo)
						setTimeout(() => {
							uni.hideLoading();
							// 这里此提示会被this.start()方法中的提示覆盖
							uni.showToast({
								icon:"success",
								title:'修改成功'
							})
							
						}, 500);
						setTimeout(() =>{
							uni.navigateBack({
							delta: 1
						})
						},1000)
						
					} else {
						uni.$u.toast(res.msg == 'token不能为空'? '未登录' : res.msg)
					}
				})
				
			}
		}
	}
</script>

<style lang="scss" scoped>

	.nickname{
		height: 72rpx;
		display: flex;
		background-color: white;
		margin-top: 20rpx;
		padding-left: 20rpx;
		padding-right: 20rpx;
		border-radius: 12rpx;
	}
	.submit{
		border-radius: 12rpx;
		margin: 20rpx 20rpx 20rpx 20rpx;
	}

</style>
