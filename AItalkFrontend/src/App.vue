<script setup>
import Chat from './components/Chat.vue'
</script>

<template>
  <header>
    <h1>AItalk</h1>
  </header>
  <main>
    <Chat />
  </main>
</template>

<style scoped>
header {
  display: flex;
  align-items: center;
  gap: 12px;
  justify-content: center;
  padding: 16px 0;
}
.logo {
  display: block;
}
main {
  padding: 0 16px 24px;
}
</style>
