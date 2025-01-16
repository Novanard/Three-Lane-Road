package com.example.three_lane_road
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import fragments.HighScoresFragment
import fragments.LocationFragment

class HighscoresLocation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscores_location)

        // Load the HighScoresFragment into the highscore_fragment container
        supportFragmentManager.beginTransaction()
            .replace(R.id.highscore_fragment, HighScoresFragment())
            .commit()

        // Load the MapsFragment into the location_fragment container
        supportFragmentManager.beginTransaction()
            .replace(R.id.location_fragment,LocationFragment())
            .commit()

        findViewById<Button>(R.id.restart_button).setOnClickListener {
            // Create intent to move to MainActivity
            val intent = Intent(this, GameOptions::class.java)
            startActivity(intent)
            finish()
        }
    }
}

