

package com.example.filmhunt;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.filmhunt.Adapters.HomeRecyclerAdapter;
import com.example.filmhunt.Listeners.OnMovieClickListener;
import com.example.filmhunt.Listeners.OnSearchApiListener;
import com.example.filmhunt.Models.SearchApiResponse;


public class MainActivity extends AppCompatActivity implements OnMovieClickListener {

    private static int SPLASH_SCREEN = 6000;

    //Variables
    Animation
            topAnim,
            bottomAnim,
            leftAnim,
            rightAnim,
            mirrorAnim,
            fadeOutAnim,
            creditsAnim,
            lastCreditAnim;
    ImageView logo, logo2, logo3;
    TextView title, motto, description;
    SearchView search_view;
    RecyclerView recycler_view_home;
    HomeRecyclerAdapter adapter;
    RequestManager manager;
    ProgressDialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        creditsAnim = AnimationUtils.loadAnimation(this,R.anim.rolling_credits_animation);
        lastCreditAnim = AnimationUtils.loadAnimation(this,R.anim.last_credit_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        leftAnim = AnimationUtils.loadAnimation(this,R.anim.middle_left_animation);
        rightAnim = AnimationUtils.loadAnimation(this,R.anim.middle_right_animation);
        mirrorAnim = AnimationUtils.loadAnimation(this,R.anim.mirror_animation);
        fadeOutAnim = AnimationUtils.loadAnimation(this,R.anim.fade_out_animation);

        //Hooks
        logo = findViewById(R.id.logo);
        logo2 = findViewById(R.id.logo2);
        logo3 = findViewById(R.id.logo3);
        title = findViewById(R.id.title);
        motto = findViewById(R.id.motto);
        description = findViewById(R.id.description);

        // Set listeners for the logo animation
        lastCreditAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // This method is called when the animation starts
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // This method is called when the animation ends

                // Make the title and motto views visible
                title.setVisibility(View.VISIBLE);
                motto.setVisibility(View.VISIBLE);

                // Start the animation when the logo animation ends
                title.setAnimation(leftAnim);
                motto.setAnimation(rightAnim);

                logo2.setAnimation(fadeOutAnim);
                logo3.setAnimation(fadeOutAnim);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // This method is called when the animation repeats (if set)
            }
        });

//        bottomAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                // This method is called when the animation starts
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                // This method is called when the animation ends
//
//                logo2.setAnimation(fadeOutAnim);
//                logo3.setAnimation(fadeOutAnim);
//
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // This method is called when the animation repeats (if set)
//            }
//        });


        // Start the animation set on the logos
        logo.setAnimation(topAnim);
        logo2.setAnimation(creditsAnim);
        logo3.setAnimation(lastCreditAnim);

        // Start the description animation
        description.setAnimation(bottomAnim);

        // Initially set title and motto views to be invisible
        title.setVisibility(View.INVISIBLE);
        motto.setVisibility(View.INVISIBLE);

//        logo.setAnimation(topAnim);
//        title.setAnimation(leftAnim);
//        motto.setAnimation(rightAnim);
//        description.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(logo, "logo_image");
                pairs[1] = new Pair<View, String>(title, "logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent, options.toBundle());
                finish();
            }
        }, SPLASH_SCREEN);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        search_view = findViewById(R.id.search_view);
        recycler_view_home = findViewById(R.id.recycler_view_home);
        dialog = new ProgressDialog(this);
        manager= new RequestManager( this);


        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Please wait...");
                dialog.show();
                manager.searchMovies(listener,query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
    private final OnSearchApiListener listener = new OnSearchApiListener() {
        @Override
        public void onResponse(SearchApiResponse response) {
            dialog.dismiss();
            if (response == null) {
                Toast.makeText(MainActivity.this, "No data Available", Toast.LENGTH_SHORT).show();
                return;
            }
            showResult(response);
        }

        @Override
        public void onError(String message) {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
        }
    };
    private void showResult(SearchApiResponse response) {
        recycler_view_home.setHasFixedSize(true);
        recycler_view_home.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        adapter = new HomeRecyclerAdapter(this, response.getTitles(), this);
        recycler_view_home.setAdapter(adapter);
    }


    @Override
    public void onMovieClicked(String id) {
        Toast.makeText(MainActivity.this, id, Toast.LENGTH_SHORT);

    }
}
