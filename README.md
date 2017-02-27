# Smarty Streets AutoCompleteTextView

[![Download](https://api.bintray.com/packages/raczo/maven/smartystreetsautocomplete/images/download.svg?version=1.0.0) ](https://bintray.com/raczo/maven/smartystreetsautocomplete/1.0.0/link) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SmartyStreets%20AutocompleteTextView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5338) [![Release](https://img.shields.io/badge/maven--central-v1.0.0-green.svg?style=flat-square)](http://mvnrepository.com/artifact/com.oscarsalguero/smartystreetsautocomplete/1.0.0)

Gradle library. Android AutoCompleteTextView that receives and displays US address suggestions from SmartyStreets's [US Autocomplete API](https://smartystreets.com/docs/cloud/us-autocomplete-api "US Autocomplete API").

[SmartyStreet's Android SDK](https://smartystreets.com/docs/sdk/android "SmartyStreet's Android SDK") does not offer address suggestions, it only offers address verification and zip code lookup.


## Demo

Yes, there is a demo app!

<img src="https://github.com/RacZo/SmartyStreets-AutoComplete-Demo/blob/master/media/demo.gif" width=300>

Demo app repository: https://github.com/RacZo/SmartyStreets-AutoComplete-Demo


## Dependencies

If your app is already using the following SUPER AWESOME libraries:

1. Google's Gson version 2.8.0 https://github.com/google/gson
2. Square's okhttp version 3.6.0 https://github.com/square/okhttp
3. Square's okhttp-logging-interceptor version 3.6.0 https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor

you are set! If not, add the following to your app's build.gradle:

```groovy
compile 'com.google.code.gson:gson:2.8.'
compile 'com.squareup.okhttp3:okhttp:3.6.0'
compile 'com.squareup.okhttp3:logging-interceptor:3.6.0'
```


## Installation

Via Gradle, by putting the following in your build.gradle:

```groovy
compile 'com.oscarsalguero:smartystreetsautocomplete:1.0.0'
```

Or Maven:

```xml
<dependency>
  <groupId>com.oscarsalguero</groupId>
  <artifactId>smartystreetsautocomplete</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

## Usage

1. Sign up for an account on https://smartystreets.com and create a Website key (API key).

2. Do not forget to add  ```<uses-permission android:name="android.permission.INTERNET" />``` to your app's Manifest.xml.

3. Create an adapter class called "AddressAutocompleteAdapter" and make it extend "AbstractAddressAutocompleteAdapter", implement the required methods and define a layout for the dropdown item (R.layout.dropdown_text_item).

4. You will need your API key and a "referer" (host you entered when you created your key). The referer in your app must have "https://" as prefix or the API won't work.
    ```xml
    <com.oscarsalguero.smartystreetsautocomplete.SmartyStreetsAutocompleteTextView
        android:id="@+id/autocomplete_text_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:completionHint="Number Street, City State"
        android:completionThreshold="1"
        android:hint="Address"
        android:maxLength="100"
        app:ssacv_adapterClass=".adapter.AddressAutocompleteAdapter"
        app:ssacv_referer="YOUR_REFERER"
        app:ssacv_suggestions="5"
        app:ssacv_webApiKey="YOUR_WEBSITE_KEY" />
    ```

5. To capture the address selection (when the user taps a suggestion) you'll use the provided listener:

    ```java
    autoCompleteTextView.setOnAddressSelectedListener(new OnAddressSelectedListener() {
       @Override
       public void onAddressSelected(@NonNull Address address) {
       }
    });
    ```

And... voilà!


### Contributing

1. Fork this repo and clone your fork. Create an empty file called "bintray.gradle" in the root of your project.
2. Make your desired changes
3. Add tests for your new feature and ensure all tests are passing
4. Commit and push
5. Submit a Pull Request through Github's interface and I'll review your changes to see if they make it to the next release.


### Issues

Use this repo's github issues.


### Based On

SeatGeek's autocomplete library for Google Places https://github.com/seatgeek/android-PlacesAutocompleteTextView and the fact SmartyStreet's Android SDK does not offer address suggestions.


License
=======

    Copyright 2017 Oscar Salguero

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
