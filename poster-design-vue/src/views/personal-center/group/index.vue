<template>
  <div class="team-management-container">
    <div class="content-card">
      <div class="header-row">
        <div class="page-header">
          <h2 class="page-title">我的团队</h2>
          <p class="page-subtitle">管理和查看你参与的所有团队信息</p>
        </div>
        <div class="action-bar">
          <el-button type="primary" @click="handleAddTeam" class="add-btn">
            新增队伍
          </el-button>
        </div>
      </div>
      <el-divider class="header-divider" />
      <div class="table-section">
        <el-table :data="tableData" border class="team-table" :header-cell-style="{
          background: '#f8f9fa',
          color: '#333',
          fontWeight: '600',
        }" :row-class-name="getRowClassName" fit highlight-current-row style="width: 100%">
          <el-table-column prop="team" label="队伍名称" width="180" show-overflow-tooltip />
          <el-table-column prop="competitionName" label="竞赛名称" width="240" show-overflow-tooltip />

          <el-table-column prop="createTime" label="创建日期" width="180" />

          <el-table-column prop="teacher" label="指导老师" width="180" />
          <el-table-column label="操作" min-width="100">
            <template #default="scope">
              <el-button
                size="small"
                @click="handleView(scope.row)"
                class="action-btn"
                >查看队伍详情</el-button
              >
              <!-- <el-button size="small" type="danger" @click="handleDelete(scope.row.id)" class="action-btn"
                :disabled="!isCurrentUserCaptain">
                删除队伍
              </el-button> -->
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pagination-section">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="team.pageNo"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="team.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalCount"
          class="pagination"
        />
      </div>
    </div>
    <!-- 新增：创建队伍对话框 -->
    <el-dialog v-model="addDialogVisible" title="新增队伍" width="60%">
      <el-form :model="addTeam" label-width="120px">
        <el-form-item label="队伍名称" prop="team">
          <el-input v-model="addTeam.team" placeholder="例如：算法攻坚队" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitAdd">确定</el-button>
      </template>
    </el-dialog>
    <!-- 队伍信息对话框 -->
    <el-dialog v-model="dialogVisible" title="队伍详情" width="70%">
      <template #default>
        <el-form :model="currentTeam" label-width="100px">
          <el-form-item label="队伍名称" prop="teamName">
            <el-input v-model="currentTeam.teamName" disabled />
          </el-form-item>
          <el-form-item label="创建日期" prop="createTime">
            <el-input v-model="currentTeam.createTime" disabled />
          </el-form-item>
          <el-form-item label="指导老师" prop="teacher">
            <el-input v-model="currentTeam.teacher" disabled />
          </el-form-item>

          <!-- 邀请队员/新增指导老师（仅队长可见） -->
          <el-form-item label="团队操作" v-if="isCurrentUserCaptain">
            <el-button type="primary" @click="openSearch('student')"
              >邀请队员</el-button
            >
            <el-button type="success" @click="openSearch('teacher')"
              >更新指导老师</el-button
            >
            <el-button type="warning" @click="handleDelete()"
              >删除队伍</el-button
            >
          </el-form-item>

          <!-- 队员列表 -->
          <el-form-item label="队员列表">
            <el-table
              :data="currentTeam.members"
              style="width: 100%"
              border
              empty-text="暂无队员"
            >
              <el-table-column prop="username" label="名称" width="180" />
              <el-table-column label="角色" width="180">
                <template #default="scope">
                  {{ scope.row.status === 0 ? "队长" : "队员" }}
                </template>
              </el-table-column>
              <el-table-column prop="url" label="头像" width="80">
                <template #default="scope">
                  <img
                    v-lazy
                    :src="scope.row.url || '/default-avatar.png'"
                    alt="头像"
                    style="width: 40px; height: 40px; border-radius: 50%"
                  />
                </template>
              </el-table-column>
              <el-table-column prop="university" label="学校" width="180" />
              <el-table-column prop="phone" label="手机号" width="180" />
              <el-table-column
                label="操作"
                width="120"
                v-if="isCurrentUserCaptain"
              >
                <template #default="scope">
                  <el-button
                    size="mini"
                    type="danger"
                    @click="removeMember(scope.row.memberId)"
                    :disabled="scope.row.status === 0"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <el-button @click="dialogVisible = false">确定</el-button>
      </template>
    </el-dialog>

    <!-- 搜索用户对话框（用于新增学生/老师） -->
    <el-dialog v-model="searchDialogVisible" :title="searchTitle" width="60%">
      <el-input
        v-model="searchKeyword"
        placeholder="请输入姓名进行模糊搜索"
        clearable
        style="margin-bottom: 15px"
      >
        <template #append>
          <el-button type="primary" @click="doSearch">搜索</el-button>
        </template>
      </el-input>

      <el-table
        :data="searchResult"
        style="width: 100%"
        border
        empty-text="暂无搜索结果"
      >
        <el-table-column prop="username" label="姓名" width="180" />
        <el-table-column prop="phone" label="手机号" width="180" />
        <el-table-column prop="operation" label="操作" width="120">
          <template #default="scope">
            <el-button size="mini" type="primary" @click="addUser(scope.row)">
              添加
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="内容">
          <el-input v-model="Recontent"></el-input>
        </el-table-column>
      </el-table>

      <template #footer>
        <el-button @click="searchDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
