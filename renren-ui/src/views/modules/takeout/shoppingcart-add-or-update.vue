<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="名称"></el-input>
      </el-form-item>
          <el-form-item label="图片" prop="image">
          <el-input v-model="dataForm.image" placeholder="图片"></el-input>
      </el-form-item>
          <el-form-item label="主键" prop="userId">
          <el-input v-model="dataForm.userId" placeholder="主键"></el-input>
      </el-form-item>
          <el-form-item label="菜品id" prop="dishId">
          <el-input v-model="dataForm.dishId" placeholder="菜品id"></el-input>
      </el-form-item>
          <el-form-item label="套餐id" prop="setmealId">
          <el-input v-model="dataForm.setmealId" placeholder="套餐id"></el-input>
      </el-form-item>
          <el-form-item label="口味" prop="dishFlavor">
          <el-input v-model="dataForm.dishFlavor" placeholder="口味"></el-input>
      </el-form-item>
          <el-form-item label="数量" prop="number">
          <el-input v-model="dataForm.number" placeholder="数量"></el-input>
      </el-form-item>
          <el-form-item label="金额" prop="amount">
          <el-input v-model="dataForm.amount" placeholder="金额"></el-input>
      </el-form-item>
              <el-form-item label="更新者" prop="updater">
          <el-input v-model="dataForm.updater" placeholder="更新者"></el-input>
      </el-form-item>
          <el-form-item label="更新时间" prop="updateDate">
          <el-input v-model="dataForm.updateDate" placeholder="更新时间"></el-input>
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
        name: '',
        image: '',
        userId: '',
        dishId: '',
        setmealId: '',
        dishFlavor: '',
        number: '',
        amount: '',
        creator: '',
        createDate: '',
        updater: '',
        updateDate: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        name: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        image: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        userId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        dishId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        setmealId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        dishFlavor: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        number: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        amount: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        updater: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        updateDate: [
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
      this.$http.get(`/takeout/shoppingcart/${this.dataForm.id}`).then(({ data: res }) => {
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
        this.$http[!this.dataForm.id ? 'post' : 'put']('/takeout/shoppingcart/', this.dataForm).then(({ data: res }) => {
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
