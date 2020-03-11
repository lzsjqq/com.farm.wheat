<template>
  <div>
    <Row>
      <Card shadow>
        <DrawLine style="height: 500px;"/>
      </Card>
    </Row>
  </div>
</template>
<script>
  import DrawLine from './drawLine.vue'
  import { post, get } from '@/libs/http'
  import { getDateStr } from '@/api/date'
  export default {
    components: {
      getDateStr,
      DrawLine
    },
    data () {
      return {
        addInsert : false,
        insertFormItem : {},
        pageNum : 1,
        timelineItems: [],
        show: false,
        typeList: [
          {
            label: '经济',
            value: '1'
          }
        ]
      }
    },
    methods: {
    },
    mounted: function () {
      // this.search(this.pageNum)
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
