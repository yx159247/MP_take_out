<template>
	<view>
		<view class="text">
			<text>我们已经发送了校验码到你的手机：</text>
		</view>
		<view class="phone">
			<text>{{formattedPhoneNumber}}</text>
		</view>
		<view class="u-demo-block__content" style="margin-top: 15px;   padding-left: 30rpx;padding-right: 30rpx;">

			<u-input border="bottom" placeholder="请输入短信校验码" v-model="code">


				<template slot="suffix">
					<u-code ref="uCode" @change="codeChange" seconds="60" changeText="X秒重新获取"></u-code>
					<u-button @tap="getCode" :text="tips" type="warning" size="normal"></u-button>
				</template>

			</u-input>

		</view>
		<view class="submit">
			<u-button color="#feca50" type="warning" text="确定" @click="onSubmit"></u-button>
		</view>

	</view>
</template>

<script>
	import {
		sendValidateCodeApi
	} from "../../../api/my.js"
	import regeneratorRuntime from '../../../lib/runtime/runtime.js';
	import {updateUserInfo} from '../../../api/sendCode.js'
	export default {
		data() {
			return {
				phoneNumber: '',
				tips: '',
				value: '',
				code : '',
			}
		},
		onLoad(option) {
			console.log("onLoad")
			console.log('phone',option.phoneNumber)
			this.phoneNumber = option.phoneNumber
			if (this.phoneNumber.length >= 11){
				this.getCode()
			}
		},
		computed: {
			formattedPhoneNumber() {
				let formatted = ''
				if (this.phoneNumber.length >= 11) {
					formatted = this.phoneNumber.slice(0, 3) + '****' + this.phoneNumber.slice(7)
				}
				return formatted
			}
		},

		methods: {
			codeChange(text) {
				this.tips = text;
			},
			getCode() {
				if (this.$refs.uCode.canGetCode) {
					// 模拟向后端请求验证码
					let httpData = {
						phone : this.phoneNumber
					}
					// 获取验证码接口
					sendValidateCodeApi(httpData).then(res =>{
						uni.showLoading({
								title: '正在获取验证码'
							})
						if (res.code === 0) {
							setTimeout(() => {
								uni.hideLoading();
								// 这里此提示会被this.start()方法中的提示覆盖
								uni.$u.toast('验证码已发送');
								// 通知验证码组件内部开始倒计时
								this.$refs.uCode.start();
							}, 1250);
						} else {
							uni.$u.toast('获取验证码失败，请重试');
						}
					})
					
				} else {
					uni.$u.toast('倒计时结束后再发送');
				}
			},
			change(e) {
				console.log('change', e);
			},
			onSubmit(){
				let _this = this
				let httpData = {
					phone : this.phoneNumber,
					code  : this.code
				}
				updateUserInfo(httpData).then(res =>{
					uni.showLoading({
							title: '加载中'
						})
					if (res.code === 0){
						wx.setStorageSync('phoneNumber',_this.phoneNumber)
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
							delta: 2
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
	page {}

	.text {

		color: #6f6f6f;
		font-size: 25rpx;
		text-align: center;
		margin: 10rpx;
	}

	.phone {
		text-align: center;
		margin: 10rpx;
		font-size: 35rpx;
	}

	.submit {
		border-radius: 12rpx;
		margin: 30rpx 30rpx 30rpx 30rpx;
	}
</style>
