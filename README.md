# OllamaApp
Powerful Android app packaged as an APK that runs on almost any Android device. Uses Ollama models and lets you install and run any compatible model directly on your device.

## Features
- Ollama-style terminal to download, install, and manage models
- Chat with installed models via the menu
- On-device coding agent that can create apps, write and modify code
- Dark mode support
- Large context window support (depends on selected model)

## Building
This project uses Gradle. To build the APK:
1. Open in Android Studio or use GitHub Actions
2. Run `./gradlew assembleDebug`
3. APK: `app/build/outputs/apk/debug/app-debug.apk`

## Ollama Integration
- Connects to Ollama server at `http://localhost:11434` (configurable)
- List available models via `/api/tags`
- Pull/download models via `/api/pull`
- Chat with models via `/api/chat`

## Screens
- **Chat**: Ollama-style terminal with message input
- **Models**: Pull and manage models
- **Agent**: On-device coding agent for app generation