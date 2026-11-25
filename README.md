# Lightning Spear (Fabric, Minecraft 26.3)

Hold right-click to charge, release to throw. On impact the spear calls down a
lightning bolt and flies back into your inventory (or drops, lightning-proof,
if you're gone). Craft: lightning rod (top right) + 2 blaze rods (diagonal).

Needs JDK 25, Fabric Loader 0.19.5+, Fabric API for 26.3.

Build: push to GitHub, open the Actions tab, run "Build Lightning Spear",
download the `lightning-spear-26.3` artifact. No Gradle wrapper is included on
purpose - the workflow installs Gradle itself. Locally: `gradle build`.

Hand/GUI poses live in `assets/lightning_spear/models/item/lightning_spear.json`
under "display" - tweak them in Blockbench's Display tab if they look off.
