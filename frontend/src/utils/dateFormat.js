/**
 * 日期格式化工具
 * 用于解析后端返回的中文日期格式 "yyyy 年 MM 月 dd 日 HH:mm:ss"
 */

/**
 * 解析中文日期格式
 * @param {string} dateStr - 日期字符串，格式："yyyy 年 MM 月 dd 日 HH:mm:ss"
 * @returns {Date|null} - 解析后的 Date 对象，失败返回 null
 */
export function parseChineseDate(dateStr) {
  if (!dateStr) return null
  
  // 匹配中文日期格式：2026 年 04 月 13 日 21:15:42
  const match = dateStr.match(/(\d{4}) 年 (\d{2}) 月 (\d{2}) 日 (\d{2}):(\d{2}):(\d{2})/)
  if (!match) {
    // 如果不是中文格式，尝试直接解析（兼容标准 ISO 格式）
    const directDate = new Date(dateStr)
    if (isNaN(directDate.getTime())) {
      return null
    }
    return directDate
  }
  
  const [, year, month, day, hour, minute, second] = match
  // 注意：月份从 0 开始，所以要减 1
  return new Date(year, parseInt(month) - 1, parseInt(day), parseInt(hour), parseInt(minute), parseInt(second))
}

/**
 * 格式化日期为本地格式
 * @param {string} dateStr - 日期字符串
 * @returns {string} - 格式化后的日期字符串，如："2026/04/13"
 */
export function formatDate(dateStr) {
  if (!dateStr) return '-'
  const date = parseChineseDate(dateStr)
  if (!date) return '-'
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

/**
 * 格式化日期时间为本地格式
 * @param {string} dateStr - 日期字符串
 * @returns {string} - 格式化后的日期时间字符串，如："2026/04/13 21:15:42"
 */
export function formatDateTime(dateStr) {
  if (!dateStr) return '-'
  const date = parseChineseDate(dateStr)
  if (!date) return '-'
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

/**
 * 计算剩余天数
 * @param {Object} cert - 证书对象，包含 endTime 字段
 * @returns {number} - 剩余天数
 */
export function getDaysRemaining(cert) {
  if (!cert || !cert.endTime) return 0
  const endDate = parseChineseDate(cert.endTime)
  if (!endDate) return 0
  const now = new Date()
  const diffTime = endDate - now
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  return Math.max(0, diffDays)
}

/**
 * 计算有效期百分比
 * @param {Object} cert - 证书对象，包含 startTime 和 endTime 字段
 * @returns {number} - 有效期百分比 (0-100)
 */
export function getValidityPercentage(cert) {
  if (!cert || !cert.startTime || !cert.endTime) return 0
  const startDate = parseChineseDate(cert.startTime)
  const endDate = parseChineseDate(cert.endTime)
  if (!startDate || !endDate) return 0
  const now = new Date()
  const totalDays = (endDate - startDate) / (1000 * 60 * 60 * 24)
  const remainingDays = (endDate - now) / (1000 * 60 * 60 * 24)
  return Math.max(0, Math.min(100, (remainingDays / totalDays) * 100))
}
