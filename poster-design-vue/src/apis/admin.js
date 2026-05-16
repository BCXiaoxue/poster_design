
import request from "@/utils/request";
// 根据条件搜索竞赛
export const getWithLike = (data) => request.post('/admin/competition/getWithLike', data)

// 根据id删除竞赛
export const deleteCompetition = (id) => {
  const params = new URLSearchParams();
  params.append("id", id);
  return request.delete("/competition/deleteById", { params });
}

// 根据竞赛id搜索，竞赛详情
export const getByIdWithRedis = (id) => request.get(`/admin/competition/getByIdWithRedis?id=${id}`)

// 查看这个竞赛下的评委
export const getCompetitionJudge = (competitionId) => request.get(`/admin/judge/getCompetitionJudge?competitionId=${competitionId}`)

// 保存这个竞赛下的评委
export const saveCompetitionJudge = (data) => request.post('/admin/judge/saveCompetitionJudge', data)

// 获取比赛状态统计
export const getCompetitionStatusCount = () => request.get('/admin/competition/getCompetitionStatusCount')

// 获取竞赛分类
export const getCompetitionCategory = () => request.get('/admin/competition/getCompetitionCategory')

// 根据用户权限和用户名分页查询
export const getUserByPermissionAndName = (data) => request.post('/admin/user/getByPermissionAndName', data)


// 更改用户状态
export const updateUserStatus = (data) => request.put('/admin/user/updateStatus', data)

// 统计普通用户数 & 管理用户数
export const countUserAll = () => request.get('/admin/user/countUserAll')
// 获取竞赛奖项信息
export const getAllAward = (competitionId) => request.get(`/admin/award/list?competitionId=${competitionId}`)

export const getAllAward2 = (competitionId) => request.get(`/admin/award/list?competitionId=${competitionId}`)
// 获取用户队伍作品趋势
export const getUserTeamWorkTrend = () => request.get('/admin/work/getUserTeamWorkTrend')


// 获取奖项数量统计
export const getAwardCount = () => request.get('/admin/award/getAwardCount')

// 名字模糊查询这个竞赛去掉参赛队员的列表
export const queryUserByNameAndCompetitionId = (competitionId, name) => request.get(`/admin/user/queryByNameAndCompetitionId?competitionId=${competitionId}&name=${name}`)

// 获取竞赛分类数量统计
export const getCompetitionCategoryCount = () => request.get('/admin/competition/getCompetitionCategoryCount')