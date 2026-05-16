<template>
  <div class="competition-mgmt-page">
    <div class="competition-header">
      <div>
        <div class="competition-title">竞赛管理</div>
        <div class="competition-desc">管理所有进行中与已完成的竞赛，支持筛选、搜索、快速操作</div>
      </div>
      <div class="competition-actions">
        <button class="btn-blue" @click="openCreateModal">创建竞赛</button>
      </div>
    </div>
    <!-- 搜索栏 -->
    <div class="competition-filters">
      <select class="filter-select" v-model="searchtj.categoryId">
        <option value="">全部</option>
        <option v-for="item in Stype" :key="item.id" :value="item.id">{{ item.name }}</option> 
      </select> 
      <select class="filter-select" v-model="searchtj.competitionStatus">
        <option value="0">未开始</option>
        <option value="1">报名中</option>
        <option value="2">评审中</option>
        <option value="3">已完成</option>
      </select>
      <input class="filter-search" placeholder="搜索竞赛" v-model="searchtj.competitionName" />
      <button class="btn-blue btn-search" @click="search()">搜索</button>
    </div>
    <div class="competition-section">
      <!-- 报名中 -->
      <div class="bmz" v-if="State == 1">
        <div class="section-label ongoing-label">报名中竞赛</div>
        <div class="competition-list">
          <!-- 内容 -->
          <div class="competition-card card-signup" v-for="item in mainInfo" :key="item.id">
            <img v-lazy class="competition-img" :src="item.competitionImages[0]" />
            <div class="competition-info">
              <div class="competition-card-title">{{ item.competitionName }} <span class="tag-signup">报名中</span></div>
              <div class="competition-card-desc">{{ truncate(item.introduction, 75) }}</div>
              <div class="competition-card-meta">
                <span>{{ item.registrationBegin }} ~ {{ item.registrationEnd }}</span>
                <span>参赛人数：{{ item.competitionPeople }}</span>
                <span class="tag-org">主办：速通小分队</span>
              </div>
              <div class="competition-card-users">
                <img v-lazy class="user-avatar" :src="item.userAvatars[0]" />
                <img v-lazy class="user-avatar" :src="item.userAvatars[1]" />
                <span class="user-count">+{{ item.competitionPeople }}</span>
              </div>
              <!-- 按钮 -->
              <div class="competition-card-actions">
                <button class="btn-sm" @click="openDetailModal(item.id)">详情</button>
                <button class="btn-sm btn-edit" @click="openEditModal(item.id)">编辑</button>
                <button class="btn-sm btn-assign" @click="openAssignModal(item.id)">指派评委</button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 未开始 -->
      <div class="wks" v-else-if="State == 0">
        <div class="section-label notstarted-label">未开始竞赛</div>
        <div class="competition-list">
          <div class="competition-card card-notstarted" v-for="item in mainInfo" :key="item.id">
            <img v-lazy class="competition-img" :src="item.competitionImages[0]" />
            <div class="competition-info">
              <div class="competition-card-title">{{ item.competitionName }} <span class="tag-notstarted">未开始</span></div>
              <div class="competition-card-desc">{{ truncate(item.introduction, 75) }}</div>
              <div class="competition-card-meta">
                <span>{{ item.registrationBegin }}</span>
                <span>参赛人数：0</span>
                <span class="tag-org">主办：速通小分队</span>
              </div>
              <div class="competition-card-users">
                <span class="user-count">-</span>
              </div>
              <!-- 按钮 -->
              <div class="competition-card-actions">
                <button class="btn-sm" @click="openDetailModal(item.id)">详情</button>
                <button class="btn-sm btn-edit" @click="openEditModal(item.id)">编辑</button>
                <button class="btn-sm btn-delete" @click="confirmDelete(item.id)">删除</button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 评审中 -->
      <div class="psz" v-else-if="State == 2">
        <div class="section-label reviewing-label">评审中竞赛</div>
        <div class="competition-list">
          <div class="competition-card card-reviewing" v-for="item in mainInfo" :key="item.id">
            <img v-lazy class="competition-img" :src="item.competitionImages[0]" />
            <div class="competition-info">
              <div class="competition-card-title">{{ item.competitionName }} <span class="tag-reviewing">评审中</span></div>
              <div class="competition-card-desc">{{ truncate(item.introduction, 75) }}</div>
              <div class="competition-card-meta">
                <span>{{ item.registrationEnd }} ~ {{ item.activityEnd }}</span>
                <span>参赛人数：{{ item.competitionPeople }}</span>
                <span class="tag-org">主办：速通小分队</span>
              </div>
              <div class="competition-card-users">
                <img v-lazy class="user-avatar" :src="item.userAvatars[0]" />
                <img v-lazy class="user-avatar" :src="item.userAvatars[1]" />
                <span class="user-count">+{{ item.competitionPeople }}</span>
              </div>
              <!-- 按钮 -->
              <div class="competition-card-actions">
                <button class="btn-sm" @click="openDetailModal(item.id)">详情</button>
                <button class="btn-sm btn-edit" @click="openEditModal(item.id)">编辑</button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 已完成 -->
      <div class="ywc" v-else>
        <div class="section-label finished-label">已完成竞赛</div>
        <div class="competition-list">
          <div class="competition-card card-finished" v-for="item in mainInfo" :key="item.id">
            <img v-lazy class="competition-img" :src="item.competitionImages[0]" />
            <div class="competition-info">
              <div class="competition-card-title">{{ item.competitionName }} <span class="tag-finished">已结束</span></div>
              <div class="competition-card-desc">{{ truncate(item.introduction, 75) }}</div>
              <div class="competition-card-meta">
                <span>{{ item.activityEnd }}</span>
                <span>参赛人数：{{ item.competitionPeople }}</span>
                <span class="tag-org">主办：速通小分队</span>
              </div>
              <div class="competition-card-users">
                <img v-lazy class="user-avatar" :src="item.userAvatars[0]" />
                <img v-lazy class="user-avatar" :src="item.userAvatars[1]" />
                <span class="user-count">+{{ item.competitionPeople }}</span>
              </div>
              <!-- 按钮 -->
              <div class="competition-card-actions">
                <button class="btn-sm" @click="openDetailModal(item.id)">详情</button>
                <button class="btn-sm btn-award" @click="openAwardModal(item.id)">查看成绩</button>
                <button class="btn-sm btn-works" @click="openWorksModal(item.id)">查看作品</button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
    </div>
  </div>
  <!-- 创建竞赛 -->
  <div v-if="showCreateModal" class="modal-mask">
    <div class="modal-container modal-form" @click.stop>
      <div class="modal-header">
        <span>创建竞赛</span>
        <span class="modal-close" @click="closeCreateModal">×</span>
      </div>
      <div class="modal-body">
        <div class="modal-form-item">
          <label>竞赛名称</label>
          <input v-model="form.competitionName" placeholder="请输入竞赛名称" />
        </div>
        <div class="modal-form-item">
          <label>竞赛类别</label>
          <select v-model="form.categoryId">
            <option value="" disabled selected>请选择比赛类型</option>
            <option v-for="item in Stype" :key="item.id" :value="item.id">{{ item.name }}</option> 
          </select> 
        </div>
        <div class="modal-form-item">
          <label>竞赛介绍</label>
          <textarea v-model="form.introduction" placeholder="请输入竞赛介绍"></textarea>
        </div>
        <div class="modal-form-item">
          <label>报名时间</label>
          <input type="date" v-model="form.registrationBegin" />
        </div>
        <div class="modal-form-item">
          <label>报名截止时间</label>
          <input type="date" v-model="form.registrationEnd" />
        </div>
        <div class="modal-form-item">
          <label>活动结束时间</label>
          <input type="date" v-model="form.activityEnd" />
        </div>
        <div class="modal-form-item">
          <label>人数限定</label>
          <input type="number" v-model="form.people" placeholder="请输入人数上限" />
        </div>
        <div class="modal-form-item">
          <label>报名须知</label>
          <textarea v-model="form.notes" placeholder="请输入报名须知"></textarea>
        </div>
        <div class="modal-form-item">
          <label>竞赛赛程</label>
          <textarea v-model="form.schedule" placeholder="请输入竞赛赛程"></textarea>
        </div>
        <div class="modal-form-item">
          <label>封面图片</label>
          <!-- 文件上传 -->
          <input type="file" multiple accept="image/*" @change="handleImageUpload" />
          
          <!-- 图片预览 -->
          <div class="image-preview-list">
            <div v-for="(img, index) in form.images" :key="index" class="image-preview-item">
              <img v-lazy :src="img" alt="预览" class="preview-img" />
              <button class="btn-danger btn-sm" @click="removeImage(index)">×</button>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn-blue" @click="handleCreate()">创建</button>
        <button class="btn-gray" @click="closeCreateModal">取消</button>
      </div>
    </div>
  </div>
  <!-- 编辑 -->
  <div v-if="showEditModal" class="modal-mask">
    <div class="modal-container modal-form" @click.stop>
      <div class="modal-header">
        <span>编辑竞赛</span>
        <span class="modal-close" @click="closeEditModal">×</span>
      </div>
      <div class="modal-body">
        <div class="modal-form-item">
          <label>竞赛名称</label>
          <input v-model="editForm.competitionName" placeholder="请输入竞赛名称" />
        </div>
        <div class="modal-form-item">
          <label>竞赛类别</label>
          <select v-model="editForm.categoryId">
            <option value="" disabled selected>请选择比赛类型</option>
            <option v-for="item in Stype" :key="item.id" :value="item.id">{{ item.name }}</option> 
          </select> 
        </div>
        <div class="modal-form-item">
          <label>竞赛介绍</label>
          <textarea v-model="editForm.introduction" placeholder="请输入竞赛介绍"></textarea>
        </div>
        <div class="modal-form-item">
          <label>报名时间</label>
          <input type="date" v-model="editForm.registrationBegin" />
        </div>
        <div class="modal-form-item">
          <label>报名截止时间</label>
          <input type="date" v-model="editForm.registrationEnd" />
        </div>
        <div class="modal-form-item">
          <label>活动结束时间</label>
          <input type="date" v-model="editForm.activityEnd" />
        </div>
        <div class="modal-form-item">
          <label>人数限定</label>
          <input type="number" v-model="editForm.people" placeholder="请输入人数上限" />
        </div>
        <div class="modal-form-item">
          <label>报名须知</label>
          <textarea v-model="editForm.notes" placeholder="请输入报名须知"></textarea>
        </div>
        <div class="modal-form-item">
          <label>竞赛赛程</label>
          <textarea v-model="editForm.schedule" placeholder="请输入竞赛赛程"></textarea>
        </div>
        <div class="modal-form-item">
          <label>封面图片</label>
          <!-- 文件上传 -->
          <input type="file" multiple accept="image/*" @change="handleImageUploadById" />
          
          <!-- 图片预览 -->
          <div class="image-preview-list">
            <div v-for="(img, index) in editForm.images" :key="index" class="image-preview-item">
              <img v-lazy :src="img" alt="预览" class="preview-img" />
              <button class="btn-danger btn-sm" @click="removeImageById(index)">×</button>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn-blue" @click="handleEdit">保存</button>
        <button class="btn-gray" @click="closeEditModal">取消</button>
      </div>
    </div>
  </div>
  <!-- 详情 -->
  <div v-if="showDetailModal" class="modal-mask">
    <div class="modal-container modal-detail" @click.stop>
      <div class="modal-header">
        <span>竞赛详情</span>
        <span class="modal-close" @click="closeDetailModal">×</span>
      </div>
      <div class="modal-body modal-detail-body">
        <div class="detail-title">{{detailData.competitionName}}</div>
        <div class="detail-section">
          <div class="detail-section-title">基本信息</div>
          <div class="detail-row">
            <div class="detail-label">竞赛介绍：</div>
            <div class="detail-value">{{detailData.introduction}}</div>
          </div>
          <div class="detail-row-group">
            <div class="detail-row">
              <div class="detail-label">报名时间：</div>
              <div class="detail-value">{{detailData.registrationBegin}}</div>
              <div class="detail-label">截止时间：</div>
              <div class="detail-value">{{detailData.registrationEnd}}</div>
              <div class="detail-label">活动结束时间：</div>
              <div class="detail-value">{{detailData.activityEnd}}</div>
            </div>
          </div>
          <div class="detail-row">
            <div class="detail-label">人数限定：</div>
            <div class="detail-value">{{detailData.people}}</div>
          </div>
          <div class="detail-row">
            <div class="detail-label">报名须知：</div>
            <div class="detail-value">{{detailData.notes}}</div>
          </div>
          <div class="detail-row">
            <div class="detail-label">竞赛赛程：</div>
            <div class="detail-value">{{detailData.schedule}}</div>
          </div>
        </div>
        <!-- 大赛评委展示区 -->
        <div class="assigned-judges-title">大赛评委</div>
        <div class="assigned-judges-list">
          <div v-for="(judge,index) in detailData.judges" :key="index" class="assigned-judge-item">
            <img v-lazy :src="judge.judgeAvatarUrl" class="assigned-judge-avatar" />
            <span class="assigned-judge-name">{{judge.judgeName}}</span>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn-blue" @click="closeDetailModal">关闭</button>
      </div>
    </div>
  </div>
  <!-- 评委指派 -->
  <div v-if="showAssignModal" class="modal-mask">
    <div class="modal-container modal-assign" @click.stop>
      <div class="modal-header">
        <span>指派评委 - {{ judgeData.competitionName }}</span>
        <span class="modal-close" @click="closeAssignModal">×</span>
      </div>
      <div class="modal-body modal-assign-body">
        <div class="assign-search-row">
          <input v-model="csjudge.name" placeholder="搜索评委姓名" class="assign-search-input" />
          <button class="btn-blue search-judge" @click="searchJudge()">搜索</button>
        </div>
        <div class="assign-list-row">
          <!-- 未选评委 -->
          <div class="assign-list">
            <div v-for="judge in allJudges" :key="judge.id" class="assign-judge-item" @click="selectJudge(judge.id)">
              <img v-lazy :src="judge.avatarUrl" class="assign-judge-avatar" />
              <div class="assign-judge-info">
                <div class="assign-judge-name">{{judge.username}}</div>
                <div class="assign-judge-meta">{{judge.university}}</div>
              </div>
              <span class="assign-judge-add">+</span>
            </div>
            <div v-if="allJudges.length === 0" class="assign-empty">无匹配评委</div>
          </div>
          <!-- 已选评委 -->
          <div class="assign-selected">
            <div class="assign-selected-title">已选评委：</div>
            <div v-if="judgeData.judges.length === 0" class="assign-empty">暂无</div>
            <div v-for="judge in judgeData.judges" :key="judge.judgeId" class="assign-selected-item">
              <img v-lazy :src="judge.judgeAvatarUrl" class="assign-judge-avatar" />
              <div class="assign-judge-info">
                <div class="assign-judge-name">{{judge.judgeName}}</div>
                <div class="assign-judge-meta">{{ judge.universityName }}</div>
              </div>
              <span class="assign-judge-remove" @click="removeJudge(judge.judgeId)">×</span>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn-blue" @click="handleAssignJudges">确定</button>
        <button class="btn-gray" @click="closeAssignModal">取消</button>
      </div>
    </div>
  </div>
  <!-- 查看奖项 -->
  <div v-if="showAwardModal" class="modal-mask">
    <div class="modal-container modal-award" @click.stop>
      <div class="modal-header">
        <span style="font-size: 24px; font-weight: bold; color: #222;">查看奖项 - {{ allWorks.competitionName }}</span>
        <span class="modal-close" @click="closeAwardModal">×</span>
      </div>
      <div class="modal-body modal-award-body">
        <!-- 统计信息 -->
        <div class="award-stats">
          <div class="award-stat-item">
            <div class="award-stat-number">{{ getAwardCount(1) }}</div>
            <div class="award-stat-label">一等奖</div>
          </div>
          <div class="award-stat-item">
            <div class="award-stat-number">{{ getAwardCount(2) }}</div>
            <div class="award-stat-label">二等奖</div>
          </div>
          <div class="award-stat-item">
            <div class="award-stat-number">{{ getAwardCount(3) }}</div>
            <div class="award-stat-label">三等奖</div>
          </div>
          <div class="award-stat-item">
            <div class="award-stat-number">{{ allWorks.awardList ? allWorks.awardList.length : 0 }}</div>
            <div class="award-stat-label">总获奖数</div>
          </div>
        </div>
        
        <!-- 获奖队伍列表 -->
        <div class="award-teams-container">
          <div class="award-section">
            <div class="award-section-title">
              <span class="award-icon gold-icon">🥇</span>
              一等奖
            </div>
            <div class="award-teams-grid">
              <div v-for="team in getAwardTeams(1)" :key="team.id" class="award-team-card gold-card">
                <div class="award-team-name">{{ team.teamName }}</div>
                <div class="award-team-badge gold-badge">一等奖</div>
              </div>
              <div v-if="getAwardCount(1) === 0" class="award-empty">暂无一等奖</div>
            </div>
          </div>
          
          <div class="award-section">
            <div class="award-section-title">
              <span class="award-icon silver-icon">🥈</span>
              二等奖
            </div>
            <div class="award-teams-grid">
              <div v-for="team in getAwardTeams(2)" :key="team.id" class="award-team-card silver-card">
                <div class="award-team-name">{{ team.teamName }}</div>
                <div class="award-team-badge silver-badge">二等奖</div>
              </div>
              <div v-if="getAwardCount(2) === 0" class="award-empty">暂无二等奖</div>
            </div>
          </div>
          
          <div class="award-section">
            <div class="award-section-title">
              <span class="award-icon bronze-icon">🥉</span>
              三等奖
            </div>
            <div class="award-teams-grid">
              <div v-for="team in getAwardTeams(3)" :key="team.id" class="award-team-card bronze-card">
                <div class="award-team-name">{{ team.teamName }}</div>
                <div class="award-team-badge bronze-badge">三等奖</div>
              </div>
              <div v-if="getAwardCount(3) === 0" class="award-empty">暂无三等奖</div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn-blue" @click="closeAwardModal">关闭</button>
      </div>
    </div>
  </div>


  <!-- 作品列表 -->
  <div v-if="showWorksModal" class="modal-mask">
    <div class="modal-container modal-works" @click.stop>
      <div class="modal-header">
        <span style="font-size: 24px; font-weight: bold; color: #222;">作品列表 - {{ allWorks.competitionName }}</span>
        <span class="modal-close" @click="closeWorksModal">×</span>
      </div>
      <div class="modal-body modal-works-body">
        <!-- 作品统计 -->
        <div class="works-stats">
          <div class="works-stat-item">
            <div class="works-stat-number">{{ awardedWorks.length }}</div>
            <div class="works-stat-label">获奖作品</div>
          </div>
          <div class="works-stat-item">
            <div class="works-stat-number">{{ getAwardCount(1) }}</div>
            <div class="works-stat-label">一等奖作品</div>
          </div>
          <div class="works-stat-item">
            <div class="works-stat-number">{{ getAwardCount(2) }}</div>
            <div class="works-stat-label">二等奖作品</div>
          </div>
          <div class="works-stat-item">
            <div class="works-stat-number">{{ getAwardCount(3) }}</div>
            <div class="works-stat-label">三等奖作品</div>
          </div>
        </div>
        
        <div class="works-section">
          <div class="works-title">
            <span class="works-icon">🎨</span>
            获奖作品展示
          </div>
          <div class="works-list">
            <div v-for="work in awardedWorks" :key="work.id" class="work-item" @click="showPicture(work.id)" >
              <div class="work-image-container">
                <img v-lazy :src="work.worksUrl[0]" class="work-img" />
                <div class="work-overlay">
                  <span class="work-view-btn">点击查看</span>
                </div>
              </div>
              <div class="work-info">
                <div class="work-name">{{work.teamName}}</div>
                <div class="work-award-tag">
                  <span v-if="work.awardRank === 1" class="award-badge gold">🥇 一等奖</span>
                  <span v-else-if="work.awardRank === 2" class="award-badge silver">🥈 二等奖</span>
                  <span v-else class="award-badge bronze">🥉 三等奖</span>
                </div>
              </div>
            </div>
            <div v-if="awardedWorks.length === 0" class="works-empty">
              <div class="works-empty-icon">🎨</div>
              <div class="works-empty-text">暂无获奖作品</div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn-blue" @click="closeWorksModal">关闭</button>
      </div>
    </div>
  </div>

  <!-- 作品展示 -->
  <el-dialog v-model="dialogVisible" width="90%" class="big-picture-dialog" :close-on-click-modal="false">
    <template #header>
      <div class="picture-dialog-header">
        <span class="picture-dialog-title">作品展示</span>
        <!-- <span class="picture-dialog-subtitle">点击图片可放大查看</span> -->
      </div>
    </template>
    <div class="picture-dialog-content">
      <el-carousel 
        height="600px" 
        :loop="false" 
        :autoplay="false"
        indicator-position="outside"
        arrow="always"
        class="picture-carousel"
      >
        <el-carousel-item v-for="(item,index) in pictureList" :key="index" class="picture-carousel-item">
          <div class="picture-container">
            <img v-lazy :src="item" alt="作品图片" class="big-picture-img" />
            <div class="picture-info">
              <span class="picture-counter">{{ index + 1 }} / {{ pictureList.length }}</span>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
  </el-dialog>
