package logic

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.coroutines.Runnable

class GameController(
    private val matrix:List<List<ImageView>>,
    private val lives:Array<AppCompatImageView>,
    private val context: Context) {
    private var currLane=1
    private var numLives=3
    private val numLanes=3
    private val handler = Handler(Looper.getMainLooper())
    private var gameStarted=true

    init{
        prepareMatrix()
        resetGameState()

    }

    // Init matrix with all obstacles invisible and the car in the middle lane
    private fun prepareMatrix(){

        for( i in 0 until matrix.size-1){
            for(obstacle in matrix[i]){
                obstacle.visibility= View.INVISIBLE
            }
        }
        //Placing car in the middle lane
        for(i in 0 until matrix[matrix.size-1].size){
            if(i!= currLane)
                matrix[matrix.size-1][i].visibility=View.INVISIBLE
            else
                matrix[matrix.size-1][i].visibility=View.VISIBLE
        }
    }

    fun startGame() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!gameStarted) { // Game lost and need a reset
                    prepareMatrix()
                    resetGameState()
                }
                moveObstaclesDown()
                addRandomObstacle()
                handler.postDelayed(this,1000)
            }
        }, 1000)
    }

     fun changeLane(num:Int){
        var newLane=currLane+num
        if(newLane>=2)
            newLane=2
        else if(newLane<=0)
            newLane=0
         Log.d("ChangeLane", "changeLane: $newLane")
        for(i in 0 until numLanes){
            if(i!=newLane)
            matrix[matrix.size-1][i].visibility=View.INVISIBLE
            else
                matrix[matrix.size-1][i].visibility=View.VISIBLE
        }
         currLane = newLane
    }
    private fun addRandomObstacle() {
        val randomLane = (0 until numLanes).random()
        matrix[0][randomLane].visibility = View.VISIBLE
    }
    private fun moveObstaclesDown() {
        // Process rows from bottom to top, excluding the car row
        for (i in matrix.size - 2 downTo 0) {
            for (j in 0 until numLanes) {
                if (matrix[i][j].visibility == View.VISIBLE) {
                    matrix[i][j].visibility = View.INVISIBLE // Clear current position

                    // Move obstacle to the next row
                    if (matrix[i + 1][j].visibility == View.VISIBLE) {
                        // If the next position is visible, it means a crash occurred
                        handleCrash()
                        Toast.makeText(context, "You crashed!", Toast.LENGTH_SHORT).show()
                    } else {
                        matrix[i + 1][j].visibility = View.VISIBLE
                    }
                }
            }
        }

        // Handle the bottom row separately (obstacles reaching the end without a crash)
        for (j in 0 until numLanes) {
            if (matrix[matrix.size - 1][j].visibility == View.VISIBLE && j != currLane) {
                // Clear obstacle only if it's not the car
                matrix[matrix.size - 1][j].visibility = View.INVISIBLE
            }
        }
    }



    private fun handleCrash() {
        numLives -= 1 // Decrement lives
        if (numLives >= 0) { // Updating the hearts icon layout
            lives[numLives].visibility = View.INVISIBLE
            vibrate()
        }

        if (numLives == 0) {
            endGame()
        }
    }


    private fun endGame() {
        gameStarted = false
        handler.removeCallbacksAndMessages(null)
        Toast.makeText(context, "Game over, starting a new game", Toast.LENGTH_SHORT).show()

        handler.postDelayed({
            startGame()
        }, 1000)
    }

    private fun resetGameState() {
        currLane = 1 // Reset to middle lane
        numLives = 3 // Reset lives
        gameStarted = true
        //Resetting the 3 heart icons
        for(heart in lives)
            heart.visibility=View.VISIBLE
    }




    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API 31 and above
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else { // Below API 31
            @Suppress("DEPRECATION") // Suppress deprecation warning for older APIs
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        }
    }


}