<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "@/uni_modules/uview-ui/index.scss";
</style>
<script>
	import { getBaseUrl, requestUtil, getWxLogin, getUserProfile, } from 'utils/requestUtils';
	import regeneratorRuntime from 'lib/runtime/runtime.js';
	export default {
		globalData:{
			QiNiuYunUrl:'http://192.168.65.100:9000/images/'
		},
		methods:{
			async wxLogin(loginParam) {
			        const res = await requestUtil({ url: "/user/wxLogin", data: loginParam, method: "post" });
			        
					console.log("code:"+res.code)
			        if (res.code === 1) {
						console.log(res.data)
			            wx.setStorageSync('token', res.data)
			        }
			    },
			    checkLogin() {
			
			        var that = this;
			        wx.showModal({
			            title: '提示',
			            content: '请先完成登录！',
			            success(res) {
			                if (res.confirm) {
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
			
			                } else if (res.cancel) {
			                    console.log('用户点击取消')
			                }
			
			            }
			        })
			
			    },
		},
		
		onLaunch: function() {
			console.log('App Launch')			
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
