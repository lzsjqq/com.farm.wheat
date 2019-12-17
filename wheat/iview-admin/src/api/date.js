/**
 * @param {Number} timeStamp 传入的时间戳（如：startTime，endTime）
 * @param {Number} startType 要返回的时间字符串的格式类型，传入'year'则返回年开头的完整时间（如：year）
 */
export const getDateStr = (timeStamp, startType) => {
  let d = new Date(timeStamp)
  let year = d.getFullYear()
  let month = getHandledValue(d.getMonth() + 1)
  let date = getHandledValue(d.getDate())
  let hours = getHandledValue(d.getHours())
  let minutes = getHandledValue(d.getMinutes())
  let second = getHandledValue(d.getSeconds())
  let resStr = ''
  if (startType === 'year') resStr = year + '-' + month + '-' + date + ' ' + hours + ':' + minutes + ':' + second
  else if (startType === 'simple') resStr = year + '-' + month + '-' + date
  else resStr = month + '-' + date + ' ' + hours + ':' + minutes
  return resStr
}

const getHandledValue = num => {
  return num < 10 ? '0' + num : num
}
