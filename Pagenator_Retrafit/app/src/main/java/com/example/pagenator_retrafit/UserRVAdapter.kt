package com.example.pagenator_retrafit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class UserRVAdapter(
    private val userModalArrayList: ArrayList<UserModal>, private val context: Context
) :
    RecyclerView.Adapter<UserRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // getting data from our array list in our modal class.
        val userModal = userModalArrayList[position]

        // on below line we are setting data to our text view.
        holder.firstNameTV.text = userModal.first_name
        holder.lastNameTV.text = userModal.last_name
        holder.emailTV.text = userModal.email

        // on below line we are loading our image
        // from url in our image view using picasso.
        Picasso.get()
            .load(userModal.avatar)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .fit().into(holder.userIV)

        holder.itemView.setOnClickListener { Toast.makeText(context, holder.adapterPosition.toString(),Toast.LENGTH_LONG).show() }
    }

    override fun getItemCount(): Int {
        // returning the size of array list.
        return userModalArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val firstNameTV: TextView
        val lastNameTV: TextView
        val emailTV: TextView
        val userIV: ImageView

        init {

            // initializing our variables.
            firstNameTV = itemView.findViewById(R.id.idTVFirstName)
            lastNameTV = itemView.findViewById(R.id.idTVLastName)
            emailTV = itemView.findViewById(R.id.idTVEmail)
            userIV = itemView.findViewById(R.id.idIVUser)
        }
    }
}
