import request from "@/utils/request";
//模糊查询消息列表
export const getNews = (data) => {
  return request.post("/admin/news/queryList", data);
};

//新建新闻
export const addNews = (data) => request.post("/admin/news/add", data);
//根据编辑新闻
export const updateNews = (data) => request.put("/admin/news/updateById", data);
//根据id删除新闻
export const deleteNews = (id) => {
  const params = new URLSearchParams();
  params.append("id", id);
  return request.delete("/admin/news/deleteById", { params });
};

//根据id查询新闻详情
export const getNewsById = (id) => {
  const params = new URLSearchParams();
  params.append("id", id);
  return request.get("/news/queryById", { params });
};

//根据条件搜索竞赛
export const getCompetitions = (data) =>
  request.post("/admin/competition/getWithLike", data);

//通过id查询竞赛
export const getInform = (id) => {
  const params = new URLSearchParams();
  params.append("id", id);
  return request.get("/competition/getById", { params });
};

//上传图片
export const uploadFig = (data) => request.post("/common/upload", data);

//获取新闻数目
export const getNewsCount = () => request.get("/admin/news/countNews");
