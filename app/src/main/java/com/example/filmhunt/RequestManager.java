package com.example.filmhunt;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.filmhunt.Listeners.OnSearchApiListener;
import com.example.filmhunt.Models.SearchApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public class RequestManager {
    Context context;
    //private VectorDrawableCompat GsonConvertFactory;
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit= new Retrofit.Builder().baseUrl("https://imdb-movies-web-series-etc-search.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public RequestManager(Context context) {

        this.context = context;
    }
    public void searchMovies(OnSearchApiListener listener, String movie_name){
        getMovies getMovies= retrofit.create(RequestManager.getMovies.class);
        Call<SearchApiResponse> call = getMovies.callMovies(movie_name);

        call.enqueue(new Callback<SearchApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchApiResponse> call, @NonNull Response<SearchApiResponse> response) {
                if(!response.isSuccessful()){
                    String message = "Error: " + response.code() + " " + response.message();
                    Log.e(TAG, message);
                    Toast.makeText(context, "Couldn't fetch Data!" + message, Toast.LENGTH_SHORT).show();
                    return;
                }

                listener.onResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SearchApiResponse> call, @NonNull Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public interface getMovies {
        @Headers({
                "Accept: application/json",
                "x-rapidapi-host: imdb-movies-web-series-etc-search.p.rapidapi.com",
                "x-rapidapi-key: 7041044cdemsh7788d5e758f757ap1ac2a8jsnf9a0b8a83457"
        })
        @GET("search/{movie_name}")
        Call<SearchApiResponse> callMovies(
                @Path("movie_name") String movie_name
        );
    }
}
