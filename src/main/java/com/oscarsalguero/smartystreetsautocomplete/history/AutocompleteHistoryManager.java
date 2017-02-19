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
