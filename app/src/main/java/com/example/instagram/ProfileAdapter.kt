package com.example.instagram

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.parse.ParseFile
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.parceler.Parcels

class ProfileAdapter(private val context: Context, private var posts: MutableList<Post>) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        private val textUsername: TextView = itemView.findViewById(R.id.textItemUsername)
        private val postImage: ImageView = itemView.findViewById(R.id.postItemImage)
        private val textDescription: TextView = itemView.findViewById(R.id.textItemDescription)
        private val textViewTimestamp: TextView = itemView.findViewById(R.id.textItemTimeStamp)
        private val imageItemProfile: ImageView = itemView.findViewById(R.id.imageItemProfile)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(post: Post) {
            //Bind the post data to view elements
            textDescription.text = post.description
            textUsername.text = post.user!!.username

            textViewTimestamp.text = post.timeStamp!!.toString()

            val image = post.image
            if (image != null) {
                Glide.with(context).load(post.image!!.url).into(postImage)
            }

            val radius = 100
            val margin = 2
            val profile : ParseFile = post.user!!.get("profilePic") as ParseFile
            Glide.with(context).load(profile.url).transform(RoundedCornersTransformation(radius,margin)).into(imageItemProfile)
        }

        // onClick to go to PostsDetailsActivity
        override fun onClick(p0: View?) {
            // Get item position
            Log.i("PROFILE_ADAPTER", "ITEM CLICKED")
            val position : Int = adapterPosition

            //Make sure position is valid i.e actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                //Get the post at the position
                val post : Post = posts[position]

                //Create the intent for the new activity
                val i =  Intent(context, PostDetailsActivity::class.java)

                //Serialize the post using the parceler
                i.putExtra(Post::class.java.simpleName, Parcels.wrap(post))

                // Show activity
                context.startActivity(i)
            }
        }


    }

}