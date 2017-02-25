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
package com.oscarsalguero.smartystreetsautocomplete.history;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.oscarsalguero.smartystreetsautocomplete.model.Address;

import java.util.List;

/**
 * An interface used by the PlacesAutocompleteTextView to first filter with results matching the
 * historical selections by the user
 */
public interface AutocompleteHistoryManager {

    /**
     * @param listener a listener to register to this instance of an AutocompleteHistoryManager that
     *                 will report changes to the sotred history
     */
    void setListener(@Nullable OnHistoryUpdatedListener listener);

    /**
     * @param address a selected address that should be added to the history file
     */
    void addItemToHistory(@NonNull Address address);

    /**
     * @return a list of the past selections. Should be ordered in some logical manner. The default
     * is most recently selected order.
     */
    @NonNull
    List<Address> getPastSelections();
}
