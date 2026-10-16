# RaindanceMC/healthbars

- **`healthbars-module`** — Rain's Flexible Modules — Health Bars: Health under the names of players and of hurt mobs, each player choosing for themselves whether they see it.
- **`healthbars-standalone`** — Rain's Health Bars: Rain's Health Bars, as a plugin of its own: the healthbars module and the standard wrapper, shaded together.

Part of [RaindanceMC](https://github.com/RaindanceMC); built on [RainsCore](https://github.com/RaindanceMC/RainsCore) and the shared parent in [framework](https://github.com/RaindanceMC/framework). Split out of `rains-flexible-modules` on 16.10.2026, history kept.

## Build

```
mvn install
```

Needs Java 25 and read access to `packages.tstieh.de` (server ids `packages-releases` / `packages-snapshots` in `~/.m2/settings.xml`).

## Plugins

- `healthbars-standalone/target/RainsHealthBars-0.1.2.jar` — also published as `de.raindancer.modules:healthbars-standalone:0.1.2`

## Versions

A module's version is a property in this repository's root `pom.xml`, a plugin's is `plugin.version` in its own pom. CI publishes every version that is not in the registry yet on the next push to master.
