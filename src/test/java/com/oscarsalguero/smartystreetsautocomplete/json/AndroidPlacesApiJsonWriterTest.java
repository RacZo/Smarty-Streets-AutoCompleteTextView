package com.oscarsalguero.smartystreetsautocomplete.json;

import android.util.JsonWriter;


import com.oscarsalguero.smartystreetsautocomplete.model.Address;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AndroidPlacesApiJsonWriterTest {
    AndroidSmartyStreetsApiJsonParser parser;
    private JsonWriter writer;
    private List<String> actual;

    @Before
    public void setUp() throws IOException {
        parser = new AndroidSmartyStreetsApiJsonParser();
        writer = mock(JsonWriter.class);
        actual = new ArrayList<>();

        when(writer.beginArray()).then(new CompositeValueAnswer(BEGIN_ARRAY));
        when(writer.endArray()).then(new CompositeValueAnswer(END_ARRAY));
        when(writer.beginObject()).then(new CompositeValueAnswer(BEGIN_OBJECT));
        when(writer.endObject()).then(new CompositeValueAnswer(END_OBJECT));
        when(writer.value(anyInt())).then(new ValueAnswer());
        when(writer.value(anyBoolean())).then(new ValueAnswer());
        when(writer.value(anyString())).then(new ValueAnswer());
        when(writer.name(anyString())).then(new ValueAnswer());
    }

    @Test
    public void writePlaceTest() throws IOException {
        Address place = new Address(
                "48 W 38th St, New York NY",
                "48 W 38th St",
                "New York",
                "NY");
        parser.writePlace(writer, place);
        List<String> expected = Arrays.asList(BEGIN_OBJECT,
                "text", "48 W 38th St, New York NY",
                "street_line", "48 W 38th St",
                "city", "New York",
                "state", "NY",
                END_OBJECT);
        assertEquals(expected, actual);
    }

    private static String BEGIN_ARRAY = "[";
    private static String END_ARRAY = "]";
    private static String BEGIN_OBJECT = "{";
    private static String END_OBJECT = "}";

    private class CompositeValueAnswer implements Answer<JsonWriter> {
        private String elem;

        public CompositeValueAnswer(String elem) {
            this.elem = elem;
        }

        @Override
        public JsonWriter answer(InvocationOnMock invocation) throws Throwable {
            actual.add(elem);
            return writer;
        }
    }

    private class ValueAnswer implements Answer<JsonWriter> {
        @Override
        public JsonWriter answer(InvocationOnMock invocation) throws Throwable {
            actual.add(invocation.getArguments()[0].toString());
            return writer;
        }
    }
}
