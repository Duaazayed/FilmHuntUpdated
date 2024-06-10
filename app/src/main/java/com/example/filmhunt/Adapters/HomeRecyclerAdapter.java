package com.example.filmhunt.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmhunt.Listeners.OnMovieClickListener;
import com.example.filmhunt.Models.SearchArrayObject;
import com.example.filmhunt.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    Context context;
    List<SearchArrayObject> list;
    OnMovieClickListener listener;

    public HomeRecyclerAdapter(Context context, List<SearchArrayObject> list, OnMovieClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.home_movies_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.textView_movie.setText(list.get(position).getTitle());
        Picasso.get().load(list.get(position).getImage()).into(holder.imageView_poster);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class HomeViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView_poster;
    TextView textView_movie;
    CardView home_container;
    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView_poster = itemView.findViewById(R.id.imageView_poster);
        textView_movie = itemView.findViewById(R.id. textView_movie);
        home_container = itemView.findViewById(R.id.home_container);

    }
}
