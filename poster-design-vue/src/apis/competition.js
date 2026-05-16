import request from "@/utils/request";

export const getCompetitionById = (id) => {
  return request.get("/competition/getById", {
    params: { id },
  });
};

export const getCompetitionDetailById = (workId) => {
  return request.get("/work/getById", {
    params: { workId },
  });
};

export const getCaptainEnlist = (competitionId) => {
  return request.get(`/work/isEnlisted?competitionId=${competitionId}`);
};

export const getCompetitionList = (data) =>
  request.post(`/competition/getByNameWithStatus`, data);
// 新增竞赛信息
export const addCompetition = (data) => request.post("/competition/add", data);

// 根据id修改竞赛信息
export const updateCompetition = (data) =>
  request.put("/competition/updateById", data);

//获取每个队伍第一名作品
export const getCompetitionFirstWork = () =>
  request.get("/award/getWinWithCompetition");
