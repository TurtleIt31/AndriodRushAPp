package com.example.rushapp

import Data.Models.User
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserTableAdapter(
    private val userList: List<User>, // List of user data
    private val onEditClick: (User) -> Unit // Lambda function for handling edit clicks
) : RecyclerView.Adapter<UserTableAdapter.UserTableViewHolder>() {

    class UserTableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userDetailText: TextView = view.findViewById(R.id.userDetailText)
        val editIcon: ImageView = view.findViewById(R.id.editIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_table_row, parent, false)
        return UserTableViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserTableViewHolder, position: Int) {
        val user = userList[position]
        holder.userDetailText.text = "${user.name} - ${user.userType}" // Bind data to the TextView
        holder.editIcon.setOnClickListener {
            onEditClick(user) // Handle the click event for the edit icon
        }
    }

    override fun getItemCount(): Int = userList.size
}
