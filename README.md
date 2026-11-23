<div align="center">
  <img src="assets/vinovim-logo.png" alt="Vinovim Desktop" width="96" />
  <h1>Vinovim Desktop</h1>
  <p>Player de mídia gratuito para Windows, macOS e Linux.</p>
  <p>
    <a href="https://github.com/vitinv1t/vinovim-desktop/releases">Download</a>  · 
    <a href="https://vinovim.tv">Site</a>  · 
    <a href="https://github.com/vitinv1t/vinovim-desktop/issues">Reportar problema</a>
  </p>
</div>

> 🚧 **Versão alpha.** O Vinovim Desktop ainda está sendo construído. Use por conta e risco — coisas vão quebrar, dados podem ser perdidos e atualizações podem mudar tudo. Se você está aqui, é porque quer ajudar a testar, e isso é muito bem-vindo.


## O que é

Vinovim Desktop é um player de mídia open source que funciona com as suas próprias fontes de conteúdo. Ele organiza tudo em uma biblioteca com capas, avaliações, legendas automáticas e progresso salvo entre dispositivos. Sem assinatura, sem plataforma fechada.

Roda no mesmo código base que o Vinovim para Android e iOS.


## Download

| Sistema | Formato |
|---|---|
| Windows | `.msi` |
| macOS | `.dmg` (arm64 e x86_64) |
| Linux | `.deb` `.rpm` `.flatpak` `.AppImage` |

Todas as versões em [Releases](https://github.com/vitinv1t/vinovim-desktop/releases).


## Como compilar

```bash
git clone https://github.com/vitinv1t/vinovim-desktop.git
cd vinovim-desktop
```

Rodar em desenvolvimento:

```bash
# macOS e Linux
./gradlew :composeApp:run

# Windows
.\gradlew.bat :composeApp:run
```

Gerar pacote de release:

```bash
# Detecta o sistema atual
./gradlew :composeApp:packageReleaseDistributionForCurrentOS

# Windows — MSI
.\gradlew.bat :composeApp:packageReleaseMsi --rerun-tasks

# macOS — DMG
./scripts/build-macos-release-dmgs.sh --package-only

# Linux — DEB
./gradlew :composeApp:packageReleaseDeb
```


## Android e iOS

```bash
# Android
./gradlew :androidApp:assembleRelease

# iOS — abrir no Xcode
open iosApp/iosApp.xcodeproj
```


## Tecnologia

| | |
|---|---|
| Interface | Compose Multiplatform |
| Linguagem | Kotlin Multiplatform |
| Vídeo no Android | ExoPlayer + libass + libmpv |
| Vídeo no Desktop | libmpv via JNI |
| Rede | Ktor |
| P2P | Vinovim Engine |


## Contribuir

Leia o [CONTRIBUTING.md](CONTRIBUTING.md) antes de abrir um pull request. Bugs e sugestões vão em [Issues](https://github.com/vitinv1t/vinovim-desktop/issues).


## Licença

[GNU General Public License v3.0](LICENSE)


<details>
<summary>🇬🇧 English</summary>


Vinovim Desktop is a free, open-source media player for Windows, macOS, and Linux. Use your own content sources — Vinovim organizes them into a library with artwork, ratings, subtitles, and playback progress synced across devices. No subscription, no locked platform.

> 🚧 **Alpha build.** Things will break, data may be lost, and updates can change everything. If you're here, you're a tester — and that's appreciated.

**Download** — [GitHub Releases](https://github.com/vitinv1t/vinovim-desktop/releases)

| Platform | Format |
|---|---|
| Windows | `.msi` |
| macOS | `.dmg` (arm64 and x86_64) |
| Linux | `.deb` `.rpm` `.flatpak` `.AppImage` |

**Build from source**

```bash
git clone https://github.com/vitinv1t/vinovim-desktop.git
cd vinovim-desktop
./gradlew :composeApp:run
```

**Tech stack** — Compose Multiplatform, Kotlin Multiplatform, libmpv, ExoPlayer, Ktor, Koin, Kotlin Coroutines, Vinovim Engine (P2P), Sentry.

**Contributing** — Read [CONTRIBUTING.md](CONTRIBUTING.md) before opening a pull request.

**License** — [GNU General Public License v3.0](LICENSE)

</details>
