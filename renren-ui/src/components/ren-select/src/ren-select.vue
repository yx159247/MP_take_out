<template>
    <el-select :value="value+''" @input="$emit('input', $event)" :placeholder="placeholder" clearable>
        <el-option :label="data.dictLabel" v-for="data in dataList" :key="data.dictValue" :value ="data.dictValue">{{data.dictLabel}}</el-option>
    </el-select>
</template>
<script>
import { getDictDataList } from '@/utils'
export default {
  name: 'RenSelect',
  data () {
    return {
      dataList: getDictDataList(this.dictType)
    }
  },
  props: {
    value: [Number, String],
    dictType: String,
    placeholder: String
  }
}
</script>
