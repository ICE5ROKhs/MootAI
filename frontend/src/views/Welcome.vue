<template>
  <div class="welcome-page" @click="showLogin">
    <!-- 动态紫色背景 -->
    <div class="animated-background">
      <div class="bg-gradient"></div>
      <div class="bg-particles">
        <div v-for="i in 20" :key="i" class="particle" :style="getParticleStyle(i)"></div>
      </div>
    </div>
    
    <!-- 主要内容 -->
    <div class="welcome-content" :class="{ 'fade-out': showLoginForm || showRegisterForm }">
      <!-- Logo和标题区域 -->
      <div class="hero-section fade-in">
        <div class="logo-container float">
          <div class="logo-icon">⚖️</div>
          <div class="logo-glow"></div>
        </div>
        <h1 class="main-title">
          <span class="title-text">MootAI</span>
          <span class="title-subtitle">AI模拟法庭</span>
        </h1>
      </div>
      
      <!-- 装饰元素 -->
      <div class="decorative-elements">
        <div class="deco-item deco-1 float" style="animation-delay: 0s">📜</div>
        <div class="deco-item deco-2 float" style="animation-delay: 1s">⚖️</div>
        <div class="deco-item deco-3 float" style="animation-delay: 2s">🔨</div>
      </div>
      
      <!-- 提示文字 -->
      <div class="click-hint pulse">
        <div class="hint-icon">👆</div>
        <p>点击任意位置开始体验</p>
      </div>
    </div>
    
    <!-- 登录弹窗 -->
    <transition name="modal">
      <div class="modal-overlay" v-if="showLoginForm" @click.self="closeLogin">
        <LoginForm @close="closeLogin" @switch-to-register="switchToRegister" />
      </div>
    </transition>
    
    <!-- 注册弹窗 -->
    <transition name="modal">
      <div class="modal-overlay" v-if="showRegisterForm" @click.self="closeRegister">
        <RegisterForm @close="closeRegister" @switch-to-login="switchToLogin" />
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import LoginForm from '@/components/LoginForm.vue'
import RegisterForm from '@/components/RegisterForm.vue'

const showLoginForm = ref(false)
const showRegisterForm = ref(false)

const showLogin = () => {
  if (!showLoginForm.value && !showRegisterForm.value) {
    showLoginForm.value = true
  }
}

const closeLogin = () => {
  showLoginForm.value = false
}

const closeRegister = () => {
  showRegisterForm.value = false
}

const switchToRegister = () => {
  showLoginForm.value = false
  setTimeout(() => {
    showRegisterForm.value = true
  }, 200)
}

const switchToLogin = () => {
  showRegisterForm.value = false
  setTimeout(() => {
    showLoginForm.value = true
  }, 200)
}

const getParticleStyle = (index) => {
  const size = Math.random() * 4 + 2
  const left = Math.random() * 100
  const delay = Math.random() * 5
  const duration = Math.random() * 3 + 2
  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${left}%`,
    animationDelay: `${delay}s`,
    animationDuration: `${duration}s`
  }
}
</script>

<style scoped>
.welcome-page {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
  cursor: pointer;
  background: var(--bg-secondary);
}

/* 动态背景 */
.animated-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}

.bg-gradient {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    135deg,
    var(--primary-purple-lightest) 0%,
    var(--primary-purple-lighter) 25%,
    var(--primary-purple-light) 50%,
    var(--primary-purple) 75%,
    var(--primary-purple-dark) 100%
  );
  background-size: 400% 400%;
  animation: gradientShift 15s ease infinite;
}

.bg-particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.particle {
  position: absolute;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 50%;
  animation: float 3s ease-in-out infinite;
  box-shadow: 0 0 10px rgba(139, 92, 246, 0.5);
}

/* 主要内容 */
.welcome-content {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 40px;
  transition: opacity var(--transition-base);
}

.welcome-content.fade-out {
  opacity: 0.2;
}

/* Hero区域 */
.hero-section {
  text-align: center;
  margin-bottom: 0;
  animation: fadeIn 1s ease;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.logo-container {
  position: relative;
  display: inline-block;
  margin-bottom: 30px;
}

.logo-icon {
  font-size: 80px;
  filter: drop-shadow(0 8px 16px rgba(139, 92, 246, 0.4));
  position: relative;
  z-index: 1;
}

.logo-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 140px;
  height: 140px;
  background: radial-gradient(circle, rgba(139, 92, 246, 0.3), transparent 70%);
  border-radius: 50%;
  animation: pulse 2s ease-in-out infinite;
}

.main-title {
  margin: 0 0 20px 0;
}

.title-text {
  display: block;
  font-size: 48px;
  font-weight: bold;
  background: linear-gradient(135deg, var(--primary-purple), var(--white));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 4px 8px rgba(139, 92, 246, 0.3);
  letter-spacing: 3px;
  margin-bottom: 10px;
}

.title-subtitle {
  display: block;
  font-size: 20px;
  color: var(--text-white);
  font-weight: 300;
  letter-spacing: 5px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.main-description {
  font-size: 16px;
  color: var(--text-white);
  line-height: 1.8;
  margin: 24px 0 0 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  opacity: 0.95;
}

/* 装饰元素 */
.decorative-elements {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.deco-item {
  position: absolute;
  font-size: 48px;
  filter: drop-shadow(0 4px 8px rgba(139, 92, 246, 0.3));
  opacity: 0.6;
}

.deco-1 {
  top: 15%;
  left: 10%;
}

.deco-2 {
  top: 20%;
  right: 15%;
}

.deco-3 {
  bottom: 25%;
  left: 20%;
}

/* 提示文字 */
.click-hint {
  text-align: center;
  color: var(--text-white);
  animation: pulse 2s ease-in-out infinite;
  margin-top: auto;
  padding-bottom: 40px;
}

.hint-icon {
  font-size: 24px;
  margin-bottom: 8px;
  animation: float 2s ease-in-out infinite;
}

.click-hint p {
  font-size: 14px;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  font-weight: 300;
}

/* 弹窗覆盖层 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

/* 弹窗过渡动画 */
.modal-enter-active,
.modal-leave-active {
  transition: all var(--transition-base);
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-overlay > *,
.modal-leave-to .modal-overlay > * {
  transform: scale(0.9) translateY(20px);
}

.modal-enter-to .modal-overlay > *,
.modal-leave-from .modal-overlay > * {
  transform: scale(1) translateY(0);
}
</style>
