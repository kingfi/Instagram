package com.example.instagram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostsAdapter(private val context: Context, private val posts: List<Post>) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textUsername: TextView
        private val postImage: ImageView
        private val textDescription: TextView

        init {
            textUsername = itemView.findViewById(R.id.textUsername)
            textDescription = itemView.findViewById(R.id.textDescription)
            postImage = itemView.findViewById(R.id.postImage)
        }
        
        fun bind(post: Post) {
            //Bind the post data to view elements
            textDescription.text = post.description
            textUsername.text = post.user!!.username
            val image = post.image
            if (image != null) {
                Glide.with(context).load(post.image!!.url).into(postImage)
            }
        }


    }

}