# 今月の標語 #

**Push前　Pullしているか　再確認**

コンフリクトが起きやすいので、ローカルでCommitした後はすぐにPushせず、
Pullしてmargeしておきましょう


# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* WURCS
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact

### 編集者向け###

ここでは編集しているソースコードについて、松原が意図しているクラス名等のルールについて記しておきます。

* 他の編集者はこの書き方にする必要はありません。あくまで松原のソースコードを読む際の参考にしてください。
* 参照範囲が狭い変数では、ルールに則っていない場合もあります。

1. 命名規則全般
    * 原則、[キャメルケース](http://ja.wikipedia.org/wiki/%E3%82%AD%E3%83%A3%E3%83%A1%E3%83%AB%E3%82%B1%E3%83%BC%E3%82%B9)を使用。
1. クラス名
    * Importer: 文字列読み込みクラス
    * Exporter: 文字列書き出しクラス
    * XXXToYYY: オブジェクトXXXからオブジェクトYYYへの変換クラス
    * Old: 古いバージョン
        * クラス名の末尾に追加
        * 適宜数値を付けて残す場合もあり
    * TBC, TBD, TBA: 作成中
        * TBC = To Be Confirmed  （確認中）
        * TBD = To Be Determined  (現在未決定だが、将来決定する）
        * TBA = To Be Announced  （後日発表）
1. メソッド名
    * 原則、動詞で始める
        * getter/setterはget/set
        * 返り値がbooleanの場合はisやhas
1. 変数名
    * 原則、"?_"で始める（?部分は種類毎に記述）
        * クラス変数は"m_"（memberの"m"）
        * メソッド内変数は"t_"（temporaryの"t"）
        * メソッド引数は"a_"（argumentの"a"）
    * "_"の直後はその変数の型を表す語を追加
        * ゆるいハンガリアン記法
        * String: "str", "s"
        * int, Integer: "i", "n"
            * "i"は番号など数値そのものに意味がある場合
            * "n"は個数などカウントに使う場合
        * char: "c"
        * boolean: "b"
        * List(Array, []): "a"
    * 型の後はキャメルケースで変数名を記述