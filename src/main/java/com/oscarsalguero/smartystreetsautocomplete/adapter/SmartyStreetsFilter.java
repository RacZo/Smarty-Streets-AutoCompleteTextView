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
package com.oscarsalguero.smartystreetsautocomplete.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Filter;

import com.oscarsalguero.smartystreetsautocomplete.Constants;
import com.oscarsalguero.smartystreetsautocomplete.SmartyStreetsApi;
import com.oscarsalguero.smartystreetsautocomplete.history.AutocompleteHistoryManager;
import com.oscarsalguero.smartystreetsautocomplete.model.Address;
import com.oscarsalguero.smartystreetsautocomplete.model.SuggestionsResponse;
import com.oscarsalguero.smartystreetsautocomplete.util.ArrayAdapterDelegate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Smarty Streets address filter
 * Created by oscar on 2/17/17.
 */
public class SmartyStreetsFilter extends Filter {

    private static final String LOG_TAG = SmartyStreetsFilter.class.getName();

    @NonNull
    private SmartyStreetsApi mApi;

    @Nullable
    private AutocompleteHistoryManager mHistoryManager;

    @NonNull
    private final ArrayAdapterDelegate<Address> mAdapterDelegate;

    public SmartyStreetsFilter(@NonNull final SmartyStreetsApi api, @Nullable final AutocompleteHistoryManager historyManager,
                               @NonNull final ArrayAdapterDelegate<Address> adapterDelegate) {
        this.mApi = api;
        this.mHistoryManager = historyManager;
        this.mAdapterDelegate = adapterDelegate;
    }

    @Override
    protected FilterResults performFiltering(final CharSequence constraint) {
        final FilterResults filterResults = new FilterResults();

        String stringConstraint = constraint != null ? constraint.toString() : "";
        boolean history = stringConstraint.startsWith(Constants.MAGIC_HISTORY_VALUE_PRE);

        if (history) {
            stringConstraint = stringConstraint.substring(Constants.MAGIC_HISTORY_VALUE_PRE.length());
        }

        final String finalStringConstraint = stringConstraint;

        if (TextUtils.isEmpty(finalStringConstraint) && mHistoryManager == null) {
            if (Constants.DEBUG) {
                Log.w(LOG_TAG, "Autocomplete called with an empty string...");
            }
            filterResults.values = new ArrayList<Address>(0);
            filterResults.count = 0;
        } else if ((TextUtils.isEmpty(finalStringConstraint) || history) && mHistoryManager != null) {
            final List<Address> pastSelections = mHistoryManager.getPastSelections();

            sortHistory(finalStringConstraint, pastSelections, false);

            filterResults.values = pastSelections;
            filterResults.count = pastSelections.size();
        } else {
            try {
                final SuggestionsResponse response = mApi.autocomplete(finalStringConstraint);
                filterResults.values = response.getSuggestions();
            } catch (final IOException e) {
                Log.e(LOG_TAG, "Unable to fetch autocomplete results from the api", e);
                filterResults.values = new ArrayList<Address>(0);
                filterResults.count = 0;
            }

            final List<Address> pastSelections = mHistoryManager != null ? mHistoryManager.getPastSelections() : null;
            if (pastSelections != null && !pastSelections.isEmpty()) {
                sortHistory(finalStringConstraint, pastSelections, true);

                for (final Address pastSelection : pastSelections) {
                    if (pastSelection.getText().startsWith(finalStringConstraint)) {
                        // remove the item if it was already returned from the api
                        ((List<Address>) filterResults.values).remove(pastSelection);
                        // insert into top
                        ((List<Address>) filterResults.values).add(0, pastSelection);
                    }
                }
            }
            filterResults.count = ((List<Address>) filterResults.values).size();
        }

        return filterResults;
    }

    private static void sortHistory(final String finalStringConstraint, final List<Address> pastSelections, final boolean asc) {
        if (!pastSelections.isEmpty()) {
            Collections.sort(pastSelections, new Comparator<Address>() {
                @Override
                public int compare(final Address lhs, final Address rhs) {
                    final boolean lhsStarts = lhs.getText().startsWith(finalStringConstraint);
                    final boolean rhsStarts = rhs.getText().startsWith(finalStringConstraint);
                    return lhsStarts && rhsStarts ? 0 : lhsStarts ? (asc ? 1 : -1) : (asc ? -1 : 1);
                }
            });
        }
    }

    @Override
    protected void publishResults(final CharSequence constraint, final FilterResults results) {
        mAdapterDelegate.setNotifyOnChange(false);
        mAdapterDelegate.clear();
        mAdapterDelegate.addAll((Collection<Address>) results.values);
        mAdapterDelegate.notifyDataSetChanged();
    }

    public void setApi(@NonNull final SmartyStreetsApi api) {
        this.mApi = api;
    }

    public void setHistoryManager(@Nullable final AutocompleteHistoryManager historyManager) {
        this.mHistoryManager = historyManager;
    }

    @NonNull
    public SmartyStreetsApi getApi() {
        return mApi;
    }

    @Nullable
    public AutocompleteHistoryManager getHistoryManager() {
        return mHistoryManager;
    }


}
