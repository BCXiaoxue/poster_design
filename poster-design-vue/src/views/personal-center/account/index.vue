<template>
  <div class="page-wrapper">
    <div class="profile-header-row">
      <div class="profile-header-card profile-header-card-relative">
        <div class="profile-header-flex">
          <div class="profile-header-left">
            <div class="avatar-upload-vertical">
              <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                :before-upload="editBeforeAvatarUpload"
                :http-request="handleAvatarUpload"
              >
                <img
                  v-lazy
                  v-if="pendingAvatarUrl || formData.avatarUrl"
                  :src="pendingAvatarUrl || formData.avatarUrl"
                  class="avatar"
                  width="96px"
                  height="96px"
                  alt="用户头像"
                />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
              <div v-if="pendingAvatarUrl" class="avatar-save-btn-wrap">
                <el-button type="primary" size="small" @click="saveAvatarToDb"
                  >保存头像</el-button
                >
              </div>
            </div>
            <div class="profile-header-info">
              <div class="profile-header-main">
                <span class="profile-header-name"
                  >{{ formData.username }}
                  <template v-if="formData.gender === 1">
                    <el-icon class="gender-icon" title="男"><User /></el-icon>
                  </template>
                  <template v-else-if="formData.gender == 2">
                    <el-icon class="gender-icon female" title="女"
                      ><UserFilled
                    /></el-icon>
                  </template>
                </span>
              </div>
              <div class="profile-header-join">
                ID：ST{{ String(formData.id).padStart(5, "0") }}
              </div>
            </div>
          </div>
          <div class="profile-header-divider"></div>
          <div class="profile-header-meta-flex">
            <div class="profile-header-meta-row">
              <div class="meta-group">
                <span class="meta-label">{{
                  formData.university || "未填写"
                }}</span>
                <span class="meta-desc">学校</span>
              </div>
              <div class="meta-group">
                <span class="meta-label">{{
                  formData.birthday || "未填写"
                }}</span>
                <span class="meta-desc">出生日期</span>
              </div>
              <div class="meta-group">
                <span class="meta-label">{{ formData.phone || "未填写" }}</span>
                <span class="meta-desc">电话号码</span>
              </div>
              <div class="meta-group email">
                <span class="meta-label">{{ formData.email || "未填写" }}</span>
                <span class="meta-desc">邮箱</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="extra-content-row">
      <el-card class="extra-content-card">
        <template #header>
          <h2 class="section-title">相关统计</h2>
        </template>
        <el-row :gutter="24" class="stats-row">
          <el-col :span="6" xs="24" sm="12" md="6">
            <div class="stat-item">
              <div class="stat-value">
                {{ count.participateCompetitionNum }}
              </div>
              <div class="stat-label">参赛次数</div>
            </div>
          </el-col>
          <el-col :span="6" xs="24" sm="12" md="6">
            <div class="stat-item">
              <div class="stat-value">{{ count.winNum }}</div>
              <div class="stat-label">获奖次数</div>
            </div>
          </el-col>
          <el-col :span="6" xs="24" sm="12" md="6">
            <div class="stat-item">
              <div class="stat-value">{{ count.workNum }}</div>
              <div class="stat-label">作品数量</div>
            </div>
          </el-col>
          <el-col :span="6" xs="24" sm="12" md="6">
            <div class="stat-item">
              <div class="stat-value">{{ count.teamNum }}</div>
              <div class="stat-label">团队个数</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <el-card class="quick-actions-card" style="margin-top: 24px">
      <template #header>
        <h2 class="section-title">常用系统入口</h2>
      </template>
      <el-row :gutter="20">
        <el-col
          v-for="item in quickActions"
          :key="item.name"
          :span="6"
          style="margin-bottom: 20px"
        >
          <div
            class="quick-action-item"
            :style="{
              background: '#fff',
              borderRadius: '12px',
              boxShadow: '0 2px 8px rgba(0,0,0,0.06)',
              cursor: 'pointer',
              display: 'flex',
              alignItems: 'center',
              padding: '18px 16px',
            }"
            @click="handleQuickAction(item.route)"
          >
            <el-icon
              :style="{
                fontSize: '28px',
                color: item.color,
                marginRight: '16px',
              }"
            >
              <component :is="item.icon" />
            </el-icon>
            <span style="font-size: 16px; font-weight: 500; color: black">{{
              item.name
            }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 编辑资料弹窗 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑个人资料"
      width="420px"
      :close-on-click-modal="false"
    >
      <el-form label-width="80px">
        <el-form-item label="姓名">
          <el-input
            v-model="editForm.username"
            placeholder="请输入姓名"
            disabled
          />
        </el-form-item>
        <el-form-item label="出生日期">
          <el-date-picker
            v-model="editForm.birthday"
            type="date"
            placeholder="请选择出生日期"
            style="width: 100%"
            value-format="YYYY-MM-DD" 
          />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input
            v-model="editForm.phone"
            placeholder="请输入手机号"
            disabled
          />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input
            v-model="editForm.email"
            placeholder="请输入邮箱"
            type="email"
          />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="editForm.gender" disabled>
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio> <!-- 女生选项值为2 -->
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleEditSave">保存</el-button>
          <el-button @click="editDialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { getUserInfo, updateUserInfo, uploadImage } from "@/apis/person.js";
import { Plus, User, UserFilled } from "@element-plus/icons-vue";
import { usePersonStore } from "@/stores/person";
import { useRouter } from "vue-router";
import { getcount } from "@/apis/user.js";

const personStore = usePersonStore();
const router = useRouter();

const count = ref({
  participateCompetitionNum: 0,
  winNum: 0,
  workNum: 0,
  teamNum: 0,
});

// 主用户信息（展示区使用）
const formData = reactive({
  id: 0,
  username: "",
  email: "",
  phone: "",
  status: 0,
  lastLogin: "",
  avatarUrl: "",
  gender: "",
  birthday: "",
  authority: 0,
  schoolId: 0,
});

// 编辑表单数据（弹窗中使用）
const editForm = reactive({
  id: 0,
  username: "",
  email: "",
  phone: "",
  gender: "",
  birthday: "",
});

const uploadLoading = ref(false);
const editDialogVisible = ref(false);
const pendingAvatarUrl = ref("");

// 获取用户信息
const search = async () => {
  try {
    const res = await getUserInfo();
    if (res.code) {
      // 更新展示数据
      Object.assign(formData, res.data);
      // 更新编辑表单数据
      Object.assign(editForm, {
        id: res.data.id,
        username: res.data.username,
        email: res.data.email,
        phone: res.data.phone,
        gender: res.data.gender,
        birthday: res.data.birthday,
      });
    } else {
      ElMessage.error(res.msg || "获取用户信息失败");
    }
  } catch (error) {
    console.error("获取用户信息失败:", error);
    ElMessage.error("网络错误，获取用户信息失败");
  }
};

// 更新用户信息
const mypudate = async (data) => {
  if (data.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)) {
    ElMessage.warning("请输入有效的邮箱地址");
    return;
  }

  try {
    const updateData = {
      id: data.id,
      username: data.username,
      email: data.email,
      gender: data.gender,
      avatarUrl: formData.avatarUrl, // 使用主数据中的头像URL
      birthday: data.birthday,
    };

    const res = await updateUserInfo(updateData);
    if (res.code) {
      // 更新成功后更新主数据
      Object.assign(formData, updateData);
      ElMessage.success("更新用户信息成功");
      return true;
    } else {
      ElMessage.error(res.msg || "更新用户信息失败");
      return false;
    }
  } catch (error) {
    console.error("更新用户信息失败:", error);
    ElMessage.error("网络错误，更新失败");
    return false;
  }
};

