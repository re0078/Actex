package com.mobiledevelopment.actex.models;


import com.mobiledevelopment.actex.views.TrendListAdapter;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public enum TrendListType {
    UPCOMING(0, new TrendListAdapter(new ArrayList<Movie>())), TOP_RATED(1, new TrendListAdapter(new ArrayList<>())), MOST_POPULAR(2, new TrendListAdapter(new ArrayList<>()));
    private int index;
    private TrendListAdapter adapter;

    TrendListType(int i, TrendListAdapter adapter) {
        this.adapter = adapter;
        this.index = i;
    }
}
