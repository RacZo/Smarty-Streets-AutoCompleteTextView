package com.oscarsalguero.smartystreetsautocomplete.network;


import com.oscarsalguero.smartystreetsautocomplete.json.JsonParsingException;

import java.io.InputStream;

interface ResponseHandler<T> {
    T handleStreamResult(InputStream is) throws JsonParsingException;
}