// 图片上传前校验
const editBeforeAvatarUpload = (rawFile) => {
  const isImage = rawFile.type.startsWith("image/");
  if (!isImage) {
    ElMessage.error("请上传图片文件！");
    return false;
  }
  if (rawFile.size / 1024 / 1024 > 5) {
    ElMessage.error("图片大小不能超过5MB!");
    return false;
  }
  return true;
};

// 自定义图片上传方法
const handleAvatarUpload = async (params) => {
  uploadLoading.value = true;
  try {
    const formData1 = new FormData();
    formData1.append("file", params.file);
    formData1.append("userId", formData.id);
    const res = await uploadImage(formData1);
    if (res.code) {
      pendingAvatarUrl.value = res.data;
      ElMessage.success("头像上传成功，请点击下方保存按钮完成更换");
      params.onSuccess();
    } else {
      ElMessage.error(res.msg || "头像上传失败");
      params.onError();
    }
  } catch (error) {
    console.error("头像上传失败:", error);
    ElMessage.error("网络错误，头像上传失败");
    params.onError();
  } finally {
    uploadLoading.value = false;
  }
};

// 保存头像到数据库
const saveAvatarToDb = async () => {
  if (!pendingAvatarUrl.value) return;
  formData.avatarUrl = pendingAvatarUrl.value;
  await mypudate(formData);
  pendingAvatarUrl.value = "";
};

