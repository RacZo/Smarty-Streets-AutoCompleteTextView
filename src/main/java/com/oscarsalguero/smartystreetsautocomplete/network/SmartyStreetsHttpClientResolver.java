package com.oscarsalguero.smartystreetsautocomplete.network;

import com.oscarsalguero.smartystreetsautocomplete.json.JsonParserResolver;
import com.oscarsalguero.smartystreetsautocomplete.json.SmartyStreetsApiJsonParser;

public final class SmartyStreetsHttpClientResolver {

    public static final SmartyStreetsHttpClient SMARTY_STREETS_HTTP_CLIENT;

    static {
        boolean hasOkHttp;

        try {
            Class.forName("okhttp3.OkHttpClient");
            Class.forName("okhttp3.logging.HttpLoggingInterceptor");
            hasOkHttp = true;
        } catch (ClassNotFoundException e) {
            hasOkHttp = false;
        }

        SmartyStreetsApiJsonParser parser = JsonParserResolver.JSON_PARSER;

        SMARTY_STREETS_HTTP_CLIENT = hasOkHttp ? new OkHttpSmartyStreetsHttpClient(parser) : new HttpUrlConnectionMapsHttpClient(parser);
    }

    private SmartyStreetsHttpClientResolver() {
        throw new RuntimeException("No Instances!");
    }
}
