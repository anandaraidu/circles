package com.example.muralic.circlesmay22;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
 * Created by Muralic on 5/25/15.
 */
public class HttpClientService  extends IntentService  {

    HttpClientService() {
        super("HttpClientService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            Log.d("UserLocationService", "onHandleIntent");
            final String action = intent.getAction();
            if (Constants.HTTP_GET_REQ.equals(action)) {
                handleGetReq(intent);
            } else if (Constants.HTTP_POST_REQ.equals(action)) {
                handlePostReq(intent);
            }
        }// Implement HTTP client service as part of this.
    }

    public void handlePostReq(Intent intent) {
        String Url = intent.getStringExtra(Constants.HTTP_URL);
        String Data1 = intent.getStringExtra(Constants.EXTENDED_DATA1);
        String Data2 = intent.getStringExtra(Constants.EXTENDED_DATA2);

        Log.d("Data1:", Data1);
        Log.d("Data2:", Data2);


        DefaultHttpClient lClient = new DefaultHttpClient();
        HttpPost lHttpPost = new HttpPost(Url);

        // 3. build jsonObject
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("name", Data1);
            jsonObject.accumulate("country", Data2);
            jsonObject.accumulate("twitter", Data2);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("HttpClientService:", "JSONException");
            return;
        }
        // 4. convert JSONObject to JSON to String
        json = jsonObject.toString();

        // 5. set json to StringEntity
        StringEntity se;
        try {
            se = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("HttpClientService", "UnsupportedEncoding Exception");
            return;
        }
        // 6. set httpPost Entity
        lHttpPost.setEntity(se);

        // 7. Set some headers to inform server about the type of the content
        lHttpPost.setHeader("Accept", "application/json");
        lHttpPost.setHeader("Content-type", "application/json");

        try {
            HttpResponse lResponse = lClient.execute(lHttpPost);
            String lResponseStr = inputStreamToString(lResponse.getEntity().getContent()).toString();

            if (lResponseStr != null) {
                sendResultBroadcast(lResponseStr);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase
    {

        public HttpGetWithEntity() {
            super();
        }

        public HttpGetWithEntity(URI uri) {
            super();
            setURI(uri);
        }

        public HttpGetWithEntity(String uri) {
            super();
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return HttpGet.METHOD_NAME;
        }
    }

    public void handleGetReq(Intent intent) {
        String Url = intent.getStringExtra(Constants.HTTP_URL);
        String Data1 = intent.getStringExtra(Constants.EXTENDED_DATA1);
        String Data2 = intent.getStringExtra(Constants.EXTENDED_DATA2);

        Log.d("Data1:", Data1);
        Log.d("Data2:", Data2);

        DefaultHttpClient lClient = new DefaultHttpClient();
        HttpGetWithEntity lHttpGet = new HttpGetWithEntity(Url);
        String json = "";
        // 3. build jsonObject
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("name", Data1);
            jsonObject.accumulate("country", Data2);
            jsonObject.accumulate("twitter", Data2);
        } catch (JSONException e) {
            Log.d("HttpClientService:", "JSON Exception");
            e.printStackTrace();
            return;
        }
        // 4. convert JSONObject to JSON to String
        json = jsonObject.toString();

        // 5. set json to StringEntity
        StringEntity se;
        try {
            se = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            Log.d("HttpClientService:", "UnsupportedEncodingException");
            e.printStackTrace();
            return;
        }
        // 6. set httpPost Entity
        lHttpGet.setEntity(se);

        // 7. Set some headers to inform server about the type of the content
        lHttpGet.setHeader("Accept", "application/json");
        lHttpGet.setHeader("Content-type", "application/json");


        try {
            HttpResponse lResponse = lClient.execute(lHttpGet);
            String lResponseStr = inputStreamToString(lResponse.getEntity().getContent()).toString();

            if (lResponseStr != null) {
                sendResultBroadcast(lResponseStr);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader rd = new BufferedReader(isr);

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    private void sendResultBroadcast(String lResponse) {

        Intent in = new Intent();
        in.putExtra("jsonresult", lResponse);
        in.setAction(Constants.HTTP_RESP);
        sendBroadcast(in);
    }


    public final class Constants {
        public static final String  HTTP_RESP =
                "com.example.muralic.circlesmay22.HTTPRESP";

        // Defines a custom Intent action
        public static final String HTTP_GET_REQ =
                "com.example.muralic.circlesmay22.HTTPGETREQ";

        public static final String HTTP_POST_REQ =
                "com.example.muralic.circlesmay22.HTTPPUTREQ";

        public static final String HTTP_URL =
                "com.example.muralic.circlesmay22.HTTP_URL";

        // Defines the key for the status "extra" in an Intent
        public static final String EXTENDED_DATA1 =
                "com.example.muralic.circlesmay22.Data1";

        // Defines the key for the status "extra" in an Intent
        public static final String EXTENDED_DATA2 =
                "com.example.muralic.circlesmay22.Data2";
    }
}
