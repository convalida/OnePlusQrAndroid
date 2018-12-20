package com.convalida.user.jsonparsing;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class OrderActivity extends AppCompatActivity {
    WebView webView;
    String order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        webView= (WebView) findViewById(R.id.orderWeb);
        Intent intent=getIntent();
        order=intent.getStringExtra("OrderLink");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
      //  webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if(savedInstanceState==null) {
            webView.loadUrl(order);
        }
    }

   /** public boolean onKeyDown(int keyCode, KeyEvent keyEvent){
        if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_BACK:
                if(webView.canGoBack()){
                    webView.goBack();
                }
                else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode,keyEvent);
    }**/
 /**  public void onBackPressed(){
       if(webView.canGoBack()){
           webView.goBack();
       }
       else super.onBackPressed();
   }**/
 boolean doubleBackToExitPressedOnce=false;
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(event.getAction()==KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_BACK:
                    if(doubleBackToExitPressedOnce){
                        if(webView.canGoBack()){
                            webView.goBack();
                        }
                        else {
                            super.onBackPressed();
                            // finish();
                        }
                        return true;
                        //  break;
                        //moveTaskToBack(true);
                        //break;
                        //    super.onBackPressed();
                        //  break;
                        //return;

                    }
                    this.doubleBackToExitPressedOnce=true;
                    Toast.makeText(getApplicationContext(),"Press back again to go back",Toast.LENGTH_SHORT).show();
                    //   doubleBackToExitPressedOnce=false;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce=false;

                        }
                    },2000);

                    /**  if(webView.canGoBack()){
                     webView.goBack();
                     //return true;

                     }
                     else {
                     finish();
                     //  super.onBackPressed();
                     }**/
                    return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }


    /**   protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }**/


    private class MyBrowser extends WebViewClient {
    }
}
