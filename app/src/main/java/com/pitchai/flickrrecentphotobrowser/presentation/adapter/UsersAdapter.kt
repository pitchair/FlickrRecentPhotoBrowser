/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.pitchai.flickrrecentphotobrowser.presentation.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.presentation.adapter.UsersAdapter.UserViewHolder

/**
 * Adaptar that manages a collection of.
 */
class UsersAdapter(context: Context) : RecyclerView.Adapter<UserViewHolder>() {
    private val usersCollection: List<String>
    private val layoutInflater: LayoutInflater
    override fun getItemCount(): Int {
        return 30
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = layoutInflater.inflate(R.layout.row_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.textViewTitle.text = "Hello$position"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class UserViewHolder(itemView: View) : ViewHolder(itemView) {
        var textViewTitle: TextView

        init {
            textViewTitle = itemView.findViewById<View>(R.id.title) as TextView
        }
    }

    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        usersCollection = emptyList()
    }
}