import axios from "axios";
import { ElMessage } from "element-plus";
import { useTokenStore } from "@/stores/token";
import router from "../router";

/* const instance = axios.create({
    baseURL: "http://localhost:8080"
}); */

const instance = axios.create({
  baseURL: "/api",
});

// 添加请求拦截器
instance.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么
    // 添加token
    // 解决跨域问题
    // axios.defaults.withCredentials = true;
    const jwt = localStorage.getItem("jwt");
    if (jwt) {
      config.headers["Authentication"] = jwt;
    }
    // console.log(config.headers.Authentication);
    /* const tokenStore = useTokenStore();
        // console.log(tokenStore.token)
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token;
            config.headers.Authorization = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiLmuLjpvpnlqIEifQ.ilMGoU47wjBk7d5NTv8HM_v0K1ce_4LKJ4ZfU3WHr9w";
        }
        console.log(config.headers.Authorization)*/
    return config;
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

// 添加响应拦截器
instance.interceptors.response.use(
  (response) => {
    if (response.data.code === 1) {
      return response.data;
    }

    // 如果状态码为0，则将错误信息显示到页面上
    ElMessage.error(response.data.msg ? response.data.msg : "服务异常");
    return Promise.reject(response);
  },
  (error) => {
    if (error.response.status == 401) {
      //提示信息
      ElMessage.error("登录超时，请重新登录");
      //跳转登录页面
      router.push("/login");
    }
    ElMessage.error("服务异常");
    return Promise.reject(error);
  }
);

export default instance;
