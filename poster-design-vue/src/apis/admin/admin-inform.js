import request from "@/utils/request";

// 模糊查询，获取通知列表
// 改成管理端接口
export const getWithLike = (data) =>
  request.post("/admin/annunciation/queryList", data);

//根据id获取通知详情
export const getInformById = (id) => {
  const params = new URLSearchParams();
  params.append("id", id);
  return request.get("/annunciation/getById", { params });
};

//新增通知
export const addInform = (data) => request.post("/annunciation/add", data);

// 根据id删除通知
export const deleteInform = (id) => {
  const params = new URLSearchParams();
  params.append("id", id);
  return request.delete("/admin/annunciation/deleteById", { params });
};

//根据id修改通知
export const updateInform = (data) =>
  request.put("/admin/annunciation/updateById", data);

//通过id查询竞赛
export const getInform = (id) => {
  const params = new URLSearchParams();
  params.append("id", id);
  return request.get("/competition/getById", { params });
};

//根据条件搜索竞赛
export const getCompetitions = (data) =>
  request.post("/admin/competition/getWithLike", data);

//上传图片
export const uploadFig = (data) => request.post("/common/upload", data);

//统计大赛通知数
export const getInformCount = () =>
  request.get("/admin/annunciation/countAnnunciation");
