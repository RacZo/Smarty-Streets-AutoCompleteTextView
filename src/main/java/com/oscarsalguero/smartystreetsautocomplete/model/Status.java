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

public enum Status {
    @SerializedName("OK")
    OK(true),
    @SerializedName("ZERO_RESULTS")
    ZERO_RESULTS(true),
    @SerializedName("OVER_QUERY_LIMIT")
    OVER_QUERY_LIMIT(false),
    @SerializedName("REQUEST_DENIED")
    REQUEST_DENIED(false),
    @SerializedName("INVALID_REQUEST")
    INVALID_REQUEST(false);

    private final boolean successful;

    Status(final boolean successfulResponse) {
        successful = successfulResponse;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
