<div align="center">

<img src="fastlane/metadata/android/en-US/images/icon.png" width="120" alt="Liquid Reader icon">

# Liquid Reader

یک خبرخوان RSS اندروید با نوار فیلتر شیشه‌ای، ساخته‌شده با Kotlin و Jetpack Compose. این مخزن نسخهٔ شخصی‌سازی‌شدهٔ Read You است.

[![Version](https://img.shields.io/github/v/release/Yww1996/ReadYou)](https://github.com/Yww1996/ReadYou/releases/latest)
[![License](https://img.shields.io/github/license/Yww1996/ReadYou)](LICENSE)
[![Build](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml/badge.svg)](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml)

[English](README.md) · [简体中文](README-zh-CN.md) · [繁體中文](README-zh-TW.md) · [Deutsch](README-de.md) · [日本語](README-ja.md) · [فارسی](README-fa.md)

</div>

## ویژگی‌ها

- اشتراک RSS و ورود و خروج فایل OPML
- حساب محلی و پشتیبانی از Fever، Google Reader API و FreshRSS
- استخراج متن مقاله و خواندن با صدا
- طراحی Material You و شیشهٔ مایع در Android 13 و بالاتر
- رنگ‌بندی یکپارچهٔ شیشه در حالت روشن و تاریک

## دریافت

**[Liquid Reader APK →](https://github.com/Yww1996/ReadYou/releases/latest)**

APK امضاشده را از Releases همین مخزن دریافت کنید. فروشگاه‌های دیگر نسخهٔ اصلی را توزیع می‌کنند.

## نیازمندی‌ها

Android 8.0 یا بالاتر (API 26). شیشهٔ مایع به Android 13 یا بالاتر (API 33) نیاز دارد.

## ساخت

JDK 17 و Android SDK 36 لازم است. گردش کار، آزمون‌های واحد و ساخت APK را اجرا می‌کند.

```sh
git clone https://github.com/Yww1996/ReadYou.git
cd ReadYou
./gradlew testGithubReleaseUnitTest assembleGithubRelease --max-workers=2
```

[GitHub Actions](https://github.com/Yww1996/ReadYou/actions/workflows/build_commit.yaml) · [Issues](https://github.com/Yww1996/ReadYou/issues)

## پروژهٔ اصلی و مجوز

بر پایهٔ [Read You](https://github.com/ReadYouApp/ReadYou) با حفظ نام پدیدآورندگان و مشارکت‌کنندگان. [GNU GPL v3.0](LICENSE).

## الهام گرفته از:

-   [MusicYou](https://github.com/Kyant0/MusicYou)
-   [ParseRSS](https://github.com/muhrifqii/ParseRSS): [MIT](https://github.com/muhrifqii/ParseRSS/blob/master/LICENSE)
-   [Readability4J](https://github.com/dankito/Readability4J): [Apache License 2.0](https://github.com/dankito/Readability4J/blob/master/LICENSE)
-   [opml-parser](https://github.com/mdewilde/opml-parser): [Apache License 2.0](https://github.com/mdewilde/opml-parser/blob/master/LICENSE)
-   [compose-html](https://github.com/ireward/compose-html): [Apache License 2.0](https://github.com/ireward/compose-html/blob/main/LICENSE.txt)
-   [Rome](https://github.com/rometools/rome): [Apache License 2.0](https://github.com/rometools/rome/blob/master/LICENSE)
-   [Feeder](https://gitlab.com/spacecowboy/Feeder): [GPL v3.0](https://gitlab.com/spacecowboy/Feeder/-/blob/master/LICENSE)

## لایسنس

[GNU GPL v3.0](https://github.com/ReadYouApp/ReadYou/blob/main/LICENSE)
