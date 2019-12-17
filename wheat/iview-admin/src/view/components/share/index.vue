<template>
  <div>
    <div>
      <Form :model="formItem" :label-width="80" inline>
        <FormItem label="股票代码">
          <Input v-model="formItem.shareCode" placeholder="请输入股票代码..."/>
        </FormItem>
        <FormItem label="所属行业">
          <Input v-model="formItem.industry" placeholder="请输入所属行业..."/>
        </FormItem>
        <FormItem>
          <Button type="primary" @click="search()" >搜索</Button>
        </FormItem>
      </Form>
     </div>

    <Card>
      <tables    ref="tables" editable searchable search-place="top" v-model="tableData"  :columns="columns" @on-delete="handleDelete" stripe border />
      <Page :total="total" :current="startRow" :page-size="endRow" @on-change="pageChange" @on-page-size-change="pagesizechange" show-sizer show-total></Page>
      <Button style="margin: 10px 0;" type="primary" @click="exportExcel">导出为Csv文件</Button>
    </Card>
  </div>
</template>

<script>
import Tables from '_c/tables'
import { getTableData } from './shareData'
import { getDateStr } from '@/api/date'
export default {
  name: 'tables_page',
  components: {
    Tables
  },
  data () {
    return {
      formItem: {
        industry: '',
        shareCode: ''
      },
      // 总页数
      total: 246,
      // 开始页
      startRow: 1,
      // 每页条数
      endRow: 10,
      columns: [
        { title: '股票代码', key: 'shareCode', width: 100 },
        { title: '股票名称', key: 'shareName', width: 200 },
        { title: '所属行业', key: 'industry', width: 200 },
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
      getTableData(this.formItem).then(res => {
        let page = res.data
        this.tableData = page.list
        this.total = page.total
        this.startRow = page.pageNum
      })
    },
    pageChange (page) {
      this.startRow = page
      this.search()
    },
    pagesizechange (endRow) {
      this.endRow = endRow
      this.search()
    }
  },
  mounted: function () {
    this.search()
  }
}
</script>

<style>

</style>
