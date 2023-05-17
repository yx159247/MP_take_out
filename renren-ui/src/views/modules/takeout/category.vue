<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-takeout__category}">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.id" placeholder="id" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">{{ $t('query') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="exportHandle()">{{ $t('export') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('takeout:category:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('takeout:category:delete')" type="danger" @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="id" label="ID" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="type" label="类型   1 菜品分类 2 套餐分类" header-align="center" align="center"></el-table-column>-->

        <el-table-column
            align="center"
            prop="type"
            label="分类类型"
        >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 1" size="small" type="success">菜品分类</el-tag>
            <el-tag v-else size="small" type="warning">套餐分类</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="分类名称" header-align="center" align="center"></el-table-column>
        <el-table-column prop="sort" label="顺序" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="creator" label="创建人" header-align="center" align="center"></el-table-column>-->
        <el-table-column prop="createDate" label="创建时间" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="updater" label="修改人" header-align="center" align="center"></el-table-column>-->
        <el-table-column prop="updateDate" label="更新时间" header-align="center" align="center"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="$hasPermission('takeout:category:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">{{ $t('update') }}</el-button>
            <el-button v-if="$hasPermission('takeout:category:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
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
import AddOrUpdate from './category-add-or-update'
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/takeout/category/page',
        getDataListIsPage: true,
        exportURL: '/takeout/category/export',
        deleteURL: '/takeout/category',
        deleteIsBatch: true,

      },
      order: 'asc',
      orderField: 'sort',
      dataForm: {
        id: ''
      }
    }
  },
  components: {
    AddOrUpdate
  }
}
</script>
