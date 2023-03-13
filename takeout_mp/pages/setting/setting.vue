<template>
	<view>
		<view class="card">
			<view class="avatarUrl">
				<button type="balanced" open-type="chooseAvatar" @chooseavatar="onChooseavatar">
					<image :src="avatarUrl" class="refreshIcon"></image>
				</button>
			</view>

			<u-cell-group>
				<u-cell title="姓名" :value="name" isLink="true" url="/pages/setting/changeName/changeName"></u-cell>
				<u-cell title="身份证号" :value="idNumber" isLink="true" url="/pages/setting/changeIdNumber/changeIdNumber"></u-cell>
				<u-cell title="手机号" :value="phoneNumber" isLink="true" url="/pages/setting/changePhone/changePhone">
				</u-cell>
				<u-cell title="昵称" :value="nickName" isLink="true" url="/pages/setting/setNickName/setNickName">
				</u-cell>
				<u-cell title="性别" :value="sex" @click='selectSex' isLink="true"></u-cell>
			</u-cell-group>
		</view>

	</view>
</template>

<script>
	import {
		updateUserInfoApi
	} from '../../api/my.js'
	import {
		getUserInfoApi
	} from '../../api/setting.js'
	import instance from '../../utils/request.js'
	const gender = ['男', '女', '保密']
	export default {
		data() {
			return {
				avatarUrl: '',
				headImg: '/static/logo.png',
				sex: '男',
				nickName: '',
				phoneNumber: '',
				name : '',
				idNumber : ''
			}
		},
		onLoad() {
			
		},
		onShow() {
			this.getUserInfo()
		},
		methods: {

			getUserInfo() {
				let _this = this
				let httpData = {
					userId: wx.getStorageSync('userId')
				}
				getUserInfoApi(httpData).then(res => {
					uni.showLoading({
						title: '加载中'
					})
					if (res.code === 0) {
						setTimeout(() => {
							uni.hideLoading();
							// 这里此提示会被this.start()方法中的提示覆盖
							_this.phoneNumber = res.data.phone
							_this.avatarUrl = res.data.avatarUrl
							_this.nickName = res.data.nickName
							_this.name = res.data.name,
							_this.idNumber = res.data.idNumber.replace(/^(.{6}).*(.{4})$/, '$1********$2')
							_this.sex = gender[res.data.gender]
						}, 500);

					} else {
						uni.$u.toast(res.msg == 'token不能为空'? '未登录' : res.msg)
					}
				})
			},
			selectSex() {
				const arr = ['男', '女', '保密']
				let _this = this
				uni.showActionSheet({
					itemList: arr,
					success: (res) => {
						let tapIndex = res.tapIndex
						let httpData = {
							gender: tapIndex
						}
						updateUserInfoApi(httpData).then(res => {
							uni.showLoading({
								title: '加载中'
							})
							if (res.code === 0) {
								setTimeout(() => {
									uni.hideLoading();
									// 这里此提示会被this.start()方法中的提示覆盖
									_this.sex = arr[tapIndex]
								}, 500);

							} else {
								uni.$u.toast(res.msg == 'token不能为空'? '未登录' : res.msg)
							}
						})


					},
					fail: function(res) {
						console.log(res.errMsg);
					}
				});
			},
			onChooseavatar(e) {
				console.log(e)
				//该图片需要上传到自己服务器--此处没做处理
				let _this = this;
				uni.showLoading({
					title: '加载中'
				});
				uni.uploadFile({
					url: instance().baseURL + 'mp/oss/upload',
					filePath: e.target.avatarUrl,
					name: 'file',
					header: {
						"token": wx.getStorageSync('token')
					},
					success: (res) => {
						console.log('上传成功')
						console.log(res)
						// 注意：这里返回的uploadFileRes.data 为JSON 需要自己去转换
						let data = JSON.parse(res.data);
						if (data.code === 0) {
							let httpData = {
								avatarUrl : data.data.src
							}
							updateUserInfoApi(httpData).then(res =>{
								if (res.code === 0){
									
									_this.avatarUrl = data.data.src;
									let userInfo = wx.getStorageSync('userInfo')
									userInfo.avatarUrl = _this.avatarUrl
									wx.setStorageSync('userInfo',userInfo)
									uni.showToast({
										icon:'success',
										title:'上传成功',
										duration: 500
									});
								} else{
									uni.$u.toast(res.msg == 'token不能为空'? '未登录' : res.msg)
								}
							})
							
						}

					},
					fail: (error) => {
						uni.showToast({
							title: error,
							duration: 2000
						});
					},
					complete: () => {
						uni.hideLoading();
					}
				})
			}
		},
	}
</script>

<style lang="scss" scoped>
	.card {
		border-radius: 12rpx;
		background: #ffffff;
		height: 100vh;

		.avatarUrl {
			padding: 40rpx 0 20rpx;
			background: #fff;

			button::after {
				border: initial;
			}

			button {
				background: #fff;
				line-height: 80rpx;
				height: auto;
				width: auto;
				padding: 20rpx 30rpx;
				margin: 0;
				display: flex;
				justify-content: center;
				align-items: center;

				.refreshIcon {
					width: 160rpx;
					height: 160rpx;
					border-radius: 50%;
				}

				.jt {
					width: 14rpx;
					height: 28rpx;
				}
			}

		}

		.head {

			align-items: center;
			padding: 30rpx !important;
			display: flex;
			flex-direction: column;


		}
	}
</style>
