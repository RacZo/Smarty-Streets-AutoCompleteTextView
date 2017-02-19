package com.oscarsalguero.smartystreetsautocomplete.json;

import android.util.JsonReader;

import com.oscarsalguero.smartystreetsautocomplete.model.Address;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AndroidPlacesApiJsonParserTest {
    private AndroidSmartyStreetsApiJsonParser parser;
    private JsonReader reader;

    @Before
    public void setUp() {
        parser = mock(AndroidSmartyStreetsApiJsonParser.class);
        reader = mock(JsonReader.class);
    }

    @Test
    public void readPlaceTest() throws IOException {
        when(parser.readPlace(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, false, false, false);
        when(reader.nextName()).thenReturn("text", "street_line", "city", "state");
        when(reader.nextString()).thenReturn("48 W 38th St, New York NY");

        Address expected = new Address("48 W 38th St, New York NY", "48 W 38th St", "New Jersey", "NJ");
        Address actual = parser.readPlace(reader);

        assertEquals(expected, actual);
    }

    @Test
    public void readPredictionsArrayTest() throws IOException {
        when(parser.readPredictionsArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, false);
        Address place = new Address("text", "street_line", "city", "state");
        when(parser.readPlace(reader)).thenReturn(place);
        List<Address> expected = Collections.singletonList(place);
        List<Address> actual = parser.readPredictionsArray(reader);
        assertEquals(expected, actual);
    }
}
