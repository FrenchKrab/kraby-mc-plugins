# MCarcinizer
Provides a different game experience from vanilla Minecraft. Each subplugin can be disabled separately.

## DeathKeeper
On player death, a monster that holds the player's objects spawns.

Default config file: [mcarcinizer/src/main/resources/deathkeeper.yml](mcarcinizer/src/main/resources/deathkeeper.yml)

## HealthHardcorizer

- **Hardcore respawn**: The player respawns with low life & food.
- **Hardcore regen**: Actually makes the game easier. The food bar is instantly consumed to replenish the health bar as soon as the latter depletes.

Default config file:
[mcarcinizer/src/main/resources/healthhardcorizer.yml](mcarcinizer/src/main/resources/healthhardcorizer.yml)

## Enchanté!

Allows tweaks on the vanilla Minecraft enchantment system.

- **BetterAnvil** : Multiple feature to give more control to players :
  - Lower the repair cost of items
  - Add attributes, also called *attchantment* (for attribute enchantment lol ...)

Default config file:
[mcarcinizer/src/main/resources/healthhardcorizer.yml](mcarcinizer/src/main/resources/enchante.yml)


# How to build the project

Clone this repository and run
```
mvn package
```
inside the mcarcinizer/ folder.