package com.mobiledevelopment.actex.models;

public enum ListType {
    FAVORITE(0),
    WATCHLIST(1),
    CUSTOM(2);

    int id;

    ListType(int id) {
        this.id = id;
    }
}
