---
layout: post
title: "Titanium AlloyでSNS認証をアプリスキーマを使ってクライアント側で行う方法"
date: 2014-05-14 13:39:48 +0900
comments: true
categories: [Tech]
keywords: "Titanium,Alloy,SNS,アプリスキーマ,OAuth"
tags: [Titanium,Alloy,SNS,OAuth,アプリスキーマ]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

**Titanium Alloy** で SNS 認証 (*OAuth*) をアプリ側で行う方法のまとめです。

iPhone や Android だと、アプリスキーマ （例: snsnet://twitter）を使ってブラウザのリンクからアプリを立ち上げることができます。
この仕組を使って、アプリ内でブラウザを立ち上げるのではなく、アプリのブラウザを使って **OAuth** 認証を行い、アプリに戻ってくるということができるようになります。

ここで紹介するやり方は、iPhone と Android の両方で使える方法ですが、一部ブラウザ側の画面を閉じるためにサーバサイドが必要になります。
また、Facebook は認証後の戻りURL (redirect_url) にアプリスキーマを利用することができませんので、Facebook 連携を行うのもサーバサイドが必要です。

まぁ、Facebook に関しては、Titanium にすでにモジュールが組み込まれていて、簡単に認証を行えるようになっているので、わざわざアプリのブラウザを使って認証を行う必要もないんですけど。。

<!-- more -->

ところどころ、処理を端折っていたりしますが、雰囲気だけ掴んでください。`OAuth` オブジェクトは *oauth_adapter* とかで検索するとでてくるライブラリを使っています。

サンプルのプロジェクトフォルダは [snsnet.zip](/assets/sample/snsnet.zip) にあります。これだけでは動きませんが雰囲気をつかむように利用してください。

