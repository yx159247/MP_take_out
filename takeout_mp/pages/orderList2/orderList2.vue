<template>
	<mescroll-uni ref="mescrollRef" @init="mescrollInit" @down="downCallback" @up="upCallback">
		<!-- <mescroll-empty v-if="goods.length==0"></mescroll-empty> -->
		<!-- <good-list :list="goods"></good-list> -->
		<order-list :list="goods"></order-list>


	</mescroll-uni>
</template>

<script>
	import MescrollMixin from "@/uni_modules/mescroll-uni/components/mescroll-uni/mescroll-mixins.js";
	import regeneratorRuntime, {
		async
	} from '../../lib/runtime/runtime';
	import {
		addOrderApi,
		orderListApi,
		orderPagingApi,
		orderAgainApi,
		deleteOrderApi
	} from '../../api/orderList.js';
	export default {
		mixins: [MescrollMixin], // 使用mixin
		data() {
			return {
				goods: [] // 数据列表
			}
		},
		methods: {

			/*上拉加载的回调: 其中page.num:当前页 从1开始, page.size:每页数据条数,默认10 */
			upCallback(page) {
				// 此处可以继续请求其他接口
				// if(page.num == 1){
				// 	// 请求其他接口...
				// }

				// 如果希望先请求其他接口,再触发upCallback,可参考以下写法
				// if(!this.isInitxx){
				// 	apiGetxx().then(res=>{
				// 		this.isInitxx = true
				// 		this.mescroll.resetUpScroll() // 重新触发upCallback
				// 	}).catch(()=>{
				// 		this.mescroll.endErr()
				// 	})
				// 	return // 此处return,先获取xx
				// }
				this.mescroll.optUp.textNoMore = '没有更多了'
				const token = wx.getStorageSync('token')
				if (token) {
					let httpData = {
						page: page.num,
						limit: page.size
					}
					orderPagingApi(httpData).then(res => {
						if (res.code === 0) {
							this.mescroll.endBySize(res.data.list.length, res.data.total)
							if (res.data.list && Array.isArray(res.data.list)) {
								res.data.list.forEach(item => {
									let number = 0;
									item.orderDetails.forEach(ele => {
										number += ele.number;
									});
									item.sumNum = number;
								});
							}
							if (page.num == 1) this.goods = []; //如果是第一页需手动制空列表
							this.goods = this.goods.concat(res.data.list); //追加新数据
						}

					}).catch(() => {
						//联网失败, 结束加载
						this.mescroll.endErr();
					})
				}

			}
		}
	}
</script>

<style>

</style>