</template>
<script setup>
import { ref, computed,watch,onMounted } from 'vue';
import { queryUserByNameAndCompetitionId,getCompetitionCategory,getWithLike,getAllAward2,saveCompetitionJudge,getByIdWithRedis,getCompetitionJudge,deleteCompetition } from '@/apis/admin'
import { addCompetition,getCompetitionById,updateCompetition } from '@/apis/competition'
import { uploadImage } from '@/apis/person'
import { queryUserByName } from '@/apis/user.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const State= ref(0)
onMounted(() => {
  search()
  getStype()
});
const searchtj = ref({
  competitionName: '',
  competitionStatus: '0',
  categoryId: ''
});

const Stype = ref([])
const getStype= async () => {
  const result = await getCompetitionCategory()
  if(result.code){
    Stype.value = result.data
  }
}
const truncate = (text, length) => {
  return text.length > length ? text.substring(0, length) + '...' : text;
};

let debounceTimer = null

watch(
  () => searchtj.value.competitionName,
  (newVal) => {
    if (debounceTimer) {
      clearTimeout(debounceTimer)
    }
    debounceTimer = setTimeout(() => {
      searchtj.value.competitionName = newVal
      // console.log(State.value);
      
    }, 500) // 延迟500ms执行
  },
  { deep: true }
)

