<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false"
             :close-on-press-escape="true">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()"
             :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
<!--      <el-form-item label="用户ID" prop="userId">-->
<!--        <el-input v-model="dataForm.userId" placeholder="用户id"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="用户ID" prop="userId">
      <el-autocomplete
          v-model="dataForm.userId"
          :fetch-suggestions="querySearch"
          placeholder="请输入内容"
          @select="handleSubmit"
          :trigger-on-focus="false"
          clearable
      ></el-autocomplete>
      </el-form-item>

      <el-form-item label="收货人" prop="consignee">
        <el-input v-model="dataForm.consignee" placeholder="收货人"></el-input>
      </el-form-item>
<!--      <el-form-item label="性别" prop="gender">-->
<!--        <el-input v-model="dataForm.gender" placeholder="性别 0 男 1 女"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item prop="gender" :label="$t('user.gender')">
        <el-radio-group v-model="dataForm.gender">
          <el-radio :label="0">男</el-radio>
          <el-radio :label="1">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="dataForm.phone" placeholder="手机号"></el-input>
      </el-form-item>
<!--      <el-form-item label="省级编号" prop="provinceCode">-->
<!--        <el-input v-model="dataForm.provinceCode" placeholder="省级区划编号"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="省级名称" prop="provinceName">
        <el-input v-model="dataForm.provinceName" placeholder="省级名称"></el-input>
      </el-form-item>
<!--      <el-form-item label="市级编号" prop="cityCode">-->
<!--        <el-input v-model="dataForm.cityCode" placeholder="市级区划编号"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="市级名称" prop="cityName">
        <el-input v-model="dataForm.cityName" placeholder="市级名称"></el-input>
      </el-form-item>
<!--      <el-form-item label="区级编号" prop="districtCode">-->
<!--        <el-input v-model="dataForm.districtCode" placeholder="区级区划编号"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="区级名称" prop="districtName">
        <el-input v-model="dataForm.districtName" placeholder="区级名称"></el-input>
      </el-form-item>
      <el-form-item label="详细地址" prop="detail">
        <el-input v-model="dataForm.detail" placeholder="详细地址"></el-input>
      </el-form-item>
      <el-form-item label="标签" prop="label">
<!--        <el-input v-model="dataForm.label" placeholder="标签"></el-input>-->
        <el-radio-group v-model="dataForm.label" size="small">
          <el-radio-button label="无"></el-radio-button>
          <el-radio-button label="家" ></el-radio-button>
          <el-radio-button label="学校"></el-radio-button>
          <el-radio-button label="公司"></el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="默认地址" prop="isDefault">
<!--        <el-input v-model="dataForm.isDefault" placeholder="默认 0 否 1是"></el-input>-->
        <el-radio-group v-model="dataForm.isDefault">
          <el-radio :label="0">否</el-radio>
          <el-radio :label="1">是</el-radio>
        </el-radio-group>
      </el-form-item>
<!--      <el-form-item label="修改人" prop="updater">-->
<!--        <el-input v-model="dataForm.updater" placeholder="修改人"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="更新时间" prop="updateDate">-->
<!--        <el-input v-model="dataForm.updateDate" placeholder="更新时间"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="是否删除" prop="isDeleted">-->
<!--        <el-input v-model="dataForm.isDeleted" placeholder="是否删除"></el-input>-->
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
  data() {
    return {
      visible: false,
      dataForm: {
        id: '',
        userId: '',
        consignee: '',
        gender: '',
        phone: '',
        provinceCode: '',
        provinceName: '',
        cityCode: '',
        cityName: '',
        districtCode: '',
        districtName: '',
        detail: '',
        label: '无',
        isDefault: '',
        creator: '',
        createDate: '',
        updater: '',
        updateDate: '',
        isDeleted: ''
      },
      userList:[],

    }
  },
  computed: {
    dataRule() {
      return {
        userId: [
          {required: true, message: this.$t('validate.required'), trigger: 'blur'}
        ],
        consignee: [
          {required: true, message: this.$t('validate.required'), trigger: 'blur'}
        ],
        gender: [
          {required: true, message: this.$t('validate.required'), trigger: 'blur'}
        ],
        phone: [
          {required: true, message: this.$t('validate.required'), trigger: 'blur'}
        ],

        detail: [
          {required: true, message: this.$t('validate.required'), trigger: 'blur'}
        ],

      }
    }
  },
  methods: {

    init() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.getInfo()
        }
      })
    },

    handleSubmit() {
      //根据自身情况
      //该方法是提交时触发

    },
    //输入框获取焦点时调用的方法
    querySearch(queryString, cb) {
      //allInfos是怎么来，是从父组件传过来的还是在自己组件调用api接口拿到的还是其他
      //我这里的allInfos是从父组件传过来的，在这里也举例子组件从父组件传值
      let results = []
      this.$http.get('/takeout/user/page').then(({data :res})=>{
        if (res.code !== 0 ){
          return this.$message.error(res.msg)
        }
        this.userList = res.data.list.map((terminal) =>{
          return {
            value: terminal.id,
            nickName: terminal.nickName,
          };
        })
        console.log(this.userList)
        results = this.userList
        console.log("results",results)
        results = queryString
            ? results.filter(this.createFilter(queryString))
            : results;
        //cb是回调函数，返回筛选出的结果数据到输入框下面的输入列表
        cb(results);
      }).catch(() => {
      })

    },
    //该方法仿写官方文档，如果没有特别的需求，该方法改动不大
    //这是当输入数据时触发的，筛选出和输入数据匹配的建议输入。
    //我这里用的是调用match方法，是模糊匹配；官方调用的是indexOf，是精确匹配，看自身情况选择
    createFilter(queryString) {
      return (item) => {
        return item.value.toUpperCase().match(queryString.toUpperCase());
      }
    },
      // 获取信息
    getInfo() {
      this.$http.get(`/takeout/addressbook/${this.dataForm.id}`).then(({data: res}) => {
        if (res.code !== 0) {
          return this.$message.error(res.msg)
        }
        this.dataForm = {
          ...this.dataForm,
          ...res.data
        }
      }).catch(() => {
      })
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        this.$http[!this.dataForm.id ? 'post' : 'put']('/takeout/addressbook/', this.dataForm).then(({data: res}) => {
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
        }).catch(() => {
        })
      })
    }, 1000, {'leading': true, 'trailing': false})
  }
}
</script>
