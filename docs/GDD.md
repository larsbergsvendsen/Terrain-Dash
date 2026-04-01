# Terrain Dash - Game Design Document

## 1. Spillkonsept

**Tittel:** Terrain Dash
**Sjanger:** Fysikkbasert sidescroller / racing
**Plattform:** Android (minimum API 24 / Android 7.0)
**Engine:** LibGDX med Box2D-fysikk
**Inspirasjon:** Hill Climb Racing, men raskere tempo, mer action og visuelt punchy

### Elevator Pitch

> Terrain Dash er et fartsfylt, fysikkbasert kjørespill der du raser gjennom ville terreng med et kjøretøy du må holde i balanse. Kontroller hellingen med touchen din, samle boost og mynt, og unngå å flippe - eller det er game over. Raskere, råere og kulere enn originalen.

### Kjerneloop

```
Start bane → Akselerere automatisk → Kontroller helling med touch →
Samle mynter/boost → Unngå flip/krasj → Nå mål eller sett rekord →
Oppgrader kjøretøy → Lås opp ny bane → Gjenta
```

---

## 2. Gameplay

### 2.1 Kontroller

| Input | Handling |
|-------|---------|
| **Venstre side av skjerm (hold)** | Tilte kjøretøy bakover (nese opp) |
| **Høyre side av skjerm (hold)** | Tilte kjøretøy fremover (nese ned) |
| **Dobbelt-tap høyre** | Aktivér nitro-boost (hvis tilgjengelig) |
| **Swipe opp** | Hopp (kun med hopp-oppgradering) |

Kjøretøyet akselererer automatisk fremover. Spilleren fokuserer 100% på balanseering og timing.

### 2.2 Fysikk og balansering

- **Box2D-fysikkmotor** for realistisk tyngdekraft, friksjon og kollisjon
- Kjøretøyet har et tyngdepunkt som reagerer på terrengets vinkel
- Ved over 160° rotasjon → kjøretøyet er "flippet" → liv tapt
- **Terreng-generering:** Forhåndsdesignede baner med håndlagde seksjoner som kombineres proseduralt
- Ulik friksjon per overflate (asfalt, sand, is, gjørme)

### 2.3 Fart og tempo

Forskjeller fra Hill Climb Racing for å oppnå raskere tempo:

| Aspekt | Hill Climb Racing | Terrain Dash |
|--------|-------------------|--------------|
| Hastighet | Moderat | 2-3x raskere basisfart |
| Boost | Nei | Nitro-boost med visuell effekt |
| Hindringer | Få | Ramper, loopings, knusbare objekter |
| Kamera | Fast zoom | Dynamisk zoom ut ved høy fart |
| Musikk | Chill | Energisk EDM/synthwave |

### 2.4 Banesystem

Spillet har **6 verdener** med **5 baner** i hver = **30 baner totalt** ved launch.

| # | Verden | Terreng | Spesielt |
|---|--------|---------|----------|
| 1 | **Countryside** | Grønne åser, gress | Tutorial, myke kurver |
| 2 | **Desert Storm** | Sand, klipper | Sandstorm-partikler, lav friksjon |
| 3 | **Arctic Rush** | Is, snø | Glatt is, snøskred-events |
| 4 | **Neon City** | Asfalt, ramper | Neonlys, loopings, boost-pads |
| 5 | **Volcano Ridge** | Lava, stein | Fallende steiner, smale stier |
| 6 | **Sky Highway** | Skyer, plattformer | Gapper å hoppe over, vind |

Hver bane har:
- **3 stjerner:** Basert på tid, mynter samlet, og ingen flip
- **Leaderboard:** Global rangliste per bane
- **Hemmelig rute:** Alternativ sti med ekstra belønning

### 2.5 Endless Mode

I tillegg til baner: en **Endless Mode** per verden der terrenget er proseduralt generert og blir gradvis vanskeligere. Highscore = distanse.

---

## 3. Kjøretøy

### 3.1 Kjøretøytyper

