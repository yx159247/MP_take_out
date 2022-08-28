<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="类型" prop="type">
          <el-input v-model="dataForm.type" placeholder="类型   1 菜品分类 2 套餐分类"></el-input>
      </el-form-item>
          <el-form-item label="分类名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="分类名称"></el-input>
      </el-form-item>
<!--          <el-form-item label="顺序" prop="sort">-->
<!--          <el-input v-model="dataForm.sort" placeholder="顺序"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item prop="sort" label="顺序">
        <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :label="$t('menu.sort')"></el-input-number>
      </el-form-item>
<!--              <el-form-item label="修改人" prop="updater">-->
<!--          <el-input v-model="dataForm.updater" placeholder="修改人"></el-input>-->
<!--      </el-form-item>-->
<!--          <el-form-item label="更新时间" prop="updateDate">-->
<!--          <el-input v-model="dataForm.updateDate" placeholder="更新时间"></el-input>-->
<!--      </el-form-item>-->
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
        type: '',
        name: '',
        sort: '',
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
        type: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        name: [
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
      this.$http.get(`/takeout/category/${this.dataForm.id}`).then(({ data: res }) => {
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
        this.$http[!this.dataForm.id ? 'post' : 'put']('/takeout/category/', this.dataForm).then(({ data: res }) => {
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