const myteamId = ref(null);
import {
  getTeam,
  getTeamById,
  getUser,
  queryTeamByName,
  addTeamMember,
  deleteTeamMember,
  updateTeacher,
  deleteTeam,
  addmyTeam,
  sendInform,
} from "@/apis/person";
import { ElMessageBox, ElMessage } from "element-plus";
import { usePersonStore } from "@/stores/person";
const usePerson = usePersonStore();
// 队伍列表相关
const team = ref({
  name: "",
  pageNo: 1,
  pageSize: 10,
});
const tableData = ref([]);
const totalCount = ref(0);
const checkUserId = usePerson.userId; // 当前登录用户ID
// 详情对话框相关
const dialogVisible = ref(false);
const currentTeam = ref({
  id: null,
  teamName: "",
  createTime: "",
  teacher: "",
  members: [],
});

// 当前用户是否为队长的计算属性（status=0为队长）
const isCurrentUserCaptain = computed(() => {
  if (!currentTeam.value.members || currentTeam.value.members.length === 0) {
    return false;
  }
  const currentUser = currentTeam.value.members.find(
    (member) => member.userId === checkUserId
  );
  return currentUser ? currentUser.status === 0 : false;
});

// 搜索对话框相关
const searchDialogVisible = ref(false);
const searchType = ref(""); // 'student' 或 'teacher'
const searchTitle = ref("");
const searchKeyword = ref("");
const searchResult = ref([]);

// 初始化加载队伍列表
onMounted(() => {
  fetchTeamList();
});

// 新增：创建队伍对话框
const addDialogVisible = ref(false);
const addTeam = ref({
  team: "",
  teacherId: "",
});

// 新建队伍
const handleAddTeam = () => {
  // if (!isCurrentUserCaptain.value) {
  //   ElMessage.warning("只有队长可以创建队伍");
  //   return;
  // }
  addDialogVisible.value = true;
  addTeam.value = { team: "", teacherId: "0" };
};

const handleSubmitAdd = async () => {
  try {
    const res = await addmyTeam(addTeam.value);
    if (res.code) {
      ElMessage.success("队伍创建成功！");
      addDialogVisible.value = false;
      fetchTeamList();
    } else {
      ElMessage.error(res.message || "队伍创建失败");
    }
  } catch (err) {
    console.error("创建队伍失败:", err);
    ElMessage.error("网络异常，请稍后重试");
  }
};

// 获取队伍列表
const fetchTeamList = async () => {
  try {
    const res = await getTeam(team.value);
    const resUser = await getUser();
    if (res.code) {
      tableData.value = res.data.records || [];
      totalCount.value = res.data.total || 0;
    } else {
      ElMessageBox.error(res.message || "获取队伍列表失败");
    }
  } catch (err) {
    console.error("获取队伍列表错误:", err);
    ElMessageBox.error("网络异常，请稍后重试");
  }
};

