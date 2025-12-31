<template>
  <div class="home-page">
      <!-- 页面标题区域 -->
      <div class="page-banner fade-in">
        <div class="banner-content">
          <h1 class="banner-title">
            <span class="title-icon">⚖️</span>
            <span class="title-text">案件准备中心</span>
          </h1>
          <p class="banner-subtitle">上传案件资料，开始您的模拟法庭之旅</p>
        </div>
        <div class="banner-decoration">
          <div class="decoration-item float" style="animation-delay: 0s">📜</div>
          <div class="decoration-item float" style="animation-delay: 1s">⚖️</div>
          <div class="decoration-item float" style="animation-delay: 2s">🔨</div>
        </div>
      </div>
      
      <!-- 主要内容卡片 -->
      <div class="content-cards">
        <!-- 身份选择卡片 -->
        <div class="card-item slide-in-right" style="animation-delay: 0.1s">
          <div class="card-header">
            <div class="card-icon">👤</div>
            <h2 class="card-title">选择身份</h2>
          </div>
          <div class="card-body">
            <div class="identity-selector">
              <div
                class="identity-option"
                :class="{ 'active': selectedIdentity === 'plaintiff' }"
                @click="selectIdentity('plaintiff')"
              >
                <div class="option-icon">👨‍⚖️</div>
                <div class="option-label">原告</div>
                <div class="option-desc">提起诉讼的一方</div>
              </div>
              <div
                class="identity-option"
                :class="{ 'active': selectedIdentity === 'defendant' }"
                @click="selectIdentity('defendant')"
              >
                <div class="option-icon">⚖️</div>
                <div class="option-label">被告</div>
                <div class="option-desc">被起诉的一方</div>
              </div>
            </div>
            <div v-if="selectedIdentity" class="identity-badge">
              <span class="badge-icon">✓</span>
              <span>当前身份：{{ selectedIdentity === 'plaintiff' ? '原告' : '被告' }}</span>
            </div>
          </div>
        </div>
        
        <!-- 文件上传卡片 -->
        <div class="card-item slide-in-right" style="animation-delay: 0.2s">
          <div class="card-header">
            <div class="card-icon">📁</div>
            <h2 class="card-title">上传案件资料</h2>
            <el-button
              v-if="fileList.length > 0"
              text
              size="small"
              @click="clearAllFiles"
              class="clear-btn"
            >
              清空
            </el-button>
          </div>
          <div class="card-body">
            <div class="upload-area" @click="triggerUpload" :class="{ 'has-files': fileList.length > 0 }">
              <input
                ref="fileInput"
                type="file"
                multiple
                style="display: none"
                @change="handleFileChange"
              />
              <div v-if="fileList.length === 0" class="upload-placeholder">
                <div class="upload-icon">📤</div>
                <p class="upload-text">点击或拖拽文件到此处上传</p>
                <p class="upload-hint">支持多个文件同时上传</p>
              </div>
              <div v-else class="file-preview">
                <div class="file-count">{{ fileList.length }} 个文件</div>
                <div class="file-list">
                  <div
                    v-for="(file, index) in fileList"
                    :key="index"
                    class="file-tag"
                    :class="{ 'confirmed': filesConfirmed }"
                  >
                    <span class="file-icon">📄</span>
                    <span class="file-name">{{ file.name }}</span>
                    <el-button
                      v-if="!filesConfirmed"
                      text
                      size="small"
                      @click.stop="removeFile(index)"
                      class="remove-btn"
                    >
                      ×
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
            <el-button
              v-if="fileList.length > 0 && !filesConfirmed"
              type="primary"
              class="confirm-btn"
              @click="confirmFiles"
              :loading="confirming"
            >
              <span class="btn-icon">✓</span>
              确认文件
            </el-button>
          </div>
        </div>
        
        <!-- 案件描述卡片 -->
        <div class="card-item slide-in-right" style="animation-delay: 0.3s">
          <div class="card-header">
            <div class="card-icon">📝</div>
            <h2 class="card-title">案件描述</h2>
          </div>
          <div class="card-body">
            <el-input
              v-model="caseDescription"
              type="textarea"
              :autosize="{ minRows: 6, maxRows: 15 }"
              placeholder="案件描述将由系统自动生成..."
              :readonly="!filesConfirmed"
              class="description-input"
            />
            <div v-if="!filesConfirmed" class="description-tip">
              <span class="tip-icon">💡</span>
              <span>请先确认文件，系统将自动生成案件描述</span>
            </div>
            <div v-else class="description-tip success">
              <span class="tip-icon">✓</span>
              <span>您可以编辑上述内容进行调整</span>
            </div>
            <el-button
              v-if="filesConfirmed && caseDescription"
              type="primary"
              class="start-btn"
              @click="startSimulation"
              :loading="saving"
            >
              <span class="btn-icon">🚀</span>
              开始模拟
            </el-button>
          </div>
        </div>
      </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useCaseStore } from '@/stores/case'
