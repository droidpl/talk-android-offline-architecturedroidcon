package com.mobgen.droidcon.offline.shared.utils;

import android.util.Log;

import com.mobgen.droidcon.offline.BuildConfig;

import java.io.IOException;

import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Logs the requests and the responses produced.
 */
public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = interceptRequest(request, chain.connection());
        Response response = chain.proceed(request);
        response = interceptResponse(request, response);
        return response;
    }

    /**
     * Intercepts the request and logs the url, headers and method type.
     *
     * @param request    The request to process.
     * @param connection The connection object.
     * @return The same request.
     */
    public Request interceptRequest(Request request, Connection connection) {
        //Log only if the printer is on it to avoid wasting time
        if (BuildConfig.DEBUG) {
            Log.d("Request", String.format("%s: %s \n%s", request.method(), request.url(), request.headers().toString()));
        }
        return request;
    }

    /**
     * Intercepts te
     *
     * @param request  The request intercepted.
     * @param response The response intercepted.
     * @return The response.
     */
    public Response interceptResponse(Request request, Response response) {
        //It does not print to avoid wasting time on the object creation
        Response finalResponse = response;
        if (BuildConfig.DEBUG) {
            Log.d("Response", String.format("%s \n%s", request.url(), request.headers().toString()));
            //Log the response
            try {
                ResponseBody responseBody = response.body();
                String responseBodyString = responseBody.string();
                Log.d("Response data", responseBodyString);
                finalResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString)).build();
            } catch (IOException e) {
                Log.e("Response", "Error while parsing the response.");
            }
        }
        return finalResponse;
    }
}