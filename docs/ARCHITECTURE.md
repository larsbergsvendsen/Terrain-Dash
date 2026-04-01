# Terrain Dash - Teknisk Arkitektur

## Oversikt

Terrain Dash bruker **LibGDX** som spillmotor med **Box2D** for fysikk. Prosjektet er strukturert som et multi-modul Gradle-prosjekt med tre moduler: `core`, `android` og `desktop`.

## Moduler

```
terrain-dash/
├── core/       → All spillogikk, plattform-uavhengig
├── android/    → Android launcher og plattform-spesifikk kode
├── desktop/    → Desktop launcher for rask testing under utvikling
└── assets/     → Delte ressurser (bilder, lyd, banedata)
```

## Arkitektur-prinsipper

### 1. Screen-basert navigasjon

LibGDX sin `Screen`-interface brukes for all navigasjon. Hver skjerm er en selvstendig enhet som håndterer sin egen rendering og input.

```
Game (TerrainDashGame)
  └── setScreen(screen)
       ├── SplashScreen      → Logo + lasting
       ├── MenuScreen         → Hovedmeny
       ├── WorldSelectScreen  → Velg verden
       ├── LevelSelectScreen  → Velg bane i verden
       ├── GameScreen         → Selve spillet
       ├── ResultScreen       → Resultat etter bane
       ├── GarageScreen       → Kjøretøy + oppgraderinger
       └── SettingsScreen     → Innstillinger
```

### 2. Entity-Component-mønster (forenklet)

Vi bruker et forenklet entity-mønster der entities er konkrete klasser (ikke fullt ECS):

```java
abstract class GameEntity {
    Vector2 position;
    Body body;                // Box2D body (nullable for non-physics)
    TextureRegion texture;

    abstract void update(float delta);
    abstract void render(SpriteBatch batch);
}
```

Konkrete entities:
- `Vehicle` - Spillerens kjøretøy med chassis + hjul
- `Coin` - Samlbar mynt med animasjon
- `BoostPad` - Gir nitro-boost ved kontakt
- `Obstacle` - Knusbar/fast hindring
- `Decoration` - Visuell dekorasjon uten kollisjon

### 3. Fysikk-lag

Box2D kjører i sin egen "verden" med meter som enhet (1 meter ≈ 64 piksler).

```
PhysicsWorld
  ├── World (Box2D)          → Gravitasjon, step()
  ├── ContactHandler          → Lytter på kollisjoner
  ├── DebugRenderer           → Viser Box2D-former (debug)
  └── Konvertering            → Piksler ↔ Meter
```

**Viktige fysikk-verdier:**

| Parameter | Verdi | Beskrivelse |
|-----------|-------|-------------|
| Gravitasjon | (0, -15) | Litt sterkere enn reell for mer action |
| Tidssteg | 1/60 | Fast 60 FPS fysikk |
| Velocity iterations | 8 | Box2D presisjon |
| Position iterations | 3 | Box2D presisjon |
| Piksler per meter | 64 | Rendering-skala |

### 4. Terreng-system

Terrenget er delt i **chunks** som lastes/unloades rundt kamera:

```
[Chunk -1] [Chunk 0] [CAMERA] [Chunk 1] [Chunk 2]
  (unload)  (aktiv)   (her)    (aktiv)   (preload)
```

Hvert chunk har:
- `ChainShape` for Box2D-kollisjon
- Visuell mesh for rendering (fylt polygon under terreng-linjen)
- Overflate-type (påvirker friksjon og visuell stil)
- Liste over entities (mynter, hindringer, etc.)

### 5. Kamera-system

```
CameraController
  ├── Mål-posisjon: Kjøretøy + offset fremover
  ├── Smoothing: Lerp mot mål (0.1)
  ├── Dynamisk zoom: Zoom ut ved høy fart
  ├── Look-ahead: Kamera leder foran kjøretøyet
  └── Shake: Amplitude + decay ved krasj/landing
```

Kameraet bruker en "look-ahead" teknikk der det alltid er litt foran kjøretøyet i kjøreretning, slik at spilleren ser hva som kommer.

### 6. Input-håndtering

```
TouchInput
  ├── Venstre halvdel → tiltBack = true
  ├── Høyre halvdel → tiltForward = true
  ├── Dobbelt-tap høyre → activateNitro()
  └── Ingen touch → ingen tilt-kraft
```

Input mattes til en `tiltForce` i range [-1, 1]:
- `-1` = maks tilt bakover
- `0` = ingen tilt
- `+1` = maks tilt fremover

Denne verdien ganges med kjøretøyets `tiltStrength` og sendes som torque til Box2D.

### 7. Data og lagring

All spilldata lagres lokalt med `SharedPreferences` (via LibGDX `Preferences`):

```json
{
  "player": {
    "xp": 1250,
    "level": 5,
    "coins": 3400,
    "gems": 25
  },
  "vehicles": {
    "buggy": {
      "unlocked": true,
      "engine_level": 3,
      "suspension_level": 2,
      "wheels_level": 4,
      "nitro_level": 1,
      "armor_level": 2
    }
  },
  "levels": {
    "countryside_1": {
      "completed": true,
      "best_time": 32.5,
      "stars": 3,
      "best_coins": 67
    }
  },
  "settings": {
    "music_volume": 0.8,
    "sfx_volume": 1.0,
    "tilt_sensitivity": 0.7,
    "graphics_quality": "high"
  }
}
```

### 8. Rendering pipeline

Rekkefølge per frame:

```
1. Tøm skjerm (glClear)
2. Parallax-bakgrunn (3-4 lag, forskjellig scroll-hastighet)
3. Terreng (fylt polygon + overflate-tekstur)
4. Entities bak kjøretøy (dekorasjoner)
5. Kjøretøy (chassis + hjul)
6. Entities foran kjøretøy (mynter, effekter)
7. Partikkeleffekter (støv, gnister, nitro)
8. HUD overlay (via Stage / Scene2D)
9. [Debug] Box2D debug-rendering
```

### 9. Asset-pipeline

```
assets/
├── textures/
│   ├── atlas.png + atlas.atlas    → TextureAtlas for alt 2D
│   └── Pakket med LibGDX TexturePacker
├── audio/
│   ├── music/*.ogg                → Streaming (ikke i minne)
│   └── sfx/*.wav                  → Forhåndslastet i minne
├── levels/
│   └── *.json                     → Bane-definisjoner
├── fonts/
│   └── *.fnt + *.png              → BitmapFont
└── shaders/
    └── *.glsl                     → Shader-programmer (valgfritt)
```

### 10. Ytelsesbudsjett

| Metrikk | Mål |
|---------|-----|
| FPS | 60 FPS stabil |
| Draw calls | < 50 per frame |
| Teksturminne | < 64 MB |
| Total APK | < 50 MB |
| Lastetid | < 3 sekunder |
| RAM-bruk | < 200 MB |

### 11. Testing-strategi

| Type | Verktøy | Fokus |
|------|---------|-------|
| Desktop-testing | Desktop launcher | Rask iterasjon på gameplay |
| Fysikk-testing | Box2D DebugRenderer | Sjekke kollisjon og krefter |
| Enhetstest | JUnit 5 | Spillogikk, matematikk |
| UI-testing | Manuell | Skjermflyt og responsivitet |
| Ytelsestest | Android Profiler | FPS, minne, batteri |
