<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-takeout__orderdetail}">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.id" placeholder="订单ID" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">{{ $t('query') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="exportHandle()">{{ $t('export') }}</el-button>
        </el-form-item>
<!--        <el-form-item>-->
<!--          <el-button v-if="$hasPermission('takeout:orderdetail:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-button v-if="$hasPermission('takeout:orderdetail:delete')" type="danger" @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="id" label="ID" header-align="center" align="center"></el-table-column>
        <el-table-column prop="name" label="名字" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="image" label="图片" header-align="center" align="center"></el-table-column>-->
        <el-table-column prop="image" label="图片"  align="center">
          align="center">
          <template slot-scope="{ row }">
            <el-image style="width: auto; height: 40px; border:none;cursor: pointer;" :src="row.image" :preview-src-list="[ row.image ]" >
              <div slot="error" class="image-slot">
                <img src="../../../assets/img/noImg.png"  style="width: auto; height: 40px; border:none;" >
              </div>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="orderId" label="订单ID" header-align="center" align="center"></el-table-column>
        <el-table-column prop="dishId" label="菜品ID" header-align="center" align="center"></el-table-column>
        <el-table-column prop="setmealId" label="套餐ID" header-align="center" align="center"></el-table-column>
        <el-table-column prop="dishFlavor" label="口味" header-align="center" align="center"></el-table-column>
        <el-table-column prop="number" label="数量" header-align="center" align="center"></el-table-column>
        <el-table-column prop="amount" label="金额" header-align="center" align="center"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
<!--            <el-button v-if="$hasPermission('takeout:orderdetail:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">{{ $t('update') }}</el-button>-->
            <el-button v-if="$hasPermission('takeout:orderdetail:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
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
  </el-card>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import AddOrUpdate from './orderdetail-add-or-update'

export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/takeout/orderdetail/page',
        getDataListIsPage: true,
        exportURL: '/takeout/orderdetail/export',
        deleteURL: '/takeout/orderdetail',
        deleteIsBatch: true
      },
      dataForm: {
        id: ''
      },

    }
  },
  components: {
    AddOrUpdate
  },
  methods:{

  }
}
</script>