// 查看队伍详情
const handleView = async (row) => {
  try {
    myteamId.value = row.id;
    const res = await getTeamById(row.id);
    if (res.code) {
      const teamUserList = res.data.teamUserList || [];
      const memberPromises = teamUserList.map(async (member) => {
        try {
          const userRes = await getUser(member.userId);
          if (userRes.code) {
            return {
              userId: member.userId,
              memberId: member.userId,
              username: userRes.data.username || "未知姓名",
              status: member.status, // 0-队长，1-队员
              university: userRes.data.school || "未知学校",
              phone: userRes.data.phone || "未知电话",
              ...member,
            };
          } else {
            console.error(`获取用户${member.userId}信息失败:`, userRes.message);
            return {
              userId: member.userId,
              memberId: member.userId,
              username: "获取失败",
              status: member.status,
              ...member,
            };
          }
        } catch (err) {
          console.error(`获取用户${member.userId}信息出错:`, err);
          return {
            userId: member.userId,
            memberId: member.userId,
            username: "网络错误",
            status: member.status,
            ...member,
          };
        }
      });

      const members = await Promise.all(memberPromises);
      currentTeam.value = {
        id: res.data.id,
        teamName: res.data.team,
        createTime: row.createTime,
        teacher: res.data.teacher,
        members: members,
      };
      dialogVisible.value = true;
    } else {
      ElMessageBox.error(res.message || "获取队伍详情失败");
    }
  } catch (err) {
    console.error("获取队伍详情错误:", err);
    ElMessageBox.error("网络异常，请稍后重试");
  }
};

// 打开搜索对话框（仅队长可操作）
const openSearch = (type) => {
  if (!isCurrentUserCaptain.value) {
    ElMessage.warning("只有队长可以执行此操作");
    return;
  }
  searchType.value = type;
  searchTitle.value = type === "student" ? "邀请队员" : "新增指导老师";
  searchKeyword.value = "";
  searchResult.value = [];
  searchDialogVisible.value = true;
};

// 执行搜索
const doSearch = async () => {
  const res = await queryTeamByName(searchKeyword.value);
  if (res.code) {
    searchResult.value = res.data;
    ElMessage.success("搜索成功！");
  } else {
    ElMessage.error(res.message);
  }
};

// 发送邀请信息相关
const receiver = ref({
  sendRole: 1,
  receiveId: "",
  receiveRole: 0,
  content: "",
  category: 0,
  teamId: "",
});
const Recontent = ref("");

// 添加用户（仅队长可操作）
const addUser = async (user) => {
  if (!isCurrentUserCaptain.value) {
    ElMessage.warning("只有队长可以执行此操作");
    return;
  }

  ElMessageBox.confirm("是否执行当前操作？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(async () => {
    if (searchType.value === "student") {
      receiver.value = {
        sendRole: 1,
        receiveId: user.id,
        receiveRole: 0,
        content: Recontent.value,
        category: 0,
        teamId: currentTeam.value.id,
      };
      const res1 = await sendInform(receiver.value);
      if (res1.code) {
        ElMessage.success("已发送邀请，等待对方同意！");
        searchDialogVisible.value = false;
      }
    } else if (searchType.value === "teacher") {
      const res = await updateTeacher(user.id, currentTeam.value.id);
      // handleView(myteamId.value)
      if (res.code) {
        ElMessage.success("更新指导老师成功！");
        // 关闭所有对话框
        dialogVisible.value = false;
        searchDialogVisible.value = false;
        fetchTeamList();
        // handleView(myteamId.value); 
      }
    }
  });
};

// 删除队伍（仅队长可操作）
const handleDelete = async () => {
  if (!isCurrentUserCaptain.value) {
    ElMessage.warning("只有队长可以删除队伍");
    return;
  }

  ElMessageBox.confirm("确定要删除该队伍吗？删除后不可恢复", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(async () => {
    const res = await deleteTeam(myteamId.value);
    if (res.code) {
      ElMessage.success("队伍删除成功");
      dialogVisible.value = false
      fetchTeamList();
    } else {
      ElMessage.error(res.message || "删除失败");
    }
  });
};

// 删除队员（仅队长可操作，且不能删除队长）
const removeMember = async (memberId) => {
  if (!isCurrentUserCaptain.value) {
    ElMessage.warning("只有队长可以删除队员");
    return;
  }

  // 检查是否为队长
  const member = currentTeam.value.members.find((m) => m.memberId === memberId);
  if (member && member.status === 0) {
    ElMessage.warning("队长不能被删除");
    return;
  }

  ElMessageBox.confirm("确定要删除该队员吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(async () => {
    const res = await deleteTeamMember(currentTeam.value.id, memberId);
    if (res.code) {
      ElMessage.success("队员删除成功");
      handleView({ id: currentTeam.value.id }); // 刷新详情
    } else {
      ElMessage.error(res.message || "删除失败");
    }
  });
};

