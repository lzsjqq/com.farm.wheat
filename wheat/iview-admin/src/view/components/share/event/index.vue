<template>
  <Timeline>
    <TimelineItem v-for="item in timelineItems">
      <p class="time">{{getDateStr(item.eventDate, 'simple')}}</p>
      <p class="content"><span class="fontWeight">事件：</span>{{item.event}}</p>
      <p class="content"><span class="fontWeight">影响：</span>{{item.affect}}</p>
    </TimelineItem>
    <TimelineItem v-if="show"><a href="#" @click="search(++this.pageNum)" >查看更多</a></TimelineItem>
  </Timeline>
</template>

<script>
  import { post, get } from '@/libs/http'
  import { getDateStr } from '@/api/date'
  export default {
    components: {
      getDateStr
    },
    data () {
      return {
        pageNum : 1,
        timelineItems: [],
        show: false
      }
    },
    methods: {
      search (pageNum) {
        post({pageNum:pageNum,pageSize:5 }, 'deal/listEvents').then(res => {
          let page = res.data
          this.timelineItems = page.list
          this.show = page.lastPage !== page.firstPage
        })
      },
      getDateStr (timeStamp, startType) {
        return getDateStr(timeStamp, startType)
      }
    },
    mounted: function () {
      this.search(this.pageNum)
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
  .fontWeight{
    font-weight: bold;
  }
</style>
