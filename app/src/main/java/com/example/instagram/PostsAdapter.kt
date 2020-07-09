package com.example.instagram

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.hours

class PostsAdapter(private val context: Context, private var posts: MutableList<Post>) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    // Clean all elements of the recycler
    fun clear() {
        posts.clear()
        notifyDataSetChanged()
    }

    //Add a list of items --change to type used
    fun addAll(list: List<Post>) {
        posts.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textUsername: TextView = itemView.findViewById(R.id.textUsername)
        private val postImage: ImageView = itemView.findViewById(R.id.postImage)
        private val textDescription: TextView = itemView.findViewById(R.id.textDescription)
        private val textViewTimestamp: TextView = itemView.findViewById(R.id.textViewTimeStamp)
        private val buttonComment: ImageView = itemView.findViewById(R.id.buttonComment)

        fun bind(post: Post) {
            //Bind the post data to view elements
            textDescription.text = post.description
            textUsername.text = post.user!!.username

            textViewTimestamp.text = post.timeStamp!!.toString()

            buttonComment.setOnClickListener {

            }

            val image = post.image
            if (image != null) {
                Glide.with(context).load(post.image!!.url).into(postImage)
            }
        }




    }

}