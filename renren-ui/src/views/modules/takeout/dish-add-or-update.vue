<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false"
             :close-on-press-escape="true" >
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()"
             :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
      <el-form-item label="菜品名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="请填写菜品名称"></el-input>
      </el-form-item>
      <!--      <el-form-item label="菜品分类" prop="categoryId">-->
      <!--        <el-input v-model="dataForm.categoryId" placeholder="菜品分类id"></el-input>-->
      <!--      </el-form-item>-->
      <el-form-item
          label="菜品分类"
          prop="categoryId"
      >
        <el-select
            v-model="dataForm.categoryId"
            placeholder="请选择菜品分类"
        >
          <el-option v-for="(item,index) in dishList" :key="index" :label="item.name" :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="菜品价格" prop="price">
        <el-input v-model="dataForm.price" placeholder="菜品价格"></el-input>
      </el-form-item>
      <!--      <el-form-item label="商品码" prop="code">-->
      <!--        <el-input v-model="dataForm.code" placeholder="商品码"></el-input>-->
      <!--      </el-form-item>-->
      <el-form-item label="口味做法配置:">
        <el-form-item>
          <div class="flavorBox">
              <span
                  v-if="dishFlavors.length == 0"
                  class="addBut"
                  @click="addFlavore"
              > + 添加口味</span>
            <div
                v-if="dishFlavors.length != 0"
                class="flavor"
            >
              <div class="title">
                <span>口味名（3个字内）</span><span>口味标签（输入标签回车添加）</span>
              </div>
              <div class="cont">
                <div
                    v-for="(item, index) in dishFlavors"
                    :key="index"
                    class="items"
                >
                  <div class="itTit">
                    <!-- <SelectInput
                      :dish-flavors-data="dishFlavorsData"
                      :index="index"
                      :value="item.name"
                      @select="selectHandle"
                    /> -->
                    <div class="selectInput">
                      <div>
                        <el-input
                            v-model="item.name"
                            type="text"
                            style="width: 100%"
                            placeholder="请输入口味"
                            @focus="selectFlavor(true,index)"
                            @blur="outSelect(false,index)"
                            @input="inputHandle(index)"
                        />
                      </div>
                      <div v-show="item.showOption" class="flavorSelect">
                          <span
                              v-for="(it, ind) in dishFlavorsData"
                              :key="ind"
                              class="items"
                              @click="checkOption(it,ind,index)"
                          >{{ it.name }}</span>
                        <span
                            v-if="dishFlavorsData == []"
                            class="none"
                        >无数据</span>
                      </div>
                    </div>
                  </div>
                  <div
                      class="labItems"
                      style="display: flex"
                  >
                      <span
                          v-for="(it, ind) in item.value"
                          :key="ind"
                      >{{ it }} <i @click="delFlavorLabel(index, ind)">X</i></span>
                    <div
                        class="inputBox"
                        :style="inputStyle"
                        contenteditable="true"
                        @focus="flavorPosition(index)"
                        @keydown.enter="(val)=>keyDownHandle(val,index)"
                    ></div>
                  </div>
                  <span
                      class="delFlavor delBut non"
                      @click="delFlavor(index)"
                  >删除</span>
                </div>
              </div>
              <div
                  class="addBut"
                  @click="addFlavore"
              >
                添加口味
              </div>
            </div>
          </div>
        </el-form-item>
      </el-form-item>
      <!--      <el-form-item label="菜品图片" prop="image">-->
      <!--        <el-input v-model="dataForm.image" placeholder="图片"></el-input>-->
      <!--      </el-form-item>-->
      <el-form-item
          label="菜品图片:"
          prop="region"

      >
        <el-upload
            class="avatar-uploader"
            :headers="headerObj"
            action="http://localhost:8080/api/takeout/common/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :on-change="onChange"
            ref="upload"
        >
          <img
              v-if="imageUrl"
              :src="imageUrl"
              class="avatar"
          />
          <i
              v-else
              class="el-icon-plus avatar-uploader-icon"
          ></i>
        </el-upload>
      </el-form-item>
      <!--      <el-form-item label="描述信息" prop="description">-->
      <!--        <el-input v-model="dataForm.description" placeholder="描述信息"></el-input>-->
      <!--      </el-form-item>-->
      <el-form-item prop="status" :label="$t('user.status')" size="mini">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="1">启售</el-radio>
          <el-radio :label="0">停售</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
          label="菜品描述:"
          prop="region"
      >
        <el-input
            v-model="dataForm.description"
            type="textarea"
            :rows="3"
            placeholder="菜品描述，最长200字"
            maxlength="200"
        />
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
import Cookies from "js-cookie";
import {getCategoryList} from "@/api/dish/dish";
import QiniuUrl from "@/utils/QiniuUrl";

