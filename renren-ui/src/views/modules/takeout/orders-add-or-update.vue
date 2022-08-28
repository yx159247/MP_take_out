<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="订单号" prop="number">
          <el-input v-model="dataForm.number" placeholder="订单号"></el-input>
      </el-form-item>
          <el-form-item label="订单状态 1待付款，2待派送，3已派送，4已完成，5已取消" prop="status">
          <el-input v-model="dataForm.status" placeholder="订单状态 1待付款，2待派送，3已派送，4已完成，5已取消"></el-input>
      </el-form-item>
          <el-form-item label="下单用户" prop="userId">
          <el-input v-model="dataForm.userId" placeholder="下单用户"></el-input>
      </el-form-item>
          <el-form-item label="地址id" prop="addressBookId">
          <el-input v-model="dataForm.addressBookId" placeholder="地址id"></el-input>
      </el-form-item>
          <el-form-item label="下单时间" prop="orderTime">
          <el-input v-model="dataForm.orderTime" placeholder="下单时间"></el-input>
      </el-form-item>
          <el-form-item label="结账时间" prop="checkoutTime">
          <el-input v-model="dataForm.checkoutTime" placeholder="结账时间"></el-input>
      </el-form-item>
          <el-form-item label="支付方式 1微信,2支付宝" prop="payMethod">
          <el-input v-model="dataForm.payMethod" placeholder="支付方式 1微信,2支付宝"></el-input>
      </el-form-item>
          <el-form-item label="实收金额" prop="amount">
          <el-input v-model="dataForm.amount" placeholder="实收金额"></el-input>
      </el-form-item>
          <el-form-item label="备注" prop="remark">
          <el-input v-model="dataForm.remark" placeholder="备注"></el-input>
      </el-form-item>
          <el-form-item label="" prop="phone">
          <el-input v-model="dataForm.phone" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="address">
          <el-input v-model="dataForm.address" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="userName">
          <el-input v-model="dataForm.userName" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="consignee">
          <el-input v-model="dataForm.consignee" placeholder=""></el-input>
      </el-form-item>
      </el-form>
    <template slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
    </template>
  </el-dialog>
</template>

<script>
import debounce from 'lodash/debounce'
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        id: '',
        number: '',
        status: '',
        userId: '',
        addressBookId: '',
        orderTime: '',
        checkoutTime: '',
        payMethod: '',
        amount: '',
        remark: '',
        phone: '',
        address: '',
        userName: '',
        consignee: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        number: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        status: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        userId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        addressBookId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        orderTime: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        checkoutTime: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        payMethod: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        amount: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        phone: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        address: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        userName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        consignee: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init () {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.getInfo()
        }
      })
    },
    // 获取信息
    getInfo () {
      this.$http.get(`/takeout/orders/${this.dataForm.id}`).then(({ data: res }) => {
        if (res.code !== 0) {
          return this.$message.error(res.msg)
        }
        this.dataForm = {
          ...this.dataForm,
          ...res.data
        }
      }).catch(() => {})
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        this.$http[!this.dataForm.id ? 'post' : 'put']('/takeout/orders/', this.dataForm).then(({ data: res }) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg)
          }
          this.$message({
            message: this.$t('prompt.success'),
            type: 'success',
            duration: 500,
            onClose: () => {
              this.visible = false
              this.$emit('refreshDataList')
            }
          })
        }).catch(() => {})
      })
    }, 1000, { 'leading': true, 'trailing': false })
  }
}
</script>
