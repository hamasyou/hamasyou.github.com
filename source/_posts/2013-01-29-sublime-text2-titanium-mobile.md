---
layout: post
title: "Sublime Text2 で Titanium Mobile のコード補完を効かせる方法"
date: 2013-01-29 13:23
comments: true
categories: [Blog]
keywords: "Sublime Text2, Titanium Mobile, コード補完"
tags: [SublimeText2,Titanium]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

Qiita にも書きました。 <a href="http://qiita.com/items/ef8c332710960e2730d6" rel="external nofollow">Qiita: http://qiita.com/items/ef8c332710960e2730d6</a>

<h3>1. Package Manager で SublimeCodeIntel をインストールする</h3>

Sublime Text2 の PackageManager を使って SublimeCodeIntel をインストールします。

<i class="fa fa-search"></i> <a href="http://ready-study-go.blogspot.jp/2011/09/sublime-text-2.html" rel="external nofollow">http://ready-study-go.blogspot.jp/2011/09/sublime-text-2.html</a>

<h3>2. jsca2.js を使って Titanium Mobile の API doc を JavaScript 化する。</h3>

<i class="fa fa-search"></i> <a href="https://github.com/navinpeiris/jsca2js" rel="external nofollow">jsca2js: https://github.com/navinpeiris/jsca2js</a>

git clone で落としてきて

{% terminal %}
$ ./titanium-mobile.py 3.0.0
{% endterminal %}

のように、生成した API のバージョンを指定すればOK。

<h4>Titanium 3.0.0 のコードをインストールする場合の注意</h4>

titanium-mobile.py の中でバージョン毎に参照先の URL を変えるようにしていますが、3.0.0 の API Doc 参照先が間違っています。

なので、URL を修正してあげる必要があります。titanium-mobile.py を開き 23行目辺りにある URL を次のように書き換えます。

```python titanium-mobile.py
if version.startswith('2.'):
  url = 'http://docs.appcelerator.com/titanium/data/' + version + '/api.json'
else:
  #url = 'http://developer.appcelerator.com/apidoc/mobile/' + version + '/api.json'
  url = 'http://docs.appcelerator.com/titanium/data/' + version + '/api.json'
```

<h3>3. 生成された titanium-mobile-3.0.0.js を ~/.codeintel/extra にコピーする</h3>

SublimeCodeIntel をインストールすると ~/.codeintel というフォルダが作られているはずです。

追加の補完用コードをまとめるために、extra フォルダを作ってそこに生成された titanium-mobile-3.0.0.js をコピーします。

<h3>4. SublimeCodeIntel に extra フォルダのパスを通す</h3>

~/.codeintel/config を開いて次のコードを追記します。

<pre class="code"><code>{
  "JavaScript": {
    "javascriptExtraPaths": ["~/.codeintel/extra"]
  }
}</code></pre>

これで、 SublimeCodeIntel が JavaScript ファイルで Titanium Mobile のコード補完を行うようになります。

<h3>5. (オプション) CoffeeScript でもコード補完を効かせる</h3>

SublimeCodeIntel が CoffeeScript のコード補完に対応していないので CoffeeScript のシンタックスを JavaScript に設定してやります。

Sublime Text2 の > Preferences > Package Settings > SublimeCodeIntel > Settings - User を開いて次の設定を追加します。

<pre class="code"><code>{
  "codeintel_syntax_map":
  {
    "CoffeeScript": "JavaScript"
  }
}</code></pre>
