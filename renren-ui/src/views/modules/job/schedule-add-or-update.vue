<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="beanName" :label="$t('schedule.beanName')">
        <el-input v-model="dataForm.beanName" :placeholder="$t('schedule.beanNameTips')"></el-input>
      </el-form-item>
      <el-form-item prop="params" :label="$t('schedule.params')">
        <el-input v-model="dataForm.params" :placeholder="$t('schedule.params')"></el-input>
      </el-form-item>
      <el-form-item prop="cronExpression" :label="$t('schedule.cronExpression')">
        <el-popover v-model="cronPopover">
          <cron @change="changeCron" @close="cronPopover=false" i18n="cn"></cron>
          <el-input slot="reference" @click="cronPopover=true" v-model="dataForm.cronExpression" :placeholder="$t('schedule.cronExpressionTips')"></el-input>
        </el-popover>
      </el-form-item>
      <el-form-item prop="remark"  :label="$t('schedule.remark')">
        <el-input v-model="dataForm.remark" :placeholder="$t('schedule.remark')"></el-input>
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
import { cron } from 'vue-cron'
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        id: '',
        beanName: '',
        params: '',
        cronExpression: '',
        remark: '',
        status: 0
      },
      cronPopover: false
    }
  },
  components: {
    cron
  },
  computed: {
    dataRule () {
      return {
        beanName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        cronExpression: [
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
    changeCron (val) {
      this.dataForm.cronExpression = val
    },
    // 获取信息
    getInfo () {
      this.$http.get(`/sys/schedule/${this.dataForm.id}`).then(({ data: res }) => {
        if (res.code !== 0) {
          return this.$message.error(res.msg)
        }
        this.dataForm = res.data
      }).catch(() => {})
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        this.$http[!this.dataForm.id ? 'post' : 'put']('/sys/schedule', this.dataForm).then(({ data: res }) => {
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
