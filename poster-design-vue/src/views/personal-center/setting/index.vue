<template>
  <div class="setting-management-container">
    <div class="content-card">
      <div class="page-header">
        <h2 class="page-title">账号安全设置</h2>
        <p class="page-subtitle">管理和修改你的账号密码、手机号等安全信息</p>
      </div>
      <el-divider class="header-divider" />
      <el-tabs v-model="activeTab" class="security-tabs">
        <!-- 修改密码面板 -->
        <el-tab-pane label="修改密码" name="password">
          <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="120px"
            class="security-form">
            <!-- 密码修改方式选择 -->
            <el-form-item label="修改方式" prop="changeMethod">
              <el-radio-group v-model="passwordForm.changeMethod" @change="resetPasswordForm">
                <el-radio label="password">输入原密码</el-radio>
                <el-radio label="phoneCode">手机验证码</el-radio>
              </el-radio-group>
            </el-form-item>

            <!-- 原密码方式 -->
            <el-form-item label="当前密码" prop="password" v-if="passwordForm.changeMethod === 'password'">
              <el-input v-model="passwordForm.password" type="password" placeholder="请输入当前密码" show-password />
            </el-form-item>

            <!-- 手机验证码方式 -->
            <template v-if="passwordForm.changeMethod === 'phoneCode'">
              <el-form-item label="绑定手机号" prop="phone">
                <el-input v-model="passwordForm.phone" placeholder="当前绑定的手机号" disabled />
              </el-form-item>
              <el-form-item label="验证码" prop="code">
                <el-row :gutter="10">
                  <el-col :span="14">
                    <el-input v-model="passwordForm.code" placeholder="请输入验证码" />
                  </el-col>
                  <el-col :span="10">
                    <el-button type="default" @click="sendPasswordCode" :disabled="passwordCountDown > 0">
                      {{
                        passwordCountDown > 0
                          ? `${passwordCountDown}s后重发`
                          : "发送验证码"
                      }}
                    </el-button>
                  </el-col>
                </el-row>
              </el-form-item>
            </template>

            <!-- 新密码（两种方式共用） -->
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码（6-20位）"
                @input="checkPasswordStrength" show-password />
              <div class="password-strength" v-if="passwordForm.newPassword">
                <div class="strength-bar" :class="strengthClass[0]"></div>
                <div class="strength-bar" :class="strengthClass[1]"></div>
                <div class="strength-bar" :class="strengthClass[2]"></div>
                <span class="strength-text">{{ strengthText }}</span>
              </div>
            </el-form-item>

            <el-form-item label="确认新密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="submitPassword">保存修改</el-button>
            </el-form-item>

          </el-form>
        </el-tab-pane>

        <!-- 改绑手机面板 -->
        <el-tab-pane label="改绑手机" name="phone">
          <el-form ref="phoneFormRef" :model="phoneForm" :rules="phoneRules" label-width="120px" class="security-form">
            <el-form-item label="当前手机号" prop="phone">
              <el-input v-model="phoneForm.phone" placeholder="当前绑定的手机号" disabled />
            </el-form-item>

            <el-form-item label="新手机号" prop="newPhone">
              <el-input v-model="phoneForm.newPhone" placeholder="请输入新手机号" maxlength="11" />
            </el-form-item>

            <el-form-item label="验证码" prop="code">
              <el-row :gutter="10">
                <el-col :span="14">
                  <el-input v-model="phoneForm.code" placeholder="请输入验证码" />
                </el-col>
                <el-col :span="10">
                  <el-button type="default" @click="sendPhoneCode"
                    :disabled="phoneCountDown > 0 || !phoneForm.newPhone">
                    {{
                      phoneCountDown > 0
                        ? `${phoneCountDown}s后重发`
                        : "发送验证码"
                    }}
                  </el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="submitPhone">确认改绑</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import {
  getUserInfo,
  updatePassword,
  updatePasswordByPhone,
  sendCode,
  updatePhone,
} from "@/apis/person";
import { usePersonStore } from "@/stores/person";
const personStore = usePersonStore();

// 标签页切换
const activeTab = ref("password");

// 密码表单数据
const passwordForm = reactive({
  changeMethod: "password", // 默认：输入原密码
  password: "",
  newPassword: "",
  confirmPassword: "",
  phone: "", // 绑定的手机号（实际从接口获取）
  code: "",
});

// 获取用户信息
const search = async () => {
  const res = await getUserInfo();
  if (res.code) {
    passwordForm.phone = res.data.phone;
    phoneForm.phone = res.data.phone;
  } else {
    ElMessage.error(res.msg || "获取用户信息失败");
  }
};

