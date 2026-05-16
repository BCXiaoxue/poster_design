import request from "@/utils/request";
// 搜索
export const queryNewsPageApi = (cskeys) =>
    request.post(`/news/queryList`, cskeys)

// 根据ID获取
export const queryNewsById = (id) => request.get(`/news/queryById?id=${id}`)

// 根据ID获取前后ID
export const judgmentNewsById = (id) => request.get(`/news/judgmentById?id=${id}`)

// 获取n个随机新闻
export const getNewsRandom = (number) => request.get(`/news/getRandom?number=${number}`)
