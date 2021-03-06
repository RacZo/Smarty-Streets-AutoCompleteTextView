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
package com.oscarsalguero.smartystreetsautocomplete.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Smarty Streets autocomplete response
 * Created by oscar on 2/14/17.
 */
public class SuggestionsResponse implements Serializable {

    @SerializedName("suggestions")
    List<Address> suggestions;

    public SuggestionsResponse(List<Address> suggestions) {
        this.suggestions = suggestions;
    }

    public List<Address> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Address> suggestions) {
        this.suggestions = suggestions;
    }
}
