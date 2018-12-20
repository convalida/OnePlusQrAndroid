package com.convalida.user.jsonparsing;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by User on 6/23/2017.
 */

public class HttpHandler {
    private static final String TAG=HttpHandler.class.getSimpleName();

    public HttpHandler()
    {
    }

    public String makeServiceCall(String reqUrl){
        String response=null;
        try{
            URL url=new URL(reqUrl);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
          //  connection.getResponseCode();
     //       connection.getErrorStream();
          //  int responseCode=connection.getResponseCode();
          //  if(responseCode>=400 && responseCode<=499){
            //    throw new Exception("Bad authentication status: "+responseCode);
            //}
            //else {
            InputStream inputStream= new BufferedInputStream(connection.getInputStream());
            response=convertStreamToString(inputStream);}
        //}


         catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG,"Error: "+e.getMessage());
            e.printStackTrace();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String convertStreamToString(InputStream inputStream) {
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb=new StringBuilder();
        String line;
        try {
            while ((line=reader.readLine())!=null){
                sb.append(line).append('\n');
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    }
