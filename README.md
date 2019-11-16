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

上記のSetup手順はMODとリソースパックを利用してWynncraftを遊ぶ場合のものです。
リソースパックを作成するときは、WynnText.zipそのまま.minecraft/resourcepacksフォルダに配置するのではなくて、
__解凍してから__配置してください。
翻訳の手順は以下のようになります。

1. Wynncraftをプレイしていてwynntr.jsonファイルに未登録のテキストを表示させてください。
2. wynntr.jsonファイルとYOUR_LANGUAGE_CODE.jsonファイルに新規の置換データが追記されます。
3. YOUR_LANGUAGE_CODE.jsonファイルの文章を翻訳されたものに書き換えます。
4. F3+Tでリソースパックをリロードしてください。
5. 対象のテキストを再表示して正しく置換できたか確認してください(一度離れて近づく、サーバーに入り直す等)。

## License

This mod is licenced under the MIT licence, see LICENSE.