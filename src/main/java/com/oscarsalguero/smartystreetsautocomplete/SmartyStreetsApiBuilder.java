package com.oscarsalguero.smartystreetsautocomplete;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.oscarsalguero.smartystreetsautocomplete.network.SmartyStreetsHttpClient;
import com.oscarsalguero.smartystreetsautocomplete.network.SmartyStreetsHttpClientResolver;


public class SmartyStreetsApiBuilder {

    @Nullable
    private SmartyStreetsHttpClient mApiClient;

    @Nullable
    private String mWebApiKey;

    @Nullable
    private String mReferer;

    private int mSuggestions;

    public SmartyStreetsApiBuilder setApiClient(@NonNull final SmartyStreetsHttpClient apiClient) {
        this.mApiClient = apiClient;
        return this;
    }

    public SmartyStreetsApiBuilder setWebApiKey(@NonNull final String webApiKey) {
        if (TextUtils.isEmpty(webApiKey)) {
            throw new IllegalArgumentException("web key cannot be null or empty!");
        }
        this.mWebApiKey = webApiKey;
        return this;
    }

    public SmartyStreetsApiBuilder setReferer(@NonNull final String referal) {
        if (TextUtils.isEmpty(referal)) {
            throw new IllegalArgumentException("referer cannot be null or empty!");
        }
        this.mReferer = referal;
        return this;
    }

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
            throw new IllegalArgumentException("referal cannot be null when building " + SmartyStreetsApi.class.getSimpleName());
        }

        return new SmartyStreetsApi(mApiClient, mWebApiKey, mReferer, mSuggestions);
    }
}
