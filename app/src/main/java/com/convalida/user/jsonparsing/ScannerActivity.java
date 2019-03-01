package com.convalida.user.jsonparsing;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    private static final int MY_PERMISSION_CAMERA = 98;

    ZXingScannerView zXingScannerView;
    public static final String TAG="ScannerActivity";
    String businessId, points,loginPin,key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sacnner);
//        checkCameraPermission();
        zXingScannerView=new ZXingScannerView(this);
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

/**    private boolean checkCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }

            return false;
        }
        else {
            Intent intent=new Intent(ScannerActivity.this,ScannerActivity.class);
            startActivity(intent);
            return true;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case MY_PERMISSION_CAMERA:
                for(int i=0, len=permissions.length; i<len; i++){
                    String permission=permissions[i];
                    if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                    boolean showRationale=shouldShowRequestPermissionRationale(permission);
                    if(!showRationale){
                        Intent intent=new Intent(ScannerActivity.this,CameraPermission.class);
                        startActivity(intent);
                    }
                }
              /**  else if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                   // Intent intent=getIntent();
                        Intent intent=new Intent(ScannerActivity.this,ScannerActivity.class);
                    startActivity(intent);
                    }**/
            /**  else {
                        startActivity(getIntent());
                    }**/
       /**     else{
                Intent intent=new Intent(ScannerActivity.this,ScannerActivity.class);
                startActivity(intent);
                    }
            /**  else{
                  finish();
                  startActivity(getIntent());
                    }**/
          /**      }
        }
    }**/

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
        loginPin=ptsBsnid[2];
        key=ptsBsnid[3];
        if(!isNetworkAvailable()){
            new AlertDialog.Builder(ScannerActivity.this)
                    .setMessage("Internet connection is required")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=getIntent();
                            startActivity(intent);
                        }
                    })
                    .setCancelable(false)
                    .create().show();
        }
        else {
            new GetUserId().execute();
        }
      //  zXingScannerView.resumeCameraPreview(this);**/

        //later on make a db entry and on another screen, show congratulations and that u've earned this much points
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null;
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
                        customAlert(points);
                      /**  AlertDialog.Builder builder=new AlertDialog.Builder(ScannerActivity.this);
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
                        alertDialog.show();**/

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
                parameters.put("loginpin",loginPin);
                parameters.put("Key",key);
                Log.e(TAG,"Parameters are "+parameters.toString());
                return parameters;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(ScannerActivity.this);
        rQueue.add(request);

    }

    public void customAlert(String points) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View dialogLayout=inflater.inflate(R.layout.points_dialog,null);
        Button btn=dialogLayout.findViewById(R.id.okBtn);
        TextView pointsText=dialogLayout.findViewById(R.id.pointsText);
        TextView point=dialogLayout.findViewById(R.id.points);
        pointsText.setText(points);
        int pointsValue=Integer.parseInt(points);
        if(pointsValue==1){
           point.setText("Point");
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ScannerActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
       // builder.setPositiveButton("Ok",null);
        builder.setView(dialogLayout);
        builder.setCancelable(false);
        builder.show();
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
