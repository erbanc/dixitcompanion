# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Dixit Companion is an Android app (Java, minSdk 21, targetSdk 31) that serves as a score tracker for the Dixit board game. It is a single-module Gradle project under `dixit-companion/`.

## Build Commands

```bash
# Build the project
./gradlew build

# Build debug APK
./gradlew assembleDebug

# Build release APK (minified with ProGuard)
./gradlew assembleRelease

# Run unit tests (JVM, no device needed)
./gradlew test

# Run a single unit test class
./gradlew test --tests "fr.erban.dixitcompanion.ExampleUnitTest"

# Run instrumented tests (requires connected device or emulator)
./gradlew connectedAndroidTest

# Clean build outputs
./gradlew clean
```

Room schema export is configured to write to `dixit-companion/schemas/`. When the `@Database` version is bumped, a new schema JSON will be generated there.

## Architecture

### Two parallel data models

The codebase maintains a strict separation between runtime objects and persistence objects:

**Runtime beans** (`game/` package) — passed between Activities via `Intent.putExtra()` as `Serializable`:
- `GameBean` — full game state including players list, current turn, win conditions, finished flag
- `PlayerBean` — player state including current score, per-turn scoresheet, lifetime stats
- `Turn` — single turn state: storyteller, votes list, noOneFound/everybodyFound flags
- `VoteBean` / `VotesBean` — a single player's card vote within a turn
- `TurnScore` — score value at a specific turn number, used to build the end-game graph

**Database entities** (`db/` package) — Room entities persisted at game end only:
- `GameEntity` — stores metadata + scoresheet as a Gson-serialized JSON string
- `PlayerEntity` — stores lifetime stats (name as primary key, nbGames, nbWins)
- `GamePlayerCrossRefEntity` — junction table (not yet populated in practice)
- `GameConverter` / `PlayerConverter` — static utility classes to convert between beans and entities

### Activity flow

The game runs as a linear activity chain. State accumulates in `GameBean` and `Turn` objects passed via Intent extras:

```
MainActivity
└─► SelectPlayersActivity        (build GameBean with player list)
    └─► SelectObjectivesActivity  (set pointsToWin / maxTurns on GameBean)
        └─► [Turn loop]
            SelectStoryTellerActivity  (increment currentTurn, set Turn.storyTeller)
            └─► EveryoneFoundActivity  (did everyone find the card?)
                ├─► [YES] EndTurnActivity           (skip voting)
                └─► [NO]  WhoDidFindActivity        (checkboxes for who found)
                          └─► SelectVotesActivity   (collect one vote per non-storyteller)
                              └─► EndTurnActivity
                                  ├─► [game over] ScoresResultActivity  (graph + home)
                                  └─► [continue]  SelectStoryTellerActivity
```

### Scoring logic

Implemented in `EndTurnActivity.getPointsForTheTurn()`:
- **No one found OR everyone found**: storyteller gets 0; all others get +2
- **Some found**: storyteller gets +3; each non-storyteller who found gets +3
- **Any non-storyteller**: +1 per other player who voted for their card

### Persistence

`GameRepository` writes to two stores when a game ends (triggered from `EndTurnActivity`):
1. **Room** (local SQLite) — via `DxitDatabase.databaseWriteExecutor` (4-thread pool) to avoid blocking the UI thread
2. **Firebase Realtime Database** — keyed by `FirebaseInstallations` device ID + timestamp string; failures are silently swallowed with a log warning

`PlayerViewModel` uses `LiveData<List<PlayerEntity>>` to observe the player list reactively. `GameViewModel` only exposes `insert()`.

### Win conditions

`SelectObjectivesActivity` encodes the win mode by setting one value to `Integer.MAX_VALUE`:
- Points-based: `maxTurns = Integer.MAX_VALUE`, `pointsToWin = N`
- Turn-based: `pointsToWin = Integer.MAX_VALUE`, `maxTurns = N`

Tie-breaking in `EndTurnActivity`: if two players share the winning score, `endGameReached` is reset to `false` and the game continues.

## Key Conventions

### Lombok
All domain beans and entities use Lombok annotations. Static-only utility classes use `@NoArgsConstructor(access = AccessLevel.PRIVATE)`. Always annotate new beans with `@Builder`, `@Getter`, `@Setter` as appropriate, and add `@AllArgsConstructor`/`@NoArgsConstructor` when needed by Room.

### Internationalization
Default language is **French** (`res/values/strings.xml`). English overrides live in `res/values-en/strings.xml`. All user-visible strings must be in `strings.xml`; hardcoded French strings in `SelectVotesActivity` ("Pour qui a voté … ?") are a known exception.

### Room schema migrations
The `schemas/` directory tracks exported schema versions. The `@Database` annotation in `DxitDatabase` is currently pinned to version 1 (the `2.json` schema file exists but the annotation was not updated). When adding migrations, increment `@Database(version = ...)`, provide a `Migration` object, and pass it to the `RoomDatabase.Builder`.

### Package structure
- `fr.erban.dixitcompanion` — `MainActivity` and top-level entry point
- `fr.erban.dixitcompanion.game` — runtime game state beans and game-level activities
- `fr.erban.dixitcompanion.game.turn` — turn-level activities, adapters, beans
- `fr.erban.dixitcompanion.db` — Room database, DAOs, entities, converters, ViewModels
- `fr.erban.dixitcompanion.stats` — statistics screen
- `fr.erban.dixitcompanion.rules` — rules display screen
- `fr.erban.dixitcompanion.common` — `DxitConstants` (only the DB name)
