<div align="center">
  <img src="assets/vinovim-logo.png" alt="Vinovim" width="120" />

  <h1>Vinovim Desktop</h1>

  Um app de mídia gratuito e open-source para **Windows**, **macOS** e **Linux**.<br/>
  Traga suas próprias fontes. O Vinovim transforma tudo em uma biblioteca com capas, avaliações, legendas e seu progresso salvo em todos os dispositivos.

  [Site](https://vinovim.tv) · [Releases](https://github.com/vitinv1t/vinovim-desktop/releases) · [Suporte](https://vinovim.tv/support)

  ---

  *[English version below](#english)*
</div>

---

> ⚠️ **Software em Alpha — Desenvolvimento Lento — Apenas para Testadores**
>
> O Vinovim Desktop está em alpha e é destinado apenas a testadores. Está em desenvolvimento e não é adequado para uso diário.
>
> Espere mudanças incompatíveis a cada atualização. Funcionalidades, configurações, dados armazenados e compatibilidade podem mudar ou parar de funcionar sem aviso. Não use esta versão como seu app de mídia principal e reporte qualquer problema encontrado durante os testes.

---

## Baixar o Vinovim Desktop

Baixe a versão mais recente em [GitHub Releases](https://github.com/vitinv1t/vinovim-desktop/releases).

- **Windows:** instalador MSI
- **macOS:** instalador DMG
- **Linux:** DEB, RPM, Flatpak ou AppImage

---

## Compilar do código-fonte

```bash
git clone https://github.com/vitinv1t/vinovim-desktop.git
cd vinovim-desktop
```

### Executar o app

```bash
./gradlew :composeApp:run
```

**No Windows PowerShell:**

```powershell
.\gradlew.bat :composeApp:run
```

### Empacotar o app

Gera um pacote de release para o sistema atual:

```bash
./gradlew :composeApp:packageReleaseDistributionForCurrentOS
```

**Por plataforma:**

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

O app compartilhado é construído com [Kotlin Multiplatform](https://www.jetbrains.com/kotlin-multiplatform/) e [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/).

---

## Android & iOS

O Vinovim também roda em Android e iOS a partir do mesmo código compartilhado.

- **Android:** `./gradlew :androidApp:assembleRelease`
- **iOS:** abra `iosApp/iosApp.xcodeproj` no Xcode

---

## Stack Tecnológica

| Camada | Tecnologia |
|---|---|
| UI | Compose Multiplatform (Desktop / Android / iOS) |
| Linguagem | Kotlin Multiplatform |
| Vídeo (Android) | ExoPlayer + libass + libmpv |
| Vídeo (Desktop) | libmpv via JNI |
| Rede | Ktor |
| DI | Koin |
| Assíncrono | Kotlin Coroutines & Flow |
| Streaming P2P | Vinovim Engine (BitTorrent customizado) |
| Relatórios de Erros | Sentry |

---

## Contribuindo

Contribuições são bem-vindas. Leia o [CONTRIBUTING.md](CONTRIBUTING.md) antes de abrir um pull request.

- Bugs → [GitHub Issues](https://github.com/vitinv1t/vinovim-desktop/issues)
- Sugestões → [GitHub Issues](https://github.com/vitinv1t/vinovim-desktop/issues)
- Pull Requests → [GitHub Pull Requests](https://github.com/vitinv1t/vinovim-desktop/pulls)

---

## Licença

[GNU General Public License v3.0](LICENSE)

---

<a name="english"></a>

<div align="center">
  <h2>🇬🇧 English</h2>
</div>

A free, open-source media app for **Windows**, **macOS**, and **Linux**.<br/>
Bring your own sources. Vinovim turns them into a library with artwork, ratings, subtitles, and your place saved on every screen.

> ⚠️ **Alpha Software — Slow Development — Testers Only**
>
> Vinovim Desktop is currently in alpha and is intended only for testers. It is under active development and not suitable for daily use.
>
> Expect breaking changes with every update. Features, settings, stored data, and compatibility may change or stop working without notice. Do not rely on this build as your primary media app, and report any issues you encounter.

### Get Vinovim Desktop

Download the latest build from [GitHub Releases](https://github.com/vitinv1t/vinovim-desktop/releases).

- **Windows:** MSI installer
- **macOS:** DMG installer
- **Linux:** DEB, RPM, Flatpak, or AppImage

### Build from source

```bash
git clone https://github.com/vitinv1t/vinovim-desktop.git
cd vinovim-desktop
./gradlew :composeApp:run
```

### Tech Stack

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

### Contributing

Contributions are welcome. Read [CONTRIBUTING.md](CONTRIBUTING.md) before opening a pull request.

### License

[GNU General Public License v3.0](LICENSE)
