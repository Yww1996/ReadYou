<div align="center">

<img src="fastlane/metadata/android/en-US/images/icon.png" width="120" alt="Liquid Reader icon">

# Liquid Reader

Ein Android-RSS-Reader mit einer Liquid-Glass-Filterleiste, entwickelt mit Kotlin und Jetpack Compose. Ein persönlicher Fork von Read You.

[![Version](https://img.shields.io/github/v/release/Yww1996/ReadYou)](https://github.com/Yww1996/ReadYou/releases/latest)
[![License](https://img.shields.io/github/license/Yww1996/ReadYou)](LICENSE)
[![Build](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml/badge.svg)](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml)

[English](README.md) · [简体中文](README-zh-CN.md) · [繁體中文](README-zh-TW.md) · [Deutsch](README-de.md) · [日本語](README-ja.md) · [فارسی](README-fa.md)

</div>

## Funktionen

- RSS-Abonnements und OPML-Import/-Export
- Lokale Konten sowie Fever, Google Reader API und FreshRSS
- Artikelextraktion, Volltextanalyse und Vorlesen
- Material You und Liquid Glass ab Android 13
- Einheitliche Glasfärbung im hellen und dunklen Design

## Download

**[Liquid Reader APK →](https://github.com/Yww1996/ReadYou/releases/latest)**

Die signierte APK ist in den Releases dieses Repositorys verfügbar. Andere Stores bieten die Originalversion an.

## Voraussetzungen

Android 8.0+ (API 26); Liquid Glass benötigt Android 13+ (API 33).

## Build

JDK 17 und Android SDK 36. Der Workflow führt Unit-Tests aus und erstellt die GitHub-Release-APK.

```sh
git clone https://github.com/Yww1996/ReadYou.git
cd ReadYou
./gradlew testGithubReleaseUnitTest assembleGithubRelease --max-workers=2
```

[GitHub Actions](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml) · [Issues](https://github.com/Yww1996/ReadYou/issues)

## Ursprung und Lizenz

Basiert auf [Read You](https://github.com/ReadYouApp/ReadYou). Hinweise auf ursprüngliche Autoren bleiben erhalten. [GNU GPL v3.0](LICENSE).

## Credits

### Open Source Projekte

- [MusicYou](https://github.com/Kyant0/MusicYou)
- [ParseRSS](https://github.com/muhrifqii/ParseRSS)
- [Readability4J](https://github.com/dankito/Readability4J)
- [opml-parser](https://github.com/mdewilde/opml-parser)
- [compose-html](https://github.com/ireward/compose-html)
- [Rome](https://github.com/rometools/rome)
- [Feeder](https://gitlab.com/spacecowboy/Feeder)
- [Seal](https://github.com/JunkFood02/Seal)
- [news-flash](https://gitlab.com/news-flash)
- [besticon](https://github.com/mat/besticon)
- [Jiffy Reader](https://github.com/ansh/jiffyreader.com)
- ...

### Special Thanks

[<img src="https://avatars.githubusercontent.com/u/76829190?v=4" width="180" height="180" style="display: block; border: 1px solid #f5f5f5; border-radius: 9999px;"/>](https://github.com/Kyant0)

Vielen Dank an **@Kyant0** für die Design-Inspiration und die Implementierung der Monet-Engine für **Read You**.

[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png" width="200" alt="Copyright © 2000-2023 JetBrains s.r.o. JetBrains and the JetBrains logo are registered trademarks of JetBrains s.r.o."/>](https://www.jetbrains.com/)

Vielen Dank an **JetBrains** für die Bereitstellung von kostenlosen Open-Source-Lizenzen für IDEs für **Read You**.

[<img src="https://hosted.weblate.org/widgets/readyou/-/287x66-white.png"  width="200"/>](https://hosted.weblate.org/engage/readyou/)

Vielen Dank an **Weblate** für das kostenlose Hosting von Open-Source-Projekten für **Read You**.

## Lizenz

GNU GPL v3.0 © [Read You](https://github.com/ReadYouApp/ReadYou/blob/main/LICENSE)
