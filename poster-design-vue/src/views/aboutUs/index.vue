<template>
  <header-vue></header-vue>
  <div class="about-us">
    <!-- 主要内容区域 -->
    <main class="main-content">
      <!-- 页面标题区域 -->
      <section class="hero-section">
        <div class="container">
          <h1 class="page-title">关于我们</h1>
          <p class="page-subtitle">全国大学生海报设计大赛组委会</p>
        </div>
      </section>
      <!-- 大赛介绍 -->
      <section class="intro-section">
        <div class="container">
          <div class="section-header">
            <h2>大赛介绍</h2>
            <div class="underline"></div>
          </div>
          <div class="intro-content">
            <div class="intro-text">
              <p>
                全国大学生海报设计大赛是由教育部高等教育司指导，面向全国高等院校在校学生的重要设计赛事。大赛旨在激发大学生的创新思维，提升设计能力，培养具有国际视野的优秀设计人才。
              </p>
              <p>
                自创办以来，大赛已成功举办多届，吸引了来自全国数百所高校的数万名学生参与，成为国内最具影响力的学生设计赛事之一。
              </p>
            </div>
            <div class="intro-stats">
              <div class="stat-item">
                <div class="stat-number">10+</div>
                <div class="stat-label">举办届数</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">500+</div>
                <div class="stat-label">参与高校</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">50000+</div>
                <div class="stat-label">参赛学生</div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 组织架构 -->
      <section class="organization-section">
        <div class="container">
          <div class="section-header">
            <h2>组织架构</h2>
            <div class="underline"></div>
          </div>
          <div class="org-structure">
            <div class="org-item">
              <div class="org-icon">🏆</div>
              <h3>指导单位</h3>
              <p>教育部高等教育司</p>
            </div>
            <div class="org-item">
              <div class="org-icon">🎨</div>
              <h3>主办单位</h3>
              <p>全国大学生海报设计大赛组委会</p>
            </div>
            <div class="org-item">
              <div class="org-icon">🤝</div>
              <h3>协办单位</h3>
              <p>各高校艺术学院、设计学院</p>
            </div>
            <div class="org-item">
              <div class="org-icon">💼</div>
              <h3>承办单位</h3>
              <p>专业设计机构、行业协会</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 大赛特色 -->
      <section class="features-section">
        <div class="container">
          <div class="section-header">
            <h2>大赛特色</h2>
            <div class="underline"></div>
          </div>
          <div class="features-grid">
            <div class="feature-card">
              <div class="feature-icon">🌟</div>
              <h3>权威评审</h3>
              <p>
                邀请国内外知名设计师、教授组成评审团，确保评审的专业性和权威性
              </p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">🎯</div>
              <h3>主题多样</h3>
              <p>
                涵盖公益、文化、科技等多个领域，激发学生的创新思维和社会责任感
              </p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">🏅</div>
              <h3>丰厚奖励</h3>
              <p>设立多个奖项等级，提供奖金、证书及实习机会等多元化奖励</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">🌍</div>
              <h3>国际交流</h3>
              <p>优秀作品有机会参与国际设计展览，促进国际设计文化交流</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">📚</div>
              <h3>学习平台</h3>
              <p>提供专业的设计教程、在线课程和资源分享，助力学生技能提升</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">🚀</div>
              <h3>创业支持</h3>
              <p>为优秀参赛者提供创业指导、项目孵化和投资对接服务</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 开发成员 -->
      <section class="team-section">
        <div class="container">
          <div class="section-header">
            <h2>开发成员</h2>
            <div class="underline"></div>
          </div>
          <div class="team-grid">
            <div class="team-member">
              <div class="avatar-gradient">
                <img
                  v-lazy
                  class="avatar"
                  src="http://122.51.51.36:9000/poster-design/20250714/7a78c18f-bd9e-428f-806a-3b3fb96083c8-thumb_TSK.jpg"
                  alt="成员1"
                />
              </div>
              <div class="member-name">田溯开</div>
              <div class="member-role">前端开发</div>
            </div>
            <div class="team-member">
              <div class="avatar-gradient">
                <img
                  v-lazy
                  class="avatar"
                  src="http://122.51.51.36:9000/poster-design/20250714/ddd2bc93-eb93-4eec-a5b8-73f58af8900b-thumb_ZZL.jpg"
                  alt="成员2"
                />
              </div>
              <div class="member-name">张志霖</div>
              <div class="member-role">UI设计</div>
            </div>
            <div class="team-member">
              <div class="avatar-gradient">
                <img
                  v-lazy
                  class="avatar"
                  src="http://122.51.51.36:9000/poster-design/20250714/e29872ff-36cf-469d-99cd-6c5ea6791d38-thumb_YLW.jpg"
                  alt="成员3"
                />
              </div>
              <div class="member-name">游龙威</div>
              <div class="member-role">后端开发、运维工程师</div>
            </div>
            <div class="team-member">
              <div class="avatar-gradient">
                <img
                  v-lazy
                  class="avatar"
                  src="http://122.51.51.36:9000/poster-design/20250714/a16a6bac-b291-442d-8b72-50015d629d62-thumb_SZL.jpg"
                  alt="成员4"
                />
              </div>
              <div class="member-name">孙哲麟</div>
              <div class="member-role">前端开发</div>
            </div>
            <div class="team-member">
              <div class="avatar-gradient">
                <img
                  v-lazy
                  class="avatar"
                  src="http://122.51.51.36:9000/poster-design/20250714/a7269579-62f4-474a-b18b-f205c343a803-thumb_HKY.jpg"
                  alt="成员5"
                />
              </div>
              <div class="member-name">贺克屿</div>
              <div class="member-role">测试工程师</div>
            </div>
            <div class="team-member">
              <div class="avatar-gradient">
                <img
                  v-lazy
                  class="avatar"
                  src="http://122.51.51.36:9000/poster-design/20250714/713e3453-125a-4a1d-b22b-a8f8505348c7-thumb_CBH.jpg"
                  alt="成员6"
                />
              </div>
              <div class="member-name">陈博辉</div>
              <div class="member-role">前端开发</div>
            </div>
          </div>
        </div>
      </section>

      <!-- 联系我们 -->
      <section class="contact-section">
        <div class="container">
          <div class="section-header">
            <h2>联系我们</h2>
            <div class="underline"></div>
          </div>
          <div class="contact-content">
            <div class="contact-info">
              <div class="contact-item">
                <div class="contact-icon">📧</div>
                <div class="contact-details">
                  <h4>邮箱地址</h4>
                  <p>usx@poster-design.com</p>
                </div>
              </div>
              <div class="contact-item">
                <div class="contact-icon">📞</div>
                <div class="contact-details">
                  <h4>联系电话</h4>
                  <p>400-123-4567</p>
                </div>
              </div>
              <div class="contact-item">
                <div class="contact-icon">📍</div>
                <div class="contact-details">
                  <h4>办公地址</h4>
                  <p>浙江省绍兴市越城区绍兴文理学院</p>
                </div>
              </div>
              <div class="contact-item">
                <div class="contact-icon">⏰</div>
                <div class="contact-details">
                  <h4>工作时间</h4>
                  <p>周一至周五 9:00-18:00</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>

    <footer-vue></footer-vue>
  </div>