// 提交密码修改
const submitPassword = async () => {
  try {
    await passwordFormRef.value.validate();
    if (passwordForm.changeMethod === "password") {
      const res = await updatePassword(passwordForm);
      if (res.code) {
        ElMessage.success("密码修改成功！");
        resetPasswordForm();
      } else {
        ElMessage.error(res.msg || "密码修改失败");
      }
    } else if (passwordForm.changeMethod === "phoneCode") {
      const res1 = await updatePasswordByPhone(passwordForm);
      if (res1.code) {
        ElMessage.success("密码修改成功！");
        resetPasswordForm();
      } else {
        ElMessage.error(res1.msg || "密码修改失败");
      }
    }
  } catch (error) {
    console.log("密码表单验证失败", error);
  }
};

// 提交手机改绑
const submitPhone = async () => {
  try {
    await phoneFormRef.value.validate();
    const res = await updatePhone(phoneForm);
    if (res.code) {
      ElMessage.success("手机号改绑成功！");
      phoneForm.newPhone = "";
      phoneForm.code = "";
      phoneCountDown.value = 0;
    }
    // 模拟接口请求
    // setTimeout(() => {
    //     ElMessage.success('手机号改绑成功');
    //     // 更新显示的手机号
    //     phoneForm.phone = phoneForm.newPhone.replace(/(\d{3})(\d{4})(\d{4})/, '$1****$3');
    //     // 同步更新密码表单中的手机号
    //     passwordForm.phone = phoneForm.phone;
    //     // 重置表单
    //     phoneForm.newPhone = '';
    //     phoneForm.code = '';
    //     phoneCountDown.value = 0;
    // }, 800);
  } catch (error) {
    console.log("手机表单验证失败", error);
  }
};

// 手机表单数据
const phoneForm = reactive({
  phone: "", // 当前手机号（实际从接口获取）
  newPhone: "",
  code: "",
});

// 表单引用
const passwordFormRef = ref(null);
const phoneFormRef = ref(null);

// 切换修改方式时重置表单
const resetPasswordForm = () => {
  passwordForm.password = "";
  passwordForm.code = "";
  passwordForm.newPassword = "";
  passwordForm.confirmPassword = "";
  if (passwordFormRef.value) {
    passwordFormRef.value.clearValidate();
  }
};

