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
package com.oscarsalguero.smartystreetsautocomplete;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.oscarsalguero.smartystreetsautocomplete.network.SmartyStreetsHttpClient;
import com.oscarsalguero.smartystreetsautocomplete.network.SmartyStreetsHttpClientResolver;

/**
 * Builder class used to set up an instance of {@link SmartyStreetsApi}
 */
public class SmartyStreetsApiBuilder {

    @Nullable
    private SmartyStreetsHttpClient mApiClient;

    @Nullable
    private String mWebApiKey;

    @Nullable
    private String mReferer;

    private int mSuggestions;

    /**
     * Basic API client constructor
     *
     * @param apiClient
     * @return
     */
    public SmartyStreetsApiBuilder setApiClient(@NonNull final SmartyStreetsHttpClient apiClient) {
        this.mApiClient = apiClient;
        return this;
    }

    /**
     * Adds the website key (API key)
     *
     * @param webApiKey
     * @return
     */
    public SmartyStreetsApiBuilder setWebApiKey(@NonNull final String webApiKey) {
        if (TextUtils.isEmpty(webApiKey)) {
            throw new IllegalArgumentException("web key cannot be null or empty!");
        }
        this.mWebApiKey = webApiKey;
        return this;
    }

    /**
     * Adds the referer
     *
     * @param referer
     * @return
     */
    public SmartyStreetsApiBuilder setReferer(@NonNull final String referer) {
        if (TextUtils.isEmpty(referer)) {
            throw new IllegalArgumentException("referer cannot be null or empty!");
        }
        this.mReferer = referer;
        return this;
    }

    /**
     * Sets how many suggestions you want to get from the Cloud API
     *
     * @param suggestions an integer with the number of suggestions you want to get
     * @return
     */
    public SmartyStreetsApiBuilder setSuggestions(final int suggestions) {
        this.mSuggestions = suggestions;
        return this;
    }

    @NonNull
    public SmartyStreetsApi build() {
        if (mApiClient == null) {
            mApiClient = SmartyStreetsHttpClientResolver.SMARTY_STREETS_HTTP_CLIENT;
        }

        if (mWebApiKey == null) {
            throw new IllegalArgumentException("web key cannot be null when building " + SmartyStreetsApi.class.getSimpleName());
        }

        if (mReferer == null) {
            throw new IllegalArgumentException("referer cannot be null when building " + SmartyStreetsApi.class.getSimpleName());
        }

        return new SmartyStreetsApi(mApiClient, mWebApiKey, mReferer, mSuggestions);
    }
}
