import request from "@/utils/request";

//获取用户信息
export const getUserInfo = () => request.get("/user/getUserInfo");
////
//修改用户基础信息
export const updateUserInfo = (data) => request.put("/user/updateUser", data);

//图像上传
export const uploadImage = (data) => request.post("/common/upload", data);

//根据原密码修改密码
export const updatePassword = (data) =>
  request.put("/user/updateByPassword", data);

//根据手机号验证码修改密码
export const updatePasswordByPhone = (user) =>
  request.put(`/user/updateByPhone`, user);

//发送验证码
export const sendCode = (phone) => {
  const params = new URLSearchParams();
  params.append("phone", phone);
  return request.get("/user/send-code", { params });
};

//根据验证码换绑手机号
export const updatePhone = (data) => request.put("/user/updatePhone", data);

//根据竞赛名、状态进行模糊搜索
export const searchCompetition = (data) => {
  // const params = new URLSearchParams();
  // params.append("data", data);
  // return request.post("/competition/getByNameAndStatus", { params: data });
  return request.post("/competition/getByNameAndStatus", data);
};

// 信息相关
//获取我发送的信息
export const getSendInform = (data) => {
  return request.post("/message/getBySendMessageDTO", data);
};

//获取我接受的信息
export const getReceiveInform = (data) => {
  return request.post("/message/getByReceiveMessageDTO", data);
};

//根据id更新信息状态
export const updateStatus = (data) => {
  return request.put("/message/updateStatusById", data);
};

//获取用户信息
export const getUser = () => {
  return request.get("/user/getUserInfo");
};

//发送信息
export const sendInform = (data) => {
  return request.post("/message/send", data);
};
//根据id更新信息状态
export const updateStatusById = (data) => {
  return request.put("/message/updateStatusById", data);
};

// 团队
//根据userId分页查询团队
export const getTeam = (data) => {
  return request.post("/team/queryList", data);
};

//更新指导老师
export const updateTeacher = (teacherId, teamId) => {
  const formData = new URLSearchParams();
  formData.append("teacherId", teacherId);
  formData.append("teamId", teamId);
  return request.put("/team/updateTeacher", formData);
};

//新增队伍
export const addmyTeam = (data) => {
  return request.post("/team/add", data);
};

//新增队员
export const addTeamMember = (teamId, memberId) => {
  return request.post(
    "/team/addMember",
    {}, // 请求体为空（接口无请求体数据，仅传 query 参数）
    {
      params: { teamId, memberId }, // query 参数放在 params 中
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      }, // 明确请求头（可选，若框架已全局配置可省略）
    }
  );
};

//根据teamId获取队伍信息
export const getTeamById = (teamId) => {
  const params = new URLSearchParams();
  params.append("teamId", teamId);
  return request.get("/team/getById", { params });
};
// request.get("/team/getById", { params: { teamId } });

//删除队伍
export const deleteTeam = (teamId) => {
  const params = new URLSearchParams();
  params.append("teamId", teamId);
  return request.delete("/team/dissolveTeam", { params });
};

//删除队员
export const deleteTeamMember = (teamId, memberId) => {
  const params = new URLSearchParams();
  params.append("teamId", teamId);
  params.append("memberId", memberId);
  return request.delete("/team/deleteMember", { params });
};

//根据姓名模糊查询
export const queryTeamByName = (name) =>
  request.get("/user/queryByName", { params: { name } });

// 评委评审
// 评委竞赛查询
export const getJudgeCompetition = (data) =>
  request.post("/judge/queryList", data);

//获取该用户获奖的所有奖状
export const getAward = () => request.get("/award/getAwardList");

//获取新闻
export const getNews = (data) => request.post("/news/queryList", data);

//获取通告
export const getNotice = (data) =>
  request.post("/annunciation/queryList", data);

// 下载图片接口

export const downloadFig = (imageUrl) => {
  return request.get('/common/download-image', {
    params: { imageUrl },
    responseType: 'blob' // 关键：指定接收二进制数据
  });
}