package com.oscarsalguero.smartystreetsautocomplete.history;

import android.support.annotation.NonNull;

import com.oscarsalguero.smartystreetsautocomplete.model.Address;

import java.util.List;

public interface OnHistoryUpdatedListener {
    public void onHistoryUpdated(@NonNull List<Address> updatedHistory);
}
