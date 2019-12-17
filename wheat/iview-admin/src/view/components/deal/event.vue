<style scoped>
  .time{
    font-size: 14px;
    font-weight: bold;
  }
  .content{
    padding-left: 5px;
  }
</style>
<template>
  <Timeline>
    <TimelineItem  v-for="item in items">
      <p class="time">{{getDateStr(item.eventDate)}} </p>
      <p class="content">{{item.event}}</p>
    </TimelineItem>
  </Timeline>
</template>
<script>
import { post } from '@/libs/http'
import { getDateStr } from '@/api/date'
export default {
  data () {
    return {
      items: [

      ]
    }
  },
  methods: {
    eventTimeLine () {
      post(this.insertFormItem, 'deal/listEvents').then(res => {
        this.items = res.data
      })
    },
    getDateStr (time) {
      return getDateStr(time, 'simple')
    }
  },
  mounted: function () {
    this.eventTimeLine()
  }
}
</script>
