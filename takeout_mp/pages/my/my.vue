<template>
	<view>
		<view class="user">
			<!-- 			<view class="divHead">

				<u-avatar size="60" shape="circle" :src="user.avatarUrl"></u-avatar>



				<view class="divUserName" v-if="user">
					<u-text :text="user.nickName" bold="true"></u-text>
				</view>
				
				<view class="divLogin" v-else>
						<text style="margin-left: 0;padding-left: 10rpx;font-size: 15px;"
						@click="toLogin"
						>登录/注册</text>
						</view>
				<view class="userPhone">
					<u-text :text="phoneNumber"></u-text>
				</view>
				<u-modal :show="show" :content='content'>
					<view style="font-size: 36rpx;text-align: center;">绑定手机号
						<view style="color: lightgray; font-size: 28rpx;text-align: center">请先绑定手机号再进行此操作</view>
					</view>
					<u-button openType="getPhoneNumber" @getphonenumber="confirm" type="success" slot="confirmButton"
						shape="circle" text="微信用户一键绑定"></u-button>

				</u-modal>
				<view class="shezhi">
					<image src="/static/me/xitongshezhi.png"></image>
				</view>
			</view> -->

			<view class="headBox">

				<!-- 登录 -->
				<view class="u-flex u-p-l-30 u-p-r-20 u-p-t-30 u-p-b-30">
					<block v-if="userToken">
						<view class="u-m-r-20">
							<image class="avatar" mode="aspectFill" :src="user.avatarUrl || '/static/logo.png'">
							</image>
						</view>
						<view class="u-flex-1" @click="onJump">
							<view class="nickName u-flex">
								<view class="name u-m-r-10" style="color: #423e3e"
									v-if="user.nickName || phoneUserName">{{user.nickName || phoneUserName}}</view>
								<view class="placardVip">美食元素</view>
							</view>
							<view class="detail" v-if="phoneNumber">手机号：{{phoneNumber}}</view>
							<view class="detail" v-else>手机号:未绑定</view>
						</view>
					</block>
					<block v-else>
						<view class="u-m-r-20">
							<view class="avatar u-flex" style="justify-content: center;">
								<u-icon name="account-fill" color="#fff" size="30"></u-icon>
							</view>
						</view>
						<view class="u-flex-1" @click="openLogin">
							<view class="u-font-lg" style="color: #423e3e;font-weight: bold;">登录/注册</view>
							<view class="detail" style="color: #423e3e">登录后享受更好的服务体验</view>
						</view>
					</block>
					<view>
						<u-icon v-if="userToken" name="arrow-right" color="#fff" size="13"></u-icon>
					</view>
				</view>


			</view>


			<scroll-view scroll-y="true" :style="{height: wh-75 + 'px'}">


				<view class="divContent">
					<view class="divLinks">
						<view @click="toAddress" class="item">
							<image src="../../static/index_image/dingwei.png"></image>
							<text>我的地址</text>
							<view>
								<u-icon name="arrow-right"></u-icon>
							</view>
						</view>
						<view class="divSplit"></view>
						<view @click="toOrderList" class="item">
							<image src="../../static/me/dingdan.png"></image>
							<text>历史订单</text>
							<view>
								<u-icon name="arrow-right"></u-icon>
							</view>
						</view>


					</view>
					<view class="divOrders" v-if="flag && user">
						<view class="title">最新订单</view>
						<view class="timeStatus">
							<text>{{order[0].orderTime}}</text>
							<text>{{getStatus(order[0].status)}}</text>
							<!-- <text>正在派送</text> -->
						</view>
						<view class="dishList">
							<view v-for="(item,index) in order[0].orderDetails" :key="index" class="item">
								<text>{{item.name}}</text>
								<text>x{{item.number}}</text>
							</view>
						</view>
						<view class="result">
							<text>共{{order[0].sumNum}} 件商品,实付</text>
							<text class="price">￥{{order[0].amount}}</text>
						</view>
						<view class="btn" v-if="order[0].status === 4">
							<view class="btnAgain" @click="addOrderAgain">再来一单</view>
						</view>
					</view>
					<view v-if="userToken" class="foot">
						<view @click="logout" class="logout">
							退出登录
						</view>
					</view>
				</view>


			</scroll-view>
		</view>

		<view>
			<u-popup :show="loginPopupShow" mode="bottom" :round="10" @close="closeLogin" zIndex="999998">
				<view class="f__login">
					<view class="loginLoading" v-if="isLoading">
						<u-loadmore status="loading" loadingText="正在登录..."></u-loadmore>
					</view>
					<!--                <view class="logo">
		                    <image class="img" src="/static/logo.png"></image>
		                </view> -->
					<view class="title">欢迎登录~</view>
					<view class="text">会员用户登录后，享受更好的服务体验</view>
					<view class="loginButton" v-if="!isPhoneLogin">
						<!-- #ifdef MP-WEIXIN -->
						<!-- <button class="button" @click="login" :style="{background:PrimaryColor}">微信授权登录</button> -->
						<button class="button marginT" open-type="getPhoneNumber" @getphonenumber="confirm"
							:style="{background:PrimaryColor}">微信一键登录</button>
						<!-- #endif -->
						<button class="button" @click="isPhoneLogin = !isPhoneLogin"
							style="background:#fff;margin-top:24rpx;"
							:style="{border:'2rpx solid '+PrimaryColor,color:PrimaryColor}">
							使用手机号登录/注册
						</button>
					</view>
					<!-- 验证码登录 -->
					<view class="loginPhone" v-if="isPhoneLogin">

						<view class="form-row">
							<input class="input" type="number" v-model="phone" placeholder="请输入手机号码"
								placeholder-style="font-weight:normal;color:#bbbbbb;"></input>
						</view>
						<view class="form-row">
							<input class="input" type="number" v-model="vCode" placeholder="请输入验证码"
								placeholder-style="font-weight:normal;color:#bbbbbb;"></input>
							<view class="getvcode" :class="{forhidden:readonly}" @click="getVcode">{{ codeText }}</view>
						</view>
						<button class="submit" size="default" @click="onSubmit"
							:style="{background:PrimaryColor}">确定</button>
						<!-- #ifdef MP-WEIXIN -->
						<view class="tips">
							<view @click="isPhoneLogin = !isPhoneLogin" class="goBuy" :style="{color:PrimaryColor}">
								{{isPhoneLogin?'快速登录':'手机号登录'}}
							</view>
						</view>
						<!-- #endif -->
						<!-- #ifdef H5 -->
						<view class="tips">
							<view @click="isPhoneLogin = !isPhoneLogin" class="goBuy" :style="{color:PrimaryColor}">
								返回
							</view>
						</view>
						<!-- #endif -->

					</view>


				</view>

			</u-popup>

			<view>
				<u-popup :show="showWxLogin" mode="bottom" round="44rpx" :closeable="true" @close="onWXClose">
					<view class="popupBox">
						<view class="h2 bold">获取您的昵称、头像</view>
						<view class="h3">获取用户头像、昵称，主要用于向用户提供具有辨识度的用户中心界面</view>
						<view class="form">
							<view class="input u-flex">
								<view class="h2">头像</view>
								<view class="info">
									<button class="avatar-wrapper" open-type="chooseAvatar"
										@chooseavatar="onChooseavatar">
										<image class="avatar" :src="avatarUrl || '../../static/head.png'"></image>
									</button>
								</view>
								<u-icon name="arrow-right" size="32rpx" color="#999"></u-icon>
							</view>
							<view class="input u-flex">
								<view class="h2">昵称</view>
								<view class="info">
									<input type="nickname" v-module="nickName" placeholder="请输入昵称" @blur="onNickname" />
								</view>
							</view>
						</view>
						<view class="submit" :style="{background:PrimaryColor}" @tap.stop="WxgetUserProfile">确定</view>
					</view>
				</u-popup>
			</view>
			<wx-user-info-modal v-model="showAuthorizationModal" @updated="updatedUserInfoEvent">

			</wx-user-info-modal>

			<view>
				<u-modal @cancel="logoutCancel" @confirm="logoutConfirm" :show="logoutshow" :title="logoutTitle" :content='logoutContent'
					:showCancelButton='true' confirmColor='#feca50'></u-modal>
				<!-- <u-button @click="show = true">打开</u-button> -->
			</view>

		</view>


	</view>

