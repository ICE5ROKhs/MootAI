<template>
  <div class="register-form-overlay" @click.self="$emit('close')">
    <div class="register-form-container scale-in">
      <div class="register-form">
        <!-- 关闭按钮 -->
        <div class="close-btn" @click.stop="handleClose">
          <span>×</span>
        </div>
        
        <!-- 装饰背景 -->
        <div class="form-decoration"></div>
        
        <!-- 标题 -->
        <div class="form-header">
          <div class="form-icon">✨</div>
          <h2 class="form-title">注册</h2>
          <p class="form-subtitle">创建您的账号</p>
        </div>
        
        <!-- 注册表单 -->
        <div class="form-content">
          <el-form :model="registerForm" label-width="0" :rules="rules" ref="formRef">
            <el-form-item prop="username">
              <div class="input-wrapper">
                <span class="input-icon">👤</span>
                <el-input
                  v-model="registerForm.username"
                  placeholder="请输入用户名（3-50个字符）"
                  clearable
                  class="custom-input"
                />
              </div>
            </el-form-item>
            <el-form-item prop="password">
              <div class="input-wrapper">
                <span class="input-icon">🔒</span>
                <el-input
                  v-model="registerForm.password"
                  type="password"
                  placeholder="请输入密码（至少6个字符）"
                  show-password
                  clearable
                  class="custom-input"
                />
              </div>
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <div class="input-wrapper">
                <span class="input-icon">🔐</span>
                <el-input
                  v-model="registerForm.confirmPassword"
                  type="password"
                  placeholder="请再次输入密码"
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
              @click="handleRegister" 
              :loading="loading" 
              class="register-btn"
            >
              <span v-if="!loading">注册</span>
              <span v-else>注册中...</span>
            </el-button>
          </div>
        </div>
        
        <!-- 底部链接 -->
        <div class="form-footer">
          <span class="login-link" @click="$emit('switch-to-login')">
            已有账号？<span class="link-text">立即登录</span>
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

const emit = defineEmits(['close', 'switch-to-login'])

const router = useRouter()
const formRef = ref(null)

const handleClose = () => {
  emit('close')
}
const loading = ref(false)

// 注册表单
const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

// 表单验证规则
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.value.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度在 3 到 50 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 100, message: '密码长度在 6 到 100 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 注册
const handleRegister = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    const response = await request.post('/auth/register', {
      username: registerForm.value.username,
      password: registerForm.value.password
    })
    
    if (response.code === 200 && response.data) {
      // 保存token和用户信息
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('username', response.data.username)
      localStorage.setItem('userId', response.data.userId)
      
      ElMessage.success('注册成功，已自动登录')
      // 关闭注册窗口
      emit('close')
      // 延迟跳转，确保弹窗关闭动画完成
      setTimeout(() => {
        router.push('/home')
      }, 200)
    } else {
      ElMessage.error(response.message || '注册失败')
    }
  } catch (error) {
    console.error('注册错误:', error)
    ElMessage.error(error.response?.data?.message || error.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-form-overlay {
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

.register-form-container {
  width: 260px;
  min-width: 260px;
  max-width: 280px;
}

.register-form {
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

.register-btn {
  width: 100%;
  height: 32px;
  font-size: 12px;
  font-weight: 600;
  border-radius: var(--radius-md);
  background: linear-gradient(135deg, var(--primary-purple), var(--primary-purple-light));
  border: none;
  transition: all var(--transition-base);
}

.register-btn:hover {
  background: linear-gradient(135deg, var(--primary-purple-dark), var(--primary-purple));
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.register-btn:active {
  transform: translateY(0);
}

.form-footer {
  margin-top: 8px;
  text-align: center;
  position: relative;
  z-index: 1;
}

.login-link {
  font-size: 11px;
}

.login-link {
  font-size: 14px;
  color: var(--text-secondary);
  cursor: pointer;
  transition: color var(--transition-fast);
}

.login-link:hover {
  color: var(--primary-purple);
}

.link-text {
  color: var(--primary-purple);
  font-weight: 500;
  text-decoration: underline;
}
</style>
