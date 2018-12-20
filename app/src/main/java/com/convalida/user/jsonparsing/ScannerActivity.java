package com.convalida.user.jsonparsing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    String scannedResult;

    ZXingScannerView zXingScannerView;
    public static final String TAG="ScannerActivity";
    String businessId, points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sacnner);
        zXingScannerView=new ZXingScannerView(this);
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }
    public void onPause(){
        super.onPause();
        zXingScannerView.stopCamera();

    }

    @Override
    public void handleResult(Result result) {
        Log.e(TAG,result.getText());
        Log.e(TAG, result.getBarcodeFormat().toString());
        scannedResult=result.getText();
      /**  AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("You have earned "+points+" points");
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
   //   registerPoints();**/
        String[] ptsBsnid=scannedResult.split("/");
        businessId=ptsBsnid[0];
        points=ptsBsnid[1];
        new GetUserId().execute();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("You have earned "+points+" points");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent inte=new Intent(ScannerActivity.this,MainActivity.class);
                startActivity(inte);
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

      //  zXingScannerView.resumeCameraPreview(this);**/

        //later on make a db entry and on another screen, show congratulations and that u've earned this much points
    }

    private void registerPoints(final String userId) {
        String url="http://demo.oneplusrewards.com/app/api.asmx/AddRewardByApp";
        final StringRequest request= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String result = jsonObject.getString("result");
                    if (result.equals(String.valueOf(1))) {
                        Log.e(TAG, "Points added");
                    } else {
                        Log.e(TAG, "Some error in result value");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"Some error occured",Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> parameters=new HashMap<>();
                parameters.put("Appid","123456789");
                parameters.put("MID",userId);
                parameters.put("rewards",points);
                parameters.put("businessId",businessId);
                Log.e(TAG,"Parameters are "+parameters.toString());
                return parameters;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(ScannerActivity.this);
        rQueue.add(request);

    }

    public void onBackPressed(){
        Intent inte=new Intent(ScannerActivity.this,MainActivity.class);
        startActivity(inte);
    }

    private class GetUserId extends AsyncTask<Void,Void,String>{
        String stringNum,url,userId;

        protected void onPreExecute(){
            super.onPreExecute();
            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("saveNumber",MODE_PRIVATE);
            stringNum=sharedPreferences.getString("Number","");
            Log.e(TAG,"Contact no. user is "+stringNum);
            url="http://demo.oneplusrewards.com/app/api.asmx/UserDataByPhone?Appid=123456789&CustomerPhone="+stringNum;
        }
        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler httpHandler=new HttpHandler();
            String json=httpHandler.makeServiceCall(url);
            Log.e(TAG,"Response from url: "+json);
            if(json!=null){
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    userId=jsonObject.getString("MID");
                    Log.e(TAG,"User id inside other method in background thread is "+userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return userId;
        }
        public void onPostExecute(String userId){
            super.onPostExecute(userId);
            if(userId!=null){
                Log.e(TAG,"User id inside other method in onpostexecute is "+userId);
                registerPoints(userId);
            }
        }
    }
}
