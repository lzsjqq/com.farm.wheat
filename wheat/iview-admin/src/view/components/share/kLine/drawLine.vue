<template>
    <div ref="dom"></div>
</template>

<script>
import echarts from 'echarts'
import { option } from './option'
import { on, off } from '@/libs/tools'
import { post, get } from '@/libs/http'
export default {
  name: 'serviceRequests',
  data () {
    return {
      dom: null,
      upColor: '#ec0000',
      upBorderColor: '#8A0000',
      downColor: '#00da3c',
      downBorderColor: '#008F28',
      rawData: [],
      sharePrices:[]
    }
  },
  methods: {
    resize () {
      this.dom.resize()
    },
    async getBasePrice (shareCode) {
       await post({ shareCode: shareCode}, 'draw/line/sharePrices').then(res => {
        this.rawData = res.data
      })
    },
    async draw (shareCode) {
      // 数据意义：开盘(open)，收盘(close)，最低(lowest)，最高(highest)
      await this.getBasePrice(shareCode)
      this.$nextTick(() => {
        this.dom = echarts.init(this.$refs.dom)
        this.dom.setOption(option(this.rawData))
        on(window, 'resize', this.resize)
      })
    }
  },
  mounted () {
    this.draw("000001")
  },
  beforeDestroy () {
    off(window, 'resize', this.resize)
  }
}
</script>
