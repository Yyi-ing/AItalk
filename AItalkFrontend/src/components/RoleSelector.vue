<script setup>
import { ref, onMounted } from 'vue'
import { roleList as fetchRoles } from '@/services/api'

const roles = ref([])
const selectedRole = ref(null)
const loading = ref(false)
const error = ref('')

const emit = defineEmits(['roleSelected'])

onMounted(async () => {
  loading.value = true
  try {
    roles.value = await fetchRoles()
  } catch (err) {
    error.value = err.message || '获取角色列表失败'
  } finally {
    loading.value = false
  }
})

function selectRole(role) {
  selectedRole.value = role
  emit('roleSelected', role)
}

function clearSelection() {
  selectedRole.value = null
  emit('roleSelected', null)
}
</script>

<template>
  <div class="role-selector">
    <div class="header">
      <h3>选择角色</h3>
      <button v-if="selectedRole" @click="clearSelection" class="clear-btn">清除选择</button>
    </div>
    
    <div v-if="loading" class="loading">加载角色中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="role-grid">
      <div 
        v-for="role in roles" 
        :key="role.id"
        class="role-card"
        :class="{ selected: selectedRole?.id === role.id }"
        @click="selectRole(role)"
      >
        <div class="role-name">{{ role.name }}</div>
        <div class="role-background">{{ role.background }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.role-selector {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 12px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  flex-shrink: 0;
}

.header h3 {
  margin: 0;
  color: #374151;
}

.clear-btn {
  padding: 4px 12px;
  background: #ef4444;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 12px;
}

.clear-btn:hover {
  background: #dc2626;
}

.loading, .error {
  text-align: center;
  padding: 20px;
  color: #6b7280;
}

.error {
  color: #ef4444;
}

.role-grid {
  display: flex;
  flex-direction: column;
  gap: 8px;
  overflow-y: auto;
  flex: 1;
  min-height: 0;
}

.role-card {
  padding: 10px;
  background: white;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  flex-shrink: 0;
}

.role-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 2px 4px rgba(59, 130, 246, 0.1);
}

.role-card.selected {
  border-color: #10b981;
  background: #f0fdf4;
  box-shadow: 0 2px 4px rgba(16, 185, 129, 0.1);
}

.role-name {
  font-weight: bold;
  font-size: 14px;
  color: #1f2937;
  margin-bottom: 6px;
}

.role-background {
  font-size: 12px;
  color: #4b5563;
  line-height: 1.3;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.role-personality, .role-style {
  font-size: 12px;
  color: #6b7280;
  margin-bottom: 4px;
  line-height: 1.3;
}

.role-personality strong, .role-style strong {
  color: #374151;
}
</style>