import request from '@/utils/request'
const router = useRouter()
const caseStore = useCaseStore()

// 从store恢复状态
const selectedIdentity = ref(caseStore.selectedIdentity || '')
const fileList = ref(caseStore.fileList || [])
const filesConfirmed = ref(caseStore.filesConfirmed || false)
const caseDescription = ref(caseStore.caseDescription || '')
const confirming = ref(false)
const saving = ref(false)

// 文件输入引用
const fileInput = ref(null)

const selectIdentity = (identity) => {
  selectedIdentity.value = identity
  caseStore.setIdentity(identity)
  ElMessage.success(`已选择身份：${identity === 'plaintiff' ? '原告' : '被告'}`)
}

const triggerUpload = () => {
  fileInput.value?.click()
}

const handleFileChange = (event) => {
  const files = Array.from(event.target.files)
  files.forEach(file => {
    fileList.value.push({
      name: file.name,
      raw: file
    })
  })
  caseStore.setFileList(fileList.value)
  ElMessage.success(`已添加 ${files.length} 个文件`)
  
  if (filesConfirmed.value) {
    filesConfirmed.value = false
    caseStore.resetConfirm()
    caseStore.setCaseDescription('')
    caseDescription.value = ''
    ElMessage.info('检测到新文件，请重新确认文件以生成案件描述')
  }
  
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

const removeFile = (index) => {
  fileList.value.splice(index, 1)
  caseStore.setFileList(fileList.value)
  ElMessage.info('文件已移除')
}

const clearAllFiles = () => {
  if (fileList.value.length === 0) return
  fileList.value = []
  caseStore.setFileList([])
  filesConfirmed.value = false
  caseStore.resetConfirm()
  caseStore.setCaseDescription('')
  caseDescription.value = ''
  ElMessage.success('已清空所有文件')
}

const confirmFiles = async () => {
  if (fileList.value.length === 0) {
    ElMessage.warning('请先上传文件')
    return
  }
  
  if (!selectedIdentity.value) {
    ElMessage.warning('请先选择身份（原告/被告）')
    return
  }
  
  confirming.value = true
  ElMessage.info('正在分析文件，生成案件描述...')
  
  try {
    // 模拟AI处理，生成案件描述
    await new Promise(resolve => setTimeout(resolve, 1500))
    
    const description = `案件基本情况：
本案涉及一起合同纠纷案件。原告与被告于2023年1月签订了一份服务合同，约定被告向原告提供技术服务，合同金额为50万元。合同签订后，原告按约定支付了首付款30万元，但被告未能按合同约定提供服务，导致原告遭受经济损失。

争议焦点：
1. 被告是否存在违约行为
2. 原告的经济损失如何计算
3. 合同解除后的责任承担问题

相关法条：
《中华人民共和国民法典》第五百七十七条：当事人一方不履行合同义务或者履行合同义务不符合约定的，应当承担继续履行、采取补救措施或者赔偿损失等违约责任。
《中华人民共和国民法典》第五百六十三条：有下列情形之一的，当事人可以解除合同：（一）因不可抗力致使不能实现合同目的；（二）在履行期限届满前，当事人一方明确表示或者以自己的行为表明不履行主要债务；（三）当事人一方迟延履行主要债务，经催告后在合理期限内仍未履行；（四）当事人一方迟延履行债务或者有其他违约行为致使不能实现合同目的；（五）法律规定的其他情形。

案件要素：
- 合同类型：技术服务合同
- 合同金额：50万元
- 已支付金额：30万元
- 争议金额：30万元及违约金
- 合同签订时间：2023年1月
- 违约发生时间：2023年3月`
    
    caseDescription.value = description
    filesConfirmed.value = true
    caseStore.setCaseDescription(description)
    caseStore.confirmFiles()
    ElMessage.success('案件描述已生成，您可以进行编辑调整')
  } catch (error) {
    console.error('生成案件描述失败:', error)
    ElMessage.error('生成案件描述失败，请重试')
  } finally {
    confirming.value = false
  }
}

watch(caseDescription, (newVal) => {
  caseStore.setCaseDescription(newVal)
})

const startSimulation = async () => {
  if (!selectedIdentity.value) {
    ElMessage.warning('请先选择身份')
    return
  }
  if (fileList.value.length === 0) {
    ElMessage.warning('请先上传文件')
    return
  }
  if (!caseDescription.value) {
    ElMessage.warning('请先确认文件并生成案件描述')
    return
  }
  
  saving.value = true
  
  try {
    const fileNames = fileList.value.map(file => file.name)
    
    const response = await request.post('/cases', {
      identity: selectedIdentity.value,
      fileNames: fileNames,
      caseDescription: caseDescription.value
    })
    
    if (response.code === 200) {
      ElMessage.success('案件信息已保存')
      router.push({
        name: 'courtroom',
        query: {
          caseId: response.data.id,
          identity: selectedIdentity.value,
          description: caseDescription.value
        }
      })
    } else {
      ElMessage.error(response.message || '保存案件信息失败')
    }
  } catch (error) {
    console.error('保存案件信息失败:', error)
    ElMessage.error(error.response?.data?.message || error.message || '保存案件信息失败')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: var(--bg-secondary);
  padding-bottom: 40px;
}

/* 页面横幅 */
.page-banner {
  position: relative;
  background: linear-gradient(135deg, var(--primary-purple) 0%, var(--primary-purple-light) 100%);
  border-radius: var(--radius-xl);
  padding: 32px 24px;
  margin-bottom: 24px;
  overflow: hidden;
  box-shadow: var(--shadow-lg);
  max-width: 1400px;
  margin-left: auto;
  margin-right: auto;
}

.page-banner::before {
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

.banner-content {
  position: relative;
  z-index: 1;
  text-align: center;
  color: var(--text-white);
}

.banner-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-bottom: 16px;
}

.title-icon {
  font-size: 32px;
  animation: float 3s ease-in-out infinite;
}

.title-text {
  font-size: 24px;
  font-weight: bold;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.banner-subtitle {
  font-size: 14px;
  opacity: 0.9;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.banner-decoration {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  pointer-events: none;
}

.decoration-item {
  position: absolute;
  font-size: 32px;
  opacity: 0.3;
}

.decoration-item:nth-child(1) {
  top: 20%;
  left: 10%;
}

.decoration-item:nth-child(2) {
  top: 30%;
  right: 15%;
}

.decoration-item:nth-child(3) {
  bottom: 20%;
  left: 20%;
}

/* 内容卡片 */
.content-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
}

.card-item {
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  padding: 20px;
  box-shadow: var(--shadow-md);
  transition: all var(--transition-base);
  border: 1px solid var(--border-color);
}

.card-item:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid var(--bg-tertiary);
}

.card-icon {
  font-size: 20px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-overlay);
  border-radius: var(--radius-md);
}

.card-title {
  flex: 1;
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.clear-btn {
  color: var(--text-secondary);
  font-size: 14px;
}

.clear-btn:hover {
  color: var(--primary-purple);
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 身份选择器 */
.identity-selector {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 12px;
}

.identity-option {
  padding: 16px;
  border: 2px solid var(--border-color);
  border-radius: var(--radius-lg);
  text-align: center;
  cursor: pointer;
  transition: all var(--transition-base);
  background: var(--bg-primary);
}

.identity-option:hover {
  border-color: var(--primary-purple-light);
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.identity-option.active {
  border-color: var(--primary-purple);
  background: var(--bg-overlay);
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.1);
}

.option-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.option-label {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 6px;
}

.option-desc {
  font-size: 13px;
  color: var(--text-secondary);
}

.identity-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: var(--bg-overlay);
  border-radius: var(--radius-md);
  color: var(--primary-purple);
  font-weight: 500;
}

.badge-icon {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--primary-purple);
  color: var(--text-white);
  border-radius: 50%;
  font-size: 12px;
}

/* 文件上传区域 */
.upload-area {
  min-height: 120px;
  border: 2px dashed var(--border-color);
  border-radius: var(--radius-lg);
  padding: 24px;
  text-align: center;
  cursor: pointer;
  transition: all var(--transition-base);
  background: var(--bg-secondary);
}

.upload-area:hover {
  border-color: var(--primary-purple-light);
  background: var(--bg-overlay);
}

.upload-area.has-files {
  border-style: solid;
  border-color: var(--primary-purple-light);
  background: var(--bg-primary);
  padding: 24px;
  min-height: auto;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.upload-icon {
  font-size: 40px;
  margin-bottom: 12px;
  opacity: 0.6;
}

.upload-text {
  font-size: 14px;
  color: var(--text-primary);
  margin: 0 0 6px 0;
  font-weight: 500;
}

.upload-hint {
  font-size: 12px;
  color: var(--text-secondary);
  margin: 0;
}

.file-preview {
  width: 100%;
}

.file-count {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 12px;
  font-weight: 500;
}

.file-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.file-tag {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 14px;
  transition: all var(--transition-fast);
}

.file-tag.confirmed {
  background: var(--bg-overlay);
  border-color: var(--primary-purple-light);
}

.file-tag:hover {
  background: var(--bg-tertiary);
  transform: translateY(-2px);
}

.file-icon {
  font-size: 16px;
}

.file-name {
  color: var(--text-primary);
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.remove-btn {
  padding: 0;
  min-width: auto;
  width: 20px;
  height: 20px;
  color: var(--text-secondary);
  font-size: 18px;
  line-height: 1;
}

.remove-btn:hover {
  color: var(--primary-purple);
}

/* 按钮样式 */
.confirm-btn,
.start-btn {
  width: 100%;
  height: 40px;
  font-size: 14px;
  font-weight: 600;
  border-radius: var(--radius-md);
  transition: all var(--transition-base);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.confirm-btn:hover,
.start-btn:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.btn-icon {
  font-size: 18px;
}

/* 案件描述 */
.description-input {
  width: 100%;
}

:deep(.description-input .el-textarea__inner) {
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 16px;
  font-size: 14px;
  line-height: 1.8;
  transition: all var(--transition-fast);
}

:deep(.description-input .el-textarea__inner:hover) {
  border-color: var(--primary-purple-light);
}

:deep(.description-input .el-textarea__inner:focus) {
  border-color: var(--primary-purple);
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.1);
}

:deep(.description-input .el-textarea__inner[readonly]) {
  background: var(--bg-secondary);
  color: var(--text-secondary);
  cursor: not-allowed;
}

.description-tip {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  font-size: 14px;
  color: var(--text-secondary);
}

.description-tip.success {
  background: var(--bg-overlay);
  color: var(--primary-purple);
}

.tip-icon {
  font-size: 16px;
}

/* 响应式设计 - 桌面端优先 */
@media (min-width: 1400px) {
  .content-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .card-item:last-child {
    grid-column: 1 / -1;
  }
}

/* 小屏幕适配 */
@media (max-width: 1200px) {
  .content-cards {
    grid-template-columns: 1fr;
    padding: 0 32px;
  }
  
  .page-banner {
    padding: 60px 40px;
    margin: 0 32px 32px 32px;
  }
}
</style>
