package com.practice.android.pocketmate.Pocket

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.threetenabp.AndroidThreeTen
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.Tip.TipBoardActivity
import com.practice.android.pocketmate.databinding.ActivityWritePocketBoardBinding
import com.practice.android.pocketmate.databinding.ActivityWriteTipBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef
import com.practice.android.pocketmate.util.ScreenUtils
import org.threeten.bp.LocalDate

class WritePocketBoardActivity : AppCompatActivity() {
    lateinit var binding : ActivityWritePocketBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritePocketBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        AndroidThreeTen.init(this)
        handleBtns()
    }

    private fun handleBtns() {
        binding.postBtn.setOnClickListener {
            postAndSwitchScreen()
        }

        binding.textColorChange.setOnClickListener {
            changeTextColor()
        }
    }

    private fun changeTextColor() {
        showColorPickerDialog { selectedColor ->
            binding.content.setTextColor(selectedColor)
            binding.textColorChange.setTextColor(selectedColor)
        }
    }

    private fun showColorPickerDialog(onColorSelected: (Int) -> Unit) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_color_picker)
        val colorPalette = dialog.findViewById<GridLayout>(R.id.color_palette)

        val colorArray = listOf(
            Color.RED, Color.GREEN, Color.BLUE,
            Color.YELLOW, Color.CYAN, Color.MAGENTA,
            Color.BLACK, Color.GRAY
        )

        colorArray.forEach { color ->
            val colorButton = Button(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 100
                    height = 100
                    setMargins(10, 10, 10, 10)
                }
                setBackgroundColor(color)
            }

            colorButton.setOnClickListener {
                onColorSelected(color)
                dialog.dismiss()
            }

            colorPalette.addView(colorButton)
        }
            dialog.show()
    }

    private fun postAndSwitchScreen() {
        val user = FBAuth.getUid()
        val date = LocalDate.now().toString()
        val title = binding.title.text.toString().trim()
        val content = binding.content.text.toString().trim()
        val color = binding.content.currentTextColor ?: R.color.black
        val image = 0 //null일 때와 아닐 때 분리 필요
        val agree = 0
        val disagree = 0
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용은 한 글자 이상 작성해야 합니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            val key = FBRef.pocketRef.push().key.toString()
            val pocket = BoardModel(user, date, title, content, color, image, agree, disagree)
            FBRef.pocketRef.child(key).setValue(pocket)
            ScreenUtils.switchScreen(this, PocketBoardActivity::class.java)
        }
    }
}
