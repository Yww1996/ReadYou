<div align="center">

<img src="fastlane/metadata/android/en-US/images/icon.png" width="120" alt="Liquid Reader icon">

# Liquid Reader

Kotlin と Jetpack Compose で構築した、液体ガラスのフィルターバーを備える Android RSS リーダーです。Read You の個人フォークです。

[![Version](https://img.shields.io/github/v/release/Yww1996/ReadYou)](https://github.com/Yww1996/ReadYou/releases/latest)
[![License](https://img.shields.io/github/license/Yww1996/ReadYou)](LICENSE)
[![Build](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml/badge.svg)](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml)

[English](README.md) · [简体中文](README-zh-CN.md) · [繁體中文](README-zh-TW.md) · [Deutsch](README-de.md) · [日本語](README-ja.md) · [فارسی](README-fa.md)

</div>

## 機能

- RSS 購読と OPML のインポート・エクスポート
- ローカルアカウント、Fever、Google Reader API、FreshRSS に対応
- 本文抽出、全文解析、読み上げ、読書設定
- Material You と Android 13 以降の液体ガラス表示
- ライト・ダーク両テーマでガラスの色調を統一

## ダウンロード

**[Liquid Reader APK →](https://github.com/Yww1996/ReadYou/releases/latest)**

このリポジトリの Releases から署名済み APK をダウンロードしてください。他のストアは上流版を配布しています。

## 動作環境

Android 8.0 以降（API 26）。液体ガラスには Android 13 以降（API 33）が必要です。

## ビルド

JDK 17 と Android SDK 36 が必要です。ワークフローは単体テストと APK ビルドを実行します。

```sh
git clone https://github.com/Yww1996/ReadYou.git
cd ReadYou
./gradlew testGithubReleaseUnitTest assembleGithubRelease --max-workers=2
```

[GitHub Actions](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml) · [Issues](https://github.com/Yww1996/ReadYou/issues)

## 上流とライセンス

[Read You](https://github.com/ReadYouApp/ReadYou) を基にしています。原作者と貢献者の表記を保持しています。[GNU GPL v3.0](LICENSE)。

## クレジット

### オープンソースプロジェクト

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
- ...

### スペシャルサンクス

[<img src="https://avatars.githubusercontent.com/u/76829190?v=4" width="180" height="180" style="display: block; border: 1px solid #f5f5f5; border-radius: 9999px;"/>](https://github.com/Kyant0)

Thanks to **@Kyant0** for the design inspiration and Monet engine implementation for **Read You**.

[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png" width="200" alt="Copyright © 2000-2023 JetBrains s.r.o. JetBrains and the JetBrains logo are registered trademarks of JetBrains s.r.o."/>](https://www.jetbrains.com/)

Thanks to **JetBrains** for allocating free open-source licences for IDEs for **Read You**.

[<img src="https://hosted.weblate.org/widgets/readyou/-/287x66-white.png"  width="200"/>](https://hosted.weblate.org/engage/readyou/)

Thanks to **Weblate** for providing free hosting of open source projects for **Read You**.

## ライセンス

GNU GPL v3.0 © [Read You](https://github.com/ReadYouApp/ReadYou/blob/main/LICENSE)
