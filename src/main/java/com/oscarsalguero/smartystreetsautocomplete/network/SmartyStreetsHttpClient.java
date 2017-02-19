package com.oscarsalguero.smartystreetsautocomplete.network;

import android.net.Uri;

import com.oscarsalguero.smartystreetsautocomplete.model.SuggestionsResponse;

import java.io.IOException;

/**
 * Smarty Streets endpoints
 * Created by oscar on 2/17/17.
 */

public interface SmartyStreetsHttpClient {

    SuggestionsResponse executeAutocompleteRequest(Uri uri, String referal, int suggestions) throws IOException;

}
