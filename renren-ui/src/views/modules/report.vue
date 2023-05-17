<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="always" class="box-card">
          <div class="card-icon-container">
            <i class="iconfont el-icon-mywenzhang-copy" style="color: #40C9C6"></i>
          </div>
          <div class="card-desc">
            <div class="card-title">今日订单量</div>
            <div class="card-count">{{toDayOrder}}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="always" class="box-card">
          <div class="card-icon-container">
            <i class="iconfont el-icon-mywenzhang-copy" style="color: #36A3F7"></i>
          </div>
          <div class="card-desc">
            <div class="card-title">昨日订单量</div>
            <div class="card-count">{{yDayOrder}}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="always" class="box-card">
          <div class="card-icon-container">
            <i class="iconfont el-icon-myuser" style="color: #34BFA3"></i>
          </div>
          <div class="card-desc">
            <div class="card-title">用户数量</div>
            <div class="card-count">{{userCount}}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="always" class="box-card">
          <div class="card-icon-container">
            <i class="iconfont el-icon-myfangwenliang" style="color: #F4516C"></i>
          </div>
          <div class="card-desc">
            <div class="card-title">员工数量</div>
            <div class="card-count">{{empCount}}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top:1.25rem">
      <div class="e-title">一周流水统计</div>
      <div id="oneWeekLiuShui" style="height:350px">
      </div>
    </el-card>
    <el-row :gutter="30" style="margin-top:1.25rem">
      <!-- 文章浏览量排行 -->
      <el-col :span="16">
        <el-card>
          <div class="e-title">一周订单统计</div>
          <div id="oneWeekOrder" style="height:350px">
          </div>
        </el-card>
      </el-col>
      <!-- 分类数据统计 -->
      <el-col :span="8">
        <el-card>
          <div class="e-title">热卖菜品统计</div>
          <div id="hotSeal" style="height:350px">
          </div>
        </el-card>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import * as echarts from 'echarts';
