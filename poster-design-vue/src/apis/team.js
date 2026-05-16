import request from "@/utils/request";

//获取用户信息
export const getUserTeam = () => request.get("/team/getTeamByCaptain");

export const getTeamMember = (teamId) =>
  request.get(`/team/getById?teamId=${teamId}`);

export const setTeamWork = (data) => request.post("/work/save", data);
