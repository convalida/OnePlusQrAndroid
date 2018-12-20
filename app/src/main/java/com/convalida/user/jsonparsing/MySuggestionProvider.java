package com.convalida.user.jsonparsing;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Convalida on 8/8/2017.
 */

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY=MySuggestionProvider.class.getName();
    public static final int MODE=DATABASE_MODE_QUERIES;

    public MySuggestionProvider(){
        setupSuggestions(AUTHORITY,MODE);
    }
}
