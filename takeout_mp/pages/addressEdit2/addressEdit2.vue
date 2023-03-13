<template>
	<view>
		<view class="address_edit">

			<view class="divContent" :style="{height:windowheight+'px'}">
				<view class="divItem">
					<text style="font-weight: bold;">联系人：</text>
					<view class="inputUser">
						<u-input fontSize="28rpx" border="none" maxlength='10' placeholder="请填写收货人的姓名"
							v-model="form.consignee"></u-input>
					</view>
					
					<text class="spanChecked" @click="form.gender = '0'">
						<text :class="{iActive:form.gender == '0'}"></text>
						先生
					</text>
					<text class="spanChecked" @click="form.gender = '1'">
						<text :class="{iActive:form.gender == '1'}"></text>
						女士
					</text>
				</view>
				<view class="divItem">
					<text style="font-weight: bold;">手机号：</text>
					<u-input fontSize="28rpx" border="none" maxlength='11' placeholder="请填写收货人手机号码"
						v-model="form.phone"></u-input>

				</view>
				<view class="divItem">
					<text style="font-weight: bold;">收货地址：</text>
					<u-input fontSize="28rpx" border="none" maxlength='140' placeholder="请输入收货地址" v-model="form.detail">
					</u-input>

				</view>
				<view class="divItem ">
					<text style="font-weight: bold;">标签：</text>
					<text v-model="activeIndex" v-for="(item,index) in labelList" :key="index"
						
						@click="form.label = item;activeIndex = index"
						:class="{spanItem:true,spanActiveSchool:form.label === item}">{{item}}</text>
				</view>
				<view class="divSave" @click="saveAddress">保存地址</view>
				<view class="divDelete" @click="deleteAddress" v-if="id">删除地址</view>
				<u-modal :show="show" title="温馨提示" @cancel="cancel" @confirm="confirm" showCancelButton="true">
					<view class="slot-content" style="font-size: 36rpx;">确认要删除当前地址吗?</view>
				</u-modal>
			</view>
		</view>
	</view>
</template>

<script>
	import regeneratorRuntime, {
		async
	} from '../../lib/runtime/runtime';
	import {
		addressFindOneApi,
		updateAddressApi,
		addAddressApi,
		deleteAddressApi
	} from '../../api/address.js'
	export default {
		data() {
			return {
				show: false,
				title: '新增收货地址',
				form: {
					consignee: '', //联系人
					phone: undefined, //手机号
					gender: 0, //0表示男 1 表示女
					detail: '', //收货地址
					label: '公司', //标签
				},
				labelList: [
					'无', '公司', '家', '学校'
				],
				id: undefined,
				activeIndex: 0,
				windowheight:0
			};
		},
		onLoad: function(option) {
			console.log("地址ID" + option.id)
			this.id = option.id
			addressFindOneApi(option.id).then((res) => {
				if (res.code === 0) {
					this.form = res.data
				} else {
					return uni.$showMsg(res.msg)

				}
			})
			let _this=this
			console.log('init')
			uni.getSystemInfo({
				success(res) {
					console.log(res)
					_this.windowheight=res.windowHeight
				}
			})
		},
		
		computed: {},
		created() {
			
		},
		mounted() {this.labelOnload()},
		methods: {
			cancel() {
				this.show = false
			},
			labelOnload(){
				let list = this.labelList
				for(let index in list){
					console.log(index,list[index])
					if(this.form.label == list[index]){
						this.activeIndex = index
					}
				}
			},
			async confirm() {
				console.log(this.id)
				const res = await deleteAddressApi({
					id: this.id
				})
				if (res.code === 0) {
					uni.navigateBack({
						delta: 1
					})
				} else {
					return uni.$showMsg(res.msg)
				}
			},
			async saveAddress() {
				const form = this.form
				if (!form.consignee) {
					return uni.$showMsg('请输入联系人', 1500, 'none')

				}
				if (!form.phone) {
					return uni.$showMsg('请输入手机号', 1500, 'none')
				}
				if (!form.detail) {
					return uni.$showMsg('请输入收货地址', 1500, 'none')
				}
				const reg = /^1[3|4|5|7|8][0-9]{9}$/
				if (!reg.test(form.phone)) {
					return uni.$showMsg('手机号不合法', 1500, 'none')
				}
				let res = {}
				if (this.id) {
					res = await updateAddressApi(this.form)
				} else {
					res = await addAddressApi(this.form)
				}

				if (res.code === 0) {
					uni.navigateBack({
						delta: 1
					})
				} else {
					return uni.$showMsg(res.msg)
				}
			},
			deleteAddress() {
				this.show = true

			},
		}
	}
</script>

<style>
	@import url(../addressEdit/addressEdit.css);
</style>
