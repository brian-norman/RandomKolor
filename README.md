# RandomKolor
🎨 A tiny (Kotlin) library for generating attractive colors

Inspired by David Merfield's [randomColor.js](https://github.com/davidmerfield/randomColor). 
You can use the library to generate attractive random colors on Android apps.

This project comes with a sample app which demonstrates some of the functionality:

<img src="screenshot.png" width="300"/>

## Installation
The library is [hosted on JitPack](https://jitpack.io/#brian-norman/RandomKolor/1.0)
1. Add it in your root `build.gradle` at the end of repositories:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
2. Add the dependency
```
dependencies {
    implementation 'com.github.brian-norman:RandomKolor:1.0'
}
```

## Options
- ```hue``` – Controls the hue of the generated color. You can pass in a color: `Color.RED`, `Color.ORANGE`, `Color.YELLOW`, `Color.Green`, `Color.BLUE`, `Color.Purple`, `Color.PINK` and `Color.MONOCHROME` are currently supported.
- ```luminosity``` – Controls the luminosity of the generated color. Library supports: `Luminosity.RANDOM`, `Luminosity.BRIGHT`, `Luminosity.LIGHT`, `Luminosity.DARK`
- ```count``` – An integer which specifies the number of colors to generate.

## API
```kotlin
fun randomColor(hue: Hue = RandomHue, luminosity: Luminosity = Luminosity.RANDOM, format: Format = Format.RGB): String
```

```kotlin
fun randomColors(count: Int, hue: Hue = RandomHue, luminosity: Luminosity = Luminosity.RANDOM, format: Format = Format.RGB): List<String>
```

## Examples
```kotlin
// Generate an attractive RGB randomly
val rgbString = RandomKolor().randomColor()
```

```kotlin
// Generate a light blue RGB 
val rgbString = RandomKolor().randomColor(hue = ColorHue(BLUE), luminosity = Luminosity.LIGHT)
```

```kotlin
// Generate three light blue oranges
val rgbStrings = RandomKolor().randomColors(count = 3, hue = ColorHue(ORANGE), luminosity = Luminosity.LIGHT)
```

## Acknowledgements
- David Merfield's original [library](https://github.com/davidmerfield/randomColor)
- Wei Wang's swift port [RandomColorSwift](https://github.com/onevcat/RandomColorSwift)

## TODO
- Currently only RGB is supported, would like to support other formats
- Add alpha to RGB
