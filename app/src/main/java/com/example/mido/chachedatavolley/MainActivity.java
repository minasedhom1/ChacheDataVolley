package com.example.mido.chachedatavolley;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.textView);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.androidhive.info/json/movies.json";

      CacheRequest cacheRequest = new CacheRequest(Request.Method.GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                  //  JSONObject jsonObject = new JSONObject(jsonString);
                    textView.setText(jsonString.toString());
                    Toast.makeText(MainActivity.this, "onResponse:\n\n" + jsonString.toString(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  textView.setText( error.toString());

                Toast.makeText(context, "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(cacheRequest);

         /*
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                textView.setText(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

        // Add the request to the RequestQueue.
        if(queue.getCache().get(url)!=null){
            //response exists
            String cachedResponse = new String(queue.getCache().get(url).data);
            textView.setText(cachedResponse);

        }else {
            queue.add(request);
        }
*/

    }

  /* @Override
    protected void onPostResume() {
        super.onPostResume();
        onCreate(null);
    }*/
}
