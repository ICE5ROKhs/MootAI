<template>
  <div class="pretrial-container">
    <!-- 顶部导航 -->
    <div class="nav-tabs">
      <el-button
        :type="activeTab === 'basic' ? 'primary' : ''"
        :class="{ 'active': activeTab === 'basic' }"
        @click="activeTab = 'basic'"
        class="nav-btn"
      >
        基本信息
      </el-button>
      <el-button
        :type="activeTab === 'strategy' ? 'primary' : ''"
        :class="{ 'active': activeTab === 'strategy' }"
        @click="activeTab = 'strategy'"
        class="nav-btn"
      >
        诉讼策略
      </el-button>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <!-- 基本信息 -->
      <div v-if="activeTab === 'basic'" class="unified-section">
        <div class="info-item">
          <h4 class="info-title">案件基本情况</h4>
          <div class="info-content">
            {{ basicInfo.caseOverview }}
          </div>
        </div>
        <div class="info-item">
          <h4 class="info-title">争议焦点</h4>
          <div class="info-content">
            {{ basicInfo.disputes }}
          </div>
        </div>
        <div class="info-item">
          <h4 class="info-title">相关法条</h4>
          <div class="info-content">
            {{ basicInfo.laws }}
          </div>
        </div>
        <div class="info-item">
          <h4 class="info-title">案件要素</h4>
          <div class="info-content">
            {{ basicInfo.elements }}
          </div>
        </div>
      </div>

      <!-- 诉讼策略 -->
      <div v-else-if="activeTab === 'strategy'" class="unified-section">
        <div class="strategy-item strategy-aggressive">
          <h4 class="strategy-title">激进策略</h4>
          <div class="strategy-content">
            {{ strategies.aggressive }}
          </div>
        </div>
        <div class="strategy-item strategy-conservative">
          <h4 class="strategy-title">保守策略</h4>
          <div class="strategy-content">
            {{ strategies.conservative }}
          </div>
        </div>
        <div class="strategy-item strategy-balanced">
          <h4 class="strategy-title">均衡策略</h4>
          <div class="strategy-content">
            {{ strategies.balanced }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  activeSubTab: {
    type: String,
    default: 'basic'
  }
})

const emit = defineEmits(['update:activeSubTab'])

const activeTab = ref(props.activeSubTab || 'basic')

// 监听外部传入的 activeSubTab 变化
watch(() => props.activeSubTab, (newVal) => {
  if (newVal) {
    activeTab.value = newVal
  }
})

// 设置活动标签的方法
const setActiveTab = (tab) => {
  activeTab.value = tab
  emit('update:activeSubTab', tab)
}

// 监听内部 activeTab 变化，同步到外部
watch(activeTab, (newVal) => {
  emit('update:activeSubTab', newVal)
})

defineExpose({
  setActiveTab
})

// 基本信息（AI生成，暂时使用固定文字）
const basicInfo = ref({
  caseOverview: `本案涉及一起合同纠纷案件。原告与被告于2023年1月签订了一份服务合同，约定被告向原告提供技术服务，合同金额为50万元。合同签订后，原告按约定支付了首付款30万元，但被告未能按合同约定提供服务，导致原告遭受经济损失。`,
  disputes: `1. 被告是否存在违约行为
2. 原告的经济损失如何计算
3. 合同解除后的责任承担问题
4. 违约金是否过高需要调整`,
  laws: `《中华人民共和国民法典》第五百七十七条：当事人一方不履行合同义务或者履行合同义务不符合约定的，应当承担继续履行、采取补救措施或者赔偿损失等违约责任。

《中华人民共和国民法典》第五百六十三条：有下列情形之一的，当事人可以解除合同：（一）因不可抗力致使不能实现合同目的；（二）在履行期限届满前，当事人一方明确表示或者以自己的行为表明不履行主要债务；（三）当事人一方迟延履行主要债务，经催告后在合理期限内仍未履行；（四）当事人一方迟延履行债务或者有其他违约行为致使不能实现合同目的；（五）法律规定的其他情形。`,
  elements: `- 合同类型：技术服务合同
- 合同金额：50万元
- 已支付金额：30万元
- 争议金额：30万元及违约金
- 合同签订时间：2023年1月
- 违约发生时间：2023年3月
- 诉讼请求：返还已支付款项30万元，支付违约金10万元`
})

