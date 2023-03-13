<template>
	<view>
		<view class="order">
			<view class="divBody" v-if="list.length > 0">
					<view v-for="(order, index) in list" :key="index">
						<!-- <view class="item" @touchstart="touchstartE(order)" @touchend="touchendE(order)"> -->
							<view class="item">
							<view class="timeStatus">
								<view class="zuodingwei"></view>
								<text class="time">{{ order.orderTime }}</text>
								<view class="youdingwei">
									<text style="color: #ffffff;font-size: 24rpx;">{{ getStatus(order.status) }}</text>
									<view class="triangle"></view>
								</view>
							</view>
		
							<view class="dishList">
								<view v-for="(item, index) in order.orderDetails" :key="index" class="item">
									<text>{{ item.name }}</text>
									<text>x{{ item.number }}</text>
								</view>
							</view>
		
							<view style="height: 1px;border-top: 1rpx  solid #efefef"></view>
							<view class="result">
								<text>共{{ order.sumNum }} 件商品,实付</text>
								<text class="price">￥{{ order.amount }}</text>
							</view>
							<view class="btn" v-if="order.status === 4">
								<view class="btnAgain" @click="addOrderAgain(order)">再来一单</view>
							</view>
							<view class="foot"></view>
						</view>
					</view>
			</view>
		</view>
	</view>
</template>

<script>
	import regeneratorRuntime, {
		async
	} from '../../lib/runtime/runtime';
	import {
		orderAgainApi,
		deleteOrderApi
	} from '../../api/orderList.js';
	export default {
		props:{
			list: {
				type: Array,
				default(){
					return []
				}
			}
		},
		name:"order-list",
		data() {
			return {
				
				

				orderList: [],
				
				
				orderId: '',

			};
		},
		methods: {
			getStatus(status) {
				let str = '';
				switch (status) {
					case 1:
						str = '待付款';
						break;
					case 2:
						str = '正在派送';
			
						break;
					case 3:
						str = '已派送';
						break;
					case 4:
						str = '已完成';
						break;
					case 5:
						str = '已取消';
						break;
				}
				return str;
			},
			async addOrderAgain(order) {
				const res = await orderAgainApi({
					id: order.id
				});
				if (res.code === 0) {
					uni.switchTab({
						url: '/pages/index/index'
					});
				} else {
					return uni.$showMsg(res.msg);
				}
			},
		},
	}
</script>

<style>
.order {
  height: 100%;
}

.order .divHead {
  width: 100%;
  height: 120rpx;
  opacity: 1;
  background: #333333;
  position: relative;
}

.order .divHead .divTitle {
  font-size: 36rpx;
  font-family: PingFangSC, PingFangSC-Regular;
  font-weight: 500;
  text-align: center;
  /* color: #ffffff; */
  line-height: 50rpx;
  letter-spacing: 0;
  position: absolute;
  bottom: 36rpx;
  width: 100%;
}

.order .divHead .divTitle i {
  position: absolute;
  left: 32rpx;
  top: 50%;
  transform: translate(0, -50%);
}

.order .divBody {
  margin: 20rpx 24rpx 20rpx 24rpx;
  /* background: #ffffff; */
  border-radius: 12rpx;
  padding-left: 20rpx;
  /* padding-right: 20rpx; */
  
}

.order .divBody .u-list .u-list-item .u-cell::after {
  border: 0;
}

	.order .divBody .item{
		padding: 0 5px;
		margin-right: 10rpx;
		background-color: #ffffff;
		border-radius: 10px;
		margin-bottom: 15px;
		box-shadow: 4rpx 10rpx 12rpx  #dcdcdf;
	}
.order .divBody .item .timeStatus {
  position: relative;
  padding-left: 8px;
  height: 92rpx;
  /* line-height: 32rpx; */
  font-size: 28rpx;
  font-family: PingFangSC, PingFangSC-Regular;
  font-weight: 400;
  text-align: left;
  color: #f9ae3d;
  line-height: 40rpx;
  letter-spacing: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 4rpx solid #efefef;
  border-top: 2rpx solid #efefef;
}

.order .divBody .item .timeStatus text:first-child {
  color: #333333;
}
.order .divBody .item .timeStatus .time{
	flex: 1;
	color: black;
	height: 50px;
	line-height: 50px;
	align-self: flex-start;
}
.order .divBody .item .timeStatus .youdingwei {
  /* color: #333333; */
  position: relative;
  padding: 2px 4px 2rpx 10px;
  right: -20rpx;
  top: 0;
 
  border-bottom-left-radius: 10px;
  border-top-left-radius: 10px;
  background-color: #1ab71b;
}
.order .divBody .item .timeStatus .zuodingwei {
position: relative;
left: -14px;
width: 6px;
height: 22px;
background-color: #feca50;
}
.order .divBody .item .timeStatus .triangle {
	position: absolute;
	right: 0;
    width: 0;
    height: 0;
    border-top: 10rpx solid #1ab71b;
    border-right:10rpx solid transparent;
}
.order .divBody .item .dishList {
  padding-top: 20rpx;
  padding-bottom: 22rpx;
}
	

.order .divBody .item .dishList .item {
  padding-top: 10rpx;
  padding-bottom: 10rpx;
  display: flex;
  justify-content: space-between;
  height: 40rpx;
  opacity: 1;
  font-size: 28rpx;
  font-family: PingFangSC, PingFangSC-Regular;
  font-weight: 400;
  text-align: left;
  color: #666666;
  line-height: 40rpx;
  letter-spacing: 0;
  box-shadow: none;
}

.order .divBody .item .result {
  display: flex;
  justify-content: flex-end;
  height: 40rpx;
  opacity: 1;
  font-size: 28rpx;
  font-family: PingFangSC, PingFangSC-Regular;
  font-weight: 400;
  text-align: left;
  color: #666666;
  line-height: 40rpx;
}

.order .divBody .item .result .price {
  font-weight: bold;
  color: #000000;
}

.order .divBody .item .btn {
  display: flex;
  justify-content: flex-end;
  /* margin-bottom: 34rpx; */
  margin-top: 20rpx;
}
.order .divBody .item .foot {
	height: 40rpx;
}


.order .divBody .btn .btnAgain {
  width: 248rpx;
  height: 72rpx;
  opacity: 1;
  border: 2rpx solid #feca50;//边框
  border-radius: 38rpx;
  opacity: 1;
  font-size: 28rpx;
  font-family: PingFangSC, PingFangSC-Regular;
  font-weight: 500;
  text-align: center;
  color: #333333;
  line-height: 72rpx;
  letter-spacing: 0;
  position: relative;
  color: #feca50;
}

.order .divNoData {
  width: 100%;
  height: calc(100% - 176rpx);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.order .divNoData .divContainer image {
  width: 480rpx;
  height: 258rpx;
}

.order .divNoData .divContainer view {
  font-size: 48rpx;
  font-family: PingFangSC, PingFangSC-Medium;
  font-weight: 500;
  text-align: center;
  color: #333333;
  line-height: 66rpx;
  height: 66rpx;
  margin-top: 40rpx;
}

</style>