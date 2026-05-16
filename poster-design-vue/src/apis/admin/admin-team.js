import request from "@/utils/request";

//模糊搜索队伍列表
export const getWithLike = (data) =>
  request.post("/admin/team/queryList", data);

//根据teamId查询队伍详情
export const getTeamById = (teamId) => {
  const params = new URLSearchParams();
  params.append("teamId", teamId);
  return request.get("/team/getById", { params });
};

//根据teamId获取团队参加的竞赛
export const getCompetitionByTeamId = (teamId) => {
  const params = new URLSearchParams();
  params.append("teamId", teamId);
  return request.get("/admin/team/getTeamCompetition", { params });
};

//根据团队id和竞赛id获取团队作品即评语
export const getTeamWork = (teamId, competitionId) => {
  const params = new URLSearchParams();
  params.append("teamId", teamId);
  params.append("competitionId", competitionId);
  return request.get("/admin/team/getTeamWorkAndComment", { params });
};
