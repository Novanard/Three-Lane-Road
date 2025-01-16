package fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.three_lane_road.R

class HighScoresFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_high_scores, container, false)
        val highScoresTextView: TextView = view.findViewById(R.id.high_scores_text_view)

        // Retrieve high scores from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("HighScoresList", Context.MODE_PRIVATE)
        val highScoresString = sharedPreferences.getString("highScores", null)

        // Format and display high scores
        val highScores = highScoresString?.split(",")?.map { it.toInt() } ?: emptyList()
        if (highScores.isNotEmpty()) {
            highScoresTextView.text = highScores.joinToString(separator = "\n") { "Score: $it" }
        } else {
            highScoresTextView.text = getString(R.string.no_high_scores_yet)
        }

        return view
    }

}
