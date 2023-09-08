package com.example.todoapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.TodoItemBinding
import com.example.todoapp.fragments.EditTask
import com.example.todoapp.responses.TodoItem

class TodoAdapter(private var listView : List<TodoItem>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private fun getStatusText(statusCode: Int): String {
        return when (statusCode) {
            0 -> "Todo"
            1 -> "In Progress"
            2 -> "Done"
            else -> "Unknown"
        }
    }
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
            date.text = list.todo_date
            time.text = list.todo_time
            val id = list.id
            //status.text = list.status.toString()
            status.text = getStatusText(list.status)
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

        holder.binding.cardView.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("title", currentItem.title)
            bundle.putString("description", currentItem.description)
            bundle.putString("status", currentItem.status.toString())
            bundle.putString("date", currentItem.todo_date)
            bundle.putString("time", currentItem.todo_time)
            bundle.putString("id", currentItem.id.toString())
            val editTaskFragment = EditTask()
            editTaskFragment.arguments = bundle

            holder.itemView.findNavController().navigate(R.id.action_todoMain_to_editTask,bundle)
        }

    }
}



