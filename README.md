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

1. 空のリソースパックを用意する。最小の構成は次の2ファイル。
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
2. 言語設定に WynnText というものが追加されているので選択する。
3. ゲームをプレイすると wynntr.json にゲーム内のテキストが記録されていく。
4. wynntr.json を同階層に ja_jp.json という名前で複製して内容を翻訳する。
	* 基本は単純な全文置き換えを行う。
	* 数値やプレイヤー名等を含み全文置き換えで対応できない場合は正規表現を使用する。
	* キーを複製して末尾に.regexpをつけると、元のキーとセットで正規表現・置換対象文字列として機能する。
	* 詳しく説明していないので、既存のリソパを参考にしてください。
	* クリックやホバーイベントは装飾コード・テキスト内容が完全一致する場合にオリジナルのイベントを再割り当てします。
5. 言語設定を 日本語 にして遊ぶと翻訳が反映される。

## License

This mod is licenced under the MIT licence, see LICENSE.