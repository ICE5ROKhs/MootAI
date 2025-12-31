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
    
    <!-- 主内容区 -->
    <div class="courtroom-wrapper">
      <!-- 页面标题 -->
      <div class="page-header fade-in">
        <h1 class="page-title">
          <span class="title-icon">⚖️</span>
          <span class="title-text">模拟法庭</span>
        </h1>
        <p class="page-subtitle">智能诉讼审判模拟系统</p>
      </div>
      
      <!-- 顶部导航标签 -->
      <div class="nav-tabs slide-in-right">
        <div
          v-for="tab in tabs"
          :key="tab.key"
          class="nav-tab"
          :class="{ 'active': activeTab === tab.key }"
          @click="navigateToTab(tab.key)"
        >
          <span class="tab-icon">{{ tab.icon }}</span>
          <span class="tab-text">{{ tab.name }}</span>
          <div class="tab-indicator"></div>
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="content-area fade-in">
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
    <transition name="fade">
      <div 
        v-show="showBackToTop" 
        class="back-to-top" 
        @click="scrollToTop"
      >
        <span class="back-icon">↑</span>
      </div>
    </transition>
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

const tabs = [
  { key: 'pretrial', name: '庭前准备', icon: '📋' },
  { key: 'debate', name: '庭中辩论', icon: '⚖️' },
  { key: 'verdict', name: '庭后宣判', icon: '📜' }
]

// 侧栏显示状态
const sidebarVisible = ref(false)
const sidebarTimer = ref(null)

// 鼠标移动处理
const handleMouseMove = (event) => {
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
  min-height: calc(100vh - 64px);
  background: var(--bg-secondary);
  position: relative;
  padding: 0;
}

/* 左侧边栏 */
.sidebar {
  position: fixed;
  left: 0;
  top: 40px;
  height: calc(100vh - 40px);
  width: 80px;
  background: var(--bg-primary);
  box-shadow: var(--shadow-lg);
  transform: translateX(-100%);
  transition: transform var(--transition-base);
  z-index: 100;
  border-right: 1px solid var(--border-color);
}

.sidebar-visible {
  transform: translateX(0);
}

.sidebar-content {
  padding: 8px 0;
  height: 100%;
  overflow-y: auto;
}

.sidebar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  padding: 8px 4px;
  cursor: pointer;
  transition: all var(--transition-fast);
  color: var(--text-primary);
  position: relative;
  font-size: var(--font-size-xs);
  text-align: center;
}

.sidebar-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: var(--primary-purple);
  transform: scaleY(0);
  transition: transform var(--transition-fast);
}

.sidebar-item:hover {
  background: var(--bg-overlay);
  color: var(--primary-purple);
}

.sidebar-item.active {
  background: var(--bg-overlay);
  color: var(--primary-purple);
  font-weight: 600;
}

.sidebar-item.active::before {
  transform: scaleY(1);
}

.sidebar-header {
  font-weight: 600;
  margin-bottom: 8px;
}

.sidebar-icon {
  font-size: 16px;
  width: auto;
  text-align: center;
}

.sidebar-text {
  font-size: 10px;
  font-weight: 500;
  line-height: 1.2;
}

.sidebar-divider {
  height: 1px;
  background: var(--border-color);
  margin: 6px 8px;
}

.sidebar-section {
  margin-bottom: 8px;
}

.sidebar-submenu {
  padding-left: 0;
  margin-top: 2px;
}

.sidebar-subitem {
  padding: 6px 4px;
  font-size: 9px;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--transition-fast);
  position: relative;
  text-align: center;
}

.sidebar-subitem::before {
  content: '';
  position: absolute;
  left: 4px;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: var(--text-tertiary);
  transition: all var(--transition-fast);
}

.sidebar-subitem:hover {
  color: var(--primary-purple);
  background: var(--bg-secondary);
}

.sidebar-subitem.active {
  color: var(--primary-purple);
  font-weight: 500;
}

.sidebar-subitem.active::before {
  background: var(--primary-purple);
  width: 5px;
  height: 5px;
}

/* 主内容区 */
.courtroom-wrapper {
  margin-left: 0;
  padding: 16px;
  max-width: 100%;
  margin: 0 auto;
  transition: margin-left var(--transition-base);
  width: 100%;
}

/* 页面标题 */
.page-header {
  text-align: center;
  margin-bottom: 16px;
  padding: 16px 0;
  background: linear-gradient(135deg, var(--primary-purple) 0%, var(--primary-purple-light) 100%);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-md);
  position: relative;
  overflow: hidden;
}

.page-header::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -10%;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1), transparent);
  border-radius: 50%;
  animation: float 6s ease-in-out infinite;
}

.page-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-bottom: 12px;
  position: relative;
  z-index: 1;
}

.title-icon {
  font-size: 20px;
  animation: float 3s ease-in-out infinite;
}

.title-text {
  font-size: 16px;
  font-weight: bold;
  color: var(--text-white);
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.page-subtitle {
  font-size: 11px;
  color: var(--text-white);
  opacity: 0.9;
  margin: 0;
  position: relative;
  z-index: 1;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

/* 导航标签 */
.nav-tabs {
  display: flex;
  gap: 6px;
  margin-bottom: 16px;
  background: var(--bg-primary);
  padding: 4px;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border-color);
}

.nav-tab {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  padding: 5px 10px;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-fast);
  color: var(--text-secondary);
  position: relative;
  font-weight: 500;
  font-size: var(--font-size-sm);
}

.nav-tab:hover {
  background: var(--bg-overlay);
  color: var(--primary-purple);
  transform: translateY(-2px);
}

.nav-tab.active {
  background: linear-gradient(135deg, var(--primary-purple), var(--primary-purple-light));
  color: var(--text-white);
  box-shadow: var(--shadow-md);
}

.tab-icon {
  font-size: 12px;
}

.tab-text {
  font-size: var(--font-size-xs);
}

.tab-indicator {
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  height: 3px;
  background: var(--text-white);
  border-radius: 2px;
  opacity: 0;
  transition: opacity var(--transition-fast);
}

.nav-tab.active .tab-indicator {
  opacity: 1;
}

/* 内容区域 */
.content-area {
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  padding: 16px;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-color);
  min-height: 300px;
  width: 100%;
}

/* 回到顶部按钮 */
.back-to-top {
  position: fixed;
  right: 32px;
  bottom: 32px;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-purple), var(--primary-purple-light));
  color: var(--text-white);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: var(--shadow-lg);
  transition: all var(--transition-base);
  z-index: 1000;
  border: 2px solid var(--bg-primary);
}

.back-to-top:hover {
  transform: translateY(-5px) scale(1.1);
  box-shadow: var(--shadow-xl);
}

.back-icon {
  font-size: 24px;
  font-weight: bold;
}

/* 过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity var(--transition-base);
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
