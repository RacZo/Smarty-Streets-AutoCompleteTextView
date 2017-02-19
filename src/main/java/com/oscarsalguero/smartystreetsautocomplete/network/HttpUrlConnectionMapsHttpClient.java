package com.oscarsalguero.smartystreetsautocomplete.network;

import android.net.Uri;

import com.oscarsalguero.smartystreetsautocomplete.json.SmartyStreetsApiJsonParser;
import com.oscarsalguero.smartystreetsautocomplete.model.SuggestionsResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpUrlConnectionMapsHttpClient extends AbstractSmartyStreetsHttpClient {

    HttpUrlConnectionMapsHttpClient(final SmartyStreetsApiJsonParser parser) {
        super(parser);
    }

    @Override
    protected <T extends SuggestionsResponse> T executeNetworkRequest(final Uri uri, final String referer, final int suggestions, final ResponseHandler<T> handler) throws IOException {
        URL url = new URL(uri.toString());

        T response = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                is = conn.getInputStream();
                response = handler.handleStreamResult(is);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

            if (is != null) {
                is.close();
            }
        }

        return response;
    }
}
