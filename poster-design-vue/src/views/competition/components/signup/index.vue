<template>
  <header-vue :isRightShow="true" />
  <div ref="bodyContainer" class="body">
    <div class="signup-body">
      <!-- 页面头部区域 -->
      <div class="hero-section">
        <div class="hero-content">
          <div class="hero-text">
            <h1 class="hero-title">竞赛报名</h1>
            <p class="hero-subtitle">{{ competitionData.competitionName }}</p>
            <div class="hero-info">
              <span class="info-badge">
                <el-icon><User /></el-icon>
                竞赛分类：{{ competitionData.categoryName }}
              </span>
              <span class="info-badge">
                <el-icon><UserFilled /></el-icon>
                人数限制：{{ competitionData.people }}
              </span>
            </div>
          </div>
          <div class="hero-visual">
            <div class="floating-elements">
              <div class="floating-circle circle-1"></div>
              <div class="floating-circle circle-2"></div>
              <div class="floating-circle circle-3"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="signup-container">
        <el-form :model="formData" label-width="100px" class="signup-form">
          <!-- 团队选择 -->
          <div class="form-section">
            <div class="section-header">
              <el-icon><UserFilled /></el-icon>
              <span class="section-title">选择团队</span>
            </div>
            <el-form-item label="选择团队" class="team-select-item">
              <el-select
                v-model="formData.teamId"
                placeholder="请选择团队"
                style="width: 100%"
                @change="onTeamChange"
                class="team-select"
              >
                <el-option
                  v-for="team in teamList"
                  :key="team.id"
                  :label="team.teamName"
                  :value="team.id"
                />
              </el-select>
            </el-form-item>
          </div>

          <!-- 团队成员展示 -->
          <div class="form-section" v-if="selectedTeamMembers.length">
            <div class="section-header">
              <el-icon><Avatar /></el-icon>
              <span class="section-title">团队成员</span>
            </div>
            <div class="member-info">
              <div class="teacher-info">
                <el-icon><User /></el-icon>
                <span>指导老师：{{ teamInfo.teacher }}</span>
              </div>
              <div class="member-list">
                <div
                  v-for="(member, idx) in selectedTeamMembers"
                  :key="idx"
                  class="member-card"
                >
                  <div class="member-avatar">
                    <img v-lazy :src="member.url" alt="" />
                  </div>
                  <div class="member-details">
                    <p class="member-name">{{ member.username }}</p>
                    <p class="member-role">
                      {{ member.status ? "队员" : "队长" }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 作品展示 -->
          <div class="form-section" v-if="workList.length">
            <div class="section-header">
              <el-icon><Picture /></el-icon>
              <span class="section-title">已添加作品</span>
            </div>
            <div class="work-list">
              <div
                v-for="(work, index) in workList"
                :key="index"
                class="work-card"
              >
                <div class="work-header">
                  <span class="work-number">作品 {{ index + 1 }}</span>
                  <el-button
                    type="danger"
                    size="small"
                    text
                    @click="removeWork(index)"
                    class="remove-btn"
                  >
                    <el-icon><Delete /></el-icon>
                    删除
                  </el-button>
                </div>
                <div class="work-content">
                  <div class="work-image">
                    <img v-lazy :src="work.picture" alt="" />
                  </div>
                  <div class="work-details">
                    <p class="work-name">{{ work.name }}</p>
                    <p class="work-intro">{{ work.introduction }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 操作按钮区域 -->
          <div class="action-section">
            <div class="action-buttons">
              <el-button
                type="primary"
                :disabled="!peoplePass"
                @click="showAddWork = true"
                class="action-btn add-work-btn"
              >
                <el-icon><Plus /></el-icon>
                添加作品
              </el-button>

              <el-button
                :disabled="!peoplePass"
                @click="showNotice = true"
                class="action-btn notice-btn"
              >
                <el-icon><Document /></el-icon>
                查看报名须知
              </el-button>

              <el-button
                type="success"
                :disabled="signupRule"
                @click="onClickSignup"
                class="action-btn signup-btn"
              >
                <el-icon><Check /></el-icon>
                立即报名
              </el-button>
            </div>
          </div>
        </el-form>
      </div>

      <!-- 添加作品弹窗 -->
      <el-dialog
        v-model="showAddWork"
        title="添加作品"
        :width="isMobile ? '90%' : '500px'"
        class="work-dialog"
        :close-on-click-modal="false"
        destroy-on-close
      >
        <el-form :model="tempWork" class="work-form">
          <el-form-item label="作品名">
            <el-input 
              v-model="tempWork.name" 
              placeholder="请输入作品名称"
              @input="(value) => tempWork.name = value"
            />
          </el-form-item>
          <el-form-item label="简介">
            <el-input
              type="textarea"
              v-model="tempWork.introduction"
              placeholder="请输入作品简介"
              :rows="isMobile ? 3 : 4"
              @input="(value) => tempWork.introduction = value"
            />
          </el-form-item>
          <el-form-item label="图片">
            <el-upload
              :auto-upload="false"
              :on-change="handleImageChange"
              :limit="1"
              :file-list="tempWork.fileList"
              list-type="picture"
              class="work-upload"
            >
              <el-button type="primary" plain>
                <el-icon><Upload /></el-icon>
                选择图片
              </el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button
              @click="onclickCancel"
              :disabled="isPictureLoad"
              class="cancel-btn"
              >取消</el-button
            >
            <el-button type="primary" @click="addWork" class="confirm-btn"
              >确认添加</el-button
            >
          </div>
        </template>
      </el-dialog>

      <!-- 报名须知弹窗 -->
      <el-dialog
        v-model="showNotice"
        title="报名须知"
        :width="isMobile ? '95%' : '600px'"
        class="notice-dialog"
        :close-on-click-modal="false"
        destroy-on-close
      >
        <div ref="noticeContent" class="notice-content" @scroll="handleScroll">
          <div class="notice-text">
            <p>尊敬的参赛者：</p>
            <p>
              欢迎报名参加全国大学生海报设计大赛！为确保大赛公平、公正、顺利进行，并保障您的权益，请在报名前务必仔细阅读并完全理解以下《报名须知》（以下简称“须知”）。点击“我已知晓”即视为您已阅读、理解并自愿接受本须知的全部内容及约束。如您不同意本须知的任何条款，请勿报名参赛。
            </p>
            <p><b>一、 参赛资格</b></p>
            <p>
              <b>身份要求：</b
              >参赛者必须是中华人民共和国教育部认可的普通高等学校（含高职高专院校）全日制在校学生（包括本科生、专科生、硕士研究生、博士研究生）。报名时需提供真实有效的学生证信息及身份证明（如身份证）。
            </p>
            <p>
              <b>个人/团队参赛：</b
              >允许以个人或团队形式参赛。团队参赛需指定一名主创人员（队长）
              作为联系人及作品权益代表人，负责团队联络、作品提交、奖金分配等事宜。团队成员信息须在报名时完整填写。
            </p>
            <p>
              <b>指导老师：</b
              >参赛者可选择填写指导老师信息（限1名），指导老师应为所在高校在职教师。
            </p>
            <p><b>二、 作品要求</b></p>
            <p>
              <b>主题契合：</b>参赛作品必须严格围绕本届大赛公布的主题进行创作。
            </p>
            <p>
              <b>原创性：</b
              >作品必须是参赛者独立原创或团队共同原创。严禁抄袭、剽窃他人作品，不得侵犯任何第三方的知识产权（包括但不限于著作权、商标权、肖像权、名誉权、隐私权等）。不得使用人工智能生成内容（AIGC）作为主要创作手段或核心创意来源。
            </p>
            <p>
              <b>内容要求：</b
              >作品内容需积极健康，符合国家法律法规和社会主义核心价值观，不得含有反动、暴力、色情、种族歧视等不良信息。作品中如使用他人肖像、作品元素（如字体、图片、音乐片段等），参赛者必须确保已获得合法有效的授权，并承担由此产生的全部责任。大赛组委会不承担因参赛作品侵权而产生的任何法律责任。
            </p>
            <p>
              <b>作品说明：</b
              >报名时需提交作品创意说明，阐述设计理念、构思过程、表现手法等。
            </p>
            <p><b>三、 报名流程与提交</b></p>
            <p>
              <b>报名方式：</b
              >所有参赛者必须通过大赛官方指定的在线报名系统完成报名及作品上传。不接受邮件、邮寄等其他方式。
            </p>
            <p>
              <b>信息填写：</b
              >请如实、准确、完整地填写所有报名信息（包括个人/团队信息、联系方式、学校、作品信息等）。所有信息一经提交，原则上不可修改。如因特殊原因确需修改（非作品核心内容），请在截止日期前联系大赛组委会邮箱申请，组委会保留是否接受修改的最终决定权。
            </p>
            <p>
              <b>作品提交：</b
              >请严格按照系统要求上传作品文件。请在上传后仔细检查文件是否完整、清晰、符合格式要求。提交后无法替换（除非系统故障且在截止前成功联系组委会并获准）。提交即视为作品最终稿。
            </p>
            <p><b>四、 评审与奖项</b></p>
            <p>
              <b>评审标准：</b
              >评审将主要依据主题契合度、创意构思、艺术表现力、技术水准、传播价值等方面进行。
            </p>
            <p><b>评审流程：</b>大赛评审由组委会邀请的专家评委进行匿名评审。</p>
            <p>
              <b>奖项设置：</b
              >具体奖项等级、名额及奖金/奖品设置详见大赛官方发布的《奖项设置公告》。
            </p>
            <p>
              <b>结果公布：</b
              >评审结果将在评审结束后于大赛官方网站公布，并通过报名系统通知获奖者。请密切关注官方信息。
            </p>
            <p>
              <b>奖金与证书：</b
              >获奖者将获得大赛组委会颁发的获奖证书及相应奖金/奖品（具体发放方式另行通知）。奖金涉及个人所得税的，由获奖者自行承担。
            </p>
            <p><b>五、 知识产权与作品使用</b></p>
            <p>
              <b>著作权归属：</b>参赛作品的著作权归原作者（个人或团队）所有。
            </p>
            <p>
              <b>授权许可：</b
              >参赛者一经报名参赛，即视为无偿授权大赛组委会及其关联方（包括主办方、承办方、协办方、支持单位等）在全球范围内享有以下权利（包括但不限于）：在评审期间使用作品。在宣传、推广、展览、出版、印刷、媒体报道、网络传播等与大赛相关的活动中使用作品（包括但不限于展示、复制、发行、信息网络传播、汇编、演绎等）。在非商业性公益活动中使用作品。使用期限：通常为自提交之日起不少于20年。使用方式：包括但不限于线上线下展示、制作宣传册、年鉴、出版物、媒体报道、数字平台展示等。
            </p>
            <p>
              <b>署名权：</b
              >组委会在使用参赛作品时，将尽可能标明作者姓名（或团队名称）及学校信息。
            </p>
            <p>
              <b>第三方使用：</b
              >如组委会拟将作品用于前述授权范围之外的商业用途，将另行与作者协商并签订书面协议。
            </p>
            <p><b>六、 重要声明与违规处理</b></p>
            <p>
              <b>真实性承诺：</b
              >参赛者承诺所提交的所有信息（包括身份、学籍、作品信息、原创性声明等）均真实、准确、合法、有效。如有虚假，一经查实，组委会有权立即取消其参赛资格、获奖资格，并追回已发放的奖金、奖品、证书。造成损失的，参赛者须承担相应法律责任。
            </p>
            <p>
              <b>原创性保证：</b
              >参赛者保证作品为原创，不侵犯任何第三方合法权益。如作品涉及抄袭、剽窃、侵权（包括但不限于侵犯著作权、商标权、肖像权、名誉权、隐私权等）或违反公序良俗，一经发现或第三方投诉属实：组委会有权立即取消其参赛及获奖资格，追回奖金奖品证书。由此产生的全部法律责任由参赛者自行承担，与大赛组委会无关。若因此给大赛组委会造成损失（包括但不限于赔偿金、律师费、诉讼费等），参赛者须全额赔偿。
            </p>
            <p>
              <b>AI使用限制：</b
              >严禁使用AIGC工具直接生成核心创意或主体画面。允许适度使用AI作为辅助工具（如生成部分素材、优化效果等），但必须在作品说明中清晰标注具体使用情况（如使用了XX工具的XX功能）。未标注或过度依赖AI生成的作品，组委会有权取消资格。
            </p>
            <p>
              <b>公平竞赛：</b
              >禁止任何形式的刷票、贿选、干扰评委等不正当竞争行为，违者将被取消资格。
            </p>
            <p>
              <b>不可抗力：</b
              >如遇不可抗力因素（如自然灾害、政策调整、网络攻击等）导致大赛延期、调整规则或终止，组委会将尽力通知，但不承担由此产生的责任。
            </p>
            <p>
              <b>规则解释权：</b
              >大赛组委会拥有对本《报名须知》及大赛所有相关规则的最终解释权。组委会有权根据实际情况对规则进行合理调整（如时间、流程等），调整内容将及时通过官方渠道公布。
            </p>
            <p><b>再次提醒：</b></p>
            <p>
              请您务必认真、完整阅读以上所有条款！点击“我已知晓”按钮，即表示您已充分理解并完全接受本《报名须知》的全部内容，承诺遵守大赛规则，并自愿承担相应的责任和义务。预祝您在大赛中取得优异成绩！
            </p>
            <p style="text-align: right; margin-top: 1em">速通分队组委会</p>
          </div>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button
              type="primary"
              :disabled="!canAcknowledge"
              @click="acknowledge"
              class="acknowledge-btn"
            >
              <el-icon><Check /></el-icon>
              我已知晓
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>

    <aside-vue :container="bodyContainer" v-if="isAsideShow" />
    <footer-vue />
  </div>
</template>

<script setup>
import headerVue from "@/libs/header/index.vue";
import footerVue from "@/libs/footer/index.vue";
import asideVue from "@/views/main/componets/aside/index.vue";
import { ref, onMounted, watch, computed } from "vue";
import { isMobile } from "@/utils/flexible.js";
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import {
  getCompetitionById,
  getCaptainEnlist,
} from "../../../../apis/competition";
import { getUserTeam, getTeamMember, setTeamWork } from "../../../../apis/team";
import { uploadImage } from "@/apis/person.js";
import {
  User,
  UserFilled,
  Avatar,
  Picture,
  Delete,
  Plus,
  Document,
  Check,
  Upload,
} from "@element-plus/icons-vue";

const bodyContainer = ref(null);
const isAsideShow = ref(true);
const route = useRoute();
const router = useRouter();
const competitionData = ref("");
let teamId = 0;
const works = ref([]);
const isEnlist = ref(false);
const isPictureLoad = ref(false);

// 获取竞赛数据
const getCompetition = async () => {
  try {
    const res = await getCompetitionById(route.params.id);
    if (res.code === 1 && res.data) {
      competitionData.value = res.data;
    } else {
      ElMessage.error(res.msg || "获取竞赛信息失败");
    }
  } catch (error) {
    console.error("接口错误：", error);
    ElMessage.error("请求失败，请稍后再试");
  }
};

const getEnlist = async () => {
  try {
    const res = await getCaptainEnlist(route.params.id);
    if (res.code === 1 && res.data) {
      isEnlist.value = res.data;
      if (isEnlist.value) router.push(`/competition/${route.params.id}`);
    }
  } catch (error) {
    console.error("接口错误：", error);
    ElMessage.error("请求失败，请稍后再试");
  }
};

onMounted(() => {
  getCompetition();
  getTeam();
  getEnlist();
});

// 表单数据
const formData = ref({ teamId: null });
const teamList = ref({});
const selectedTeamMembers = ref([]);
const teamInfo = ref({});
const peoplePass = ref(true);

const getTeam = async () => {
  const res = await getUserTeam();
  teamList.value = res.data;
};

const getMember = async (id) => {
  const res = await getTeamMember(id);
  teamId = id;
  teamInfo.value = res.data;
  selectedTeamMembers.value = res.data.teamUserList;
  if (res.data.teamUserList.length > competitionData.value.people) {
    peoplePass.value = false;
  } else {
    peoplePass.value = true;
  }
};

const onTeamChange = (id) => {
  getMember(id);
};

let warningMessageInstance = null;
watch(
  () => peoplePass.value,
  (newVal) => {
    if (!newVal) {
      if (!warningMessageInstance) {
        warningMessageInstance = ElMessage({
          message: "团队人数超出规则限制",
          duration: 0,
          showClose: true,
          type: "warning",
        });
      }
    } else {
      if (warningMessageInstance) {
        warningMessageInstance.close();
        warningMessageInstance = null;
      }
    }
  }
);

// 作品列表与上传逻辑
const workList = ref([]);
const showAddWork = ref(false);
const tempWork = ref({
  name: "",
  introduction: "",
  picture: "",
  file: null,
  fileList: [],
});

const handleImageChange = async (file) => {
  console.log('选择的文件:', file);
  isPictureLoad.value = true;
  const formData1 = new FormData();
  formData1.append("file", file.raw);
  
  try {
    const res = await uploadImage(formData1);
    console.log('上传响应:', res);
    
    if (res.code) {
      // 修复：使用 res.msg 作为图片URL，因为 res.data 是 null
      tempWork.value.picture = res.msg;
      tempWork.value.fileList = [file];
      console.log('图片上传成功，当前tempWork:', tempWork.value);
      ElMessage.success("作品图片上传成功");
      isPictureLoad.value = false;
    } else {
      ElMessage.error(res.msg || "图片上传失败");
      isPictureLoad.value = false;
    }
  } catch (error) {
    console.error("上传失败：", error);
    ElMessage.error("网络错误，图片上传失败");
    isPictureLoad.value = false;
  }
};

const addWork = () => {
  // 添加调试信息
  console.log('当前作品数据:', tempWork.value);
  
  // 更严格的验证逻辑
  const name = tempWork.value.name?.trim();
  const introduction = tempWork.value.introduction?.trim();
  const picture = tempWork.value.picture;
  
  if (!name) {
    ElMessage.warning("请输入作品名称");
    return;
  }
  
  if (!introduction) {
    ElMessage.warning("请输入作品简介");
    return;
  }
  
  if (!picture) {
    ElMessage.warning("请上传作品图片");
    return;
  }
  
  // 创建新的作品对象
  const newWork = {
    name: name,
    introduction: introduction,
    picture: picture,
    file: tempWork.value.file,
    fileList: tempWork.value.fileList
  };
  
  // 验证通过，添加作品 - 使用响应式更新
  workList.value = [...workList.value, newWork];
  works.value = [...works.value, newWork];
  
  console.log('作品添加成功，当前works数组:', works.value);
  console.log('作品添加成功，当前workList数组:', workList.value);
  
  isPictureLoad.value = false;
  showAddWork.value = false;
  
  // 重置表单
  tempWork.value = {
    name: "",
    introduction: "",
    picture: "",
    file: null,
    fileList: [],
  };
  
  ElMessage.success("作品添加成功");
};

// 报名须知
const showNotice = ref(false);
const hasAcknowledged = ref(false);
const canAcknowledge = ref(false);
const noticeContent = ref(null);

const handleScroll = () => {
  const el = noticeContent.value;
  if (el.scrollTop + el.clientHeight >= el.scrollHeight - 10) {
    canAcknowledge.value = true;
  }
};
const acknowledge = () => {
  hasAcknowledged.value = true;
  showNotice.value = false;
  ElMessage.success("已确认报名须知");
};

watch(
  () => isMobile.value,
  () => {
    isAsideShow.value = !isMobile.value;
  },
  { immediate: true }
);

const removeWork = (index) => {
  // 使用响应式更新删除作品
  workList.value = workList.value.filter((_, i) => i !== index);
  works.value = works.value.filter((_, i) => i !== index);
  
  console.log('作品删除成功，当前works数组:', works.value);
  console.log('作品删除成功，当前workList数组:', workList.value);
  
  ElMessage.success("作品已删除");
};

const setCallback = async (data) => {
  try {
    const res = await setTeamWork(data);
    if (res.code == 1) {
      ElMessage.success("报名成功");
      router.push(`/competition/${route.params.id}`);
    } else {
      ElMessage.error(res.msg || "发送报名信息失败");
    }
  } catch (error) {
    console.error("接口错误：", error);
    ElMessage.error("请求失败，请稍后再试");
  }
};

const signupRule = computed(() => {
  const rule = !hasAcknowledged.value || works.value.length === 0;
  console.log('signupRule计算:', {
    hasAcknowledged: hasAcknowledged.value,
    worksLength: works.value.length,
    works: works.value,
    result: rule
  });
  return rule;
});

const onclickCancel = () => {
  console.log('取消操作，重置表单');
  tempWork.value = {
    name: "",
    introduction: "",
    picture: "",
    file: null,
    fileList: [],
  };
  showAddWork.value = false;
  isPictureLoad.value = false;
};

const onClickSignup = () => {
  console.log('点击报名按钮，当前数据:', {
    competitionId: route.params.id,
    teamId: teamId,
    works: works.value,
    worksLength: works.value.length,
    hasAcknowledged: hasAcknowledged.value
  });
  
  const competitionId = route.params.id;
  const setData = { competitionId, teamId, works: works.value };
  setCallback(setData);
};
</script>

<style scoped>
/* 整体布局 */
.body {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5fa 100%);
}

html.dark .body {
  background: linear-gradient(135deg, #333 0%, #333 50%, #333 100%);
}

.signup-body {
  min-height: 90vh;
  padding: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}

/* 英雄区域 */
.hero-section {
  background: linear-gradient(135deg, #1b78f7 0%, #409eff 50%, #66b1ff 100%);
  padding: 3rem 2rem;
  position: relative;
  overflow: hidden;
  width: 100%;
  max-width: 1100px;
  margin: 0 auto 2rem auto;
  border-radius: 0.75rem;
  box-sizing: border-box;
}

html.dark .hero-section {
  background: linear-gradient(135deg, darkblue 0%, #074eb2 50%, #1066bc 100%);
}

.hero-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1000px;
  margin: 0 auto;
  position: relative;
  z-index: 2;
  gap: 3rem;
}

.hero-text {
  flex: 1;
  color: white;
}

.hero-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0 0 1rem 0;
  line-height: 1.3;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  letter-spacing: -0.5px;
}

.hero-subtitle {
  font-size: 1.25rem;
  margin: 0 0 1.5rem 0;
  opacity: 0.9;
  font-weight: 500;
  letter-spacing: 0.5px;
}

.hero-info {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.info-badge {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 2rem;
  font-size: 0.875rem;
  font-weight: 500;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.hero-visual {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.floating-elements {
  position: relative;
  width: 200px;
  height: 200px;
}

.floating-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 80px;
  height: 80px;
  top: 10px;
  left: 10px;
  animation-delay: 0s;
}

.circle-2 {
  width: 60px;
  height: 60px;
  top: 80px;
  right: 20px;
  animation-delay: 2s;
}

.circle-3 {
  width: 40px;
  height: 40px;
  bottom: 20px;
  left: 40px;
  animation-delay: 4s;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

/* 主容器 */
.signup-container {
  width: 100%;
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 2rem 3rem 2rem;
}

.signup-form {
  background: #fff;
  border-radius: 0.75rem;
  box-shadow: 0 8px 32px rgba(27, 120, 247, 0.08);
  padding: 2.5rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

html.dark .signup-form {
  background-color: #444343;
  box-shadow: 0 8px 32px rgba(21, 60, 115, 0.08);
}

/* 表单区域 */
.form-section {
  background: #f8fafc;
  border-radius: 0.5rem;
  padding: 1.5rem;
  border-left: 4px solid #1b78f7;
}

html.dark .form-section {
  background-color: #5a5b5c;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.25rem;
  font-weight: 700;
  color: #1b78f7;
  margin-bottom: 1rem;
}

.section-header .el-icon {
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.section-title {
  font-size: 1.25rem;
  color: #1a202c;
  font-weight: 700;
  letter-spacing: 0.5px;
}

html.dark .section-title {
  color: white;
}

/* 团队选择 */
.team-select-item {
  margin-bottom: 0;
}

.team-select {
  border-radius: 0.5rem;
}

.team-select :deep(.el-input__wrapper) {
  border-radius: 0.5rem;
  box-shadow: 0 2px 8px rgba(27, 120, 247, 0.06);
}

/* 成员信息 */
.member-info {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.teacher-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  background: rgba(27, 120, 247, 0.05);
  border-radius: 0.5rem;
  color: #1b78f7;
  font-weight: 500;
}

html.dark .teacher-info {
  color: #2c82fa;
}

.member-list {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.member-card {
  background: #fff;
  border-radius: 0.5rem;
  padding: 1rem;
  box-shadow: 0 2px 12px rgba(27, 120, 247, 0.06);
  display: flex;
  align-items: center;
  gap: 0.75rem;
  min-width: 200px;
  transition: transform 0.2s, box-shadow 0.2s;
}

html.dark .member-card {
  background-color: #676a6b;
}

.member-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(27, 120, 247, 0.1);
}

.member-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.member-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.member-details p {
  margin: 0;
  font-size: 0.875rem;
}

.member-name {
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 0.25rem;
}

html.dark .member-name {
  color: white;
}

.member-role {
  color: #64748b;
  font-size: 0.75rem;
}

html.dark .member-role {
  color: #8f9fb5;
}

/* 作品列表 */
.work-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.work-card {
  background: #fff;
  border-radius: 0.5rem;
  padding: 1rem;
  box-shadow: 0 2px 12px rgba(27, 120, 247, 0.06);
  transition: transform 0.2s, box-shadow 0.2s;
}

html.dark .work-card {
  background-color: #676a6b;
}

.work-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(27, 120, 247, 0.1);
}

.work-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid #e2e8f0;
}

.work-number {
  font-weight: 600;
  color: #1b78f7;
  font-size: 1rem;
}

html.dark .work-number {
  color: #468ef3;
}

.remove-btn {
  color: #ef4444;
  border: none;
  background: none;
  padding: 0.5rem;
  border-radius: 0.25rem;
  transition: background 0.2s;
}

.remove-btn:hover {
  background: rgba(239, 68, 68, 0.1);
}

.work-content {
  display: flex;
  gap: 1rem;
  align-items: flex-start;
}

.work-image {
  width: 100px;
  height: 100px;
  border-radius: 0.25rem;
  overflow: hidden;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(27, 120, 247, 0.08);
}

.work-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.work-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.work-name {
  font-weight: 600;
  color: #1a202c;
  margin: 0;
  font-size: 1rem;
}

html.dark .work-name {
  color: white;
}

.work-intro {
  color: #64748b;
  margin: 0;
  font-size: 0.875rem;
  line-height: 1.5;
}

html.dark .work-intro {
  color: #9ca4af;
}

/* 操作按钮区域 */
.action-section {
  background: #f8fafc;
  border-radius: 0.5rem;
  padding: 1.5rem;
  border-left: 4px solid #10b981;
}

html.dark .action-section {
  background-color: #5a5b5c;
}

.action-buttons {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  justify-content: center;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border-radius: 0.5rem;
  font-weight: 600;
  font-size: 1rem;
  transition: all 0.2s;
  min-width: 140px;
  justify-content: center;
}

.add-work-btn {
  background: linear-gradient(135deg, #1b78f7 0%, #409eff 100%);
  border: none;
  color: #fff;
  box-shadow: 0 2px 8px rgba(27, 120, 247, 0.2);
}

.add-work-btn:hover {
  background: linear-gradient(135deg, #409eff 0%, #1b78f7 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(27, 120, 247, 0.3);
}

.notice-btn {
  background: #fff;
  border: 2px solid #1b78f7;
  color: #1b78f7;
  box-shadow: 0 2px 8px rgba(27, 120, 247, 0.1);
}

.notice-btn:hover {
  background: #1b78f7;
  color: #fff;
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(27, 120, 247, 0.2);
}

.signup-btn {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
  color: #fff;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.2);
}

.signup-btn:hover {
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(16, 185, 129, 0.3);
}

.action-btn:disabled {
  background: #d1d5db !important;
  color: #fff !important;
  cursor: not-allowed;
  opacity: 0.7;
  transform: none !important;
  box-shadow: none !important;
}

/* 弹窗样式 */
.work-dialog,
.notice-dialog {
  border-radius: 0.75rem !important;
}

.work-dialog :deep(.el-dialog__header),
.notice-dialog :deep(.el-dialog__header) {
  font-size: 1.2rem;
  color: #1b78f7;
  font-weight: 700;
  border-bottom: 1px solid #e2e8f0;
  padding: 1.5rem 1.5rem 1rem 1.5rem;
}

.work-dialog :deep(.el-dialog__body),
.notice-dialog :deep(.el-dialog__body) {
  padding: 1.5rem;
}

.work-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.work-form :deep(.el-input__wrapper) {
  border-radius: 0.5rem;
  box-shadow: 0 2px 8px rgba(27, 120, 247, 0.06);
}

.work-form :deep(.el-textarea__inner) {
  border-radius: 0.5rem;
  border: 1px solid #d1d5db;
  resize: none;
}

.work-form :deep(.el-textarea__inner:focus) {
  border-color: #1b78f7;
  box-shadow: 0 0 0 3px rgba(27, 120, 247, 0.1);
}

.work-upload {
  width: 100%;
}

.work-upload :deep(.el-upload) {
  width: 100%;
}

.work-upload :deep(.el-upload-dragger) {
  border-radius: 0.5rem;
  border: 2px dashed #1b78f7;
  background: rgba(27, 120, 247, 0.05);
}

.work-upload :deep(.el-upload-list) {
  margin-top: 0.5rem;
}

.work-upload :deep(.el-upload-list__item) {
  border-radius: 0.5rem;
  border: 1px solid #e2e8f0;
}

.notice-content {
  height: 300px;
  overflow-y: auto;
  border: 1px solid #e2e8f0;
  padding: 1rem;
  border-radius: 0.5rem;
  background: #f8fafc;
}

html.dark .notice-content {
  background-color: #333;
}

.notice-text p {
  margin: 0.5rem 0;
  color: #64748b;
  line-height: 1.6;
}

html.dark .notice-text p {
  color: white;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #e2e8f0;
}

.cancel-btn {
  background: #e5e7eb;
  color: #374151;
  border: none;
  border-radius: 0.5rem;
  padding: 0.75rem 1.5rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  background: #d1d5db;
  transform: translateY(-1px);
}

.confirm-btn {
  background: linear-gradient(135deg, #1b78f7, #409eff);
  border: none;
  color: #fff;
  border-radius: 0.5rem;
  padding: 0.75rem 1.5rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.confirm-btn:hover {
  background: linear-gradient(135deg, #0f5bb3, #1b78f7);
  transform: translateY(-1px);
}

.acknowledge-btn {
  background: linear-gradient(135deg, #1b78f7 0%, #409eff 100%);
  border: none;
  color: #fff;
  border-radius: 0.5rem;
  padding: 0.75rem 1.5rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
}

.acknowledge-btn:hover {
  background: linear-gradient(135deg, #409eff 0%, #1b78f7 100%);
  transform: translateY(-1px);
}

.acknowledge-btn:disabled {
  background: #d1d5db !important;
  color: #fff !important;
  cursor: not-allowed;
  opacity: 0.7;
  transform: none !important;
}

/* 移动端适配 - 平板设备 */
@media (max-width: 1024px) {
  .hero-section {
    max-width: 100%;
    padding: 2.5rem 1.5rem;
    border-radius: 0.75rem;
  }

  .signup-container {
    max-width: 100%;
    padding: 0 1.5rem 2rem 1.5rem;
  }

  .signup-form {
    padding: 2rem;
  }

  .hero-title {
    font-size: 2.25rem;
  }

  .hero-subtitle {
    font-size: 1.125rem;
  }

  .floating-elements {
    width: 180px;
    height: 180px;
  }

  .circle-1 {
    width: 70px;
    height: 70px;
  }

  .circle-2 {
    width: 50px;
    height: 50px;
  }

  .circle-3 {
    width: 35px;
    height: 35px;
  }
}

/* 移动端适配 - 手机设备 */
@media (max-width: 768px) {
  .hero-section {
    max-width: 100%;
    padding: 2rem 1rem;
    border-radius: 0.5rem;
    margin: 0 auto 1.5rem auto;
  }

  .signup-container {
    max-width: 100%;
    padding: 0 1rem 1.5rem 1rem;
  }

  .hero-content {
    flex-direction: column;
    text-align: center;
    gap: 1.5rem;
  }

  .hero-title {
    font-size: 1.875rem;
    margin: 0 0 0.75rem 0;
  }

  .hero-subtitle {
    font-size: 1rem;
    margin: 0 0 1rem 0;
  }

  .hero-info {
    justify-content: center;
    gap: 0.75rem;
  }

  .info-badge {
    font-size: 0.8rem;
    padding: 0.4rem 0.8rem;
  }

  .floating-elements {
    width: 150px;
    height: 150px;
  }

  .circle-1 {
    width: 60px;
    height: 60px;
  }

  .circle-2 {
    width: 45px;
    height: 45px;
  }

  .circle-3 {
    width: 30px;
    height: 30px;
  }

  .signup-form {
    padding: 1.5rem;
    gap: 1.5rem;
  }

  .form-section {
    padding: 1.25rem;
  }

  .section-header {
    font-size: 1.125rem;
    gap: 0.5rem;
    margin-bottom: 0.75rem;
  }

  .section-header .el-icon {
    font-size: 1.35rem;
  }

  .section-title {
    font-size: 1.125rem;
  }

  .member-list {
    gap: 0.75rem;
  }

  .member-card {
    min-width: 160px;
    padding: 0.75rem;
    gap: 0.5rem;
  }

  .member-avatar {
    width: 45px;
    height: 45px;
  }

  .member-details p {
    font-size: 0.8rem;
  }

  .work-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 0.75rem;
  }

  .work-image {
    width: 80px;
    height: 80px;
  }

  .work-name {
    font-size: 0.9rem;
  }

  .work-intro {
    font-size: 0.8rem;
  }

  .action-buttons {
    flex-direction: column;
    align-items: stretch;
    gap: 0.75rem;
  }

  .action-btn {
    width: 100%;
    padding: 0.875rem 1.25rem;
    font-size: 0.95rem;
    min-width: auto;
  }

  /* 弹窗适配 */
  .work-dialog,
  .notice-dialog {
    margin: 1rem;
  }

  .work-dialog :deep(.el-dialog),
  .notice-dialog :deep(.el-dialog) {
    width: calc(100% - 2rem) !important;
    max-width: none;
    margin: 1rem auto !important;
  }

  .work-dialog :deep(.el-dialog__header),
  .notice-dialog :deep(.el-dialog__header) {
    padding: 1rem 1rem 0.75rem 1rem;
    font-size: 1.1rem;
  }

  .work-dialog :deep(.el-dialog__body),
  .notice-dialog :deep(.el-dialog__body) {
    padding: 1rem;
  }

  .work-form {
    gap: 0.75rem;
  }

  .work-form :deep(.el-form-item__label) {
    font-size: 0.9rem;
    padding-bottom: 0.25rem;
  }

  .work-form :deep(.el-input__wrapper) {
    box-shadow: 0 1px 4px rgba(27, 120, 247, 0.04);
  }

  .work-form :deep(.el-textarea__inner) {
    font-size: 0.9rem;
  }

  .work-upload :deep(.el-upload-dragger) {
    padding: 1rem;
  }

  .work-upload :deep(.el-upload-dragger .el-icon) {
    font-size: 1.5rem;
  }

  .notice-content {
    height: 250px;
    padding: 0.75rem;
  }

  .notice-text p {
    font-size: 0.9rem;
    line-height: 1.5;
  }

  .dialog-footer {
    padding: 0.75rem 1rem;
    gap: 0.75rem;
  }

  .cancel-btn,
  .confirm-btn,
  .acknowledge-btn {
    padding: 0.6rem 1rem;
    font-size: 0.9rem;
    min-height: 40px;
  }
}

/* 移动端适配 - 小屏手机 */
@media (max-width: 480px) {
  .hero-section {
    padding: 1.5rem 0.75rem;
    border-radius: 0.375rem;
  }

  .signup-container {
    padding: 0 0.75rem 1rem 0.75rem;
  }

  .hero-title {
    font-size: 1.5rem;
    margin: 0 0 0.5rem 0;
  }

  .hero-subtitle {
    font-size: 0.875rem;
    margin: 0 0 0.75rem 0;
  }

  .hero-info {
    flex-direction: column;
    align-items: center;
    gap: 0.5rem;
  }

  .info-badge {
    font-size: 0.75rem;
    padding: 0.35rem 0.7rem;
  }

  .floating-elements {
    width: 120px;
    height: 120px;
  }

  .circle-1 {
    width: 50px;
    height: 50px;
  }

  .circle-2 {
    width: 35px;
    height: 35px;
  }

  .circle-3 {
    width: 25px;
    height: 25px;
  }

  .signup-form {
    padding: 1rem;
    gap: 1rem;
  }

  .form-section {
    padding: 1rem;
  }

  .section-header {
    font-size: 1rem;
    gap: 0.4rem;
    margin-bottom: 0.5rem;
  }

  .section-header .el-icon {
    font-size: 1.2rem;
  }

  .section-title {
    font-size: 1rem;
  }

  .member-list {
    gap: 0.5rem;
  }

  .member-card {
    min-width: 140px;
    padding: 0.5rem;
    gap: 0.4rem;
  }

  .member-avatar {
    width: 40px;
    height: 40px;
  }

  .member-details p {
    font-size: 0.75rem;
  }

  .work-image {
    width: 70px;
    height: 70px;
  }

  .work-name {
    font-size: 0.85rem;
  }

  .work-intro {
    font-size: 0.75rem;
  }

  .action-btn {
    padding: 0.75rem 1rem;
    font-size: 0.9rem;
  }

  .work-dialog :deep(.el-dialog__body),
  .notice-dialog :deep(.el-dialog__body) {
    padding: 0.75rem;
  }

  .work-form {
    gap: 0.5rem;
  }

  .work-form :deep(.el-form-item__label) {
    font-size: 0.85rem;
    padding-bottom: 0.2rem;
  }

  .work-form :deep(.el-input__wrapper) {
    box-shadow: 0 1px 3px rgba(27, 120, 247, 0.03);
  }

  .work-form :deep(.el-textarea__inner) {
    font-size: 0.85rem;
  }

  .work-upload :deep(.el-upload-dragger) {
    padding: 0.75rem;
  }

  .work-upload :deep(.el-upload-dragger .el-icon) {
    font-size: 1.25rem;
  }

  .notice-content {
    height: 200px;
    padding: 0.5rem;
  }

  .notice-text p {
    font-size: 0.85rem;
    line-height: 1.4;
  }

  .dialog-footer {
    padding: 0.5rem 0.75rem;
    gap: 0.5rem;
  }

  .cancel-btn,
  .confirm-btn,
  .acknowledge-btn {
    padding: 0.5rem 0.75rem;
    font-size: 0.85rem;
    min-height: 36px;
  }
}

/* 超小屏设备适配 */
@media (max-width: 360px) {
  .hero-title {
    font-size: 1.3rem;
  }

  .hero-subtitle {
    font-size: 0.8rem;
  }

  .floating-elements {
    width: 100px;
    height: 100px;
  }

  .signup-form {
    padding: 0.75rem;
  }

  .form-section {
    padding: 0.75rem;
  }

  .section-header {
    font-size: 0.9rem;
  }

  .section-header .el-icon {
    font-size: 1.1rem;
  }

  .section-title {
    font-size: 0.9rem;
  }

  .member-card {
    min-width: 120px;
    padding: 0.4rem;
  }

  .member-avatar {
    width: 35px;
    height: 35px;
  }

  .action-btn {
    padding: 0.6rem 0.8rem;
    font-size: 0.85rem;
  }

  /* 弹窗超小屏优化 */
  .work-dialog :deep(.el-dialog),
  .notice-dialog :deep(.el-dialog) {
    width: calc(100% - 1rem) !important;
    margin: 0.5rem auto !important;
  }

  .work-dialog :deep(.el-dialog__header),
  .notice-dialog :deep(.el-dialog__header) {
    padding: 0.75rem 0.75rem 0.5rem 0.75rem;
    font-size: 1rem;
  }

  .work-dialog :deep(.el-dialog__body),
  .notice-dialog :deep(.el-dialog__body) {
    padding: 0.5rem;
  }

  .work-form {
    gap: 0.4rem;
  }

  .work-form :deep(.el-form-item__label) {
    font-size: 0.8rem;
    padding-bottom: 0.15rem;
  }

  .work-form :deep(.el-textarea__inner) {
    font-size: 0.8rem;
  }

  .work-upload :deep(.el-upload-dragger) {
    padding: 0.5rem;
  }

  .work-upload :deep(.el-upload-dragger .el-icon) {
    font-size: 1.1rem;
  }

  .notice-content {
    height: 180px;
    padding: 0.4rem;
  }

  .notice-text p {
    font-size: 0.8rem;
    line-height: 1.3;
  }

  .dialog-footer {
    padding: 0.4rem 0.5rem;
    gap: 0.4rem;
  }

  .cancel-btn,
  .confirm-btn,
  .acknowledge-btn {
    padding: 0.4rem 0.6rem;
    font-size: 0.8rem;
    min-height: 32px;
  }
}

/* 横屏模式适配 */
@media (max-height: 500px) and (orientation: landscape) {
  .hero-section {
    padding: 1rem 1rem;
  }

  .hero-content {
    gap: 1rem;
  }

  .hero-title {
    font-size: 1.5rem;
    margin: 0 0 0.5rem 0;
  }

  .hero-subtitle {
    font-size: 0.8rem;
    margin: 0 0 0.5rem 0;
  }

  .floating-elements {
    width: 100px;
    height: 100px;
  }

  .signup-container {
    padding: 0 1rem 1rem 1rem;
  }

  .signup-form {
    padding: 1rem;
    gap: 1rem;
  }
}

/* 触摸设备优化 */
@media (hover: none) and (pointer: coarse) {
  .action-btn:hover {
    transform: none;
  }

  .member-card:hover {
    transform: none;
  }

  /* 增加触摸目标大小 */
  .action-btn {
    min-height: 44px;
  }

  .member-card {
    min-height: 44px;
  }

  .work-header {
    min-height: 44px;
  }

  .remove-btn {
    min-height: 32px;
    min-width: 32px;
  }

  /* 弹窗触摸优化 */
  .work-dialog :deep(.el-dialog__headerbtn),
  .notice-dialog :deep(.el-dialog__headerbtn) {
    min-width: 44px;
    min-height: 44px;
  }

  .work-dialog :deep(.el-dialog__headerbtn .el-dialog__close),
  .notice-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
    font-size: 1.2rem;
  }

  .work-upload :deep(.el-upload-dragger) {
    min-height: 80px;
  }

  .work-upload :deep(.el-upload-dragger .el-icon) {
    font-size: 1.8rem;
  }

  .cancel-btn,
  .confirm-btn,
  .acknowledge-btn {
    min-height: 44px;
    min-width: 80px;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hero-section,
.form-section,
.action-section {
  animation: fadeInUp 0.8s ease-out;
}

.form-section:nth-child(1) {
  animation-delay: 0.1s;
}
.form-section:nth-child(2) {
  animation-delay: 0.2s;
}
.form-section:nth-child(3) {
  animation-delay: 0.3s;
}
.action-section {
  animation-delay: 0.4s;
}

/* 滚动条样式 */
.notice-content::-webkit-scrollbar {
  width: 6px;
}

.notice-content::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 3px;
}

.notice-content::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #1b78f7, #409eff);
  border-radius: 3px;
}

.notice-content::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #0f5bb3, #1b78f7);
}

/* 移动端滚动条优化 */
@media (max-width: 768px) {
  .notice-content::-webkit-scrollbar {
    width: 4px;
  }

  .notice-content::-webkit-scrollbar-thumb {
    background: #cbd5e1;
  }
}
</style>