const handleEditSave = async () => {
  const success = await mypudate(editForm);
  if (success) {
    editDialogVisible.value = false;
  }
};

const quickActions = [
  {
    name: "我的竞赛",
    icon: "Trophy",
    color: "#2979ff",
    route: "/personal/competition",
  },
  {
    name: "报名竞赛",
    icon: "EditPen",
    color: "#00b386",
    route: "/competitionlist?Type=1",
  },
  {
    name: "组建团队",
    icon: "User",
    color: "#2979ff",
    route: "/personal/group",
  },
  {
    name: "团队详情",
    icon: "UserFilled",
    color: "#1cc8ee",
    route: "/personal/group",
  },
  {
    name: "评审作品",
    icon: "DocumentChecked",
    color: "#ff9800",
    route: "/personal/judge",
  },
  {
    name: "我的消息",
    icon: "Message",
    color: "#2979ff",
    route: "/personal/message",
  },
  {
    name: "编辑资料",
    icon: "Edit",
    color: "#409EFF",
    route: "__edit_profile__",
  },
  {
    name: "账号设置",
    icon: "Setting",
    color: "#00b386",
    route: "/personal/setting",
  },
];

const handleQuickAction = (route) => {
  if (route === "__edit_profile__") {
    // 打开编辑弹窗前同步数据
    Object.assign(editForm, {
      id: formData.id,
      username: formData.username,
      email: formData.email,
      phone: formData.phone,
      gender: formData.gender,
      birthday: formData.birthday,
    });
    editDialogVisible.value = true;
    return;
  }
  router.push(route);
};

onMounted(async () => {
  const result = await getcount();
  if (result.code) {
    count.value = result.data;
  }
  search();
});
</script>

<style scoped>
.page-wrapper {
  padding: 0;
  background: transparent;
  width: 100%;
  max-width: none;
  margin: 0;
  padding-top: 0;
}
.profile-header-row {
  margin-bottom: 8px;
  margin-top: 12px;
}
.profile-header-card {
  background: transparent;
  box-shadow: none;
  border-radius: 0;
  border: none;
  padding: 0;
}
.profile-header-flex {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
  padding: 24px 16px 24px 16px;
  min-height: 140px;
  gap: 0;
}
.profile-header-left {
  display: flex;
  align-items: center;
  min-width: 220px;
  max-width: 260px;
  flex: 0 0 260px;
  margin-left: 32px;
}
.avatar-uploader {
  border: none;
  width: 96px;
  height: 96px;
  border-radius: 12px;
  margin-bottom: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}
.avatar-uploader-icon {
  font-size: 48px;
  color: #8c8c8c;
}
.avatar {
  border-radius: 12px;
  object-fit: cover;
  width: 96px;
  height: 96px;
}
.profile-header-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.profile-header-main {
  font-size: 28px;
  font-weight: 700;
  color: #222;
  margin-bottom: 8px;
}
.profile-header-join {
  color: #aaa;
  font-size: 16px;
}
.profile-header-divider {
  width: 1px;
  height: 80px;
  background: #e5e6eb;
  margin: 0 12px;
}
.profile-header-meta-flex {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0;
  height: 100%;
  justify-content: flex-start;
}
.profile-header-meta-row {
  display: flex;
  flex: 1;
  align-items: center;
  justify-content: flex-start;
  min-width: 0;
  gap: 60px;
  margin-left: 32px;
}
.meta-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 100px;
  max-width: 180px;
  word-break: break-all;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.meta-label {
  color: #222;
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 2px;
}
.meta-desc {
  color: #b0b3bb;
  font-size: 15px;
}
.extra-content-row {
  display: flex;
  gap: 32px;
  margin-top: 0;
}
.extra-content-card {
  flex: 1;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 24px 32px 24px 32px;
  margin-bottom: 0;
}
.stats-row {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  gap: 0;
  justify-content: space-between;
}
.stat-item {
  background: #f7f8fa;
  border-radius: 12px;
  padding: 18px 0;
  text-align: center;
  width: 100%;
  font-size: 16px;
  box-shadow: none;
  margin: 0;
  min-width: 0;
  max-width: 100%;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #165dff;
  margin-bottom: 5px;
  display: block;
}
.stat-label {
  color: #666;
  font-size: 14px;
}
.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 18px;
  color: #222;
}
.quick-actions-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 24px 32px 24px 32px;
  margin-bottom: 0;
}
.quick-action-item {
  display: flex;
  align-items: center;
  padding: 18px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
}
.quick-action-item:hover {
  background-color: #f5f7fa;
}
.meta-group.email {
  max-width: none;
  white-space: normal;
  overflow: visible;
  text-overflow: initial;
  word-break: break-all;
}
.gender-icon {
  font-size: 18px;
  color: #409eff;
  margin-left: 8px;
  vertical-align: middle;
}
.gender-icon.female {
  color: #ff69b4;
}
.avatar-upload-vertical {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 40px;
}
.avatar-save-btn-wrap {
  margin-top: 8px;
  text-align: center;
  width: 100%;
  display: flex;
  justify-content: center;
}