// 密码验证规则
const passwordRules = {
  changeMethod: [
    { required: true, message: "请选择修改方式", trigger: "change" },
  ],
  password: [{ required: true, message: "请输入当前密码", trigger: "blur" }],
  phone: [{ required: true, message: "手机号不能为空", trigger: "blur" }],
  code: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { len: 6, message: "验证码为6位数字", trigger: "blur" },
    { pattern: /^\d{6}$/, message: "验证码只能是数字", trigger: "blur" },
  ],
  newPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度在6-20位之间", trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        if (
          passwordForm.changeMethod === "password" &&
          value === passwordForm.password
        ) {
          callback(new Error("新密码不能与当前密码相同"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
  confirmPassword: [
    { required: true, message: "请确认新密码", trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
};

// 密码强度检测
const strengthClass = ref(["", "", ""]);
const strengthText = ref("");

const checkPasswordStrength = (value) => {
  let strength = 0;
  // 包含数字
  if (/\d/.test(value)) strength++;
  // 包含字母
  if (/[a-zA-Z]/.test(value)) strength++;
  // 包含特殊字符
  if (/[^a-zA-Z0-9]/.test(value)) strength++;

  // 更新强度样式
  strengthClass.value = ["", "", ""].map((_, i) =>
    i < strength ? "strength-" + (i + 1) : ""
  );

  // 更新强度文本
  const textMap = ["弱", "中", "强"];
  strengthText.value = `密码强度：${textMap[strength - 1] || "未达标"}`;
};

// 密码验证码倒计时
const passwordCountDown = ref(0);
const sendPasswordCode = async () => {
  // 模拟发送验证码
  passwordCountDown.value = 60;
  // ElMessage.success("验证码已发送至手机：" + passwordForm.phone);
  const res = await sendCode(passwordForm.phone);
  if (res.code) {
    ElMessage.success(res.msg);
  } else {
    ElMessage.error(res.msg);
  }
  const timer = setInterval(() => {
    passwordCountDown.value--;
    if (passwordCountDown.value <= 0) {
      clearInterval(timer);
    }
  }, 1000);
};

// 手机验证规则
const phoneRules = {
  newPhone: [
    { required: true, message: "请输入新手机号", trigger: "blur" },
    {
      pattern: /^1[3-9]\d{9}$/,
      message: "请输入正确的手机号",
      trigger: "blur",
    },
    {
      validator: (rule, value, callback) => {
        if (value === phoneForm.phone.replace(/\*/g, "")) {
          callback(new Error("新手机号不能与当前手机号相同"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
  code: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { len: 6, message: "验证码为6位数字", trigger: "blur" },
    { pattern: /^\d{6}$/, message: "验证码只能是数字", trigger: "blur" },
  ],
};

// 手机验证码倒计时
const phoneCountDown = ref(0);
const sendPhoneCode = async () => {
  // 验证新手机号格式
  if (!/^1[3-9]\d{9}$/.test(phoneForm.newPhone)) {
    ElMessage.error("请输入正确的新手机号");
    return;
  }

  // 模拟发送验证码
  phoneCountDown.value = 60;
  // ElMessage.success("验证码已发送至原手机：" + phoneForm.phone);
  const res = await sendCode(phoneForm.phone);
  if (res.code) {
    ElMessage.success(res.msg);
  } else {
    ElMessage.error(res.msg);
  }
  const timer = setInterval(() => {
    phoneCountDown.value--;
    if (phoneCountDown.value <= 0) {
      clearInterval(timer);
    }
  }, 1000);
};


onMounted(() => {
  search();
});
</script>

<style scoped>
.setting-management-container {
  padding: 0;
  background: transparent;
  width: 100%;
  max-width: none;
  margin: 0;
}

.content-card {
  margin: 0;
  margin-top: 12px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  background: #fff;
  padding: 24px 32px 24px 32px;
}

.page-header {
  padding-top: 24px;
  margin-bottom: 8px;
  padding-left: 8px;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  color: #222;
  margin: 0 0 4px 0;
}

.page-subtitle {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.header-divider {
  margin: 0 0 16px 0;
}

.security-tabs {
  margin-bottom: 0;
}

.security-form {
  max-width: 520px;
  margin-left: 0px;
  background: transparent;
}

/* 密码强度样式 */
.password-strength {
  display: flex;
  align-items: center;
  margin-top: 8px;
  height: 20px;
}

.strength-bar {
  width: 60px;
  height: 6px;
  margin-right: 5px;
  background-color: #eee;
  border-radius: 3px;
}

.strength-1 {
  background-color: #ff4d4f;
}

.strength-2 {
  background-color: #faad14;
}

.strength-3 {
  background-color: #52c41a;
}

.strength-text {
  margin-left: 10px;
  font-size: 12px;
  color: #666;
}

/* 移动端适配 */
@media (max-width: 767px) {
  .setting-management-container {
    padding: 0 8px;
  }

  .content-card {
    padding: 16px 12px;
    margin-top: 8px;
  }

  .page-header {
    padding-top: 16px;
    margin-bottom: 8px;
    padding-left: 0;
  }

  .page-title {
    font-size: 18px;
    margin-bottom: 6px;
  }

  .page-subtitle {
    font-size: 13px;
  }

  .header-divider {
    margin: 0 0 12px 0;
  }

  .security-form {
    max-width: 100%;
    margin-left: 0;
  }

  /* 标签页移动端适配 */
  :deep(.el-tabs__header) {
    margin-bottom: 16px;
  }

  :deep(.el-tabs__nav-wrap) {
    padding: 0 8px;
  }

  :deep(.el-tabs__item) {
    font-size: 14px;
    padding: 0 12px;
  }

  :deep(.el-tabs__content) {
    padding: 0;
  }

  /* 表单移动端适配 */
  :deep(.el-form-item__label) {
    font-size: 14px;
    line-height: 1.4;
  }

  :deep(.el-form-item) {
    margin-bottom: 20px;
  }

  :deep(.el-input) {
    font-size: 14px;
  }

  :deep(.el-button) {
    font-size: 14px;
    height: 40px;
  }

  /* 单选按钮组移动端适配 */
  :deep(.el-radio-group) {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  :deep(.el-radio) {
    margin-right: 0;
    margin-bottom: 8px;
  }

  :deep(.el-radio__label) {
    font-size: 14px;
  }

  /* 验证码输入框移动端适配 */
  :deep(.el-row) {
    margin: 0 !important;
  }

  :deep(.el-col) {
    padding: 0 !important;
  }

  :deep(.el-col-14) {
    width: 100% !important;
    margin-bottom: 8px;
  }

  :deep(.el-col-10) {
    width: 100% !important;
  }

  :deep(.el-col-10 .el-button) {
    width: 100%;
    height: 40px;
  }

  /* 密码强度指示器移动端适配 */
  .password-strength {
    margin-top: 12px;
    height: 24px;
  }

  .strength-bar {
    width: 50px;
    height: 8px;
    margin-right: 4px;
  }

  .strength-text {
    font-size: 11px;
    margin-left: 8px;
  }

  /* 提交按钮移动端适配 */
  :deep(.el-form-item:last-child) {
    margin-bottom: 0;
  }

  :deep(.el-form-item:last-child .el-button) {
    width: 100%;
    height: 44px;
    font-size: 16px;
    font-weight: 600;
  }
}

/* 平板端适配 */
@media (min-width: 768px) and (max-width: 1023px) {
  .content-card {
    padding: 20px 16px;
  }

  .page-title {
    font-size: 20px;
  }

  .security-form {
    max-width: 600px;
  }

  :deep(.el-tabs__item) {
    font-size: 15px;
  }
}
</style>
