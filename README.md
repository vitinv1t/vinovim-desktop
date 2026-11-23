<div align="center">
  <img src="composeApp/src/desktopMain/resources/icons/vinovim-app-icon-transparent.png" alt="Vinovim" width="100" />

  <h1>Vinovim Desktop</h1>

  <p>Seu player de mídia. Suas fontes. Do seu jeito.</p>

  <p>
    <a href="https://github.com/vitinv1t/vinovim-desktop/releases">📦 Download</a> ·
    <a href="https://vinovim.tv">🌐 Site</a> ·
    <a href="https://github.com/vitinv1t/vinovim-desktop/issues">🐛 Reportar Bug</a>
  </p>

  <img src="https://img.shields.io/badge/plataforma-Windows%20%7C%20macOS%20%7C%20Linux-blue" alt="Plataformas" />
  <img src="https://img.shields.io/badge/licença-GPLv3-green" alt="Licença" />
  <img src="https://img.shields.io/badge/status-alpha-orange" alt="Status" />
</div>

---

> ⚠️ **Alpha — Apenas para testadores**
>
> O Vinovim Desktop está em desenvolvimento ativo. Espere mudanças frequentes, bugs e instabilidade. Não use como app principal no dia a dia.

---

## O que é o Vinovim?

O Vinovim Desktop é um app de mídia gratuito e de código aberto que roda em Windows, macOS e Linux. Ele não te prende em nenhuma plataforma — você traz suas próprias fontes de conteúdo e o Vinovim cuida do resto: biblioteca organizada, capas, avaliações, legendas automáticas e progresso sincronizado entre dispositivos.

Feito para quem quer controle total sobre como e onde assiste.

---

## Download

Baixe a versão mais recente diretamente no [GitHub Releases](https://github.com/vitinv1t/vinovim-desktop/releases).

| Sistema | Formato |
|---|---|
| Windows | `.msi` |
| macOS | `.dmg` |
| Linux | `.deb` · `.rpm` · `.flatpak` · `.AppImage` |

---

## Compilar e rodar

```bash
git clone https://github.com/vitinv1t/vinovim-desktop.git
cd vinovim-desktop
```

**Rodar em modo desenvolvimento:**

```bash
# macOS / Linux
./gradlew :composeApp:run

# Windows
.\gradlew.bat :composeApp:run
```

**Gerar pacote de release:**

```bash
# Detecta o sistema automaticamente
./gradlew :composeApp:packageReleaseDistributionForCurrentOS

# Windows — MSI
.\gradlew.bat :composeApp:packageReleaseMsi --rerun-tasks

# macOS — DMG
./scripts/build-macos-release-dmgs.sh --package-only

# Linux — DEB
./gradlew :composeApp:packageReleaseDeb
```

---

## Android & iOS

O mesmo código roda em mobile. Para compilar:

```bash
# Android
./gradlew :androidApp:assembleRelease

# iOS — abra no Xcode
open iosApp/iosApp.xcodeproj
```

---

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Interface | Compose Multiplatform |
| Linguagem | Kotlin Multiplatform |
| Vídeo (Android) | ExoPlayer + libass + libmpv |
| Vídeo (Desktop) | libmpv via JNI |
| Rede | Ktor |
| Injeção de dependência | Koin |
| Concorrência | Kotlin Coroutines & Flow |
| Streaming P2P | Vinovim Engine |
| Monitoramento | Sentry |

---

## Contribuindo

Pull requests são bem-vindos. Leia o [CONTRIBUTING.md](CONTRIBUTING.md) antes de começar.

- **Bugs e sugestões:** [Issues](https://github.com/vitinv1t/vinovim-desktop/issues)
- **Código:** [Pull Requests](https://github.com/vitinv1t/vinovim-desktop/pulls)

---

## Licença

[GNU General Public License v3.0](LICENSE)

---

<br/>

<div align="center">

---

  <img src="composeApp/src/desktopMain/resources/icons/vinovim-app-icon-transparent.png" alt="Vinovim" width="60" />

  <h2>English</h2>

  <p>Your media player. Your sources. Your way.</p>

</div>

Vinovim Desktop is a free, open-source media app for Windows, macOS, and Linux. Bring your own content sources — Vinovim organizes everything into a library with artwork, ratings, automatic subtitles, and playback progress synced across your devices.

> ⚠️ **Alpha — Testers only.** Active development, frequent changes, expect bugs.

### Download

Get the latest release from [GitHub Releases](https://github.com/vitinv1t/vinovim-desktop/releases).

| Platform | Format |
|---|---|
| Windows | `.msi` |
| macOS | `.dmg` |
| Linux | `.deb` · `.rpm` · `.flatpak` · `.AppImage` |

### Build from source

```bash
git clone https://github.com/vitinv1t/vinovim-desktop.git
cd vinovim-desktop
./gradlew :composeApp:run
```

### Tech Stack

| Layer | Technology |
|---|---|
| UI | Compose Multiplatform |
| Language | Kotlin Multiplatform |
| Video (Android) | ExoPlayer + libass + libmpv |
| Video (Desktop) | libmpv via JNI |
| Networking | Ktor |
| DI | Koin |
| Async | Kotlin Coroutines & Flow |
| P2P Streaming | Vinovim Engine |
| Monitoring | Sentry |

### Contributing

Read [CONTRIBUTING.md](CONTRIBUTING.md) before opening a pull request.

### License

[GNU General Public License v3.0](LICENSE)
