# AItalkFrontend

A simple Vue 3 + Vite frontend for the AItalk chat backend.

## Prerequisites
- Node.js 20+
- Backend Spring Boot running on http://localhost:8090 with GET /chat?prompt=...

## Development
```sh
npm install
npm run dev
```
The dev server proxies API requests from `/api/*` to `http://localhost:8090`.

Example: frontend calls `/api/chat?prompt=hello` → backend `/chat?prompt=hello`.

## Voice Input (Speech-to-Text)
- Uses the Web Speech API (SpeechRecognition).
- Works in Chromium-based browsers (Chrome/Edge). Safari/iOS support varies.
- Microphone access typically requires HTTPS or localhost and user permission.
- Click the microphone button to start/stop recording. Interim text shows with an ellipsis.

## Build
```sh
npm run build
npm run preview
```
