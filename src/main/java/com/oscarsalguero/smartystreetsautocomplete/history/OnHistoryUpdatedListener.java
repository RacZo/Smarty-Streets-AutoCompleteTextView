package com.oscarsalguero.smartystreetsautocomplete.history;

import android.support.annotation.NonNull;

import com.oscarsalguero.smartystreetsautocomplete.model.Address;

import java.util.List;

/**
 * History update listener
 */
public interface OnHistoryUpdatedListener {
    void onHistoryUpdated(@NonNull List<Address> updatedHistory);
}
