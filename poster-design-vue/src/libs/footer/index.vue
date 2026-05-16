<template>
  <div class="fbody">
    <div class="foot">
      <p>
        <a href="#">关于我们</a>
        <a href="#">帮助中心</a>
        <a href="#">业务概况</a>
        <a href="#">网站声明</a>
        <a href="#">商务合作</a>
        <a href="#">搜索推荐</a>
        <a href="#">友情链接</a>
      </p>
      <p>Copyright &copy; 全国大学生海报设计大赛</p>
    </div>
  </div>
</template>
<script setup>
import { ref } from "vue";
</script>
<style scoped>
.fbody {
  background-color: #333;
  width: 100%;
}

html.dark .fbody {
  background-color: #121212;
}

.foot {
  height: 100px;
  padding-top: 40px;
  text-align: center;
  color: #999;
  font-size: 15px;
}

.foot p {
  line-height: 1;
  margin-bottom: 20px;
}

.foot a {
  text-decoration: none;
  color: #999;
  line-height: 1;
  padding: 0 10px 0 6px;
  border-right: 1px solid #999;
}

.foot a:last-child {
  border-right: none;
}
</style>
