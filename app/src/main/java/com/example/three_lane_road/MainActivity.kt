package com.example.three_lane_road

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
   private var lives=3
  private  var currLane=1
  private  lateinit var rows: List<ViewGroup>
   private lateinit var matrix: List<List<ImageView>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            initGame()

        }


   private fun initGame(){
        lives=3
        currLane=1
        initGrid()



    }
   private fun initGrid(){
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
                findViewById<ImageView>(R.id.carRight),
                findViewById<ImageView>(R.id.carMid),
                findViewById<ImageView>(R.id.carLeft))
        )

       for(i in 0 until matrix.size-1){
           for(cell in matrix[i])
               cell.visibility= View.INVISIBLE
       }
       matrix[matrix.size-1][0].visibility=View.INVISIBLE
       matrix[matrix.size-1][currLane].visibility=View.VISIBLE
       matrix[matrix.size-1][2].visibility=View.INVISIBLE
    }
    }
