<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { chat as chatApi } from '@/services/api'

const messages = ref([
  { role: 'assistant', content: '你好，我是你的AI助手。有什么可以帮你？' }
])
const userInput = ref('')
const isSending = ref(false)
const errorText = ref('')

// Speech recognition state
const supportsSpeech = ref(false)
const isRecording = ref(false)
let recognition = null
const recognizedBuffer = ref('')

onMounted(() => {
  const SR = window.SpeechRecognition || window.webkitSpeechRecognition
  if (SR) {
    supportsSpeech.value = true
    recognition = new SR()
    recognition.lang = 'zh-CN'
    recognition.interimResults = true
    recognition.continuous = true

    recognition.onresult = (event) => {
      for (let i = event.resultIndex; i < event.results.length; i++) {
        const res = event.results[i]
        if (res.isFinal) {
          recognizedBuffer.value += res[0].transcript
        }
        // 不显示中间结果或最终文本到输入框
      }
    }

    recognition.onerror = (e) => {
      isRecording.value = false
      errorText.value = `语音识别出错: ${e.error || '未知错误'}`
    }

    recognition.onend = async () => {
      isRecording.value = false
      const text = recognizedBuffer.value.trim()
      recognizedBuffer.value = ''
      if (text) {
        await sendContent(text)
      }
    }
  }
})

onBeforeUnmount(() => {
  try {
    if (recognition && isRecording.value) recognition.stop()
  } catch {}
})

function toggleRecord() {
  if (!supportsSpeech.value || !recognition) return
  errorText.value = ''
  try {
    if (!isRecording.value) {
      recognizedBuffer.value = ''
      recognition.start()
      isRecording.value = true
    } else {
      recognition.stop()
    }
  } catch (e) {
    errorText.value = '无法启动语音识别（浏览器权限或环境限制）'
    isRecording.value = false
  }
}

async function sendContent(content) {
  const text = content.trim()
  if (!text || isSending.value) return
  errorText.value = ''
  messages.value.push({ role: 'user', content: text })
  isSending.value = true
  try {
    const reply = await chatApi(text)
    messages.value.push({ role: 'assistant', content: reply })
  } catch (err) {
    errorText.value = err.message || '发送失败'
  } finally {
    isSending.value = false
  }
}

async function sendMessage() {
  const content = userInput.value.trim()
  if (!content) return
  userInput.value = ''
  await sendContent(content)
}
</script>

<template>
  <div class="chat">
    <div class="messages" ref="list">
      <div v-for="(m, idx) in messages" :key="idx" class="message" :class="m.role">
        <div class="bubble">{{ m.content }}</div>
      </div>
    </div>
    <div class="error" v-if="errorText">{{ errorText }}</div>
    <form class="input" @submit.prevent="sendMessage">
      <input
        v-model="userInput"
        type="text"
        placeholder="输入你的问题...（也可点击麦克风语音发送）"
        :disabled="isSending"
      />
      <button
        type="button"
        class="mic"
        :title="supportsSpeech ? (isRecording ? '停止录音' : '开始录音') : '浏览器不支持语音识别'"
        :disabled="!supportsSpeech || isSending"
        @click="toggleRecord"
      >
        {{ isRecording ? '停止' : '🎤' }}
      </button>
      <button type="submit" :disabled="isSending || !userInput.trim()">
        {{ isSending ? '发送中...' : '发送' }}
      </button>
    </form>
  </div>
</template>

<style scoped>
.chat {
  max-width: 800px;
  margin: 0 auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.messages {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-height: 50vh;
  background: #f7f7f9;
  border: 1px solid #e9e9ef;
  border-radius: 8px;
  padding: 12px;
  overflow-y: auto;
}
.message {
  display: flex;
}
.message.user {
  justify-content: flex-end;
}
.message.assistant {
  justify-content: flex-start;
}
.bubble {
  max-width: 80%;
  padding: 10px 12px;
  border-radius: 12px;
  white-space: pre-wrap;
  word-break: break-word;
}
.message.user .bubble {
  background: #3b82f6;
  color: white;
  border-top-right-radius: 4px;
}
.message.assistant .bubble {
  background: white;
  border: 1px solid #e5e7eb;
  border-top-left-radius: 4px;
}
.input {
  display: flex;
  gap: 8px;
}
.input input {
  flex: 1;
  padding: 10px 12px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
}
.input button {
  padding: 0 16px;
  border: none;
  background: #10b981;
  color: white;
  border-radius: 8px;
  cursor: pointer;
}
.input .mic {
  padding: 0 12px;
  background: #ef4444;
}
.input button[disabled] {
  opacity: 0.6;
  cursor: not-allowed;
}
.error {
  color: #ef4444;
}
</style>
