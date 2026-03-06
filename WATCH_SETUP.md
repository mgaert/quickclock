# Galaxy Watch / Wear OS Setup Guide

Da die App für Galaxy Watch (Wear OS) entwickelt wurde, brauchst du einen entsprechenden Emulator oder ein physisches Gerät.

## Option 1: Physisches Gerät (empfohlen)

### Voraussetzungen:
- Samsung Galaxy Watch oder andere Wear OS Watch
- USB-Kabel
- USB-Debugging auf der Watch aktiviert

### Setup:

1. **Watch mit USB verbinden**
2. **USB-Debugging aktivieren** auf der Watch:
   - Einstellungen > Entwickler > USB-Debugging aktivieren
3. **In Android Studio**:
   - Run > Run 'app' öffnen
   - Device Selector sollte deine Watch anzeigen
   - Select und starten

## Option 2: Wear OS Emulator (für Entwicklung)

### Schritt 1: AVD Manager öffnen
```
Android Studio > Tools > Device Manager > Create Device
```

### Schritt 2: Wear OS Profil auswählen
1. **Category**: Wearables
2. **Device**: Wähle eine Watch (z.B. "Wear OS 3 Round")
3. **Next**

### Schritt 3: System Image auswählen
1. **Release Channel**: Recommended
2. **API Level**: 33 oder höher (z.B. 34)
3. **Download** (wenn notwendig)
4. **Next**

### Schritt 4: AVD konfigurieren
- **Name**: z.B. "Wear OS Emulator" oder "Galaxy Watch 5"
- **Startup size & orientation**: Standard
- **Finish**

### Schritt 5: Emulator starten
- Im Device Manager: **Play-Button** neben dem Emulator
- Warten bis er vollständig bootet (ca. 2-3 Minuten)

### Schritt 6: App ausführen
```
Run > Run 'app'
```
Wähle den Wear OS Emulator aus der Device-Liste

## Option 3: Smartphone Emulator (für Testing)

Falls du keinen Wear OS Emulator brauchst, kannst du auch auf einem regulären Android-Phone-Emulator testen:

1. **Create Device** (Phone, nicht Wearable)
2. **Pixel 8** oder ähnlich
3. **API Level 34+**
4. App wird dort auch laufen

## Häufige Probleme

### "No Devices Found"
**Lösung:**
- Emulator im Device Manager starten
- Oder: Tools > Device Manager > Warte bis Emulator aktiv ist
- Dann Run > Run 'app' erneut versuchen

### Emulator startet nicht
**Versuche:**
1. Emulator im Device Manager neu starten
2. Android Studio neu starten
3. Virtualisierung in BIOS aktivieren (bei Computern)

### Wear OS Emulator sehr langsam
Das ist normal beim ersten Start. Nach dem vollständigen Boot wird es schneller.

### Watch ist verbunden, aber wird nicht erkannt
1. Developer Mode aktivieren auf Watch
2. USB-Debugging aktivieren
3. Auf der Watch "Allow" drücken, wenn es fragt
4. `adb devices` in Terminal ausführen zum Prüfen

## Schnell-Kommandos

```bash
# Prüfe verbundene Geräte
adb devices

# Emulator aus Command Line starten
emulator -avd "Wear OS Emulator"

# App direkt bauen und installieren
npm run cap:build
cd android
./gradlew installDebug
```

## Für Galaxy Watch spezifisch

Wenn du eine echte Galaxy Watch hast:

1. **Watch & Phone pairen** (falls nicht schon getan)
2. **Developer Mode aktivieren**:
   - Watch: Einstellungen > About > Build #7x antippen
3. **USB Debugging aktivieren**
4. **Watch mit PC verbinden**
5. Prüfe mit `adb devices` ob erkannt

## Weitere Ressourcen

- [Android Emulator Docs](https://developer.android.com/studio/run/emulator)
- [Wear OS Development](https://developer.android.com/wear)
- [Galaxy Watch Developer Guide](https://developer.samsung.com/watch)
