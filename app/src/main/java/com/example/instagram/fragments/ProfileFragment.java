package com.example.instagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagram.LoginActivity;
import com.example.instagram.Post;
import com.example.instagram.PostsAdapter;
import com.example.instagram.ProfileAdapter;
import com.example.instagram.R;
import com.example.instagram.SpacesItemDecoration;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";
    private RecyclerView recyclerPosts;
    private ProfileAdapter adapter;
    private List<Post> allPosts;
    private TextView textViewLogout;
    private ImageView profileImage;
    private TextView textViewProfileUsername;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerPosts = view.findViewById(R.id.recyclerPostsProfile);
        textViewLogout = view.findViewById(R.id.textViewLogout);
        profileImage =view.findViewById(R.id.profileImage);
        textViewProfileUsername = view.findViewById(R.id.textViewProfileUsername);

        textViewProfileUsername.setText(ParseUser.getCurrentUser().getUsername());

        ParseFile profile = (ParseFile) ParseUser.getCurrentUser().get("profilePicture");
        if (profile != null) {
            Glide.with(getContext()).load(profile.getUrl()).into(profileImage);
        }

        int radius = 30;
        int margin = 2;
        Glide.with(getContext()).load(profile.getUrl()).transform(new RoundedCornersTransformation(radius,margin)).into(profileImage);



        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

//        SpacesItemDecoration decoration = new SpacesItemDecoration(0);
//        recyclerPosts.addItemDecoration(decoration);

        // create the data source
        allPosts = new ArrayList<>();
        // create the adapter
        adapter = new ProfileAdapter(getContext(), allPosts);
        // set the adapter on the recycler view
        recyclerPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        recyclerPosts.setLayoutManager(new GridLayoutManager(getContext(),3));
        queryUserPosts();

    }


    private void queryUserPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        //include author information
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
