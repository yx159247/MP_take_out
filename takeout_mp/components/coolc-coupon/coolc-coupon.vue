<template>
    <view class="coupon-item">
		<view class="coupon-money">
			<view class="nick" v-if="!types">{{item.seller_name}}使用</view>
			<view class="layof" :style="{color:theme}">￥{{item.money}}</view>
			<view class="end_time">{{item.end_time}}前使用</view>
			<view v-if="!types">
				<view class="tit">券号：{{item.ticket}}</view>
				<view class="demand">{{item.title}}</view>
			</view>
		</view>
		<view class="get-btn" v-if="types" :style="{color:color, borderColor:color, background:solid}">选择使用</view>
		<navigator class="get-btn" v-if="!types" :style="{color:color, borderColor:color, background:solid}" :url='item.url'>立即使用</navigator>
    </view>
</template>

<script>
export default {
	components:{

	},
	data() {
		return {

		}
	},
	props:{
		item:{
			type: Object
		},
		types:{
			type: String,
			default: ''
		},
		theme:{
			type: String,
			default: '#ff9000'
		},
		solid:{
			type: String,
			default: '#ffffff'
		},
		color:{
			type: String,
			default: '#ff9000'
		},
	},
	methods: {

	}
}
</script>

<style lang='scss'>
.coupon-item {
	width:100%; height:auto; display:table; border-radius:10upx; padding:0 20upx; margin-top:22upx; border:1px solid #eeeeee; position:relative;
	.coupon-money {
		width:465upx; height:auto; display:table; float:left; padding:26upx 0; border-style:none dotted none none; border-color:#eeeeee;
		
		.nick { width:100%; height:50upx; line-height:30upx; font-size:$font-sm; color:$font-color-000; }
		.tit { width:100%; height:50upx; line-height:50upx; font-size:$font-sm; color:$font-color-999; }
		.demand { width:100%; height:30upx; line-height:30upx; font-size:$font-sm; color:$font-color-999; }
		
		.layof { width:100%; height:48upx; line-height:30upx; font-size:44upx; color:#ff9000; font-weight:bold; }
		.end_time { width:100%; height:30upx; line-height:30upx; font-size:$font-sm; color:$font-color-999; }
	}
	.get-btn { width:146upx; height:52upx; line-height:50upx; position:absolute; top:50%; right:26upx; margin-top:-26upx; text-align:center; border-radius:60upx; color:#ff9000; border:1px solid #ff9000; font-size:$font-sm; float:right; }
}
.coupon-item:after { width:40upx; height:20upx; position:absolute; left:460upx; top:-1px; border-radius:0 0 40upx 40upx; content:""; display:block; background:$bgcolor_white; border:1px solid #eeeeee; border-top:0px; }
.coupon-item:before { width:40upx; height:20upx; position:absolute; left:460upx; bottom:-1px; border-radius:40upx 40upx 0 0; content:""; display:block; background:$bgcolor_white; border:1px solid #eeeeee; border-bottom:0px; }
</style>