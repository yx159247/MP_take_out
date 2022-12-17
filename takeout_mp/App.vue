<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "@/uni_modules/uview-ui/index.scss";
</style>
<script>
	import {
		getBaseUrl,
		requestUtil,
		getWxLogin,
		getUserProfile,
	} from 'utils/requestUtils';
	import regeneratorRuntime from 'lib/runtime/runtime.js';
	export default {
		globalData: {
			QiNiuYunUrl: 'http://192.168.65.100:9000/images/'
		},
		methods: {
			async wxLogin(loginParam) {
				const res = await requestUtil({
					url: "mp/login",
					data: loginParam,
					method: "post"
				});

				console.log("code:" + res.code)
				if (res.code === 0) {
					console.log(res.data)
					wx.setStorageSync('token', res.data.token)
					wx.setStorageSync('userId', res.data.userId)
					if (res.data.phone == null) {
						this.show = true
						uni.hideTabBar({
							animation: true
						})
					}
					wx.setStorageSync('phoneNumber', res.data.phone)
					uni.$showMsg("登录成功")
					
				}
			},
			async checkLogin() {

				var that = this;
				Promise.all([getWxLogin(), getUserProfile()]).then((res) => {

					console.log(res)

					let loginParam = {
						code: res[0].code,
						nickName: res[1].userInfo.nickName,
						avatarUrl: res[1].userInfo.avatarUrl
					}
					console.log(loginParam)


					that.wxLogin(loginParam);
					

				})
				return 1

			},
		},

		onLaunch: function() {
			console.log('App Launch')
			uni.clearStorage('userInfo')
			uni.clearStorage('userId')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/*每个页面公共css */
</style>
