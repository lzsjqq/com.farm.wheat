<style>
  .ivu-table .demo-table-info-row td{
    background-color: #2db7f5;
    color: #fff;
  }
  .ivu-table .demo-table-error-row td{
    background-color: #ff6600;
    color: #fff;
  }
  .ivu-table td.demo-table-info-column{
    background-color: #2db7f5;

  }
  .ivu-table .demo-table-info-cell-name {
    background-color: #2db7f5;
    color: #fff;
  }
  .ivu-table .demo-table-info-cell-age {
    background-color: #ff6600;
  }
  .ivu-table .demo-table-info-cell-address {
    background-color: #187;
    color: #fff;
  }
</style>
<template>
  <div>
    <div>
      <Form :model="formItem" :label-width="80" inline>
        <FormItem label="股票代码">
          <Input v-model="formItem.shareCode" placeholder="请输入股票代码..."/>
        </FormItem>
        <FormItem>
          <Button type="primary" @click="search()" >搜索</Button>
        </FormItem>
      </Form>
     </div>

    <Card>
      <tables :loading="loading"  disabled-hover max-height="1200"   ref="tables" editable  search-place="top" v-model="tableData"  :columns="columns" @on-delete="handleDelete" stripe border />
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
        { title: '计划代码', key: 'idDealInfo', width: 80 },
        { title: '股票名称', key: 'shareName', width: 100, className: 'demo-table-info-column' },
        { title: '股票代码', key: 'shareCode', width: 100 },
        { title: '剩余数量', key: 'volume', width: 100 },
        { title: '止损价', key: 'stopLossPrice', width: 100 , className: 'demo-table-info-cell-age',
            render: (h, params) => {
                if (params.row.$isEdit) {
                    return h('input', {
                        domProps: {
                            value: params.row.stopLossPrice
                        },
                        style: {
                            width: '60px'
                        },
                        on: {
                            input: function (event) {
                                params.row.stopLossPrice = event.target.value
                            }
                        }
                    })
                } else {
                    return h('div', params.row.stopLossPrice)
                }
            }
        },
        { title: '成本价', key: 'firstCost', width: 100 },
        { title: '手续费', key: 'changeMoney', width: 100 },
        { title: '印花税', key: 'stampDuty', width: 100 },
        { title: '卖出价格', key: 'sellPrice', width: 100 },
        { title: '收益', key: 'profit', width: 100 },
        { title: '计划',
          key: 'plan',
          width: 100,
          render: (h, params) => {
            if (params.row.$isEdit) {
              return h('input', {
                domProps: {
                  value: params.row.plan
                },
                style: {
                  width: '60px'
                },
                on: {
                  input: function (event) {
                    params.row.plan = event.target.value
                  }
                }
              })
            } else {
              return h('div', params.row.plan)
            }
          }
        },
        { title: '当日复盘',
          key: 'analyseOne',
          width: 100,
          render: (h, params) => {
            if (params.row.$isEdit) {
              return h('input', {
                domProps: {
                  value: params.row.analyseOne
                },
                style: {
                  width: '60px'
                },
                on: {
                  input: function (event) {
                    params.row.analyseOne = event.target.value
                  }
                }
              })
            } else {
              return h('div', params.row.analyseOne)
            }
          }
        },
        // { title: '复盘', key: 'analyse', width: 100 },
        { title: '期间最低价',
          key: 'lowPrice',
          width: 100,
          render: (h, params) => {
            if (params.row.$isEdit) {
              return h('input', {
                domProps: {
                  value: params.row.lowPrice
                },
                style: {
                  width: '60px'
                },
                on: {
                  input: function (event) {
                    params.row.lowPrice = event.target.value
                  }
                }
              })
            } else {
              return h('div', params.row.lowPrice)
            }
          }
        },
        { title: '期间最高价',
          key: 'highPrice',
          width: 100,
          render: (h, params) => {
            if (params.row.$isEdit) {
              return h('input', {
                domProps: {
                  value: params.row.highPrice
                },
                style: {
                  width: '60px'
                },
                on: {
                  input: function (event) {
                    params.row.highPrice = event.target.value
                  }
                }
              })
            } else {
              return h('div', params.row.highPrice)
            }
          }
        },
        { title: '5日最低价',
          key: 'fiveLowPrice',
          width: 100,
          render: (h, params) => {
            if (params.row.$isEdit) {
              return h('input', {
                domProps: {
                  value: params.row.fiveLowPrice
                },
                style: {
                  width: '60px'
                },
                on: {
                  input: function (event) {
                    params.row.fiveLowPrice = event.target.value
                  }
                }
              })
            } else {
              return h('div', params.row.fiveLowPrice)
            }
          }
        },
        { title: '5日最高价',
          key: 'fiveHighPrice',
          width: 100,
          render: (h, params) => {
            if (params.row.$isEdit) {
              return h('input', {
                domProps: {
                  value: params.row.fiveHighPrice
                },
                style: {
                  width: '60px'
                },
                on: {
                  input: function (event) {
                    params.row.fiveHighPrice = event.target.value
                  }
                }
              })
            } else {
              return h('div', params.row.fiveHighPrice)
            }
          }
        },
        { title: '5日风险收益比', key: 'fiveProfit', width: 100 },
        { title: 'R比率', key: 'rRate', width: 100 },
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
        },
        {
          title: 'Action',
          key: 'action',
          fixed: 'right',
          width: 140,
          render: (h, params) => {
            return h('div', [
              h('Button', {
                props: {
                  type: 'primary',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    this.show(params.row)
                  }
                }
              }, 'View'),
              h('Button', {
                props: {
                  type: 'primary',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    if (params.row.$isEdit) {
                      this.handleSave(params.row)
                    } else {
                      this.handleEdit(params.row)
                    }
                  }
                }
              }, params.row.$isEdit ? 'Save' : 'Edit ')
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
      post(this.formItem, 'deal/dealInfo').then(res => {
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
    },
    show (row) {
      this.$Modal.info({
        title: 'info',
        content: `<span style="color: #2d8cf0">计划：</span><br>${row.plan ? row.plan : ''}<br><span style="color: #2d8cf0">复盘：</span><br>${row.analyse ? row.analyse : ''}`
      })
    },
    handleEdit (row) {
      this.$set(row, '$isEdit', true)
    },
    handleSave (row) {
      this.$set(row, '$isEdit', false)
      post(row, 'deal/updateDealInfo').then(res => {
        this.search()
        this.$Message.info('success')
      })
    }
  },
  mounted: function () {
    this.search()
  }
}
</script>

<style>

</style>
