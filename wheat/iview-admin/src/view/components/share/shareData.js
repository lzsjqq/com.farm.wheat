import axios from '@/libs/api.request'

export const getTableData = (formItem) => {
  return axios.request({
    url: 'simple/shares',
    method: 'post',
    data: formItem
  })
}
