// import request from '@/utils/request'

// // 根据state获取订单列表
// export const orderListByStateService = (data: any) => {
//     const params = new URLSearchParams()
//     for (let key in data) {
//         params.append(key, data[key])
//     }
//     return request.get('/admin/order/list', { params })
// }

// // 通过id修改订单state
// export const changeOrderStateService = (data: any) => {
//     const params = new URLSearchParams()
//     params.append('id', data.id)
//     params.append('state', data.state)
//     return request.put('/admin/order/state', params)
// }

// // 根据id删除订单
// export const deleteOrderByIdService = (data: any) => {
//     const params = new URLSearchParams()
//     params.append('id', data)
//     return request.delete('/admin/order/delete', { params })
// }

// // 根据订单id获取详细数据
// export const orderDetailByIdService = (data: any) => {
//     const params = new URLSearchParams()
//     params.append('id', data)
//     return request.get('/admin/order/detail', { params })
// }

// // 获取订单销售额统计数据
// export const orderSalesService = (data: any) => {
//     const params = new URLSearchParams()
//     for (let key in data) {
//         params.append(key, data[key])
//     }
//     return request.get('/admin/order/sales', { params })
// }

// // 获取销量统计数据
// export const orderSalesCountService = (data: any) => {
//     const params = new URLSearchParams()
//     for (let key in data) {
//         params.append(key, data[key])
//     }
//     return request.get('/admin/order/sales/count', { params })
// }

// // 导出数据
// export const orderExportService = () => {
//     return request.get('/admin/order/export', {
//         responseType: 'blob', // 设置响应类型为blob
//         headers: {
//             'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
//         }
//     });
// }
