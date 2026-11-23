<div align="center">
  <img src="assets/vinovim-logo.png" alt="Vinovim" width="120" />

  <h1>Vinovim Desktop</h1>

  A free, open-source media app for **Windows**, **macOS**, and **Linux**.<br/>
  Bring your own sources. Vinovim turns them into a library with artwork, ratings, subtitles, and your place saved on every screen.

  [Website](https://vinovim.tv) · [GitHub Releases](https://github.com/vitinv1t/vinovim-desktop/releases) · [Support Vinovim](https://vinovim.tv/support)
</div>

---

> ⚠️ **Alpha Software — Slow Development — Testers Only**
>
> Vinovim Desktop is currently in alpha and is intended only for testers. It is under development and is not suitable for daily use.
>
> Expect breaking changes with every update. Features, settings, stored data, and compatibility may change or stop working without notice. Do not rely on this build as your primary media app, and report any issues you encounter during testing.

---

## Get Vinovim Desktop

Download the latest build from [GitHub Releases](https://github.com/vitinv1t/vinovim-desktop/releases).

- **Windows:** MSI installer
- **macOS:** DMG installer
- **Linux:** DEB, RPM, Flatpak, or AppImage

---

## Build from source

```bash
git clone https://github.com/vitinv1t/vinovim-desktop.git
cd vinovim-desktop
```

### Run the app

```bash
./gradlew :composeApp:run
```

**On Windows PowerShell:**

```powershell
.\gradlew.bat :composeApp:run
```

### Package the app

Build a release package for the current host:

```bash
./gradlew :composeApp:packageReleaseDistributionForCurrentOS
```

**For platform-specific packaging:**

Windows PowerShell:
```powershell
.\gradlew.bat :composeApp:packageReleaseMsi --rerun-tasks
```

macOS:
```bash
./scripts/build-macos-release-dmgs.sh --package-only
```

Linux:
```bash
./gradlew :composeApp:packageReleaseDeb
```

The shared app is built with [Kotlin Multiplatform](https://www.jetbrains.com/kotlin-multiplatform/) and [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/).

---

## Android & iOS

Vinovim also runs on Android and iOS from the same shared codebase.

- **Android:** build via `./gradlew :androidApp:assembleRelease`
- **iOS:** open `iosApp/iosApp.xcodeproj` in Xcode

---

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Compose Multiplatform (Desktop / Android / iOS) |
| Language | Kotlin Multiplatform |
| Video (Android) | ExoPlayer + libass + libmpv |
| Video (Desktop) | libmpv via JNI |
| Networking | Ktor |
| DI | Koin |
| Async | Kotlin Coroutines & Flow |
| P2P Streaming | Vinovim Engine (custom BitTorrent) |
| Crash Reporting | Sentry |

---

## Contributing

Contributions are welcome. Please read [CONTRIBUTING.md](CONTRIBUTING.md) before opening a pull request.

- Bug reports → [GitHub Issues](https://github.com/vitinv1t/vinovim-desktop/issues)
- Feature requests → [GitHub Issues](https://github.com/vitinv1t/vinovim-desktop/issues)
- Pull requests → [GitHub Pull Requests](https://github.com/vitinv1t/vinovim-desktop/pulls)

---

## License

[GNU General Public License v3.0](LICENSE)
