<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-takeout__orders}">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.id" placeholder="订单号" clearable></el-input>
        </el-form-item>
        <el-date-picker v-model="orderTime"
                        clearable
                        value-format="yyyy-MM-dd HH:mm:ss"
                        type="datetimerange"
                        placeholder="选择日期"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        :default-time="['00:00:00', '23:59:59']"
                        style="width: 400px;margin-left: 20px;margin-right: 10px"
        ></el-date-picker>
        <el-form-item>
          <el-button @click="getDataList()">{{ $t('query') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="exportHandle()">{{ $t('export') }}</el-button>
        </el-form-item>
<!--        <el-form-item>-->
<!--          <el-button v-if="$hasPermission('takeout:orders:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-button v-if="$hasPermission('takeout:orders:delete')" type="danger" @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
        <el-table-column prop="number" label="订单号" min-width="110"
                         align="center"></el-table-column>
        <el-table-column prop="订单状态" label="订单状态"
                         align="center">
          <template slot-scope="{ row }">
            <span>{{ getOrderType(row) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="用户"
                         align="center"></el-table-column>
        <el-table-column prop="phone" label="手机号"
                         align="center"></el-table-column>
        <el-table-column prop="address" label="地址" show-overflow-tooltip
                         align="center"></el-table-column>
        <el-table-column prop="orderTime" label="下单时间" min-width="100"
                         align="center"></el-table-column>
        <el-table-column prop="amount" label="实收金额"
                         align="center">
          <template slot-scope="{ row }">
            <span>￥{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="btn" label="操作"
                         align="center">
          <template slot-scope="{ row }">
            <el-button type="success" size="small" @click="goDetail(row)" class="blueBug">
              查看
            </el-button>
            <el-divider v-if="row.status === 2" direction="vertical"></el-divider>
            <el-button v-if="row.status === 2" type="warning" size="small"  @click="cancelOrDeliveryOrComplete(3, row.id)" class="blueBug">
              派送
            </el-button>
            <el-divider v-if="row.status === 3" direction="vertical"></el-divider>
            <el-button v-if="row.status === 3" type="primary" size="small" @click="cancelOrDeliveryOrComplete(4, row.id)" class="blueBug">
              完成
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="limit"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="pageSizeChangeHandle"
        @current-change="pageCurrentChangeHandle">
      </el-pagination>
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>

    <!-- 查看弹框部分 -->
    <el-dialog
        title="订单信息"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose"
    >
      <div class="info-box">
        <div class="item-box">
          <span class="label">订单号：</span>
          <span class="des">{{ diaForm.number }}</span>
        </div>
        <div class="item-box">
          <span class="label">订单状态：</span>
          <span class="des">{{ getOrderType(diaForm) }}</span>
        </div>
        <div class="item-box">
          <span class="label">收货人：</span>
          <span class="des">{{ diaForm.consignee }}</span>
        </div>
        <div class="item-box">
          <span class="label">联系电话：</span>
          <span class="des">{{ diaForm.phone }}</span>
        </div>
        <div class="item-box">
          <span class="label">地址：</span>
          <span class="des">{{ diaForm.address }}</span>
        </div>
        <div class="item-box">
          <span class="label">支付金额：</span>
          <span class="des">￥{{ diaForm.amount }}</span>
        </div>
        <div class="item-box">
          <span class="label">下单时间：</span>
          <span class="des">{{ diaForm.orderTime }}</span>
        </div>
        <el-table
            :data="orderDetails"
            height="200"
            border
            style="width: 100%">
          <el-table-column
              align="center"
              prop="name"
              label="商品"
              width="159">
          </el-table-column>
          <el-table-column
              align="center"
              prop="number"
              label="数量"
              width="159">
          </el-table-column>
          <el-table-column
              align="center"
              prop="amount"
              label="金额"
              width="159">
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

  </el-card>


</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import AddOrUpdate from './orders-add-or-update'
import {editOrderDetail, queryOrderDetailById} from '@/api/orders/orders'

export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/takeout/orders/page',
        getDataListIsPage: true,
        exportURL: '/takeout/orders/export',
        deleteURL: '/takeout/orders',
        deleteIsBatch: true
      },
      orderTime: '',

      tableData : [],
      orderDetails: [],
      dialogVisible: false,
      diaForm: {},
      dataForm: {
        id: '',
        beginTime: '',
        endTime: ''
      }
    }
  },
  components: {
    AddOrUpdate
  },
  watch:{
    orderTime (val) {
      if (val && val.length >= 2) {
        this.dataForm.beginTime = val[0]
        this.dataForm.endTime = val[1]
      } else {
        this.dataForm.beginTime = ''
        this.dataForm.endTime = ''
      }
    }
  },
  methods:{
    goDetail (row) {
      this.diaForm = {}
      this.dialogVisible = true
      this.diaForm = { ...row }

      queryOrderDetailById(row.id).then(({ data: res })=>{
        if (res.code === 0){

          this.orderDetails = res.data
          this.orderDetails.amount = this.orderDetails.amount * this.orderDetails.number

        }
      }).catch(err => {
        this.$message.error('请求出错了：' + err)
      })
    },
    handleClose () {
      this.dialogVisible = false
    },
    // 取消，派送，完成
    cancelOrDeliveryOrComplete (status, id) {
      this.$confirm('确认更改该订单状态?', '提示', {
        'confirmButtonText': '确定',
        'cancelButtonText': '取消',
        'type': 'warning'
      }).then(() => {
        editOrderDetail(params).then(({ data: res }) => {
          if (res.code === 0) {
            this.$message.success(status === 3 ? '订单已派送' : '订单已完成')
            this.query()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        }).catch(err => {
          this.$message.error('请求出错了：' + err)
        })
      })
      const params = {
        status,
        id
      }
    },
    getOrderType (row) {
      let str = ''
      switch(row.status){
        case 1:
          str =  '待付款'
          break;
        case 2:
          str =  '正在派送'
          break;
        case 3:
          str =  '已派送'
          break;
        case 4:
          str =  '已完成'
          break;
        case 5:
          str =  '已取消'
          break;

      }
      return str
    }
  }
}

</script>
<style>
.search-btn {
  margin-left: 20px;
}
.tableBar{
  justify-content: flex-start !important;
}
.info-box{
  margin: -15px 0px 20px;
}
.info-box .item-box{
  display: flex;
  height: 20px;
  line-height: 20px;
  font-size: 14px;
  font-weight: 400;
  color: #666666;
  text-align: left;
  margin-bottom: 14px;
}
.info-box .item-box:last-child{
  margin-bottom: 0;
}
.info-box .item-box .label{
  width: 96px;
}
.info-box .item-box .des{
  flex: 1;
  color: #333333;
}
</style>
