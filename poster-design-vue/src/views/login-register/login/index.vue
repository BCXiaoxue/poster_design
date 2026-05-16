<template>
  <div class="login-page">
    <!-- 左侧品牌/宣传 -->
    <div class="login-left" :style="{ backgroundImage: `url(${loginLeftBg})` }">
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
    <!-- 右侧登录表单 -->
    <div class="login-right">
      <div class="form-box">
        <div class="form-title">欢迎登录</div>
        <div class="form-subtitle">画笔登录中... 100%创意加载完毕！</div>
        <div class="login-tabs">
          <div :class="['tab', loginType === 'password' ? 'active' : '']" @click="loginType = 'password'">
            密码登录
          </div>
          <div :class="['tab', loginType === 'sms' ? 'active' : '']" @click="loginType = 'sms'">
            短信登录
          </div>
        </div>
        <el-form class="login-form">
          <el-input v-model="loginForm.phone" placeholder="账号/手机号" class="login-input" inputmode="text">
            <template #prefix>
              <el-icon>
                <Phone />
              </el-icon>
            </template>
          </el-input>
          <template v-if="loginType === 'password'">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" class="login-input"
              inputmode="text">
              <template #prefix>
                <el-icon>
                  <Lock />
                </el-icon>
              </template>
            </el-input>
          </template>
          <template v-else>
            <div class="sms-row">
              <el-input v-model="loginForm.sms" placeholder="请输入短信验证码" class="login-input" style="flex: 1"
                inputmode="numeric" />
              <el-button class="sms-btn" :type="buttonType" :disabled="isSending" @click="sentCode">{{ sendText
                }}</el-button>
            </div>
          </template>
          <div class="form-actions">
            <a class="forget-link" href="javascript:void(0)" @click="forget">忘记密码</a>
          </div>
          <el-button class="login-btn" type="primary" @click="login">登 录</el-button>
          <el-button class="register-btn" type="info" @click="register">注册</el-button>
        </el-form>
        <div class="other-login">
          <div class="other-line"><span>选择其他登录方式</span></div>
          <div class="other-icons">
            <el-icon class="other-icon wechat"><svg viewBox="0 0 1024 1024" width="28" height="28">
                <circle cx="512" cy="512" r="512" fill="#1AAD19" />
                <circle cx="512" cy="512" r="400" fill="#fff" />
                <circle cx="420" cy="540" r="40" fill="#1AAD19" />
                <circle cx="600" cy="540" r="40" fill="#1AAD19" />
              </svg></el-icon>
            <el-icon class="other-icon alipay"><svg viewBox="0 0 1024 1024" width="28" height="28">
                <circle cx="512" cy="512" r="512" fill="#1677FF" />
                <text x="50%" y="60%" text-anchor="middle" font-size="320" fill="#fff" font-family="Arial">
                  支
                </text>
              </svg></el-icon>
          </div>
        </div>
      </div>
    </div>
  </div>
  <captcha-modal v-if="showCaptcha" @close="showCaptcha = false" @success="handleCaptchaSuccess" />
</template>

<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { loginApi, loginByCode } from "@/apis/login.js";
import { sentcode } from "@/apis/user.js";
import logoUrl from "@/assets/images/logo.png";
import loginLeftBg from "@/assets/images/login_left.png";
import { Phone, Lock } from "@element-plus/icons-vue";
import captchaModal from "../components/captchaModal.vue";
import { usePersonStore } from "@/stores/person.js";
import { getUserInfo } from "@/apis/person.js";

const showCaptcha = ref(false);
const isCaptchaVerified = ref(false);
const personStore = usePersonStore();
const handleCaptchaSuccess = () => {
  showCaptcha.value = false;
  isCaptchaVerified.value = true;
  login(); // 成功后再触发登录逻辑
};

const openCaptcha = () => {
  showCaptcha.value = true;
  isCaptchaVerified.value = false;
};
let loginForm = ref({ phone: "", password: "", sms: "" });
const router = useRouter();
const loginType = ref("password");

// 注册页验证码倒计时逻辑
const sendText = ref("发送验证码");
const isSending = ref(false);
const countdown = ref(60);
const buttonType = ref("primary");
let timer = null;

