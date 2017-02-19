package com.oscarsalguero.smartystreetsautocomplete.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.oscarsalguero.smartystreetsautocomplete.SmartyStreetsApi;
import com.oscarsalguero.smartystreetsautocomplete.history.AutocompleteHistoryManager;
import com.oscarsalguero.smartystreetsautocomplete.model.Address;
import com.oscarsalguero.smartystreetsautocomplete.util.ArrayAdapterDelegate;

import java.util.Collection;

/**
 * Base class for Adapters for the AddressAutocompleteTextView. Provides the logic for fetching the
 * autocomplete results using the Adapter's filtering mechanism. Can provide your own implementation
 * to provide a different row view for the address.
 * Created by oscar on 2/14/17.
 */

public abstract class AbstractAddressAutocompleteAdapter extends ArrayAdapter<Address> {

    @NonNull
    private final SmartyStreetsFilter mFilter;

    public AbstractAddressAutocompleteAdapter(@NonNull Context context,
                                              @NonNull SmartyStreetsApi api,
                                              @Nullable AutocompleteHistoryManager historyManager) {
        super(context, 0);
        mFilter = new SmartyStreetsFilter(
                api,
                historyManager,
                new ArrayAdapterDelegate<Address>() {

                    @Override
                    public void setNotifyOnChange(final boolean notifyOnChange) {
                        AbstractAddressAutocompleteAdapter.this.setNotifyOnChange(notifyOnChange);
                    }

                    @Override
                    public void clear() {
                        AbstractAddressAutocompleteAdapter.this.clear();
                    }

                    @Override
                    public void addAll(final Collection<Address> values) {
                        if (values != null) {
                            AbstractAddressAutocompleteAdapter.this.addAll(values);
                        }
                    }

                    @Override
                    public void notifyDataSetChanged() {
                        AbstractAddressAutocompleteAdapter.this.notifyDataSetChanged();
                    }
                }
        );
    }

    @Override
    public final View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView == null ? newView(parent) : convertView;
        bindView(view, getItem(position));
        return view;
    }

    /**
     * Creates a new view for a Place
     *
     * @param parent the parent view, used for inflating
     * @return the row view for the item in the adapter
     */
    protected abstract View newView(final ViewGroup parent);

    /**
     * Binds a address to a given view
     *
     * @param view the view to bind to
     * @param item the address that should be bound to the view
     */
    protected abstract void bindView(final View view, final Address item);


    @Override
    @NonNull
    public Filter getFilter() {
        return mFilter;
    }

    /**
     * @param historyManager the history manager to use for adding items to history and fetching
     *                       past selection results
     */
    public void setHistoryManager(@Nullable final AutocompleteHistoryManager historyManager) {
        mFilter.setHistoryManager(historyManager);
    }

    /**
     * @param api the instance of the SmartyStreetsApi to use for autocompletion requests
     */
    public void setApi(@NonNull final SmartyStreetsApi api) {
        mFilter.setApi(api);
    }

    @NonNull
    public SmartyStreetsApi getApi() {
        return mFilter.getApi();
    }

    @Nullable
    public AutocompleteHistoryManager getHistoryManager() {
        return mFilter.getHistoryManager();
    }

}
