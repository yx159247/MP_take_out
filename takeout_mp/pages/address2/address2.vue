<template>
	<view>
		<view class="address">
			<view class="divContent" :style="{height: windowheight+'px'}">
				<u-empty :show="show" text="地址为空" mode="data" marginTop="50%"
					icon="http://cdn.uviewui.com/uview/empty/data.png">
				</u-empty>
				<view class="divItem" v-for="(item,index) in addressList" :key="index">
					<view class="divAddress">
						<text
							:class="{spanNone:item.label === '无',spanCompany:item.label === '公司',spanHome:item.label === '家',spanSchool:item.label === '学校'}">{{item.label}}</text>
						{{item.detail}}
					</view>
					<view class="divUserPhone">
						<text>{{item.consignee}}</text>
						<text>{{item.gender === 0 ? '先生' : '女士'}}</text>
						<text>{{item.phone}}</text>
					</view>
					<image src="../../static/images/edit.png" @click.stop.prevent="toAddressEditPage(item)" />
					<view class="divSplit"></view>
					<view class="divDefault">
						<image src="../../static/images/checked_true.png" v-if="item.isDefault === 1">
							<image src="../../static/images/checked_false.png"
								@click.stop.prevent="setDefaultAddress(item)" v-else>设为默认地址
					</view>
				</view>
			</view>
			<view class="divBottom" @click="toAddressCreatePage">+ 添加收货地址</view>

		</view>

		</u-empty>
	</view>
</template>

<script>
	import {
		getBaseUrl,
		requestUtil,
		getWxLogin,
		getUserProfile
	} from '../../utils/requestUtils';
	import regeneratorRuntime, {
		async
	} from '../../lib/runtime/runtime';
	import {
		setDefaultAddressApi,
		addressFindOneApi,
		updateAddressApi,
		addAddressApi,
		deleteAddressApi,
		addressListApi
	} from '../../api/address.js'
	export default {
		data() {
			return {
				show: false,
				addressList: [

				],
				windowheight:0
			}
		},
		computed: {},
		created() {

		},
		mounted() {},
		onLoad() {
			let _this=this
			console.log('init')
			uni.getSystemInfo({
				success(res) {
					console.log(res)
					_this.windowheight=res.windowHeight
				}
			})
		},
		onShow() {
			this.initData()
		},
		methods: {
			toAddressEditPage(item) {
				uni.navigateTo({
					url: "/pages/addressEdit2/addressEdit2?id=" + item.id,

				})
			},
			toAddressCreatePage() {
				uni.navigateTo({
					url: '/pages/addressEdit/addressEdit'
				})

			},
			async initData() {
				this.show = false
				const res = await addressListApi()
				if (res.code === 0) {
					this.addressList = res.data
					if (this.addressList.length < 1) {
						this.show = true
					}
				} else {
					return uni.$showMsg(res.msg)
				}
			},
			async setDefaultAddress(item) {
				if (item.id) {
					const res = await setDefaultAddressApi({
						id: item.id
					})
					if (res.code === 0) {
						this.initData()
					} else {
						return uni.$showMsg(res.msg)
					}
				}
			},
			itemClick(item) {

				let pages = getCurrentPages()
				let prevPage = pages[pages.length - 2]
				console.log(prevPage)
				prevPage.setData({
					address: item
				})
				// prevPage.data.address.consignee = item.consignee
				// prevPage.data.address.detail = item.detail
				// prevPage.data.address.phone = item.phone
				// prevPage.data.address.gender = item.gender
				//prevPage.data.address = item
				//表示是从订单页面跳转过来的

				uni.navigateBack()
			},
			async setDefaultAddress(item) {
				if (item.id) {
					const res = await setDefaultAddressApi({
						id: item.id
					})
					if (res.code === 0) {
						this.initData()
					} else {
						return uni.$showMsg(res.msg)
					}
				}
			},

		}
	}
</script>
<style>
	page{
		background-color:  #f3f2f7;
	}
</style>
<style>
	@import url(../address/address.css);
</style>
