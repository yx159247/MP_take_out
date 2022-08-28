<template>
  <el-radio-group :value="value+''" @input="$emit('input', $event)">
    <el-radio :label="data.dictValue" v-for="data in dataList" :key="data.dictValue">{{data.dictLabel}}</el-radio>
  </el-radio-group>
</template>
<script>
import { getDictDataList } from '@/utils'
export default {
  name: 'RenRadioGroup',
  data () {
    return {
      dataList: getDictDataList(this.dictType)
    }
  },
  props: {
    value: [Number, String],
    dictType: String
  }
}
</script>
