# Android Build Setup Guide

Das Android-Projekt wurde erfolgreich mit Capacitor initialisiert. Es gibt jedoch ein bekanntes Gradle-Kompatibilitätsproblem mit Java-Versionen, das in Android Studio automatisch gelöst wird.

## Quick Start

### Option 1: Android Studio (empfohlen)
```bash
npm run cap:open:android
```

Das öffnet Android Studio mit dem Projekt. Android Studio wird:
- Die richtige JDK-Version automatisch erkennen
- Gradle-Daemon clearen
- Das Projekt automatisch synchronisieren

Wenn du gefragt wirst, akzeptiere alle Gradle-Updates.

### Option 2: Kommandozeile
```bash
cd android
./gradlew clean build
```

## Häufige Fehler & Lösungen

### Problem: "Unsupported class file major version 65"
**Ursache:** Java-Version ist zu neu für Gradle 8.0.2

**Lösung:**
1. Android Studio öffnen: `npm run cap:open:android`
2. Menu: **File > Project Structure > SDK Location**
3. JDK location auf die richtige Version setzen
4. Gradle sync erneut starten

### Problem: "Could not find tools.jar"
**Lösung:** Android Studio neu starten oder JDK Location überprüfen

## Entwicklung

### Projekt bauen:
```bash
npm run cap:build    # Build + Sync
```

### Auf Gerät/Emulator installieren:
```bash
npm run cap:open:android
# Dann in Android Studio: Run > Run 'app'
```

### Live Reload während Entwicklung:
```bash
npm run start        # Dev Server
npm run cap:sync     # Code in Android sync
# Im Android Studio dann auf Run klicken
```

## Weitere Ressourcen

- [Capacitor Android Docs](https://capacitorjs.com/docs/android)
- [Gradle Troubleshooting](https://gradle.org/install/)
- [Android Studio Setup](https://developer.android.com/studio/intro)
