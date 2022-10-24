package hu.ait.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import hu.ait.minesweeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        resetButton()
        flagToggle()
    }

    private fun resetButton() {
        binding.btnReset.setOnClickListener {
            binding.minesweeper.resetGame()
        }
    }

    private fun flagToggle() {
        binding.toggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.minesweeper.setFlag(true)
            } else {
                binding.minesweeper.setFlag(false)
            }
        }
    }

    fun isWon(win: Boolean) {
        if (win) {
            Snackbar.make(binding.root, resources.getString(R.string.game_win_text), Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(binding.root, resources.getString(R.string.game_lose_text), Snackbar.LENGTH_LONG).show()
        }
    }
}
