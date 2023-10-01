package com.mstudio.android.xnote.adapter

import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair


import androidx.recyclerview.widget.RecyclerView
import com.mstudio.android.xnote.R
import com.mstudio.android.xnote.activity.add_activity
import com.mstudio.android.xnote.model.model

class adapter(private val context: Context, private var data: List<model>) :
    RecyclerView.Adapter<adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }
    fun updateData(newData: List<model>) {
        data = newData
        notifyItemInserted(0)
    }

    fun updateDatadefault(newData: List<model>) {


        data = newData
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]

        holder.titleText.text = currentItem.title
        holder.subtitleText.text = currentItem.subtitle
        holder.dateText.text = currentItem.date
        holder.itemView.setOnClickListener {
            val intent = Intent(context, add_activity::class.java)
            intent.putExtra("keynote", "1")
            intent.putExtra("keynote_id", currentItem.id.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.titleTextView)
        val subtitleText: TextView = itemView.findViewById(R.id.subtitleTextView)
        val dateText: TextView = itemView.findViewById(R.id.dateTextView)
    }



}

