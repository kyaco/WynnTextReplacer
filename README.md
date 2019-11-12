# Wynn Text Replacer

## About

This mod replaces [Wynncraft](https://wynncraft.com/) texts into translation keys so that minecraft client translate them when rendering texts.

Wynncraft hard-coded texts<br>
↓<br>
↓ [replacement]<br>
↓<br>
translation keys<br>
↓<br>
↓ [minecraft normal translation]<br>
↓<br>
Wynncraft texts in Your language

## Setup

You have to get the following components.
* MultiMC minecraft launcher and FabricMC mod loader.
	* Follow the [fabricmc installation instructions](https://fabricmc.net/wiki/install).
* WynnTextReplacer mod jar file.
	* You can download it at [WynnTextReplacer in CurseForge](https://www.curseforge.com/minecraft/mc-mods/wynntextreplacer).
* Resource packs.
	* You have to get 2 resource packs.
	* One contains en_us.json file refered to replace wynncraft hard-coded texts into translation keys.
	* Onother has <YOUR_LANGUAGE_CODE>.json file which minecraft refers to translate.
	* Download link coming soon!

1. Create FabricMC instance in MultiMC launcher.
2. Click the _Edit Instance_ button on the right side.
2. Add WynnTextReplacer into the Loader mod pane.
3. Add the two resource packs into the Resource packs pane.
4. Launch minecraft.
5. Make sure the two resource packs are loaded (no particular order).

## How to make resource packs

### en_us.json

WynnTextReplacer will record unregistered target text of translation and output them as a text file.
Some texts need to be edited to regexp format when they have player name or variable numbers in it.

Detail will be added later.


### [YOUR_LANGUAGE_CODE].json

Clone the en_us.json, rename, remove the regexp lines and translate components.

## License

This mod is licenced under the MIT licence, see LICENSE.