/* watch(
  () => State.value,
  (newVal) => {
    search()
  }
); */

const mainInfo = ref([])

const search = async ()=>{
  const result = await getWithLike(searchtj.value)
  if(result.code){
    mainInfo.value = result.data
    State.value = searchtj.value.competitionStatus
  }
  
  
}
// 弹窗控制变量
const showCreateModal = ref(false);
const showEditModal = ref(false);
const showDetailModal = ref(false);
const showAssignModal = ref(false);
const showAwardModal = ref(false);
const showAwardResultModal = ref(false);
const showWorksModal = ref(false);
const worksTarget = ref('');
const awardTarget = ref('');
const awardSetDone = ref({}); // 记录每个竞赛是否已设置奖项

// 弹窗表单数据
let form = ref({
  competitionName: '',
  categoryId: '',
  introduction: '',
  registrationBegin: '',
  registrationEnd: '',
  activityEnd: '',
  people: '',
  notes: '',
  schedule: '',
  images: []
});
const editForm = ref({
  competitionName: '',
  categoryId: '',
  introduction: '',
  registrationBegin: '',
  registrationEnd: '',
  activityEnd: '',
  people: '',
  notes: '',
  schedule: '',
  images: []
});
const detailData = ref({
  competitionName: '',
  introduction: '',
  registrationBegin: '',
  registrationEnd: '',
  activityEnd: '',
  people: '',
  notes: '',
  schedule: '',
  teamCount: '',
  judges: []
});

// 处理图片上传
async function handleImageUpload(event) {
  
  const files = event.target.files;
  
  if (!files || files.length == 0) return;

  for (let i = 0; i < files.length; i++) {
    const file = files[i];
    const formData =new FormData();
    formData.append('file', file);
    
    const result = await uploadImage(formData);
    if(result.code){
      
      form.value.images.push(result.data);
    }
  }
  
}

// 移除某一张图片
function removeImage(index) {
  form.value.images.splice(index, 1);
}

// 处理图片上传
async function handleImageUploadById(event) {
  const files = event.target.files;

  if (!files || files.length === 0) return;

  for (let i = 0; i < files.length; i++) {
    const file = files[i];
    const formData =new FormData();
    formData.append('file', file);
    
    const result = await uploadImage(formData);
    if(result.code){
     editForm.value.images.push(result.data);
    }
  }
  
}