import {
  getOneWeekOrderApi,
  getToDayOrderApi,
  getYesDayOrderApi,
  getOneWeekLiuShuiApi,
  getHotSealApi,
  getUserCountApi, getEmpCountApi
} from "@/api/report/report";
export default {
  data() {
    return {

      loading: true,
      toDayOrder: null,
      yDayOrder: null,
      userCount: null,
      empCount: null,
      oneWeekLiuShui: {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross"
          }
        },
        color: ["#3888fa"],
        legend: {
          data: ["订单流水"]
        },

        xAxis: {
          lineStyle: {
            // 设置x轴颜色
            color: "#666"
          },
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        grid: {
          left: "0%",
          right: "0%",
          bottom: "0%",
          top: "10%",
          containLabel: true
        },
        yAxis: {
          axisLine: {
            lineStyle: {
              // 设置y轴颜色
              color: "#048CCE"
            }
          }
        },
        series: [
          {
            name: "金额",
            data: [null, null, null, null, null, null, null],
            type: 'line',
            smooth: true
          }
        ],
      },
      oneWeekOrder: {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross"
          }
        },
        color: ["#58AFFF"],
        grid: {
          left: "0%",
          right: "0%",
          bottom: "0%",
          top: "10%",
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [null, null, null, null, null, null, null],
            type: 'bar',
            showBackground: true,
            backgroundStyle: {
              color: 'rgba(180, 180, 180, 0.2)'
            }
          }
        ]
      },
      hotSeal:{
        color: [
          "#7EC0EE",
          "#FF9F7F",
          "#FFD700",
          "#C9C9C9",
          "#E066FF",
          "#C0FF3E"
        ],
        legend: {
          data: [],
          bottom: "bottom"
        },
        tooltip: {
          trigger: "item",
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        series: [
          {
            itemStyle: {
              borderRadius: 5
            },
            name: "菜品",
            radius: [20, 140],
            roseType: 'area',
            type: "pie",
            data: []
          }
        ]
      }


    }
  },
  computed: {},
  created() {
    this.getToDayOrder();
    this.getYDayOrder();
    this.getUserCount();
    this.getEmpCount();
    this.getOneWeekLiuShui();
    this.getOneWeekOrder();
    this.getHotSeal();

  },
  mounted() {
    setTimeout(()=>{
      this.initOneWeekLiuShui()
    },1000)
    setTimeout(()=>{
      this.initOneWeekOrder()
    },1000)
    setTimeout(()=>{
      this.initHotSeal()
    },1000)
  },


  methods: {

    getHotSeal(){
      getHotSealApi().then(({data:res})=>{
        if (res.code === 0){

          res.data.dishCount.forEach(item =>{
            this.hotSeal.series[0].data.push({
              value: item.dishCount,
              name: item.name,
            })
            this.hotSeal.legend.data.push(item.name)
          })
          res.data.setmealCount.forEach(item =>{
            this.hotSeal.series[0].data.push({
              value: item.setmealCount,
              name: item.name,
            })
            this.hotSeal.legend.data.push(item.name)
          })

        }
      })
    },

    getOneWeekOrder(){
      getOneWeekOrderApi().then(({data:res})=>{
        if (res.code === 0){
          this.oneWeekOrder.series[0].data = res.data.orderCount;
          this.oneWeekOrder.xAxis.data = res.data.days;
        }
      })
    },
    getOneWeekLiuShui(){
      getOneWeekLiuShuiApi().then(({data:res})=>{
        if (res.code === 0){
          this.oneWeekLiuShui.series[0].data = res.data.amount;
          this.oneWeekLiuShui.xAxis.data = res.data.days;
        }
      })
    },

    getEmpCount(){
      getEmpCountApi().then(({data:res})=>{
        if (res.code === 0){
          this.empCount= res.data
        }
      }).catch(err => {
        this.$message.error('请求出错了：' + err)
      })

    },
    getUserCount() {
      getUserCountApi().then(({data:res})=>{
        if (res.code === 0){
          this.userCount = res.data
        }
      }).catch(err => {
        this.$message.error('请求出错了：' + err)
      })

    },

    getYDayOrder(){
      getYesDayOrderApi().then(({data:res})=>{
        if (res.code === 0){
          this.yDayOrder = res.data
        }
      }).catch(err => {
        this.$message.error('请求出错了：' + err)
      })
    },
    getToDayOrder(){
      getToDayOrderApi().then(({data:res})=>{
        if (res.code === 0){
          this.toDayOrder = res.data
        }
      }).catch(err => {
        this.$message.error('请求出错了：' + err)
      })
    },

    initOneWeekLiuShui(){
      var myChart = echarts.init(document.getElementById('oneWeekLiuShui'));
      var option = this.oneWeekLiuShui
      myChart.setOption(option);
    },
    initOneWeekOrder(){
      var myChart = echarts.init(document.getElementById('oneWeekOrder'));
      var option = this.oneWeekOrder
      myChart.setOption(option);
    },
    initHotSeal(){
      var myChart = echarts.init(document.getElementById('hotSeal'));
      var option = this.hotSeal
      myChart.setOption(option);
    },



  },
}
</script>

<style scoped>
.blueBug {
  color: #419eff !important;
  position: relative;
}
.blueBug::after {
  content: "";
  position: absolute;
  right: -6px;
  height: 14px;
  width: 0px;
  border-right: solid 1px #ebeef5 !important;
}
.delBut {
  color: #f56c6c !important;
  position: relative;
}
.delBut::after {
  content: "";
  position: absolute;
  right: -6px;
  height: 14px;
  width: 0px;
  border-right: solid 1px #ebeef5 !important;

}
.card-icon-container {
  display: inline-block;
  font-size: 3rem;
}
.card-desc {
  font-weight: bold;
  float: right;
}
.card-title {
  margin-top: 0.3rem;
  line-height: 18px;
  color: rgba(0, 0, 0, 0.45);
  font-size: 1rem;
}
.card-count {
  margin-top: 0.75rem;
  color: #666;
  font-size: 1.25rem;
}
.echarts {
  width: 100%;
  height: 100%;
}
.e-title {
  font-size: 13px;
  color: #202a34;
  font-weight: 700;
}
.box-card {
  width: 100%;
}
.text {
  font-size: 14px;
}

.item {
  padding: 18px 0;
}


</style>
