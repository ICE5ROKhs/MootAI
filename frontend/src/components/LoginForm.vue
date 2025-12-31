<template>
  <div class="login-form-overlay" @click.self="$emit('close')">
    <div class="login-form-container scale-in">
      <div class="login-form">
        <!-- 关闭按钮 -->
        <div class="close-btn" @click.stop="handleClose">
          <span>×</span>
        </div>
        
        <!-- 装饰背景 -->
        <div class="form-decoration"></div>
        
        <!-- 标题 -->
        <div class="form-header">
          <div class="form-icon">🔐</div>
          <h2 class="form-title">登录</h2>
          <p class="form-subtitle">欢迎回来</p>
        </div>
        
        <!-- 登录表单 -->
        <div class="form-content">
          <el-form :model="loginForm" label-width="0" :rules="rules" ref="formRef">
            <el-form-item prop="username">
              <div class="input-wrapper">
                <span class="input-icon">👤</span>
                <el-input
                  v-model="loginForm.username"
                  placeholder="请输入用户名"
                  clearable
                  class="custom-input"
                />
              </div>
            </el-form-item>
            <el-form-item prop="password">
              <div class="input-wrapper">
                <span class="input-icon">🔒</span>
                <el-input
                  v-model="loginForm.password"
                  type="password"
                  placeholder="请输入密码"
                  show-password
                  clearable
                  class="custom-input"
                />
              </div>
            </el-form-item>
          </el-form>
          <div class="form-actions">
            <el-button 
              type="primary" 
              @click="handleLogin" 
              :loading="loading" 
              class="login-btn"
            >
              <span v-if="!loading">登录</span>
              <span v-else>登录中...</span>
            </el-button>
          </div>
        </div>
        
        <!-- 底部链接 -->
        <div class="form-footer">
          <span class="register-link" @click="$emit('switch-to-register')">
            还没有账号？<span class="link-text">立即注册</span>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { useRouter } from 'vue-router'

const emit = defineEmits(['close', 'switch-to-register'])

const router = useRouter()
const formRef = ref(null)

const handleClose = () => {
  emit('close')
}
const loading = ref(false)

// 登录表单
const loginForm = ref({
  username: '',
  password: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
  ]
}

// 登录
const handleLogin = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    const response = await request.post('/auth/login', {
      username: loginForm.value.username,
      password: loginForm.value.password
    })
    
    if (response.code === 200 && response.data) {
      // 保存token和用户信息
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('username', response.data.username)
      localStorage.setItem('userId', response.data.userId)
      
      ElMessage.success('登录成功')
      // 关闭登录窗口
      emit('close')
      // 延迟跳转，确保弹窗关闭动画完成
      setTimeout(() => {
        router.push('/home')
      }, 200)
    } else {
      ElMessage.error(response.message || '登录失败')
    }
  } catch (error) {
    console.error('登录错误:', error)
    ElMessage.error(error.response?.data?.message || error.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-form-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(2px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn var(--transition-base);
  pointer-events: auto;
}

.login-form-container {
  width: 240px;
  min-width: 240px;
  max-width: 280px;
}

.login-form {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: 14px 12px;
  box-shadow: var(--shadow-xl);
  position: relative;
  overflow: hidden;
  border: 1px solid var(--border-color);
}

.form-decoration {
  position: absolute;
  top: -50%;
  right: -20%;
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, var(--primary-purple-lightest), transparent);
  border-radius: 50%;
  opacity: 0.2;
  animation: float 6s ease-in-out infinite;
  z-index: 0;
}

.close-btn {
  position: absolute;
  top: 6px;
  right: 6px;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 50%;
  transition: all var(--transition-fast);
  z-index: 100;
  pointer-events: auto;
}

.close-btn:hover {
  background: var(--bg-tertiary);
  transform: rotate(90deg);
}

.close-btn span {
  font-size: 16px;
  color: var(--text-secondary);
  line-height: 1;
  transition: color var(--transition-fast);
}

.close-btn:hover span {
  color: var(--primary-purple);
}

.form-header {
  text-align: center;
  margin-bottom: 10px;
  position: relative;
  z-index: 1;
}

.form-icon {
  font-size: 18px;
  margin-bottom: 6px;
  animation: float 3s ease-in-out infinite;
}

.form-title {
  font-size: 14px;
  font-weight: bold;
  margin: 0 0 4px 0;
  background: linear-gradient(135deg, var(--primary-purple), var(--primary-purple-light));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.form-subtitle {
  font-size: 10px;
  color: var(--text-secondary);
  margin: 0;
}

.form-content {
  position: relative;
  z-index: 1;
}

:deep(.el-form-item) {
  margin-bottom: 8px;
}

:deep(.el-input__wrapper) {
  padding: 6px 10px;
  min-height: 32px;
  font-size: 12px;
}

:deep(.el-input__inner) {
  font-size: 12px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 16px;
  font-size: 18px;
  z-index: 1;
  color: var(--text-secondary);
}

:deep(.custom-input .el-input__wrapper) {
  padding-left: 48px;
  border-radius: var(--radius-md);
  box-shadow: 0 0 0 1px var(--border-color) inset;
  transition: all var(--transition-fast);
}

:deep(.custom-input .el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--primary-purple-light) inset;
}

:deep(.custom-input .el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px var(--primary-purple) inset;
}

.form-actions {
  margin-top: 10px;
}

.login-btn {
  width: 100%;
  height: 32px;
  font-size: 12px;
  font-weight: 600;
  border-radius: var(--radius-md);
  background: linear-gradient(135deg, var(--primary-purple), var(--primary-purple-light));
  border: none;
  transition: all var(--transition-base);
}

.login-btn:hover {
  background: linear-gradient(135deg, var(--primary-purple-dark), var(--primary-purple));
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.login-btn:active {
  transform: translateY(0);
}

.form-footer {
  margin-top: 8px;
  text-align: center;
  position: relative;
  z-index: 1;
}

.register-link {
  font-size: 11px;
}

.register-link {
  font-size: 14px;
  color: var(--text-secondary);
  cursor: pointer;
  transition: color var(--transition-fast);
}

.register-link:hover {
  color: var(--primary-purple);
}

.link-text {
  color: var(--primary-purple);
  font-weight: 500;
  text-decoration: underline;
}
</style>
