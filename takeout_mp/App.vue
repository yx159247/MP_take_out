<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "@/uni_modules/uview-ui/index.scss";
	
	@import "@/uni_modules/uview-ui/libs/css/flex.scss";
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

