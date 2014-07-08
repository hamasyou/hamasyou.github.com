---
layout: post
title: "Apple Swift プログラミング言語おぼえがき"
date: 2014-06-26 08:00:55 +0900
comments: true
categories: [Programming]
keywords: "Apple,Swift,iOS,プログラミング言語"
tags: [Swift,Apple,プログラミング言語,iOS]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

Apple が新しいプログラミング言語 **Swift** を2014年の WWDC で発表しました。言語仕様自体は色々なモダンプログラミング言語のいいとこ取りのような感じです。独特な仕様もいくつかあるのでおぼえがきしておきます。

なお、Swift で作成したアプリは iOS8 と OS X Yosemite がリリースされた時点で審査に入れるようです。

[Introducing Swift](https://developer.apple.com/swift/)

※ Xcode6 Beta3 を元に記載しています。

<!-- more -->

<div id="toc"></div>

## The Basics

`NSLog` の代わりに `println` が使える。Xcode のコンソールにも表示される。文字列の変数展開は `\()` を使う。

```swift
let name = "hamasyou"
println("Hello \(name)")  // Hello hamasyou
```


### 基本型

`Int`、`Double`、`Float`、`Bool`、`String`、`Array`、`Dictionary` などが用意されている。`Int16` や `UInt8` などもある。型の変換は `型名()` を使う。

Swift では `nil` を特別な値として定義している。`Bool` 値は `true` と `false`。条件式では `Bool` 値のみが使える。

```swift
UInt8.max   // 255
UInt8.min   // 0

let three = 3
let floatValue = 0.14159
let pi = Double(three) + floatValue // 3.14159
let integerPi = Int(pi) // 3

if pi {
    // この文法はコンパイルエラーになる。正しくは↓
}

if pi == 3.14159 {
}
```

タプルも使える。不要な値を受け取るときは `_` を使う。

```swift
let httpError = (404, "Not Found")
httpError.0   // 404
httpError.1   // "Not Found"

let otherError = (errorCode: 500, errorMessage: "Internal Server Error")
otherError.errorCode    // 500
otherError.errorMessage // "Internal Server Error"

let (code, msg) = httpError
code  // 404
msg   // "Not Found"

let (_, msg2) = otherError
msg2  // "Internal Server Error"
```

配列は `[Int]` のように記述する。

```swift
let ary: [Int] = [10, 20, 30]
```


### リテラルと変数

Swift ではリテラルの定義は `let` を使い、変数の定義は `var` を使う。`let` で定義したリテラルは変更できない。

```swift
let myName = "hamasyou"
let π = 3.14
var x = 0.0, y = 0.0, z = 0.0
x = 3.0
```

文字列を数値に直すのに `toInt` メソッドが使える。この時返される値は `Int` 型でなく、`Int?` 型なので注意。

```swift
let str = "123"
let num = str.toInt()   // num は Int? か Optional Int として型推論される
```

if 文の条件式には `Bool` しか記述できないが、この、`型 + ?` の形式で表される `Optional 型` も if 文の式に記述できる。この場合値が nil ではない場合に `true` と評価される。

```swift
var str = "123"
var num = str.toInt()

if num {
    println("\(str) is number of \(num!)")
}
```

このように、Swift では `nil` を返す可能性があるメソッドの戻り値の型は `Optional 型` になる。`Optional 型` から値を取り出すには `!` をつける。但し、nil が入っている変数から値を取り出そうとするとランタイムエラーになる。

```swift
str = "abc"
num = str.toInt()

println(num!)   // これはランタイムエラーになるので、必ず下の様に呼び出す

if !num {
    println("\(str) is not number")
}
```

Optional 型から値を取り出す方法に、`!` をつける以外に次のようにも書ける。（Optional Binding）

```swift
let str = "123"
let expectNum = str.toInt()
if let actual = expectNum {
    println(actual)
}
```


### Implicity Unwrapped Optionals

Swift は Objective-C の nil ポインタとは違う `nil` を採用している。nil が格納される可能性がある変数は Optional 型として定義する必要がある。

Optional 型の変数から値を取り出すには `!` を使う。必ず値が入っている確証が持てる場合は `型名 + !` の形で記述することもできる。値が入っていない時に `!` を使うとランタイムエラーがでるので注意。

```swift
var optioanValue: Int?   // Optional 型の変数は nil で初期化される

let str = "123"
let num: Int! = str.toInt()
num   // num は 123 が入る Int! で定義しない場合は型推論によって Int? 型の Wrapped Value が入っているので 123 ではない
```


### Range

`(a..<b)`、`(a...b)` の形で範囲を求めることができる。`(a..<b)` は b を含まない。Ruby とは逆の動きをする。

```swift
for i in (0 ..< 3) {
    println(i)    // 0, 1, 2
}
```

Xcode6 Beta3 から終端を含まない範囲を `..<` で表すようになった。


## String And Character

`String` は `NSString` が使われているところでも使える。String は `Character` のコレクション型である。

```swift
let str = "Hello World"
let c: Character = "!"

let s = str + c

str.isEmpty             // false
str.hasPrefix("Hello")  // true
str.hasSuffix("World")  // true
```


## Collection Types

### Array

Swift の配列は `Array` 型。`Array<SomeType>` で表す。略語として `型名[]` の形式も使える。`NSArray`、`NSMutableArray` が使われているところでも使える。

```swift
var list = ["Hello", "World", "Good"]
list.count
list += "Nice"      // ["Hello", "World", "Good", "Nice"]
list.removeLast()   // "Nice"

var stringList: String[]?             // nil
var anyList: Array<AnyObject>?        // nil
var stringList2: Array<String> = []   // empty String array
```


### Dictionary

Swift の Hash は `Dictionary` 型。`Dictionary<KeyType, ValueType>` で定義する。`NSDictionary`、`NSMutableDictionary` が使われているところでも使える。

```swift
var airports = ["TYO": "Tokyo", "DUB": "Dublin"]
// var airports: Dictionary<String, String> = ["TYO": "Tokyo", "DUB": "Dublin"] とも書ける
airports.count
airports["OSK"] = "OSAKA"
airports["HOGE"]    // nil
airports.keys       // ["TYO", "DUB", "OSK"]
airports.values

for (code, name) in airports {
}
 
var dict: Dictionary<String, String>?             // nil
var emptyDict = Dictionary<String, String>()      // empty Dictionary 
var emptyDict2: Dictionary<String, String> = [:]  // empty Dictionary
```


## Functions

関数は `func メソッド名(引数: 型) -> 戻り値の型` の形で書く。戻り値が必要ない場合は省略するか、`Void` を記述する。

Swift の関数（メソッド）は *第一級関数* なので、変数や引数に渡すことができる。
 
```swift
func hello(name: String, greet: String) -> String {
    return "Hello \(name), \(greet)"
} 
println(hello("hamasyou", "Good Morning"))

func sayHello(name: String) -> Void {
    println("Hello \(name)")
}
sayHello("hamasyou")
```

戻り値にはタプルを指定することもできる。

```swift
func count() -> (vowels: Int, consonants: Int, others: Int) {
    var n1 = 3, n2 = 5, n3 = 10
    return (n1, n2, n3)
}

let ret = count()
ret.vowels
ret.consonants
ret.others
```


### External Parameter Names

関数呼び出し時にキーワード引数として呼び出す名前を指定できる。

```swift
func someFunction(externalParameterName localParameterName: Int) {
    var total = 10 + localParameterName
}
someFunction(externalParameterName: 5)

func join(s1: String, s2: String, withJoiner joiner: String) -> String {
    return s1 + joiner + s2
}
join("Hello", "World", withJoiner: "-")
```

`externalParameterName` と `localParameterName` を同じにしたいときは、`#` を使う。

```swift
func(containsCharacter(#string: String, #characterToFind: Character) -> Bool {
    for character in string {
        if character == characterToFind {
            return true
        }
    }
    return false
}
```

引数にデフォルト値を指定することもできる。デフォルト値を指定して、externalParameterName を指定しないときは、localParameterName が externalParameterName になる。

```swift
func join(s1: String, s2: String, joiner: String = "-") -> String {
    return s1 + joiner + s2
}
join("Hello", "World", joiner: " ")    
```

`_` を externalParameterName に指定すれば、デフォルト値を設定しても呼び出し時に externalParameterName を指定する必要はなくなる。

```swift
func join(s1: String, s2: String, _ joiner: String = "-") -> String {
    return s1 + joiner + s2
}
join("Hello", "World", " ")
```

### In-Out Parameters

関数の引数は定数（`let`）として渡される。引数を変数（`var`）として渡したい場合は、引数名の前に `var` をつける。

var をつけて引数を定義しても、変更の影響を受けるのは関数の中だけ。関数の外の変数にも影響を及ぼしたい場合には `inout` をつけて引数を定義して、引数を渡すときに `&` を使う。 

```swift
func swap(inout s1: String, inout s2: String) {
    let tmp = s1
    s1 = s2
    s2 = tmp
}

var s1 = "Hello"
var s2 = "World"
swap(&s1, &s2)
s1    // "World"
s2    // "Hello"
```


## Closures

Swift の Closure は次の点が最適化されている。

- 引数と戻り値の型が推論される
- Closre Body が単一式の場合はそれが暗黙の戻り値になる
- 引数を略式で書ける
- Closure を最後の引数として取るメソッドの場合、Closure を `()` の外側に書くことができる

```swift Closure Syntax
{ (parameters) -> returnType in
    statements
}
```

```swift Closure syntaxes
let names = ["Chris", "Alex", "Ewa", "Barry", "Daniella"]
sort(names, { (s1: String, s2: String) -> Bool in
    return s1 < s2
})
    
sort(names, { (s1: String, s2: String) -> Bool in return s1 < s2 })
sort(names, { s1, s2 in return s1 < s2 })
sort(names, { s1, s2 in s2 < s2 })
sort(names, { $0 < $1 })
sort(names) { $0 < $1 }  
```

```swift map sample
let digitNames = [
    0: "Zero", 1: "One", 2: "Two",   3: "Three", 4: "Four",
    5: "Five", 6: "Six", 7: "Seven", 8: "Eight", 9: "Nine"
]
let numbers = [16, 58, 510]
let strings = numbers.map { (var num) -> String in
    var output = ""
    while num > 0 {
        output = digitNames[num % 10]! + output
        num /= 10
    }
    return output
}
strings
```

```swift Closure sample
func makeIncrementor(forIncrement amount: Int) -> (() -> Int) {
    var runningTotal = 0
    func incrementor() -> Int {
        runningTotal += amount
        return runningTotal
    }
    return incrementor
}

let incrementByTen = makeIncrementor(forIncrement: 10)
incrementByTen()    // 10
incrementByTen()    // 20
incrementByTen()    // 30
```


## Enumerations

Swift では `Enumeration` も first-class type である。Swift ではデフォルト値を設定しない限り、自動で 0, 1, 2 の様な値がふられることはない。

```swift
enum CompassPoint {
    case North
    case South
    case East
    case West
}
CompassPoint.North

enum Numbers: Int {
    case Zero = 0, One, Two, Three, Four, Five, Six, Seven, Eight, Nine
}
Numbers.Four.toRaw()                // 4
Numbers.fromRaw(7) == Numbers.Seven // true 
```

### Associated Values

Swift の enumerations は関連する値を保持することができる。

```swift
enum Barcode {
    case UPCA(Int, Int, Int)
    case QRCode(String)
}
  
let productBarcode = Barcode.UPCA(8, 85900_51226, 3)
switch productBarcode {
case let .UPCA(numberSystem, identifier, check):
    println("UPC-A with value of \(numberSystem), \(identifier), \(check).")
case let .QRCode(productCode):
    println("QR code with value of \(productCode).")
} 
```


## Classes and Structures

Swift では、クラスと構造体はほぼ同じ物と考えることができる。構造体は参照ではなく copy value で管理される。

{% blockquote Classes Are Reference Types %}
Unlike value types, reference types are not copied when they are assigned to a variable or constant, or when they are passed to a function. Rather than a copy, a reference to the same existing instance is used instead.
{% endblockquote %}


#### クラスと構造体の共通点

- **プロパティ** を定義することができる
- **メソッド** を定義することができる
- **subscripts** （配列の`[0]` のようにアクセスできる仕組み）を提供することができる
- **イニシャライザ** （コンストラクタ）を定義することができる
- デフォルトの振る舞いを拡張することができる
- **protocol** を使うことができる

#### クラスだけがもつ機能

- **継承**
- タイプキャストが可能
- **デイニシャライザ** （デストラクタ）を定義することができる
- ひとつ以上の参照カウントが許される（構造体は copy-value、クラスは reference）


```swift syntax
struct Resolution {
    var width = 0
    var height = 0
}
    
class VideoMode {
    var resolution = Resolution()
    var interlaced = false
    var frameRate = 0.0
    var name: String?
}
let someResolution = Resolution()
let someVideoMode = VideoMode()
someVideoMode.resolution.width = 1280
```

Objective-C とは違って、参照をたどって直接値を設定することも可能。

構造体は、暗黙のイニシャライザを使って、プロパティを初期化することが可能。クラスはイニシャライザを明示的に定義しなければいけない。

```swift
let rvga = Resolution(width: 640, height: 480)
```


### クラスと構造体のどちらを選ぶか

- 構造体を使う一番の理由は関係する値をまとめるため
- プロパティのデータを何かしら処理する場合には、クラスの方がいいかも


### Array と Dictionary

Swift の Array と Dictionary は構造体の様に振舞う。

Dictionary は変数に割り当てられる際にコピーされる。Key/Value の Value がさらに Dictionary の場合はそれもコピーされる。

```swift
var ages = ["Peter": 23, "Wei": 35, "Anish": 65, "Katya": 19]
var copiedAges = ages
ages["Peter"] = 24    // 24
copiedAges["Peter"]   // 23
```

Xcode6 Beta3 からは、Array の動作も Dictionary と同じ動きをする。つまり、値がコピーされる。


```swift
var nums = [10, 20, 30, 40]
var otherNums = nums

nums[0] = 50
otherNums[0]    // 50

nums += 100     // occure copy, nums is [50, 20, 30, 40, 100]
nums[0] = 10    // nums is [10, 20, 30, 40, 100]
otherNums[0]    // 50, otherNums is [50, 20, 30, 40]
```

Array で参照を切りたいときは `unshare()` メソッドを使う。変数への割り当て時に強制的にコピーしたいときは `copy()` メソッドを使う。


## Properties

### Lazy Stored Properties

最初にアクセスされるまで初期化されないようにするには、`@lazy` を使用する。変数（var）で定義するプロパティには @lazy をつけるようにするといい。

{% blockquote Lazy Stored Properties %}
You must always declare a lazy property as a variable (with the var keyword), because its initial value may not be retrieved until after instance initialization completes. Constant properties must always have a value before initialization completes, and therefore cannot be declared as lazy.
{% endblockquote %}

```swift
class DataManager {
    @lazy var importer = DataImporter()   // DataImporter is assumed to take a non-trivial amount of time to initialize.
}
```
     

### Computed Properties

`center` プロパティが Computed Property になる。`set` で受け取る引数は明示してもいいし、明示しない場合は `newValue` で受け取れる。

```swift
struct Rect {
    var origin = Point()
    var size = Size()
    var center: Point {
    get {
        let centerX = origin.x + (size.width / 2)
        let centerY = origin.y + (size.height / 2)
        return Point(x: centerX, y: centerY)
    }
    set {
        origin.x = newValue.x - (size.width / 2)
        origin.y = newValue.y - (size.height / 2)
    }
    }
}
```

### Property Observers

`willSet`, `didSet` を使うと、プロパティが設定されるまえ、設定された後にコールバックを受け取ることができる。これらのコールバックは初期化時にはよばれない。

```swift
class StepCounter {
    var totalSteps: Int = 0 {
    willSet(newTotalSteps) {
        println("About to set totalSteps to \(newTotalSteps)")
    }
    didSet {
        if totalSteps > oldValue  {
            println("Added \(totalSteps - oldValue) steps")
        }
    }
    }
}
let stepCounter = StepCounter()
stepCounter.totalSteps = 200
// About to set totalSteps to 200
// Added 200 steps
```


## Methods

メソッドにも関数と同じく `externalParameterName` と `localParameterName` を指定できる。メソッドの場合は関数と違って、externalParameterName と localParameterName の定義の振る舞いが違う。

メソッドは最初の引数の externalParameterName は、localParameterName と同じになり、呼び出し時は最初の引数の externalParameterName は省略できる。

```swift
class MyClass {
    func hello(name: String, greet: String) -> String {
        return "Hello \(name) \(greet)"
    }
}

let obj = MyClass()
obj.hello("hamasyou", greet: "Good Morning")
```

第二引数以降の呼び出しで externalParameterName を指定したくない場合は `_` を使う。

```swift
class MyClass {
    func hello(name: String, _ greet: String) -> String {
        return "Hello \(name) \(greet)"
    }
}

let obj = MyClass()
obj.hello("hamasyou", "Good Night")
```

### Modifying Value Types from Within Instance Methods

構造体と Enumerations はデフォルトでメソッドの中からプロパティの変更を行うことができない。変更できるようにするには `mutating` でメソッドを修飾する。

```swift
struct Point {
    var x = 0.0, y = 0.0
    mutating func moveByX(deltaX: Double, y deltaY: Double) {
        x += deltaX
        y += deltaY
    }
}
```


## Subscripts

Subscript はコレクションにアクセスするための略式を定義するもの。クラスや構造体、Enumeration に定義できる。定義すると `[0]` などでアクセスできるようになる。

`subscript` の引数は複数指定することもできる。

```swift
subscript(index: Int) -> Int {
    get {
        // return an appropriate subscript value here
    }
    set(newValue) {
        // perform a suitable setting action here
    }
}
```


## Inheritance

メソッドやプロパティのオーバーライドには `override` キーワードを使用する。override されたくないメソッドやプロパティには `@final` を指定する。


## Initialization

イニシャライザは `init` で定義する。イニシャライザに引数を指定する場合、externalPropertyName を指定しなければ暗黙で localPropertyName が externalPropertyName に指定される。

メソッドと違い、*イニシャライザの呼び出し時には全ての externalPropertyName の指定が必要*。

```swift
struct Color {
    let red = 0.0, green = 0.0, blue = 0.0
    init(red: Double, green: Double, blue: Double) {
        self.red   = red
        self.green = green
        self.blue  = blue
    }
}
let magenta = Color(red: 1.0, green: 0.0, blue: 1.0)
```

### Designated Initializers and Convenience Initializers

`Designated Initializer` はインスタンスの初期化の目的で定義される。`Convenience Initializer` は Designated Intitializer を呼び出す際のデフォルト値などを設定する目的で使う。

```swift
class MyClass {
    var name: String
    init(name: String) {
        self.name = name
    }
    
    convenience init() {
        self.init(name: "hamasyou")
    }
}
let me = MyClass()
```


## Type Casting

型チェックには `is` を使う。

```swift
for item in library {
    if item is Movie {
        // execute movie logic
    }
}    
```

ダウンキャストに `as` が使える。Optional Type の場合は `as?` を使う。Dictionary とかから値を取り出すときに使える。

```swift
for item in library {
    if let movie = item as? Movie {
        println("Movie: '\(movie.name)', dir. \(movie.director)")
    } else if let song = item as? Song {
        println("Song: '\(song.name)', by \(song.artist)")
    }
}
```

配列の型に `AnyObject` を使うと、利用時にキャストが必要だが何の型か指定しなくても入れれるようになる。

```swift
let someObjects: AnyObject[] = [
    Movie(name: "2001: A Space Odyssey", director: "Stanley Kubrick"),
    Movie(name: "Moon", director: "Duncan Jones"),
    Movie(name: "Alien", director: "Ridley Scott")
]
for object in someObjects {
    let movie = object as Movie
    println("Movie: '\(movie.name)', dir. \(movie.director)")
}
```

配列の型に `Any` を使うと、なんでもいれれるようになる。

```swift
var things = Any[]()
 
things.append(0)
things.append(0.0)
things.append(42)
things.append(3.14159)
things.append("hello")
things.append((3.0, 5.0))
things.append(Movie(name: "Ghostbusters", director: "Ivan Reitman"))

for thing in things {
    switch thing {
    case 0 as Int:
        println("zero as an Int")
    case 0 as Double:
        println("zero as a Double")
    case let someInt as Int:
        println("an integer value of \(someInt)")
    case let someDouble as Double where someDouble > 0:
        println("a positive double value of \(someDouble)")
    case is Double:
        println("some other double value that I don't want to print")
    case let someString as String:
        println("a string value of \"\(someString)\"")
    case let (x, y) as (Double, Double):
        println("an (x, y) point at \(x), \(y)")
    case let movie as Movie:
        println("a movie called '\(movie.name)', dir. \(movie.director)")
    default:
        println("something else")
    }
}
```


## Extensions

既存のクラス、構造体、Enumeration に機能を追加できる。

```swift
extension SomeType: SomeProtocol, AnotherProtocol {
    // implementation of protocol requirements goes here
}

extension Double {
    var km: Double { return self * 1_000.0 }
    var m: Double { return self }
    var cm: Double { return self / 100.0 }
    var mm: Double { return self / 1_000.0 }
    var ft: Double { return self / 3.28084 }
}
```
