package com.convalida.user.jsonparsing;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class EditProfile extends AppCompatActivity {
private Toolbar toolbar;
    private EditText etfname,etlname,etmobileNum,etEmail;
    private TextInputLayout inputfname,inputlname,inputmobile,inputemail;
    Button updateInfoBtn,qrCode,scanQr;
    String str;
    private static final String TAG="EditProfile";
    String fname,lname,email,mobile;
    String mail, namel, namef,mob;
    ProgressDialog progressDialog;
    private static String editProfileUrl="http://localhost:10945/App/api2x.asmx/EditProfile";
    private CollapsingToolbarLayout collapsingToolbar=null;
    private ZXingScannerView scannerView;
    Context context;
    public static final int MY_PERMISSION_CAMERA=98;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        AppBarLayout appBarLayout= (AppBarLayout) findViewById(R.id.appBarLayout);
        float heightDp= (float) (getResources().getDisplayMetrics().heightPixels/2.6);
        CoordinatorLayout.LayoutParams lp= (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        lp.height= (int) heightDp;

        inputfname= (TextInputLayout) findViewById(R.id.fnameLayout);
        inputlname= (TextInputLayout) findViewById(R.id.lnameLayout);
        inputmobile= (TextInputLayout) findViewById(R.id.mobileLayout);
        inputmobile.setEnabled(false);
        inputemail= (TextInputLayout) findViewById(R.id.emailLayout);
        etfname= (EditText) findViewById(R.id.fnameInput);
        etlname= (EditText) findViewById(R.id.lnameInput);
        etmobileNum= (EditText) findViewById(R.id.mobileInput);
        etEmail= (EditText) findViewById(R.id.emailInput);
        updateInfoBtn= (Button) findViewById(R.id.editBtn);
      //  qrCode= (Button) findViewById(R.id.qrBtn);
       // scanQr= (Button) findViewById(R.id.qrScanBtn);
        etfname.requestFocus();

         SharedPreferences data=getApplicationContext().getSharedPreferences("saveNumber",MODE_PRIVATE);
         str=data.getString("Number","");
         etmobileNum.setText(str);
         if(!isNetworkAvailable()){
             new AlertDialog.Builder(EditProfile.this)
                     .setMessage("Internet connection required")
                     .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             Intent intent=getIntent();
                             startActivity(intent);
                         }
                     })
                     .setCancelable(false)
                     .create()
                     .show();

         }
         else {
             new GetCustomerDetails().execute();
         }

     //   toolbar= (Toolbar) findViewById(R.id.toolbarEditProfile);
       // setSupportActionBar(toolbar);
        // getSupportActionBar().setTitle("Edit profile");
        collapsingToolbar= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_edit);
        collapsingToolbar.setTitle("Edit Profile");
        etfname.setText(namef);
        etlname.setText(namel);
        etEmail.setText(mail);
      //  etmobileNum.setText(mob);

       // etfname.setText("This is just a test");
        etfname.addTextChangedListener(new MyTextWatcher(etfname));
        etlname.addTextChangedListener(new MyTextWatcher(etlname));
       // etmobileNum.addTextChangedListener(new MyTextWatcher(etmobileNum));
        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));
        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo();
            }
        });
      /**  qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQR();
            }
        });**/
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

   //     scannerView=new ZXingScannerView(this);

        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.oneplusrewards7);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor=palette.getMutedColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                collapsingToolbar.setContentScrimColor(mutedColor);
            }
        });

    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent intent=new Intent(EditProfile.this,MainActivity.class);
        startActivity(intent);
    }

    private void editInfo() {
        String str;
        if (!validateFirstName()) {
            return;
        }
        if (!validateLastName()) {
            return;
        }
        // if(!validateMobile()){
        //   return;
        //}
        if (!validateEmail()) {
            return;
        }
        fname = etfname.getText().toString();
        lname = etlname.getText().toString();
        email = etEmail.getText().toString();

        if (!isNetworkAvailable()) {
            new AlertDialog.Builder(EditProfile.this)
                    .setMessage("Internet connection required")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=getIntent();
                            startActivity(intent);
                        }
                    })
            .setCancelable(false)
            .create()
            .show();

        } else {
            //  mob=etmobileNum.getText().toString();
            // String urlUpdate="http://demo.oneplusrewards.com/app/api.asmx/EditProfile?CustomerPhone="+etmobileNum.getText().toString();
            String urlUpdate = "http://oneplusrewards.com/app/api.asmx/EditProfile";

            StringRequest request = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.getString("result");
                        if (result.equals(String.valueOf(1))) {
                            Toast.makeText(getApplicationContext(), "Customer details updated successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(EditProfile.this, MainActivity.class);
                            startActivity(intent);
                        }
                        //   else if(result.equals(String.valueOf(0))){
                        //     Toast.makeText(getApplicationContext(),"No customer with given contact exist",Toast.LENGTH_LONG).show();
                        //}
                        else if (result.equals(String.valueOf(2))) {
                            Toast.makeText(getApplicationContext(), "Invalid app id", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditProfile.this, "Some error occured", Toast.LENGTH_LONG).show();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("Appid", "123456789");
                    parameters.put("FirstName", etfname.getText().toString());
                    parameters.put("LastName", etlname.getText().toString());
                    parameters.put("CustomerPhone", etmobileNum.getText().toString());
                    parameters.put("EmailID", etEmail.getText().toString());
                    return parameters;
                }
            };
            RequestQueue rQueue = Volley.newRequestQueue(EditProfile.this);
            rQueue.add(request);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null;
    }

    private boolean validateEmail() {
        String email=etEmail.getText().toString().trim();
        if(!isValidEmail(email)){
            inputemail.setError("Please enter your valid email id");
            requestFocus(etEmail);
            return false;
        }
        else
            inputemail.setErrorEnabled(false);
        return true;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

/**    private boolean validateMobile() {
        String mobile=etmobileNum.getText().toString().trim();
        if(mobile.isEmpty() || mobile.length()!=10 || !mobile.matches("[0-9]+")){
            inputmobile.setError("Please enter your valid contact no.");
            requestFocus(etmobileNum);
            return false;
        }
        else
            inputmobile.setErrorEnabled(false);
        return true;
    }**/

    private boolean validateLastName() {
        String lasname=etlname.getText().toString().trim();
        if(lasname.isEmpty() || !lasname.matches("[a-zA-Z]+")){
            inputlname.setError("Please enter your last name");
            requestFocus(etlname);
            return false;
        }
        else{
            inputlname.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateFirstName() {
        String firname=etfname.getText().toString().trim();
      if(firname.isEmpty()|| !firname.matches("[a-zA-Z]+")){
          inputfname.setError("Please enter your first name");
          requestFocus(etfname);
          return false;
      }
      else
          inputfname.setErrorEnabled(false);
        return true;
    }

    private void requestFocus(View view) {
        if(view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

  /**  @Override
    public void handleResult(Result result) {
        Log.e(TAG,result.getText());
        Log.e(TAG, result.getBarcodeFormat().toString());
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(result.getText());
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }**/

    private class MyTextWatcher implements TextWatcher {
        private View view;
        public MyTextWatcher(View view) {
            this.view=view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()){
                case R.id.fnameInput:
                    validateFirstName();
                    break;
                case R.id.lnameInput:
                    validateLastName();
                    break;
              //  case R.id.mobileInput:
                //    validateMobile();
                  //  break;
                case R.id.emailInput:
                    validateEmail();
                    break;

            }

        }
    }


    private class GetCustomerDetails extends AsyncTask<Void, Void, Void> {
        String url="http://oneplusrewards.com/app/api.asmx/UserDataByPhone?Appid=123456789&CustomerPhone="+str;
        String firName,lasName,mail,mobile;

        protected void onPreExecute(){
            super.onPreExecute();
            Log.e(TAG,"Contact value "+str);
            Log.e(TAG,"Url value: "+url);
        }
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler handler=new HttpHandler();
            String json=handler.makeServiceCall(url);
            Log.e(TAG,"Response from url "+json);
            if(json!=null){
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    firName=jsonObject.getString("FirstName");
                    lasName=jsonObject.getString("LastName");
                    mail=jsonObject.getString("EmailID");
               //     mobile=jsonObject.getString("CustomerPhone");

                    HashMap<String,String> map=new HashMap<>();
                    map.put("FirstName",firName);
                    map.put("LastName",lasName);
                    map.put("EmailID",mail);
                 //   map.put("CustomerPhone",mobile);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            etfname.setText(firName);
            etlname.setText(lasName);
            etEmail.setText(mail);
          //  etmobileNum.setText(mobile);
        }
    }
}
