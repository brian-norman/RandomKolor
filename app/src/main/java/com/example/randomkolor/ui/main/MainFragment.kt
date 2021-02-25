package com.example.randomkolor.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.randomkolor.databinding.MainFragmentBinding
import com.example.randomkolor.src.Color.BLUE
import com.example.randomkolor.src.Color.ORANGE
import com.example.randomkolor.src.ColorHue
import com.example.randomkolor.src.Luminosity
import com.example.randomkolor.src.RandomKolor

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _binding?.oneRandomColorBtn?.setOnClickListener {
            var rgbString = RandomKolor().randomColor()
            rgbString = rgbString.removePrefix("(")
            rgbString = rgbString.removeSuffix(")")
            rgbString = rgbString.replace("\\s".toRegex(), "")

            val rgbStringSplit = rgbString.split(',')

            val color = Color.rgb(rgbStringSplit[0].toInt(), rgbStringSplit[1].toInt(), rgbStringSplit[2].toInt())
            _binding?.oneRandomColorCircle?.setColorFilter(color)
        }

        _binding?.oneRandomBlueBtn?.setOnClickListener {
            var rgbString = RandomKolor().randomColor(hue = ColorHue(BLUE), luminosity = Luminosity.LIGHT)
            rgbString = rgbString.removePrefix("(")
            rgbString = rgbString.removeSuffix(")")
            rgbString = rgbString.replace("\\s".toRegex(), "")

            val rgbStringSplit = rgbString.split(',')

            val color = Color.rgb(rgbStringSplit[0].toInt(), rgbStringSplit[1].toInt(), rgbStringSplit[2].toInt())
            _binding?.oneRandomBlueCircle?.setColorFilter(color)
        }

        _binding?.threeRandomOrangeButton?.setOnClickListener {
            val rgbStrings = RandomKolor().randomColors(count = 3, hue = ColorHue(ORANGE), luminosity = Luminosity.LIGHT)
            for (i in 0..2) {
                var rgbString = rgbStrings[i]
                rgbString = rgbString.removePrefix("(")
                rgbString = rgbString.removeSuffix(")")
                rgbString = rgbString.replace("\\s".toRegex(), "")

                val rgbStringSplit = rgbString.split(',')

                val color = Color.rgb(rgbStringSplit[0].toInt(), rgbStringSplit[1].toInt(), rgbStringSplit[2].toInt())
                if (i == 0) {
                    _binding?.firstOrange?.setColorFilter(color)
                }
                if (i == 1) {
                    _binding?.secondOrange?.setColorFilter(color)
                }
                if (i == 2) {
                    _binding?.thirdOrange?.setColorFilter(color)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
