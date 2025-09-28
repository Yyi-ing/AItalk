<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import { chat as chatApi, chatAudio as chatAudioApi, getHistory } from '@/services/api'

const props = defineProps({
  selectedRole: {
    type: Object,
    default: null
  }
})

const messages = ref([
  { role: 'assistant', content: '请在左侧角色列表选择AI的扮演角色，并开始对话' }
])
const userInput = ref('')
const isSending = ref(false)
const errorText = ref('')
const isLoadingHistory = ref(false)

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

async function playAudioBlob(blob) {
  const url = URL.createObjectURL(blob)
  try {
    const audio = new Audio(url)
    await audio.play()
  } catch {}
  return url
}

// Watch for role changes from parent
watch(() => props.selectedRole, async (newRole) => {
  if (newRole) {
    isLoadingHistory.value = true
    try {
      // 获取该角色的聊天历史
      const history = await getHistory(newRole.id)
      if (history && history.length > 0) {
        // 如果有历史记录，显示历史记录
        messages.value = history.map(msg => ({
          role: msg.role,
          content: msg.content,
          audioUrl: null // 历史记录不包含音频
        }))
      } else {
        // 如果没有历史记录，显示角色介绍
        messages.value = [
          { role: 'assistant', content: `你好！我是${newRole.name}` }
        ]
      }
    } catch (err) {
      console.error('获取聊天历史失败:', err)
      // 如果获取历史失败，显示角色介绍
      messages.value = [
        { role: 'assistant', content: `你好！我是${newRole.name}` }
      ]
    } finally {
      isLoadingHistory.value = false
    }
  } else {
    messages.value = [
      { role: 'assistant', content: '请在左侧角色列表选择AI的扮演角色，并开始对话' }
    ]
  }
}, { immediate: true })

async function sendContent(content) {
  const text = content.trim()
  if (!text || isSending.value) return
  errorText.value = ''
  
  // 添加角色信息到消息中
  const userMessage =  text
  messages.value.push({ role: 'user', content: userMessage })
  
  isSending.value = true
  try {
    // 优先请求后端返回音频，携带角色ID
    const blob = await chatAudioApi(text, props.selectedRole?.id)
    const audioUrl = await playAudioBlob(blob)
    messages.value.push({ role: 'assistant', content: '语音回复', audioUrl })
  } catch (err) {
    // 回退：若音频失败，尝试文字接口
    // try {
    //   const reply = await chatApi(text)
    //   messages.value.push({ role: 'assistant', content: reply })
    // } catch (e2) {
    //   errorText.value = err?.message || e2?.message || '发送失败'
    // }
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
    <div v-if="props.selectedRole" class="selected-role">
      <strong>当前角色：</strong>{{ props.selectedRole.name }}
    </div>
    
    <div class="messages" ref="list">
      <div v-if="isLoadingHistory" class="loading-history">
        正在加载聊天历史...
      </div>
      <div v-for="(m, idx) in messages" :key="idx" class="message" :class="m.role">
        <div class="bubble">
          <template v-if="m.audioUrl">
            🔊 {{ m.content }}
            <audio :src="m.audioUrl" controls style="display:block; margin-top:6px; width:100%"></audio>
          </template>
          <template v-else>
            {{ m.content }}
          </template>
        </div>
      </div>
    </div>
    <div class="error" v-if="errorText">{{ errorText }}</div>
    <form class="input" @submit.prevent="sendMessage">
      <input
        v-model="userInput"
        type="text"
        :placeholder="props.selectedRole ? `与${props.selectedRole.name}对话...（也可点击麦克风语音发送）` : '请输入文本...（也可点击麦克风语音发送）'"
        :disabled="isSending"
      />
      <button
        type="button"
        class="mic"
        :title="supportsSpeech ? (isRecording ? '停止录音' : '开始录音') : '浏览器不支持语音识别'"
        :disabled="!supportsSpeech || isSending"
        @click="toggleRecord"
      >
        {{ isRecording ? '停止' : '录音' }}
      </button>
      <button type="submit" :disabled="isSending || !userInput.trim()">
        {{ isSending ? '发送中...' : '发送' }}
      </button>
    </form>
  </div>
</template>

<style scoped>
.chat {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 12px;
  width: 800px;
}
.messages {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
  background: #f7f7f9;
  border: 1px solid #e9e9ef;
  border-radius: 8px;
  padding: 12px;
  overflow-y: auto;
  min-height: 0;
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

.selected-role {
  padding: 12px;
  background: #f0fdf4;
  border: 1px solid #10b981;
  border-radius: 8px;
  margin-bottom: 12px;
}

.selected-role strong {
  color: #059669;
}

.role-info {
  font-size: 14px;
  color: #6b7280;
  margin-top: 4px;
}

.loading-history {
  text-align: center;
  padding: 20px;
  color: #6b7280;
  font-style: italic;
}
</style>
