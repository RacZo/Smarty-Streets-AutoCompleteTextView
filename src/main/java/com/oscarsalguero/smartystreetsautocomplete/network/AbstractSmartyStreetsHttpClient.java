package com.oscarsalguero.smartystreetsautocomplete.network;

import android.net.Uri;

import com.oscarsalguero.smartystreetsautocomplete.json.JsonParsingException;
import com.oscarsalguero.smartystreetsautocomplete.json.SmartyStreetsApiJsonParser;
import com.oscarsalguero.smartystreetsautocomplete.model.SuggestionsResponse;

import java.io.IOException;
import java.io.InputStream;

abstract class AbstractSmartyStreetsHttpClient implements SmartyStreetsHttpClient {

    protected final SmartyStreetsApiJsonParser placesApiJsonParser;

    protected AbstractSmartyStreetsHttpClient(SmartyStreetsApiJsonParser parser) {
        placesApiJsonParser = parser;
    }

    @Override
    public SuggestionsResponse executeAutocompleteRequest(final Uri uri, final String referer, int suggestions) throws IOException {
        return executeNetworkRequest(uri, referer, suggestions, new ResponseHandler<SuggestionsResponse>() {

            @Override
            public SuggestionsResponse handleStreamResult(final InputStream is) throws JsonParsingException {
                return placesApiJsonParser.autocompleteFromStream(is);
            }
        });
    }

    protected abstract <T extends SuggestionsResponse> T executeNetworkRequest(Uri uri, String referer, int suggestions, ResponseHandler<T> responseHandler) throws IOException;

}
