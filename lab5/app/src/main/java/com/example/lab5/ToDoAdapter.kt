package com.example.lab5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

class ToDoAdapter : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    var data: List<ToDo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ToDoViewHolder(itemView)
    }

    override fun getItemCount() : Int = data.size

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.textView.text = data[position].name
        holder.checkBox.isChecked = data[position].isDone
    }

    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        val textView = itemView.findViewById<TextView>(R.id.textView)
    }
}