// 移除某一张图片
function removeImageById(index) {
  editForm.value.images.splice(index, 1);
}
// 评委指派相关
const assignTarget = ref('');
const judgeSearch = ref('');
const allJudges = ref([]);
const csjudge=ref({
  competitionId: '',
  name: ''
})

// 搜索用户
const searchJudge = async () => {
  const result = await queryUserByNameAndCompetitionId(csjudge.value.competitionId,csjudge.value.name);
  if(result.code){
    allJudges.value = result.data;
  }else{
    allJudges.value = [];
  }
  for(let i = 0; i < judgeData.value.judges.length; i++){
    allJudges.value = allJudges.value.filter(j => j.id !== judgeData.value.judges[i].judgeId);
  }
  
}

const selectedJudges = ref([]);
/* const filteredJudges = computed(() => {
  if (!judgeSearch.value) return allJudges.value.filter(j => !selectedJudges.value.some(s => s.id === j.id));
  return allJudges.value.filter(j =>
    (j.name.includes(judgeSearch.value) || j.title.includes(judgeSearch.value) || j.org.includes(judgeSearch.value)) &&
    !selectedJudges.value.some(s => s.id === j.id)
  );
}); */

// 奖项设置表单
const awardForm = ref({
  first: 30,
  second: 20,
  third: 10
});
// 静态队伍数据
const teams = [
  { id: 1, name: 'A队', score: 97.5 },
  { id: 2, name: 'B队', score: 92.3 },
  { id: 3, name: 'C队', score: 89.7 },
  { id: 4, name: 'D队', score: 85.2 },
  { id: 5, name: 'E队', score: 80.1 },
  { id: 6, name: 'F队', score: 78.6 }
];
const sortedTeams = computed(() => [...teams].sort((a, b) => b.score - a.score));
const awardedTeams = computed(() => {
  // 按比例分配奖项
  const total = teams.length;
  const firstNum = Math.round(total * awardForm.value.first / 100);
  const secondNum = Math.round(total * awardForm.value.second / 100);
  const thirdNum = Math.round(total * awardForm.value.third / 100);
  return [
    { level: '一等奖', teams: sortedTeams.value.slice(0, firstNum) },
    { level: '二等奖', teams: sortedTeams.value.slice(firstNum, firstNum + secondNum) },
    { level: '三等奖', teams: sortedTeams.value.slice(firstNum + secondNum, firstNum + secondNum + thirdNum) }
  ];
});


// 分类
const categories = [
  '艺术设计', '摄影', '广告', '书法', '数字艺术', '其他'
];

// 筛选日期范围
const filterStartDate = ref('');
const filterEndDate = ref('');

// 弹窗事件函数
function openCreateModal() {
  showCreateModal.value = true;
}
function closeCreateModal() {
  showCreateModal.value = false;
}
async function handleCreate() {
  const result = await addCompetition(form.value);
  
  if (result.code) {
    ElMessage.success('创建成功');
    closeCreateModal();
    search();
    form.value = {
      competitionName: '',
      categoryId: '',
      introduction: '',
      registrationBegin: '',
      registrationEnd: '',
      activityEnd: '',
      people: '',
      notes: '',
      schedule: '',
      images: []
    };
  }else{
    ElMessage.error(result.message || '创建失败');
  }
}
async function openEditModal(id) {
  const result = await getCompetitionById(id);
  if(result.code){
    editForm.value = result.data;
    showEditModal.value = true;
  }else{
    ElMessage.error(result.message || '获取数据失败');
  }
}
function closeEditModal() {
  showEditModal.value = false;
}
async function handleEdit() {
  const result = await updateCompetition(editForm.value);
  if(result.code){
    ElMessage.success('修改成功');
    closeEditModal();
    search();
  }else{
    ElMessage.error(result.message || '修改失败');
  }
  closeEditModal();
}
async function openDetailModal(id) {
  
  const result = await getByIdWithRedis(id);
  if(result.code){
    detailData.value = result.data;
    showDetailModal.value = true;
  }else{
    ElMessage.error(result.message || '获取数据失败');
  }
  
}
function closeDetailModal() {
  showDetailModal.value = false;
}
// 评委
const judgeData = ref({})
async function openAssignModal(id) {
  csjudge.value.competitionId = id;
  const result = await getCompetitionJudge(id);
  
  if(result.code){
    judgeData.value = result.data;
    searchJudge()
    showAssignModal.value = true;
  }else{
    ElMessage.error(result.message || '获取数据失败');
  }
  
}
function closeAssignModal() {
  showAssignModal.value = false;
}
function selectJudge(id) {
  if (!judgeData.value.judges.some(j => j.judgeId === id)) {
    const Judge = allJudges.value.find(j => j.id === id);
    const addJudge = { judgeId: Judge.id, judgeAvatarUrl: Judge.avatarUrl, judgeName: Judge.username, universityName: Judge.university };
    judgeData.value.judges.push(addJudge);
  }
  allJudges.value = allJudges.value.filter(j => j.id !== id);
  
}
// 移除评委
function removeJudge(id) {
  if (!allJudges.value.some(j => j.id === id)) {
    const Judge = judgeData.value.judges.find(j => j.judgeId === id);
    const addJudge = { id: Judge.judgeId, avatarUrl: Judge.judgeAvatarUrl, username: Judge.judgeName, university: Judge.universityName };
    allJudges.value.push(addJudge);
  }
  judgeData.value.judges = judgeData.value.judges.filter(j => j.judgeId !== id);
  
}
async function handleAssignJudges() {
  const sclist=[]
  for (let i =0 ; i < judgeData.value.judges.length; i++){
    sclist.push(judgeData.value.judges[i].judgeId)
  }
  const bc={
    competitionId: judgeData.value.competitionId,
    judges: sclist
  }
  const result = await saveCompetitionJudge(bc);
  if(result.code){
    ElMessage.success('分配成功');
  }else{
    ElMessage.error(result.message || '分配失败');
  }
  
  closeAssignModal();
}
// 设置奖项
const allWorks = ref({});

// 获取指定等级的获奖数量
const getAwardCount = (rank) => {
  if (!allWorks.value.awardList) return 0;
  return allWorks.value.awardList.filter(team => team.awardRank === rank).length;
};

// 获取指定等级的获奖队伍
const getAwardTeams = (rank) => {
  if (!allWorks.value.awardList) return [];
  return allWorks.value.awardList.filter(team => team.awardRank === rank);
};

async function openAwardModal(id) {
  const result = await getAllAward2(id);
  if(result.code){
    allWorks.value = result.data;
    showAwardModal.value = true;
  }else{
    ElMessage.error(result.message || '获取奖项信息失败');
  }
}

function closeAwardModal() {
  showAwardModal.value = false;
}
function handleAwardSet() {
  awardSetDone.value[awardTarget.value] = true;
  closeAwardModal();
}
function openAwardResultModal(competitionName) {
  awardTarget.value = competitionName;
  showAwardResultModal.value = true;
}
function closeAwardResultModal() {
  showAwardResultModal.value = false;
}
// 作品展示
// 静态作品数据

let awardedWorks = ref([])

// 控制弹窗
let dialogVisible = ref(false)
async function openWorksModal(id) {
  awardedWorks.value = []
  const result = await getAllAward2(id);
  if(result.code){
    allWorks.value = result.data;
    showWorksModal.value = true;
    // 筛选获奖作品
    if (allWorks.value.awardList && allWorks.value.awardList.length > 0) {
      for (let i = 0; i < allWorks.value.awardList.length; i++){
        if(allWorks.value.awardList[i].awardRank >= 1 && allWorks.value.awardList[i].awardRank <= 3){
          awardedWorks.value.push(allWorks.value.awardList[i])
        }
      }
    }
  }else{
    ElMessage.error(result.message || '获取作品信息失败');
  }
}
const pictureList = ref([])
const showPicture = (id) => {
  for (let i=0 ; i < allWorks.value.awardList.length; i++){
    if(allWorks.value.awardList[i].id == id){
      pictureList.value = allWorks.value.awardList[i].worksUrl;
    }
  }
  dialogVisible.value=true;
  
}
function closeWorksModal() {
  showWorksModal.value = false;
}

