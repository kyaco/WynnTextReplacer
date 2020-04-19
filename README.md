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
	* Get [WynnText.zip for WynnTextReplacer](https://github.com/kyaco/WynnText/blob/master/WynnText.zip).
	* _wynntr.json_ file is refered to replace wynncraft hard-coded texts into translation keys.
	* _YOUR_LANGUAGE_CODE.json_ file is refered by minecraft to translate.

1. Create FabricMC instance in MultiMC launcher.
2. Click the _Edit Instance_ button on the right side.
2. Add WynnTextReplacer into the Loader mod pane.
3. Add the two resource packs into the Resource packs pane.
4. Launch minecraft.
5. Make sure the two resource packs are loaded (no particular order).

## How to make resource packs

The above process is just to use this mod to play Wynncraft in translated.
If you want to make resource pack, follow below constructions.

1. Make empty resource pack. The minimum constitution is 2 files; pack.mcmeta and wynntr.json.
	* ./minecraft/resourcepacks/WynnText/pack.mcmeta
	~~~
	{
		"pack": {
			"pack_format": 4,
			"description": "Wynncraft text for WynnTextReplacer. version *"
		},
		"language": {
			"wynntr": {
				"name": "WynnText",
				"region": "wynncraft"
			}
		}
	}
	~~~
	* ./minecraft/resourcepacks/WynnText/assets/minecraft/lang/wynntr.json
	~~~
	{"_":""}
	~~~
2. Launch minecraft and activate WynnText resource pack.
3. Select "WynnText" in Language select screen.
4. Play wynncraft and in-game texts with formatting code are added into the wynntr.json file.
5. Copy wynntr.json and rename it to <YOUR_LANG_CODE>.json (ex. Japanese: ja_jp, French: fr_fr).
6. Translate the content.
	* Basically, do a simple full-text translation.
	* You may use regular expression to translate numbers or player names.
	* Duplicate a key-value pair and add ".regexp" to the end of the key, the value will work as a regular expression.
	* Hmm... it is hard to explain in English for me, please see existing resource pack.
	* Click events or hover events will works only if the formatting codes of in game text and resource pack text are completely same.
6. Select your language in minecraft setting and you will see translated text in your wynncraft.

## License

This mod is licenced under the MIT licence, see LICENSE.
