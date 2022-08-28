<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="dictName" :label="$t('dict.dictName')">
        <el-input v-model="dataForm.dictName" :placeholder="$t('dict.dictName')"></el-input>
      </el-form-item>
      <el-form-item prop="dictType" :label="$t('dict.dictType')">
        <el-input v-model="dataForm.dictType" :placeholder="$t('dict.dictType')"></el-input>
      </el-form-item>
      <el-form-item prop="sort" :label="$t('dict.sort')">
        <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :label="$t('dict.sort')"></el-input-number>
      </el-form-item>
      <el-form-item prop="remark" :label="$t('dict.remark')">
        <el-input v-model="dataForm.remark" :placeholder="$t('dict.remark')"></el-input>
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
        dictName: '',
        dictType: '',
        sort: 0,
        remark: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        dictName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        dictType: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        sort: [
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
      this.$http.get(`/sys/dict/type/${this.dataForm.id}`).then(({ data: res }) => {
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
        this.$http[!this.dataForm.id ? 'post' : 'put']('/sys/dict/type', this.dataForm).then(({ data: res }) => {
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
