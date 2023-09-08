package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.todoapp.R
import org.json.JSONObject

class PersonalInfoAdapter(private val userJson: String) {

    inner class TodoViewHolder(itemView: View) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val email: TextView = itemView.findViewById(R.id.tvEmail)
        val mobile: TextView = itemView.findViewById(R.id.tvMobile)

        fun bindItem(user: JSONObject) {
            name.text = user.getString("name")
            email.text = user.getString("email")
            mobile.text = user.getString("mobile_no")
        }
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_personal_information, parent, false)
        return TodoViewHolder(itemView)
    }

    fun getItemCount(): Int {
        return 1 // Since there's only one user in this case
    }

    fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val userData = JSONObject(userJson).getJSONObject("data").getJSONObject("user")
        holder.bindItem(userData)
    }

    fun onItemClick(position: Int) {
        // Handle item click if needed
    }
}