</template>

<script setup>
import headerVue from "@/libs/header/index.vue";
import footerVue from "@/libs/footer/index.vue";
</script>

<style scoped>
.about-us {
  min-height: 100vh;
  background-color: #f8f9fa;
}

html.dark .about-us {
  background-color: #f8f9fa;
}

.main-content {
  padding-top: 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

/* 页面标题区域 */
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 4rem 0;
  text-align: center;
}

.page-title {
  font-size: 3rem;
  font-weight: bold;
  margin-bottom: 1rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.page-subtitle {
  font-size: 1.2rem;
  opacity: 0.9;
}

/* 通用样式 */
.section-header {
  text-align: center;
  margin-bottom: 3rem;
}

.section-header h2 {
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 1rem;
}

.underline {
  width: 80px;
  height: 4px;
  background: linear-gradient(90deg, #667eea, #764ba2);
  margin: 0 auto;
  border-radius: 2px;
}

/* 大赛介绍区域 */
.intro-section {
  padding: 5rem 0;
  background: white;
}

.intro-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
}

.intro-text p {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #666;
  margin-bottom: 1.5rem;
}

.intro-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

.stat-item {
  text-align: center;
  padding: 2rem;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.stat-number {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.stat-label {
  font-size: 1rem;
  opacity: 0.9;
}

/* 组织架构区域 */
.organization-section {
  padding: 5rem 0;
  background: #f8f9fa;
}

.org-structure {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.org-item {
  background: white;
  padding: 2rem;
  border-radius: 15px;
  text-align: center;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.org-item:hover {
  transform: translateY(-5px);
}

.org-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.org-item h3 {
  font-size: 1.3rem;
  color: #333;
  margin-bottom: 1rem;
}

.org-item p {
  color: #666;
  line-height: 1.6;
}

/* 大赛特色区域 */
.features-section {
  padding: 5rem 0;
  background: white;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 2.5rem;
  border-radius: 15px;
  text-align: center;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 1.5rem;
}

.feature-card h3 {
  font-size: 1.4rem;
  margin-bottom: 1rem;
}

.feature-card p {
  line-height: 1.6;
  opacity: 0.9;
}

/* 开发成员区域 */
.team-section {
  padding: 5rem 0 2rem 0;
  background: #f8f9fa;
  margin-top: 2rem;
  margin-bottom: 2rem;
}

.team-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  justify-items: center;
}

.team-member {
  background: linear-gradient(white, white) padding-box,
    linear-gradient(135deg, #667eea 0%, #764ba2 100%) border-box;
  color: #185a9d;
  border-radius: 15px;
  border: 3px solid transparent;
  box-shadow: 0 10px 30px rgba(24, 90, 157, 0.08);
  padding: 3rem 2rem 2.5rem 2rem;
  text-align: center;
  width: 100%;
  max-width: 320px;
  transition: transform 0.3s;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.team-member:hover {
  transform: translateY(-5px) scale(1.03);
}

.avatar-gradient {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  padding: 4px;
  display: inline-block;
  margin-bottom: 1.5rem;
}
.avatar {
  width: 144px;
  height: 144px;
  border-radius: 50%;
  object-fit: cover;
  background: #fff;
  display: block;
}

.member-name {
  font-size: 1.3rem;
  font-weight: bold;
  color: #185a9d;
  margin-bottom: 0.7rem;
  letter-spacing: 1px;
}

.member-role {
  font-size: 1.1rem;
  color: #333;
  opacity: 0.95;
}

/* 联系我们区域 */
.contact-section {
  padding: 5rem 0;
  background: #fff;
}

.contact-content {
  display: flex;
  justify-content: center;
}

.contact-info {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
  max-width: 1200px;
}

.contact-item {
  display: flex;
  align-items: center;
  background: white;
  padding: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.contact-icon {
  font-size: 2rem;
  margin-right: 1rem;
}

.contact-details h4 {
  color: #333;
  margin-bottom: 0.5rem;
}

.contact-details p {
  color: #666;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 0 1rem;
  }

  .page-title {
    font-size: 2rem;
  }

  .intro-content {
    grid-template-columns: 1fr;
    gap: 2rem;
  }

  .intro-stats {
    grid-template-columns: 1fr;
  }

  .contact-info {
    grid-template-columns: 1fr;
  }

  .org-structure {
    grid-template-columns: 1fr;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 900px) {
  .team-grid {
    grid-template-columns: 1fr 1fr;
  }
  .contact-info {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 600px) {
  .team-grid {
    grid-template-columns: 1fr;
  }
  .contact-info {
    grid-template-columns: 1fr;
  }
}

/* 500px以下的媒体查询，放在最后，确保优先级最高 */
@media (max-width: 500px) {
  .container {
    padding: 0 0.5rem;
  }
  .main-content {
    padding-top: 0;
  }
  .hero-section {
    padding: 2rem 0 1.5rem 0;
  }
  .page-title {
    font-size: 1.4rem;
    margin-bottom: 0.5rem;
  }
  .page-subtitle {
    font-size: 1rem;
  }
  .section-header h2 {
    font-size: 1.3rem;
  }
  .underline {
    width: 40px;
    height: 2px;
  }
  .intro-section,
  .organization-section,
  .features-section,
  .team-section,
  .contact-section {
    padding: 2rem 0 1rem 0;
  }
  .intro-content {
    gap: 1rem;
  }
  .intro-text p {
    font-size: 0.95rem;
    margin-bottom: 1rem;
  }
  .intro-stats {
    grid-template-columns: 1fr 1fr 1fr !important;
    gap: 0.5rem;
  }
  .stat-item {
    padding: 0.7rem 0.2rem;
  }
  .stat-number {
    font-size: 1.05rem;
  }
  .stat-label {
    font-size: 0.85rem;
  }
  .org-structure {
    grid-template-columns: 1fr 1fr !important;
    gap: 1rem;
  }
  .org-item {
    padding: 1rem;
  }
  .org-icon {
    font-size: 2rem;
  }
  .features-grid {
    grid-template-columns: 1fr 1fr !important;
    gap: 1rem;
  }
  .feature-card {
    padding: 1.2rem;
  }
  .feature-icon {
    font-size: 2rem;
    margin-bottom: 0.7rem;
  }
  .feature-card h3 {
    font-size: 1.1rem;
  }
  .feature-card p {
    font-size: 0.95rem;
  }
  .team-section {
    padding: 2rem 0 1rem 0;
    margin-top: 1rem;
    margin-bottom: 1rem;
  }
  .team-grid {
    grid-template-columns: 1fr 1fr !important;
    gap: 1rem;
  }
  .team-member {
    padding: 1rem 0.5rem 1rem 0.5rem;
    max-width: 180px;
  }
  .avatar {
    width: 72px;
    height: 72px;
  }
  .member-name {
    font-size: 1.05rem;
    margin-bottom: 0.25rem;
  }
  .member-role {
    font-size: 0.95rem;
  }
  .contact-content {
    flex-direction: column;
    align-items: stretch;
  }
  .contact-info {
    gap: 1rem;
  }
  .contact-item {
    padding: 1rem;
  }
  .contact-icon {
    font-size: 1.3rem;
    margin-right: 0.5rem;
  }
  .contact-details h4 {
    font-size: 1rem;
  }
  .contact-details p {
    font-size: 0.95rem;
  }
}
</style>