// 删除竞赛
async function confirmDelete(id) {
  ElMessageBox.confirm('确定要删除该竞赛吗？此操作不可恢复', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const result = await deleteCompetition(id);
      if (result.code) {
        ElMessage.success('删除成功');
        search(); // 重新加载数据
      } else {
        ElMessage.error(result.message || '删除失败');
      }
    } catch (error) {
      ElMessage.error('删除失败：' + error.message);
    }
  }).catch(() => {
    // 取消删除
  });
}

const signupTeams = [
  {
    id: 1,
    name: '星辰队',
    members: [
      { id: 1, name: '张明', avatar: 'https://randomuser.me/api/portraits/men/32.jpg', role: 'leader' },
      { id: 2, name: '李华', avatar: 'https://randomuser.me/api/portraits/women/44.jpg', role: 'member' },
      { id: 5, name: '王老师', avatar: 'https://randomuser.me/api/portraits/men/45.jpg', role: 'teacher' }
    ],
    works: [
      { id: 1, name: '星空之下', img: 'https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=120&q=80' }
    ],
    judges: [
      { id: 1, name: '评委A', avatar: 'https://randomuser.me/api/portraits/men/11.jpg', score: 92 },
      { id: 2, name: '评委B', avatar: 'https://randomuser.me/api/portraits/women/12.jpg', score: 95 },
      { id: 3, name: '评委C', avatar: 'https://randomuser.me/api/portraits/men/13.jpg', score: 90 }
    ],
    totalScore: 92.3,
    award: '一等奖'
  },
  {
    id: 2,
    name: '夜光队',
    members: [
      { id: 3, name: '王芳', avatar: 'https://randomuser.me/api/portraits/men/12.jpg', role: 'leader' },
      { id: 4, name: '赵青', avatar: 'https://randomuser.me/api/portraits/women/31.jpg', role: 'member' },
      { id: 6, name: '李老师', avatar: 'https://randomuser.me/api/portraits/women/46.jpg', role: 'teacher' }
    ],
    works: [
      { id: 2, name: '夜色流光', img: 'https://images.unsplash.com/photo-1519125323398-675f0ddb6308?auto=format&fit=crop&w=120&q=80' },
      { id: 3, name: '城市之光', img: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=120&q=80' }
    ],
    judges: [
      { id: 1, name: '评委A', avatar: 'https://randomuser.me/api/portraits/men/11.jpg', score: 88 },
      { id: 2, name: '评委B', avatar: 'https://randomuser.me/api/portraits/women/12.jpg', score: 91 },
      { id: 3, name: '评委C', avatar: 'https://randomuser.me/api/portraits/men/13.jpg', score: 90 }
    ],
    totalScore: 89.7,
    award: '二等奖'
  }
]

const assignedJudges = [
  { id: 1, name: '评委A', avatar: 'https://randomuser.me/api/portraits/men/11.jpg' },
  { id: 2, name: '评委B', avatar: 'https://randomuser.me/api/portraits/women/12.jpg' },
  { id: 3, name: '评委C', avatar: 'https://randomuser.me/api/portraits/men/13.jpg' }
]
</script>
<style scoped>
.competition-mgmt-page {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  padding: 32px 32px 24px 32px;
  min-height: 600px;
}
.competition-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 18px;
}
.competition-title {
  font-size: 26px;
  font-weight: bold;
  color: #222e3c;
  margin-bottom: 4px;
}
.competition-desc {
  color: #888;
  font-size: 15px;
}
.competition-actions {
  display: flex;
  gap: 12px;
}
.btn-blue {
  background: #1976d2;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 8px 18px;
  font-size: 15px;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.2s;
}
.btn-blue:hover {
  background: #1565c0;
}
.search-judge{
  width: 75px;
  height: 34px;
}
.btn-gray {
  background: #f5f7fa;
  color: #1976d2;
  border: none;
  border-radius: 6px;
  padding: 8px 18px;
  font-size: 15px;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.2s;
}
.btn-gray:hover {
  background: #e3e8ee;
}
/* 删除按钮样式已移至.btn-sm.btn-delete */
.btn-search {
  margin-left: 8px;
  padding: 8px 18px;
}
.competition-filters {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}
.filter-select {
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  padding: 6px 12px;
  font-size: 14px;
  background: #fafbfc;
  color: #333;
}
.filter-search {
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  padding: 6px 12px;
  font-size: 14px;
  background: #fafbfc;
  color: #333;
  flex: 1;
}
.competition-section {
  margin-bottom: 18px;
}
.section-label {
  font-size: 17px;
  font-weight: bold;
  margin: 18px 0 10px 0;
  color: #1976d2;
  letter-spacing: 1px;
}
.ongoing-label {
  color: #1976d2;
}
.finished-label {
  color: #888;
}
.competition-list {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
  flex-wrap: wrap;
  justify-content: flex-start;
  box-sizing: border-box;
}
.competition-list-finished {
  opacity: 0.85;
}
.competition-list-notstarted {
  opacity: 0.85;
}
.competition-card {
  background: #f8fafc;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  width: calc((100% - 64px) / 3); /* 3卡片3间距，略缩小 */
  min-width: 320px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: relative;
  transition: box-shadow 0.2s, transform 0.2s;
  border: 2px solid transparent;
  box-sizing: border-box;
}
.competition-card:hover {
  box-shadow: 0 6px 24px rgba(25, 118, 210, 0.13);
  transform: translateY(-4px) scale(1.03);
  border: 2px solid #1976d2;
}
.card-ongoing {
  border-left: 5px solid #1976d2;
}
.card-finished {
  border-left: 5px solid #888;
  background: #f3f3f3;
}
.card-notstarted {
  border: 2px dashed #f44336;
  background: #fff1f0;
}
.competition-img {
  width: 100%;
  height: 190px;
  object-fit: cover;
}
.competition-info {
  padding: 18px 18px 12px 18px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.competition-card-title {
  font-size: 18px;
  font-weight: bold;
  color: #222e3c;
  margin-bottom: 2px;
}
.tag-hot {
  background: #ff9800;
  color: #fff;
  border-radius: 6px;
  font-size: 12px;
  padding: 2px 8px;
  margin-left: 8px;
}
.tag-finished {
  background: #888;
  color: #fff;
  border-radius: 6px;
  font-size: 12px;
  padding: 2px 8px;
  margin-left: 8px;
}
.tag-org {
  background: #e3f2fd;
  color: #1976d2;
  border-radius: 6px;
  font-size: 12px;
  padding: 2px 8px;
  margin-left: 8px;
}
.competition-card-desc {
  color: #666;
  font-size: 14px;
  margin-bottom: 2px;
}
.competition-card-meta {
  color: #888;
  font-size: 13px;
  display: flex;
  gap: 18px;
  margin-bottom: 4px;
  flex-wrap: wrap;
}
.competition-card-users {
  display: flex;
  align-items: center;
  gap: 4px;
}
.user-avatar {
  width: 26px;
  height: 26px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
.user-count {
  color: #888;
  font-size: 13px;
  margin-left: 4px;
}
.competition-card-actions {
  margin-top: 10px;
  display: flex;
  gap: 8px;
}
.btn-sm {
  background-color: #e0e0e0;
  color: #333;
  padding: 6px 12px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  font-size: 13px;
  transition: background-color 0.2s;
}
.btn-sm:hover {
  background-color: #d0d0d0;
}
/* 确保删除按钮样式优先级更高 */
.btn-sm.btn-delete {
  background-color: #ffcdd2;
  color: #c62828;
}
.btn-sm.btn-delete:hover {
  background-color: #ef9a9a;
}
.btn-sm.btn-edit {
  background: #fffbe6;
  color: #ff9800;
}
.btn-sm.btn-edit:hover {
  background: #ffe0b2;
}
.btn-sm.btn-danger {
  background: #fde2e2;
  color: #d32f2f;
}
.btn-sm.btn-danger:hover {
  background: #ffbdbd;
}
.btn-sm.btn-view {
  background: #e3f2fd;
  color: #1976d2;
}
.btn-sm.btn-view:hover {
  background: #bbdefb;
}
.competition-pagination {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #888;
  font-size: 14px;
  margin-top: 12px;
}
.btn-page {
  background: #1976d2;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 4px 14px;
  font-size: 14px;
  cursor: pointer;
  font-weight: 500;
}
/* 未开始竞赛样式 */
.section-label.notstarted-label {
  font-size: 18px;
  font-weight: 600;
  color: #ff9800;
  margin: 32px 0 16px 0;
}
.card-notstarted {
  border: 2px dashed #f44336;
  background: #fff1f0;
}
.tag-notstarted {
  background: #f44336;
  color: #fff;
  border-radius: 4px;
  font-size: 13px;
  padding: 2px 8px;
  margin-left: 8px;
}
/* 弹窗样式 */
.modal-mask {
  position: fixed;
  z-index: 1000;
  left: 0; top: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.25);
  display: flex;
  align-items: center;
  justify-content: center;
}
.modal-container {
  background: #fff;
  border-radius: 10px;
  width: 600px;
  max-width: 98vw;
  box-shadow: 0 8px 32px rgba(0,0,0,0.18);
  padding: 0 0 18px 0;
  animation: modalIn 0.2s;
}

.modal-form {
  width: 800px;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 20px;
  font-weight: 600;
  padding: 18px 24px 10px 24px;
  border-bottom: 1px solid #f0f0f0;
}
.modal-close {
  font-size: 26px;
  cursor: pointer;
  color: #888;
  transition: color 0.2s;
}
.modal-close:hover {
  color: #f44336;
}
.modal-body {
  padding: 18px 24px 0 24px;
}

.modal-form .modal-body {
  flex: 1;
  overflow-y: auto;
  max-height: calc(80vh - 120px);
}
.modal-form-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 14px;
}
.modal-form-item label {
  font-size: 15px;
  color: #333;
  font-weight: 500;
}
.modal-form-item input,
.modal-form-item select,
.modal-form-item textarea {
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  padding: 7px 10px;
  font-size: 15px;
  outline: none;
  transition: border 0.2s;
}
.modal-form-item input:focus,
.modal-form-item select:focus,
.modal-form-item textarea:focus {
  border: 1.5px solid #1976d2;
}
.modal-form-item textarea {
  min-height: 48px;
  resize: vertical;
}

