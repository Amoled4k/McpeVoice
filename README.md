# Flarial Voice Starter (Android prototype)

This is **not** an official Flarial app or integration. It opens a Jitsi Meet voice room in an Android WebView. The other players must join the same room separately. No Minecraft positions are read and **no proximity audio** is implemented. Background audio/microphone behavior is **not guaranteed**; some Android/WebView versions may not support Jitsi media properly.

## Build APK without a computer
1. Create a GitHub repository and upload **the contents** of this folder (including `.github/workflows/build-apk.yml`). GitHub mobile web's upload interface may require desktop site mode or a Git client.
2. Open the repository's **Actions** tab, choose **Build Android APK**, then **Run workflow**. Push to `main` also triggers a build.
3. Once completed, open the run and download the `FlarialVoiceStarter-debug-apk` artifact. Unzip it to obtain `app-debug.apk`.
4. Install on your own Android device. Android may require permission to install apps from the browser/file manager.

The app uses `https://meet.jit.si` (a third-party service). Read its terms and privacy policy before using. Use a hard-to-guess room name and don't share personal information. Some rooms may require authentication/moderation depending on Jitsi's service policies.

## Limitations
- Not verified on an actual Android phone, and no APK was built in this environment.
- No Flarial integration, automatic lobby discovery, position tracking, or proximity voice.
- WebView mic capture is not guaranteed to work with Jitsi on all Android devices.
- The app may stop capturing audio when backgrounded; it does not use a foreground microphone service.
- Debug APK is for personal testing only.
