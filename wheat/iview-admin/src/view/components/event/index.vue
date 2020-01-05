
<template>
  <Timeline>
    <TimelineItem>
      <p class="time">1976年</p>
      <p class="content">Apple I 问世</p>
    </TimelineItem>
  </Timeline>
</template>
import { post, get } from '@/libs/http'
<script>
  export default {
    data () {
      return {
        pageNum : 1,
        timelineItems: []
      }
    },
    mounted: function () {
      this.search()
    },
    methods: {
      search () {
        post(null,'deal/listEvents').then(res => {
          let page = res.data
          this.timelineItems = page.list
          this.total = page.total
        })
      },
    }
  }
</script>
<style scoped>
  .time{
    font-size: 14px;
    font-weight: bold;
  }
  .content{
    padding-left: 5px;
  }
</style>
