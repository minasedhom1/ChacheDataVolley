package com.example.mido.chachedatavolley;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Created by mido on 3/8/2017.
 */

public class CacheRequest extends Request<NetworkResponse> {
    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;

    public CacheRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }


    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response); //first try to cache it from server
        if (cacheEntry == null) {   //if no-cache!!
            cacheEntry = new Cache.Entry();  //then make a new chache and get data manually
        }
        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
        final long cacheExpired =10000; //24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
        long now = System.currentTimeMillis();  //add my cache attributes
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;
        cacheEntry.data = response.data;  //here to pass response data to chache
        cacheEntry.softTtl = softExpire;
        cacheEntry.ttl = ttl;
        String headerValue;
        headerValue = response.headers.get("Date");
        if (headerValue != null) {
            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        headerValue = response.headers.get("Last-Modified");
        if (headerValue != null) {
            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        cacheEntry.responseHeaders = response.headers;
        return Response.success(response, cacheEntry);   //hna b return el response//cached data
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }
}
