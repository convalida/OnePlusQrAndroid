package com.convalida.user.jsonparsing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=2000;
    String str;
    private static final String TAG="SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                SharedPreferences data=getApplicationContext().getSharedPreferences("saveUserId",MODE_PRIVATE);
                str=data.getString("userId","");
                Log.e(TAG,"User id is "+str);
                if(str.equals("")){
                    Intent i=new Intent(SplashScreen.this,Login.class);
                    startActivity(i);
                }
                else{
                    Intent i=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(i);
                }

              /**  SharedPreferences data=getApplicationContext().getSharedPreferences("saveNumber",MODE_PRIVATE);
                str=data.getString("Number","");
                Log.e(TAG,"Contact no. is "+str);
                if(str.equals("")){
                Intent i=new Intent(SplashScreen.this,Login.class);
                startActivity(i);
            }
                else{
                    Intent i=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(i);
                }**/
             //   Intent intent=new Intent(SplashScreen.this,Login.class);
               // startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
