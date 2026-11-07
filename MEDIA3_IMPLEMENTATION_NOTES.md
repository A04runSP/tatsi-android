# TATSI Media3 integration

This package integrates the existing Compose player controls with a Media3 `MediaController` connected to the app's `MediaSessionService`. The service owns ExoPlayer, so playback can persist beyond the Activity lifecycle.

## What changed
- Added Media3 ExoPlayer, Session and UI dependencies.
- Declared media playback foreground-service permissions and service in AndroidManifest.xml.
- Added optional `Track.audioUri` source field.
- Wired mini-player and Now Playing controls to MediaController for play/pause, next/previous, seek and live position updates.
- Existing TATSI screens, themes and sample metadata are retained.

## Audio-source limitation
The supplied sample tracks are metadata only and have no audio file/stream URI. The UI integration is ready to use a valid `content://` or `https://` URI assigned to `Track.audioUri`; without one, pressing play will not produce audio. A file picker/library scan and persistent local audio catalog are not included because they were not in the source inputs.

## Verification status
This is source integration, not a verified release build. The supplied project archive has no Gradle wrapper and this environment does not have an Android SDK/Gradle build configured, so compilation and on-device playback could not be verified here. Run the repository's GitHub Actions Android build before merging.
