package com.application.alphacapital.superapp.acpital.classes;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class JSONParser implements Runnable {

    static InputStream isInputStream = null;
    static JSONObject jObj = null;
    static String json = "";
    static String url=null;

    // constructor
    public JSONParser() {

    }
    public String getJSON(String url){

        JSONParser.url = url;

        Runnable r=new JSONParser();
        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // return JSON String
        return json;

    }


    @Override
    public void run() {
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            System.out.println("URL in Run Method" + JSONParser.url);
            HttpGet httpPost = new HttpGet(JSONParser.url);
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 60000); // Timeout
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            isInputStream = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    isInputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            isInputStream.close();
            json = sb.toString();
            System.out.println("This is Json from url" + json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

    }
}