| # | Navn | Fart | Grep | Vekt | Spesielt | Lås opp |
|---|------|------|------|------|----------|---------|
| 1 | **Buggy** | ★★☆ | ★★★ | ★★☆ | Allrounder | Start |
| 2 | **Monstertruck** | ★★☆ | ★★★ | ★★★ | Store hjul, tåler mer | Verden 2 |
| 3 | **Sportsbil** | ★★★ | ★★☆ | ★☆☆ | Rask, lett å flippe | Verden 3 |
| 4 | **Rakett-sykkel** | ★★★ | ★☆☆ | ★☆☆ | Kan dobbelt-hoppe | Verden 4 |
| 5 | **Tank** | ★☆☆ | ★★★ | ★★★ | Knuser hindringer | Verden 5 |
| 6 | **Hovercraft** | ★★★ | ★★☆ | ★★☆ | Flyr kort, sakte fall | Verden 6 |

### 3.2 Oppgraderingssystem

Hvert kjøretøy kan oppgraderes i 5 kategorier, hver med 10 nivåer:

| Kategori | Effekt |
|----------|--------|
| **Motor** | Øker toppfart og akselerasjon |
| **Fjæring** | Bedre stabilitet, tåler hardere landinger |
| **Hjul** | Øker grep og friksjon |
| **Nitro** | Lengre og kraftigere boost |
| **Panser** | Tåler flere krasj/flip før game over |

Oppgraderinger kjøpes med **mynter** (in-game valuta).

---

## 4. Økonomi og progresjon

### 4.1 Valuta

| Valuta | Opptjening | Bruk |
|--------|-----------|------|
| **Mynter (gull)** | Samles på baner, belønning for stjerner | Oppgraderinger, nye kjøretøy |
| **Gems (premium)** | Daglige oppdrag, sjeldne funn, (valgfritt: IAP) | Skins, skip ventetid |

### 4.2 Progresjonssystem

```
Spiller-nivå (XP) ──→ Låser opp verdener
    │
    ├── Bane-stjerner ──→ Låser opp neste bane
    │
    ├── Mynter ──→ Oppgraderinger
    │
    └── Daglige oppdrag ──→ Gems + bonus-XP
```

### 4.3 Daglige oppdrag (eksempler)

- "Fullfør 3 baner uten å flippe"
- "Samle 500 mynter totalt"
- "Bruk nitro 10 ganger"
- "Sett ny personlig rekord på en bane"

---

## 5. Visuell stil

### 5.1 Art direction

- **Stil:** Semi-flat 2D med parallax-scrolling bakgrunner (3-4 lag)
- **Fargeprofil:** Mettet og fargerik, verden-spesifikke paletter
- **Partikkeleffekter:** Støv, gnister, nitro-flamme, eksplosjoner
- **Kamera:** Dynamisk zoom og shake ved høy fart / krasj

### 5.2 UI-stil

- Clean, moderne UI med avrundede kort
- Stor, lesbar tekst
- Juicy animasjoner på knapper og overganger
- Minimalt HUD under spilling:
  - Fart (øverst venstre)
  - Mynter (øverst høyre)
  - Nitro-bar (nede til høyre)
  - Distanse/tid (øverst midt)

### 5.3 Skjermflyt

```
Splash → Hovedmeny
              │
              ├── Spill
              │    ├── Velg verden
              │    │    └── Velg bane
              │    │         └── Gameplay → Resultat → (Neste bane / Meny)
              │    └── Endless Mode
              │         └── Velg verden → Gameplay → Game Over / Highscore
              │
              ├── Garasje
              │    ├── Velg kjøretøy
              │    └── Oppgrader
              │
              ├── Butikk
              │    ├── Skins
              │    └── Gems (valgfri IAP)
              │
              ├── Daglige oppdrag
              │
              └── Innstillinger
                   ├── Lyd
                   ├── Kontroller (følsomhet)
                   └── Grafikk-kvalitet
```

---

## 6. Lyd

### 6.1 Musikk

| Kontekst | Stil | Tempo |
|-----------|------|-------|
| Meny | Synthwave, chill | 100 BPM |
| Countryside | Upbeat indie-rock | 130 BPM |
| Desert Storm | Arabisk-inspirert electronica | 140 BPM |
| Arctic Rush | Ambient drum & bass | 150 BPM |
| Neon City | Retrowave / synthwave | 145 BPM |
| Volcano Ridge | Heavy metal-inspirert synth | 160 BPM |
| Sky Highway | Trance / euphoric | 150 BPM |