.image-preview-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 8px;
}

.image-preview-item {
  position: relative;
  width: 100px;
  height: 100px;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}

.preview-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-preview-item .btn-sm.btn-danger {
  position: absolute;
  top: 4px;
  right: 4px;
  padding: 2px 6px;
  font-size: 12px;
}
.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 0 24px;
}

.modal-form .modal-footer {
  flex-shrink: 0;
}
@keyframes modalIn {
  from { transform: translateY(40px); opacity: 0; }
  to { transform: none; opacity: 1; }
}
/* 详情弹窗美化 */
.modal-detail {
  width: 1100px;
  max-width: 99vw;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(25,118,210,0.13);
}
.modal-detail-body {
  padding: 32px 48px 24px 48px;
  background: #f8fafc;
  border-radius: 0 0 16px 16px;
  max-height: 80vh;
  overflow-y: auto;
}
.detail-title {
  font-size: 30px;
  font-weight: bold;
  color: #1976d2;
  margin-bottom: 32px;
  text-align: center;
  letter-spacing: 2px;
}
.detail-section {
  margin-bottom: 32px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(25,118,210,0.06);
  padding: 28px 32px 18px 32px;
}
.detail-section-title {
  font-size: 20px;
  font-weight: 600;
  color: #1976d2;
  margin-bottom: 18px;
  letter-spacing: 1px;
}
.detail-row {
  display: flex;
  align-items: flex-start;
  margin-bottom: 16px;
  flex-wrap: wrap;
  font-size: 17px;
}
.detail-label {
  min-width: 120px;
  color: #888;
  font-size: 17px;
  font-weight: 500;
  margin-right: 12px;
}
.detail-value {
  color: #222;
  font-size: 17px;
  margin-right: 32px;
  word-break: break-all;
}
.detail-row-group {
  margin-bottom: 10px;
}
/* 指派评委弹窗样式 */
.modal-assign {
  width: 820px;
  max-width: 99vw;
}
.modal-assign-body {
  padding-top: 10px;
}
.assign-search-row {
  display: flex;
  align-items: center; /* 垂直居中 */
  gap: 12px; /* 可选：添加间距 */
}
.assign-search-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  font-size: 15px;
  outline: none;
  transition: border 0.2s;
}
.assign-search-input:focus {
  border: 1.5px solid #1976d2;
}
.assign-list-row {
  display: flex;
  gap: 24px;
}
.assign-list {
  flex: 1.2;
  min-width: 0;
  max-height: 260px;
  overflow-y: auto;
  border-right: 1px solid #f0f0f0;
  padding-right: 16px;
}
.assign-judge-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 7px 0;
  cursor: pointer;
  border-radius: 5px;
  transition: background 0.15s;
}
.assign-judge-item:hover {
  background: #f5f7fa;
}
.assign-judge-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}
.assign-judge-info {
  flex: 1;
  min-width: 0;
}
.assign-judge-name {
  font-weight: 500;
  font-size: 15px;
}
.assign-judge-meta {
  color: #888;
  font-size: 13px;
}
.assign-judge-add {
  color: #1976d2;
  font-size: 22px;
  font-weight: bold;
  margin-left: 8px;
}
.assign-selected {
  flex: 1;
  min-width: 0;
  padding-left: 16px;
}
.assign-selected-title {
  font-size: 15px;
  font-weight: 500;
  margin-bottom: 6px;
}
.assign-selected-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 7px;
  background: #f8fafc;
  border-radius: 5px;
  padding: 5px 8px;
  position: relative;
}
.assign-judge-remove {
  color: #f44336;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  margin-left: 8px;
  transition: color 0.18s;
}
.assign-judge-remove:hover {
  color: #b71c1c;
}
.assign-empty {
  color: #bbb;
  font-size: 14px;
  padding: 18px 0 0 0;
}
.btn-sm.btn-assign {
  background: #fff3e0;
  color: #ff9800;
  border: none;
  border-radius: 6px;
  padding: 6px 14px;
  font-size: 15px !important;
  font-weight: 500 !important;
  cursor: pointer;
  margin-right: 8px;
  transition: background 0.2s, color 0.2s;
}
.btn-sm.btn-assign:hover {
  background: #ffe0b2;
  color: #fb8c00;
}
/* 奖项设置弹窗样式 */
.modal-award {
  width: 900px;
  max-width: 95vw;
  max-height: 90vh;
  border-radius: 18px;
  box-shadow: 0 8px 32px rgba(25,118,210,0.18);
  background: #fff;
  padding-bottom: 18px;
  display: flex;
  flex-direction: column;
}
.modal-award-body {
  padding: 20px 24px;
  flex: 1;
  overflow-y: auto;
  max-height: calc(90vh - 140px);
}

/* 奖项统计样式 */
.award-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  padding: 20px;
  background: linear-gradient(135deg, #f8fafc 0%, #e3f2fd 100%);
  border-radius: 12px;
  border: 1px solid #e3f2fd;
}

.award-stat-item {
  flex: 1;
  text-align: center;
  padding: 16px 12px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(25,118,210,0.08);
  transition: transform 0.2s;
}

.award-stat-item:hover {
  transform: translateY(-2px);
}

.award-stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #1976d2;
  margin-bottom: 6px;
}

.award-stat-label {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

/* 奖项队伍容器 */
.award-teams-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 奖项分组样式 */
.award-section {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(25,118,210,0.08);
  overflow: hidden;
  border: 1px solid #e3f2fd;
}

.award-section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px 20px;
  font-size: 18px;
  font-weight: bold;
  color: #1976d2;
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  border-bottom: 1px solid #bbdefb;
}

.award-icon {
  font-size: 20px;
}

.gold-icon {
  color: #ffd700;
}

.silver-icon {
  color: #c0c0c0;
}

.bronze-icon {
  color: #cd7f32;
}

/* 获奖队伍网格 */
.award-teams-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
  padding: 20px;
}

/* 获奖队伍卡片 */
.award-team-card {
  background: #f8fafc;
  border-radius: 10px;
  padding: 16px 12px;
  text-align: center;
  border: 2px solid transparent;
  transition: all 0.2s;
  position: relative;
  overflow: hidden;
}

.award-team-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, transparent 0%, currentColor 50%, transparent 100%);
  opacity: 0.6;
}