const sentCode = async () => {
  if (!loginForm.value.phone) {
    ElMessage.warning("请输入手机号后再发送验证码");
    return;
  }
  if (isSending.value) {
    ElMessage.info(`请等待 ${countdown.value} 秒后再次发送`);
    return;
  }
  const result = await sentcode(loginForm.value.phone);
  if (result.code) {
    ElMessage.success(result.msg);
    startCountdown();
  } else {
    ElMessage.error(result.msg);
  }
};
function startCountdown() {
  isSending.value = true;
  buttonType.value = "info";
  sendText.value = `${countdown.value}秒后重新发送`;
  timer = setInterval(() => {
    countdown.value--;
    sendText.value = `${countdown.value}秒后重新发送`;
    if (countdown.value <= 0) {
      isSending.value = false;
      buttonType.value = "primary";
      sendText.value = "发送验证码";
      countdown.value = 60;
      clearInterval(timer);
    }
  }, 1000);
}

const getUser = async () => {
  const result = await getUserInfo();
  personStore.userId = result.data.id;
  personStore.imgUrl = result.data.avatarUrl;
  personStore.authority = result.data.authority;
};

const login = async () => {
  if (loginType.value === "password") {
    const result = await loginApi({
      phone: loginForm.value.phone,
      password: loginForm.value.password,
    });
    if (!isCaptchaVerified.value) {
      openCaptcha();
      return;
    }
    if (result.code) {
      ElMessage.success("登录成功");
      localStorage.setItem("jwt", result.data.jwt);
      await getUser();
      if (result.data.authority == 0) {
        router.push("/index");
      } else {
        router.push("/admin");
      }
    } else {
      ElMessage.error(result.msg);
    }
  } else {
    // 短信登录逻辑
    const result = await loginByCode({
      phone: loginForm.value.phone,
      code: loginForm.value.sms,
    });

    if (!isCaptchaVerified.value) {
      openCaptcha();
      return;
    }
    if (result.code) {
      ElMessage.success("登录成功");
      localStorage.setItem("jwt", result.data.jwt);
      await getUser();
      if (result.data.authority == 0) {
        router.push("/index");
      } else {
        router.push("/admin");
      }
    } else {
      ElMessage.error(result.msg);
    }
  }
};
const forget = () => {
  router.push("/forget");
};
const register = () => {
  router.push("/register");
};
</script>

<style scoped>
.login-page {
  display: flex;
  min-height: 100vh;
  background: #f7faff;
  width: 100%;
  overflow-x: hidden;
}

