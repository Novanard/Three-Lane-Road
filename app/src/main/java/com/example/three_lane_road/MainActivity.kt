package com.example.three_lane_road

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import logic.GameController

class MainActivity : AppCompatActivity() {

  private  lateinit var rows: List<ViewGroup>
   private lateinit var matrix: List<List<ImageView>>
   private lateinit var controller:GameController
   private lateinit var mainLeftArrow:FloatingActionButton
   private lateinit var mainRightArrow:FloatingActionButton
   private lateinit var lives:Array<AppCompatImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

           findViews()
        initArrows()
         controller = GameController(matrix,lives,this)
        controller.startGame()



        }

   private fun findViews(){
       mainRightArrow=findViewById<FloatingActionButton>(R.id.rightArrowBtn)
       mainLeftArrow=findViewById<FloatingActionButton>(R.id.leftArrowBtn)
       lives = arrayOf(findViewById(R.id.main_heart1),
           findViewById(R.id.main_heart2),
           findViewById(R.id.main_heart3))
        rows = listOf(
           findViewById(R.id.firstRow),
            findViewById(R.id.secondRow),
            findViewById(R.id.thirdRow),
            findViewById(R.id.fourthRow),
            findViewById(R.id.fifthRow),
            findViewById(R.id.carRow)

        )
        matrix=listOf(
            listOf(
                findViewById<ImageView>(R.id.stone00),
                findViewById<ImageView>(R.id.stone01),
                findViewById<ImageView>(R.id.stone02)),
            listOf(
                findViewById<ImageView>(R.id.stone10),
                findViewById<ImageView>(R.id.stone11),
                findViewById<ImageView>(R.id.stone12)),
            listOf(
            findViewById<ImageView>(R.id.stone20),
            findViewById<ImageView>(R.id.stone21),
                findViewById<ImageView>(R.id.stone22)),
            listOf(

                findViewById<ImageView>(R.id.stone30),
                findViewById<ImageView>(R.id.stone31),
                findViewById<ImageView>(R.id.stone32)),

            listOf(

                findViewById<ImageView>(R.id.stone40),
                findViewById<ImageView>(R.id.stone41),
                findViewById<ImageView>(R.id.stone42)),

            listOf(
                findViewById<ImageView>(R.id.carLeft),
                findViewById<ImageView>(R.id.carMid),
                findViewById<ImageView>(R.id.carRight))
        )

    }

    private fun initArrows(){
        mainLeftArrow.setOnClickListener {
            Log.d("ArrowClick", "Left Arrow Clicked")
            controller.changeLane(-1)
        }
        mainRightArrow.setOnClickListener {
            controller.changeLane(1)
        }
    }
    }