/* 移动端适配 */
@media (max-width: 767px) {
  .page-wrapper {
    padding: 0 8px;
  }

  .profile-header-row {
    margin-top: 8px;
    margin-bottom: 12px;
  }

  .profile-header-flex {
    flex-direction: column;
    align-items: center;
    gap: 20px;
    padding: 20px 16px;
    min-height: auto;
    border-radius: 16px;
  }

  .profile-header-left {
    flex-direction: column;
    align-items: center;
    min-width: auto;
    max-width: 100%;
    flex: unset;
    margin-left: 0;
    margin-bottom: 0;
  }

  .avatar-upload-vertical {
    margin-right: 0;
    margin-bottom: 16px;
  }

  .avatar-uploader {
    width: 80px;
    height: 80px;
  }

  .avatar {
    width: 80px;
    height: 80px;
  }

  .profile-header-info {
    text-align: center;
  }

  .profile-header-main {
    font-size: 24px;
    margin-bottom: 6px;
  }

  .profile-header-join {
    font-size: 14px;
  }

  .profile-header-divider {
    display: none;
  }

  .profile-header-meta-flex {
    width: 100%;
  }

  .profile-header-meta-row {
    flex-direction: column;
    align-items: center;
    gap: 16px;
    margin-left: 0;
    width: 100%;
  }

  .meta-group {
    min-width: auto;
    max-width: 100%;
    white-space: normal;
    text-align: center;
  }

  .meta-label {
    font-size: 18px;
  }

  .meta-desc {
    font-size: 14px;
  }

  .extra-content-row {
    flex-direction: column;
    gap: 12px;
    margin-top: 0;
  }

  .extra-content-card {
    padding: 20px 16px;
  }

  .stats-row {
    gap: 8px;
  }

  .stat-item {
    padding: 16px 0;
    margin-bottom: 8px;
  }

  .stat-value {
    font-size: 20px;
  }

  .stat-label {
    font-size: 13px;
  }

  .quick-actions-card {
    padding: 20px 16px;
  }

  .quick-action-item {
    padding: 16px 12px;
  }

  .section-title {
    font-size: 16px;
    margin-bottom: 16px;
  }
  .extra-content-row {
    flex-direction: column;
    gap: 8px;
  }
  .extra-content-card,
  .quick-actions-card {
    padding: 12px 8px;
    border-radius: 8px;
    margin-bottom: 8px;
  }
  .stats-row {
    gap: 0;
    flex-direction: column;
  }
  .stat-item {
    font-size: 13px;
    padding: 10px 0;
  }
  .stat-value {
    font-size: 18px;
  }
  .section-title {
    font-size: 15px;
    margin-bottom: 10px;
  }
  .quick-action-item {
    padding: 10px 8px;
    font-size: 13px;
  }
}

@media (max-width: 1200px) {
  .page-wrapper {
    width: 100%;
    max-width: none;
    padding: 0;
  }
  .profile-header-flex {
    flex-direction: column;
    align-items: flex-start;
    gap: 24px;
    padding: 16px 4px;
    min-height: unset;
  }
  .profile-header-left {
    max-width: 100%;
    min-width: 0;
    flex: unset;
    margin-bottom: 12px;
  }
  .profile-header-divider {
    display: none;
  }
  .profile-header-meta-flex {
    flex-direction: column;
    align-items: flex-start;
    width: 100%;
  }
  .profile-header-meta-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
    width: 100%;
  }
  .meta-group {
    min-width: 90px;
    max-width: 100%;
    white-space: normal;
  }
}
@media (max-width: 900px) {
  .stats-row {
    gap: 12px 0;
  }
  .stat-item {
    margin: 0 0 12px 0;
    min-width: 0;
    max-width: 100%;
  }
}
</style>