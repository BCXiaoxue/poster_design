import request from '@/utils/request'
//新增
export const addApi = (user) => request.post('/user/register', user)

//发送验证码
export const sentcode = (phone) => {
    const params = new URLSearchParams()
    params.append('phone', phone)
    return request.get('/user/send-code', { params })
}

// 根据手机修改密码
//修改
export const updatePasswordByPhone = (user) => request.put(`/user/updateByPhone`, user)

// 查询学校
export const querySchool = (name) => request.get(`/user/university?name=${name}`)

// 获取用户信息
export const getUserInfo = () => request.get(`/user/getUserInfo`)

// 根据姓名模糊查询
export const queryUserByName = (name) => request.get(`/user/queryByName?name=${name}`)
export const getcount = () => request.get(`/user/count`)
