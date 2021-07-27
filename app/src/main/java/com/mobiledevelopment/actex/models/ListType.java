package com.mobiledevelopment.actex.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ListType {
    FAVORITE(0),
    WATCHLIST(1),
    CUSTOM(2);

    int id;
}
