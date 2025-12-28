<template>
  <div class="courtroom-page" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">
    <!-- 左侧边栏 -->
    <div 
      class="sidebar" 
      :class="{ 'sidebar-visible': sidebarVisible }"
      @mouseenter="sidebarVisible = true"
      @mouseleave="handleSidebarLeave"
    >
      <div class="sidebar-content">
        <!-- 返回按钮 -->
        <div class="sidebar-item sidebar-header" @click="goHome">
          <span class="sidebar-icon">←</span>
          <span class="sidebar-text">返回首页</span>
        </div>
        
        <div class="sidebar-divider"></div>
        
        <!-- 庭前准备 -->
        <div class="sidebar-section">
          <div 
            class="sidebar-item" 
            :class="{ 'active': activeTab === 'pretrial' }"
            @click="navigateToTab('pretrial')"
          >
            <span class="sidebar-icon">📋</span>
            <span class="sidebar-text">庭前准备</span>
          </div>
          <div 
            v-if="activeTab === 'pretrial'"
            class="sidebar-submenu"
          >
            <div 
              class="sidebar-subitem"
              :class="{ 'active': pretrialSubTab === 'basic' }"
              @click="navigateToSubTab('basic')"
            >
              <span class="sidebar-text">基本信息</span>
            </div>
            <div 
              class="sidebar-subitem"
              :class="{ 'active': pretrialSubTab === 'strategy' }"
              @click="navigateToSubTab('strategy')"
            >
              <span class="sidebar-text">诉讼策略</span>
            </div>
          </div>
        </div>
        
        <!-- 庭中辩论 -->
        <div 
          class="sidebar-item" 
          :class="{ 'active': activeTab === 'debate' }"
          @click="navigateToTab('debate')"
        >
          <span class="sidebar-icon">⚖️</span>
          <span class="sidebar-text">庭中辩论</span>
        </div>
        
        <!-- 庭后宣判 -->
        <div 
          class="sidebar-item" 
          :class="{ 'active': activeTab === 'verdict' }"
          @click="navigateToTab('verdict')"
        >
          <span class="sidebar-icon">📜</span>
          <span class="sidebar-text">庭后宣判</span>
        </div>
      </div>
    </div>
    
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">模拟法庭</h1>
    </div>
    
    <div class="courtroom-wrapper">
      <!-- 顶部导航 -->
      <div class="nav-tabs">
        <el-button
          :type="activeTab === 'pretrial' ? 'primary' : ''"
          :class="{ 'active': activeTab === 'pretrial' }"
          @click="activeTab = 'pretrial'"
          class="nav-btn"
        >
          庭前准备
        </el-button>
        <el-button
          :type="activeTab === 'debate' ? 'primary' : ''"
          :class="{ 'active': activeTab === 'debate' }"
          @click="activeTab = 'debate'"
          class="nav-btn"
        >
          庭中辩论
        </el-button>
        <el-button
          :type="activeTab === 'verdict' ? 'primary' : ''"
          :class="{ 'active': activeTab === 'verdict' }"
          @click="activeTab = 'verdict'"
          class="nav-btn"
        >
          庭后宣判
        </el-button>
      </div>

      <!-- 内容区域 -->
      <div class="content-area">
        <PreTrial 
          v-if="activeTab === 'pretrial'" 
          ref="preTrialRef"
          :active-sub-tab="pretrialSubTab"
          @update:active-sub-tab="pretrialSubTab = $event"
        />
        <Debate v-else-if="activeTab === 'debate'" />
        <Verdict v-else-if="activeTab === 'verdict'" />
      </div>
    </div>
    
    <!-- 回到顶部按钮 -->
    <div 
      v-show="showBackToTop" 
      class="back-to-top" 
      @click="scrollToTop"
    >
      🚀
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PreTrial from '@/components/PreTrial.vue'
import Debate from '@/components/Debate.vue'
import Verdict from '@/components/Verdict.vue'

const route = useRoute()
const router = useRouter()
const activeTab = ref('pretrial')
const pretrialSubTab = ref('basic')
const preTrialRef = ref(null)

// 侧栏显示状态
const sidebarVisible = ref(false)
const sidebarTimer = ref(null)

// 鼠标移动处理
const handleMouseMove = (event) => {
  // 如果鼠标在左侧 50px 内，显示侧栏
  if (event.clientX < 50) {
    sidebarVisible.value = true
    if (sidebarTimer.value) {
      clearTimeout(sidebarTimer.value)
      sidebarTimer.value = null
    }
  }
}

