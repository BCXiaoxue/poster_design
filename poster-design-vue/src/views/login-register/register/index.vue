<template>
  <div class="register-page">
    <!-- 左侧品牌/宣传 -->
    <div class="register-left" :style="{ backgroundImage: `url(${loginLeftBg})` }">
      <div class="logo-row">
        <img v-lazy class="logo-img" :src="logoUrl" alt="logo" />
        <div>
          <div class="main-title">全国大学生海报设计大赛</div>
          <div class="main-subtitle">
            National College Students' Poster Design Competition
          </div>
        </div>
      </div>
      <div class="slogan-group">
        <div class="slogan-title">创意无限</div>
        <div class="slogan-desc">汇聚全国高校创意，见证设计新星诞生</div>
      </div>
    </div>
    <!-- 右侧注册表单 -->
    <div class="register-right">
      <div class="form-box">
        <div class="form-title">用户注册</div>
        <div class="form-subtitle">加入我们，开启你的设计之旅！</div>
        <el-form :model="user" :rules="rules" ref="userFormRef" class="register-form">
          <el-form-item prop="username">
            <div class="input-row">
              <label class="input-label">真实姓名</label>
              <div class="input-right">
                <div class="input-wrapper">
                  <el-input v-model="user.username" placeholder="请输入你的真实姓名" class="register-input" />
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item prop="idCard">
            <div class="input-row">
              <label class="input-label">身份证号</label>
              <div class="input-right">
                <div class="input-wrapper">
                  <el-input v-model="user.idCard" placeholder="请输入你的身份证号" class="register-input" />
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item prop="phone">
            <div class="input-row">
              <label class="input-label">手机号</label>
              <div class="input-right">
                <div class="input-wrapper">
                  <el-input v-model="user.phone" placeholder="请输入你的手机号" class="register-input" />
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item prop="password">
            <div class="input-row">
              <label class="input-label">密码</label>
              <div class="input-right">
                <div class="input-wrapper">
                  <el-input v-model="user.password" placeholder="请输入你的密码" type="password" show-password
                    class="register-input" />
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item prop="repassword">
            <div class="input-row">
              <label class="input-label">确认密码</label>
              <div class="input-right">
                <div class="input-wrapper">
                  <el-input v-model="user.repassword" placeholder="请再次输入你的密码" type="password" show-password
                    class="register-input" />
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item prop="schoolId">
            <div class="input-row">
              <label class="input-label">学校</label>
              <div class="input-right">
                <div class="custom-select-wrapper">
                  <input v-model="schoolSearchText" @input="handleSchoolSearch" placeholder="请输入学校名称"
                    class="custom-select-input" @focus="showSchoolDropdown = true" />
                  <div v-if="showSchoolDropdown" class="school-dropdown">
                    <div v-for="option in filteredOptions" :key="option.id" class="school-option"
                      @click="selectSchool(option)">
                      {{ option.name }}
                    </div>
                    <div v-if="filteredOptions.length === 0" class="no-results">
                      暂无匹配的学校
                    </div>
                  </div>
                  <div v-if="showSchoolDropdown" class="dropdown-overlay" @click="showSchoolDropdown = false"></div>
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item prop="gender">
            <div class="input-row">
              <label class="input-label">性别</label>
              <div class="input-right">
                <el-radio-group v-model="user.gender" class="gender-group">
                  <el-radio value="1">男</el-radio>
                  <el-radio value="2">女</el-radio>
                </el-radio-group>
              </div>
            </div>
          </el-form-item>
          <el-form-item prop="code">
            <div class="input-row">
              <label class="input-label">验证码</label>
              <div class="input-right">
                <div class="code-row">
                  <el-input v-model="user.code" placeholder="请输入验证码" class="register-input" style="flex: 1" />
                  <el-button :type="buttonType" @click="sentCode" :disabled="isSending" class="code-btn">{{ sendText
                    }}</el-button>
                </div>
              </div>
            </div>
          </el-form-item>
          <div class="form-actions">
            <el-button type="primary" @click="reg" class="register-submit-btn">注册</el-button>
            <el-button type="info" @click="ret" class="back-btn">返回登录</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { addApi, sentcode, querySchool, getUserInfo } from "@/apis/user.js";
