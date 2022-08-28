<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="套餐id " prop="setmealId">
          <el-input v-model="dataForm.setmealId" placeholder="套餐id "></el-input>
      </el-form-item>
          <el-form-item label="菜品id" prop="dishId">
          <el-input v-model="dataForm.dishId" placeholder="菜品id"></el-input>
      </el-form-item>
          <el-form-item label="菜品名称 （冗余字段）" prop="name">
          <el-input v-model="dataForm.name" placeholder="菜品名称 （冗余字段）"></el-input>
      </el-form-item>
          <el-form-item label="菜品原价（冗余字段）" prop="price">
          <el-input v-model="dataForm.price" placeholder="菜品原价（冗余字段）"></el-input>
      </el-form-item>
          <el-form-item label="份数" prop="copies">
          <el-input v-model="dataForm.copies" placeholder="份数"></el-input>
      </el-form-item>
          <el-form-item label="排序" prop="sort">
          <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
      </el-form-item>
              <el-form-item label="修改人" prop="updater">
          <el-input v-model="dataForm.updater" placeholder="修改人"></el-input>
      </el-form-item>
          <el-form-item label="更新时间" prop="updateDate">
          <el-input v-model="dataForm.updateDate" placeholder="更新时间"></el-input>
      </el-form-item>
          <el-form-item label="是否删除" prop="isDeleted">
          <el-input v-model="dataForm.isDeleted" placeholder="是否删除"></el-input>
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
        setmealId: '',
        dishId: '',
        name: '',
        price: '',
        copies: '',
        sort: '',
        creator: '',
        createDate: '',
        updater: '',
        updateDate: '',
        isDeleted: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        setmealId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        dishId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        price: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        copies: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        sort: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        updater: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        updateDate: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        isDeleted: [
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
      this.$http.get(`/takeout/setmealdish/${this.dataForm.id}`).then(({ data: res }) => {
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
        this.$http[!this.dataForm.id ? 'post' : 'put']('/takeout/setmealdish/', this.dataForm).then(({ data: res }) => {
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
