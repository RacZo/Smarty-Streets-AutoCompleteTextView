package com.oscarsalguero.smartystreetsautocomplete.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Smarty Streets autocomplete response
 * Created by oscar on 2/14/17.
 */

public class SuggestionsResponse implements Serializable {

    @SerializedName("suggestions")
    List<Address> suggestions;

    public SuggestionsResponse(List<Address> suggestions) {
        this.suggestions = suggestions;
    }

    public List<Address> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Address> suggestions) {
        this.suggestions = suggestions;
    }
}