import { useRouter } from "vue-router";
import logoUrl from "@/assets/images/logo.png";
import loginLeftBg from "@/assets/images/login_left.png";

const router = useRouter();

const user = reactive({
  username: "",
  password: "",
  phone: "",
  gender: 0,
  idCard: "",
  schoolId: "",
  code: "",
  repassword: "",
});

const userFormRef = ref();

const rules = ref({
  username: [
    { required: true, message: "请输入真实姓名", trigger: "blur" },
    {
      min: 1,
      max: 20,
      message: "姓名长度应在1到20个字符之间",
      trigger: "blur",
    },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    {
      min: 1,
      max: 20,
      message: "密码长度应在1到20个字符之间",
      trigger: "blur",
    },
  ],
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    {
      pattern: /^1[3456789]\d{9}$/,
      message: "手机号格式不正确",
      trigger: "blur",
    },
  ],
  gender: [{ required: true, message: "请选择性别", trigger: "change" }],
  idCard: [
    { required: true, message: "请输入身份证号", trigger: "blur" },
    {
      pattern:
        /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
      message: "身份证号格式不正确",
      trigger: "blur",
    },
  ],
  schoolId: [{ required: true, message: "请选择大学", trigger: "change" }],
  code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
  repassword: [
    { required: true, message: "请再次输入密码", trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        if (value !== user.password) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
});

const reg = () => {
  if (!userFormRef.value) return;
  userFormRef.value.validate(async (valid) => {
    if (valid) {
      const result = await addApi(user);
      if (result.code) {
        ElMessage.success("保存成功");
        router.push("/login");
      } else {
        ElMessage.error(result.msg);
      }
    } else {
      ElMessage.error("表单校验不通过");
    }
  });
};

const sendText = ref("发送验证码");
const sentCode = async () => {
  if (!user.phone) {
    ElMessage.warning("请输入手机号后再发送验证码");
    return;
  }

  if (isSending.value) {
    ElMessage.info(`请等待 ${countdown.value} 秒后再次发送`);
    return;
  }

  const result = await sentcode(user.phone);
  if (result.code) {
    ElMessage.success(result.msg);
    startCountdown();
  } else {
    ElMessage.error(result.msg);
  }
};

// 倒计时相关变量
const isSending = ref(false);
const countdown = ref(60);
const buttonType = ref("primary");

const startCountdown = () => {
  isSending.value = true;
  buttonType.value = "info"; // 设置为 info 类型
  sendText.value = `${countdown.value}秒后重新发送`;

  const timer = setInterval(() => {
    countdown.value--;
    sendText.value = `${countdown.value}秒后重新发送`;

    if (countdown.value <= 0) {
      isSending.value = false;
      buttonType.value = "primary"; // 恢复为 primary 类型
      sendText.value = "发送验证码";
      countdown.value = 60;
      clearInterval(timer);
    }
  }, 1000);
};

const selectedSchool = ref("");
const options = ref([]);
const loading = ref(false);
const schoolSearchText = ref("");
const showSchoolDropdown = ref(false);
const filteredOptions = ref([]);

const remoteMethod = async (query) => {
  if (query) {
    loading.value = true;
    setTimeout(async () => {
      loading.value = false;
      console.log(query.toLowerCase());
      const result = await querySchool(query.toLowerCase());
      if (result.code) {
        options.value = result.data;
      }
    }, 200);
  } else {
    options.value = [];
  }
};

const ret = () => {
  router.push("/login");
};

// 防抖
// 防抖函数
function debounce(fn, delay) {
  let timer = null;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => {
      fn.apply(this, args);
    }, delay);
  };
}
// 原有的 handleSchoolSearch 方法
const handleSchoolSearch = debounce(async (event) => {
  const query = event.target.value;
  if (query) {
    loading.value = true;
    setTimeout(async () => {
      loading.value = false;
      const result = await querySchool(query.toLowerCase());
      if (result.code) {
        filteredOptions.value = result.data;
      }
    }, 1000);
  } else {
    filteredOptions.value = [];
  }
}, 500); // 设置为 500ms 防抖时间

