<template>

	<view>
		<u-skeleton rows="18" title :loading="loading"></u-skeleton>
		<u-empty :show="show" text="订单为空" mode="order" marginTop="50%"
			icon="http://cdn.uviewui.com/uview/empty/order.png">
		</u-empty>

		<view class="order" v-if="!loading">

			<view class="divBody" v-if="orderList.length > 0">


				<u-list @scrolltolower="scrolltolower" lowerThreshold="150">
					<u-list-item v-for="(order,index) in orderList" :key="index">


						<view class="item">
							<view class="timeStatus">
								<text>{{order.orderTime}}</text>
								<text :style="{'color' : colorList[order.status%6] }">{{getStatus(order.status)}}</text>

							</view>
							<view class="dishList">

								<view v-for="(item,index) in order.orderDetails" :key="index" class="item">
									<text>{{item.name}}</text>
									<text>x{{item.number}}</text>
								</view>
							</view>
							<view class="result">
								<text>共{{order.sumNum}} 件商品,实付</text><text class="price">￥{{order.amount}}</text>
							</view>
							<view class="btn" v-if="order.status === 4">
								<view class="btnAgain" @click="addOrderAgain(order)">再来一单</view>
							</view>
							<view class="foot"></view>
						</view>



					</u-list-item>
					<u-loadmore :status="status" />
				</u-list>



			</view>
		</view>

	</view>
</template>

<script>
	import '../../api/orderList.js'
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
		addOrderApi,
		orderListApi,
		orderPagingApi,
		orderAgainApi,
		deleteOrderApi

	} from '../../api/orderList.js'
	export default {
		data() {
			return {
				colorList:[
					"#909399",
					"#f9ae3d",
					"#f9ae3d",
					"#3c9cff",
					"#5ac725"
					
				],
				isloading: false,
				loading: true,
				status: "loadmore",
				wh: 0,
				active: 1,
				paging: {
					page: 1,
					pageSize: 10
				},
				orderList: [],
				show: false,
				orderId: ''

			};
		},
		computed: {},
		created() {

		},
		mounted() {

			const sysInfo = uni.getSystemInfoSync()
			this.wh = sysInfo.windowHeight

		},
		onShow() {
			this.loading = true
			this.paging.page = 1
			this.orderList = []
			this.getList()
			setTimeout(() => {
				this.loading = false;
			}, 500);

			// if(this.orderList.length == 0){
			// 	this.show = true
			// }
		},
		onPullDownRefresh() {
			this.status = "loadmore"
			this.paging.page = 1
			this.orderList = []
			this.getList()
			setTimeout(function() {
				uni.stopPullDownRefresh();
			}, 1000);
		},
		onReachBottom() {

		},
		methods: {
			scrolltolower() {
				if (this.isloading) return
				if (this.status != 'nomore') {
					this.status = 'loading'
					this.getList()
				}
			},

			initData() {
				this.getList()
			},
			async getList() {
				this.isloading = true
				const res = await orderPagingApi(this.paging)
				this.isloading = false
				if (res.code === 1) {
					this.orderList.push(...res.data.records)

					let records = res.data.records
					if (records) {
						this.show = false
					}
					if (records && Array.isArray(records)) {
						records.forEach(item => {
							let number = 0
							item.orderDetails.forEach(ele => {
								number += ele.number
							})
							item.sumNum = number
						})

					}
					if (this.paging.page >= res.data.pages) {
						this.status = 'nomore';
					}
					this.paging.page = this.paging.page + 1

				} else {
					return uni.$showMsg(res.msg)
				}
				if (this.loading) {
					setTimeout(() => {
						this.loading = false;
					}, 1500);
				}

			},
			onRefresh() {
				// 清空列表数据
				console.log("正在刷新")
				this.finished = false;

				// 重新加载数据
				// 将 loading 设置为 true，表示处于加载状态
				this.loading = true;
				console.log("正在调用onload")
				this.onLoad()

			},
			async addOrderAgain(order) {
				const res = await orderAgainApi({
					id: order.id
				})
				if (res.code === 1) {
					uni.switchTab({
						url: '/pages/index/index'
					})

				} else {
					return uni.$showMsg(res.msg)
				}
			},
			beforeClose({
				position,
				instance
			}) {
				switch (position) {
					case 'left':
					case 'cell':
					case 'outside':
						instance.close();
						break;
					case 'right':
						instance.close();

						//this.deleteOrder();
						break;
				}
			},
			deleteOrder(id) {
				this.$dialog.confirm({
					title: '确认删除',
					message: '确认要删除当前订单吗？',
				}).then(() => {
					deleteOrderApi(id).then(res => {
						if (res.code === 1) {
							this.getList();
							console.log("删除成功!正在调用onload")
							this.$notify({
								type: 'success',
								message: '删除成功'
							});

						} else {
							this.$notify({
								type: 'warning',
								message: res.msg
							});
						}
					}).catch(err => {
						this.$notify({
							type: 'danger',
							message: err
						});
					}).finally(() => {
						console.log("进入finally")
						this.refreshing = true;
						this.onRefresh();

					})
				})


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
			}
		}
	}
</script>

<style>
	@import url(./orderList.css);
</style>
