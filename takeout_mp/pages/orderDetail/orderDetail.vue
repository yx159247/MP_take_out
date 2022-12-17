<template>
	<view :style="{ height: windowHeight + 'px' }" class="outer">
		<view class="divHead" :style="{ height: view1 + 'px' }">
			<view class="left">
				<text style="font-size: 18px;margin-bottom: 5px;">订单正在配送中</text>
				<text>配送员“<text style="color:#ffc100;font-size: 14px;">商家</text>”正在配送...</text>
				<text>点击查看配送员位置 ></text>
			</view>
			<view class="iamge">
				<image src="/static/index_image/配送快递服务.png"></image>
			</view>
		</view>
		<view class="divUserinfo" :style="{ height: view2 + 'px' }">
			<view class="user">收货人信息</view>
			<view class="userInfo">
			    收货人：
				<text>{{item.userName}} </text>
			</view>
			<view class="userInfo">
			    电话：
				 {{item.phone}}</text>
			</view>
			<view class="line"></view>
			<view class="addressInfo">
				地址：
				<text>{{item.address}}</text>
			</view>
		</view>
		<view class="divGoodsinfo" :style="{ height: view3 + 'px' }">
			<view class="goods">商品信息</view>
			<view class="goodDetail">
				<text>{{item.orderDetails[0].name}}</text>
				<text>￥{{item.amount}}/份 X{{item.orderDetails[0].number}}</text>
			
			</view>
			<view class="line"></view>
			<view class="item">
				<text>备注</text>
				<text>无</text>
			</view>
			<view class="line"></view>
			<view class="item">
				<text>商品金额</text>
				<text>￥{{item.amount}}</text>
			</view>
			<view class="item">
				<text>配送费</text>
				<text>￥0</text>
			</view>
			<view class="item">
				<text>优惠</text>
				<text>￥0</text>
			</view>
			<view class="item">
				<text>总计</text>
				<text style="color: red;font-size: 18px;font-weight: 600;">￥{{item.amount}}</text>
			</view>
		</view>
		<view class="divButton">
			<view class="button">联系商家</view>
			<view class="button">申请退款</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			windowHeight: 0,
			view1: 0,
			view2: 0,
			view3: 0,
			item:{
				
			}
		};
	},
	onLoad() {
		const _this = this;
		
		uni.getSystemInfo({
			success(res) {
				_this.windowHeight = res.windowHeight;
				_this.view1 = res.windowHeight * 0.15;
				_this.view2 = res.windowHeight * 0.20;
				_this.view3 = res.windowHeight * 0.5;
			}
		});
		this.item=uni.getStorageSync('item');
		console.log(this.item)
	}
};
</script>

<style lang="scss">
@mixin flex_r_be {
	display: flex;
	justify-content: space-between;
	align-items: center;
}
@mixin flex_c_center {
	display: flex;
	justify-content: space-between;
	flex-direction: column;
}
@mixin title {
	background-color: #c0d84d;
	color: white;
	padding: 5px 10px;
}
@mixin shadow {
	box-shadow: 3px 4px 2px #e1e1e3;
}
@mixin line{
			width: 100%;
			height: 1rpx;
			border-bottom: 1rpx solid #f3f3f3;
		}
.outer {
	@include flex_c_center();
	font-weight: 500;
	padding-bottom: 20px;
	box-sizing: border-box;
	.divHead {
		display: flex;
		justify-content: space-between;
		padding: 0 20px ;
		color: #ffffff;
		// justify-content: center;
		background-color: #619b51;
		.left{
			@include flex_c_center()
			justify-content: center;
			font-size: 12px;
		}
		.iamge{
			height: 100%;
			width: 30%;
			&>image{
				height: 100%;
				width:100%;
			}
		}
	}
	.divUserinfo {
		@include shadow;

		@include flex_c_center();
		background-color: #ffffff;
		margin: 0 10px;
		padding-bottom: 10px;;
		.user {
			@include title();
		}
		.userInfo {
			padding: 0 10px;
		}
		.addressInfo {
			padding: 0 10px;
			align-items: center;
		}
		.line{
			@include line
		}
		
	}
	.divGoodsinfo {
		background-color: #ffffff;
		@include shadow;
		@include flex_c_center();
		margin: 0 10px;
		.goodDetail {
			@include flex_r_be() position: relative;
			height: 20%;
			padding: 0 10px;
		}
		.goods {
			@include title();
		}
		.item {
			padding: 5px 10px;
			@include flex_r_be();
			
		}
		.line{
			@include line
		}
	}
	.divButton {
		
		padding: 0 10px;
		display: flex;
		  flex-flow: row-reverse wrap;
		.button {
			padding: 10px 20px;
			margin-left: 10px;
			border-radius: 15px;
			border: 3px solid #afcc4b;
		}
	}
}
</style>
