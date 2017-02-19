package com.oscarsalguero.smartystreetsautocomplete.json;

import android.util.JsonReader;
import android.util.JsonWriter;

import com.oscarsalguero.smartystreetsautocomplete.model.Address;
import com.oscarsalguero.smartystreetsautocomplete.model.SuggestionsResponse;
import com.oscarsalguero.smartystreetsautocomplete.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

class AndroidSmartyStreetsApiJsonParser implements SmartyStreetsApiJsonParser {

    @Override
    public SuggestionsResponse autocompleteFromStream(final InputStream is) throws JsonParsingException {
        JsonReader reader = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            reader = new JsonReader(bufferedReader);

            List<Address> suggestions = null;

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "suggestions":
                        suggestions = readPredictionsArray(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            return new SuggestionsResponse(suggestions);
        } catch (Exception e) {
            throw new JsonParsingException(e);
        } finally {
            ResourceUtils.closeResourceQuietly(reader);
        }
    }

    @Override
    public List<Address> readHistoryJson(final InputStream in) throws JsonParsingException {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            List<Address> addresses = new ArrayList<>();
            reader.beginArray();
            while (reader.hasNext()) {
                addresses.add(readPlace(reader));
            }
            reader.endArray();
            reader.close();
            return addresses;
        } catch (Exception e) {
            throw new JsonParsingException(e);
        } finally {
            ResourceUtils.closeResourceQuietly(reader);
        }
    }

    @Override
    public void writeHistoryJson(final OutputStream os, final List<Address> addresses) throws JsonWritingException {
        JsonWriter writer = null;
        try {
            writer = new JsonWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.setIndent("  ");
            writer.beginArray();
            for (Address address : addresses) {
                writePlace(writer, address);
            }
            writer.endArray();
            writer.close();
        } catch (Exception e) {
            throw new JsonWritingException(e);
        } finally {
            ResourceUtils.closeResourceQuietly(writer);
        }
    }

    void writePlace(JsonWriter writer, Address address) throws IOException {
        writer.beginObject();
        writer.name("text").value(address.getText());
        writer.name("street_line").value(address.getStreetLine());
        writer.name("city").value(address.getCity());
        writer.name("state").value(address.getState());
        writer.endObject();
    }

    List<Address> readPredictionsArray(JsonReader reader) throws IOException {
        List<Address> predictions = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            predictions.add(readPlace(reader));
        }
        reader.endArray();
        return predictions;
    }

    Address readPlace(JsonReader reader) throws IOException {
        String text = null;
        String streetLine = null;
        String city = null;
        String state = null;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "text":
                    text = reader.nextString();
                    break;
                case "street_line":
                    streetLine = reader.nextString();
                    break;
                case "city":
                    city = reader.nextString();
                    break;
                case "state":
                    state = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Address(text, streetLine, city, state);
    }
}
