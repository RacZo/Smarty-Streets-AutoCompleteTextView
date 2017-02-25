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
