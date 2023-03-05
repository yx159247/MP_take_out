<template>
	<view>
		<view class="nickname">
			<u-input
		placeholder="请输入手机号"
		clearable
		border="none"
		v-model="phoneNumber"
		></u-input>
		</view>
		<view class="submit">
			<u-button color="#feca50"  type="warning" text="下一步" @click="toNext"></u-button>
		</view>
				
	</view>
</template>

<script>
	export default {
		data() {
			return {
				phoneNumber : '',
			}
		},
		methods: {
			toNext(item){
				if (this.phoneNumber == '') {
				    uni.showToast({
				    	title: '请输入手机号~',
				    	icon: 'none'
				    });
					return;
				}
				const phoneRegular = /^(13[0-9]{9})|(15[0-9]{9})|(17[0-9]{9})|(18[0-9]{9})|(19[0-9]{9})$/;
				if (!phoneRegular.test(this.phoneNumber)) {
				    uni.showToast({
				    	title: '手机号格式不正确~',
				    	icon: 'none'
				    });
					return;
				}
				let _this = this
				uni.$emit('getPhone',{data : this.phoneNumber})
				uni.navigateTo({
					url: '/pages/setting/sendCode/sendCode?phoneNumber=' + _this.phoneNumber,
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
		margin: 20rpx;
		border-radius: 12rpx;
	}
	.submit{
		border-radius: 12rpx;
		margin: 20rpx 20rpx 20rpx 20rpx;
	}

</style>