// 诉讼策略（暂时使用固定文字）
const strategies = ref({
  aggressive: `激进策略强调主动进攻，通过强有力的证据和法理依据，全面主张权利。主要特点：
1. 主张全额返还已支付款项30万元
2. 要求支付高额违约金10万元
3. 申请财产保全，冻结被告账户
4. 准备充分的证据材料，包括合同、付款凭证、沟通记录等
5. 在庭审中采取主动出击的方式，质疑被告的每一个抗辩理由`,
  conservative: `保守策略注重稳妥，通过协商和调解优先解决问题。主要特点：
1. 优先考虑通过调解解决争议
2. 主张返还已支付款项，但可适当让步
3. 违约金主张较为温和，可协商调整
4. 注重证据的完整性和合法性
5. 在庭审中采取防守为主的方式，等待对方提出方案后再回应`,
  balanced: `均衡策略兼顾进攻与防守，在维护自身权益的同时保持灵活性。主要特点：
1. 主张返还已支付款项30万元，态度坚决
2. 违约金主张适中，约5-7万元，可协商
3. 准备充分的证据，但不过度激化矛盾
4. 在庭审中采取攻守兼备的方式，既主动主张权利，又保持协商空间
5. 根据庭审情况灵活调整策略`
})
</script>

<style scoped>
.pretrial-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 顶部导航 */
.nav-tabs {
  display: flex;
  gap: 8px;
  background: #f5f7fa;
  padding: 8px;
  border-radius: 8px;
}

.nav-btn {
  flex: 1;
  height: 32px;
  font-size: 10px;
  border-radius: 6px;
  padding: 0 12px;
  transition: all 0.3s;
}

.nav-btn:hover {
  transform: translateY(-2px);
}

.nav-btn.active {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border-color: #409eff;
  color: white;
}

/* 内容区域 */
.content-area {
  background: transparent;
  border-radius: 0;
  padding: 0;
  box-shadow: none;
  display: flex;
  flex-direction: column;
}

/* 统一模块样式 */
.unified-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 基本信息 */
.info-item {
  background: #f5f7fa;
  border-radius: 6px;
  padding: 12px;
  border-left: 3px solid #409eff;
  transition: all 0.3s;
}

.info-item:hover {
  background: #ecf5ff;
  transform: translateX(3px);
}

.info-title {
  font-size: 6px;
  color: #409eff;
  margin: 0 0 8px 0;
  font-weight: 600;
}

.info-content {
  font-size: 6px;
  color: #606266;
  line-height: 1.6;
  white-space: pre-line;
}

/* 诉讼策略 */
.strategy-item {
  border-radius: 6px;
  padding: 12px;
  border-top: 3px solid;
  transition: all 0.3s;
}

.strategy-aggressive {
  background: #fee;
  border-top-color: #f56c6c;
}

.strategy-conservative {
  background: #fffbeb;
  border-top-color: #e6a23c;
}

.strategy-balanced {
  background: #f5f7fa;
  border-top-color: #67c23a;
}

.strategy-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.strategy-title {
  font-size: 6px;
  margin: 0 0 8px 0;
  font-weight: 600;
}

.strategy-aggressive .strategy-title {
  color: #f56c6c;
}

.strategy-conservative .strategy-title {
  color: #e6a23c;
}

.strategy-balanced .strategy-title {
  color: #67c23a;
}

.strategy-content {
  font-size: 6px;
  color: #606266;
  line-height: 1.6;
  white-space: pre-line;
}

</style>

