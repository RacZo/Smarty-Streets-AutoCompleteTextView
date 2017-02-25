package com.oscarsalguero.smartystreetsautocomplete.async;

/**
 * Background job
 *
 * @param <R>
 */
public interface BackgroundJob<R> {

    R executeInBackground() throws Exception;

    void onSuccess(R result);

    void onFailure(Throwable error);

}
