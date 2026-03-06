# Galaxy Watch 8 mit Android Studio verbinden

## Schritt-für-Schritt Anleitung

### Schritt 1: Developer Mode aktivieren (Galaxy Watch 8)

**Galaxy Watch 8 hat ein neues Menü-System!**

1. **Watch-Display antippen** um zu aktivieren
2. **Von oben nach unten wischen** (Quick Settings öffnen)
3. **Settings Icon** (Zahnrad) antippen
4. Scrolle down zu: **Apps > Settings** (oder einfach **Settings**)
5. Gehe zu: **About Watch**
6. Scrolle zu **Software Version** oder **Build Number**
7. **7x auf die Versionsnummer tippen** (Build Number)
8. Warte bis "Developer Mode activated" erscheint

**Falls du nur Settings siehst:**
- Gehe zu: **Settings > Apps**
- Suche nach **Developer Options** oder **System**
- Öffne diese
- Scrolle zu **Developer Mode** und aktiviere es

### Schritt 2: USB Debugging aktivieren

1. **Von oben nach unten wischen** → Settings öffnen
2. **Settings > Apps > Settings** (oder die System-App)
3. Scrolle zu **Developer > Developer Options** oder direkt **Developer**
4. Aktiviere:
   - **ADB Debugging** (oder **USB Debugging**)
   - **Network ADB** (sehr wichtig!)
5. Die Watch zeigt dann eine IP-Adresse an - **diese notieren!**

### Schritt 3: Watch mit PC verbinden (kabellos)

#### Option A: Über Samsung SmartThings App (einfachster Weg)
1. **Samsung SmartThings App** auf deinem Phone installieren
2. Watch & Phone pairen (falls nicht schon getan)
3. In SmartThings: Watch > Settings > Developer
4. **ADB über Netzwerk** aktivieren
5. IP-Adresse notieren

#### Option B: Manuelle ADB-Verbindung

**1. Watch mit USB-Dock laden UND gleichzeitig mit PC verbinden:**
   - Optisches Ladepad der Watch anschließen
   - Watch mit USB-Kabel/Dock zu PC verbinden
   - Oder: USB-Hub verwenden zum parallel laden

**2. Terminal/CMD öffnen und ausführen:**
   ```bash
   adb devices
   ```
   - Watch sollte in der Liste erscheinen (z.B. `FA8..., device`)

**3. TCP-Modus aktivieren:**
   ```bash
   adb tcpip 5555
   ```

**4. Watch IP-Adresse finden:**
   - Watch: Einstellungen > About > IP-Adresse
   - Oder im Terminal:
   ```bash
   adb shell ip addr show
   ```
   - Suche nach einer IP wie `192.168.x.x`

**5. Kabellos verbinden:**
   ```bash
   adb connect 192.168.1.XXX:5555
   ```
   (Ersetze XXX mit der IP von Schritt 4)

**6. USB-Kabel abziehen** - Watch bleibt nun kabellos verbunden!

### Schritt 4: In Android Studio prüfen

**Run > Select Device:**
- Deine Galaxy Watch 8 sollte jetzt in der Liste auftauchen
- Oder: **Edit Configurations > Device** und Watch auswählen

**Oder manuell prüfen:**
```bash
adb devices
```
Sollte zeigen:
```
FA8XXXXX         device    (deine Watch)
emulator-5554    device    (falls Emulator läuft)
```

### Schritt 5: App ausführen
1. Android Studio: **Run > Run 'app'**
2. Wähle deine **Galaxy Watch 8**
3. **OK** - App wird installiert & gestartet

---

## Häufige Probleme

### Watch wird nicht erkannt
**Lösung:**
```bash
# Watch neu verbinden
adb disconnect
adb connect 192.168.1.XXX:5555

# Oder ADB neu starten
adb kill-server
adb start-server
```

### IP-Adresse vergessen
```bash
# Watch am PC anschließen
adb devices

# Wenn verbunden:
adb shell ip addr show

# Suche Zeile mit "inet 192.168"
```

### "Connection refused" beim Connect
- Watch ist zu weit weg / WiFi-Verbindung schwach
- Watch von USB abziehen und neu verbinden
- Neustart: Watch & PC neu starten

### Watch zeigt "Allow USB Debugging?" Popup
- Antippe auf der Watch **ALLOW**
- Dann sollte sie verbunden sein

---

## Schnell-Referenz

```bash
# Prüfe Verbindung
adb devices

# Verbinde kabellos (IP ersetzen!)
adb connect 192.168.1.100:5555

# Trenne Verbindung
adb disconnect

# Sehe alle verbundenen Geräte
adb devices -l

# Watch neu starten (falls nötig)
adb reboot
```

## Wenn alles nicht klappt

Versuche den **Wear OS Emulator** stattdessen:
- Android Studio: Tools > Device Manager
- Create Device > Wear OS
- Viel weniger Probleme beim Entwickeln!

Die physische Watch brauchst du erst später zum finalen Testen.
