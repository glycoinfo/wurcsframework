# WURCSFramework テスト環境について

テストといってもWURCS Frameworkに変更を行った際にWURCSを処理した際に問題が起こるかどうかを検証するテスト. プログラムの修正などにともなうWURCSの変化を観測する.

## テストの枠組みと使用するプログラム

テストは以下の様に進められる

1 WURCSFrameworkを用いてWURCSを入力, グラフ化, パース, 正規化, 標準化の後にWURCSを出力する.

WURCSFrameworkで処理した際に変化が起こるかを観測. 簡単なテストの場合は, test2フォルダにあるtest_in.txtを入力に用いる.　WURCS全体をテストしたい場合には, GlyTouCanのエンドポイントからSPARQLによってWURCSを取ってきてから加工して入力として使用する.

## WURCS 全体をとってくる

GlyTouCanエンドポイント
```
https://ts.glytoucan.org/sparql
```

において下記のSPRQLを実行してIDとWURCSを得る.　

```
PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>
PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>

SELECT DISTINCT ?Saccharide ?PrimaryId ?Sequence
FROM <http://rdf.glytoucan.org/core>
FROM <http://rdf.glytoucan.org/sequence/wurcs>
WHERE {
    ?Saccharide glytoucan:has_primary_id ?PrimaryId .
    ?Saccharide glycan:has_glycosequence ?GlycoSequence .
    ?GlycoSequence glycan:has_sequence ?Sequence .
    ?GlycoSequence glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs.
}
ORDER BY ?PrimaryId
```
TSVで結果をダウンロードすると便利かもしれない. 担当社者はTSVファイルがあまり好みではないためCSVをダウンロードしてタブ区切りに加工した. WURCSがダブルクオーテーションでくくられているかもしれないがこれは削除する.　そうやってIDとWURCS文字列がタブで区切られたファイルを作成する.

これを`all_wurcs.txt`としておく.

## jarファイルを作成する.

ここでは `~/jar`というディレクトリにjarを作成する. eclipseにてcloneしたエントリーから, src>org.glycoifolWUrCSFramework.execをたどる. `NormalizeWURCSTestFromStream.java`, `WURCSGraphCompareTestFromStream.java`, `WURCSGraphCompareTest_WURCSver_FromStream`の3つのソースに対してexport-->runnable jar fileを選択してjarを作成する.今回は以下の様に作成した.
```
1. NormalizeWURCSTestFromStream_test2.jar
```
IDとWURCSをタブ区切りで収録したテキストファイルを入力としてWURCSを処理する.
入力と出力を比較して変化がなければパスとする.

```
2. WURCSGraphCompareTestFromStream_test2.jar
```
1のテストにおいて変化が観測されたWURCSについてグラフが同じかどうかを検証する.
入力は, IDと上記入力, 出力WURCSをタブ区切りで収録したファイルとする.

```
3. WURCSGraphCompareTest_WURCSver_FromStream_test2.jar
```
上記のテストをグラフからさらにWURCSに戻して比較する. 1のテストの変形バージョンと考える. グラフ比較については最新の注意をもって作成されているが, 万一比較に漏れがあった際の保険として実行する.

## smallテスト

作成したjarを用いて上記1-3のテストを順に行う.
```
$ java -jar NormalizeWURCSTestFromStream_test2.jar <test_in.txt >test_out.txt 2>test_err.txt
```
1のテストを行った際に変化があるWURCSがあった場合にtest_out.txtファイルから2および3のgraphを比較するテストのためのインプットを作成する. 
test2フォルダにあるout2list.awkを用いる.
out2list.awkは
```
BEGIN{FS=":"}
/^G.......:/{id=$1;
	getline;
	before=$1;
	getline;
	after=$1;
	printf"%s%s%s\n",id,before,after;
	}
```

コマンドは
```
$ awk -f out2list.awk test_out.txt >graph_test_in.txt
```

これを用いて2, 3のテストを行う.
```
$ java -jar ~/jar/WURCSGraphCompareTestFromStream_test2.jar <graph_test_in.txt >graph_test_out.txt 2>graph_test_err.txt
```

```
$ java -jar ~/jar/WURCSGraphCompareTest_WURCSver_FromStream_test2.jar <graph_test_in.txt >graph_test_w_out.txt 2>graph_test_w_err.txt
```

`graph_test_out.txt`,`graph_test_w_out.txt`においては 同じグラフ/WURCSであればIDとコロンのみが出力される. 異なった場合は比較した2つのwURCSが出力される.

現在のsmallテストの場合は, 1つだけWURCS比較のテストで異なるものが出力されている.　最近尻尾を出しているMAPStarが決まらない問題としてイシュー化されている問題と関連しているのではないかとにらんでいる. 


グラフ比較とWURCS比較とで今回のように食い違いがある場合もあるので, 当面この2つのテストは両方行うと良い. これによって新しい問題を発見できる可能性もある. 実際に今回のテストでいくつかのイシューを設定できた.

## WURCS全体のテスト

最初にSPARALで得た全WURCSを収めたファイルall_Wurcs.txtに対して例えば下記のように行えば全WURCSに対するテストを行う事ができる.
```
$ java -jar NormalizeWURCSTestFromStream_test2.jar <all_wurcs.txt >all_wurcs_out.txt 2>all_wurcs_err.txt

$ awk -f out2list.awk all_wurcs_out.txt >graph_all_wurcs_in.txt

$ java -jar ~/jar/WURCSGraphCompareTestFromStream_test2.jar <graph_all_wurcs_in.txt >graph_all_wurcs_out.txt 2>graph_all_wurcs_err.txt

$ java -jar ~/jar/WURCSGraphCompareTest_WURCSver_FromStream_test2.jar <graph_all_wurcs_in.txt >graph_all_wurcs_w_out.txt 2>graph_all_wurcs_w_err.txt
```











