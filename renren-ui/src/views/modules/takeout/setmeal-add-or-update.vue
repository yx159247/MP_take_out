<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false"
             :close-on-press-escape="true">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()"
             :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
      <div>
        <el-form-item label="套餐名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="请填写套餐名称" maxlength="20"/>
        </el-form-item>
        <el-form-item label="套餐分类" prop="idType">
          <el-select v-model="dataForm.idType" placeholder="请选择套餐分类" @change="$forceUpdate()">
            <el-option v-for="(item, index) in setMealList" :key="index" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
      </div>
      <div>
        <el-form-item label="套餐价格" prop="price">
          <el-input v-model="dataForm.price" placeholder="请设置套餐价格"/>
        </el-form-item>
      </div>
      <div>
        <el-form-item label="套餐菜品" class="setmealFood">
          <el-form-item>
            <div class="addDish">
              <span v-if="dishTable.length == 0" class="addBut" @click="openAddDish"> + 添加菜品</span>
              <div v-if="dishTable.length != 0" class="content">
                <div class="addBut" style="margin-bottom: 20px" @click="openAddDish">+ 添加菜品</div>
                <div class="table">
                  <el-table :data="dishTable" style="width: 100%">
                    <el-table-column prop="name" label="名称" width="180" align="center"></el-table-column>
                    <el-table-column prop="price" label="原价" width="180">
                      <template slot-scope="scope"> {{ Number(scope.row.price) / 100 }}</template>
                    </el-table-column>
                    <el-table-column prop="address" label="份数" align="center">
                      <template slot-scope="scope">
                        <el-input-number
                            v-model="scope.row.copies"
                            size="small"
                            :min="1"
                            :max="99"
                            label="描述文字"
                        ></el-input-number>
                      </template>
                    </el-table-column>
                    <el-table-column prop="address" label="操作" width="180px;" align="center">
                      <template slot-scope="scope">
                        <el-button type="text" size="small" @click="delDishHandle(scope.$index)"> 删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </div>
            </div>
          </el-form-item>
        </el-form-item>
      </div>
      <div>
        <el-form-item label="套餐图片" class="uploadImg">
          <el-upload
              class="avatar-uploader"
              :action="uploadUrl"
              :headers="headerObj"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :on-change="onChange"
              ref="upload"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar"></img>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item prop="status" :label="$t('user.status')" size="mini">
          <el-radio-group v-model="dataForm.status">
            <el-radio :label="1">启售</el-radio>
            <el-radio :label="0">停售</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>
      <el-form-item label="套餐描述">
        <el-input v-model="dataForm.description" type="textarea" :rows="3" placeholder="套餐描述，最长200字" maxlength="200"/>
      </el-form-item>
      <el-dialog
          title="添加菜品"
          class="addDishList"
          :visible.sync="dialogVisible"
          width="60%"
          :before-close="handleClose"
          :modal="false"
      >
        <el-input
            v-model="value"
            class="seachDish"
            placeholder="请输入菜品名称进行搜索"
            style="width: 250px"
            size="small"
            clearable
        >
          <i slot="prefix" class="el-input__icon el-icon-search" style="cursor: pointer" @click="seachHandle"></i>
        </el-input>
        <!-- <AddDish ref="adddish" :check-list="checkList" :seach-key="seachKey" @checkList="getCheckList" /> -->

        <div class="addDishCon">
          <div class="leftCont">
            <div
                v-show="seachKey.trim() == ''"
                class="tabBut"
            >
              <span
                  v-for="(item, index) in dishType"
                  :key="index"
                  :class="{act:index == keyInd}"
                  @click="checkTypeHandle(index, item.id)"
              >{{ item.name }}</span>
            </div>
            <div class="tabList">
              <div class="table">
                <div v-if="dishAddList.length == 0" style="padding-left:10px">
                  暂无菜品!
                </div>
                <el-checkbox-group
                    v-if="dishAddList.length > 0"
                    v-model="checkedList"
                    @change="checkedListHandle"
                >
                  <div
                      v-for="(item, index) in dishAddList"
                      :key="index"
                      class="items"
                  >
                    <el-checkbox
                        :key="index"
                        :label="item"
                    >
                      <div class="item">
                        <span style="flex: 3;text-align: left">{{ item.dishName }}</span>
                        <span>{{ item.status == 0 ? '停售' : '在售' }}</span>
                        <span>{{ Number(item.price) / 100 }}</span>
                      </div>
                    </el-checkbox>
                  </div>
                </el-checkbox-group>
              </div>
            </div>
          </div>
          <div class="ritCont">
            <div class="tit">
              已选菜品({{ checkedList.length }})
            </div>
            <div class="items">
              <div
                  v-for="(item, ind) in checkedList"
                  :key="ind"
                  class="item"
              >
                <span>{{ item.dishName }}</span>
                <span class="price">￥ {{ Number(item.price) / 100 }} </span>
                <span
                    class="del"
                    @click="delCheck(ind)"
                >
                  <img
                      src="../../../assets/icons/btn_clean@2x.png"
                      alt=""
                  >
                </span>
              </div>
            </div>
          </div>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="handleClose">取 消</el-button>
          <el-button type="primary" @click="addTableList">确 定</el-button>
        </span>
      </el-dialog>
    </el-form>
    <template slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
    </template>
  </el-dialog>
