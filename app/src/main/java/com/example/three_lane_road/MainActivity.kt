package com.example.three_lane_road

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import logic.GameController
import utilities.TiltDetector

class MainActivity : AppCompatActivity() {

    private lateinit var rows: List<ViewGroup>
    private lateinit var matrix: List<List<ImageView>>
    private lateinit var controller: GameController
    private lateinit var mainLeftArrow: FloatingActionButton
    private lateinit var mainRightArrow: FloatingActionButton
    private lateinit var lives: Array<AppCompatImageView>
    private var tiltDetector: TiltDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Retrieve the control mode from the Intent
        val controlMode = intent.getStringExtra("CONTROL_MODE") ?: "buttons"

        findViews()
        controller = GameController(matrix, lives, this)

        if (controlMode == "buttons") {
            // Initialize game with buttons
            initArrows()
        } else if (controlMode == "sensors") {
            // Initialize game with sensors
            tiltDetector = TiltDetector(this, controller)
            tiltDetector?.start()

            //Hiding the arrows for sensor mode
            findViewById<FloatingActionButton>(R.id.leftArrowBtn).visibility= View.GONE
            findViewById<FloatingActionButton>(R.id.rightArrowBtn).visibility= View.GONE
        }
    }

    private fun findViews() {
        mainRightArrow = findViewById(R.id.rightArrowBtn)
        mainLeftArrow = findViewById(R.id.leftArrowBtn)
        lives = arrayOf(
            findViewById(R.id.main_heart1),
            findViewById(R.id.main_heart2),
            findViewById(R.id.main_heart3)
        )
        rows = listOf(
            findViewById(R.id.firstRow),
            findViewById(R.id.secondRow),
            findViewById(R.id.thirdRow),
            findViewById(R.id.fourthRow),
            findViewById(R.id.fifthRow),
            findViewById(R.id.sixthRow),
            findViewById(R.id.seventhRow),
            findViewById(R.id.carRow)
        )
        matrix = listOf(
            listOf(
                findViewById(R.id.obs00),
                findViewById(R.id.obs01),
                findViewById(R.id.obs02),
                findViewById(R.id.obs03),
                findViewById(R.id.obs04)
            ),
            listOf(
                findViewById(R.id.obs10),
                findViewById(R.id.obs11),
                findViewById(R.id.obs12),
                findViewById(R.id.obs13),
                findViewById(R.id.obs14)
            ),
            listOf(
                findViewById(R.id.obs20),
                findViewById(R.id.obs21),
                findViewById(R.id.obs22),
                findViewById(R.id.obs23),
                findViewById(R.id.obs24)
            ),
            listOf(
                findViewById(R.id.obs30),
                findViewById(R.id.obs31),
                findViewById(R.id.obs32),
                findViewById(R.id.obs33),
                findViewById(R.id.obs34)
            ),
            listOf(
                findViewById(R.id.obs40),
                findViewById(R.id.obs41),
                findViewById(R.id.obs42),
                findViewById(R.id.obs43),
                findViewById(R.id.obs44)
            ),
            listOf(
                findViewById(R.id.obs50),
                findViewById(R.id.obs51),
                findViewById(R.id.obs52),
                findViewById(R.id.obs53),
                findViewById(R.id.obs54)
            ),
            listOf(
                findViewById(R.id.obs60),
                findViewById(R.id.obs61),
                findViewById(R.id.obs62),
                findViewById(R.id.obs63),
                findViewById(R.id.obs64)
            ),
            listOf(
                findViewById(R.id.car0),
                findViewById(R.id.car1),
                findViewById(R.id.car2),
                findViewById(R.id.car3),
                findViewById(R.id.car4)
            )
        )
    }

    private fun initArrows() {
        mainLeftArrow.setOnClickListener {
            Log.d("ArrowClick", "Left Arrow Clicked")
            controller.changeLane(-1)
        }
        mainRightArrow.setOnClickListener {
            Log.d("ArrowClick", "Right Arrow Clicked")
            controller.changeLane(1)
        }
    }
}
