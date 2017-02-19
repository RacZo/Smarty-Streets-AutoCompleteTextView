package com.oscarsalguero.smartystreetsautocomplete.json;

import com.oscarsalguero.smartystreetsautocomplete.model.Address;
import com.oscarsalguero.smartystreetsautocomplete.model.SuggestionsResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface SmartyStreetsApiJsonParser {

    SuggestionsResponse autocompleteFromStream(InputStream is) throws JsonParsingException;

    List<Address> readHistoryJson(InputStream in) throws JsonParsingException;

    void writeHistoryJson(OutputStream os, List<Address> addresses) throws JsonWritingException;
}
