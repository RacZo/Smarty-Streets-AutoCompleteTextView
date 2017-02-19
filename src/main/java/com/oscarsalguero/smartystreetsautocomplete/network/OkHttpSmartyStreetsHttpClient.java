package com.oscarsalguero.smartystreetsautocomplete.network;

import android.net.Uri;

import com.oscarsalguero.smartystreetsautocomplete.Constants;
import com.oscarsalguero.smartystreetsautocomplete.json.SmartyStreetsApiJsonParser;
import com.oscarsalguero.smartystreetsautocomplete.model.SuggestionsResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

class OkHttpSmartyStreetsHttpClient extends AbstractSmartyStreetsHttpClient {
    private final OkHttpClient okHttpClient;

    OkHttpSmartyStreetsHttpClient(SmartyStreetsApiJsonParser parser) {
        super(parser);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .readTimeout(15L, TimeUnit.SECONDS)
                .connectTimeout(15L, TimeUnit.SECONDS)
                .writeTimeout(15L, TimeUnit.SECONDS);

        // Logging Interceptor to see what is happening with the request/response in LogCat
        if (Constants.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
        }

        okHttpClient = clientBuilder.build();
    }

    protected <T extends SuggestionsResponse> T executeNetworkRequest(final Uri uri, final String referer, final int suggestions, final ResponseHandler<T> responseHandler) throws IOException {
        final Request request = new Request.Builder()
                .url(uri.toString())
                .addHeader("Host", "us-autocomplete.api.smartystreets.com")
                .addHeader("Referer", referer)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        T body = responseHandler.handleStreamResult(response.body().byteStream());

        return body;
    }
}
