# Hindustan Tube Pro — Android Build

A Trusted Web Activity (TWA) wrapper that packages the web app as a native Android APK
and Android App Bundle (AAB) with full PWA capabilities.

## Why TWA?

* Native install + launcher icon + full-screen experience
* Access to notification channels, file picker, share sheet, and Android Shortcuts
* Updates ship automatically (the APK is just a thin shell over the deployed web app)
* Single code-base — no duplicated UI / state code

## Project layout

```
android/
├── app/
│   ├── build.gradle          # App module config (SDK, signing, Proguard)
│   ├── proguard-rules.pro    # Release shrinker rules
│   └── src/main/
│       ├── AndroidManifest.xml  # TWA + intent filters + asset-statements
│       ├── java/app/htp/pro/   # MainActivity (Kotlin) + HTPApp + OriginVerifier
│       └── res/
│           ├── drawable/         # splash.xml, ic_launcher_foreground.xml
│           ├── mipmap-*/         # Adaptive launcher icon
│           ├── values/           # colors.xml, strings.xml, themes.xml
│           └── xml/              # network_security_config, shortcuts, file_paths
├── build.gradle            # Top-level build script
├── settings.gradle         # Module registration
├── gradle.properties       # JVM + AndroidX flags
├── gradle/wrapper/         # Gradle wrapper jar + properties
└── keystore/               # Release keystore generation instructions
```

## Quick start (requires JDK 17 + Android SDK 34+)

```bash
cd android
./gradlew assembleRelease              # → APK at app/build/outputs/apk/release/
./gradlew bundleRelease                # → AAB at app/build/outputs/bundle/release/
./gradlew installRelease               # Install on connected device
```

To override the production domain (default `hindustantube.pro`):

```bash
HTP_HOST=preview.example.com ./gradlew assembleRelease
```

## Digital asset verification (TWA)

For full "Open in app" behaviour on Android 12+ you must host an
assetlinks.json at `https://hindustantube.pro/.well-known/assetlinks.json`
**and** a matching fingerprint declared in the keystore. A template is
provided at `app/src/main/res/xml/asset_statements.json` and the
`AndroidManifest.xml` already contains the matching meta-data.

```json
[{
  "relation": ["delegate_permission/common.handle_all_urls"],
  "target": { "namespace": "android_app", "package_name": "app.htp.pro",
              "sha256_cert_fingerprints": ["PASTE_YOUR_SHA256_HERE"] }
}]
```

Print your SHA-256 fingerprint with:

```bash
keytool -list -v -keystore android/keystore/release.keystore \
  -alias htp -storepass htp-storepass | grep SHA256
```

## What's preconfigured

* `app.htp.pro` package + `Hindustan Tube Pro` label
* 4-tab launcher shortcuts (Home / Shorts / Create) via `<shortcuts>`
* Material-3 splash using the brand `splash_background` (#0F1115)
* Adaptive launcher icon (vector — installs cleanly on any density)
* Network security config allowing cleartext only on localhost / 10.0.2.2 (emulator)
* FileProvider for sharing downloaded media into other apps
* `POST_NOTIFICATIONS`, `READ_MEDIA_VIDEO`, `READ_MEDIA_IMAGES` permissions
  wired for upload / chat media features
* Proguard rules keeping TWA entry points and stripping Log calls in release

## Build the keystore (one-time)

```bash
keytool -genkey -v \
  -keystore android/keystore/release.keystore \
  -alias htp -keyalg RSA -keysize 2048 -validity 36500 \
  -storepass htp-storepass -keypass htp-keypass \
  -dname "CN=Hindustan Tube Pro, O=Hindustan Tube Labs, C=IN"
```

## CI signing

Set these environment variables for unattended builds:

| Variable | Purpose |
| --- | --- |
| `HTP_KEYSTORE` | Absolute path to the keystore file |
| `HTP_KEYSTORE_PWD` | Storepass |
| `HTP_KEY_ALIAS` | Key alias (default `htp`) |
| `HTP_KEY_PWD` | Keypass |
| `HTP_HOST` | Override default `hindustantube.pro` for the manifest |

## Gradle wrapper

The wrapper jar is committed to the repo so first-time contributors do not need
Gradle pre-installed. The wrapper version is `gradle-8.10.2` — generate the
executable wrapper with:

```bash
gradle wrapper --gradle-version 8.10.2 --distribution-type bin
```

(On machines with Gradle pre-installed.)
