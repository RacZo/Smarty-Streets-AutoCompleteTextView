package com.oscarsalguero.smartystreetsautocomplete.json;

public final class JsonParserResolver {
    public static final SmartyStreetsApiJsonParser JSON_PARSER;

    static {
        boolean hasGson;
        try {
            Class.forName("com.google.gson.Gson");
            hasGson = true;
        } catch (ClassNotFoundException e) {
            hasGson = false;
        }

        JSON_PARSER = hasGson ? new GsonSmartyStreetsApiJsonParser() : new AndroidSmartyStreetsApiJsonParser();
    }

    private JsonParserResolver() {
        throw new RuntimeException("No instances");
    }
}
