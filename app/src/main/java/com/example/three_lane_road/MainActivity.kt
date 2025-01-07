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
            findViewById(R.id.sixthRow),
            findViewById(R.id.seventhRow),
            findViewById(R.id.carRow)

        )
        matrix=listOf(
            listOf(
                findViewById<ImageView>(R.id.obs00),
                findViewById<ImageView>(R.id.obs01),
                findViewById<ImageView>(R.id.obs02),
                findViewById<ImageView>(R.id.obs03),
                findViewById<ImageView>(R.id.obs04)),
            listOf(
                findViewById<ImageView>(R.id.obs10),
                findViewById<ImageView>(R.id.obs11),
                findViewById<ImageView>(R.id.obs12),
                findViewById<ImageView>(R.id.obs13),
                findViewById<ImageView>(R.id.obs14)),
            listOf(
                findViewById<ImageView>(R.id.obs20),
                findViewById<ImageView>(R.id.obs21),
                findViewById<ImageView>(R.id.obs22),
                findViewById<ImageView>(R.id.obs23),
                findViewById<ImageView>(R.id.obs24)),
            listOf(

                findViewById<ImageView>(R.id.obs30),
                findViewById<ImageView>(R.id.obs31),
                findViewById<ImageView>(R.id.obs32),
                findViewById<ImageView>(R.id.obs33),
                findViewById<ImageView>(R.id.obs34)),

            listOf(

                findViewById<ImageView>(R.id.obs40),
                findViewById<ImageView>(R.id.obs41),
                findViewById<ImageView>(R.id.obs42),
                findViewById<ImageView>(R.id.obs43),
                findViewById<ImageView>(R.id.obs44)),

            listOf(
                findViewById<ImageView>(R.id.car0),
                findViewById<ImageView>(R.id.car1),
                findViewById<ImageView>(R.id.car2),
                findViewById<ImageView>(R.id.car3),
                findViewById<ImageView>(R.id.car4))
        )

    }

    private fun initArrows(){
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