// 选择学校
const selectSchool = (option) => {
  user.schoolId = option.id;
  schoolSearchText.value = option.name;
  showSchoolDropdown.value = false;
};
</script>
<style scoped>
.register-page {
  display: flex;
  min-height: 100vh;
  background: #f7faff;
}

.register-left {
  flex: 1.1;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  padding: 60px 0 0 80px;
  min-width: 480px;
  max-width: 630px;
  position: relative;
}

.logo-row {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
}

.logo-img {
  width: 60px;
  height: 60px;
  border-radius: 13px;
}

.main-title {
  font-size: 24px;
  font-weight: 800;
  color: #222;
  line-height: 1.2;
}

.main-subtitle {
  font-size: 15px;
  color: #7b8ba3;
  margin-top: 3px;
  font-weight: 500;
}

.slogan-title {
  font-size: 32px;
  font-weight: 800;
  color: #222;
  margin-bottom: 16px;
  text-align: center;
  letter-spacing: 2px;
}

.slogan-desc {
  font-size: 18px;
  color: #7b8ba3;
  text-align: center;
  max-width: 340px;
  line-height: 1.6;
}

.slogan-group {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 80px;
  position: relative;
  left: -40px;
}

.register-right {
  flex: 1.6;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 420px;
  box-shadow: -8px 0 32px 0 rgba(80, 120, 200, 0.06);
}

.form-box {
  width: 560px;
  background: #fff;
  border-radius: 16px;
  padding: 48px 36px 32px 36px;
  display: flex;
  flex-direction: column;
  align-items: stretch;
}

.form-title {
  font-size: 36px;
  font-weight: 700;
  color: #222;
  margin-bottom: 8px;
}

.form-subtitle {
  font-size: 18px;
  font-weight: 500;
  color: #7b8ba3;
  margin-bottom: 28px;
}

.register-form {
  display: flex;
  flex-direction: column;
  align-items: stretch;
}

.register-form :deep(.el-form-item) {
  margin-bottom: 18px;
}

.register-form :deep(.el-form-item__content) {
  display: flex;
  align-items: center;
}

.register-form :deep(.el-form-item__error) {
  margin-top: 4px;
  margin-bottom: 0;
  font-size: 12px;
  color: #f56c6c;
  line-height: 0.7;
  margin-left: 80px;
  padding-left: 0;
}

.register-input {
  width: 100%;
  box-sizing: border-box;
  height: 52px;
  font-size: 16px;
  border-radius: 8px;
}

/* 确保所有输入框组件高度一致 */
.register-form :deep(.el-input__wrapper) {
  height: 52px !important;
  line-height: 52px !important;
}

.register-form :deep(.el-select .el-input__wrapper) {
  height: 52px !important;
  line-height: 52px !important;
}

.register-form :deep(.el-input__inner) {
  height: 52px !important;
  line-height: 52px !important;
}

.register-form :deep(.el-select .el-input__inner) {
  height: 52px !important;
  line-height: 52px !important;
}

/* 专门针对选择框的样式 */
.register-form :deep(.el-select) {
  width: 100%;
}

.register-form :deep(.el-select .el-input) {
  height: 52px !important;
}

.register-form :deep(.el-select .el-input .el-input__wrapper) {
  height: 52px !important;
  line-height: 52px !important;
  padding: 0 15px !important;
  min-height: 52px !important;
}

.register-form :deep(.el-select .el-input .el-input__inner) {
  height: 52px !important;
  line-height: 52px !important;
  font-size: 16px !important;
}

/* 更强制的方式设置选择框高度 */
.register-form :deep(.el-select) {
  height: 52px !important;
}

.register-form :deep(.el-select .el-input__wrapper) {
  height: 52px !important;
  min-height: 52px !important;
  max-height: 52px !important;
}

.register-form :deep(.el-select .el-input__inner) {
  height: 52px !important;
  line-height: 52px !important;
}

