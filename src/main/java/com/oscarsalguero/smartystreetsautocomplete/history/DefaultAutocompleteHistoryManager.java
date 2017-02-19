package com.oscarsalguero.smartystreetsautocomplete.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.AtomicFile;
import android.text.TextUtils;
import android.util.Log;

import com.oscarsalguero.smartystreetsautocomplete.Constants;
import com.oscarsalguero.smartystreetsautocomplete.async.BackgroundExecutorService;
import com.oscarsalguero.smartystreetsautocomplete.async.BackgroundJob;
import com.oscarsalguero.smartystreetsautocomplete.json.JsonParserResolver;
import com.oscarsalguero.smartystreetsautocomplete.json.SmartyStreetsApiJsonParser;
import com.oscarsalguero.smartystreetsautocomplete.model.Address;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultAutocompleteHistoryManager implements AutocompleteHistoryManager {

    private static final String LOG_TAG = DefaultAutocompleteHistoryManager.class.getName();

    private static final String BASE_AUTOCOMPLETE_HISTORY_DIR = "autocomplete";

    private static final int MAX_HISTORY_ITEM_COUNT = 5;

    public static AutocompleteHistoryManager fromPath(@NonNull Context context, @NonNull String historyFileName) {
        if (TextUtils.isEmpty(historyFileName)) {
            throw new IllegalArgumentException("Cannot have an empty historyFile name");
        }


        File historyDir = new File(context.getCacheDir(), BASE_AUTOCOMPLETE_HISTORY_DIR);

        if (!historyDir.exists()) {
            historyDir.mkdirs();
        }

        return new DefaultAutocompleteHistoryManager(new File(historyDir, historyFileName), JsonParserResolver.JSON_PARSER);
    }

    @NonNull
    private final AtomicFile savedFile;

    @NonNull
    private final SmartyStreetsApiJsonParser jsonParser;

    @NonNull
    private List<Address> addresses;

    @Nullable
    private OnHistoryUpdatedListener listener;

    private DefaultAutocompleteHistoryManager(@NonNull final File historyFile, @NonNull final SmartyStreetsApiJsonParser parser) {
        savedFile = new AtomicFile(historyFile);

        jsonParser = parser;

        addresses = new ArrayList<>();

        readPlaces();
    }

    private void readPlaces() {
        BackgroundExecutorService.INSTANCE.enqueue(new BackgroundJob<List<Address>>() {
            @Override
            public List<Address> executeInBackground() throws Exception {
                if (!savedFile.getBaseFile().exists()) {
                    return Collections.emptyList();
                }

                InputStream is = null;
                try {
                    is = savedFile.openRead();
                    return jsonParser.readHistoryJson(is);
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            }

            @Override
            public void onSuccess(final List<Address> result) {
                Collections.reverse(result);

                for (Address address : result) {
                    internalAddItem(address);
                }
            }

            @Override
            public void onFailure(final Throwable error) {
                if (Constants.DEBUG) {
                    Log.e(LOG_TAG, "Unable to load history from history file", error);
                }
            }
        });
    }

    @Override
    public void setListener(@Nullable final OnHistoryUpdatedListener listener) {
        this.listener = listener;
    }

    @Override
    public void addItemToHistory(@NonNull final Address address) {
        internalAddItem(address);

        executeSave();
    }

    private void internalAddItem(@NonNull final Address address) {
        addresses.remove(address);
        addresses.add(0, address);

        trimPlaces();
    }

    private void trimPlaces() {
        if (addresses.size() > MAX_HISTORY_ITEM_COUNT) {
            addresses = new ArrayList<>(addresses.subList(0, MAX_HISTORY_ITEM_COUNT));
        }
    }

    private void executeSave() {
        final List<Address> finalPlaces = new ArrayList<>(addresses);

        BackgroundExecutorService.INSTANCE.enqueue(new BackgroundJob<Void>() {
            @Override
            public Void executeInBackground() throws Exception {
                FileOutputStream fos = null;
                try {
                    fos = savedFile.startWrite();
                    jsonParser.writeHistoryJson(fos, finalPlaces);
                } catch (IOException e) {
                    savedFile.failWrite(fos);
                    throw new IOException("Failed history file write", e);
                } finally {
                    savedFile.finishWrite(fos);
                }
                return null;
            }

            @Override
            public void onSuccess(Void result) {
                fireUpdatedListener();
                if (Constants.DEBUG) {
                    Log.i(LOG_TAG, "Successfully wrote autocomplete history.");
                }
            }

            @Override
            public void onFailure(final Throwable error) {
                addresses = new ArrayList<>();
                fireUpdatedListener();
                if (Constants.DEBUG) {
                    Log.e(LOG_TAG, "Failure to save the autocomplete history!", error);
                }
            }

            private void fireUpdatedListener() {
                if (listener != null) {
                    listener.onHistoryUpdated(addresses);
                }
            }
        });
    }

    @Override
    @NonNull
    public List<Address> getPastSelections() {
        return addresses;
    }

}