[参考: [Android]TwitterのOAuth認証を行う](http://wada811.blogspot.com/2014/01/android-twitter-oauth.html)


## 1. アプリのブラウザを開く

SNS 認証を行うトリガーとなる処理で、まずはアプリのブラウザを開きます。アプリのブラウザを開くには `Ti.Platform.openURL` を使います。

```javascript /app/controllers/index.js
function onFacebookLogin(e) {
    var url = OAuth.addToURL('https://www.facebook.com/dialog/oauth', {
        client_id: Alloy.CFG.facebookConsumerKey,
        redirect_uri: 'https://example.com/fbredirect',
        scope: Alloy.CFG.facebookPermissions,
        auth_type: 'reauthenticate'
    });
    Ti.Platform.openURL(url);
}

function onTwitterLogin(e) {
    var oauth = new OAuth({
        consumerKey: Alloy.CFG.twitterConsumerKey,
        consumerSecret: Alloy.CFG.twitterConsumerSecret
    });
    oauth.get('https://api.twitter.com/oauth/request_token', {
        oauth_callback: 'https://example.com/twredirect'
    }, function(e) {
        Ti.Platform.openURL(String.format('%s?%s&force_login=false', 'https://api.twitter.com/oauth/authorize', this.responseText));
    });
}

$.index.open();
```

ブラウザを開くときに、*twitter* の場合は `oauth_callback` で OAuth の戻りの URL を指定します。`twredirect` の処理は、サンプルプロジェクトの `/server` ディレクトリにあります。

### OAuth の callback URL をサーバ側で受ける理由

callback URL にアプリスキーマをつけて、`snsnet://twitter` の URL に戻ってくるようにすれば、サーバサイドの処理は不要になりますが、これだとアプリのブラウザに認証画面が残ってしまいます。

気にしないのであればいいですが、ブラウザで認証が終わったあとに閉じるように、JavaScript で画面を閉じるようにした画面をサーバサイドから返すようにして、そのなかで iframe を使ってアプリスキーマを呼び出しています。


## 2. 認証後の callback をアプリで受け取る

OAuth 後の callback URL にアプリスキーマを設定しておけば OAuth 後にアプリが開かれます。そこでは次のような処理を行います。

```javascript /app/alloy.js
Ti.App.addEventListener('resumed', function(e) {
    var launchOptions = (OS_IOS) ? Ti.App.getArguments() : (e && e.args),
        host, queryString, parameters, oauth;

    if (launchOptions && launchOptions.url) {
        host = launchOptions.url.split('?')[0];
        queryString = launchOptions.url.split('?')[1];

        if (queryString) {
            parameters = OAuth.decodeForm(queryString);
            if (host === 'snsnet://twitter') {
                oauth = new OAuth({
                    consumerKey: Alloy.CFG.twitterConsumerKey,
                    consumerSecret: Alloy.CFG.twitterConsumerSecret
                });
                oauth.post('https://api.twitter.com/oauth/access_token', parameters, function(e) {
                    // access_token 取得後の処理
                });

            } else if (host === 'snsnet://facebook') {
                // Facebook callback
                var url = Alloy.Globals.RestClient.addToURL('https://graph.facebook.com/oauth/access_token', _.extend(OAuth.getParameterMap(parameters), {
                    client_id: Alloy.CFG.facebookConsumerKey,
                    client_secret: Alloy.CFG.facebookConsumerSecret,
                    redirect_uri: Alloy.CFG.facebookCallbackURL
                }));
                Alloy.Globals.RestClient.get(url, function(e) {
                    // access_token 取得後の処理
                });
            }
        }
    }
});

// For SNS Activity
if (OS_ANDROID) {
    Ti.Android.currentActivity.addEventListener('app:resume', function(e) {
        Ti.API.debug('***** app:resume:');
        Ti.App.fireEvent('resumed', {
            args: {
                url: e.data
            }
        });
    });
}
```

アプリスキーマで戻ってきたときには、`resumed` が呼び出されますのでここで `access_token` を受け取るようにします。

### Android の場合は、画面の途中で SNS 認証が入ると、元の画面にもどれない

Titanium のアーキテクチャの仕様上、Android で HeavyWeight Window で画面を複数立ち上げていると、OAuth 後に途中の画面に戻ってくることができません。

そこで、OAuth のコールバックを受け取って終了するだけの Activity を作ってコレを利用します。

```java /platform/android/src/com/hamasyou/SnsAuthActivity.java
package com.hamasyou;

import  android.app.Activity;
import  android.os.Bundle;
import  android.content.Intent;
import  android.util.Log;
import  android.net.Uri;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiRootActivity;
import org.appcelerator.titanium.proxy.ActivityProxy;
import org.appcelerator.kroll.KrollDict;

public class SnsAuthActivity extends Activity {

    private static final String TAG = "SnsAuthActivity";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "********* onCreate");

        Intent currentIntent = getIntent();
        Uri uri = currentIntent.getData();
        Log.d(TAG, "***** uri:" + uri);

        TiRootActivity app = (TiRootActivity) TiApplication.getAppRootOrCurrentActivity();
        ActivityProxy proxy = app.getActivityProxy();
        Log.d(TAG, "**** app:" + app);
        Log.d(TAG, "**** proxy:" + proxy);

        KrollDict event = new KrollDict();
        event.put("data", uri.toString());
        proxy.fireEvent("app:resume", event);

        finish();
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.d(TAG, "********* onNewIntent");

        finish();
    }
}
```

この Activity を `tiapp.xml` で設定してアプリスキーマの起動をここで受け取るようにします。

```xml tiapp.xml
<?xml version="1.0" encoding="UTF-8"?>
<ti:app xmlns:ti="http://ti.appcelerator.org">
    <id>com.hamasyou</id>
    <name>snsnet</name>
    <version>1.0</version>
    <publisher>hamasyou</publisher>
    <url>http://</url>
    <description>not specified</description>
    <copyright>2014 by hamasyou</copyright>
    <icon>appicon.png</icon>
    <fullscreen>false</fullscreen>
    <navbar-hidden>false</navbar-hidden>
    <analytics>true</analytics>
    <guid>749b688a-dc5e-4308-badd-d1013baaf13e</guid>
    <property name="ti.ui.defaultunit" type="string">dp</property>
    <property name="ti.android.bug2373.finishfalseroot" type="bool">true</property>
    <ios>
        <plist>
            <dict>
                <key>UISupportedInterfaceOrientations~iphone</key>
                <array>
                    <string>UIInterfaceOrientationPortrait</string>
                </array>
                <key>UISupportedInterfaceOrientations~ipad</key>
                <array>
                    <string>UIInterfaceOrientationPortrait</string>
                    <string>UIInterfaceOrientationPortraitUpsideDown</string>
                    <string>UIInterfaceOrientationLandscapeLeft</string>
                    <string>UIInterfaceOrientationLandscapeRight</string>
                </array>
                <key>UIRequiresPersistentWiFi</key>
                <false/>
                <key>UIPrerenderedIcon</key>
                <false/>
                <key>UIStatusBarHidden</key>
                <false/>
                <key>UIStatusBarStyle</key>
                <string>UIStatusBarStyleDefault</string>
            </dict>
        </plist>
    </ios>
    <android xmlns:android="http://schemas.android.com/apk/res/android">
        <manifest>
          <application android:icon="@drawable/appicon" android:label="snsnet" android:name="SnsnetApplication" android:debuggable="false">
            <activity android:name=".SnsnetActivity" android:label="@string/app_name" android:theme="@style/Theme.Titanium" android:configChanges="keyboardHidden|orientation|screenSize">
              <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
              </intent-filter>
            </activity>
            <!-- SNS 連携用の Activity を作って処理させる -->
            <!--  http://wada811.blogspot.com/2014/01/android-twitter-oauth.html -->
            <activity android:launchMode="singleTask" android:name=".SnsAuthActivity">
                <intent-filter>
                    <action android:name="android.intent.action.VIEW"/>
                    <category android:name="android.intent.category.DEFAULT"/>
                    <category android:name="android.intent.category.BROWSABLE"/>
                    <data android:scheme="snsnet"/>
                </intent-filter>
            </activity>
          </application>
      </manifest>
    </android>
    <mobileweb>
        <precache/>
        <splash>
            <enabled>true</enabled>
            <inline-css-images>true</inline-css-images>
        </splash>
        <theme>default</theme>
    </mobileweb>
    <modules/>
    <deployment-targets>
        <target device="android">true</target>
        <target device="blackberry">false</target>
        <target device="ipad">true</target>
        <target device="iphone">true</target>
        <target device="mobileweb">false</target>
        <target device="tizen">false</target>
    </deployment-targets>
    <sdk-version>3.2.3.GA</sdk-version>
    <plugins>
        <plugin version="1.0">ti.alloy</plugin>
    </plugins>
</ti:app>
```

`SnsAuthActivity` で OAuth のコールバックを受け取って、`Ti.Android.currentActivity` の `app:resume` イベントを fire して、元のアプリに戻しています。
これで、元の画面を残したまま OAuth の処理を行うことができます。


ちょっと説明が不足している気もしますが、サンプルプロジェクトでなんとなく雰囲気を感じ取っていただければ。