### 6.2 Lydeffekter

- Motor-lyd (dynamisk pitch basert på fart)
- Hjul på ulike overflater (grus, is, asfalt)
- Nitro-boost (whoosh + bass)
- Mynter (pling med stigende tonehøyde ved combo)
- Krasj / flip (metallisk smell + glass)
- Landinger (fjæring-lyd, tyngre = kraftigere)
- UI-lyder (klikk, swipe, stjerne-opptelling)

---

## 7. Teknisk arkitektur

### 7.1 Teknologivalg

| Komponent | Teknologi |
|-----------|-----------|
| **Game Engine** | LibGDX 1.12+ |
| **Fysikk** | Box2D (innebygd i LibGDX) |
| **Byggeverktøy** | Gradle 8+ |
| **Språk** | Kotlin (Android) + Java (LibGDX core) |
| **Min Android API** | 24 (Android 7.0) |
| **Target API** | 34 (Android 14) |
| **Grafikk** | OpenGL ES 3.0 |
| **Lyd** | LibGDX Audio API |
| **Lagring** | SharedPreferences + JSON for save-data |
| **Analytics** | Firebase Analytics (valgfritt) |

### 7.2 Prosjektstruktur

```
terrain-dash/
├── core/                          # Plattform-uavhengig spillkode
│   └── src/main/java/com/terraindash/
│       ├── TerrainDashGame.java   # Hoved Game-klasse
│       ├── screens/               # Skjermhåndtering
│       │   ├── MenuScreen.java
│       │   ├── GameScreen.java
│       │   ├── GarageScreen.java
│       │   ├── ResultScreen.java
│       │   └── ...
│       ├── world/                 # Spillverden
│       │   ├── GameWorld.java     # Box2D-verden + logikk
│       │   ├── TerrainGenerator.java
│       │   ├── TerrainChunk.java
│       │   └── WorldRenderer.java
│       ├── entities/              # Spillobjekter
│       │   ├── Vehicle.java
│       │   ├── VehicleConfig.java
│       │   ├── Coin.java
│       │   ├── BoostPad.java
│       │   └── Obstacle.java
│       ├── physics/               # Fysikk-hjelpere
│       │   ├── PhysicsWorld.java
│       │   ├── ContactHandler.java
│       │   └── VehiclePhysics.java
│       ├── ui/                    # UI-komponenter
│       │   ├── HUD.java
│       │   ├── GameUI.java
│       │   └── widgets/
│       ├── data/                  # Data og lagring
│       │   ├── PlayerProgress.java
│       │   ├── LevelData.java
│       │   ├── VehicleData.java
│       │   └── SaveManager.java
│       ├── audio/                 # Lydhåndtering
│       │   ├── MusicManager.java
│       │   └── SFXManager.java
│       ├── effects/               # Visuell effekt
│       │   ├── ParticleManager.java
│       │   ├── CameraController.java
│       │   └── ScreenShake.java
│       └── utils/                 # Verktøy
│           ├── Constants.java
│           ├── Assets.java
│           └── MathUtils.java
│
├── android/                       # Android-spesifikk kode
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   └── java/.../AndroidLauncher.kt
│   └── build.gradle.kts
│
├── desktop/                       # Desktop (for testing)
│   └── src/main/java/.../DesktopLauncher.java
│
├── assets/                        # Spillressurser
│   ├── textures/
│   │   ├── vehicles/
│   │   ├── terrain/
│   │   ├── ui/
│   │   └── effects/
│   ├── audio/
│   │   ├── music/
│   │   └── sfx/
│   ├── levels/                    # Bane-definisjoner (JSON)
│   ├── fonts/
│   └── shaders/
│
├── docs/                          # Dokumentasjon
│   ├── GDD.md
│   └── ARCHITECTURE.md
│
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

### 7.3 Kjernearkitektur - Klassediagram

```
TerrainDashGame (Game)
    │
    ├── ScreenManager
    │    ├── MenuScreen
    │    ├── GameScreen ──→ GameWorld ──→ PhysicsWorld (Box2D)
    │    │                      │              │
    │    │                      │              ├── Vehicle (Body + Fixtures)
    │    │                      │              ├── Terrain (ChainShape)
    │    │                      │              └── ContactHandler
    │    │                      │
    │    │                      ├── TerrainGenerator
    │    │                      ├── EntityManager
    │    │                      ├── WorldRenderer
    │    │                      └── CameraController
    │    │
    │    ├── GarageScreen
    │    └── ResultScreen
    │
    ├── SaveManager (SharedPreferences)
    ├── MusicManager
    ├── SFXManager
    └── Assets (AssetManager)
