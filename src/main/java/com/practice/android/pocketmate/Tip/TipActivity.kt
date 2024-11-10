package com.practice.android.pocketmate.Tip

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityTipBinding
import com.practice.android.pocketmate.util.FBRef

class tipAdapter(val tipList : MutableList<BoardModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return 1
    }

    override fun getItem(position: Int): Any {
        return tipList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.activity_tip, parent, false)
        }

        val title = view?.findViewById<TextView>(R.id.title)
        title!!.text = tipList[position].title
        val content = view?.findViewById<TextView>(R.id.content)
        content?.text = tipList[position].content
        val user = view?.findViewById<TextView>(R.id.nickname)
        user!!.text = tipList[position].user

        return view!!
    }

}

class TipActivity : AppCompatActivity() {

    lateinit var binding : ActivityTipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key").toString()

        getTipData(key)
    }

    fun getTipData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    if (data.key == key) {
                        val tip = data.getValue(BoardModel::class.java)
                        binding.nickname.text = tip!!.user
                        binding.title.text = tip.title
                        binding.content.text = tip.content
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.tipRef.child(key).addValueEventListener(postListener)
    }
}