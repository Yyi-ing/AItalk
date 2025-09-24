export async function chat(prompt) {
  const params = new URLSearchParams({ prompt })
  const response = await fetch(`/api/chat?${params.toString()}`)
  if (!response.ok) {
    const text = await response.text().catch(() => '')
    throw new Error(`Request failed: ${response.status} ${text}`)
  }
  return await response.text()
}
