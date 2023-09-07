package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.TodoItemBinding
import com.example.todoapp.responses.TodoItem

class TodoAdapter(private var listView : List<TodoItem>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(val binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(list: TodoItem) {
            val title: TextView = binding.textViewTitle
            val description: TextView = binding.textViewDescription
            val status: TextView = binding.textViewStatus
            val date: TextView = binding.textViewDate
            val time: TextView = binding.textViewTime

            title.text = list.title
            description.text = list.description
            status.text = list.status.toString()
            date.text = list.todo_date
            time.text = list.todo_time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            TodoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listView.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = listView[position]
        holder.bindItem(currentItem)

        holder.itemView.setOnClickListener {
            //onItemClick(currentItem)
        }
    }


}