package com.convalida.user.jsonparsing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


//import org.ksoap2.SoapEnvelope;
//import org.ksoap2.serialization.SoapObject;
//import org.ksoap2.serialization.SoapPrimitive;
//import org.ksoap2.serialization.SoapSerializationEnvelope;
//import org.ksoap2.transport.AndroidHttpTransport;
//import org.ksoap2.transport.HttpTransportSE;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    String TAG="Response";
    String userData;
    String resultString;
    String getAppid;
    String contact;
    String getCustomerPhone;
    Boolean isConnectionExist;
    static boolean errored=false;
   //  ProgressBar webservicepg;
    boolean loginStatus;
    String appid="123456789";
    String mail="N";
    String sms="N";
    private CollapsingToolbarLayout collapsingToolbarLayout=null;
 static EditText etFirstName,etLastName,etMobile,etEmail,etAppId;
    String fname, lname,email;
    static String mobile;

   TextView testing;
    Button signUp;
    private TextInputLayout inputLayoutFirstName, inputLayoutLastdName, inputLayoutMobile, inputLayoutEmail;
    String loginurl="http://demo.oneplusrewards.com/app/api.asmx/UserDataByPhone?Appid=123456789&CustomerPhone=9675106217";
    private static final String REGISTRATION_URL="http://demo.oneplusrewards.com/app/api.asmx?op=RegisterUser?Appid=123456789";
    private static final String KEY_FNAME="FirstName";
    private static final String KEY_LNAME="LastName";
    private static final String KEY_EMAIL="Email";
    private static final String KEY_MOBILE="CustomerPhone";

   // AndroidHttpTransport androidHttpTransport;
    JSONObject jsonObject;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_first_name);
        inputLayoutLastdName = (TextInputLayout) findViewById(R.id.input_layout_last_name);
        inputLayoutMobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        etFirstName = (EditText) findViewById(R.id.input_first_name);
        etLastName = (EditText) findViewById(R.id.input_last_name);
        etMobile = (EditText) findViewById(R.id.input_mobile);
        etEmail = (EditText) findViewById(R.id.input_email);
        //  etAppId= (EditText) findViewById(R.id.input_appId);
     //   testing = (TextView) findViewById(R.id.test);
       AppBarLayout appBarLayout= (AppBarLayout) findViewById(R.id.appBarLayoutRegister);
        float heightDp= (float) (getResources().getDisplayMetrics().heightPixels/2.6);
        CoordinatorLayout.LayoutParams lp= (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        lp.height= (int) heightDp;

        signUp = (Button) findViewById(R.id.signUpBtn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
       // dynamicToolbarColor();
        toolbarTextAppearance();



        etFirstName.addTextChangedListener(new MyTextWatcher(etFirstName));
        etLastName.addTextChangedListener(new MyTextWatcher(etLastName));
        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));
        etMobile.addTextChangedListener(new MyTextWatcher(etMobile));
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.oneplusrewards7);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor=palette.getMutedColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
             //   int mutedDarkColor=palette.getDarkMutedColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));

                collapsingToolbarLayout.setContentScrimColor(mutedColor);
               // collapsingToolbarLayout.setStatusBarScrimColor(mutedDarkColor);

            }
        });

   /**     JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginurl, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e(TAG,"Inside onResponse");
                    jsonObject = response.getJSONObject("");
                    testing.setText("Hello: "+response.getString("FirstName"));
                    progressDialog.dismiss();
                } catch (JSONException e) {
                  //  e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Error! Try again", Toast.LENGTH_LONG).show();
                    //progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("tag", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error while loading...", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
        AppController.getInstance(this).addToRequestQueue(jsonObjectRequest);**/
    }
    boolean doubleBackToExitPressedOnce=false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed(){
        if(doubleBackToExitPressedOnce){
            //  moveTaskToBack(true);
            //  super.onBackPressed();
            finishAffinity();

            /**   Intent intent=new Intent(Intent.ACTION_MAIN);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             finish();
             startActivity(intent);**/

            return;
        }
        this.doubleBackToExitPressedOnce=true;
        Toast.makeText(this,"Press back again to exit application",Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                doubleBackToExitPressedOnce=false;
            }
        },2000);
    }

    private void dynamicToolbarColor() {
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.oneplusrewards7);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.color));
            }
        });
    }
    private void toolbarTextAppearance(){
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    private void submitForm() {
        if (!isNetworkAvailable()) {
            new AlertDialog.Builder(Register.this)
                    .setTitle("Internet connection is required")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = getIntent();
                            //  finish();
                            startActivity(intent);
                            // finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }
        else {
            if (!validateFirstName()) {
                return;
            }
            if (!validateLastName()) {
                return;
            }
            if (!validateEmail()) {
                return;
            }
            if (!validateMobile()) {
                return;
            }
            contact = etMobile.getText().toString();
            if (contact.length() < 10) {
                Toast.makeText(getApplicationContext(), "Please enter your complete contact no.", Toast.LENGTH_LONG).show();
                return;
            }

            //  AsyncGetData task=new AsyncGetData();
            //  task.execute();

            fname = etFirstName.getText().toString();
            lname = etLastName.getText().toString();
            mobile = etMobile.getText().toString();
            email = etEmail.getText().toString();

            //   if(fname.isEmpty() || lname.isEmpty() || mobile.isEmpty() || email.isEmpty()){
            //     Intent intent=new Intent(Register.this,MainActivity.class);
            //   startActivity(intent);
            // }
//else{
            /**   RequestParams params=new RequestParams();
             params.put("Appid",appid);
             params.put("CustomerPhone",mobile);
             params.put("EmailID",email);
             params.put("FirstName",fname);
             params.put("LastName",lname);
             params.put("Email",mail);
             params.put("SMS",sms);
             invokeWS(params);**/
            /**  String appid="123456789";
             String mail="N";
             String sms="N";
             new ExecuteTask().execute(appid,mobile,email,fname,lname,mail,sms);**/
            String url = "http://demo.oneplusrewards.com/app/api.asmx/RegisterUser";
            //   String url="http://localhost:10945/App/Api.asmx/RegisterUser";
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jObj = jsonArray.getJSONObject(0);
                        //  JSONObject jsonObject=new JSONObject(response);
                        String result = jObj.getString("result");
                        if (result.equals(String.valueOf(1))) {
                            Toast.makeText(getApplicationContext(), "Customer registered successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                        } else if (result.equals(String.valueOf(0))) {
                            Toast.makeText(getApplicationContext(), "Already registered. Please Login", Toast.LENGTH_LONG).show();
                            //   goLogin();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Register.this, "Some error occurred", Toast.LENGTH_LONG).show();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("Appid", appid);
                    parameters.put("CustomerPhone", mobile);
                    parameters.put("EmailID", email);
                    parameters.put("FirstName", fname);
                    parameters.put("LastName", lname);
                    parameters.put("Email", mail);
                    parameters.put("SMS", sms);
                    return parameters;
                }
            };
            RequestQueue rQueue = Volley.newRequestQueue(Register.this);
            rQueue.add(request);

            SavePreferences();
        }
      //  Intent intent=new Intent(Register.this,MainActivity.class);
        //startActivity(intent);



    }//}

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null;
    }


    private void SavePreferences() {
        SharedPreferences data=getApplicationContext().getSharedPreferences("saveNumber", MODE_PRIVATE);
        SharedPreferences.Editor editor=data.edit();
        editor.putString("Number",mobile);
        editor.commit();
    }

    //private void invokeWS(RequestParams params) {
      //  AsyncHttpClient client=new AsyncHttpClient();

       // public static void get(Context context, String url, )

       // client.get("http://demo.oneplusrewards.com/app/api.asmx/RegisterUser",params,new AsyncHttpResponseHandler(){

    //}


    private class MyTextWatcher implements TextWatcher {
        private View view;
        public MyTextWatcher(View view) {
            this.view=view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
        }
        public void afterTextChanged(Editable editable){
            switch (view.getId()){
                case R.id.input_first_name:
                    validateFirstName();
                    break;
                case R.id.input_last_name:
                    validateLastName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_mobile:
                    validateMobile();
                    break;


            }
        }
    }

  //  private void validateAppId() {
    //    getAppid=etAppId.getText().toString().trim();
    //}

    private boolean validateFirstName() {
        String firname=etFirstName.getText().toString().trim();
        if(firname.isEmpty() || !firname.matches("[a-zA-Z ]+")){
            inputLayoutFirstName.setError("Please enter your first name");
            requestFocus(etFirstName);
            return false;
        }
        else{
            inputLayoutFirstName.setErrorEnabled(false);
        }
    return true;
    }

    private void requestFocus(View view) {
        if(view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateLastName(){
        String lasname=etLastName.getText().toString().trim();
        if(lasname.isEmpty() || !lasname.matches("[a-zA-Z ]+")){
            inputLayoutLastdName.setError("Please enter your last name");
            requestFocus(etLastName);
            return false;
        }
        else{
            inputLayoutLastdName.setErrorEnabled(false);
        }
    return true;
    }
    private boolean validateEmail(){
        String emailId=etEmail.getText().toString().trim();
        if(emailId.isEmpty() || !isValidEmail(emailId)){
            inputLayoutEmail.setError("Please enter your valid email id");
            requestFocus(etEmail);
            return false;
        }
        else{
            inputLayoutEmail.setErrorEnabled(false);
        }
    return true;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateMobile(){
        contact=etMobile.getText().toString().trim();

        if(contact.isEmpty() || contact.length()>10 || !contact.matches("[0-9]+")){
            inputLayoutMobile.setError("Please enter your valid mobile no.");
            requestFocus(etMobile);
            return false;
        }
        else{
            inputLayoutMobile.setErrorEnabled(false);
        }
        return true;
    }

 /**   private class ExecuteTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            PostData(params);
            return null;
        }
    }

    private void PostData(String[] values) {


        // URL url=new URL("http://demo.oneplusrewards.com/app/api.asmx?op=RegisterUser");
        //HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        HttpClient httpClient=new DefaultHttpClient();
        HttpPost httpPost=new HttpPost("http://demo.oneplusrewards.com/app/api.asmx/RegisterUser");

        List<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("Appid",values[0]));
        list.add(new BasicNameValuePair("CustomerPhone",values[1]));
        list.add(new BasicNameValuePair("EmailID",values[2]));
        list.add(new BasicNameValuePair("FirstName",values[3]));
        list.add(new BasicNameValuePair("LastName",values[4]));
        list.add(new BasicNameValuePair("Email",values[5]));
        list.add(new BasicNameValuePair("SMS",values[6]));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            httpClient.execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // connection.setE

    }**/


}