export default {
  data() {
    return {
      visible: false,
      dishList: [],
      dishFlavors: [],
      dishFlavorsData: [],
      inputStyle: {'flex': 1},
      headerObj: {
        token: Cookies.get("token")
      },
      QiNiuYunUrl: QiniuUrl,
      imageUrl: '',


      dataForm: {
        id: '',
        name: '',
        categoryId: '',
        price: '',
        code: '',
        image: '',
        description: '',
        status: 1,
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
    dataRule() {
      return {
        'name': [
          {'required': true, 'message': '请填写菜品名称', 'trigger': 'blur'}
        ],
        'categoryId': [
          {'required': true, 'message': '请选择菜品分类', 'trigger': 'change'}
        ],
        'price': [
          {
            'required': true,
            // 'message': '请填写菜品价格',
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
            'trigger': 'blur'
          }
        ],
      }
    }
  },
  created() {

  },
  mounted() {


  },
  methods: {

    BeforeOpen(){
      // if (!this.dataForm.id) {
      //   this.dataForm = {}
      //   this.dishList = []
      //   this.dishFlavors = []
      //   this.dishFlavorsData = []
      //   this.getDishList()
      //   // 口味临时数据
      //   this.getFlavorListHand()
      //
      // }else {
      //   this.getDishList()
      //   // 口味临时数据
      //   this.getFlavorListHand()
      // }

    },
    init() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        this.getDishList()
        this.getFlavorListHand()
        if (this.dataForm.id) {
          this.getInfo()
          this.getFlavorListHand()
        }
      })
    },
    // 获取信息
    getInfo() {
      this.$http.get(`/takeout/dish/${this.dataForm.id}`).then(({data: res}) => {
        if (res.code !== 0) {
          return this.$message.error(res.msg)
        }
        this.dataForm = {
          ...this.dataForm,
          ...res.data
        }
        this.dataForm.price = String(res.data.price / 100)
        this.dishFlavors = res.data.flavors && res.data.flavors.map(obj => ({
          ...obj,
          value: JSON.parse(obj.value),
          showOption: false
        }))
        this.imageUrl = this.QiNiuYunUrl+this.dataForm.image;

      }).catch(() => {
      })
    },

    // 获取菜品分类
    getDishList() {
      getCategoryList({'type': 1}).then(({data: res}) => {
        if (res.code === 0) {
          this.dishList = res.data
        } else {
          this.$message.error(res.msg || '操作失败')
        }
      })
    },

    // 获取口味列表
    getFlavorListHand() {
      // flavor flavorData
      this.dishFlavorsData = [
        {'name': '甜味', 'value': ['无糖', '少糖', '半糖', '多糖', '全糖']},
        {'name': '温度', 'value': ['热饮', '常温', '去冰', '少冰', '多冰']},
        {'name': '忌口', 'value': ['不要葱', '不要蒜', '不要香菜', '不要辣']},
        {'name': '辣度', 'value': ['不辣', '微辣', '中辣', '重辣']}
      ]
    },
    // 按钮 - 添加口味
    addFlavore() {
      this.dishFlavors.push({'name': '', 'value': [], showOption: false}) // JSON.parse(JSON.stringify(this.dishFlavorsData))
    },

    // 按钮 - 删除口味
    delFlavor(ind) {
      this.dishFlavors.splice(ind, 1)
    },

    // 按钮 - 删除口味标签
    delFlavorLabel(index, ind) {
      this.dishFlavors[index].value.splice(ind, 1)
    },
// 添加口味标签
    keyDownHandle(val, index) {
      console.log('keyDownHandle----val', val)
      console.log('keyDownHandle----index', index)
      console.log('keyDownHandle----this.dishFlavors', this.dishFlavors)
      if (event) {
        event.cancelBubble = true
        event.preventDefault()
        event.stopPropagation()
      }

      if (val.target.innerText.trim() != '') {
        this.dishFlavors[index].value.push(val.target.innerText)
        val.target.innerText = ''
      }
    },

    handleAvatarSuccess(response, file, fileList) {
      // 拼接down接口预览
      if (response.code === 1 && response.msg === '未登录') {
        window.top.location.href = '/backend/page/login/login.html'
      } else {
        //this.imageUrl = `/common/download?name=${response.data}`

        this.imageUrl = this.QiNiuYunUrl + response.data
        this.dataForm.image = response.data
        console.log("imageUrl", this.imageUrl)
        console.log("image", this.dataForm.image)
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
    //口味位置记录
    flavorPosition(index) {
      this.index = index
    },
    selectFlavor(st, index) {
      console.log('st', st)
      console.log('index', index)
      console.log('this.dishFlavors', this.dishFlavors)
      const obj = JSON.parse(JSON.stringify(this.dishFlavors[index]))
      obj.showOption = st
      this.dishFlavors.splice(index, 1, obj)
      // this.dishFlavors[index].showOption = st
    },

    outSelect(st, index) {
      const _this = this
      setTimeout(() => {
        const obj = JSON.parse(JSON.stringify(_this.dishFlavors[index]))
        obj.showOption = st
        _this.dishFlavors.splice(index, 1, obj)
      }, 200)
    },

    inputHandle(val, index) {
      // this.selectFlavor(false,index)
    },

    checkOption(val, ind, index) {
      this.selectHandle(val.name, index, ind)
      this.dishFlavors[index].name = val.name
    },

    selectHandle(val, key, ind) {
      const arrDate = [...this.dishFlavors]
      arrDate[key] = JSON.parse(JSON.stringify(this.dishFlavorsData[ind]))
      this.dishFlavors = arrDate
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        let params = {...this.dataForm}
        // params.flavors = this.dishFlavors
        params.status = this.dataForm.status ? 1 : 0
        params.price *= 100
        params.categoryId = this.dataForm.categoryId
        params.flavors = this.dishFlavors.map(obj => ({...obj, value: JSON.stringify(obj.value)}))
        delete params.dishFlavors
        if (!this.imageUrl) {
          this.$message.error('请上传菜品图片')
          return
        }
        this.$http[!this.dataForm.id ? 'post' : 'put']('/takeout/dish/', params).then(({data: res}) => {
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
<style>

.selectInput {
  position: relative;
  width: 100%;
  min-width: 100px;
}

.selectInput .flavorSelect {
  position: absolute;
  width: 100%;
  padding: 0 10px;
  border-radius: 3px;
  border: solid 1px #FF903D;
  line-height: 30px;
  text-align: center;
  background: #fff;
  top: 50px;
  z-index: 99;
}

.selectInput .flavorSelect .items {
  cursor: pointer;
  display: inline-block;
  width: 100%;
  line-height: 35px;
  border-bottom: solid 1px #f4f4f4;
  color: #666;
}

.selectInput .flavorSelect .none {
  font-size: 14px;
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
  border-color: #409EFF;
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

.flavorBox {
  width: 777px;
}

.flavorBox .addBut {
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

.flavorBox .flavor {
  border: solid 1px #dfe2e8;
  border-radius: 3px;
  padding: 15px;
  background: #fafafb;
}

.flavorBox .flavor .title {
  color: #606168;
}

.flavorBox .flavor .cont .items {
  display: flex;
  margin: 10px 0;
}

.flavorBox .flavor .cont .items .itTit {
  width: 150px;
  margin-right: 15px;
}

.flavorBox .flavor .cont .items .itTit input {
  width: 100%;
  line-height: 40px;
  border-radius: 3px;
  padding: 0 10px;
}

.flavorBox .flavor .cont .items .labItems {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  border-radius: 3px;
  min-height: 39px;
  border: solid 1px #d8dde3;
  background: #fff;
  padding: 0 5px;
}

.flavorBox .flavor .cont .items .labItems span {
  display: inline-block;
  color: #f19c59;
  margin: 5px;
  line-height: 26px;
  height: 26px;
  padding: 0 10px;
  background: #fdf4eb;
  border-radius: 3px;
  border: solid 1px #fae2cd;
}

.flavorBox .flavor .cont .items .labItems span i {
  cursor: pointer;
  font-style: normal;
}

.flavorBox .flavor .cont .items .labItems .inputBox {
  display: inline-block;
  width: 100%;
  height: 36px;
  line-height: 36px;
  overflow: hidden;
}

.flavorBox .flavor .cont .items .delFlavor {
  display: inline-block;
  padding: 0 10px;
  color: #f19c59;
  cursor: pointer;
}
</style>