.login-left {
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
  box-sizing: border-box;
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

.login-right {
  flex: 1.6;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 420px;
  box-shadow: -8px 0 32px 0 rgba(80, 120, 200, 0.06);
  box-sizing: border-box;
  flex-shrink: 0;
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

.login-tabs {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}

.tab {
  font-size: 16px;
  color: #7b8ba3;
  cursor: pointer;
  padding-bottom: 4px;
  border-bottom: 2px solid transparent;
  transition: color 0.2s, border-color 0.2s;
}

.tab.active {
  color: #1677ff;
  border-bottom: 2px solid #1677ff;
  font-weight: 600;
}

.login-form {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  margin-bottom: 10px;
}

.login-input {
  width: 100%;
  box-sizing: border-box;
  margin-bottom: 18px;
  height: 52px;
  font-size: 16px;
  border-radius: 8px;
}

.login-input:last-of-type {
  margin-bottom: 0;
}

.sms-row {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 18px;
}

.sms-btn {
  height: 44px;
  min-width: 110px;
  font-size: 14px;
  border-radius: 8px;
  padding: 0 12px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 8px;
}

.forget-link {
  color: #7b8ba3;
  font-size: 14px;
  text-decoration: none;
  transition: color 0.2s;
}

.forget-link:hover {
  color: #1677ff;
}

.login-btn {
  width: 100% !important;
  height: 44px !important;
  font-size: 17px !important;
  font-weight: 600 !important;
  border-radius: 8px !important;
  background: #1677ff !important;
  border: none !important;
  color: #fff !important;
  margin-bottom: 16px !important;
  box-shadow: 0 2px 8px 0 rgba(22, 119, 255, 0.08) !important;
  transition: background 0.2s !important;
  box-sizing: border-box !important;
  display: block !important;
  text-align: center !important;
  line-height: 44px !important;
}

.login-btn:hover {
  background: #409eff !important;
}

.register-btn {
  width: 100% !important;
  height: 44px !important;
  font-size: 17px !important;
  font-weight: 600 !important;
  border-radius: 8px !important;
  background: #f7faff !important;
  border: 1.5px solid #e3edfa !important;
  color: #1677ff !important;
  margin: 0 !important;
  box-shadow: none !important;
  transition: background 0.2s, color 0.2s !important;
  box-sizing: border-box !important;
  display: block !important;
  text-align: center !important;
  line-height: 44px !important;
}

.register-btn:hover {
  background: #e3edfa;
  color: #409eff;
}

.other-login {
  margin-top: 28px;
  text-align: center;
}

.other-line {
  color: #bfc8d6;
  font-size: 13px;
  margin-bottom: 12px;
  position: relative;
}

.other-line:before,
.other-line:after {
  content: "";
  display: inline-block;
  width: 60px;
  height: 1px;
  background: #e3edfa;
  vertical-align: middle;
  margin: 0 10px;
}

.other-icons {
  display: flex;
  justify-content: center;
  gap: 24px;
}

.other-icon {
  cursor: pointer;
  border-radius: 50%;
  background: #f7faff;
  box-shadow: 0 2px 8px 0 rgba(22, 119, 255, 0.08);
  padding: 6px;
  transition: box-shadow 0.2s, transform 0.2s;
}

.other-icon:hover {
  box-shadow: 0 4px 16px 0 rgba(22, 119, 255, 0.16);
  transform: translateY(-2px) scale(1.08);
}

.wechat svg {
  display: block;
}

.alipay svg {
  display: block;
}

/* 平板端适配 */
@media (max-width: 900px) {
  .login-page {
    flex-direction: column;
  }

  .login-left,
  .login-right {
    min-width: 0;
    width: 100%;
    padding: 0;
    box-shadow: none;
  }

  .login-left {
    align-items: center;
    padding: 40px 20px 20px 20px;
    min-height: 300px;
  }

  .form-box {
    margin: 20px auto;
    width: 90%;
    max-width: 480px;
    padding: 32px 24px 24px 24px;
  }

  .form-title {
    font-size: 28px;
  }

  .form-subtitle {
    font-size: 16px;
  }

  .slogan-title {
    font-size: 28px;
  }

  .slogan-desc {
    font-size: 16px;
    max-width: 280px;
  }

  .slogan-group {
    display: none;
  }
}

/* 移动端适配 */
@media (max-width: 768px) {

  .login-page,
  .login-left,
  .login-right,
  .form-box {
    width: 100%;
    max-width: 100%;
    margin: 0 auto;
    box-sizing: border-box;
    padding-left: 0;
    padding-right: 0;
  }

  .login-left {
    flex: none;
    min-height: 370px;
    padding: 0 16px 16px 16px;
    background-size: cover;
    background-position: center 63%;
  }

  .logo-row {
    gap: 12px;
    margin-bottom: 8px;
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

  .slogan-group {
    margin-top: 20px;
    left: 0;
  }

  .slogan-title {
    font-size: 24px;
    margin-bottom: 12px;
  }

  .slogan-desc {
    font-size: 13px;
    max-width: 100%;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .login-right {
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

  .form-title {
    font-size: 24px;
    margin-bottom: 6px;
  }

  .form-subtitle {
    font-size: 14px;
    margin-bottom: 20px;
  }

  .login-tabs {
    gap: 20px;
    margin-bottom: 20px;
  }

  .tab {
    font-size: 15px;
  }

  .login-input {
    height: 48px;
    font-size: 15px;
    margin-bottom: 16px;
  }

  .sms-row {
    gap: 8px;
    margin-bottom: 16px;
  }

  .sms-btn {
    height: 48px;
    min-width: 100px;
    font-size: 13px;
    padding: 0 8px;
  }

  .form-actions {
    margin-bottom: 6px;
  }

  .forget-link {
    font-size: 13px;
  }

  .login-btn,
  .register-btn {
    height: 48px;
    font-size: 16px;
    border-radius: 8px;
  }

  .login-btn {
    margin-bottom: 12px;
  }

  .other-login {
    margin-top: 20px;
  }

  .other-line {
    font-size: 12px;
    margin-bottom: 10px;
  }

  .other-line:before,
  .other-line:after {
    width: 40px;
    margin: 0 8px;
  }

  .other-icons {
    gap: 20px;
  }

  .other-icon {
    padding: 5px;
  }

  .other-icon svg {
    width: 24px;
    height: 24px;
  }
}

/* 小屏手机适配 */
@media (max-width: 480px) {
  .login-left {
    min-height: 250px;
    padding: 16px 12px 12px 12px;
  }

  .logo-row {
    gap: 10px;
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

  .slogan-group {
    margin-top: 10px;
  }

  .slogan-title {
    font-size: 20px;
    margin-bottom: 8px;
  }

  .slogan-desc {
    font-size: 12px;
    max-width: 100%;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .login-right {
    padding: 0 12px 16px 12px;
  }

  .form-box {
    padding: 20px 16px 16px 16px;
  }

  .form-title {
    font-size: 22px;
  }

  .form-subtitle {
    font-size: 13px;
    margin-bottom: 18px;
  }

  .login-tabs {
    gap: 16px;
    margin-bottom: 18px;
  }

  .tab {
    font-size: 14px;
  }

  .login-input {
    height: 44px;
    font-size: 14px;
    margin-bottom: 14px;
  }

  .sms-btn {
    height: 44px;
    min-width: 90px;
    font-size: 12px;
  }

  .login-btn,
  .register-btn {
    height: 44px;
    font-size: 15px;
  }

  .other-icon svg {
    width: 22px;
    height: 22px;
  }
}

/* 超小屏手机适配 */
@media (max-width: 360px) {
  .login-left {
    min-height: 150px;
    padding: 12px 8px 8px 8px;
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

  .slogan-title {
    font-size: 18px;
  }

  .slogan-desc {
    font-size: 12px;
  }

  .form-box {
    padding: 16px 12px 12px 12px;
  }

  .form-title {
    font-size: 20px;
  }

  .form-subtitle {
    font-size: 12px;
  }

  .login-input {
    height: 42px;
    font-size: 13px;
  }

  .sms-btn {
    height: 42px;
    min-width: 80px;
    font-size: 11px;
  }

  .login-btn,
  .register-btn {
    height: 42px;
    font-size: 14px;
  }

  .slogan-group {
    margin-top: 0px;
  }
}

/* 移动端通用优化 */
@media (max-width: 768px) {

  /* 防止输入框缩放 */
  .login-input input {
    font-size: 16px !important;
  }

  /* 优化触摸体验 */
  .tab,
  .forget-link,
  .other-icon {
    -webkit-tap-highlight-color: transparent;
  }

  /* 防止按钮点击时的闪烁 */
  .login-btn,
  .register-btn,
  .sms-btn {
    -webkit-tap-highlight-color: transparent;
    -webkit-touch-callout: none;
    -webkit-user-select: none;
    user-select: none;
  }

  /* 优化滚动体验 */
  .login-page {
    -webkit-overflow-scrolling: touch;
  }

  /* 确保内容不被状态栏遮挡 */
  .login-page {
    padding-top: env(safe-area-inset-top);
    padding-bottom: env(safe-area-inset-bottom);
  }
}

/* 强制覆盖 Element Plus 按钮样式 */
.login-btn.el-button,
.register-btn.el-button {
  width: 100% !important;
  height: 44px !important;
  font-size: 17px !important;
  font-weight: 600 !important;
  border-radius: 8px !important;
  box-sizing: border-box !important;
  display: block !important;
  text-align: center !important;
  line-height: 44px !important;
  margin: 0 !important;
  padding: 0 !important;
}

.login-btn.el-button {
  background: #1677ff !important;
  border: none !important;
  color: #fff !important;
  margin-bottom: 16px !important;
  box-shadow: 0 2px 8px 0 rgba(22, 119, 255, 0.08) !important;
}

.register-btn.el-button {
  background: #f7faff !important;
  border: 1.5px solid #e3edfa !important;
  color: #1677ff !important;
  box-shadow: none !important;
}

/* 确保按钮容器样式正确 */
.login-form {
  display: flex !important;
  flex-direction: column !important;
  align-items: stretch !important;
  width: 100% !important;
  box-sizing: border-box !important;
}
</style>
