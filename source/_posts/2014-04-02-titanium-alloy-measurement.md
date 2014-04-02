---
layout: post
title: "[Titanium] Alloyのmeasurementの実装がイケてなかったので再実装した"
date: 2014-04-02 18:17:44 +0900
comments: true
categories: [Programming]
keywords: "Titanium,Alloy,measurement"
tags: [Titanium,Alloy,measurement]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

[Titanium](http://www.appcelerator.com/titanium/) Alloy の `measurement` がイケてない感じだったので再実装しました。

`measurement` には `pxToDP` とか `dpToPX` とかの単位を変換するメソッドが用意されていますが、中身を見るとびっくりします。。

<!-- more -->

```javascript alloy/measurement.js (alloy 1.3.1)
var dpi = Ti.Platform.displayCaps.dpi, density = Ti.Platform.displayCaps.density;

exports.dpToPX = function(val) {
    return val * ("high" === density ? 2 : 1);
};

exports.pxToDP = function(val) {
    return val / ("high" === density ? 2 : 1);
};

exports.pointPXToDP = function(pt) {
    return {
        x: exports.pxToDP(pt.x),
        y: exports.pxToDP(pt.y)
    };
};
```

これ、イケてなさすぎでしょ。。

`Ti.UI.convertUnits` を使って次のように実装しなおしました。

```javascript lib/unit.js
// システムのUI単位から指定のUI単位に変換する
var currentUnit = Ti.App.Properties.getString('ti.ui.defaultunit') || 'system';
currentUnit = (currentUnit === 'system') ? (OS_IOS) ? Ti.UI.UNIT_DIP : Ti.UI.UNIT_PX : currentUnit;

function convert(val, fromUnit, toUnit) {
    return Ti.UI.convertUnits('' + parseInt(val) + fromUnit, toUnit);
}

module.exports = exports = {
    dpToPX: function(val) {
        return convert(val, Ti.UI.UNIT_DIP, Ti.UI.UNIT_PX);
    },
    pxToDP: function(val) {
        return convert(val, Ti.UI.UNIT_PX, Ti.UI.UNIT_DIP);
    },
    systemToPX: function(val) {
        if (currentUnit === Ti.UI.UNIT_DIP) {
            return this.dpToPX(val);
        } else if (currentUnit === Ti.UI.UNIT_PX) {
            return convert(val, Ti.UI.UNIT_PX, Ti.UI.UNIT_PX);
        } else {
            return convert(val, '', Ti.UI.UNIT_PX);
        }
    },
    systemToDP: function(val) {
        if (currentUnit === Ti.UI.UNIT_DIP) {
            return convert(val, Ti.UI.UNIT_DIP, Ti.UI.UNIT_DIP);
        } else if (currentUnit === Ti.UI.UNIT_PX) {
            return this.pxToDP(val);
        } else {
            return convert(val, '', Ti.UI.UNIT_DIP);
        }
    },
    pointPXToDP: function(pt) {
        return {x: this.pxToDP(pt.x), y: this.pxToDP(pt.y)};
    }
};
```


使い方と結果はこちら。

```javascript sample.js
var unit = require('unit');
console.log('----- iOS -----');
Ti.API.info('Ti.Platform.displayCaps.density: ' + Ti.Platform.displayCaps.density);
Ti.API.info('Ti.Platform.displayCaps.dpi: ' + Ti.Platform.displayCaps.dpi);
Ti.API.info('Ti.Platform.displayCaps.platformHeight: ' + Ti.Platform.displayCaps.platformHeight);
Ti.API.info('Ti.Platform.displayCaps.platformWidth: ' + Ti.Platform.displayCaps.platformWidth);
if (Ti.Platform.osname === 'android'){
  Ti.API.info('Ti.Platform.displayCaps.xdpi: ' + Ti.Platform.displayCaps.xdpi);
  Ti.API.info('Ti.Platform.displayCaps.ydpi: ' + Ti.Platform.displayCaps.ydpi);
  Ti.API.info('Ti.Platform.displayCaps.logicalDensityFactor: ' + Ti.Platform.displayCaps.logicalDensityFactor);
}
console.log('dpToPX: ' + unit.dpToPX(130));
console.log('pxToDP: ' + unit.pxToDP(130));
console.log('systemToPX: ' + unit.systemToPX(130));
console.log('systemToDP: ' + unit.systemToDP(130));
console.log('-----');
```

{% terminal %}
[INFO] :   ----- iPhone OS -----
[INFO] :   Ti.Platform.displayCaps.density: high
[INFO] :   Ti.Platform.displayCaps.dpi: 320
[INFO] :   Ti.Platform.displayCaps.platformHeight: 568
[INFO] :   Ti.Platform.displayCaps.platformWidth: 320
[INFO] :   dpToPX: 260
[INFO] :   pxToDP: 65
[INFO] :   systemToPX: 260
[INFO] :   systemToDP: 130
[INFO] :   -----

[INFO] :   ----- android -----
[INFO] :   Ti.Platform.displayCaps.density: xhigh
[INFO] :   Ti.Platform.displayCaps.dpi: 320
[INFO] :   Ti.Platform.displayCaps.platformHeight: 1280
[INFO] :   Ti.Platform.displayCaps.platformWidth: 720
[INFO] :   Ti.Platform.displayCaps.xdpi: 345.0566101074219
[INFO] :   Ti.Platform.displayCaps.ydpi: 342.2315673828125
[INFO] :   Ti.Platform.displayCaps.logicalDensityFactor: 2
[INFO] :   dpToPX: 260
[INFO] :   pxToDP: 65
[INFO] :   systemToPX: 130
[INFO] :   systemToDP: 65
[INFO] :   -----
{% endterminal %}
