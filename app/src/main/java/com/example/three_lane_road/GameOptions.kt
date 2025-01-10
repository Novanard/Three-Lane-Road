package com.example.three_lane_road

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameOptions : AppCompatActivity() {

    lateinit var controlGroup: RadioGroup
    lateinit var speedGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_options)
        findViews()

        // Listener for control group
        controlGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioSensors -> Toast.makeText(this, "Play with Sensors selected", Toast.LENGTH_SHORT).show()
                R.id.radioButtons -> Toast.makeText(this, "Play with Buttons selected", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Start Game button click
        val startGameButton = findViewById<Button>(R.id.startGameButton)
        startGameButton.setOnClickListener {
            val controlMode = when (controlGroup.checkedRadioButtonId) {
                R.id.radioSensors -> "sensors"
                R.id.radioButtons -> "buttons"
                else -> "buttons" // Default
            }

            val speedMode = when (speedGroup.checkedRadioButtonId) {
                R.id.radioFast -> "fast"
                R.id.radioSlow -> "slow"
                else -> "slow" // Default
            }

            // Create intent to move to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("CONTROL_MODE", controlMode)
            intent.putExtra("SPEED_MODE", speedMode)
            startActivity(intent)
        }
    }

    private fun findViews() {
        controlGroup = findViewById(R.id.controlGroup)
        speedGroup = findViewById(R.id.speedGroup)
    }
}