/* 确保选择框的包装器也是正确的高度 */
.register-form :deep(.el-form-item .el-select) {
  height: 52px !important;
}

.register-form :deep(.el-form-item .el-select .el-input) {
  height: 52px !important;
}

.register-form :deep(.el-form-item .el-select .el-input__wrapper) {
  height: 52px !important;
  min-height: 52px !important;
}

/* 专门针对学校选择框的样式 */
.school-select {
  height: 52px !important;
}

.school-select :deep(.el-input__wrapper) {
  height: 52px !important;
  min-height: 52px !important;
  max-height: 52px !important;
}

.school-select :deep(.el-input__inner) {
  height: 52px !important;
  line-height: 52px !important;
}

/* 更全面的选择框样式覆盖 */
.register-form :deep(.el-select) {
  height: 52px !important;
}

.register-form :deep(.el-select .el-input) {
  height: 52px !important;
}

.register-form :deep(.el-select .el-input__wrapper) {
  height: 52px !important;
  min-height: 52px !important;
  max-height: 52px !important;
  box-sizing: border-box !important;
}

.register-form :deep(.el-select .el-input__inner) {
  height: 52px !important;
  line-height: 52px !important;
  box-sizing: border-box !important;
}

/* 针对Element Plus 2.x版本的样式 */
.register-form :deep(.el-select .el-input__wrapper.is-focus) {
  height: 52px !important;
  min-height: 52px !important;
}

.register-form :deep(.el-select .el-input__wrapper:hover) {
  height: 52px !important;
  min-height: 52px !important;
}

/* 确保选择框容器也是正确高度 */
.register-form :deep(.el-form-item__content .el-select) {
  height: 52px !important;
  display: block !important;
}

.register-form :deep(.el-form-item__content .el-select .el-input) {
  height: 52px !important;
  display: block !important;
}

/* 选择框包装器样式 */
.select-wrapper {
  height: 52px;
  display: flex;
  align-items: center;
}

.select-wrapper .el-select {
  width: 100%;
  height: 52px;
}

.select-wrapper .el-select :deep(.el-input__wrapper) {
  height: 52px !important;
  min-height: 52px !important;
  max-height: 52px !important;
}

.select-wrapper .el-select :deep(.el-input__inner) {
  height: 52px !important;
  line-height: 52px !important;
}

/* 全局覆盖Element Plus选择框样式 */
:deep(.el-select) {
  height: 52px !important;
}

:deep(.el-select .el-input) {
  height: 52px !important;
}

:deep(.el-select .el-input__wrapper) {
  height: 52px !important;
  min-height: 52px !important;
  max-height: 52px !important;
}

:deep(.el-select .el-input__inner) {
  height: 52px !important;
  line-height: 52px !important;
}

/* 针对Element Plus 2.x的特定样式 */
:deep(.el-select .el-input__wrapper.is-focus) {
  height: 52px !important;
  min-height: 52px !important;
}

:deep(.el-select .el-input__wrapper:hover) {
  height: 52px !important;
  min-height: 52px !important;
}

:deep(.el-select .el-input__wrapper.is-disabled) {
  height: 52px !important;
  min-height: 52px !important;
}

/* 自定义选择框样式 */
.custom-select-wrapper {
  position: relative;
  width: 100%;
}

.custom-select-input {
  width: 100%;
  height: 52px;
  padding: 0 15px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 16px;
  background-color: #fff;
  cursor: pointer;
  box-sizing: border-box;
  transition: all 0.2s;
  color: #606266;
  font-family: inherit;
}

.custom-select-input::placeholder {
  color: #c0c4cc;
  font-family: inherit;
}

.custom-select-input:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.custom-select-input:hover {
  border-color: #c0c4cc;
}

.custom-select-input:focus::placeholder {
  color: #c0c4cc;
}

.school-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 1000;
  max-height: 200px;
  overflow-y: auto;
}

.school-option {
  padding: 12px 15px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.school-option:hover {
  background-color: #f5f7fa;
}

.no-results {
  padding: 12px 15px;
  color: #909399;
  text-align: center;
}

.dropdown-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 999;
}