</template>

<script>
import debounce from 'lodash/debounce'
import QiniuUrl from "@/utils/QiniuUrl";
import Cookies from "js-cookie";
import {getCategoryList, queryDishList} from "@/api/dish/dish";

export default {
  data() {
    return {
      uploadUrl: '',
      visible: false,
      dataForm: {
        name: '',
        categoryId: '',
        price: '',
        code: '',
        image: '',
        description: '',
        dishList: [],
        status: '1',
        idType: '',
      },
      headerObj: {
        token: Cookies.get('token')
      },
      QiNiuYunUrl: QiniuUrl,
      id: '',
      value: '',
      setMealList: [],
      seachKey: '',
      dishList: [],
      imageUrl: '',
      dishTable: [],
      dialogVisible: false,
      checkList: [],

      dishType: [],
      dishAddList: [],
      dishListCache: [],
      keyInd: 0,
      searchValue: '',
      checkedList: [],
    }
  },
  computed: {
    dataRule() {
      return {
        name: {
          required: true,
          message: '请输入套餐名称',
          trigger: 'blur',
        },
        idType: {
          required: true,
          message: '请选择套餐分类',
          trigger: 'change',
        },
        price: {
          required: true,
          // 'message': '请输入套餐价格',
          validator: (rules, value, callback) => {
            if (!value) {
              callback(new Error('请填写菜品价格'))
            } else {
              const reg = /^\d+(\.\d{0,2})?$/
              if (reg.test(value)) {
                if (value < 10000) {
                  callback()
                } else {
                  callback(new Error('菜品价格小于10000'))
                }
              } else {
                callback(new Error('菜品价格格式只能为数字,且最多保留两位小数'))
              }
            }
          },
          trigger: 'blur',
        },
      }
    }
  },
  created() {
    this.getDishTypeList()
    this.getDishType()
  },
  watch:{
    seachKey(value){
      if (value.trim()){
        this.getDishForName(this.seachKey)
      }
    },
    checkList(value){
      this.checkedList = value
    }
  },
  methods: {
    init() {
      this.uploadUrl = `${window.SITE_CONFIG['apiURL']}/sys/oss/upload?token=${Cookies.get('token')}`
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.getInfo()
        }
      })
    },
    // 获取信息
    getInfo() {
      this.$http.get(`/takeout/setmeal/${this.dataForm.id}`).then(({data: res}) => {
        if (res.code !== 0) {
          return this.$message.error(res.msg)
        }
        this.dataForm = {
          ...this.dataForm,
          ...res.data
        }

        this.dataForm.price = res.data.price / 100

        this.imageUrl = this.dataForm.image;
        this.checkList = res.data.setmealDishes
        this.dishTable = res.data.setmealDishes
        this.dataForm.idType = res.data.categoryId
      }).catch(() => {
      })
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        let prams = { ...this.dataForm }
        prams.price *= 100
        prams.setmealDishes = this.dishTable.map((obj) => ({
          copies: obj.copies,
          dishId: obj.dishId,
          name: obj.name,
          price: obj.price,
        }))
        prams.status = this.dataForm ? 1 : 0
        prams.categoryId = this.dataForm.idType
        if(prams.setmealDishes.length < 1){
          this.$message.error('请选择菜品！')
          return
        }
        if(!this.imageUrl){
          this.$message.error('请上传套餐图片')
          return
        }
        this.$http[!this.dataForm.id ? 'post' : 'put']('/takeout/setmeal/', prams).then(({data: res}) => {
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
    }, 1000, {'leading': true, 'trailing': false}),

    handleAvatarSuccess(response, file, fileList) {
      if(response.code === 0){
        this.imageUrl = response.data.src
        this.dataForm.image = this.imageUrl
        this.$message({
            message: this.$t('prompt.uploadSuccess'),
            type: 'success',
            duration: 1000,
          })
      } else{
        this.$message({
            message: this.$t('prompt.uploadFailed'),
            type: 'error',
            duration: 1000,
          })
      }
    },

    onChange(file) {
      if (file) {
        const suffix = file.name.split('.')[1]
        const size = file.size / 1024 / 1024 < 2
        if (['png', 'jpeg', 'jpg'].indexOf(suffix) < 0) {
          this.$message.error('上传图片只支持 png、jpeg、jpg 格式！')
          this.$refs.upload.clearFiles()
          return false
        }
        if (!size) {
          this.$message.error('上传文件大小不能超过 2MB!')
          return false
        }
        return file
      }
    },
    // 添加菜品
    openAddDish() {
      this.seachKey = ''
      this.dialogVisible = true
      //搜索条件清空，菜品重新查询，菜品类别选第一个重新查询
      this.value = ''
      this.keyInd = 0
      this.getDishList(this.dishType[0].id)
    },
    getDishTypeList() {
      getCategoryList({type: 2}).then(({data : res}) => {
        if (res.code === 0) {
          this.setMealList = res.data.map((obj) => ({ ...obj, idType: obj.id }))
        } else {
          this.$message.error(res.msg || '操作失败')
        }
      })
    },
    delDishHandle(index) {
      this.dishTable.splice(index, 1)
      this.checkList.splice(index, 1)
    },
// 取消添加菜品
    handleClose(done) {
      // this.$refs.adddish.close()
      this.dialogVisible = false
      // this.checkList = JSON.parse(JSON.stringify(this.dishTable))
      // this.dialogVisible = false
    },
    seachHandle() {
      this.seachKey = this.value
    },
    checkTypeHandle(ind, id) {
      this.keyInd = ind
      this.getDishList(id)
    },
    checkedListHandle(value) {
      this.getCheckList(this.checkedList)
    },
    // 获取添加菜品数据
    getCheckList(value) {
      this.checkList = [...value]
    },
    // 删除
    delCheck(ind) {
      this.checkedList.splice(ind, 1)
    },
    addTableList() {
      this.dishTable = JSON.parse(JSON.stringify(this.checkList))
      this.dishTable.forEach((n) => {
        n.copies = 1
      })
      this.dialogVisible = false
      // 添加处理逻辑清空选中list
      this.checkList = []
    },

    // 获取套餐分类
    getDishType () {
      getCategoryList({'type':1}).then(({data : res}) => {
        if (res.code === 0) {
          this.dishType = res.data
          this.getDishList(res.data[0].id)
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    // 通过套餐ID获取菜品列表分类
    getDishList (id) {
      queryDishList({categoryId: id}).then(({data : res}) => {
        if (res.code === 0) {
          if (res.data.length == 0) {
            this.dishAddList = []
            return
          }
          let newArr = res.data;
          newArr.forEach((n) => {
            n.dishId = n.id
            n.copies = 1
            // n.dishCopies = 1
            n.dishName = n.name
          })
          this.dishAddList = newArr
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    // 关键词收搜菜品列表分类
    getDishForName (name) {
      queryDishList({name}).then(({data : res}) => {
        if (res.code === 0) {
          let newArr = res.data
          newArr.forEach((n) => {
            n.dishId = n.id
            n.dishName = n.name
          })
          this.dishAddList = newArr
        } else {
          this.$message.error(res.msg)
        }
      })
    },


  },

}
</script>
<style>
.addDish .el-input {
  width: 130px;
}

.addDish .el-input-number__increase {
  border-left: solid 1px #FFE1CA;
  background: #fff3ea;
}

.addDish .el-input-number__decrease {
  border-right: solid 1px #FFE1CA;
  background: #fff3ea;
}

.addDish input {
  border: 1px solid #ffe1ca;
}

.addDish .table {
  border: solid 1px #EBEEF5;
  border-radius: 3px;
}

.addDish .table th {
  padding: 5px 0;
}

.addDish .table td {
  padding: 7px 0;
}

.addDishList .seachDish {
  position: absolute;
  top: 10px;
  right: 20px;
}

.addDishList .el-dialog__body {
  padding: 0;
  border-bottom: solid 1px #ccc;
}

.addDishList .el-dialog__footer {
  padding-top: 27px;
}

.addDish {
  width: 777px;
}

.addDish .addBut {
  background: #409eff;
  display: inline-block;
  padding: 0px 20px;
  border-radius: 3px;
  line-height: 40px;
  cursor: pointer;
  border-radius: 4px;
  color: #ffffff;
  font-weight: 500;
}

.addDish .content {
  background: #fafafb;
  padding: 20px;
  border: solid 1px #ccc;
  border-radius: 3px;
}

.addDishCon {
  padding: 0 20px;
  display: flex;
  line-height: 40px;
}

.addDishCon .leftCont {
  display: flex;
  border-right: solid 2px #E4E7ED;
  width: 60%;
  padding: 15px;
}

.addDishCon .leftCont .tabBut {
  width: 110px;
}

.addDishCon .leftCont .tabBut span {
  display: block;
  text-align: center;
  border-right: solid 2px #f4f4f4;
  cursor: pointer;
}

.addDishCon .leftCont .act {
  border-color: #FFC200 !important;
  color: #FFC200 !important;
}

.addDishCon .leftCont .tabList {
  flex: 1;
  padding: 15px;
}

.addDishCon .leftCont .tabList .table {
  border: solid 1px #f4f4f4;
  border-bottom: solid 1px #f4f4f4;
}

.addDishCon .leftCont .tabList .table .items {
  border-bottom: solid 1px #f4f4f4;
  padding: 0 10px;
  display: flex;
}

.addDishCon .leftCont .tabList .table .items .el-checkbox, .addDishCon .leftCont .tabList .table .items .el-checkbox__label {
  width: 100%;
}

.addDishCon .leftCont .tabList .table .items .item {
  display: flex;
  padding-right: 20px;
}

.addDishCon .leftCont .tabList .table .items .item span {
  display: inline-block;
  text-align: center;
  flex: 1;
}

.addDishCon .ritCont {
  width: 40%;
  padding: 0 15px;
}

.addDishCon .ritCont .item {
  box-shadow: 0px 1px 4px 3px rgba(0, 0, 0, 0.03);
  display: flex;
  text-align: center;
  padding: 0 10px;
  margin-bottom: 20px;
  border-radius: 6px;
  color: #818693;
}

.addDishCon .ritCont .item span:first-child {
  text-align: left;
  color: #20232A;
}

.addDishCon .ritCont .item .price {
  display: inline-block;
  flex: 1;
}

.addDishCon .ritCont .item .del {
  cursor: pointer;
}

.addDishCon .ritCont .item .del img {
  position: relative;
  top: 5px;
  width: 20px;
}

.addDishCon .el-checkbox__label {
  width: 100%;
}

.setmealFood .el-form-item__label::before {
  content: '*';
  color: #F56C6C;
  margin-right: 4px;
}

.uploadImg .el-form-item__label::before {
  content: '*';
  color: #F56C6C;
  margin-right: 4px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #feca50;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
