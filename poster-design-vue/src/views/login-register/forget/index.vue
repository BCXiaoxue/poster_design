<template>
  <div class="forget-page">
    <!-- 左侧品牌/宣传 -->
    <div
      class="forget-left"
      :style="{ backgroundImage: `url(${loginLeftBg})` }"
    >
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
    <!-- 右侧忘记密码表单 -->
    <div class="forget-right">
      <div class="form-box">
        <div class="form-title">忘记密码</div>
        <div class="form-subtitle">画笔重置中... 100%创意加载完毕！</div>
        <el-form
          :model="user"
          :rules="rules"
          ref="userFormRef"
          class="forget-form"
        >
          <el-form-item prop="phone">
            <el-input
              v-model="user.phone"
              placeholder="手机号"
              class="forget-input"
            >
              <template #prefix>
                <el-icon><Phone /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="code">
            <div class="sms-row">
              <el-input
                v-model="user.code"
                placeholder="请输入短信验证码"
                class="forget-input"
                style="flex: 1"
              />
              <el-button
                class="sms-btn"
                :type="buttonType"
                :disabled="isSending"
                @click="sentCode"
                >{{ sendText }}</el-button
              >
            </div>
          </el-form-item>

          <el-form-item prop="newPassword">
            <el-input
              v-model="user.newPassword"
              placeholder="新密码"
              type="password"
              show-password
              class="forget-input"
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="repassword">
            <el-input
              v-model="user.repassword"
              placeholder="确认新密码"
              type="password"
              show-password
              class="forget-input"
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-button class="reset-btn" type="primary" @click="alter"
            >重置密码</el-button
          >
          <el-button class="back-btn" type="info" @click="ret"
            >返回登录</el-button
          >
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { sentcode, updatePasswordByPhone } from "@/apis/user.js";
import { useRouter } from "vue-router";
import logoUrl from "@/assets/images/logo.png";
import loginLeftBg from "@/assets/images/login_left.png";

const router = useRouter();

const user = reactive({
  newPassword: "",
  phone: "",
  code: "",
  repassword: "",
});

const userFormRef = ref();

const rules = ref({
  newPassword: [
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
  code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
  repassword: [
    { required: true, message: "请再次输入密码", trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        if (value !== user.newPassword) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
});

const sendText = ref("发送验证码");
const isSending = ref(false);
const countdown = ref(60);
const buttonType = ref("primary");
let timer = null;

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
    ElMessage.success("发送成功");
    startCountdown();
  } else {
    ElMessage.error(result.msg);
  }
};

const startCountdown = () => {
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
};

const alter = () => {
  if (!userFormRef.value) return;
  userFormRef.value.validate(async (valid) => {
    if (valid) {
      const result = await updatePasswordByPhone(user);
      if (result.code) {
        ElMessage.success("修改成功");
        router.push("/login");
      } else {
        ElMessage.error(result.msg);
      }
    } else {
      ElMessage.error("表单校验不通过");
    }
  });
};

const ret = () => {
  router.push("/login");
};
</script>

<style scoped>
.forget-page {
  display: flex;
  min-height: 100vh;
  background: #f7faff;
}

.forget-left {
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

.forget-right {
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

.forget-form {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  margin-bottom: 10px;
}

.forget-input {
  width: 100%;
  box-sizing: border-box;
  margin-bottom: 18px;
  height: 52px;
  font-size: 16px;
  border-radius: 8px;
}

.forget-input:last-of-type {
  margin-bottom: 18px;
}

.sms-row {
  display: flex;
  gap: 10px;
  align-items: stretch;
  margin-bottom: 0;
}

.sms-btn {
  height: 52px;
  width: 120px;
  font-size: 16px;
  border-radius: 8px;
  padding: 0 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  line-height: 52px;
}

.reset-btn {
  width: 100%;
  height: 44px;
  font-size: 17px;
  font-weight: 600;
  border-radius: 8px;
  background: #1677ff;
  border: none;
  color: #fff;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px 0 rgba(22, 119, 255, 0.08);
  transition: background 0.2s;
  box-sizing: border-box;
}

.reset-btn:hover {
  background: #409eff;
}

.back-btn {
  width: 100%;
  height: 44px;
  font-size: 17px;
  font-weight: 600;
  border-radius: 8px;
  background: #f7faff;
  border: 1.5px solid #e3edfa;
  color: #1677ff;
  margin: 0;
  box-shadow: none;
  transition: background 0.2s, color 0.2s;
  box-sizing: border-box;
}

.back-btn:hover {
  background: #e3edfa;
  color: #409eff;
}

@media (max-width: 900px) {
  .forget-page {
    flex-direction: column;
  }
  .forget-left,
  .forget-right {
    min-width: 0;
    width: 100%;
    padding: 0;
    box-shadow: none;
  }
  .forget-left {
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
  .forget-page,
  .forget-left,
  .forget-right,
  .form-box {
    width: 100%;
    max-width: 100%;
    margin: 0 auto;
    box-sizing: border-box;
    padding-left: 0;
    padding-right: 0;
  }
  .forget-left {
    min-height: 370px;
    padding: 0 16px 16px 16px;
    background-size: cover;
    background-position: center 63%;
  }
  .forget-right {
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
  .forget-left {
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
  .forget-left {
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
