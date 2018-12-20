package com.convalida.user.jsonparsing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeekTiming extends AppCompatActivity {
    String timing,Day,OpenTime,CloseTime;
    TextView alertText;
    TableLayout tableLayout;
    ArrayList<String> arrDay=new ArrayList<>();
    ArrayList<String> arrOpenTime=new ArrayList<>();
    ArrayList<String> arrCloseTime=new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_week_timing);
       // alertText= (TextView) findViewById(R.id.alert);
        tableLayout= (TableLayout) findViewById(R.id.timingTable);
        Intent i=getIntent();
        timing=i.getStringExtra("Timing");
     //   this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        String timings = "";
        TableRow rowHead=new TableRow(this);
        rowHead.setId(70);
        rowHead.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView headDay=new TextView(this);
        headDay.setText("Days");
        headDay.setTypeface(headDay.getTypeface(),Typeface.BOLD);
        headDay.setPadding(20,5,20,5);
        headDay.setTextColor(Color.BLACK);
        rowHead.addView(headDay);

       TextView headOpen=new TextView(this);
        headOpen.setText("Open");
        headOpen.setTypeface(headOpen.getTypeface(),Typeface.BOLD);
        headOpen.setPadding(20,5,20,5);
        headOpen.setTextColor(Color.BLACK);
        rowHead.addView(headOpen);

        TextView headClose=new TextView(this);
        headClose.setTypeface(headClose.getTypeface(),Typeface.BOLD);
        headClose.setText("Close");
        headClose.setPadding(20,5,20,5);
        headClose.setTextColor(Color.BLACK);
        rowHead.addView(headClose);

        tableLayout.addView(rowHead, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        try {
            int count=0;
             JSONArray jsonArray = new JSONArray(timing);
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                Day = jsonObject.getString("Day");
                OpenTime = jsonObject.getString("OpenTime");
                CloseTime = jsonObject.getString("CloseTime");
                arrDay.add(Day);
                arrOpenTime.add(OpenTime);
                arrCloseTime.add(CloseTime);

                if(count<arrDay.size()) {
                    TableRow tr = new TableRow(this);
                    tr.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView labelDay = new TextView(this);
                   // String sub=labelDay.getText(arrDay.get(j).substring(0,3));
              labelDay.setText(arrDay.get(j));
              //      String sub=\
                    labelDay.setTextColor(Color.BLACK);
                    labelDay.setPadding(20,5,20,5);
                    tr.addView(labelDay);

                    TextView labelOpenTime = new TextView(this);
                    labelOpenTime.setText(arrOpenTime.get(j));
                    labelOpenTime.setTextColor(Color.BLACK);
                    labelOpenTime.setPadding(20,5,20,5);
                    tr.addView(labelOpenTime);

                    TextView labelCloseTime = new TextView(this);
                    labelCloseTime.setText(arrCloseTime.get(j));
                    labelCloseTime.setTextColor(Color.BLACK);
                    labelCloseTime.setPadding(20,5,20,5);
                    tr.addView(labelCloseTime);

                    tableLayout.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    count++;
                    // timings = timings + Day.substring(0,3).toUpperCase().trim() + "   " + OpenTime + "  -  " + CloseTime + "\n";
                }
                }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
//        alertText.setText(timings);
       // AlertDialog.Builder builder;
        //if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
          //  builder=new AlertDialog.Builder(WeekTiming.this,android.R.style.Theme_Material_Dialog_Alert);
            //   builder=new AlertDialog.Builder(OnClickRestaurant.this);
       // }
        //else {
          //  builder=new AlertDialog.Builder(WeekTiming.this);
        //}
        //builder.setTitle("Timings");
//                builder.create().getWindow().setLayout(50,50);

        //  Dialog dialog=builder.setView(new View(OnClickRestaurant.this)).create();
        //WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
        //layoutParams.copyFrom(dialog.getWindow().getAttributes());
        //layoutParams.width=WindowManager.LayoutParams.WRAP_CONTENT;
        //layoutParams.width=WindowManager.LayoutParams.WRAP_CONTENT;
        //dialog.show();
        //dialog.getWindow().setAttributes(layoutParams);
        //  builder.setMessage(array1.get(position));
        //builder.setMessage(timings+"")
          //      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            //        @Override
              //      public void onClick(DialogInterface dialog, int which) {

                //    }
                //})
                //.show();

    }
}