/* 输入框行样式 */
.input-row {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  width: 100%;
}

.input-label {
  font-size: 16px;
  font-weight: 500;
  color: #606266;
  line-height: 1.4;
  min-width: 80px;
  flex-shrink: 0;
  margin-top: 12px;
}

.input-right {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.input-wrapper {
  position: relative;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.gender-group {
  width: 100%;
  display: flex;
  gap: 32px;
}

.gender-group :deep(.el-radio) {
  margin-right: 0;
  font-size: 16px;
}

.gender-group :deep(.el-radio__input) {
  width: 18px;
  height: 18px;
}

.gender-group :deep(.el-radio__inner) {
  width: 18px;
  height: 18px;
  border-width: 2px;
}

.gender-group :deep(.el-radio__inner::after) {
  width: 8px;
  height: 8px;
}

.gender-group :deep(.el-radio__label) {
  font-size: 16px;
  padding-left: 8px;
  color: #606266;
}

.code-row {
  display: flex;
  gap: 10px;
  align-items: center;
  width: 100%;
}

.code-btn {
  height: 44px;
  min-width: 110px;
  font-size: 14px;
  border-radius: 8px;
  padding: 0 12px;
}

.form-actions {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-top: 8px;
  align-items: stretch;
}

.register-submit-btn,
.back-btn {
  width: 100%;
  min-width: 0;
  box-sizing: border-box;
  margin: 0;
  height: 44px;
  font-size: 17px;
  font-weight: 600;
  border-radius: 8px;
}

.register-submit-btn {
  background: #1677ff;
  border: none;
  color: #fff;
  box-shadow: 0 2px 8px 0 rgba(22, 119, 255, 0.08);
  transition: background 0.2s;
}

.register-submit-btn:hover {
  background: #409eff;
}

.back-btn {
  background: #f7faff;
  border: 1.5px solid #e3edfa;
  color: #1677ff;
  box-shadow: none;
  transition: background 0.2s, color 0.2s;
}

.back-btn:hover {
  background: #e3edfa;
  color: #409eff;
}

@media (max-width: 900px) {
  .register-page {
    flex-direction: column;
  }

  .register-left,
  .register-right {
    min-width: 0;
    width: 100%;
    padding: 0;
    box-shadow: none;
  }

  .register-left {
    align-items: center;
    padding: 40px 0 0 0;
  }

  .form-box {
    margin: 40px auto;
  }

  .slogan-group {
    display: none;
  }
}

@media (max-width: 768px) {

  .register-page,
  .register-left,
  .register-right,
  .form-box {
    width: 100%;
    max-width: 100%;
    margin: 0 auto;
    box-sizing: border-box;
    padding-left: 0;
    padding-right: 0;
  }

  .register-left {
    min-height: 370px;
    padding: 0 16px 16px 16px;
    background-size: cover;
    background-position: center 63%;
  }

  .register-right {
    flex: 1;
    min-width: 0;
    padding: 0 16px 20px 16px;
    box-shadow: none;
    margin-top: -20px;
  }

  .form-box {
    padding: 24px 16px 20px 16px;
    border-radius: 12px;
  }

  .logo-img {
    width: 48px;
    height: 48px;
  }

  .main-title {
    font-size: 20px;
  }

  .main-subtitle {
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .register-left {
    min-height: 250px;
    padding: 0 12px 12px 12px;
  }

  .form-box {
    padding: 20px 12px 16px 12px;
  }

  .logo-img {
    width: 40px;
    height: 40px;
  }

  .main-title {
    font-size: 18px;
  }

  .main-subtitle {
    font-size: 12px;
  }
}

@media (max-width: 360px) {
  .register-left {
    min-height: 150px;
    padding: 0 8px 8px 8px;
  }

  .form-box {
    padding: 16px 8px 12px 8px;
  }

  .logo-img {
    width: 36px;
    height: 36px;
  }

  .main-title {
    font-size: 16px;
  }

  .main-subtitle {
    font-size: 11px;
  }
}
</style>
