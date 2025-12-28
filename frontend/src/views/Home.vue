<template>
  <div class="courtroom-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">AI模拟法庭</h1>
      <p class="page-subtitle">智能诉讼审判模拟系统</p>
    </div>
    
    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 合并的模块 -->
      <div class="unified-section">
        <!-- 身份选择 -->
        <div class="sub-section">
          <h2 class="section-title">选择身份</h2>
          <div class="identity-buttons">
            <el-button
              :type="selectedIdentity === 'plaintiff' ? 'primary' : ''"
              :class="{ 'selected': selectedIdentity === 'plaintiff' }"
              @click="selectIdentity('plaintiff')"
              class="identity-btn"
            >
              原告
            </el-button>
            <el-button
              :type="selectedIdentity === 'defendant' ? 'primary' : ''"
              :class="{ 'selected': selectedIdentity === 'defendant' }"
              @click="selectIdentity('defendant')"
              class="identity-btn"
            >
              被告
            </el-button>
          </div>
          <div v-if="selectedIdentity" class="identity-tip">
            当前身份：{{ selectedIdentity === 'plaintiff' ? '原告' : '被告' }}
          </div>
        </div>

        <!-- 文件上传 -->
        <div class="sub-section">
          <div class="section-header">
            <h2 class="section-title">上传案件资料</h2>
            <el-button
              v-if="fileList.length > 0"
              text
              type="danger"
              size="small"
              class="clear-all-btn"
              @click="clearAllFiles"
            >
              清空
            </el-button>
          </div>
          <div class="upload-buttons">
            <el-button
              type="primary"
              class="upload-btn"
              @click="triggerUpload"
            >
              选择文件上传
            </el-button>
            <el-button
              v-if="fileList.length > 0 && !filesConfirmed"
              type="success"
              class="confirm-btn"
              @click="confirmFiles"
            >
              确认文件
            </el-button>
          </div>
          <input
            ref="fileInput"
            type="file"
            multiple
            style="display: none"
            @change="handleFileChange"
          />
          <div v-if="fileList.length > 0" class="file-list">
            <div v-for="(file, index) in fileList" :key="index" class="file-item">
              <span class="file-name">{{ file.name }}</span>
              <el-button
                v-if="!filesConfirmed"
                text
                type="danger"
                class="remove-file-btn"
                @click="removeFile(index)"
              >
                ×
              </el-button>
            </div>
          </div>
        </div>

        <!-- 案件描述 -->
        <div class="sub-section">
          <h2 class="section-title">案件描述</h2>
          <el-input
            v-model="caseDescription"
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 100 }"
            placeholder="案件描述将由系统自动生成或后续添加..."
            :readonly="!filesConfirmed"
            class="description-textarea"
          />
          <div v-if="!filesConfirmed" class="description-tip">
            请先确认文件，系统将自动生成案件描述
          </div>
          <div v-else class="description-tip">
            您可以编辑上述内容进行调整
          </div>
          <el-button
            v-if="filesConfirmed && caseDescription"
            type="primary"
            class="start-btn"
            @click="startSimulation"
          >
            开始模拟
          </el-button>
        </div>
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
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useCaseStore } from '@/stores/case'

const router = useRouter()
const caseStore = useCaseStore()

// 从store恢复状态
const selectedIdentity = ref(caseStore.selectedIdentity || '')
const fileList = ref(caseStore.fileList || [])
const filesConfirmed = ref(caseStore.filesConfirmed || false)
const caseDescription = ref(caseStore.caseDescription || '')

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

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

const selectIdentity = (identity) => {
  selectedIdentity.value = identity
  caseStore.setIdentity(identity)
  ElMessage.success(`已选择身份：${identity === 'plaintiff' ? '原告' : '被告'}`)
}

// 文件上传
const fileInput = ref(null)
// 记录已确认的文件数量
const confirmedFileCount = ref(0)

const triggerUpload = () => {
  fileInput.value?.click()
}

