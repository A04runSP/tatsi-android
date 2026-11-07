# TATSI Android

TATSI is an Android-first offline music player built with Kotlin, Jetpack Compose, and Media3.

## Build

The GitHub Actions workflow builds the debug APK on pushes and pull requests to `main`, and on manual workflow dispatch. The generated APK is published as the `tatsi-debug-apk` workflow artifact.

For a local build with JDK 17 and Android SDK installed:

```bash
gradle --no-daemon :app:assembleDebug
```

## Current playback note

The app includes a Media3 playback service and controller integration. The sample catalog entries are metadata examples and have no audio URI by default; add a valid local `content://` URI or reachable HTTPS audio URI to a `Track` to play actual audio. Selecting a metadata-only sample will not produce sound.
