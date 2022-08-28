<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-takeout__addressbook}">
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
          <el-button v-if="$hasPermission('takeout:addressbook:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('takeout:addressbook:delete')" type="danger" @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="id" label="地址ID" header-align="center" align="center"></el-table-column>
        <el-table-column prop="userId" label="用户ID" header-align="center" align="center"></el-table-column>
        <el-table-column prop="consignee" label="收货人" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="gender" label="性别 0 男 1 女" header-align="center" align="center"></el-table-column>-->
        <el-table-column
            align="center"
            prop="gender"
            label="性别"
        >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.gender === 0" size="small" type="success">男</el-tag>
            <el-tag v-else size="small" type="warning">女</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" header-align="center" align="center"></el-table-column>
        <el-table-column prop="provinceCode" label="省级区划编号" header-align="center" align="center"></el-table-column>
        <el-table-column prop="provinceName" label="省级名称" header-align="center" align="center"></el-table-column>
        <el-table-column prop="cityCode" label="市级区划编号" header-align="center" align="center"></el-table-column>
        <el-table-column prop="cityName" label="市级名称" header-align="center" align="center"></el-table-column>
        <el-table-column prop="districtCode" label="区级区划编号" header-align="center" align="center"></el-table-column>
        <el-table-column prop="districtName" label="区级名称" header-align="center" align="center"></el-table-column>
        <el-table-column prop="detail" label="详细地址" header-align="center" align="center"></el-table-column>
        <el-table-column prop="label" label="标签" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="isDefault" label="默认 0 否 1是" header-align="center" align="center"></el-table-column>-->
<!--        <el-table-column prop="creator" label="创建人" header-align="center" align="center"></el-table-column>-->
        <el-table-column prop="createDate" label="创建时间" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="updater" label="修改人" header-align="center" align="center"></el-table-column>-->
        <el-table-column prop="updateDate" label="更新时间" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="isDeleted" label="是否删除" header-align="center" align="center"></el-table-column>-->
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="$hasPermission('takeout:addressbook:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">{{ $t('update') }}</el-button>
            <el-button v-if="$hasPermission('takeout:addressbook:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
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
import AddOrUpdate from './addressbook-add-or-update'

export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/takeout/addressbook/page',
        getDataListIsPage: true,
        exportURL: '/takeout/addressbook/export',
        deleteURL: '/takeout/addressbook',
        deleteIsBatch: true
      },
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
