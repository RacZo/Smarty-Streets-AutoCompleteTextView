package com.oscarsalguero.smartystreetsautocomplete.model;

import java.io.IOException;

public class ApiException extends IOException {

    public ApiException() {
    }

    public ApiException(final String detailMessage) {
        super(detailMessage);
    }

    public ApiException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ApiException(final Throwable throwable) {
        super(throwable);
    }
}
