<template>
	<view>
		<view class="add_order">
			<view class="divContent">
				<view class="divAddress">
					<view @click="toAddressPage">
						<view class="address">
							<view v-if="address.detail.length < 1">
								<text style="color: gainsboro;font-size: 36rpx">请选择收货地址</text>

							</view>
							<view v-else>
								{{address.detail}}
							</view>
							<view class="icon">
								<u-icon name="arrow-right"></u-icon>
							</view>
						</view>

						<view class="name">
							<text
								v-if="address.detail.length > 1">{{address.consignee}}{{address.sex === '1' ? '先生':'女士'}}</text>
							<text>{{address.phone}}</text>


						</view>

					</view>
					<view class="divSplit"></view>
					<view class="divFinishTime">预计{{finishTime}}送达</view>
				</view>
				<view class="order">
					<view class="title">订单明细</view>
					<view class="divSplit"></view>
					<view class="itemList">
						<view class="item" v-for="(item,index) in cartData" :key="index">
							<view class="u-image1">
								<u-image width="128rpx" height="128rpx" :src="imgPathConvert(item.image)">

									<image src="../../static/images/noImg.png" />

								</u-image>
							</view>

							<view class="desc">
								<view class="name">{{item.name}}</view>
								<view class="numPrice">
									<text class="num">x{{item.number}}</text>
									<view class="price">
										<text class="spanMoney">￥</text>{{item.amount * item.number}}
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>
				<view class="note">
					<view class="title">备注</view>
					<u-textarea v-model="note" placeholder="请输入您需要备注的信息" maxlength="50" count border="bottom">
					</u-textarea>
					<van-field v-model="note" rows="2" autosize type="textarea" maxlength="50" placeholder="请输入您需要备注的信息"
						show-word-limit />
				</view>
			</view>
			<view class="divCart">
				<view class="imgCart" v-if="!cartData || cartData.length<1"></view>
				<view class="imgCartActive" v-else></view>

				<view :class="{divGoodsNum:1===1, moreGoods:cartData && cartData.length > 99}"
					v-if="cartData && cartData.length > 0">{{ goodsNum }}</view>
				<view class="divNum">
					<text>￥</text>
					<text>{{goodsPrice}}</text>
				</view>
				<view class="divPrice"></view>
				<view class="cartData.length > 0 ? 'btnSubmitActive' : ''" @click="goToPaySuccess">去支付</view>
			</view>
		</view>
	</view>
</template>

<script>
	import '../../api/index.js'
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

		cartListApi,
		categoryListApi,
		dishListApi,
		setmealListApi,
		clearCartApi,
		updateCartApi,
		setMealDishDetailsApi,
		addCartApi,
	} from '../../api/index';
	import {
		getDefaultAddressApi
	} from '../../api/address.js'
	import {
		addOrderApi
	} from '../../api/addOrder.js'
	export default {
		data() {
			return {
				QiNiuYunUrl: getApp().globalData.QiNiuYunUrl,
				imageUrl: '',
				address: {
					phone: '', //手机号
					consignee: '', //姓名
					detail: '', //详细地址
					sex: '1',
					id: undefined
				},
				finishTime: '', //送达时间
				cartData: [],
				note: '' //备注信息
			}
		},
		onShow() {
			let pages = getCurrentPages();
			let currPage = pages[pages.length - 1]; //当前页面
			let currAddress = currPage.data.address
			this.address = currAddress
		},
		computed: {
			goodsNum() {
				let num = 0
				this.cartData.forEach(item => {
					num += item.number
				})
				if (num < 99) {
					return num
				} else {
					return '99+'
				}
			},
			goodsPrice() {
				let price = 0
				this.cartData.forEach(item => {
					price += (item.number * item.amount)
				})
				return price
			}
		},
		created() {
			this.initData()
			this.getFinishTime()
		},
		mounted() {},
		methods: {
			goBack() {
				history.go(-1)
			},
			initData() {
				//获取默认的地址
				this.defaultAddress()
				//获取购物车的商品
				this.getCartData()
			},
			//获取默认地址
			async defaultAddress() {
				const res = await getDefaultAddressApi()
				if (res.code === 1) {
					this.address = res.data

				} else {

					// 	uni.navigateTo({
					// 		url:'/pages/addressEdit/addressEdit'

					// 	})

				}
			},
			//获取送达时间
			getFinishTime() {
				let now = new Date()
				let hour = now.getHours() + 1
				let minute = now.getMinutes()
				if (hour.toString().length < 2) {
					hour = '0' + hour
				}
				if (minute.toString().length < 2) {
					minute = '0' + minute
				}
				this.finishTime = hour + ':' + minute
			},
			toAddressPage() {
				uni.navigateTo({
					url: '/pages/address/address'
				})

			},
			//获取购物车数据
			async getCartData() {
				const res = await cartListApi({})
				if (res.code === 1) {
					this.cartData = res.data
				} else {
					this.$notify({
						type: 'warning',
						message: res.msg
					});
				}
			},
			async goToPaySuccess() {
				console.log(this.address + "收货地址")
				if (this.address.id == null) {
					console.log(this.address.id + '收货地址ID')
					return uni.$showMsg('请选择收货地址', 1500, 'none')
				}
				const params = {
					remark: this.note,
					payMethod: 1,
					addressBookId: this.address.id
				}
				const res = await addOrderApi(params)
				if (res.code === 1) {
					uni.navigateTo({
						url: '../paySuccess/paySuccess'
					})
				} else {
					return uni.$showMsg(res.msg)
				}
			},
			//网络图片路径转换
			imgPathConvert(path) {

				return this.QiNiuYunUrl + path
			},
		}

	}
</script>

<style>
	@import url(./addOrder.css);
</style>
