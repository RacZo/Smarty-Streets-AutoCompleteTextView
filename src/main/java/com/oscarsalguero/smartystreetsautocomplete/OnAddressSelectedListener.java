package com.oscarsalguero.smartystreetsautocomplete;

import android.support.annotation.NonNull;

import com.oscarsalguero.smartystreetsautocomplete.model.Address;


/**
 * A listener for address selected events that fire when an item is selected from the autocomplete
 * popup
 */
public interface OnAddressSelectedListener {

    /**
     * Called when a new address has been selected from the autocomplete popup
     * @param address the selected address
     */
    void onAddressSelected(@NonNull Address address);
}
