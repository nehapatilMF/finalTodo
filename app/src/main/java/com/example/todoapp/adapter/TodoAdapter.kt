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
import com.example.todoapp.util.CalenderUtil
import com.example.todoapp.util.TimePickerUtil

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

            val date1 = list.todo_date
            date.text  = CalenderUtil.reConvertDateFormat(date1)

            val time1 = list.todo_time
            time.text = TimePickerUtil.reConvertTime(time1)

            val id = list.id
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

            val cDate = CalenderUtil.reConvertDateFormat(currentItem.todo_date)
            bundle.putString("date", cDate)

            val cTime = TimePickerUtil.reConvertTime(currentItem.todo_time)
            bundle.putString("time", cTime)

            bundle.putString("id", currentItem.id.toString())
            val editTaskFragment = EditTask()
            editTaskFragment.arguments = bundle
            holder.itemView.findNavController().navigate(R.id.action_home_to_editTask,bundle)
        }

    }
}



