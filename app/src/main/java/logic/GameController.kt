package logic

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.rotationMatrix
import kotlinx.coroutines.Runnable

class GameController(
    private val matrix:List<List<ImageView>>,
    private val lives:Array<AppCompatImageView>,
    private val context: Context) {
    private var currLane=1
    private val numLives=3
    private val numLanes=3
    private val handler = Handler(Looper.getMainLooper())
    private var gameStarted=true

    init{
        prepareMatrix()

    }

    // Init matrix with all stones invisible and the car in the middle lane
    private fun prepareMatrix(){
        for( i in 0 until matrix.size-1){
            for(imageView in matrix[i]){
                imageView.visibility= View.INVISIBLE
            }
        }
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
                if (!gameStarted) return

                handler.postDelayed(this,800)
            }
        }, 800)
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
}