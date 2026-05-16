import request from "@/utils/request";
// 搜索
export const queryPageApi = (cskeys) =>
    request.post(`/annunciation/queryList`, cskeys)

// 根据ID获取
export const getAnnunciationById = (id) => request.get(`/annunciation/getById?id=${id}`)

// 根据ID获取前后ID
export const judgmentAnnunciationById = (id) => request.get(`/annunciation/judgmentById?id=${id}`)

// 获取n个随机通知
export const getAnnunciationRandom = (number) => request.get(`/annunciation/getRandom?number=${number}`)
