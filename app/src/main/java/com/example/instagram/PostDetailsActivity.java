package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    private Post post;

    private TextView textItemUsername;
    private ImageView postItemImage;
    private ImageView imageItemProfile;
    private TextView textItemDescription;
    private TextView textItemTimestamp;
    private ImageView buttonItemLike;
    private ImageView buttonItemComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        //unwrap the post passed in via intent, using its simple name as a key
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        textItemDescription = findViewById(R.id.textItemDescription);
        textItemUsername = findViewById(R.id.textItemUsername);
        postItemImage = findViewById(R.id.postItemImage);
        imageItemProfile = findViewById(R.id.imageItemProfile);
        textItemTimestamp = findViewById(R.id.textItemTimeStamp);
        buttonItemLike = findViewById(R.id.buttonItemLike);
        buttonItemComment = findViewById(R.id.buttonItemComment);

        ParseFile profile = (ParseFile) post.getUser().get("profilePicture");

        Glide.with(this).load(profile.getUrl()).into(imageItemProfile);

        Glide.with(this).load(post.getImage().getUrl()).into(postItemImage);

        textItemDescription.setText(post.getDescription());
        textItemUsername.setText(post.getUser().getUsername());
        textItemTimestamp.setText(post.getTimeStamp().toString());




    }
}