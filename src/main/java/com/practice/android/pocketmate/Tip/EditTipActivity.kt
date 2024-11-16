package com.practice.android.pocketmate.Tip

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityEditTipBinding
import com.practice.android.pocketmate.databinding.ActivityTipBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDate

class EditTipActivity : AppCompatActivity() {

    lateinit var binding : ActivityEditTipBinding
    lateinit var tipBinding: ActivityTipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tipBinding = ActivityTipBinding.inflate(layoutInflater)
        AndroidThreeTen.init(this)

        val key = intent.getStringExtra("key").toString()

        getTipData(key)
        handleBtns(key)
    }

    private fun getTipData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tip = dataSnapshot.getValue(BoardModel::class.java)!!
                binding.title.setText(tip.title)
                binding.content.setText(tip.content)
                if (tip.image != 0) {
                    binding.image.visibility = View.VISIBLE
                    binding.image.setImageResource(tip.image)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.tipRef.child(key).addValueEventListener(postListener)
    }

    private fun handleBtns(key: String) {
        binding.editBtn.setOnClickListener {
            editAndSwitchScreen(key)
        }

        binding.textColorChange.setOnClickListener {
            changeTextColor()
        }

        binding.imageBtn.setOnClickListener {
            //이미지 추가
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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

    private fun editAndSwitchScreen(key: String) {
        val uid = FBAuth.getUid()
        val title = binding.title.text.toString()
        val date = LocalDate.now().toString()
        val content = binding.content.text.toString()
        val image = 0
        val agree = getAgree()
        val disagree = getDisagree()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용은 한 글자 이상 작성해야 합니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            val tip = BoardModel(uid, date, title, content, image, agree, disagree)
            val tipValues = tip.toMap()

            val childUpdates = hashMapOf<String, Any>(
                "/TipBoard/$key" to tipValues,
            )
            val databaseRef = Firebase.database.reference
            databaseRef.updateChildren(childUpdates)

            switchScreen(this, TipBoardActivity::class.java)
        }
    }

    private fun getAgree(): Int {
        return tipBinding.agreeNumber.toString().toInt()
    }

    private fun getDisagree() : Int {
        return tipBinding.disagreeNumber.toString().toInt()
    }

    private fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
        finish()
    }
}