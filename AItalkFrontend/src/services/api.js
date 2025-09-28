export async function chat(prompt) {
  const params = new URLSearchParams({ prompt })
  const response = await fetch(`/api/chat?${params.toString()}`)
  if (!response.ok) {
    const text = await response.text().catch(() => '')
    throw new Error(`Request failed: ${response.status} ${text}`)
  }
  return await response.text()
}

export async function chatAudio(prompt, id) {
  const params = new URLSearchParams({ prompt })
  if (id !== undefined && id !== null && `${id}`.length > 0) {
    params.set('id', `${id}`)
  }
  const response = await fetch(`/api/voice?${params.toString()}`, {
    headers: {
      Accept: 'audio/*'
    }
  })
  if (!response.ok) {
    const text = await response.text().catch(() => '')
    throw new Error(`Audio request failed: ${response.status} ${text}`)
  }
  return await response.blob()
}

export async function roleList() {
  const response = await fetch('/api/roleList')
  if (!response.ok) {
    const text = await response.text().catch(() => '')
    throw new Error(`Role list request failed: ${response.status} ${text}`)
  }
  return await response.json()
}

export async function getHistory(roleId) {
  const params = new URLSearchParams({ id: roleId })
  const response = await fetch(`/api/history?${params.toString()}`)
  if (!response.ok) {
    const text = await response.text().catch(() => '')
    throw new Error(`History request failed: ${response.status} ${text}`)
  }
  return await response.json()
}
