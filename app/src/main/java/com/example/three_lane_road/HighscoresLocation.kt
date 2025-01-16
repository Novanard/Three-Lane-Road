package com.example.three_lane_road
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import fragments.HighScoresFragment

class HighscoresLocation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscores_location)

        // Load the HighScoresFragment into the highscore_fragment container
        supportFragmentManager.beginTransaction()
            .replace(R.id.highscore_fragment, HighScoresFragment())
            .commit()

        findViewById<Button>(R.id.restart_button).setOnClickListener {
            // Restart game logic here
        }
    }
}
