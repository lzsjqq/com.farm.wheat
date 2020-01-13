<template>
  <div>
    <Modal  v-model="addInsert"title="与任何当前主流趋势保持一致"@on-ok="insert" @on-cancel="cancel"  width="600" >
      <Form :model="insertFormItem" :label-width="80" inline>
        <FormItem label="发生时间">
          <Input v-model="insertFormItem.eventDate" placeholder="请输入发生时间..."/>
        </FormItem>
        <FormItem label="事件类型">
          <Select v-model="insertFormItem.type" style="width:170px">
            <Option v-for="item in typeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
          </Select>
        </FormItem>
        <FormItem label="事件描述">
          <Input v-model="insertFormItem.event" placeholder="请输入事件描述..."/>
        </FormItem>
        <FormItem label="事件影响">
          <Input v-model="insertFormItem.affect" placeholder="请输入事件影响..."/>
        </FormItem>
        <FormItem label="事件提及次数">
          <Input v-model="insertFormItem.frequency" placeholder="请输入事件提及次数..."/>
        </FormItem>
      </Form>
    </Modal>
    <Button type="primary" @click="addInsert = true">添加交易</Button>
    <Timeline>
      <TimelineItem v-for="item in timelineItems">
        <p class="time">{{getDateStr(item.eventDate, 'simple')}}</p>
        <p class="content"><span class="fontWeight">事件：</span>{{item.event}}</p>
        <p class="content"><span class="fontWeight">影响：</span>{{item.affect}}</p>
      </TimelineItem>
      <TimelineItem v-if="show"><a href="#" @click="search(++this.pageNum)" >查看更多</a></TimelineItem>
    </Timeline>
  </div>
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
      search (pageNum) {
        post({pageNum:pageNum,pageSize:5 }, 'deal/listEvents').then(res => {
          let page = res.data
          this.timelineItems = page.list
          this.show = page.lastPage !== page.firstPage
        })
      },
      getDateStr (timeStamp, startType) {
        return getDateStr(timeStamp, startType)
      },
      insert () {
        post(this.insertFormItem, 'deal/insertEvent').then(res => {
          this.pageNum = 1
          this.search(this.pageNum)
          this.$Message.info('success')
        })
      },
      cancel () {
        this.$Message.info('Clicked cancel')
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
