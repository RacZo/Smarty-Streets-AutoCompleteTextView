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
package com.oscarsalguero.smartystreetsautocomplete.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.oscarsalguero.smartystreetsautocomplete.model.Address;
import com.oscarsalguero.smartystreetsautocomplete.model.SuggestionsResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

class GsonSmartyStreetsApiJsonParser implements SmartyStreetsApiJsonParser {
    private final Gson gson;

    public GsonSmartyStreetsApiJsonParser() {
        gson = new GsonBuilder()
            .create();
    }

    @Override
    public SuggestionsResponse autocompleteFromStream(final InputStream is)  throws JsonParsingException {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return gson.fromJson(reader, SuggestionsResponse.class);
        } catch (Exception e) {
            throw new JsonParsingException(e);
        }
    }

    @Override
    public List<Address> readHistoryJson(final InputStream in) throws JsonParsingException {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            List<Address> addresses = new ArrayList<>();
            reader.beginArray();
            while (reader.hasNext()) {
                Address message = gson.fromJson(reader, Address.class);
                addresses.add(message);
            }
            reader.endArray();
            reader.close();
            return addresses;
        } catch (Exception e) {
            throw new JsonParsingException(e);
        }
    }

    @Override
    public void writeHistoryJson(final OutputStream os, final List<Address> addresses) throws JsonWritingException {
        try {
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.setIndent("  ");
            writer.beginArray();
            for (Address address : addresses) {
                gson.toJson(address, Address.class, writer);
            }
            writer.endArray();
            writer.close();
        } catch (Exception e) {
            throw new JsonWritingException(e);
        }
    }
}
