package logic

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.example.three_lane_road.HighscoresLocation
import com.example.three_lane_road.R
import interfaces.TiltCallback
import kotlinx.coroutines.Runnable
import utilities.TiltDetector

class GameController(
    private val matrix: List<List<ImageView>>,
    private val lives: Array<AppCompatImageView>,
    private val context: Context
) : TiltCallback {
    private var currLane = 2
    private var score=0
    private var numLives = 3
    private val numLanes = 5
    private val handler = Handler(Looper.getMainLooper())
    private var gameStarted = true
    private var refreshSpeed=800
    private var obsCounter = 0 // For every 3 obstacles 1 coin is spawned
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

        checkSpeed()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!gameStarted) { // Game lost and needs a reset
                    saveHighScores(score)
                    prepareMatrix()
                    resetGameState()
                    handler.removeCallbacksAndMessages(null) // To stop the game from running
                    switchToHighScoresAndLocation()
                    return // Exit the Runnable
                }
                moveObstaclesDown()
                moveCoinsDown()
                score += 1 // "Increasing the score/odometer"
                refreshScore()
                if (obsCounter == 3) { // If we reached 3 obstacles, spawn a coin and reset the counter
                    obsCounter = 0
                    addRandomCoin()
                } else {
                    addRandomObstacle()
                }
                if (gameStarted) {
                    handler.postDelayed(this, refreshSpeed.toLong()) //Loop only if the game is started
                }
            }
        }, 1000)
    }
    private fun checkSpeed(){
        val speedMode=(context as Activity).intent.getStringExtra("SPEED_MODE")?:"slow"
        if(speedMode=="slow")
            refreshSpeed=800
        else
            refreshSpeed=450
    }
    private fun clearHighScores() { //Run once to clear the highscore from sharedPreferences
        val sharedPreferences = context.getSharedPreferences("HighScoresList", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("highScores")
        editor.apply()
    }

    fun refreshScore(){
        (context as Activity).findViewById<TextView>(R.id.score_textview).setText(score.toString())
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
        matrix[0][randomLane].setImageResource(R.drawable.obs_icon)
        matrix[0][randomLane].visibility = View.VISIBLE
        matrix[0][randomLane].tag="OBSTACLE"
        obsCounter+=1
    }

    private fun addRandomCoin(){
        val randomLane = (0 until numLanes).random()
        matrix[0][randomLane].setImageResource(R.drawable.coin) //Changing the image from obstacle to a coin
        matrix[0][randomLane].visibility=View.VISIBLE
        matrix[0][randomLane].tag="COIN"
    }

    private fun moveObstaclesDown() {
        for (i in matrix.size - 2 downTo 0) { // Start from the second-to-last row
            for (j in 0 until numLanes) {
                if (matrix[i][j].visibility == View.VISIBLE && matrix[i][j].tag == "OBSTACLE") {
                    matrix[i][j].visibility = View.INVISIBLE
                    matrix[i][j].tag = null

                    // Move obstacle to the next row
                    if (i + 1 == matrix.size - 2 && j == currLane) {
                        // If the obstacle is just above the car, trigger a crash
                        handleCrash()
                    } else if (matrix[i + 1][j].visibility != View.VISIBLE) {
                        matrix[i + 1][j].setImageResource(R.drawable.obs_icon)
                        matrix[i + 1][j].visibility = View.VISIBLE
                        matrix[i + 1][j].tag = "OBSTACLE"
                    }
                }
            }
        }

        // Clear obstacles reaching the second-to-last row
        for (j in 0 until numLanes) {
            if (matrix[matrix.size - 2][j].visibility == View.VISIBLE && matrix[matrix.size - 2][j].tag == "OBSTACLE") {
                matrix[matrix.size - 2][j].visibility = View.INVISIBLE
                matrix[matrix.size - 2][j].tag = null
            }
        }
    }

    private fun moveCoinsDown() {
        for (i in matrix.size - 2 downTo 0) { // Start from the second-to-last row
            for (j in 0 until numLanes) {
                if (matrix[i][j].visibility == View.VISIBLE && matrix[i][j].tag == "COIN") {
                    matrix[i][j].visibility = View.INVISIBLE
                    matrix[i][j].tag = null

                    // Move coin to the next row
                    if (i + 1 == matrix.size - 2 && j == currLane) {
                        // If the coin is just above the car, collect the coin
                        collectCoin()
                    } else if (matrix[i + 1][j].visibility != View.VISIBLE) {
                        matrix[i + 1][j].setImageResource(R.drawable.coin)
                        matrix[i + 1][j].visibility = View.VISIBLE
                        matrix[i + 1][j].tag = "COIN"
                    }
                }
            }
        }

        // Clear coins reaching the second-to-last row
        for (j in 0 until numLanes) {
            if (matrix[matrix.size - 2][j].visibility == View.VISIBLE && matrix[matrix.size - 2][j].tag == "COIN") {
                matrix[matrix.size - 2][j].visibility = View.INVISIBLE
                matrix[matrix.size - 2][j].tag = null
            }
        }
    }


    private fun handleCrash() {
        numLives -= 1 // Decrement lives
        playCrashSound()
        if (numLives >= 0) { // Updating the hearts icon layout
            lives[numLives].visibility = View.INVISIBLE
            Toast.makeText(context, "You crashed!", Toast.LENGTH_SHORT).show()
            vibrate()
        }

        if (numLives == 0) {
            endGame()
        }
    }
    private fun collectCoin(){
        score+=1
        playCoinsSound()
    }

    private fun playCrashSound(){
        val mediaPlayer = MediaPlayer.create(context, R.raw.crash_sound)
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release() // Release resources when playback completes
        }
        mediaPlayer.start()
    }
    private fun playCoinsSound(){
        val mediaPlayer = MediaPlayer.create(context, R.raw.coin_sound)
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release() // Release resources when playback completes
        }
        mediaPlayer.start()
    }
    private fun endGame() {
        gameStarted = false
        handler.removeCallbacksAndMessages(null)
        Toast.makeText(context, "Game over!Moving to next screen", Toast.LENGTH_SHORT).show()

        handler.postDelayed({
            startGame()
        }, 1000)
    }
    private fun switchToHighScoresAndLocation(){
        handler.removeCallbacksAndMessages(null) //To stop the game from running in the background
        val intent = Intent(context,HighscoresLocation::class.java)
        context.startActivity(intent)
        (context as Activity).finish()
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

    fun getScore():Int{
        return score
    }
    fun saveHighScores(newScore: Int): Boolean {

        val sharedPreferences = context.getSharedPreferences("HighScoresList", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Retrieve the existing high scores
        val highScoresString = sharedPreferences.getString("highScores", null)
        val highScores = if (!highScoresString.isNullOrEmpty()) {
            highScoresString.split(",").map { it.toInt() }.toMutableList()
        } else {
            mutableListOf()
        }

        // Check if the new score is the highest score
        val isHighestScore = highScores.isEmpty() || newScore > highScores.maxOrNull()!!

        // Add the new score if it qualifies as a high score
        if (highScores.size < 10 || newScore > highScores.last()) {
            highScores.add(newScore)
            highScores.sortDescending() // Sort in descending order
            if (highScores.size > 10) {
                highScores.removeLast() // Keep only the top 10
            }

            // Save the updated high scores back to SharedPreferences
            val updatedScoresString = highScores.joinToString(",")
            editor.putString("highScores", updatedScoresString)
            editor.apply()
        }
        return isHighestScore
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
