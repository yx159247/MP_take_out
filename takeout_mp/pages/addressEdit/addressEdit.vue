<template>
	<view>
		<view class="address_edit">

			<view class="divContent">
				<view class="divItem">
					<text style="font-weight: bold;">联系人：</text>
					<view class="inputUser"><u-input fontSize="28rpx" border="none" maxlength='10' placeholder="请填写收货人的姓名" v-model="form.consignee"></u-input>
					</view>
					</el-input>
					<text class="spanChecked" @click="form.gender = '0'">
					<text :class="{iActive:form.gender === '0'}"></text>
					先生
					</text>
					<text class="spanChecked" @click="form.gender = '1'">
						<text :class="{iActive:form.gender === '1'}"></text>
						女士
					</text>
				</view>
				<view class="divItem">
					<text style="font-weight: bold;">手机号：</text>
					<u-input  fontSize="28rpx" border="none" maxlength='11' placeholder="请填写收货人手机号码" v-model="form.phone"></u-input>
					<!-- <el-input placeholder=" 请填写收货人手机号码" v-model="form.phone" maxlength='20'
						style="width: calc(100% - 160rpx);" />
					</el-input> -->
				</view>
				<view class="divItem">
					<text style="font-weight: bold;">收货地址：</text>
					<u-input  fontSize="28rpx" border="none" maxlength='140' placeholder="请输入收货地址" v-model="form.detail"></u-input>
					<!-- <el-input placeholder=" 请输入收货地址" v-model="form.detail" maxlength='140' />
					</el-input> -->
				</view>
				<view class="divItem ">
					<text style="font-weight: bold;">标签：</text>
					<text v-for="(item,index) in labelList" :key="index" @click="form.label = item;activeIndex = index"
						:class="{spanItem:true,spanActiveSchool:activeIndex === index}">{{item}}</text>
				</view>
				<view class="divSave" @click="saveAddress">保存地址</view>
				<view class="divDelete" @click="deleteAddress" v-if="id">删除地址</view>
			</view>
		</view>
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
	import {addressFindOneApi,updateAddressApi,addAddressApi,deleteAddressApi} from '../../api/address.js'
	export default {
		data() {
			return {
				title: '新增收货地址',
				form: {
					consignee: '', //联系人
					phone: undefined, //手机号
					gender: '1', //0表示男 1 表示女
					detail: '', //收货地址
					label: '公司', //标签
				},
				labelList: [
					'无', '公司', '家', '学校'
				],
				id: undefined,
				activeIndex: 0
			};
		},
		
		computed: {},
		created() {
			this.initData()
		},
		mounted() {},
		methods: {
			goBack() {
				history.go(-1)
			},
			async initData() {
				const params = parseUrl(window.location.search)
				this.id = params.id
				if (params.id) {
					this.title = '编辑收货地址'
					const res = await addressFindOneApi(params.id)
					if (res.code === 0) {
						this.form = res.data
					} else {
						this.$notify({
							type: 'warning',
							message: res.msg
						});
					}
				}
			},
			async saveAddress() {
				const form = this.form
				if (!form.consignee) {
					return uni.$showMsg('请输入联系人',1500,'none')
					
				}
				if (!form.phone) {
					return uni.$showMsg('请输入手机号',1500,'none')
		 	}
				if (!form.detail) {
					return uni.$showMsg('请输入收货地址',1500,'none')
				}
				const reg = /^1[3|4|5|7|8][0-9]{9}$/
				if (!reg.test(form.phone)) {
					return uni.$showMsg('手机号不合法',1500,'none')
				}
				let res = {}
				if (this.id) {
					res = await updateAddressApi(this.form)
				} else {
					res = await addAddressApi(this.form)
				}

				if (res.code === 0) {
					uni.navigateBack({
						delta:1
					})
				} else {
		 		this.$notify({
						type: 'warning',
						message: res.msg
					});
				}
			},
		 deleteAddress() {
				this.$dialog.confirm({
						title: '确认删除',
						message: '确认要删除当前地址吗？',
					})
					.then(async () => {
						const res = await deleteAddressApi({
							id: this.id
						})
						if (res.code === 0) {
							window.requestAnimationFrame(() => {
								window.location.replace('/front/page/address.html')
							})
						} else {
							this.$notify({
								type: 'warning',
								message: res.msg
							});
						}
					})
					.catch(() => {});
			},
		}
	}
</script>

<style>
@import url(./addressEdit.css);
</style>
