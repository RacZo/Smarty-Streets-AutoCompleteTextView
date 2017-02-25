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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oscarsalguero.smartystreetsautocomplete.R;
import com.oscarsalguero.smartystreetsautocomplete.SmartyStreetsApi;
import com.oscarsalguero.smartystreetsautocomplete.history.AutocompleteHistoryManager;
import com.oscarsalguero.smartystreetsautocomplete.model.Address;

/**
 * Adapter for smarty streets address autocomplete
 * Created by oscar on 2/17/17.
 */
public class DefaultAutocompleteAdapter extends AbstractAddressAutocompleteAdapter {

    public DefaultAutocompleteAdapter(final Context context, final SmartyStreetsApi api, final AutocompleteHistoryManager historyManager) {
        super(context, api, historyManager);
    }

    @Override
    protected View newView(final ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.ssacv_maps_autocomplete_item, parent, false);
    }

    @Override
    protected void bindView(final View view, final Address address) {
        ((TextView) view).setText(address.getText());
    }

}
