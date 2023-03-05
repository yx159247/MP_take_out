<template>
	<view>
		<view class="nickname">
			<u-input
		placeholder="请输入身份证号"
		clearable
		border="none"
		v-model="idNumber"
		></u-input>
		</view>
		<view class="submit">
			<u-button type="warning" text="确定" @click="onsubmit" color="#feca50"></u-button>
		</view>
				
	</view>
</template>

<script>

import {updateUserInfo} from'../../../api/sendCode.js'
	export default {
		data() {
			return {
				idNumber : '',
			}
		},
		methods: {
			validateIdNumber(idNumber) {
			  // 身份证号码的正则表达式
			  const idNumberRegex = /(^\d{15}$)|(^\d{17}([0-9]|X)$)/;
			  
			  // 判断身份证号码是否符合格式要求
			  if (!idNumberRegex.test(idNumber)) {
			    return false;
			  }
			  
			  // 计算校验位
			  const coefficients = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
			  const checkCodes = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
			  let sum = 0;
			  for (let i = 0; i < coefficients.length; i++) {
			    sum += coefficients[i] * parseInt(idNumber.charAt(i), 10);
			  }
			  const checkCodeIndex = sum % 11;
			  const checkCode = checkCodes[checkCodeIndex];
			  
			  // 判断校验位是否正确
			  if (checkCode !== idNumber.charAt(17).toUpperCase()) {
			    return false;
			  }
			  
			  return true;
			},
			onsubmit(){
				let _this = this
				let httpData = {
					idNumber : this.idNumber
				}
				if (this.validateIdNumber(this.idNumber)){
					updateUserInfo(httpData).then(res =>{
						uni.showLoading({
								title: '加载中'
							})
						if (res.code === 0){
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
				} else {
					uni.$u.toast('身份证不合法')
				}
				
				
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
