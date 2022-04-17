package com.example.randomkolor.src

import androidx.core.graphics.ColorUtils
import kotlin.math.floor
import kotlin.random.Random

class RandomKolor {
    /**
     * Generate a single random color with specified (or random) hue and luminosity.
     */
    fun randomColor(
            hue: Hue = RandomHue,
            luminosity: Luminosity = Luminosity.RANDOM,
            format: Format = Format.RGB
    ): String {

        // First we pick a hue (H)
        val hueValue = pickHue(hue)

        // Then use H to determine saturation (S)
        val saturation = pickSaturation(hueValue, hue, luminosity)

        // Then use S and H to determine brightness (B)
        val brightness = pickBrightness(hueValue, hue, saturation, luminosity)

        // Then we return the HSB color in the desired format
        return setFormat(hueValue, saturation, brightness, format)
    }

    /**
     * Generate a list of colors given the desired count.
     */
    fun randomColors(
        count: Int,
        hue: Hue = RandomHue,
        luminosity: Luminosity = Luminosity.RANDOM,
        format: Format = Format.RGB
    ): List<String> {
        val colors = mutableListOf<String>()
        for (i in 1..count) {
            colors.add(randomColor(hue, luminosity, format))
        }
        return colors
    }

    private fun pickHue(hue: Hue): Int {
        val hueRange = hue.getHueRange()
        var hueValue = randomWithin(hueRange)
        // Instead of storing red as two separate ranges,
        // we group them, using negative numbers
        if (hueValue < 0) {
            hueValue += 360
        }
        return hueValue
    }

    private fun pickSaturation(hueValue: Int, hue: Hue, luminosity: Luminosity): Int {
        if (hue == ColorHue(Color.MONOCHROME)) {
            return 0
        }

        val color: Color = matchColor(hueValue, hue)

        val sMin = color.saturationRange().first
        val sMax = color.saturationRange().second

        return when (luminosity) {
            Luminosity.RANDOM -> randomWithin(Pair(0, 100))
            Luminosity.BRIGHT -> randomWithin(Pair(55, sMax))
            Luminosity.LIGHT -> randomWithin(Pair(sMin, 55))
            Luminosity.DARK -> randomWithin(Pair(sMax - 10, sMax))
        }
    }

    private fun pickBrightness(hueValue: Int, hue: Hue, saturation: Int, luminosity: Luminosity): Int {
        val color: Color = matchColor(hueValue, hue)

        val bMin = color.brightnessRange(saturation).first
        val bMax = color.brightnessRange(saturation).second

        return when(luminosity) {
            Luminosity.RANDOM -> randomWithin(Pair(50, 100))    // I set this to 50 arbitrarily, they look more attractive
            Luminosity.BRIGHT -> randomWithin(Pair(bMin, bMax))
            Luminosity.LIGHT -> randomWithin(Pair((bMax + bMin)/2, bMax))
            Luminosity.DARK -> randomWithin(Pair(bMin, bMin + 20))
        }
    }

    private fun setFormat(hueValue: Int, saturation: Int, brightness: Int, format: Format): String {
        return when (format) {
            Format.HSL -> HSVtoHSL(hueValue, saturation, brightness).toString()
            Format.RGB -> HSVtoRGB(hueValue, saturation, brightness).toString()
            Format.HEX -> HSVtoHEX(hueValue, saturation, brightness)
        }
    }

    private fun HSVtoRGB(hueValue: Int, saturation: Int, brightness: Int): Triple<Int, Int, Int> {
        // This doesn't work for the values of 0 and 360
        // Here's the hacky fix
        // Rebase the h,s,v values
        val h: Float = hueValue.coerceIn(1, 359) / 360f
        val s = saturation/100f
        val v = brightness/100f

        val hI = floor(h * 6f).toInt()
        val f = h * 6f - hI
        val p = v * (1f - s)
        val q = v * (1f - f*s)
        val t = v * (1f - (1f - f)*s)

        var r = 256f
        var g = 256f
        var b = 256f

        when (hI) {
            0 -> { r = v; g = t; b = p }
            1 -> { r = q; g = v; b = p }
            2 -> { r = p; g = v; b = t }
            3 -> { r = p; g = q; b = v }
            4 -> { r = t; g = p; b = v }
            5 -> { r = v; g = p; b = q }
        }

        return Triple(floor(r*255f).toInt(), floor(g*255f).toInt(), floor(b*255f).toInt())
    }

    private fun HSVtoHSL(hueValue: Int, saturation: Int, brightness: Int): Triple<Float, Float, Float> {
        val rgb: Triple<Int, Int, Int> = HSVtoRGB(hueValue, saturation, brightness)

        val r = rgb.first
        val g = rgb.second
        val b = rgb.third

        val hsl: FloatArray = FloatArray(3)
        ColorUtils.RGBToHSL(r, g, b, hsl)

        return Triple(hsl[0], hsl[1], hsl[2])
    }

    private fun HSVtoHEX(hueValue: Int, saturation: Int, brightness: Int): String {
        val rgb: Triple<Int, Int, Int> = HSVtoRGB(hueValue, saturation, brightness)

        val r = rgb.first
        val g = rgb.second
        val b = rgb.third

        // Convert r, g and b values to 2 digit hex strings and concat them to color code
        // Format: #RRGGBB
        return String.format("#%02X%02X%02X", r, g, b)
    }


    /**
     * Turns hue into a color if it isn't already one.
     * First we check if hue was passed in as a color, and just return that if it is.
     * If not, we iterate through every color to see which one the given hueValue fits in.
     * For some reason if a matching hue is not found, just return Monochrome.
     */
    private fun matchColor(hueValue: Int, hue: Hue): Color {
        return when (hue) {
            is ColorHue -> hue.color
            else -> {
                // Maps red colors to make picking hue easier
                var hueVal = hueValue
                if (hueVal in 334..360) {
                    hueVal -= 360
                }

                for (color in Color.values()) {
                    if (hueVal in color.hueRange.first .. color.hueRange.second) {
                        return color
                    }
                }
                // Returning Monochrome if we can't find a value, but this should never happen
                return Color.MONOCHROME
            }
        }
    }

    private fun randomWithin(range: Pair<Int, Int>): Int {
        // Generate random evenly distinct number from:
        // https://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/
        val goldenRatio = 0.618033988749895
        var r = Random.nextDouble()
        r += goldenRatio
        r %= 1
        return floor(range.first + r*(range.second + 1 - range.first)).toInt()
    }
}
