// 日期格式化
import dayjs from 'dayjs'

export function formatDate(date) {
  const currentDate = dayjs()
  const targetDate = dayjs(date)
  if (currentDate.diff(targetDate, 'minute') < 1) {
    return targetDate.format('刚刚')
  } else if (currentDate.day() === targetDate.day()) {
    return targetDate.format('今天 HH:mm')
  } else if (currentDate.diff(targetDate, 'day') <= 1) {
    return targetDate.format('昨天 HH:mm')
  } else if (currentDate.year() === targetDate.year()) { // 如果日期的年份与当前年份相同，则只显示月日
    return targetDate.format('M-D')
  } else {
    return targetDate.format('YYYY-M-D')
  }
}

export const calcDate = (date1, date2) => {
  const date3 = date2 - date1

  const days = Math.floor(date3 / (24 * 3600 * 1000))

  const leave1 = date3 % (24 * 3600 * 1000) // 计算天数后剩余的毫秒数
  const hours = Math.floor(leave1 / (3600 * 1000))

  const leave2 = leave1 % (3600 * 1000) // 计算小时数后剩余的毫秒数
  const minutes = Math.floor(leave2 / (60 * 1000))

  const leave3 = leave2 % (60 * 1000) // 计算分钟数后剩余的毫秒数
  const seconds = Math.round(date3 / 1000)
  return {
    leave1,
    leave2,
    leave3,
    days: days,
    hours: hours,
    minutes: minutes,
    seconds: seconds
  }
}

/**
 * 日期格式化
 */
export function dateFormat(date, format) {
  format = format || 'yyyy-MM-dd hh:mm:ss'
  if (date !== 'Invalid Date') {
    const o = {
      'M+': date.getMonth() + 1, // month
      'd+': date.getDate(), // day
      'h+': date.getHours(), // hour
      'm+': date.getMinutes(), // minute
      's+': date.getSeconds(), // second
      'q+': Math.floor((date.getMonth() + 3) / 3), // quarter
      'S': date.getMilliseconds() // millisecond
    }
    if (/(y+)/.test(format)) {
      format = format.replace(RegExp.$1,
        (date.getFullYear() + '').substr(4 - RegExp.$1.length))
    }
    for (const k in o) {
      if (new RegExp('(' + k + ')').test(format)) {
        format = format.replace(RegExp.$1,
          RegExp.$1.length === 1 ? o[k]
            : ('00' + o[k]).substr(('' + o[k]).length))
      }
    }
    return format
  }
  return ''
}

/**
     * @function getdataSource 获取页面接口数据
     * */
export const getFirstDay = () => {
  // 当前月第一天
  const y = new Date().getFullYear() // 获取年份
  let m = new Date().getMonth() + 1 // 获取月份
  const d = '01'
  m = m < 10 ? '0' + m : m // 月份补 0
  return [y, m, d].join('-')
}
export const getLastDay = () => {
  // 当前月最后一天
  const y = new Date().getFullYear() // 获取年份
  let m = new Date().getMonth() + 1 // 获取月份
  let d = new Date(y, m, 0).getDate() // 获取当月最后一日
  m = m < 10 ? '0' + m : m // 月份补 0
  d = d < 10 ? '0' + d : d // 日数补 0
  return [y, m, d].join('-')
}
