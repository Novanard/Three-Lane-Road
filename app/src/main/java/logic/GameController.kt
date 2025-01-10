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
import interfaces.TiltCallback
import kotlinx.coroutines.Runnable
import utilities.TiltDetector

class GameController(
    private val matrix: List<List<ImageView>>,
    private val lives: Array<AppCompatImageView>,
    private val context: Context
) : TiltCallback {
    private var currLane = 2
    private var numLives = 3
    private val numLanes = 5
    private val handler = Handler(Looper.getMainLooper())
    private var gameStarted = true

    init {
        prepareMatrix()
        resetGameState()
    }

    // Init matrix with all obstacles invisible and the car in the middle lane
    private fun prepareMatrix() {
        for (i in 0 until matrix.size - 1) {
            for (obstacle in matrix[i]) {
                obstacle.visibility = View.INVISIBLE
            }
        }
        // Placing car in the middle lane
        for (i in 0 until matrix[matrix.size - 1].size) {
            if (i != currLane)
                matrix[matrix.size - 1][i].visibility = View.INVISIBLE
            else
                matrix[matrix.size - 1][i].visibility = View.VISIBLE
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
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    fun changeLane(targetLane: Int) {
        val newLane = targetLane.coerceIn(0, numLanes - 1) // Ensure target lane is valid

        Log.d("ChangeLane", "Changing lane to: $newLane")

        // Update visibility for all lanes
        for (i in 0 until numLanes) {
            matrix[matrix.size - 1][i].visibility = if (i == newLane) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

        currLane = newLane // Update the current lane
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
            Toast.makeText(context, "You crashed!", Toast.LENGTH_SHORT).show()
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
        currLane = 2 // Reset to middle lane
        numLives = 3 // Reset lives
        gameStarted = true
        // Resetting the 3 heart icons
        for (heart in lives)
            heart.visibility = View.VISIBLE
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
    fun getCurrLane(): Int {
        return currLane
    }

    // SENSOR FUNCTIONS
    override fun tiltLeft() {
        if (currLane > 0) {
            currLane -= 1
            changeLane(currLane)
            Log.d("Move Left", "changeLane: $currLane")
        }
    }

    override fun tiltRight() {
        if (currLane < 4) {
            currLane += 1
            changeLane(currLane)
            Log.d("Move Right", "changeLane: $currLane")
        }
    }

    override fun tiltCenter() {
        currLane = 2
        changeLane(currLane)
        Log.d("Move Center", "changeLane: $currLane")
    }

}
