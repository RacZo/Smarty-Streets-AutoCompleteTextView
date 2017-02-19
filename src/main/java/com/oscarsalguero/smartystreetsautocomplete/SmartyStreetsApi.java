package com.oscarsalguero.smartystreetsautocomplete;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.oscarsalguero.smartystreetsautocomplete.model.SuggestionsResponse;
import com.oscarsalguero.smartystreetsautocomplete.network.SmartyStreetsHttpClient;

import java.io.IOException;

/**
 * An Abstraction for the Smarty Streets API.
 * Manages the building of requests to the API
 * Created by oscar on 2/17/17.
 */

public class SmartyStreetsApi {

    private static final String LOG_TAG = SmartyStreetsApi.class.getName();

    private static final String BASE_URL = "https://us-autocomplete.api.smartystreets.com";

    private static final String PATH = "suggest";
    private static final String PARAM_PREFIX = "prefix";
    private static final String PARAM_SUGGESTIONS = "suggestions";
    private static final String PARAM_CITY_FILTER = "city_filter";
    private static final String PARAM_CITY_FILTER_VALUE = "";
    private static final String PARAM_STATE_FILTER = "state_filter";
    private static final String PARAM_STATE_FILTER_VALUE = "AL,AK,AZ,AR,CA,CO,CT,DE,FL,GA,HI,ID,IL,IN,IA,KS,KY,LA,ME,MD,MA,MI,MN,MS,MO,MT,NE,NV,NH,NJ,NM,NY,NC,ND,OH,OK,OR,PA,RI,SC,SD,TN,TX,UT,VT,VA,WA,WV,WI,WY";
    private static final String PARAM_GEOLOCATE = "geolocate";
    private static final String PARAM_GEOLOCATE_VALUE = "true";
    private static final String PARAM_GEOLOCATE_PRECISION = "geolocate_precision";
    private static final String PARAM_GEOLOCATE_PRECISION_VALUE = "city";
    private static final String PARAM_ID = "auth-id";

    @NonNull
    private final SmartyStreetsHttpClient mHttpClient;

    @NonNull
    private final String mWebApiKey;

    @NonNull
    private final String mReferer;

    private String mParamSuggestionsValue;

    public SmartyStreetsApi(@NonNull final SmartyStreetsHttpClient httpClient, @NonNull final String webApiKey, @NonNull final String referer, int suggestions) {
        this.mHttpClient = httpClient;
        this.mWebApiKey = webApiKey;
        this.mReferer = referer;
        this.mParamSuggestionsValue = String.valueOf(suggestions);
    }

    /**
     * Performs autocompletion for the given prefix
     *
     * @param prefix the textual input that will be autocompleted
     * @return an {@link SuggestionsResponse} object
     * @throws IOException
     */
    public SuggestionsResponse autocomplete(String prefix) throws IOException {

        Uri uri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(PATH)
                .appendQueryParameter(PARAM_PREFIX, prefix)
                .appendQueryParameter(PARAM_SUGGESTIONS, mParamSuggestionsValue)
                .appendQueryParameter(PARAM_CITY_FILTER, PARAM_CITY_FILTER_VALUE)
                .appendQueryParameter(PARAM_STATE_FILTER, PARAM_STATE_FILTER_VALUE)
                .appendQueryParameter(PARAM_GEOLOCATE, PARAM_GEOLOCATE_VALUE)
                .appendQueryParameter(PARAM_GEOLOCATE_PRECISION, PARAM_GEOLOCATE_PRECISION_VALUE)
                .appendQueryParameter(PARAM_ID, mWebApiKey)
                .build();

        String url = uri.toString();

        Log.d(LOG_TAG, "Smarty Streets URL: " + url);
        return mHttpClient.executeAutocompleteRequest(uri, mReferer, Integer.parseInt(mParamSuggestionsValue));
    }

}
