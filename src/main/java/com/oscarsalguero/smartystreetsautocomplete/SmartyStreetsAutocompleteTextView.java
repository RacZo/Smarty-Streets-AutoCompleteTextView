package com.oscarsalguero.smartystreetsautocomplete;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.ListAdapter;

import com.oscarsalguero.smartystreetsautocomplete.adapter.AbstractAddressAutocompleteAdapter;
import com.oscarsalguero.smartystreetsautocomplete.adapter.DefaultAutocompleteAdapter;
import com.oscarsalguero.smartystreetsautocomplete.history.AutocompleteHistoryManager;
import com.oscarsalguero.smartystreetsautocomplete.history.DefaultAutocompleteHistoryManager;
import com.oscarsalguero.smartystreetsautocomplete.model.Address;
import com.oscarsalguero.smartystreetsautocomplete.network.SmartyStreetsHttpClientResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SmartyStreetsAutocompleteTextView extends AutoCompleteTextView {

    private static final String LOG_TAG = SmartyStreetsAutocompleteTextView.class.getName();

    public static final int SUGGESTIONS = 3;

    @NonNull
    private SmartyStreetsApi api;

    @Nullable
    private OnAddressSelectedListener listener;

    @Nullable
    private AutocompleteHistoryManager historyManager;

    @NonNull
    private AbstractAddressAutocompleteAdapter adapter;

    private boolean completionEnabled = true;

    /**
     * Creates a new SmartyStreetsAutocompleteTextView with the provided API key, referer and the default history file
     */
    public SmartyStreetsAutocompleteTextView(@NonNull final Context context, @NonNull final String webApiKey, @NonNull final String referer, int suggestions) {
        super(context);

        init(context, null, R.attr.ssacv_addressAutoCompleteTextViewStyle, R.style.SSACV_Widget_SmartyStreetsAutoCompleteTextView, webApiKey, referer, suggestions, context.getString(R.string.ssacv_default_history_file_name));
    }

    /**
     * Creates a new SmartyStreetsAutocompleteTextView with the provided API key, referer and the provided history file
     */
    public SmartyStreetsAutocompleteTextView(@NonNull final Context context, @NonNull final String webApiKey, @NonNull final String referer, @NonNull final String historyFileName) {
        super(context);

        init(context, null, R.attr.ssacv_addressAutoCompleteTextViewStyle, R.style.SSACV_Widget_SmartyStreetsAutoCompleteTextView, webApiKey, referer, 0, historyFileName);
    }

    /**
     * Constructor for layout inflation
     */
    public SmartyStreetsAutocompleteTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, R.attr.ssacv_addressAutoCompleteTextViewStyle, R.style.SSACV_Widget_SmartyStreetsAutoCompleteTextView, null, null, 0, context.getString(R.string.ssacv_default_history_file_name));
    }

    /**
     * Constructor for layout inflation
     */
    public SmartyStreetsAutocompleteTextView(final Context context, final AttributeSet attrs, final int defAttr) {
        super(context, attrs, defAttr);

        init(context, attrs, defAttr, R.style.SSACV_Widget_SmartyStreetsAutoCompleteTextView, null, null, 0, context.getString(R.string.ssacv_default_history_file_name));
    }

    /**
     * Constructor for layout inflation
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmartyStreetsAutocompleteTextView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes, null, null, 0, context.getString(R.string.ssacv_default_history_file_name));
    }

    // perform basic initialization of the view by fetching layout attributes and creating the api, etc.
    private void init(@NonNull final Context context, final AttributeSet attrs, final int defAttr, final int defStyle, final String webApiKey, final String referer, final int suggestions, final String historyFileName) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartyStreetsAutocompleteTextView, defAttr, defStyle);
        String layoutApiKey = typedArray.getString(R.styleable.SmartyStreetsAutocompleteTextView_ssacv_webApiKey);
        String layoutReferer = typedArray.getString(R.styleable.SmartyStreetsAutocompleteTextView_ssacv_referer);
        int layoutSuggestions = typedArray.getInteger(R.styleable.SmartyStreetsAutocompleteTextView_ssacv_suggestions, SUGGESTIONS);
        String layoutAdapterClass = typedArray.getString(R.styleable.SmartyStreetsAutocompleteTextView_ssacv_adapterClass);
        String layoutHistoryFile = typedArray.getString(R.styleable.SmartyStreetsAutocompleteTextView_ssacv_historyFile);
        typedArray.recycle();

        final String finalHistoryFileName = historyFileName != null ? historyFileName : layoutHistoryFile;

        if (!TextUtils.isEmpty(finalHistoryFileName)) {
            historyManager = DefaultAutocompleteHistoryManager.fromPath(context, finalHistoryFileName);
        }

        final String finalApiKey = webApiKey != null ? webApiKey : layoutApiKey;

        if (TextUtils.isEmpty(finalApiKey)) {
            throw new InflateException("Did not specify web API Key!");
        }

        final String finalReferer = referer != null ? referer : layoutReferer;

        if (TextUtils.isEmpty(finalReferer)) {
            throw new InflateException("Did not specify referer!");
        }

        final int finalSuggestions = layoutSuggestions;
        if (finalSuggestions == 0) {
            Log.w(LOG_TAG, "Suggestions not set, will show the default (" + SUGGESTIONS + ")");
        }

        api = new SmartyStreetsApiBuilder()
                .setApiClient(SmartyStreetsHttpClientResolver.SMARTY_STREETS_HTTP_CLIENT)
                .setWebApiKey(finalApiKey)
                .setReferer(finalReferer)
                .setSuggestions(finalSuggestions)
                .build();

        if (layoutAdapterClass != null) {
            adapter = adapterForClass(context, layoutAdapterClass);
        } else {
            adapter = new DefaultAutocompleteAdapter(context, api, historyManager);
        }

        super.setAdapter(adapter);

        super.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                Address address = adapter.getItem(position);

                if (listener != null) {
                    listener.onAddressSelected(address);
                }

                if (historyManager != null) {
                    historyManager.addItemToHistory(address);
                }
            }
        });

        super.setDropDownBackgroundResource(R.drawable.ssacv_popup_background_white);
    }

    /**
     * DO NOT USE. Prefer {@link #setOnAddressSelectedListener} instead
     */
    @Override
    public final void setOnItemSelectedListener(final AdapterView.OnItemSelectedListener l) {
        throw new UnsupportedOperationException("Use set" + OnAddressSelectedListener.class.getSimpleName() + "() instead");
    }

    /**
     * DO NOT USE. Prefer {@link #setOnAddressSelectedListener} instead
     */
    @Override
    public final void setOnItemClickListener(final AdapterView.OnItemClickListener l) {
        throw new UnsupportedOperationException("Use set" + OnAddressSelectedListener.class.getSimpleName() + "() instead");
    }

    /**
     * Registers a listener for callbacks when a new {@link Address} is selected from the autocomplete
     * popup
     */
    public void setOnAddressSelectedListener(@Nullable OnAddressSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * @return the current adapter for displaying the list of results in the popup window
     */
    @NonNull
    public AbstractAddressAutocompleteAdapter getAutocompleteAdapter() {
        return adapter;
    }

    /**
     * @param adapter the adapter for displaying the list of results in the popup window, must
     *                extend {@link AbstractAddressAutocompleteAdapter} to maintain certain logic
     */
    @Override
    public final <T extends ListAdapter & Filterable> void setAdapter(@NonNull final T adapter) {
        if (!(adapter instanceof AbstractAddressAutocompleteAdapter)) {
            throw new IllegalArgumentException("Custom adapters must inherit from " + AbstractAddressAutocompleteAdapter.class.getSimpleName());
        }

        this.adapter = (AbstractAddressAutocompleteAdapter) adapter;

        historyManager = this.adapter.getHistoryManager();
        api = this.adapter.getApi();

        super.setAdapter(adapter);
    }

    // fun way to set adapters as layout attributes
    private AbstractAddressAutocompleteAdapter adapterForClass(final Context context, final String adapterClass) {
        Class<AbstractAddressAutocompleteAdapter> adapterClazz;
        try {
            adapterClazz = (Class<AbstractAddressAutocompleteAdapter>) Class.forName(adapterClass);
        } catch (ClassNotFoundException e) {
            throw new InflateException("Unable to find class for specified adapterClass: " + adapterClass, e);
        } catch (ClassCastException e) {
            throw new InflateException(adapterClass + " must inherit from " + AbstractAddressAutocompleteAdapter.class.getSimpleName(), e);
        }

        Constructor<AbstractAddressAutocompleteAdapter> adapterConstructor;
        try {
            adapterConstructor = adapterClazz.getConstructor(Context.class, SmartyStreetsApi.class, AutocompleteHistoryManager.class);
        } catch (NoSuchMethodException e) {
            throw new InflateException("Unable to find valid constructor with params " +
                    Context.class.getSimpleName() +
                    ", " +
                    SmartyStreetsApi.class.getSimpleName() +
                    ", and " +
                    AutocompleteHistoryManager.class.getSimpleName() +
                    " for specified adapterClass: " + adapterClass, e);
        }

        try {
            return adapterConstructor.newInstance(context, api, historyManager);
        } catch (InstantiationException e) {
            throw new InflateException("Unable to instantiate adapter with name " + adapterClass, e);
        } catch (IllegalAccessException e) {
            throw new InflateException("Unable to instantiate adapter with name " + adapterClass, e);
        } catch (InvocationTargetException e) {
            throw new InflateException("Unable to instantiate adapter with name " + adapterClass, e);
        }
    }

    /**
     * Controls the autocompletion feature.
     *
     * @param isEnabled if false, no autocompletion will occur. Default is true
     */
    public void setCompletionEnabled(boolean isEnabled) {
        completionEnabled = isEnabled;
    }

    @Override
    public boolean enoughToFilter() {
        return completionEnabled && (historyManager != null || super.enoughToFilter());
    }

    @Override
    public void performCompletion() {
        if (!completionEnabled) {
            return;
        }

        super.performCompletion();
    }

    @Override
    protected void performFiltering(CharSequence text, final int keyCode) {
        if (text == null || text.length() <= getThreshold()) {
            text = text == null ? "" : text;
            super.performFiltering(Constants.MAGIC_HISTORY_VALUE_PRE + text, keyCode);
        } else {
            super.performFiltering(text, keyCode);
        }
    }

    @Override
    protected CharSequence convertSelectionToString(final Object selectedItem) {
        return ((Address) selectedItem).getText();
    }

    /**
     * @return the {@link SmartyStreetsApi} that the Autocomplete view is using to fetch results from the
     * Google Maps Places API. You can use this to make custom requests to the API, if you so choose.
     */
    @NonNull
    public SmartyStreetsApi getApi() {
        return api;
    }

    /**
     * A setter for the {@link SmartyStreetsApi} that the Autocomplete view will use to fetch results from the
     * Google Maps Places API. You can provide your own customizations build creating your own API.
     *
     * @param api the API to use for autocompletion and address details requests
     */
    public void setApi(@NonNull SmartyStreetsApi api) {
        this.api = api;
        adapter.setApi(api);
    }

    /**
     * @return the current AutocompleteHistoryManager that stores and provides the selection history
     * for Places in the Autocomplete view
     */
    @Nullable
    public AutocompleteHistoryManager getHistoryManager() {
        return historyManager;
    }

    /**
     * Allows for passing your own implementation of the AutocompleteHistoryManager. This would be
     * if you wanted to provide your own storage mechanism (e.g. sqlite, shared prefs, etc.) for
     * whatever reasoning you'd want.
     *
     * @param historyManager The new history manager for managing the storage of selected Places for
     *                       later autocompletion use. Setting this to null will disable history
     */
    public void setHistoryManager(@Nullable final AutocompleteHistoryManager historyManager) {
        this.historyManager = historyManager;

        adapter.setHistoryManager(historyManager);
    }
}