</template>

<script>
	var clear;
	import WxUserInfoModal from '@/uni_modules/tuniaoui-wx-user-info/components/tuniaoui-wx-user-info/tuniaoui-wx-user-info.vue'
	import {
		addOrderApi,
		orderListApi,
		orderPagingApi,
		orderAgainApi,
		deleteOrderApi,

	} from '../../api/orderList.js'
	import {
		logoutApi,
		updateUserInfoApi,
		phoneLoginApi,
		sendValidateCodeApi
	} from "../../api/my.js"
	import {
		getBaseUrl,
		requestUtil,
		getWxLogin,
		getUserProfile,
	} from '../../utils/requestUtils.js';
	import regeneratorRuntime from '../../lib/runtime/runtime.js';
	export default {
		components: {
			WxUserInfoModal
		},
		data() {
			return {
				logoutshow: false,
				logoutTitle: '确定退出？',
				logoutContent: '退出登录后将无法查看订单，重新登录即可查看',
				showAuthorizationModal: false,
				phoneUserName: '',
				userToken: '',
				showWxLogin: false,
				PrimaryColor: '#1fba1a', //主题色
				avatarUrl: '',
				nickName: '',
				isLoading: false,
				isPhoneLogin: false, //是否显示验证码登录
				readonly: false,
				codeText: '获取验证码',
				phone: '', //号码
				vCode: '', //验证码
				code: '', //uni.login获取的code
				PrimaryColor: '#1fba1a', //主题色
				loginPopupShow: false,
				userId: '',
				content: "微信用户快速登录",
				flag: false,
				phoneNumber: "",
				show: false,
				user: {},
				getPhoneParam: {},
				wh: 0,
				QiNiuYunUrl: getApp().globalData.QiNiuYunUrl,
				imageUrl: '',
				ruleForm: {
					'id': '',
					'phone': '',
					'gender': '男',
					'status': '',
					'avatar': '',
					'idNumber': '',
				},
				form: {
					phone: '',
					code: ''
				},
				msgFlag: false,
				order: [{
					orderTime: '', //下单时间
					status: undefined, //订单状态 1已结账，2未结账，3已退单，4已完成，5已取消
					orderDetails: [{
						name: '', //菜品名称
						number: undefined, //数量
					}], //明细
					amount: undefined, //实收金额
					sumNum: 0, //菜品总数
				}]

			};
		},
		created() {

			this.getUserInfo()
		},
		onShow() {
			this.checkLogin()
			this.getUserInfo()
			this.initData()

		},
		mounted() {
			const sysInfo = uni.getSystemInfoSync()
			this.wh = sysInfo.windowHeight
			this.checkLogin()
			this.getUserInfo()
			this.initData()

		},
		watch: {

		},
		methods: {
			checkLogin() {
				if (uni.getStorageSync('token') == null || uni.getStorageSync('token') == '') {
					// uni.$showMsg("未登录")
					wx.clearStorageSync()
					console.log("ok!!!!")
				}
			},
			initData() {
				if (this.userId) {
					this.getLatestOrder()
				}

			},
			onJump() {
				console.log("点击了跳转")
				uni.navigateTo({
					url: '/pages/setting/setting'
				})
			},
			onAuthorization() {
				this.loginPopupShow = false
				this.showWxLogin = true
			},
			onWXClose() {
				this.showWxLogin = false
				uni.showTabBar({
					animation: true
				})
			},
			onChooseavatar(e) {
				console.log(e)
				//该图片需要上传到自己服务器--此处没做处理
				let _this = this;
				uni.showLoading({
					title: '加载中'
				});
				uni.uploadFile({
					url: getBaseUrl() + 'mp/oss/upload',
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
								avatarUrl: data.data.src
							}
							updateUserInfoApi(httpData).then(res => {
								if (res.code === 0) {

									_this.avatarUrl = data.data.src;
									let userInfo = wx.getStorageSync('userInfo')
									userInfo.avatarUrl = _this.avatarUrl
									wx.setStorageSync('userInfo', userInfo)
									uni.showToast({
										icon: 'success',
										title: '上传成功',
										duration: 500
									});
								} else {
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
			},
			onNickname(e) {
				this.nickName = e.detail.value;
			},
			initPhoneUserName() {
				let num = this.phoneNumber.slice(7)
				this.phoneUserName = '手机用户' + num
			},
			// 手机号登录
			onSubmit() {
				if (this.phone == '') {
					uni.showToast({
						title: '请输入手机号~',
						icon: 'none'
					});
					return;
				}
				const phoneRegular = /^(13[0-9]{9})|(15[0-9]{9})|(17[0-9]{9})|(18[0-9]{9})|(19[0-9]{9})$/;
				if (!phoneRegular.test(this.phone)) {
					uni.showToast({
						title: '手机号格式不正确~',
						icon: 'none'
					});
					return;
				}
				if (this.vCode == '') {
					uni.showToast({
						title: '请输入验证码~',
						icon: 'none'
					});
					return;
				}
				let httpData = {
					phone: this.phone,
					code: this.vCode
				};
				phoneLoginApi(httpData).then(res => {
					if (res.code === 0) {

						let userInfo = {
							avatarUrl: res.data.avatarUrl,
							nickName: res.data.nickName,
							id: res.data.userId
						}
						wx.setStorageSync('token', res.data.token)
						this.userToken = res.data.token
						wx.setStorageSync('userId', res.data.userId)
						wx.setStorageSync('phoneNumber', res.data.phone)
						wx.setStorageSync('userInfo', userInfo)
						this.getUserInfo()
						this.initPhoneUserName()
						this.isPhoneLogin = false
						this.loginPopupShow = false
						this.phone = ''
						this.vCode = ''

						uni.showTabBar({
							animation: true
						})
						setTimeout(() => {
							uni.showToast({
								icon: 'success',
								title: "登录成功"
							})
						}, 500)
						clearInterval(clear);
						this.readonly = false
						this.codeText = '获取验证码';
					} else {
						return uni.$showMsg(res.msg)
					}
				})
			},
			//验证码按钮文字状态
			getCodeState() {
				const _this = this;
				this.readonly = true;
				console.log('getVcode')
				if (this.readonly) {
					uni.showToast({
						title: '验证码已发送~',
						icon: 'none'
					});
				}
				this.codeText = '60S后重新获取';
				var s = 60;
				clear = setInterval(() => {
					s--;
					_this.codeText = s + 'S后重新获取';
					if (s <= 0) {
						clearInterval(clear);
						_this.codeText = '获取验证码';
						_this.readonly = false;
					}
				}, 1000);
			},
			//获取验证码
			getVcode() {
				if (this.phone == '') {
					uni.showToast({
						title: '请输入手机号~',
						icon: 'none'
					});
					return;
				}
				const phoneRegular = /^(13[0-9]{9})|(15[0-9]{9})|(17[0-9]{9})|(18[0-9]{9})|(19[0-9]{9})$/;
				if (!phoneRegular.test(this.phone)) {
					uni.showToast({
						title: '手机号格式不正确~',
						icon: 'none'
					});
					return;
				}
				let httpData = {
					phone: this.phone
				}
				// 获取验证码接口
				sendValidateCodeApi(httpData).then(res => {
					if (res.code === 0) {
						this.getCodeState(); //开始倒计时
					}
				})
			},
			async getLatestOrder() {
				const params = {
					order: "desc",
					orderField: "order_time",
					page: 1,
					limit: 1,
				}
				const res = await orderPagingApi(params)
				if (res.code === 0) {
					if (res.data.list.length != 0) {
						this.flag = true
					}
					this.order = res.data.list
					if (this.order && this.order[0].orderDetails) {
						let number = 0
						this.order[0].orderDetails.forEach(item => {
							number += item.number
						})
						this.order[0].sumNum = number
					}
				} else {
					return uni.$showMsg(res.msg)
				}
			},
			getStatus(status) {
				let str = ''
				switch (status) {
					case 1:
						str = '待付款'
						break;
					case 2:
						str = '正在派送'
						break;
					case 3:
						str = '已派送'
						break;
					case 4:
						str = '已完成'
						break;
					case 5:
						str = '已取消'
						break;

				}
				return str
			},
			toAddress() {
				const token = wx.getStorageSync('token');
				if (token) {
					uni.navigateTo({
						url: '/pages/address2/address2'
					})
				} else {
					uni.showModal({
						title: '提示',
						content: '请登录',
						success: function(res) {
							if (res.confirm) {
								uni.switchTab({
									url: '/pages/my/my'
								});
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					});
				}

			},
			toOrderList() {
				const token = wx.getStorageSync('token');
				if (token) {
					uni.navigateTo({
						url: '/pages/orderList2/orderList2'
					})
				} else {
					uni.showModal({
						title: '提示',
						content: '请登录',
						success: function(res) {
							if (res.confirm) {
								uni.switchTab({
									url: '/pages/my/my'
								});
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					});
				}
			},

			confirm(e) {
				var _this = this
				if (e.detail.errMsg == 'getPhoneNumber:ok') {
					let userInfo = {
						avatarUrl: '',
						nickName: '',
						id: ''
					}
					uni.showLoading({
						title: "加载中"
					})
					wx.login({
						success(res) {
							if (res.code) {
								console.log("登录状态", res)
								let loginParam = {
									code: res.code,
									getPhoneCode: e.detail.code,
									encryptedData: e.detail.encryptedData,
									iv: e.detail.i
								}
								requestUtil({
									url: "mp/login",
									data: loginParam,
									method: "post"
								}).then(res => {
									userInfo = {
										avatarUrl: res.data.avatarUrl,
										nickName: res.data.nickName,
										id: res.data.userId,
									}
									console.log("userInfo", userInfo)
									if (res.code === 0) {
										console.log(res.data.token)
										wx.setStorageSync('token', res.data.token)
										_this.userToken = res.data.token
										wx.setStorageSync('userId', res.data.userId)
										wx.setStorageSync('phoneNumber', res.data.phone)
										wx.setStorageSync('userInfo', userInfo)
										_this.getUserInfo()
										_this.user = userInfo
										// if (!res.data.phone){
										// 	let param = {

										// 	}
										// 	_this.getPhoneNumber(param)
										// } else{
										// 	_this.getUserInfo()
										// 	_this.user = userInfo
										// }
									}


									uni.hideLoading()
									_this.loginPopupShow = false
									uni.showTabBar({
										animation: true
									})
									setTimeout(() => {
										uni.showToast({
											icon: 'success',
											title: "登录成功"
										})
									}, 500)
									if (!userInfo.avatarUrl || !userInfo.nickName) {
										// _this.showWxLogin = true
										_this.showAuthorizationModal = true
										uni.hideTabBar({
											animation: true
										})
									}


								})
							}
						}
					})
				}
				console.log(e.detail)
			},
			async wxLogin(loginParam) {
				const res = await requestUtil({
					url: "mp/login",
					data: loginParam,
					method: "post"
				});

				console.log("code:" + res.code)
				if (res.code === 0) {
					console.log(res.data.token)
					wx.setStorageSync('token', res.data.token)
					this.userToken = res.data.token
					wx.setStorageSync('userId', res.data.userId)
					if (res.data.phone == null) {
						this.show = true
						uni.hideTabBar({
							animation: true
						})
					}
					wx.setStorageSync('phoneNumber', res.data.phone)
					this.getUserInfo()
					this.initData()
				}
			},
			openLogin() {
				this.loginPopupShow = true
				uni.hideTabBar({
					animation: true
				})
			},
			closeLogin() {
				this.loginPopupShow = false
				uni.showTabBar({
					animation: true
				})
			},
			// 获取到的用户信息
			updatedUserInfoEvent(info) {
				let _this = this
				console.log('获取到的用户信息', info)
				uni.showLoading({
					title: '加载中'
				});
				let userInfo = wx.getStorageSync('userInfo')
				userInfo.avatarUrl = info.avatar
				userInfo.nickName = info.nickname
				wx.setStorageSync('userInfo', userInfo)
				_this.getUserInfo()
				let httpData = {
					nickName: info.nickname,
					avatarUrl: info.avatar
				}
				updateUserInfoApi(httpData).then(res => {
					if (res.code === 0) {
						uni.showToast({
							icon: 'success',
							title: '授权成功'
						})
						_this.showAuthorizationModal = false
					} else {
						uni.$u.toast(res.msg == 'token不能为空'? '未登录' : res.msg)
					}
				}).finally(() => {
					uni.hideLoading()
					uni.showTabBar({
						animation: true
					})
				})

			},
			//微信登录接口
			async WxgetUserProfile() {
				let _this = this;
				if (!_this.avatarUrl) {
					uni.showToast({
						title: '请上传头像~',
						icon: 'none'
					});
					return
				}
				if (!_this.nickName) {
					uni.showToast({
						title: '请输入昵称~',
						icon: 'none'
					});
					return
				}
				uni.showLoading({
					title: '加载中'
				});

				wx.getUserProfile({
					desc: '用于完善会员资料',
					success: (res) => {
						//请求后台授权登录接口
						//自己的代码逻辑

						console.log('getUserProfileres', res)

						let param = {
							id: wx.getStorageSync("userId"),
							nickName: _this.nickName,
							avatarUrl: _this.avatarUrl
						}
						wx.setStorageSync('userInfo', param)
						console.log('userParam', param)
						updateUserInfoApi(param).then(res => {
							if (res.code === 0) {
								uni.showToast({
									icon: "success",
									title: "授权成功"
								})

							} else {
								return uni.$showMsg(res.msg)
							}
						})

					},
					fail: err => {
						uni.hideLoading();

					},
					complete() {
						_this.onWXClose()
						_this.getUserInfo()
					}
				})


			},

			login() {

				Promise.all([getWxLogin(), getUserProfile()]).then((res) => {
					console.log(res)
					let loginParam = {
						code: res[0].code,
						nickName: res[1].userInfo.nickName,
						avatarUrl: res[1].userInfo.avatarUrl
					}
					this.getPhoneParam = res[1]
					console.log("user:", this.user)
					console.log(loginParam)
					this.wxLogin(loginParam);
				})
			},
			toLogin() {
				uni.navigateTo({
					url: '/pages/login/login'
				})
			},
			async getPhoneNumber(param) {
				let that = this
				const res = await requestUtil({
					url: "mp/wxGetPhone",
					data: param,
					method: "post"
				})
				if (res.code === 0) {
					this.phone_info = res.data
					console.log("手机号" + this.phone_info)
					let phoneNumber = this.phone_info
					console.log(phoneNumber, "手机号")
					wx.setStorageSync('phoneNumber', phoneNumber)

					this.getUserInfo()
					this.initPhoneUserName()
					this.initData()

				} else {
					return uni.$showMsg(res.msg == 'token不能为空'? '未登录' : res.msg);
				}
			},
			getUserInfo() {
				this.user = wx.getStorageSync('userInfo')
				this.phoneNumber = wx.getStorageSync('phoneNumber').replace(/(\d{3})\d{4}(\d{4})/, "$1****$2")
				this.userToken = wx.getStorageSync('token')
				console.log('用户信息={}', this.user)
			},
			async logout() {
				console.log("userId", wx.getStorageSync('userId'))
				this.logoutshow = true
			},
			async logoutConfirm() {
				const res = await logoutApi({})
				if (res.code === 0) {
					this.logoutshow = false
					setTimeout(() => {
						wx.clearStorageSync()
						this.getUserInfo()
					}, 500)
				} else {
					return uni.$showMsg(res.msg == 'token不能为空'? '未登录' : res.msg);
				}
			},
			logoutCancel(){
				this.logoutshow = false
			}
		},

	}
</script>
<style>
	page{
		background-color:  #f3f2f7;
	}
</style>
<style lang="scss" scoped>
	@import url(./my.css);
	@import '@/styles/common.scss';
	@import "@/styles/iconfont.scss";

	.headBox {
		padding-top: 0rpx;
		background: linear-gradient(to left top, #feca50, #feca50);
		border-radius: 50% / 0 0 5% 5%;
		overflow: hidden;

		.avatar {
			width: 50px;
			height: 50px;
			border-radius: 25px;
			background-color: #ccc;
		}

		.nickName {
			.btn {
				font-size: 22rpx;
				font-weight: normal;
				color: #666;
				background: #fff;
				border-radius: 5rpx;
				height: 45rpx;
				line-height: 45rpx;
				padding: 0 10rpx;
				position: relative;

				.itemButton {
					border-radius: 0;
					text-align: left;
					opacity: 0;
					width: 100%;
					height: 100%;
					position: absolute;
					left: 0;
					top: 0;
				}
			}

			.name {
				color: #fff;
				font-weight: bold;
				font-size: 32rpx;
			}

			.placardVip {
				background: #2a2e44;
				color: #f4d6a1;
				font-size: 22rpx;
				padding: 4rpx 10rpx;
				text-align: center;
				border-radius: 4rpx;
			}

		}

		.detail {
			color: #423e3e;
			font-size: 24rpx;
			padding-top: 6rpx;
		}

		.vipBox {
			padding: 0 24rpx;

			.card {
				background-image: linear-gradient(to right, #314177, #202646);
				padding: 16rpx 32rpx 24rpx 32rpx;
				border-top-left-radius: 30rpx;
				border-top-right-radius: 30rpx;
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				align-items: center;

				.left {
					display: flex;
					flex-direction: row;
					align-items: center;

					.title {
						font-size: 40rpx;
						font-weight: bold;
						font-style: italic;
						color: #f9bd90;
					}

					.tips {
						font-size: 24rpx;
						color: #f9bd90;
						margin-left: 20rpx;
					}

				}

				.right {
					.button {
						padding: 8rpx 16rpx;
						color: 212849;
						border-radius: 30rpx;
						background: #f9bd90;
						font-size: 24rpx;
					}
				}
			}
		}
	}

	.f__login {
		padding: 48rpx 32rpx;
		border-radius: 18rpx 18rpx 0 0;
		z-index: 99;
		position: relative;

		.loginLoading {
			position: absolute;
			top: 0;
			bottom: 0;
			left: 0;
			right: 0;
			background: rgba(255, 255, 255, .95);
			z-index: 999;
			/* #ifndef APP-NVUE */
			display: flex;
			/* #endif */
			flex-direction: row;
			justify-content: center;
			align-items: center;
		}

		.logo {
			width: 90rpx;
			height: 90rpx;
			border-radius: 18rpx;
			overflow: hidden;

			.img {
				width: 90rpx;
				height: 90rpx;
			}
		}

		.title {
			font-size: 40rpx;
			font-weight: bold;
			margin-top: 24rpx;
		}

		.text {
			font-size: 24rpx;
			color: #666;
			margin-top: 16rpx;
		}

		.loginButton {
			margin-top: 56rpx;

			.button {
				color: #fff;
				width: 100%;
				height: 92rpx;
			}

			.marginT {
				margin-top: 24rpx;
			}
		}

		.tips {
			margin-top: 24rpx;
			/* #ifndef APP-NVUE */
			display: flex;
			/* #endif */
			flex-direction: row;
			justify-content: space-between;
			align-items: center;

			.left {
				/* #ifndef APP-NVUE */
				display: flex;
				/* #endif */
				flex-direction: row;
			}

			.goBuy {
				font-size: 24rpx;
				/* margin-left: 16rpx; */
				background: none;
				/* #ifndef APP-NVUE */
				display: flex;
				/* #endif */
				flex-direction: row;
				justify-content: flex-start;
				padding: 0;
				margin: 0;
				color: #1fba1a;
			}
		}
	}

	.loginPhone {
		.form-row {
			position: relative;
			border-bottom: 1rpx solid #e8e8e8;
			line-height: 1;
			margin-top: 24rpx;

			.input {
				font-size: 34rpx;
				line-height: 102rpx;
				height: 94rpx;
				width: 100%;
				box-sizing: border-box;
				font-size: 30rpx;
				padding: 0;
				font-weight: bold;
			}

			.getvcode {
				font-size: 26rpx;
				height: 80rpx;
				color: #333;
				line-height: 80rpx;
				background: #eee;
				min-width: 188rpx;
				text-align: center;
				border-radius: 8rpx;
				position: absolute;
				top: 50%;
				transform: translateY(-50%);
				right: 0;
				z-index: 11;

				&.forhidden {
					background: #eee;
					color: #cccccc;
				}
			}
		}

		.submit {
			margin-top: 60rpx;
			color: #fff;
			width: 100%;
			border: none;
		}
	}

	.popupBox {
		width: 100%;
		padding: 50rpx 40rpx 30rpx;

		.h2 {
			color: #333;
			font-size: 32rpx;
		}

		.h3 {
			font-size: 26rpx;
			color: #999;
			line-height: 1.4;
			padding-top: 14rpx;
		}

		.bold {
			font-weight: bold;
		}

		.form {
			margin-top: 30rpx;
			border-top: 1px solid #EFEFEF;

			.input {
				padding: 24rpx 0;
				border-bottom: 1px solid #EFEFEF;

				.info {
					flex: 1;
					margin-left: 30rpx;

					.avatar-wrapper {
						width: 70rpx;
						height: 70rpx;
						border-radius: 10rpx;

						.avatar {
							width: 70rpx;
							height: 70rpx;
							border-radius: 10rpx;
						}
					}

					input {
						background: transparent;
						width: 100%;
						height: 70rpx;
						color: #333;
						font-size: 30rpx;
					}
				}
			}
		}
	}

	.submit {
		margin-top: 30rpx;
		width: 100%;
		height: 92rpx;
		border-radius: 10rpx;
		text-align: center;
		line-height: 92rpx;
		color: #fff;
		font-size: 32rpx;
		background: #E83F3C;
	}
</style>
