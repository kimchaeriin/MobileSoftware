package com.practice.android.pocketmate.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.Tip.TipActivity
import com.practice.android.pocketmate.databinding.ActivityTipBinding

class TipAdapter(val context: Context,
                 val tipList: MutableList<BoardModel>,
                 val keyList: MutableList<String>
    )
    : RecyclerView.Adapter<TipAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindTip(board: BoardModel, key: String) {
            itemView.setOnClickListener {
                val intent = Intent(context, TipActivity::class.java)
                intent.putExtra("key", key)
                itemView.context.startActivity(intent)
            }

            val title = itemView.findViewById<TextView>(R.id.title)
            val content = itemView.findViewById<TextView>(R.id.content)
            val writer = itemView.findViewById<TextView>(R.id.writer)

            title.text = board.title
            content.text = board.content
            writer.text = board.writer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_tip, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return tipList.size
    }

    override fun onBindViewHolder(holder: TipAdapter.ViewHolder, position: Int) {
        holder.bindTip(tipList[position], keyList[position])
    }
}