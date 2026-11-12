<div align="center">

<img src="fastlane/metadata/android/en-US/images/icon.png" width="120" alt="Liquid Reader icon">

# Liquid Reader

採用液態玻璃篩選列的 Android RSS 閱讀器，使用 Kotlin 與 Jetpack Compose 建置。本儲存庫是 Read You 的個人修改版。

[![Version](https://img.shields.io/github/v/release/Yww1996/ReadYou)](https://github.com/Yww1996/ReadYou/releases/latest)
[![License](https://img.shields.io/github/license/Yww1996/ReadYou)](LICENSE)
[![Build](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml/badge.svg)](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml)

[English](README.md) · [简体中文](README-zh-CN.md) · [繁體中文](README-zh-TW.md) · [Deutsch](README-de.md) · [日本語](README-ja.md) · [فارسی](README-fa.md)

</div>

## 功能

- RSS 訂閱與 OPML 匯入、匯出
- 本機帳戶及 Fever、Google Reader API、FreshRSS 整合
- 內文擷取、全文解析、朗讀及閱讀偏好設定
- Material You 主題與 Android 13+ 液態玻璃篩選列
- 淺色及深色模式統一玻璃調色；較舊系統使用一般面板

## 下載

**[Liquid Reader APK →](https://github.com/Yww1996/ReadYou/releases/latest)**

請從本儲存庫 Release 下載已簽署 APK。其他商店提供的是上游版本。

## 系統需求

Android 8.0 以上（API 26）；液態玻璃需要 Android 13 以上（API 33）。

## 建置

需要 JDK 17 與 Android SDK 36。工作流程會執行單元測試並建置 GitHub Release APK。

```sh
git clone https://github.com/Yww1996/ReadYou.git
cd ReadYou
./gradlew testGithubReleaseUnitTest assembleGithubRelease --max-workers=2
```

[GitHub Actions](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml) · [Issues](https://github.com/Yww1996/ReadYou/issues)

## 上游與授權

修改自 [Read You](https://github.com/ReadYouApp/ReadYou)，保留原作者及貢獻者聲明，採用 [GNU GPL v3.0](LICENSE)。

## 致謝

### 開源專案

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

### 特別感謝

[<img src="https://avatars.githubusercontent.com/u/76829190?v=4" width="180" height="180" style="display: block; border: 1px solid #f5f5f5; border-radius: 9999px;"/>](https://github.com/Kyant0)

感謝 **@Kyant0** 為 **Read You** 提供的設計靈感與 Monet 引擎實作。

[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png" width="200" alt="Copyright © 2000-2023 JetBrains s.r.o. JetBrains and the JetBrains logo are registered trademarks of JetBrains s.r.o."/>](https://www.jetbrains.com/)

感謝 **JetBrains** 為 **Read You** 提供免費的開源 IDE 授權。

[<img src="https://hosted.weblate.org/widgets/readyou/-/287x66-white.png" width="200"/>](https://hosted.weblate.org/engage/readyou/)

感謝 **Weblate** 為 **Read You** 提供免費的開源專案翻譯託管服務。

## 許可證

GNU GPL v3.0 © [Read You](https://github.com/ReadYouApp/ReadYou/blob/main/LICENSE)