const handleFileChange = (event) => {
  const files = Array.from(event.target.files)
  const previousFileCount = fileList.value.length
  
  files.forEach(file => {
    fileList.value.push({
      name: file.name,
      raw: file
    })
  })
  caseStore.setFileList(fileList.value)
  ElMessage.success(`已添加 ${files.length} 个文件`)
  
  // 如果之前已经确认过文件，且现在有新文件添加，重置确认状态
  if (filesConfirmed.value && fileList.value.length > confirmedFileCount.value) {
    filesConfirmed.value = false
    caseStore.resetConfirm() // 重置store中的确认状态
    caseStore.setCaseDescription('') // 清空之前的案件描述
    caseDescription.value = '' // 清空本地案件描述
    ElMessage.info('检测到新文件，请重新确认文件以生成案件描述')
  }
  
  // 清空input，允许重复选择同一文件
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

const removeFile = (index) => {
  fileList.value.splice(index, 1)
  caseStore.setFileList(fileList.value)
  
  // 如果删除文件后，文件数量少于已确认的数量，重置确认状态
  if (filesConfirmed.value && fileList.value.length < confirmedFileCount.value) {
    filesConfirmed.value = false
    caseStore.resetConfirm() // 重置store中的确认状态
    caseStore.setCaseDescription('')
    caseDescription.value = ''
    confirmedFileCount.value = 0
    ElMessage.info('文件已移除，请重新确认文件以生成案件描述')
  } else {
    ElMessage.info('文件已移除')
  }
}

// 清空所有文件
const clearAllFiles = () => {
  if (fileList.value.length === 0) {
    return
  }
  
  fileList.value = []
  caseStore.setFileList([])
  filesConfirmed.value = false
  caseStore.resetConfirm()
  caseStore.setCaseDescription('')
  caseDescription.value = ''
  confirmedFileCount.value = 0
  ElMessage.success('已清空所有文件')
}

// 确认文件并生成案件描述
const confirmFiles = () => {
  if (fileList.value.length === 0) {
    ElMessage.warning('请先上传文件')
    return
  }
  
  // 记录当前确认的文件数量
  confirmedFileCount.value = fileList.value.length
  
  // 模拟AI处理，生成案件描述
  ElMessage.info('正在分析文件，生成案件描述...')
  
  setTimeout(() => {
    // 使用固定的默认描述文字
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
  }, 1500)
}

// 监听案件描述变化，同步到store
watch(caseDescription, (newVal) => {
  caseStore.setCaseDescription(newVal)
})

// 开始模拟
const startSimulation = () => {
  if (!selectedIdentity.value) {
    ElMessage.warning('请先选择身份')
    return
  }
  if (!caseDescription.value) {
    ElMessage.warning('请先确认文件并生成案件描述')
    return
  }
  
  // 跳转到模拟法庭界面
  router.push({
    name: 'courtroom',
    query: {
      identity: selectedIdentity.value,
      description: caseDescription.value
    }
  })
}
</script>

<style scoped>
.courtroom-container {
  width: 100%;
  min-height: 100vh;
  background: #ededed;
  padding: 20px;
  overflow: auto;
}

/* 页面标题 */
.page-header {
  text-align: center;
  margin-bottom: 30px;
  padding: 20px 0;
}

.page-title {
  font-size: 20px;
  color: #333;
  margin: 0 0 10px 0;
  font-weight: 600;
}

.page-subtitle {
  font-size: 12px;
  color: #666;
  margin: 0;
}

/* 主要内容 */
.main-content {
  max-width: 500px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 统一模块样式 */
.unified-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  width: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 子模块样式 */
.sub-section {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.section-title {
  font-size: 10px;
  color: #333;
  margin: 0;
  font-weight: 600;
}

.clear-all-btn {
  font-size: 10px;
  padding: 4px 8px;
  color: #fa5151;
}

/* 身份选择 */
.identity-buttons {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.identity-btn {
  flex: 1;
  height: 36px;
  font-size: 10px;
  border-radius: 6px;
  border: 2px solid #d9d9d9;
  background: white;
  color: #333;
  transition: all 0.3s;
}

.identity-btn:hover {
  border-color: #07c160;
  color: #07c160;
  transform: translateY(-2px);
}

.identity-btn.selected {
  background: #07c160;
  border-color: #07c160;
  color: white;
}

.identity-tip {
  text-align: center;
  color: #07c160;
  font-size: 10px;
  margin-top: 8px;
  padding: 6px;
  background: #f0f9ff;
  border-radius: 4px;
}

/* 文件上传 */
.upload-buttons {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.upload-btn {
  flex: 1;
  height: 36px;
  font-size: 10px;
  border-radius: 6px;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border-color: #409eff;
}

.upload-btn:hover {
  background: linear-gradient(135deg, #66b1ff 0%, #85c1ff 100%);
  border-color: #66b1ff;
}

.confirm-btn {
  flex: 1;
  height: 36px;
  font-size: 10px;
  border-radius: 6px;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border-color: #409eff;
}

.confirm-btn:hover {
  background: linear-gradient(135deg, #66b1ff 0%, #85c1ff 100%);
  border-color: #66b1ff;
}

.file-list {
  margin-top: 10px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.file-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px;
  background: #f7f7f7;
  border-radius: 6px;
  border-left: 3px solid #07c160;
  transition: all 0.3s;
}

.file-item:hover {
  background: #f0f0f0;
  transform: translateX(5px);
}

.file-name {
  flex: 1;
  color: #333;
  font-size: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.remove-file-btn {
  margin-left: 8px;
  font-size: 16px;
  color: #fa5151;
  padding: 0;
  min-height: auto;
  width: 20px;
  height: 20px;
  line-height: 1;
  font-weight: bold;
}

.remove-file-btn:hover {
  color: #f56c6c;
  background: rgba(250, 81, 81, 0.1);
}

/* 案件描述 */
.description-textarea {
  width: 100%;
  margin-bottom: 15px;
}

:deep(.description-textarea .el-textarea__inner) {
  background: #ffffff;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  color: #333;
  font-size: 10px;
  resize: none;
  padding: 10px;
  line-height: 1.6;
  overflow-y: visible !important;
}

:deep(.el-button--primary) {
  background-color: #07c160;
  border-color: #07c160;
}

:deep(.el-button--primary:hover) {
  background-color: #06ad56;
  border-color: #06ad56;
}

:deep(.description-textarea .el-textarea__inner[readonly]) {
  background: #f7f7f7;
  color: #666;
  cursor: not-allowed;
}

.description-tip {
  color: #999;
  font-size: 10px;
  text-align: center;
  margin-top: 8px;
  padding: 6px;
  background: #f9f9f9;
  border-radius: 4px;
}

.start-btn {
  width: 100%;
  height: 40px;
  font-size: 10px;
  border-radius: 6px;
  margin-top: 12px;
  background: #07c160;
  border-color: #07c160;
  font-weight: 600;
  transition: all 0.3s;
}

.start-btn:hover {
  background: #06ad56;
  border-color: #06ad56;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(7, 193, 96, 0.3);
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

