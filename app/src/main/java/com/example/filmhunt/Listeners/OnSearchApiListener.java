package com.example.filmhunt.Listeners;

import com.example.filmhunt.Models.SearchApiResponse;

public interface OnSearchApiListener {
    void onResponse(SearchApiResponse response);
    void onError(String message);

}