// 鼠标离开页面
const handleMouseLeave = () => {
  sidebarTimer.value = setTimeout(() => {
    sidebarVisible.value = false
  }, 300)
}

// 鼠标离开侧栏
const handleSidebarLeave = () => {
  sidebarTimer.value = setTimeout(() => {
    sidebarVisible.value = false
  }, 300)
}

// 导航到主标签
const navigateToTab = (tab) => {
  activeTab.value = tab
  if (tab === 'pretrial') {
    pretrialSubTab.value = 'basic'
  }
}

// 导航到子标签
const navigateToSubTab = (subTab) => {
  if (activeTab.value === 'pretrial') {
    pretrialSubTab.value = subTab
    if (preTrialRef.value) {
      preTrialRef.value.setActiveTab(subTab)
    }
  }
}

// 回到顶部按钮显示状态
const showBackToTop = ref(false)

// 监听滚动
const handleScroll = () => {
  showBackToTop.value = window.scrollY > 300
}

// 回到顶部
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

// 返回首页
const goHome = () => {
  router.push({ name: 'home' })
}

// 如果路由中有tab参数，切换到对应标签
onMounted(() => {
  if (route.query.tab) {
    activeTab.value = route.query.tab
  }
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  if (sidebarTimer.value) {
    clearTimeout(sidebarTimer.value)
  }
})
</script>

<style scoped>
.courtroom-page {
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  overflow: auto;
  position: relative;
}

/* 左侧边栏 */
.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  width: 30vw;
  max-width: 300px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  transform: translateX(-100%);
  transition: transform 0.3s ease;
  z-index: 1000;
  overflow-y: auto;
}

.sidebar-visible {
  transform: translateX(0);
}

.sidebar-content {
  padding: 15px 0;
}

.sidebar-item {
  display: flex;
  align-items: center;
  padding: 3px 15px;
  cursor: pointer;
  transition: all 0.3s;
  color: #333;
  font-size: 6px;
}

.sidebar-item:hover {
  background: #f0f0f0;
  color: #409eff;
}

.sidebar-item.active {
  background: #e6f4ff;
  color: #409eff;
  border-left: 3px solid #409eff;
}

.sidebar-header {
  font-weight: 600;
  font-size: 6px;
}

.sidebar-icon {
  margin-right: 8px;
  font-size: 8px;
  width: 15px;
  text-align: center;
}

.sidebar-text {
  flex: 1;
  font-size: 6px;
}

.sidebar-divider {
  height: 1px;
  background: #e0e0e0;
  margin: 3px 0;
}

.sidebar-section {
  margin-bottom: 3px;
}

.sidebar-submenu {
  padding-left: 25px;
  background: #f9f9f9;
}

.sidebar-subitem {
  padding: 3px 15px;
  cursor: pointer;
  transition: all 0.3s;
  color: #666;
  font-size: 6px;
}

.sidebar-subitem:hover {
  background: #f0f0f0;
  color: #409eff;
}

.sidebar-subitem.active {
  background: #e6f4ff;
  color: #409eff;
  font-weight: 600;
}

/* 页面标题 */
.page-header {
  position: relative;
  text-align: center;
  margin-bottom: 30px;
  padding: 20px 0;
}

.page-title {
  font-size: 20px;
  color: white;
  margin: 0;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.courtroom-wrapper {
  max-width: 800px;
  margin: 0 auto;
}

.nav-tabs {
  display: flex;
  justify-content: center;
  gap: 5px;
  margin-bottom: 20px;
  background: white;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
}

.nav-btn {
  flex: 1;
  height: 36px;
  font-size: 8px;
  border-radius: 6px;
  padding: 0 10px;
  transition: all 0.3s;
}

.nav-btn:hover {
  transform: translateY(-2px);
}

.nav-btn.active {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border-color: #409eff;
  color: white;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.content-area {
  background: white;
  border-radius: 8px;
  padding: 20px;
  width: 100%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  min-height: 400px;
}

.coming-soon {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  color: #909399;
  font-size: 16px;
  padding: 40px;
}

/* 回到顶部按钮 */
.back-to-top {
  position: fixed;
  right: 20px;
  bottom: 20px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
  transition: all 0.3s;
  z-index: 1000;
}

.back-to-top:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.5);
}
</style>

