<div align="center">

<img src="fastlane/metadata/android/en-US/images/icon.png" width="120" alt="Liquid Reader icon">

# Liquid Reader

An Android RSS reader with a liquid-glass filter bar, built with Kotlin and Jetpack Compose. A personal fork of Read You.

[![Version](https://img.shields.io/github/v/release/Yww1996/ReadYou)](https://github.com/Yww1996/ReadYou/releases/latest)
[![License](https://img.shields.io/github/license/Yww1996/ReadYou)](LICENSE)
[![Build](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml/badge.svg)](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml)

[English](README.md) · [简体中文](README-zh-CN.md) · [繁體中文](README-zh-TW.md) · [Deutsch](README-de.md) · [日本語](README-ja.md) · [فارسی](README-fa.md)

</div>

## Features

- RSS subscriptions and OPML import/export
- Local accounts, Fever, Google Reader API and FreshRSS integration
- Article extraction, full-text parsing, read aloud, and reading preferences
- Material You themes and a liquid-glass filter bar on Android 13+
- Unified glass tint in light and dark themes; older Android versions use the standard surface

## Download

**[Liquid Reader APK →](https://github.com/Yww1996/ReadYou/releases/latest)**

Download the signed APK from this repository’s Releases. Other stores distribute upstream builds, not this fork.

## Requirements

Android 8.0+ (API 26). Liquid-glass rendering requires Android 13+ (API 33).

## Build

JDK 17 and Android SDK 36. The workflow runs unit tests and builds the GitHub release APK.

```sh
git clone https://github.com/Yww1996/ReadYou.git
cd ReadYou
./gradlew testGithubReleaseUnitTest assembleGithubRelease --max-workers=2
```

[GitHub Actions](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml) · [Issues](https://github.com/Yww1996/ReadYou/issues)

## Upstream and license

Based on [Read You](https://github.com/ReadYouApp/ReadYou). Original authors, contributors, and license notices are retained. Distributed under [GNU GPL v3.0](LICENSE).

## Credits

### Open Source Projects

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

Thanks to **@Kyant0** for the design inspiration and Monet engine implementation for **Read You**.

[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png" width="200" alt="Copyright © 2000-2023 JetBrains s.r.o. JetBrains and the JetBrains logo are registered trademarks of JetBrains s.r.o."/>](https://www.jetbrains.com/)

Thanks to **JetBrains** for allocating free open-source licences for IDEs for **Read You**.

[<img src="https://hosted.weblate.org/widgets/readyou/-/287x66-white.png"  width="200"/>](https://hosted.weblate.org/engage/readyou/)

Thanks to **Weblate** for providing free hosting of open source projects for **Read You**.

## License

GNU GPL v3.0 © [Read You](https://github.com/ReadYouApp/ReadYou/blob/main/LICENSE)
