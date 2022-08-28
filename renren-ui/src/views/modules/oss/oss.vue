<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-oss__oss">
      <el-form :inline="true" :model="dataForm">
        <el-form-item>
          <el-button type="primary" @click="configHandle()">{{ $t('oss.config') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="uploadHandle()">{{ $t('oss.upload') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="dataListLoading"
        :data="dataList"
        border
        @selection-change="dataListSelectionChangeHandle"
        @sort-change="dataListSortChangeHandle"
        style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="url" :label="$t('oss.url')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="createDate" :label="$t('oss.createDate')" sortable="custom" header-align="center" align="center" width="180"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
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
      <!-- 弹窗, 云存储配置 -->
      <config v-if="configVisible" ref="config"></config>
      <!-- 弹窗, 上传文件 -->
      <upload v-if="uploadVisible" ref="upload" @refreshDataList="getDataList"></upload>
    </div>
  </el-card>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import Config from './oss-config'
import Upload from './oss-upload'
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/sys/oss/page',
        getDataListIsPage: true,
        deleteURL: '/sys/oss',
        deleteIsBatch: true
      },
      dataForm: {},
      configVisible: false,
      uploadVisible: false
    }
  },
  components: {
    Config,
    Upload
  },
  methods: {
    // 云存储配置
    configHandle () {
      this.configVisible = true
      this.$nextTick(() => {
        this.$refs.config.init()
      })
    },
    // 上传文件
    uploadHandle () {
      this.uploadVisible = true
      this.$nextTick(() => {
        this.$refs.upload.init()
      })
    }
  }
}
</script>
