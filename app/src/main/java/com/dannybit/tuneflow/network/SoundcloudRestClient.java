package com.dannybit.tuneflow.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by danielnamdar on 7/1/15.
 */
public class SoundcloudRestClient {

    private static final String BASE_URL = "http://api.soundcloud.com/tracks";
    public static final String CLIENT_ID = "e4ed2e5cae039181a659fc2812f5a8b0";

    private static AsyncHttpClient client = new AsyncHttpClient();



    public static void get(RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(), params, responseHandler);
    }

    public static void post(String id, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.post(getAbsoluteUrl(), params, responseHandler);
    }


    private static String getAbsoluteUrl(){
        return BASE_URL;
    }



}
