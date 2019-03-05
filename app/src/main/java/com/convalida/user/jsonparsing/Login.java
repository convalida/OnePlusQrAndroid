package com.convalida.user.jsonparsing;

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
import android.text.TextWatcher;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    static EditText etMobile;
    static String mobile;
    private TextInputLayout inputLayoutMobile;
    Button login, register;
    private static final String TAG = "Login.class";
    private CollapsingToolbarLayout collapsingToolbarLayout=null;
    // String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputLayoutMobile = (TextInputLayout) findViewById(R.id.layout_mobile);

        AppBarLayout appBarLayout= (AppBarLayout) findViewById(R.id.appBarLayout);
        float heightDp= (float) (getResources().getDisplayMetrics().heightPixels/2.6);
        CoordinatorLayout.LayoutParams lp= (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        lp.height= (int) heightDp;

        // else {
        etMobile = (EditText) findViewById(R.id.mobile);
        etMobile.addTextChangedListener(new MyTextWatcher(etMobile));
        mobile = etMobile.getText().toString();

        login = (Button) findViewById(R.id.loginBtn);
        register = (Button) findViewById(R.id.registerBtn);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_login);
        collapsingToolbarLayout.setTitle("");
      //  dynamicToolbarColor();
        toolbarTextAppearance();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateMobile()) {
                    return;
                }
                if (mobile.length() < 10) {
                    Toast.makeText(getApplicationContext(), "Please enter your complete contact no.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isNetworkAvailable()) {
                    new AlertDialog.Builder(Login.this)
                            .setMessage("Internet connection is required")
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
                    String url = "http://oneplusrewards.com/app/api.asmx/UserDataByPhone";
                    final String appid = "123456789";
                    //   String userId;

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        String userId, id;

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                /**       userId=jsonObject.getString("MID");
                                 Log.e(TAG,"user id is "+userId);
                                 SharedPreferences data=getApplicationContext().getSharedPreferences("saveUserId",MODE_PRIVATE);
                                 SharedPreferences.Editor editor=data.edit();
                                 editor.putString("userId",userId);
                                 editor.commit();
                                 **/
                                //  id=userId;
                                // SavePreferences();
                                String result = jsonObject.getString("result");
                                if (result.equals(String.valueOf(1))) {
                                    //     Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_LONG).show();
                                    userId = jsonObject.getString("MID");
                                    Log.e(TAG, "user id is " + userId);
                                    SharedPreferences data = getApplicationContext().getSharedPreferences("saveUserId", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = data.edit();
                                    editor.putString("userId", userId);
                                    editor.commit();
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                } else if (result.equals(String.valueOf(0))) {
                                    Toast.makeText(Login.this, "Customer not registered", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this, "Some error occurred", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            String mob = etMobile.getText().toString();
                            Map<String, String> paramters = new HashMap<String, String>();
                            paramters.put("Appid", appid);
                            paramters.put("CustomerPhone", mob);
                            return paramters;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                    requestQueue.add(request);
                    SavePreferences();
                }
            }
        });
    //}
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   mobile=etMobile.getText().toString();
                String mob = etMobile.getText().toString();
                Intent intent = new Intent(Login.this, Register.class);
                //  intent.putExtra("ContactNum",mob);
                startActivity(intent);
            }
        });

        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.oneplusrewards7);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
           int mutedColor=palette.getMutedColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
           collapsingToolbarLayout.setContentScrimColor(mutedColor);
            }
        });
    }

   /** private void dynamicToolbarColor(){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.edit);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
               collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
               collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.color));
            }
        });
    }**/

    private void toolbarTextAppearance() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }
//}

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null;
    }

    private boolean validateMobile() {
        mobile=etMobile.getText().toString().trim();
      //  Intent intent=new Intent(this, OnClickRestaurant.class);
        //intent.putExtra("Number",mobile);


        if(mobile.isEmpty() || mobile.length()>10 || !mobile.matches("[0-9]+")){
            inputLayoutMobile.setError("Please enter your valid mobile no.");
            requestFocus(etMobile);

            return false;
        }
        else{
            inputLayoutMobile.setErrorEnabled(false);
        }

        return true;

    }


    private void SavePreferences() {
        SharedPreferences data=getApplicationContext().getSharedPreferences("saveNumber", MODE_PRIVATE);
        SharedPreferences.Editor editor=data.edit();
        editor.putString("Number",mobile);
        //editor.putString("userId",userId);
        editor.commit();
    }

   /** public void onBackPressed(){
        moveTaskToBack(true);
    }**/
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

    private void requestFocus(View view) {
        if(view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

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
        public void afterTextChanged(Editable s) {
            switch (view.getId()){
                case R.id.mobile:
                    validateMobile();
                 //   SavePreferences();
                    break;
            }

        }
    }


}
