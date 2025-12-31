<template>
  <div class="verdict-container">
    <!-- 主标题 -->
    <div class="verdict-header">
      <h1 class="verdict-title">民事判决书</h1>
    </div>

    <!-- 案件基本信息 -->
    <div class="verdict-section">
      <h2 class="section-title">案件基本信息</h2>
      <div class="section-content">
        {{ verdictInfo.basicInfo }}
      </div>
    </div>

    <!-- 审理经过 -->
    <div class="verdict-section">
      <h2 class="section-title">审理经过</h2>
      <div class="section-content">
        <div class="content-item">
          <h3 class="item-title">起诉时间和事实</h3>
          <p>{{ verdictInfo.proceedings.filingTime }}</p>
        </div>
        <div class="content-item">
          <h3 class="item-title">审理过程概述</h3>
          <p>{{ verdictInfo.proceedings.process }}</p>
        </div>
        <div class="content-item">
          <h3 class="item-title">当事人主要争议点</h3>
          <p>{{ verdictInfo.proceedings.disputes }}</p>
        </div>
      </div>
    </div>

    <!-- 当事人诉讼请求和答辩 -->
    <div class="verdict-section">
      <h2 class="section-title">当事人诉讼请求和答辩</h2>
      <div class="section-content">
        <div class="content-item">
          <h3 class="item-title">原告诉讼请求</h3>
          <p>{{ verdictInfo.requests.plaintiff }}</p>
        </div>
        <div class="content-item">
          <h3 class="item-title">被告的答辩意见</h3>
          <p>{{ verdictInfo.requests.defendant }}</p>
        </div>
        <div class="content-item">
          <h3 class="item-title">争议的主要问题</h3>
          <p>{{ verdictInfo.requests.mainIssues }}</p>
        </div>
      </div>
    </div>

    <!-- 本院查明的事实 -->
    <div class="verdict-section">
      <h2 class="section-title">本院查明的事实</h2>
      <div class="section-content">
        {{ verdictInfo.facts }}
      </div>
    </div>

    <!-- 本院认为 -->
    <div class="verdict-section">
      <h2 class="section-title">本院认为</h2>
      <div class="section-content">
        <div class="content-item">
          <h3 class="item-title">法律适用分析</h3>
          <p>{{ verdictInfo.opinion.lawAnalysis }}</p>
        </div>
        <div class="content-item">
          <h3 class="item-title">对争议问题的法律判断</h3>
          <p>{{ verdictInfo.opinion.legalJudgment }}</p>
        </div>
        <div class="content-item">
          <h3 class="item-title">责任认定和理由</h3>
          <p>{{ verdictInfo.opinion.liability }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 判决书内容（AI生成，暂时使用固定文字）
const verdictInfo = ref({
  basicInfo: `原告：张三，男，1980年5月出生，住址：北京市朝阳区XX街道XX号。
被告：李四，男，1975年8月出生，住址：北京市海淀区XX街道XX号。
案由：技术服务合同纠纷
案件编号：（2024）京0105民初12345号
审理法院：北京市朝阳区人民法院
审理时间：2024年3月15日`,

  proceedings: {
    filingTime: `原告于2024年1月20日向本院提起诉讼，称：2023年1月，原告与被告签订技术服务合同，约定被告向原告提供技术服务，合同金额50万元。原告已按约定支付首付款30万元，但被告未能按合同约定提供服务，构成违约。`,
    process: `本院受理后，依法组成合议庭，于2024年3月15日公开开庭进行了审理。原告及其委托诉讼代理人、被告及其委托诉讼代理人到庭参加诉讼。本案现已审理终结。`,
    disputes: `1. 被告是否存在违约行为；2. 原告的经济损失如何计算；3. 合同解除后的责任承担问题；4. 违约金是否过高需要调整。`
  },

  requests: {
    plaintiff: `1. 判令被告返还已支付的服务费30万元；2. 判令被告支付违约金10万元；3. 判令被告承担本案诉讼费用。`,
    defendant: `1. 对原告的诉讼请求不予认可；2. 认为原告未能提供必要的配合条件，导致无法正常履行合同；3. 认为原告主张的违约金过高，不符合法律规定；4. 请求法院驳回原告的诉讼请求。`,
    mainIssues: `1. 合同履行过程中是否存在违约行为；2. 违约责任的承担方式；3. 违约金的计算标准和是否过高；4. 合同解除后的法律后果。`
  },

  facts: `经审理查明：2023年1月，原告与被告签订《技术服务合同》，约定被告向原告提供技术服务，合同金额为50万元。合同签订后，原告按约定于2023年1月15日支付了首付款30万元。但被告在收到首付款后，未能按合同约定的时间节点提供服务。原告多次催促，被告均以各种理由推脱。2023年3月，原告向被告发出解除合同通知，要求返还已支付款项并支付违约金。被告收到通知后，未予回应。

上述事实，有双方签订的服务合同、银行转账凭证、沟通记录等证据在案佐证。`,

  opinion: {
    lawAnalysis: `根据《中华人民共和国民法典》第五百七十七条规定，当事人一方不履行合同义务或者履行合同义务不符合约定的，应当承担继续履行、采取补救措施或者赔偿损失等违约责任。本案中，被告在收到首付款后，未能按合同约定提供服务，构成违约。`,
    legalJudgment: `关于争议焦点一：被告是否存在违约行为。根据合同约定和证据材料，被告在收到首付款后，未能按约定时间提供服务，且经原告多次催促仍未履行，构成违约。

关于争议焦点二：违约金的计算。合同约定的违约金为合同金额的20%，即10万元。根据《民法典》相关规定，约定的违约金过分高于造成的损失的，人民法院可以根据当事人的请求予以适当减少。但原告未能提供充分证据证明其实际损失，且违约金约定并未明显过高，本院予以支持。`,
    liability: `被告未能按合同约定履行义务，构成违约，应当承担违约责任。原告要求被告返还已支付的服务费30万元，符合法律规定，本院予以支持。关于违约金，合同约定的10万元违约金在合理范围内，本院予以支持。`
  }
})
</script>

<style scoped>
.verdict-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.verdict-header {
  text-align: center;
  padding: 12px 0;
  border-bottom: 2px solid #409eff;
  margin-bottom: 10px;
}

.verdict-title {
  font-size: 12px;
  color: #333;
  margin: 0;
  font-weight: 700;
}

.verdict-section {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 12px;
}

.section-title {
  font-size: 12px;
  color: #409eff;
  margin: 0 0 8px 0;
  font-weight: 600;
  padding-bottom: 6px;
  border-bottom: 2px solid #e0e0e0;
}

.section-content {
  font-size: 12px;
  color: #333;
  line-height: 1.8;
  white-space: pre-line;
}

.content-item {
  margin-bottom: 8px;
}

.content-item:last-child {
  margin-bottom: 0;
}

.item-title {
  font-size: 12px;
  color: #409eff;
  margin: 0 0 6px 0;
  font-weight: 600;
}

.content-item p {
  margin: 0;
  padding-left: 15px;
  line-height: 1.8;
}
</style>

