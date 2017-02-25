/***
 * Copyright (c) 2017 Oscar Salguero www.oscarsalguero.com
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    private static final String PARAM_HOST = "Host";
    private static final String PARAM_HOST_VALUE = "us-autocomplete.api.smartystreets.com";
    private static final String PARAM_REFERER = "Referer";

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
                .addHeader(PARAM_HOST, PARAM_HOST_VALUE)
                .addHeader(PARAM_REFERER, referer)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        T body = responseHandler.handleStreamResult(response.body().byteStream());

        return body;
    }
}
