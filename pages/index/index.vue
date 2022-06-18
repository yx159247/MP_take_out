<template>
	<view>
		<view class="divHead">

		</view>
		<view class="divTitle">
			<view class="divStatic">
				<image src="../../static/images/logo.png" class="logo"></image>
				<view class="divDesc">
					<view class="divName">美食元素</view>

					<view class="divSend">
						<image src="../../static/images/time.png" /><text>距离1.5km</text>
						<image src="../../static/images/money.png" /><text>配送费6元</text>
						<image src="../../static/images/location.png" /><text>预计时长12min</text>
					</view>
				</view>
			</view>
			<view class="divDesc">
				简介: 定位“大众化的美食外送餐饮”.
			</view>
		</view>
		<u-skeleton :loading="loading" rows="15"></u-skeleton>
		<view class="divBody" v-if="!loading">
			<view class="divType">
				<scroll-view scroll-y class="ul">
					<view v-for="(item,index) in categoryList" :key="index"
						@click="categoryClick(index,item.id,item.type)"
						:class="['li',activeType === index ? 'active' : '']">{{item.name}}</view>
				</scroll-view>
			</view>


			<view class="divMenu">
				<scroll-view scroll-y :scroll-top="scrollTop" :style="{height: wh+150 + 'rpx'}"
					style="margin-top: 100rpx;">

					<view class="divItem" v-for="(item,index) in dishList" :key="index" @click="dishDetails(item)">
						<u-image width="172rpx" height="172rpx" :src="imgPathConvert(item.image)">

							<image src="../../static/images/noImg.png" />

						</u-image>
						<view>
							<view class="divName">{{item.name}}</view>
							<view class="divDesc">{{item.description}}</view>
							<view class="divDesc">{{'月销' + (item.saleNum ? item.saleNum : 0)  }}</view>
							<view class="divBottom"><text>￥</text><text>{{item.price/100}}</text></view>
							<view class="divNum">
								<view class="divAdd" v-if="item.number >= 1">
									<image src="../../static/images/subtract.png"
										@click.prevent.stop="subtractCart(item)" />
								</view>
								<view class="divDishNum">{{item.number}}</view>
								<view class="divTypes" v-if="item.flavors && item.flavors.length > 0 && !item.number "
									@click.prevent.stop="chooseFlavorClick(item)">选择规格</view>
								<view class="divAdd" v-else>
									<image src="../../static/images/add.png" @click.prevent.stop="addCart(item)" />
								</view>
							</view>
						</view>
					</view>


				</scroll-view>

			</view>

		</view>
		<view class="divLayer">
			<view class="divLayerLeft"></view>
			<view class="divLayerRight"></view>
		</view>
		<!-- 购物车 -->
		<view class="divCart" v-if="categoryList.length > 0">
			<view class="imgCart" v-if="!cartData || cartData.length<1"></view>
			<view class="imgCartActive" @click="openCart" v-else></view>

			<view :class="{divGoodsNum:1===1, moreGoods:cartData && cartData.length > 99}"
				v-if="cartData && cartData.length > 0">{{ goodsNum }}</view>
			<view class="divNum">
				<text>￥</text>
				<text>{{goodsPrice}}</text>
			</view>
			<view class="divPrice"></view>
			<!-- <view class="btnSubmit" v-if="cartData.length<1">去结算</view> -->
			<button class="btnSubmit" v-if="cartData.length<1">去结算</button>
			<!-- <view  class="btnSubmitActive" @click="toAddOrderPage" v-else>去结算</view> -->
			<button class="btnSubmitActive" @click="toAddOrderPage" v-else>去结算</button>
			<!-- <u-button color="#ffc200" shape="circle" openType="getphonenumber">去结算</u-button> -->
		</view>


		<!-- 口味选项弹框 -->
		<u-popup ref="flavorDialog" @close="flavorclose" :closeOnClickOverlay="true" :round="10" mode="center"
			:show="dialogFlavor.show" v-model="dialogFlavor.show">
			<view class="dialogFlavor">
				<view class="dialogTitle">{{dialogFlavor.name}}</view>
				<view class="divContent">
					<view v-for="(flavor,index) in dialogFlavor.flavors" :key="flavor.id">
						<view class="divFlavorTitle">{{flavor.name}}</view>
						<text v-for="item in JSON.parse(flavor.value)" 
						:key="item"
						@click="flavorClick(index,item)"
						:class="{spanActive:flavor.dishFlavor === item}">{{item}}</text>
					</view>
				</view>
				<view class="divBottom">
					<view><text class="spanMoney">￥</text>{{dialogFlavor.price/100}}</view>

					<view @click="dialogFlavorAddCart">加入购物车</view>

				</view>

			</view>

		</u-popup>
		<u-notify :show="true" message="123" type="warning"></u-notify>
		<!-- 购物车弹出 -->




		<u-popup v-model="cartDialogShow" mode="bottom" :show="cartDialogShow" @close="cartclose"
			:closeOnClickOverlay="true" :round="10">


			<view class="dialogCart">
				<view class="divCartTitle">
					<view class="title">购物车</view>
					<view class="clear" @click="clearCart">
						<view class="el-icon-delete"></view> 清空
					</view>
				</view>
				<scroll-view scroll-y="true" :style="{ height: wh + 'rpx' }">

					<view class="divCartContent">

						<view v-for="item in cartData" :key="item.id" class="divCartItem">

							<u-image width="128rpx" height="128rpx" :src="imgPathConvert(item.image)">

							</u-image>
							<view class="divDesc">
								<view class="name">{{item.name}}</view>
								<view class="price">
									<text class="spanMoney">￥</text>{{item.amount}}
								</view>
							</view>
							<view class="divNum">
								<view class="divSubtract">
									<image src="../../static/images/subtract.png" @click="cartNumberSubtract(item)" />
								</view>
								<view class="divDishNum" style="font-size: 28rpx;font-weight: bold">{{item.number}}
								</view>
								<view class="divAdd">
									<image src="../../static/images/add.png" @click="cartNumAdd(item)" />
								</view>
							</view>
							<view class="divSplit"></view>

						</view>
					</view>

				</scroll-view>
			</view>




		</u-popup>

		<!-- 菜品详情弹窗 -->
		<u-popup @close="dishclose" :closeOnClickOverlay="true" :round="10" mode="center" :show="detailsDialog.show"
			v-model="detailsDialog.show" v-if="detailsDialog.show" ref="detailsDialog">
			<!-- <u-modal
				 @close="close"
				 :content-slot="true" 
				 :closeOnClickOverlay="true" 
				 :showConfirmButton="false" 
				 :show="detailsDialog.show" 
				 mode="center" 
				 v-model="detailsDialog.show"
				 v-if="detailsDialog.show"
				   ref="detailsDialog"> -->
			<view class="detailsDialog">
				<view class="divContainer">
					<u-image :src="imgPathConvert(detailsDialog.item.image)">
						<view slot="error" class="image-slot">
							<image src="../../static/images/noImg.png" />
						</view>
					</u-image>
					<view class="title">{{detailsDialog.item.name}}</view>
					<view class="content">{{detailsDialog.item.description}} </view>
				</view>
				<view class="divNum">
					<view class="left">
						<text>￥</text><text>{{detailsDialog.item.price/100}}</text>
					</view>
					<view class="right">
						<view class="divSubtract" v-if="detailsDialog.item.number > 0">
							<image src="../../static/images/subtract.png" @click="subtractCart(detailsDialog.item)" />
						</view>
						<view class="divDishNum">{{detailsDialog.item.number}}</view>
						<view class="divTypes"
							v-if="detailsDialog.item.flavors && detailsDialog.item.flavors.length > 0 && !detailsDialog.item.number "
							@click="chooseFlavorClick(detailsDialog.item)">选择规格</view>
						<view class="divAdd" v-else>
							<image src="../../static/images/add.png" @click="addCart(detailsDialog.item)" />
						</view>
					</view>
				</view>
				<!-- <view class="detailsDialogClose" @click="detailsDialog.show = false">
					            <image  src="../../static/images/close.png"/>
					          </view> -->
			</view>


			<!-- </u-modal> -->
		</u-popup>
		<!-- 套餐详情弹窗 -->
		<u-popup @close="setMealclose" :closeOnClickOverlay="true" :round="10" mode="center" :show="setMealDialog.show"
			v-model="setMealDialog.show" v-if="setMealDialog.show" ref="setMealDetailsDialogd">

			<view class="setMealDetailsDialog">


				<view class="divContainer">
					<view class="title">{{setMealDialog.item.name}}</view>
					<view class="item" v-for="(item,index) in setMealDialog.item.list" :key="index">
						<u-image :src="imgPathConvert(item.image)">
							<view slot="error" class="image-slot">
								<image src="../../static/images/noImg.png" />
							</view>
						</u-image>
						<view class="divSubTitle">{{item.name + '(' + item.copies + '份)' }}
							<view class="divPrice">
								<text>￥</text><text>{{item.price/100}}</text>
							</view>
						</view>
						<view class="content">{{item.description}}</view>
					</view>
				</view>
				<view class="divNum">
					<view class="left">
						<text>￥</text><text>{{setMealDialog.item.price/100}}</text>
					</view>
					<view class="right">
						<view class="divSubtract" v-if="setMealDialog.item.number > 0">
							<image src="../../static/images/subtract.png" @click="subtractCart(setMealDialog.item)" />
						</view>
						<view class="divDishNum">{{setMealDialog.item.number}}</view>
						<view class="divAdd" v-if="setMealDialog.item.number">
							<image src="../../static/images/add.png" @click="addCart(setMealDialog.item)" />
						</view>
						<view class="addCart" @click="addCart(setMealDialog.item)" v-if="!setMealDialog.item.number">
							加入购物车</view>
					</view>
				</view>
				<!-- <view class="detailsDialogClose" @click="setMealDialog.show = false">
					            <image src="../../static/images/close.png"/>
					          </view> -->
			</view>


		</u-popup>


	</view>
