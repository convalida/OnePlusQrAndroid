package com.convalida.user.jsonparsing;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CameraPermission extends AppCompatActivity {
    Button cameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_permission);
        cameraBtn= (Button) findViewById(R.id.enableCameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent();
                in.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri=Uri.fromParts("package",getPackageName(),null);
                in.setData(uri);
                startActivityForResult(in,5);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==5){
           // if(Manifest.permission.CAMERA.equals(PackageManager.PERMISSION_GRANTED)) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(CameraPermission.this, ScannerActivity.class);
                startActivity(intent);
            }
            else{
                Intent intent=new Intent(CameraPermission.this,CameraPermission.class);
                startActivity(intent);
            }
           // EditProfile.
            //call launchScanner EditProfile
        }
    }
   // public void onBackPressed(){
      //  moveTaskToBack(true);
    //}

}
