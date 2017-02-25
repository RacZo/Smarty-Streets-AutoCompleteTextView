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
