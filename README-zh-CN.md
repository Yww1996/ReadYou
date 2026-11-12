<div align="center">

<img src="fastlane/metadata/android/en-US/images/icon.png" width="120" alt="Liquid Reader icon">

# Liquid Reader

采用液态玻璃筛选栏的 Android RSS 阅读器，使用 Kotlin 与 Jetpack Compose 构建。本仓库是 Read You 的个人修改版。

[![Version](https://img.shields.io/github/v/release/Yww1996/ReadYou)](https://github.com/Yww1996/ReadYou/releases/latest)
[![License](https://img.shields.io/github/license/Yww1996/ReadYou)](LICENSE)
[![Build](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml/badge.svg)](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml)

[English](README.md) · [简体中文](README-zh-CN.md) · [繁體中文](README-zh-TW.md) · [Deutsch](README-de.md) · [日本語](README-ja.md) · [فارسی](README-fa.md)

</div>

## 功能

- RSS 订阅与 OPML 导入、导出
- 本地账户，以及 Fever、Google Reader API、FreshRSS 服务集成
- 正文提取、全文解析、大声朗读与阅读偏好设置
- Material You 主题与 Android 13+ 液态玻璃筛选栏
- 浅色、深色模式统一玻璃调色；较旧系统使用常规面板

## 下载

**[Liquid Reader APK →](https://github.com/Yww1996/ReadYou/releases/latest)**

请从本仓库 Release 下载已签名 APK。其他应用商店提供的是上游版本，并非本修改版。

## 运行要求

Android 8.0 及以上（API 26）；液态玻璃需要 Android 13 及以上（API 33）。

## 构建

需要 JDK 17 与 Android SDK 36。工作流会运行单元测试并打包 GitHub Release APK。

```sh
git clone https://github.com/Yww1996/ReadYou.git
cd ReadYou
./gradlew testGithubReleaseUnitTest assembleGithubRelease --max-workers=2
```

[GitHub Actions](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml) · [Issues](https://github.com/Yww1996/ReadYou/issues)

## 上游与许可

基于 [Read You](https://github.com/ReadYouApp/ReadYou) 修改，保留原作者、贡献者及许可声明，采用 [GNU GPL v3.0](LICENSE) 发布。

## 致谢

### 开源项目

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

### 特别鸣谢

[<img src="https://avatars.githubusercontent.com/u/76829190?v=4" width="180" height="180" style="display: block; border: 1px solid #f5f5f5; border-radius: 9999px;"/>](https://github.com/Kyant0)

感谢 **@Kyant0** 为 **Read You** 提供的设计灵感与 Monet 引擎实现。

[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png" width="200" alt="Copyright © 2000-2023 JetBrains s.r.o. JetBrains and the JetBrains logo are registered trademarks of JetBrains s.r.o."/>](https://www.jetbrains.com/)

感谢 **JetBrains** 为 **Read You** 提供的开源项目产品许可证。

[<img src="https://hosted.weblate.org/widgets/readyou/-/287x66-white.png"  width="200"/>](https://hosted.weblate.org/engage/readyou/)

感谢 **Weblate** 为 **Read You** 提供的开源项目免费托管服务。

## 许可证

GNU GPL v3.0 © [Read You](https://github.com/ReadYouApp/ReadYou/blob/main/LICENSE)
