package com.example.color_maker

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    //Seek bars and it's Values
    private lateinit var redSeekBar: SeekBar
    private lateinit var greenSeekBar: SeekBar
    private lateinit var blueSeekBar: SeekBar
    private lateinit var resetButton: Button
    private lateinit var redEditText: EditText
    private lateinit var greenEditText: EditText
    private lateinit var blueEditText: EditText

    //Switches
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var redSwitch: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var greenSwitch: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var blueSwitch: Switch

    //Color Box
    private lateinit var colorBox: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redSeekBar = findViewById(R.id.redSeekBar)
        greenSeekBar = findViewById(R.id.greenSeekBar)
        blueSeekBar = findViewById(R.id.blueSeekBar)
        resetButton = findViewById(R.id.resetButton)
        redSwitch = findViewById(R.id.redSwitch)
        greenSwitch = findViewById(R.id.greenSwitch)
        blueSwitch = findViewById(R.id.blueSwitch)
        colorBox = findViewById(R.id.colorBox)
        redEditText = findViewById(R.id.redEditText)
        greenEditText = findViewById(R.id.greenEditText)
        blueEditText = findViewById(R.id.blueEditText)


        // set the input type to decimal number
        redEditText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        greenEditText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        blueEditText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        // max values for each seekBar, i think this is the full range of values for 8 bit?
        redSeekBar.max = 255
        greenSeekBar.max = 255
        blueSeekBar.max = 255

        //Initialize the switches to true ("on")
        redSwitch.isChecked = true
        greenSwitch.isChecked = true
        blueSwitch.isChecked = true

        //Needed to store the previous value and set progress value to 0 if switch turned "off".
        var redPrevious = 0
        var greenPrevious = 0
        var bluePrevious = 0

        // set the input filter to allow only decimal values between 0 and 1
        redEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val originalText = redEditText.text.toString()
                val input = redEditText.text.toString().toDoubleOrNull()
                if (input != null && input in 0.0..1.0) {
                    // Input is valid
                    redEditText.error = null // no error message
                    redSeekBar.progress = (input * redSeekBar.max).roundToInt() // for progress bar
                    redEditText.setText(originalText)
                } else {
                    // Input is invalid
                    redEditText.error = "Please enter a value between 0 and 1"
                }
            }
        }
        //greenEditText
        greenEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val originalText = greenEditText.text.toString()
                val input = greenEditText.text.toString().toDoubleOrNull()
                if (input != null && input in 0.0..1.0) {
                    // Input is valid
                    greenEditText.error = null // no error message
                    greenSeekBar.progress = (input * greenSeekBar.max).roundToInt() // for progress bar
                    greenEditText.setText(originalText)
                } else {
                    // Input is invalid
                    greenEditText.error = "Please enter a value between 0 and 1"
                }
            }
        }
        //blueEditText
        blueEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val originalText = blueEditText.text.toString()
                val input = blueEditText.text.toString().toDoubleOrNull()
                if (input != null && input in 0.0..1.0) {
                    // Input is valid
                    blueEditText.error = null // no error message
                    blueSeekBar.progress = (input * blueSeekBar.max).roundToInt() // for progress bar
                    blueEditText.setText(originalText)
                } else {
                    // Input is invalid
                    blueEditText.error = "Please enter a value between 0 and 1"
                }
            }
        }

        resetButton.setOnClickListener {
            resetSeekBars()
            Toast.makeText(this, "reset button pressed!", Toast.LENGTH_SHORT).show()
        }

        updateColorBox()
        // handle the switch state change here
        redSwitch.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                redSeekBar.progress = redPrevious
                redSeekBar.isEnabled = true
                Toast.makeText(this, "redSwitch on", Toast.LENGTH_SHORT).show()

            } else {
                redPrevious = redSeekBar.progress
                redSeekBar.isEnabled = false
                redSeekBar.progress = 0
                Toast.makeText(this, "redSwitch off", Toast.LENGTH_SHORT).show()
            }
        }

        greenSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                greenSeekBar.progress = greenPrevious
                greenSeekBar.isEnabled = true
                Toast.makeText(this, "greenSwitch on", Toast.LENGTH_SHORT).show()

            } else {
                greenPrevious = greenSeekBar.progress
                greenSeekBar.isEnabled = false
                greenSeekBar.progress = 0
                Toast.makeText(this, "greenSwitch off", Toast.LENGTH_SHORT).show()
            }

        }

        blueSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                blueSeekBar.progress = bluePrevious
                blueSeekBar.isEnabled = true
                Toast.makeText(this, "blueSwitch on", Toast.LENGTH_SHORT).show()
            } else {
                bluePrevious = blueSeekBar.progress
                blueSeekBar.isEnabled = false
                blueSeekBar.progress = 0
                Toast.makeText(this, "blueSwitch off", Toast.LENGTH_SHORT).show()
            }
        }

        redSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val decimalValue = p1.toFloat() / redSeekBar.max.toFloat()
                updateColorBox()
                val formattedText = String.format("%.3f", decimalValue)
                redEditText.setText(formattedText)
                redEditText.setSelection(formattedText.length)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        greenSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val decimalValue = p1.toFloat() / greenSeekBar.max.toFloat()
                updateColorBox()
                val formattedText = String.format("%.3f", decimalValue)
                greenEditText.setText(formattedText)
                greenEditText.setSelection(formattedText.length)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        blueSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val decimalValue = p1.toFloat() / blueSeekBar.max.toFloat()
                updateColorBox()
                val formattedText = String.format("%.3f", decimalValue)
                blueEditText.setText(formattedText)
                blueEditText.setSelection(formattedText.length)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

    }

    private fun updateColorBox(){
        val red = redSeekBar.progress
        val green = greenSeekBar.progress
        val blue = blueSeekBar.progress
        val color = Color.rgb(red, green, blue)
        colorBox.setBackgroundColor(color)

        val shape = GradientDrawable() // for the border of the box
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = 8f
        shape.setColor(color)
        shape.setStroke(4, Color.BLACK)

        colorBox.background = shape
    }

    @SuppressLint("SetTextI18n")
    private fun resetSeekBars() {
        redSeekBar.progress = 0
        greenSeekBar.progress = 0
        blueSeekBar.progress = 0

        updateColorBox()
    }

}


