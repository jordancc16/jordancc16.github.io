# In Between — Card Game

A playable, text-based version of the card game **In Between**, written in Java
for AP Computer Science A.

The game uses a full 52-card deck with suits and ranks, and implements the
betting and game-state logic that decides whether each drawn card lands
"in between" the two face-up cards. It also supports saving and loading a
game to/from a text file, configurable high/low aces, rebuys, and per-player
win/loss tracking.

## Files

- `InBetween.java` — core game: board, betting, round flow, win/loss rules, save/load
- `Card.java` — a single playing card (suit, face, value, with high/low ace handling)
- `InBetweenRunner.java` — `main` entry point that starts a game

## Note

This is the student-written portion of the assignment. It relies on two
starter classes provided by the course — `Deck` and `PlayerInfo` — which are
not included here, so the project is shared as a code sample rather than a
standalone build.
