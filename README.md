# QuickClock - Arbeitszeit Tracker

Eine einfache, native Android-App zur Erfassung von Arbeitszeiten (Kommen/Gehen). Optimiert für Galaxy Watch und Smartphones.

## Features

- ✓ **Einfache Bedienung** - Zwei große Buttons für "Kommen" und "Gehen"
- ✓ **Lokale Speicherung** - Alle Daten werden auf dem Gerät gespeichert
- ✓ **Tagesübersicht** - Alle erfassten Zeiten sichtbar
- ✓ **Status-Anzeige** - Zeigt ob du gerade im Dienst bist
- ✓ **CSV-Export** - Deine Daten kannst du exportieren
- ✓ **Responsive Design** - Optimiert für kleine Bildschirme (Watch)
- ✓ **Offline** - Funktioniert komplett ohne Internetverbindung

## Tech Stack

- **Angular 17** - Frontend Framework
- **Capacitor** - Cross-platform mobile framework
- **TypeScript** - Programmiersprache
- **CSS3** - Styling mit CSS-Variablen

## Installation

### Voraussetzungen
- Node.js 18+
- npm oder yarn
- Android SDK (für Android-Build)
- Java Development Kit (JDK)

### Setup

```bash
# Abhängigkeiten installieren
npm install

# Development Server starten
npm run start

# Produktions-Build
npm run build:prod

# Mit Capacitor synchronisieren
npm run cap:sync

# Android Studio öffnen
npm run cap:open:android
```

## Verwendung

1. **Kommen:** App öffnen und "✓ Kommen" Button drücken
2. **Gehen:** "✗ Gehen" Button drücken
3. **Export:** Deine Zeiten als CSV exportieren
4. **Löschen:** Einzelne Einträge oder den ganzen Tag löschen

## Projektstruktur

```
src/
├── app/
│   ├── services/
│   │   └── worktime.service.ts  # Logik für Zeit-Erfassung
│   ├── app.component.ts         # Hauptkomponente
│   ├── app.component.html       # Template
│   └── app.component.css        # Styling
├── styles.css                   # Globale Styles
├── index.html                   # HTML Entry Point
└── main.ts                      # Angular Bootstrap
```

## Datenspeicherung

Alle Zeiten werden lokal in der Capacitor Preferences API gespeichert. Format:
```json
{
  "worktime_logs": {
    "2024-03-06": [
      {
        "id": "timestamp",
        "type": "kommen",
        "time": "08:00:00",
        "timestamp": 1709723100000,
        "date": "2024-03-06"
      }
    ]
  }
}
```

## Build für Galaxy Watch

Galaxy Watch läuft auf Wear OS (Android). Die App wird wie eine normale Android-App gebaut:

1. `npm run build:prod` - Produktions-Build erstellen
2. `npm run cap:sync` - Mit Capacitor synchronisieren
3. `npm run cap:open:android` - Android Studio öffnen
4. In Android Studio das Projekt öffnen und auf "Run" klicken

## Lizenz

MIT