</template>
<style>
	@import url('./index.css');
</style>
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
	} from '../../api/index'

	export default {
		data() {
			return {
				checked: false,
				loading: true,
				phone_info: [],
				flavorNotify: false,
				wh: 0,
				scrollTop: 0,
				active: 0,
				icon: {
					active: '../images/order.png',
					inactive: 'https://img01.yzcdn.cn/vant/user-inactive.png',
				},
				QiNiuYunUrl: getApp().globalData.QiNiuYunUrl,
				//左边菜品类别index
				activeType: 0,
				categoryList: [],
				categoryId: undefined,
				dishList: [],
				cartData: [],
				dialogFlavor: {
					name: '',
					flavors: [					
					],
					dishId: undefined,
					price: undefined,
					show: false,
					image: ''
				},
				cartDialogShow: false,
				detailsDialog: {
					show: false,
					item: {
						image: ''
					}
				},
				setMealDialog: {
					show: false,
					item: {}
				},
			}
		},
		onShow() {

			this.initData()
			let token = wx.getStorageSync('token')
			console.log("token", token)
			if (token.length == 0) {
				this.cartData = []
			}
			this.activeType = 0
			this.dialogFlavor.show = false
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
				return price.toFixed(2)
			}
		},
		onReady() {

		},
		created() {},
		watch: {
			// 'dialogFlavor.show'(flag) {
			// 	const query = uni.createSelectorQuery().in(this);
			// 	if (flag) {
			// 		query.select('.divCart').style.zIndex = 1
			// 		// document.querySelector('.divCart').style.zIndex = 1
			// 	} else {
			// 		query.select('.divCart').style.zIndex = 3000
			// 		// document.querySelector('.divCart').style.zIndex = 3000
			// 	}
			// },
		},
		mounted() {
			const sysInfo = uni.getSystemInfoSync()
			this.wh = sysInfo.windowHeight
			this.cartData = []
			this.initData()

		},
		methods: {
			cartclose() {
				this.cartDialogShow = false;
			},
			dishclose() {
				this.detailsDialog.show = false;
			},
			setMealclose() {
				this.setMealDialog.show = false;
			},
			flavorclose() {
				this.dialogFlavor.show = false;
			},
			//初始化数据
			initData() {
				Promise.all([categoryListApi(), cartListApi({})]).then(res => {
					//获取分类数据
					if (res[0].code === 1) {
						this.categoryList = res[0].data
						if (Array.isArray(res[0].data) && res[0].data.length > 0) {
							this.categoryId = res[0].data[0].id
							if (res[0].data[0].type === 1) {
								this.getDishList()
							} else {
								this.getSetmealData()
							}
						}

					} else {
						return uni.$showMsg()
					}
					//获取菜品数据
					if (res[1].code === 1) {
						this.cartData = res[1].data
					} else {

						return uni.$showMsg()
					}
					if (this.loading) {
						setTimeout(() => {
							this.loading = false;
						}, 1500);
					}
				})
			},
			//分类点击
			categoryClick(index, id, type) {
				this.scrollTop = this.scrollTop === 0 ? 1 : 0
				this.activeType = index
				this.categoryId = id
				if (type === 1) { //菜品
					this.getDishList()
				} else {
					this.getSetmealData()
				}
			},
			//获取菜品数据
			async getDishList() {
				if (!this.categoryId) {
					return
				}
				const res = await dishListApi({
					categoryId: this.categoryId,
					status: 1
				})
				if (res.code === 1) {
					let dishList = res.data
					const cartData = this.cartData
					if (dishList.length > 0 && cartData.length > 0) {
						dishList.forEach(dish => {
							cartData.forEach(cart => {
								if (dish.id === cart.dishId) {
									dish.number = cart.number
								}
							})
						})
					}
					this.dishList = dishList
				} else {
					return uni.$showMsg()
				}
			},
			//获取套餐数据setmealId
			async getSetmealData() {
				if (!this.categoryId) {
					return
				}
				const res = await setmealListApi({
					categoryId: this.categoryId,
					status: 1
				})
				if (res.code === 1) {
					let dishList = res.data
					const cartData = this.cartData
					if (dishList.length > 0 && cartData.length > 0) {
						dishList.forEach(dish => {
							cartData.forEach(cart => {
								if (dish.id === cart.setmealId) {
									dish.number = cart.number
								}
							})
						})
					}
					this.dishList = dishList
				} else {
					return uni.$showMsg()
				}
			},
			//获取购物车数据
			async getCartData() {
				const res = await cartListApi({})
				if (res.code === 1) {
					this.cartData = res.data
				} else {

					return uni.$showMsg()
				}
			},
			//菜单中往购物车中添加商品
			async addCart(item) {
				let params = {
					amount: item.price / 100, //金额
					dishFlavor: item.dishFlavor, //口味  如果没有传undefined
					dishId: undefined, //菜品id
					setmealId: undefined, //套餐id
					name: item.name,
					image: item.image
				}
				if (Array.isArray(item.flavors)) { //表示是菜品
					params.dishId = item.id
				} else { //表示套餐 套餐没有口味
					params.setmealId = item.id
				}
				const res = await addCartApi(params)
				if (res.code === 1) {
					this.dishList.forEach(dish => {
						if (dish.id === item.id) {
							dish.number = res.data.number
						}
					})
					if (this.setMealDialog.show) {
						console.log(res.data.number)
						item.number = res.data.number
					}
					this.getCartData()
				} else {
					return uni.$showMsg()
				}
			},

			//菜单中减少选中的商品
			async subtractCart(item) {
				let params = {
					dishId: item.id,
				}
				if (!Array.isArray(item.flavors)) {
					params = {
						setmealId: item.id,
					}
				}
				const res = await updateCartApi(params)
				if (res.code === 1) {
					this.dishList.forEach(dish => {
						if (dish.id === item.id) {
							dish.number = (res.data.number === 0 ? undefined : res.data.number)
						}
					})
					if (this.setMealDialog.show) {
						item.number = (res.data.number === 0 ? undefined : res.data.number)
					}
					this.getCartData()
				} else {
					return uni.$showMsg()
					this.$notify({
						type: 'warning',
						message: res.msg
					});
				}
			},

			//展开购物车
			openCart() {
				if (this.cartData.length > 0) {
					this.cartDialogShow = true
				}
			},
			//购物车中增加商品数量
			async cartNumAdd(item) {
				let params = {
					amount: item.amount, //金额
					dishFlavor: item.dishFlavor, //口味  如果没有传undefined
					dishId: item.dishId, //菜品id
					setmealId: item.setmealId, //套餐id
					name: item.name,
					image: item.image
				}
				const res = await addCartApi(params)
				if (res.code === 1) {
					this.dishList.forEach(dish => {
						if (dish.id === (item.dishId || item.setmealId)) {
							dish.number = res.data.number
						}
					})
					console.log(this.dishList)
					this.getCartData()
				} else {
					return uni.$showMsg()
				}
			},
			//购物车中减少商品数量
			async cartNumberSubtract(item) {
				let params = {
					dishId: item.dishId,
					setmealId: item.setmealId,
				}
				const res = await updateCartApi(params)
				if (res.code === 1) {
					this.dishList.forEach(dish => {
						if (dish.id === (item.dishId || item.setmealId)) {
							dish.number = (res.data.number === 0 ? undefined : res.data.number)
						}
					})
					this.getCartData()
				} else {
					return uni.$showMsg()

				}
			},

			//修改商品列表中的数据number
			changeDishList(item) {
				for (let ele of this.dishList) {
					if (ele.id === (item.setmealId || item.dishId)) {
						ele.number = item.number
					}
				}
			},

			//清空购物车
			async clearCart() {
				const res = await clearCartApi()
				if (res.code === 1) {
					for (let ele of this.dishList) {
						ele.number = undefined
					}
					this.cartData = []
					this.cartDialogShow = false
				} else {
					return uni.$showMsg()
				}
			},
			//点击选择规格
			chooseFlavorClick(item) {
				this.detailsDialog.show = false
				this.dialogFlavor = {
					name: '',
					
					flavors: [],
					dishId: undefined,
					price: undefined,
					show: false
				}
				this.dialogFlavor = {
					name: item.name,
					flavors: item.flavors,
					dishId: item.id,
					price: item.price,
					show: true,
					image: item.image
				}
				console.log("口味",this.dialogFlavor.flavors)

			},
			flavorClick(index, item) {
				console.log("index",index)
				this.dialogFlavor.flavors[index].dishFlavor = item
				// console.log("ok!")
				// console.log("输出flavor",flavor.dishFlavor)
				// flavor.dishFlavor = item
				
				// this.checked = true
				// console.log("输出flavor",flavor.dishFlavor)
				// console.log("输出flavors",this.dialogFlavor.flavors)
				// console.log("输出item",item)
				// //强制刷新dialog的dom
				this.dialogFlavor.show = false
				this.dialogFlavor.show = true
			},
			//选择规格加入购物车
			dialogFlavorAddCart() {
				const dialogFlavor = this.dialogFlavor
				let flag = true
				let dishFlavor = []
				dialogFlavor.flavors.forEach(item => {
					console.log("选择口味加入购物车",item)
					if (item.dishFlavor) {
						dishFlavor.push(item.dishFlavor)
					} else {
						flag = false
						console.log("请添加口味")
						uni.showToast({
							title: '请选择' + item.name,
							icon: 'error',
						})
					}
				})
				if (flag) {
					this.addCart({
						price: dialogFlavor.price,
						dishFlavor: dishFlavor.join(","),
						id: dialogFlavor.dishId,
						flavors: [],
						image: dialogFlavor.image,
						name: dialogFlavor.name
					})
					this.dialogFlavor.show = false
				}
			},
			//网络图片路径转换
			imgPathConvert(path) {
				return this.QiNiuYunUrl + path
				// return imgPath(path)
			},
			//跳转到去结算界面
			toAddOrderPage() {
				if (this.cartData.length > 0) {
					uni.navigateTo({
						url: '/pages/addOrder/addOrder'
					})
				}

			},
			toUserPage() {
				window.requestAnimationFrame(() => {
					window.location.href = '/front/page/user.html'
				})
			},
			async dishDetails(item) {
				//先清除对象数据，如果不行的话dialog使用v-if
				this.detailsDialog.item = {}
				this.setMealDialog.item = {}
				if (Array.isArray(item.flavors)) {
					this.detailsDialog.item = item
					this.detailsDialog.show = true
				} else {
					//显示套餐的数据
					const res = await setMealDishDetailsApi(item.id)
					if (res.code === 1) {
						this.setMealDialog.item = {
							...item,
							list: res.data
						}
						this.setMealDialog.show = true
					} else {
						return uni.$showMsg(res.msg)
					}
				}

			}

		}
	}
</script>
