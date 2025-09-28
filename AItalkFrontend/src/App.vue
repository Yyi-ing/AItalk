<script setup>
import Chat from './components/Chat.vue'
import RoleSelector from './components/RoleSelector.vue'
import { ref } from 'vue'

const selectedRole = ref(null)

function onRoleSelected(role) {
  selectedRole.value = role
}
</script>

<template>
  <div class="app">
    <header>
      <h1>AItalk</h1>
    </header>
    <div class="main-container">
      <aside class="sidebar">
        <RoleSelector @role-selected="onRoleSelected" />
      </aside>
      <main class="chat-panel">
        <Chat :selected-role="selectedRole" />
      </main>
    </div>
  </div>
</template>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.app {
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  width: 150vh;
}

header {
  text-align: center;
  padding: 12px 0;
  background: #f8fafc;
  border-bottom: 1px solid #e2e8f0;
  flex-shrink: 0;
}

header h1 {
  margin: 0;
  color: #1e293b;
  font-size: 24px;
  font-weight: 600;
}

.main-container {
  display: flex;
  flex: 1;
  overflow: hidden;
  min-height: 0;
}

.sidebar {
  width: 320px;
  background: #f8fafc;
  border-right: 1px solid #e2e8f0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.chat-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-height: 0;
}
</style>
