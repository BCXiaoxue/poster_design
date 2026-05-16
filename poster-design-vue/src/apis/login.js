import request from "@/utils/request";

//登录
export const loginApi = (data) => request.post("/user/login", data);

//用户登录
export const loginByCode = (data) => request.post("/user/loginByCode", data);
