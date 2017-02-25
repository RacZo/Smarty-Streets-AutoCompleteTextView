package com.oscarsalguero.smartystreetsautocomplete.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Smarty Streets Address
 * Created by oscar on 2/14/17.
 */
public class Address implements Serializable {

    private String text;

    @SerializedName("street_line")
    private String streetLine;

    private String city;

    private String state;

    public Address(String text, String streetLine, String city, String state) {
        this.text = text;
        this.streetLine = streetLine;
        this.city = city;
        this.state = state;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStreetLine() {
        return streetLine;
    }

    public void setStreetLine(String streetLine) {
        this.streetLine = streetLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        if (text != null ? !text.equals(address.text) : address.text != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        return result;
    }

}
