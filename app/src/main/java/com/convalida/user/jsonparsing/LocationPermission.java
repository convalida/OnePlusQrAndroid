package com.convalida.user.jsonparsing;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LocationPermission extends AppCompatActivity {
    Button locationBtn;
    private static final int REQUEST_APP_SETTINGS=168;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_permission);
        locationBtn= (Button) findViewById(R.id.enableLocationBtn);
    }

    public void enableLocation(View v){
        Intent in=new Intent();
        in.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri=Uri.fromParts("package",getPackageName(),null);
        in.setData(uri);
    //    in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
      //  in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(in,3);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==3){
          //  Uri contentUri=data.getData();
      //      if(resultCode==RESULT_OK) {
                Intent intent = new Intent(LocationPermission.this, MainActivity.class);
                startActivity(intent);
            //startActivity(new Intent(this,MainActivity.class).setData(contentUri));
            }//}
    }
    public void onBackPressed(){
      //  super.onBackPressed();
     //   moveTaskToBack(true);
   // finish();
       // return;
       /** Intent a=new Intent(Intent.ACTION_MAIN);
        //   a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(a);**/
       moveTaskToBack(true);
       //return;
       // finish();

    }
}
