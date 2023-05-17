<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-takeout__dish}">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.id" placeholder="菜品名称" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">{{ $t('query') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="exportHandle()">{{ $t('export') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('takeout:dish:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('takeout:dish:delete')" type="danger" @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('takeout:dish:update')" type="warning" @click="updateStatusHandle(null,1)">{{ $t('updateStatusBatch') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('takeout:dish:update')" type="info" @click="updateStatusHandle(null,0)">{{ $t('updateStatusBatch_stop') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="id" label="菜品ID" header-align="center" align="center"></el-table-column>
        <el-table-column prop="name" label="菜品名称" header-align="center" align="center"></el-table-column>
        <el-table-column prop="categoryName" label="菜品分类" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="price" label="菜品价格" header-align="center" align="center"></el-table-column>-->
        <el-table-column
            align="center"
            prop="price"
            label="菜品价格"
        >
          <template slot-scope="scope">
            <span>￥{{ scope.row.price/100 }}</span>
          </template>
        </el-table-column>
<!--        <el-table-column prop="code" label="商品码" header-align="center" align="center"></el-table-column>-->
<!--        <el-table-column prop="image" label="图片" header-align="center" align="center"></el-table-column>-->
        <el-table-column  prop="image" label="图片" align="center">
          <template slot-scope="{ row }">
            <el-image style="width: auto; height: 40px; border:none;cursor: pointer;"
                      :src="row.image"
                      :preview-src-list="row.image" >
              <div slot="error" class="image-slot">
                <img src="../../../assets/img/noImg.png"  style="width: auto; height: 40px; border:none;" >
              </div>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述信息" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="status" label="状态" header-align="center" align="center"></el-table-column>-->
        <el-table-column prop="status" :label="$t('user.status')" sortable="custom" header-align="center" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 0" size="small" type="danger">{{ $t('user.status0') }}</el-tag>
            <el-tag v-else size="small" type="success">{{ $t('user.status1') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="顺序" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="creator" label="创建人" header-align="center" align="center"></el-table-column>-->
        <el-table-column prop="createDate" label="创建时间" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="updater" label="修改人" header-align="center" align="center"></el-table-column>-->
<!--        <el-table-column prop="updateDate" label="更新时间" header-align="center" align="center"></el-table-column>-->
<!--        <el-table-column prop="isDeleted" label="是否删除" header-align="center" align="center"></el-table-column>-->
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="$hasPermission('takeout:dish:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">{{ $t('update') }}</el-button>
            <el-button v-if="$hasPermission('takeout:dish:update')" type="text" size="small" @click="updateStatusHandle(scope.row.id)">{{ scope.row.status == '0' ? '启售' : '停售' }}</el-button>
            <el-button v-if="$hasPermission('takeout:dish:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>

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
import AddOrUpdate from './dish-add-or-update'

import debounce from "lodash/debounce";

export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/takeout/dish/page',
        getDataListIsPage: true,
        exportURL: '/takeout/dish/export',
        deleteURL: '/takeout/dish',
        deleteIsBatch: true,
        updateStatusIsBatch: true,
        updateStatusURL: '/takeout/dish/updateStatus'
      },
      dataForm: {
        id: ''
      }
    }
  },
  components: {
    AddOrUpdate
  },
  methods:{

    updateStatus(row){
      let params = {}
      params.id = row.id
      params.status = row.status ? '0' : '1'
      this.dishState = params
      this.$confirm('确认更改该菜品状态?', '提示', {
        'confirmButtonText': '确定',
        'cancelButtonText': '取消',
        'type': 'warning'
      }).then(() => {
        // 起售停售---批量起售停售接口
        this.$http.put('/takeout/dish/', params).then(({data: res}) => {
          if (res.code === 0) {
            this.$message.success('菜品状态已经更改成功！')
            this.query()
          } else {
            return this.$message.error(res.msg)
          }
        }).catch(err => {
          this.$message.error('请求出错了：' + err)
        })
      })
    }

  }
}
</script>
