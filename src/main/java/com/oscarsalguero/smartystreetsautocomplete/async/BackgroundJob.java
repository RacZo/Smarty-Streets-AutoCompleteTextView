package com.oscarsalguero.smartystreetsautocomplete.async;

public interface BackgroundJob<R> {

    R executeInBackground() throws Exception;

    void onSuccess(R result);

    void onFailure(Throwable error);

}
