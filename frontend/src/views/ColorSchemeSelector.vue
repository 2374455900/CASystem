<template>
  <div class="scheme-selector">
    <div class="scheme-card" @click="selectScheme('scheme-1')" :class="{ active: currentScheme === 'scheme-1' }">
      <div class="scheme-preview">
        <div class="preview-item" style="background: #3b82f6;"></div>
        <div class="preview-item" style="background: #f97316;"></div>
        <div class="preview-item" style="background: #10b981;"></div>
        <div class="preview-item" style="background: #ffffff;"></div>
      </div>
      <div class="scheme-info">
        <h4>清新蓝调</h4>
        <p>专业、清新、可信赖</p>
      </div>
    </div>

    <div class="scheme-card" @click="selectScheme('scheme-2')" :class="{ active: currentScheme === 'scheme-2' }">
      <div class="scheme-preview">
        <div class="preview-item" style="background: #f59e0b;"></div>
        <div class="preview-item" style="background: #3b82f6;"></div>
        <div class="preview-item" style="background: #f43f5e;"></div>
        <div class="preview-item" style="background: #ffffff;"></div>
      </div>
      <div class="scheme-info">
        <h4>温暖琥珀</h4>
        <p>温暖、创意、亲和力</p>
      </div>
    </div>

    <div class="scheme-card" @click="selectScheme('scheme-3')" :class="{ active: currentScheme === 'scheme-3' }">
      <div class="scheme-preview">
        <div class="preview-item" style="background: #84cc16;"></div>
        <div class="preview-item" style="background: #6366f1;"></div>
        <div class="preview-item" style="background: #ec4899;"></div>
        <div class="preview-item" style="background: #ffffff;"></div>
      </div>
      <div class="scheme-info">
        <h4>科技青柠</h4>
        <p>现代、科技、活力</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ColorSchemeSelector',
  data() {
    return {
      currentScheme: 'scheme-1'
    }
  },
  methods: {
    selectScheme(scheme) {
      this.currentScheme = scheme
      this.$emit('scheme-change', scheme)
    }
  }
}
</script>

<style scoped>
.scheme-selector {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}

.scheme-card {
  flex: 1;
  min-width: 280px;
  padding: 32px;
  background: #ffffff;
  border: 2px solid #e2e8f0;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.scheme-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.1);
  transform: translateY(-4px);
}

.scheme-card.active {
  border-color: #3b82f6;
  box-shadow: 0 4px 20px rgba(59, 130, 246, 0.2);
}

.scheme-preview {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.preview-item {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.scheme-info h4 {
  font-size: 18px;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 8px;
}

.scheme-info p {
  font-size: 14px;
  color: #64748b;
  line-height: 1.6;
}
</style>