// 分页相关
const handleSizeChange = (size) => {
  team.value.pageSize = size;
  team.value.pageNo = 1;
  fetchTeamList();
};

const handleCurrentChange = (page) => {
  team.value.pageNo = page;
  fetchTeamList();
};

// 行样式
const getRowClassName = ({ row, rowIndex }) => {
  return rowIndex % 2 === 0 ? "even-row" : "odd-row";
};
</script>

<style scoped>
.team-management-container {
  padding: 0;
  background: transparent;
  width: 100%;
  max-width: none;
  margin: 0;
}

.header-row {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  margin-bottom: 5px;
}

.page-header {
  padding-top: 24px;
  margin-bottom: 8px;
  padding-left: 8px;
}

.action-bar {
  margin-bottom: 0;
  display: flex;
  align-items: flex-end;
  justify-content: flex-end;
  padding-top: 0;
}

.header-divider {
  margin: 0 0 16px 0;
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

.action-bar {
  margin-bottom: 12px;
  display: flex;
  justify-content: flex-end;
}

.add-btn {
  font-weight: 600;
  border-radius: 6px;
}

.content-card {
  margin: 0;
  margin-top: 12px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  background: #fff;
  padding: 24px 32px 24px 32px;
}

.table-section {
  margin-bottom: 16px;
}

.team-table {
  border-radius: 8px;
  overflow: hidden;
  font-size: 15px;
}

.team-table :deep(.el-table__header) {
  background: #f8f9fa;
  color: #333;
  font-weight: 600;
}

.team-table :deep(.el-table__row) {
  transition: background-color 0.2s;
}

.team-table :deep(.el-table__row:hover) {
  background-color: #f5f7fa;
}

.even-row {
  background-color: #fafbfc;
}

.odd-row {
  background-color: #fff;
}

.team-table :deep(.el-table__cell) {
  text-align: center;
  vertical-align: middle;
  padding: 12px 0;
}

.team-table :deep(.el-table__cell .el-button) {
  border-radius: 4px;
  font-weight: 500;
  font-size: 14px;
  padding: 4px 14px;
}

.team-table :deep(.el-table__cell .el-button + .el-button) {
  margin-left: 8px;
}

.action-btn {
  border-radius: 4px;
  font-weight: 500;
  margin-right: 8px;
}

.pagination-section {
  display: flex;
  justify-content: flex-start;
  margin-top: 24px;
}

.pagination {
  background: transparent;
  padding: 0;
  border-radius: 0;
  box-shadow: none;
}

.el-dialog__body {
  max-height: 60vh;
  overflow-y: auto;
}

@media (max-width: 767px) {
  .team-management-container {
    padding: 0 4px;
  }

  .content-card {
    padding: 10px 4px;
    margin-top: 8px;
    border-radius: 10px;
  }

  .header-row {
    flex-direction: column;
    align-items: flex-start;
    margin-bottom: 4px;
  }

  .page-header {
    padding-top: 10px;
    margin-bottom: 4px;
    padding-left: 0;
  }

  .page-title {
    font-size: 18px;
    margin-bottom: 4px;
  }

  .page-subtitle {
    font-size: 13px;
  }

  .action-bar {
    margin-bottom: 8px;
    padding-top: 0;
  }

  .add-btn {
    font-size: 13px;
    padding: 4px 10px;
    border-radius: 5px;
  }

  .table-section {
    margin-bottom: 8px;
    overflow-x: auto;
  }

  .team-table {
    min-width: 800px;
    width: 800px;
    overflow-x: auto;
  }

  .team-table :deep(.el-table__header),
  .team-table :deep(.el-table__row) {
    font-size: 12px;
  }

  .team-table :deep(.el-table__cell) {
    padding: 8px 4px;
  }

  .action-btn {
    font-size: 12px;
    padding: 2px 8px;
    margin-right: 4px;
  }

  .pagination-section {
    margin-top: 12px;
    justify-content: center;
  }

  .el-pagination {
    font-size: 12px;
  }

  .el-dialog__body {
    max-height: 50vh;
    padding: 8px 4px;
  }
}
</style>
