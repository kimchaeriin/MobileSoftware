package com.practice.android.pocketmate.Tip

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Adapter.BoardAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityTipBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class TipActivity : AppCompatActivity() {

    lateinit var binding : ActivityTipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key").toString()

        getTipData(key)

        binding.editOrDeleteBtn.setOnClickListener {
            editOrDeleteDialog(key)
        }
    }

    fun getTipData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tip = dataSnapshot.getValue(BoardModel::class.java)!!
                binding.title.text = tip.title
                binding.content.text = tip.content
                binding.writer.text = tip.writer
                if (tip.image == 0) {
                    binding.image.visibility = View.GONE
                }
                if (tip.writer == FBAuth.getUid()) {
                    binding.editOrDeleteBtn.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.tipRef.child(key).addValueEventListener(postListener)
    }

    private fun editOrDeleteDialog(key: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage("해당 게시글을 수정하거나 삭제하시겠습니까?")
            .setNeutralButton("취소") { dialog, which ->
                // Respond to neutral button press
            }
            .setNegativeButton("수정") { dialog, which -> //수정 필요
                val intent = Intent(this, WriteTipActivity::class.java)
                intent.putExtra("isEdit", key)
                startActivity(intent)
            }
            .setPositiveButton("삭제") { dialog, which ->
                FBRef.tipRef.child(key).removeValue()
                switchScreen(this, TipBoardActivity::class.java)
            }
            .show()
    }

    private fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
    }

}