.gold-card {
  border-color: #ffd700;
  background: linear-gradient(135deg, #fffbf0 0%, #fff8dc 100%);
}

.gold-card::before {
  background: linear-gradient(90deg, transparent 0%, #ffd700 50%, transparent 100%);
}

.silver-card {
  border-color: #c0c0c0;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

.silver-card::before {
  background: linear-gradient(90deg, transparent 0%, #c0c0c0 50%, transparent 100%);
}

.bronze-card {
  border-color: #cd7f32;
  background: linear-gradient(135deg, #fff8f0 0%, #ffe4c4 100%);
}

.bronze-card::before {
  background: linear-gradient(90deg, transparent 0%, #cd7f32 50%, transparent 100%);
}

.award-team-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(25,118,210,0.15);
}

.award-team-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  word-break: break-word;
}

.award-team-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  color: #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,0.1);
}

.gold-badge {
  background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
  color: #b8860b;
}

.silver-badge {
  background: linear-gradient(135deg, #c0c0c0 0%, #e0e0e0 100%);
  color: #616161;
}

.bronze-badge {
  background: linear-gradient(135deg, #cd7f32 0%, #daa520 100%);
  color: #6d4c1b;
}

.award-empty {
  grid-column: 1 / -1;
  text-align: center;
  color: #999;
  font-size: 14px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px dashed #dee2e6;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .modal-award {
    width: 95vw;
    max-height: 95vh;
  }
  
  .modal-award-body {
    padding: 16px;
    max-height: calc(95vh - 120px);
  }
  
  .award-stats {
    flex-direction: column;
    gap: 12px;
    padding: 16px;
  }
  
  .award-stat-item {
    padding: 12px 8px;
  }
  
  .award-stat-number {
    font-size: 24px;
  }
  
  .award-teams-grid {
    grid-template-columns: 1fr;
    gap: 12px;
    padding: 16px;
  }
  
  .award-team-card {
    padding: 12px 8px;
  }
  
  .award-team-name {
    font-size: 14px;
  }
  
  .award-section-title {
    padding: 12px 16px;
    font-size: 16px;
  }
}

/* 作品展示响应式设计 */
@media (max-width: 768px) {
  .modal-works {
    width: 95vw;
    max-height: 95vh;
  }
  
  .modal-works-body {
    padding: 16px;
    max-height: calc(95vh - 120px);
  }
  
  .works-stats {
    flex-direction: column;
    gap: 12px;
    padding: 16px;
  }
  
  .works-stat-item {
    padding: 12px 8px;
  }
  
  .works-stat-number {
    font-size: 24px;
  }
  
  .works-list {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 16px;
  }
  
  .work-image-container {
    height: 140px;
  }
  
  .work-info {
    padding: 12px;
  }
  
  .work-name {
    font-size: 14px;
  }
  
  .works-title {
    padding: 12px 16px;
    font-size: 18px;
  }
}

@media (max-width: 480px) {
  .modal-award {
    width: 98vw;
    max-height: 98vh;
  }
  
  .modal-award-body {
    padding: 12px;
    max-height: calc(98vh - 100px);
  }
  
  .award-stats {
    padding: 12px;
  }
  
  .award-stat-number {
    font-size: 20px;
  }
  
  .award-stat-label {
    font-size: 12px;
  }
  
  .award-teams-grid {
    padding: 12px;
  }
  
  .award-team-card {
    padding: 10px 6px;
  }
  
  .award-team-name {
    font-size: 13px;
  }
  
  .award-team-badge {
    font-size: 11px;
    padding: 3px 8px;
  }
  
  .modal-works {
    width: 98vw;
    max-height: 98vh;
  }
  
  .modal-works-body {
    padding: 12px;
    max-height: calc(98vh - 100px);
  }
  
  .works-stats {
    padding: 12px;
  }
  
  .works-stat-number {
    font-size: 20px;
  }
  
  .works-stat-label {
    font-size: 12px;
  }
  
  .works-list {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  
  .work-image-container {
    height: 120px;
  }
  
  .work-info {
    padding: 10px;
  }
  
  .work-name {
    font-size: 13px;
  }
  
  .award-badge {
    font-size: 12px;
    padding: 4px 12px;
  }
  
  .works-empty {
    padding: 40px 16px;
    min-height: 150px;
  }
  
  .works-empty-icon {
    font-size: 36px;
  }
  
  .works-empty-text {
    font-size: 14px;
  }
}
.award-form-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 18px;
}
.award-form-row label {
  font-size: 15px;
  color: #333;
  font-weight: 500;
}
.award-form-row input {
  width: 60px;
  padding: 5px 8px;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  font-size: 15px;
  outline: none;
  transition: border 0.2s;
}
.award-form-row input:focus {
  border: 1.5px solid #1976d2;
}
.award-teams-list {
  margin-bottom: 16px;
}
.award-teams-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  margin-bottom: 8px;
}
.award-teams-table th {
  background: #e3f2fd;
  color: #1976d2;
  font-size: 17px;
  font-weight: 600;
  padding: 12px 0;
  border-bottom: 1.5px solid #bbdefb;
}
.award-teams-table td {
  padding: 12px 0;
  text-align: center;
  font-size: 16px;
  color: #333;
  border-bottom: 1px solid #f0f0f0;
}
.award-teams-table tr:nth-child(even) td {
  background: #f6fafd;
}
.award-teams-table tr:last-child td {
  border-bottom: none;
}
.award-badge {
  display: inline-block;
  min-width: 60px;
  padding: 4px 16px;
  border-radius: 16px;
  font-size: 15px;
  font-weight: bold;
  color: #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}
.award-badge.gold {
  background: linear-gradient(90deg,#ffd700 60%,#fff8dc 100%);
  color: #b8860b;
}
.award-badge.silver {
  background: linear-gradient(90deg,#c0c0c0 60%,#f5f5f5 100%);
  color: #616161;
}
.award-badge.bronze {
  background: linear-gradient(90deg,#cd7f32 60%,#ffe4c4 100%);
  color: #6d4c1b;
}
.award-preview {
  margin-bottom: 10px;
}
.award-level-row {
  margin-bottom: 6px;
}
.award-level-label {
  font-weight: 500;
  color: #1976d2;
  margin-right: 8px;
}
.award-team-name {
  display: inline-block;
  background: #e3f2fd;
  color: #1976d2;
  border-radius: 4px;
  padding: 2px 8px;
  margin-right: 6px;
  font-size: 14px;
}
/* 作品弹窗样式 */
.modal-works {
  width: 1100px;
  max-width: 95vw;
  max-height: 90vh;
  border-radius: 18px;
  box-shadow: 0 8px 32px rgba(25,118,210,0.13);
  background: #fff;
  padding-bottom: 18px;
  display: flex;
  flex-direction: column;
}
.modal-works-body {
  padding: 20px 24px;
  flex: 1;
  overflow-y: auto;
  max-height: calc(90vh - 140px);
}

/* 作品统计样式 */
.works-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  padding: 20px;
  background: linear-gradient(135deg, #f8fafc 0%, #e3f2fd 100%);
  border-radius: 12px;
  border: 1px solid #e3f2fd;
}

.works-stat-item {
  flex: 1;
  text-align: center;
  padding: 16px 12px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(25,118,210,0.08);
  transition: transform 0.2s;
}

.works-stat-item:hover {
  transform: translateY(-2px);
}

.works-stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #1976d2;
  margin-bottom: 6px;
}

.works-stat-label {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}
.works-section {
  margin-bottom: 18px;
}
.works-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #1976d2;
  padding: 16px 20px;
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  border-radius: 10px;
  border: 1px solid #bbdefb;
}

.works-icon {
  font-size: 24px;
}
.works-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 24px;
  justify-content: flex-start;
}
.work-item {
  background: #f8fafc;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(25,118,210,0.08);
  padding: 0;
  display: flex;
  flex-direction: column;
  position: relative;
  transition: transform 0.18s, box-shadow 0.18s;
  cursor: pointer;
  overflow: hidden;
  border: 2px solid transparent;
}

.work-item:hover{
  transform: translateY(-4px);
  box-shadow: 0 8px 32px rgba(25,118,210,0.16);
  border-color: #1976d2;
}

.work-image-container {
  position: relative;
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.work-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.work-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(25, 118, 210, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.work-item:hover .work-overlay {
  opacity: 1;
}

.work-item:hover .work-img {
  transform: scale(1.1);
}

.work-view-btn {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  padding: 8px 16px;
  border: 2px solid #fff;
  border-radius: 20px;
  background: transparent;
}

.work-info {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.work-name {
  font-size: 16px;
  color: #222;
  text-align: center;
  font-weight: 600;
  margin-bottom: 12px;
  line-height: 1.4;
}
.work-award-tag {
  text-align: center;
}
.award-badge {
  display: inline-block;
  min-width: 80px;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: bold;
  color: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.12);
  transition: transform 0.2s;
}

.award-badge:hover {
  transform: scale(1.05);
}

.award-badge.gold {
  background: linear-gradient(135deg,#ffd700 0%,#ffed4e 100%);
  color: #b8860b;
}
.award-badge.silver {
  background: linear-gradient(135deg,#c0c0c0 0%,#e0e0e0 100%);
  color: #616161;
}
.award-badge.bronze {
  background: linear-gradient(135deg,#cd7f32 0%,#daa520 100%);
  color: #6d4c1b;
}
.works-empty {
  grid-column: 1 / -1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #999;
  background: #f8f9fa;
  border-radius: 12px;
  border: 2px dashed #dee2e6;
  min-height: 200px;
}

.works-empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.6;
}

.works-empty-text {
  font-size: 16px;
  font-weight: 500;
}
.btn-sm.btn-works {
  background: #e3f2fd;
  color: #1976d2;
  border: none;
  border-radius: 6px;
  padding: 6px 14px;
  font-size: 15px;
  cursor: pointer;
  font-weight: 500;
  margin-right: 8px;
  transition: background 0.2s, color 0.2s;
}
.btn-sm.btn-works:hover {
  background: #bbdefb;
  color: #0d47a1;
}

.btn-sm.btn-award {
  background: #fff3e0;
  color: #ff9800;
  border: none;
  border-radius: 6px;
  padding: 6px 14px;
  font-size: 15px !important;
  font-weight: 500 !important;
  cursor: pointer;
  margin-right: 8px;
  transition: background 0.2s, color 0.2s;
}

.btn-sm.btn-award:hover {
  background: #ffe0b2;
  color: #fb8c00;
}
.filter-date-range {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-right: 12px;
}
.filter-date-input {
  padding: 5px 10px;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  font-size: 15px;
  outline: none;
  width: 130px;
}
.filter-date-sep {
  color: #888;
  font-size: 16px;
}
/* 新增样式 */
.card-signup {
  border-left: 5px solid #1976d2;
}
.tag-signup {
  background: #1976d2;
  color: #fff;
  border-radius: 6px;
  font-size: 12px;
  padding: 2px 8px;
  margin-left: 8px;
}
.reviewing-label {
  color: #43a047;
  font-size: 18px;
  font-weight: 600;
  margin: 32px 0 16px 0;
}
.card-reviewing {
  border-left: 5px solid #43a047;
  background: #f1f8f5;
}
.tag-reviewing {
  background: #43a047;
  color: #fff;
  border-radius: 6px;
  font-size: 12px;
  padding: 2px 8px;
  margin-left: 8px;
}
.competition-list-reviewing {
  opacity: 0.95;
}
.signup-teams-title {
  font-size: 20px;
  font-weight: bold;
  color: #1976d2;
  margin: 32px 0 18px 0;
  letter-spacing: 1px;
}
.signup-teams-list {
  display: flex;
  flex-wrap: wrap;
  gap: 32px;
  margin-bottom: 24px;
}
.signup-team-card {
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 2px 12px rgba(25,118,210,0.08);
  padding: 28px 28px 18px 28px;
  min-width: 320px;
  max-width: 400px;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.signup-team-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.signup-team-name {
  font-size: 20px;
  font-weight: 700;
  color: #1976d2;
  letter-spacing: 1px;
}
.signup-team-count {
  color: #888;
  font-size: 15px;
  font-weight: 500;
}
.signup-team-award {
  background: linear-gradient(90deg,#ff9800 60%,#ffc107 100%);
  color: #fff;
  border-radius: 10px;
  font-size: 15px;
  padding: 3px 16px;
  margin-left: 16px;
  font-weight: bold;
  box-shadow: 0 1px 4px rgba(255,152,0,0.08);
}
.signup-team-members {
  display: flex;
  gap: 28px;
  margin-bottom: 10px;
}
.signup-team-member {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  min-width: 80px;
}
.signup-member-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
.signup-member-name {
  font-size: 15px;
  color: #1976d2;
  font-weight: 500;
}
.signup-member-tag {
  display: inline-block;
  font-size: 12px;
  padding: 2px 10px;
  border-radius: 10px;
  margin-top: 2px;
  margin-bottom: 1px;
  font-weight: 600;
}
.leader-tag {
  background: #e3f2fd;
  color: #1976d2;
  border: 1px solid #90caf9;
}
.teacher-tag {
  background: #fffbe6;
  color: #ff9800;
  border: 1px solid #ffe082;
}
.signup-team-works-title {
  font-size: 16px;
  color: #1976d2;
  margin: 8px 0 4px 0;
  font-weight: 600;
}
.signup-team-works {
  display: flex;
  gap: 18px;
}
.signup-team-work {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}
.signup-work-img {
  width: 64px;
  height: 64px;
  border-radius: 6px;
  object-fit: cover;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
.signup-work-name {
  font-size: 14px;
  color: #666;
  text-align: center;
  font-weight: 500;
}
.signup-team-judges-title {
  font-size: 16px;
  color: #1976d2;
  margin: 10px 0 4px 0;
  font-weight: 600;
}
.signup-team-judges {
  display: flex;
  align-items: center;
  gap: 24px;
  flex-wrap: wrap;
  margin-bottom: 6px;
}
.signup-team-judge {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #f5f7fa;
  border-radius: 8px;
  padding: 4px 14px 4px 8px;
  min-width: 120px;
}
.signup-judge-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 2px;
}
.signup-judge-name {
  font-size: 14px;
  color: #1976d2;
  margin-right: 4px;
  font-weight: 500;
}
.signup-judge-score {
  font-size: 14px;
  color: #333;
  font-weight: 600;
}
.signup-team-total-score {
  font-size: 16px;
  color: #43a047;
  font-weight: bold;
  margin-left: 24px;
  letter-spacing: 1px;
}

.assigned-judges-title {
  font-size: 20px;
  font-weight: bold;
  color: #1976d2;
  margin: 32px 0 18px 0;
  letter-spacing: 1px;
}
.assigned-judges-list {
  display: flex;
  gap: 32px;
  margin-bottom: 24px;
}
.assigned-judge-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}
.assigned-judge-avatar {
  width: 54px;
  height: 54px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
.assigned-judge-name {
  font-size: 15px;
  color: #1976d2;
  font-weight: 500;
}
.signup-team-award {
  background: #ff9800;
  color: #fff;
  border-radius: 8px;
  font-size: 13px;
  padding: 2px 10px;
  margin-left: 10px;
  font-weight: bold;
}

/* 轮播图 */
.demonstration {
  color: var(--el-text-color-secondary);
}

.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

/* 大图弹窗样式 */
.big-picture-dialog .el-dialog__body {
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 620px;
}

.picture-dialog-header {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.picture-dialog-title {
  font-size: 20px;
  font-weight: bold;
  color: #1976d2;
}

.picture-dialog-subtitle {
  font-size: 14px;
  color: #666;
}

.picture-dialog-content {
  width: 100%;
  height: 100%;
}

.picture-carousel {
  width: 100%;
}

.picture-carousel-item {
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f8fafc;
}

.picture-container {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.big-picture-img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
  background: #fff;
  transition: transform 0.3s;
  cursor: zoom-in;
}

.big-picture-img:hover {
  transform: scale(1.02);
}

.picture-info {
  position: absolute;
  bottom: 20px;
  right: 20px;
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  padding: 8px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
}

.picture-counter {
  color: #fff;
}

/* 响应式图片展示 */
@media (max-width: 768px) {
  .big-picture-dialog .el-dialog__body {
    min-height: 400px;
  }
  
  .picture-dialog-title {
    font-size: 18px;
  }
  
  .picture-dialog-subtitle {
    font-size: 12px;
  }
  
  .picture-info {
    bottom: 10px;
    right: 10px;
    padding: 6px 10px;
    font-size: 12px;
  }
}

@media (max-width: 480px) {
  .big-picture-dialog .el-dialog__body {
    min-height: 300px;
  }
  
  .picture-dialog-title {
    font-size: 16px;
  }
  
  .picture-dialog-subtitle {
    font-size: 11px;
  }
  
  .picture-info {
    bottom: 8px;
    right: 8px;
    padding: 4px 8px;
    font-size: 11px;
  }
}

</style>