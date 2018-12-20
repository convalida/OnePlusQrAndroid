package com.convalida.user.jsonparsing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class SelectRange extends AppCompatActivity {
    private static final String TAG="SelectRange";
    Button okBtn;
    static int seekProgress;//=3950;
    TextView displayProgress;
    static int seekBarProgess=50;
    static double seekBProgress=50.00;
    static float seekBaProgress=50.0f;
    float progressFloat;
    SharedPreferences sharedPreferences;
    public static final String mypref="mypref";
    public static final String progress="progressKey";
    public static final String progressText="progressText";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_range);

        final SeekBar seekBar= (SeekBar) findViewById(R.id.seekbar);
        displayProgress= (TextView) findViewById(R.id.progressText);
        //final SharedPreferences data=getApplicationContext().getSharedPreferences("saveMiles",MODE_PRIVATE);
        //progressFloat=data.getFloat(progress,0.0f);
        //if(progressFloat==0.0f) {
          //  seekBar.setProgress(seekBarProgess);
        //}
        //else{

       // }
     //  seekBar.setProgress((int) seekBaProgress);
        displayProgress.setText(""+seekBarProgess+" Miles");
       seekBar.setProgress((int) seekBarProgess);
      //seekBar.getProgress();
        //  seekBar.setProgress((int) seekBProgress);
        //seekBar.setProgress(20);
        seekBar.setMax(100);
        sharedPreferences=getSharedPreferences(mypref, Context.MODE_PRIVATE);

     final int limit=100;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         //   seekBarProgess=0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
             //   Toast.makeText(getApplicationContext(),progress+"",Toast.LENGTH_SHORT).show();
           //     int stepSize=300;
             //   progress=((int)Math.round(progress/stepSize)*stepSize);
                seekBar.setProgress(progress);
                seekBarProgess=progress;
                seekBProgress=getConvertedValue(progress);
                seekBaProgress=convertToFloat(progress);
                if(seekBarProgess>100){
                    seekBar.setProgress(limit);
                }
                else {
                    seekBar.setProgress((int) seekBaProgress);
                }
                Log.e(TAG,"Progress is "+seekBarProgess);
              //  seekProgress=seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }



            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                displayProgress.setText(""+seekBarProgess+" Miles");
            //    SharedPreferences data=getApplicationContext().getSharedPreferences("saveMiles",MODE_PRIVATE);
               // SharedPreferences.Editor editor=data.edit();
                SharedPreferences .Editor editor=sharedPreferences.edit();
               // editor.putInt(progress,seekBarProgess);
               // editor.putString(progressText, String.valueOf(seekBarProgess));
              //  editor.putFloat(progress, (float) seekBProgress);
                //editor.putString(progressText,String.valueOf(seekBProgress));
                editor.putFloat(progress,seekBaProgress);
                editor.putString(progressText,String.valueOf(seekBaProgress));
                editor.commit();

            }
        });



        okBtn= (Button) findViewById(R.id.btnClick);
       okBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                seekBarProgess=seekBar.getProgress();
               // Toast.makeText(getApplicationContext(),"seek progress is "+seekBarProgess,Toast.LENGTH_SHORT).show();

             //   if(seekBarProgess<3900){
               //     Toast.makeText(getApplicationContext(),"Sorry! no restaurants within this range",Toast.LENGTH_SHORT).show();
                //}
               // else{
              //  Fragment fragment=getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewPager +":" )
                    Intent intent=new Intent(SelectRange.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
              /**  if(MainActivity.tabPosition==0){
                  /**  finishAndRemoveTask();

             RestaurantFragment fragment=null;
                    fragment=new RestaurantFragment();
                   getSupportFragmentManager().beginTransaction().replace(R.id.select_range,fragment).addToBackStack(null).commit();**/
             //  onBackPressed();
              /**  }
                else if(MainActivity.tabPosition==1){
                  //  Intent intent=new Intent(SelectRange.this,BarFragment.class);
                   // startActivity(intent);
                }
                else if(MainActivity.tabPosition==2){
                   // Intent intent=new Intent(SelectRange.this,GroceryFragment.class);
                    //startActivity(intent);
                }
                else if(MainActivity.tabPosition==3){
                   // Intent intent=new Intent(SelectRange.this,GasFragment.class);
                    //startActivity(intent);
                }**/

              //  }
                //Log.e(TAG,"Progress is "+seekProgress);
              //  if(seekProgress>=RestaurantFragment.distanceInt){
                 //   Intent intent=new Intent(SelectRange.this,RestaurantFragment.class);
                   // startActivity(intent);
              //  }
             //   else{
              //      Toast.makeText(getApplicationContext(),"No restaurants within given range",Toast.LENGTH_SHORT).show();
            //    }
            }
        });


    }

    private float convertToFloat(int progress) {
      // int prog=progress;
        float progressFloat=progress;
        return progressFloat;
    }

    public double getConvertedValue(int initValue){
        double val=0.00;
        val=initValue;
        return val;

    }
    public void onBackPressed(){

      super.onBackPressed();
    }


}
