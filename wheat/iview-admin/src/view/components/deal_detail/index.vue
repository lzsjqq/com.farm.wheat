<template>
  <div>
    <div>

      <Modal  v-model="addInsert"title="与任何当前主流趋势保持一致"@on-ok="insert" @on-cancel="cancel"  width="600" >
        <Form :model="insertFormItem" :label-width="80" inline>
          <FormItem label="股票代码">
            <Input v-model="insertFormItem.shareCode" placeholder="请输入股票代码..."/>
          </FormItem>
          <FormItem label="股票名称">
            <Input v-model="insertFormItem.shareName" placeholder="请输入股票名称..."/>
          </FormItem>
          <FormItem label="成交价">
            <Input v-model="insertFormItem.dealPrice" placeholder="请输入成交价..."/>
          </FormItem>
          <FormItem label="止损价">
            <Input v-model="insertFormItem.stopLossPrice" placeholder="请输入止损价..."/>
          </FormItem>
          <FormItem label="成交量">
            <Input v-model="insertFormItem.volume" placeholder="请输入成交量..."/>
          </FormItem>
          <FormItem label="买卖方向">
            <Select v-model="insertFormItem.target" style="width:170px">
              <Option v-for="item in targetList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
          <FormItem label="计划">
            <Input v-model="insertFormItem.plan"  style="width: 430px" :rows="4" type="textarea" placeholder="请输入计划..."/>
          </FormItem>
        </Form>
      </Modal>

      <Form :model="formItem" :label-width="80" inline>
        <FormItem label="股票代码">
          <Input v-model="formItem.shareCode" placeholder="请输入股票代码..."/>
        </FormItem>
        <FormItem>
          <Button type="primary" @click="search()" >搜索</Button>
        </FormItem>
        <Button type="primary" @click="addInsert = true">添加交易</Button>
      </Form>
     </div>

    <Card>
      <tables  highlight-row   ref="tables" editable  search-place="top" v-model="tableData"  :columns="columns" @on-delete="handleDelete" stripe border />
      <Page :total="total" :current="startRow" :page-size="endRow" @on-change="pageChange" @on-page-size-change="pagesizechange" show-sizer show-total></Page>
      <Button style="margin: 10px 0;" type="primary" @click="exportExcel">导出为Csv文件</Button>
    </Card>
  </div>
</template>

<script>
import Tables from '_c/tables'
import { post, get } from '@/libs/http'
import { getDateStr } from '@/api/date'
export default {
  name: 'tables_page',
  components: {
    Tables
  },
  data () {
    return {
      targetList: [
        {
          value: '1',
          label: '买入'
        },
        {
          value: '2',
          label: '卖出'
        }
      ],
      formItem: {
        shareCode: ''
      },
      insertFormItem: {
        shareCode: '',
        shareName: '',
        dealPrice: '',
        stopLossPrice: '',
        volume: '',
        target: '',
        plan: ''
      },
      addInsert: false,
      // 总页数
      total: 246,
      // 开始页
      startRow: 1,
      // 每页条数
      endRow: 10,
      columns: [
        { title: '计划代码', key: 'idDealInfo', width: 100 },
        { title: '成交日期',
          key: 'tradingDate',
          width: 100,
          render: (h, params) => {
            return h('div', [
              h('span', {
                props: {
                  type: 'text',
                  size: 'small'
                }
              }, getDateStr(params.row.tradingDate, 'simple'))
            ])
          } },
        { title: '股票名称', key: 'shareName', width: 100 },
        { title: '股票代码', key: 'shareCode', width: 100 },
        { title: '止损价', key: 'stopLossPrice', width: 100 },
        { title: '成交价', key: 'dealPrice', width: 100 },
        { title: '买卖方向',
          key: 'target',
          width: 100,
          render: (h, params) => {
            return h('div', [
              h('span', {
                props: {
                  type: 'text',
                  size: 'small'
                }
              }, params.row.target === '1' ? '买入' : '卖出')
            ])
          } },
        { title: '成交量', key: 'volume', width: 100 },
        { title: '计划', key: 'plan', width: 100 },
        { title: '修改时间',
          key: 'updateTime',
          width: 200,
          render: (h, params) => {
            return h('div', [
              h('span', {
                props: {
                  type: 'text',
                  size: 'small'
                }
              }, getDateStr(params.row.updateTime, 'year'))
            ])
          }
        }
      ],
      tableData: []
    }
  },
  methods: {
    handleDelete (params) {
      console.log(params)
    },
    exportExcel () {
      this.$refs.tables.exportCsv({
        filename: `table-${(new Date()).valueOf()}.csv`
      })
    },
    search () {
      this.formItem.pageNum = this.startRow
      this.formItem.pageSize = this.endRow
      post(this.formItem, 'deal/dealDetailInfo').then(res => {
        let page = res.data
        this.tableData = page.list
        this.total = page.total
        this.startRow = page.pageNum
      })
    },
    insert () {
      post(this.insertFormItem, 'deal/insertDetail').then(res => {
        this.search()
        this.$Message.info('success')
      })
    },
    pageChange (page) {
      this.startRow = page
      this.search()
    },
    pagesizechange (endRow) {
      this.endRow = endRow
      this.search()
    },
    cancel () {
      this.$Message.info('Clicked cancel')
    }
  },

  mounted: function () {
    this.search()
    get('common/dicOne?dicCode=plan').then(res => {
      let dicOne = res.data
      this.insertFormItem.plan = dicOne.codeValue
    })
  }
}
</script>

<style>

</style>