```

### 7.4 Game Loop

```
GameScreen.render(delta):
    1. Input → Les touch-input, beregn tilt-kraft
    2. Physics → Box2D world.step(), apply forces
    3. Logic → Sjekk flip, samle mynter, sjekk mål
    4. Camera → Oppdater kameraposisjon, zoom, shake
    5. Render → Tegn bakgrunn, terreng, entities, partikler
    6. UI → Tegn HUD overlay
```

### 7.5 Terreng-representasjon

Terrenget er definert som en serie høydepunkter (x, y) som danner en ChainShape i Box2D:

```json
{
  "world": "countryside",
  "level": 1,
  "terrain_points": [
    {"x": 0, "y": 5.0},
    {"x": 2, "y": 5.2},
    {"x": 4, "y": 5.8},
    {"x": 6, "y": 5.1},
    ...
  ],
  "surface_type": "grass",
  "friction": 0.8,
  "coins": [
    {"x": 10, "y": 7.0},
    {"x": 15, "y": 8.5}
  ],
  "obstacles": [...],
  "boost_pads": [...],
  "star_thresholds": {
    "time": 45,
    "coins": 50,
    "no_flip": true
  }
}
```

### 7.6 Kjøretøy-fysikk

```
Vehicle (Box2D):
    ├── Chassis (PolygonShape) - Hoveddelen
    │    └── Properties: density, friction, restitution
    │
    ├── Wheel Front (CircleShape)
    │    └── Connected via WheelJoint
    │         └── Properties: motorSpeed, maxTorque, frequency, damping
    │
    ├── Wheel Rear (CircleShape)
    │    └── Connected via WheelJoint
    │         └── Properties: motorSpeed, maxTorque, frequency, damping
    │
    └── Tilt Control:
         └── applyTorque() basert på touch-input
```

---

## 8. Milepæler og scope

### MVP (Minimum Viable Product)

- [ ] 1 verden (Countryside) med 5 baner
- [ ] 1 kjøretøy (Buggy) med oppgraderinger
- [ ] Kjernefysikk: kjøring, tilt, flip-deteksjon
- [ ] Grunnleggende terreng-rendering
- [ ] Mynter og scoring
- [ ] Enkel meny og resultatskjerm
- [ ] Lydeffekter

### v1.0

- [ ] Alle 6 verdener (30 baner)
- [ ] Alle 6 kjøretøy med oppgraderinger
- [ ] Endless Mode
- [ ] Nitro-boost system
- [ ] Partikkeleffekter
- [ ] Dynamisk kamera
- [ ] Full UI med garasje og butikk
- [ ] Musikk for alle verdener
- [ ] Daglige oppdrag
- [ ] Lokal leaderboard

### v1.1+ (Post-launch)

- [ ] Online leaderboards (Google Play Games)
- [ ] Nye verdener og kjøretøy
- [ ] Sesong-events
- [ ] Multiplayer ghost racing
- [ ] Custom vehicle skins

---

## 9. Risiko og utfordringer

| Risiko | Påvirkning | Mitigering |
|--------|-----------|------------|
| Box2D-tuning er vanskelig | Gameplay føles dårlig | Tidlig prototyp, mye playtesting |
| Mange baner å designe | Forsinker launch | Start med 1 verden, prosedural generering for Endless |
| Ytelse på eldre enheter | Lav FPS | LOD-system, reduserbare partikler |
| Balansering av økonomi | For grindy eller for lett | A/B-testing, justerbare server-side configs |

---

## 10. Referanser og inspirasjon

- **Hill Climb Racing** - Kjerngameplay og kontrollskjema
- **Alto's Odyssey** - Visuell stil og flyt
- **Jetpack Joyride** - Tempo og "one more run"-faktor
- **Sonic Dash** - Fart og visuell punch
- **Trials Frontier** - Fysikkbaserte utfordringer
