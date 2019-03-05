package com.convalida.user.jsonparsing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class OnClickRestaurant extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    String contact,lat,lng,day,openTime,closeTime,timing,name,menu,order,imgPass,rewards,id;
    String monTime,tueTime,wedTime,thuTime,friTime,satTime,sunTime;
    String Day,OpenTime,CloseTime;
    String imgName, imgLink;
    String rewardN,rewardI,point;
    static ArrayList<HashMap<String,String>> pointsList;
    TableLayout t1;
    LinearLayout verticalLinearLayout,horizontalRow;
    private String listView_array[] = {"One", "Two", "Three", "Four", "Five"};
  //  RecyclerView recyclerView;
   // DataAdapter dataAdapter;
   // ArrayList<HashMap<String,String>> test;
    //HashMap<String,String> testMap;
    String Name,Link;
    TextView testingClick;
    JSONArray time;
    ListView listView;
  //  String[] rewardName={"Reward1","Reward2","Reward3","Reward4"};
  //  Integer[] imgId={R.drawable.reward,R.drawable.reward,R.drawable.reward,R.drawable.reward};
  static   ArrayList<String> arr=new ArrayList<>();
   static ArrayList<String> arr2=new ArrayList<>();
  static   ArrayList<String> arrDay=new ArrayList<>();
    static ArrayList<String> arrImgName=new ArrayList<>();
    static ArrayList<String> arrImgLink=new ArrayList<>();
    static ArrayList<String> arrRewardName=new ArrayList<>();
    static ArrayList<String> arrRewardItem=new ArrayList<>();
    static ArrayList<String> arrPoints=new ArrayList<>();
    ArrayList<String> array1=new ArrayList<>();
    HashMap<String,String> result=new HashMap<String, String>();
    ArrayList<HashMap<String,String>> data;
    Button orderBtn;
    ImageButton navigateBtn,callBtn,menuBtn,timingBtn;

    TextView textViewTime,rest_name,temp;
    TextView rewardName,rewardItem,points;
    private static final String TAG="OnClickRestaurant";
    ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String, String>>();
    SliderLayout sliderLayout;
    HashMap<String,String> images;
    Spinner spinner;
    Toolbar toolbar;
    String firName;
    String contactNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_click_restaurant);
        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        float heightDp=getResources().getDisplayMetrics().heightPixels/3;
        LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) sliderLayout.getLayoutParams();
        lp.height= (int) heightDp;
        if (!isNetworkAvailable()) {


            new AlertDialog.Builder(OnClickRestaurant.this)
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
            //Toast.makeText(MainActivity.this,"Internet connection required",Toast.LENGTH_SHORT).show();

        }
else{
        pointsList = new ArrayList<>();
        images = new HashMap<String, String>();

        callBtn = (ImageButton) findViewById(R.id.callBtn);
        t1 = (TableLayout) findViewById(R.id.rewardTable);


        navigateBtn = (ImageButton) findViewById(R.id.mapBtn);
        menuBtn = (ImageButton) findViewById(R.id.menuBtn);
        orderBtn = (Button) findViewById(R.id.orderBtn);
        timingBtn = (ImageButton) findViewById(R.id.timingBtn);
        //  testingClick= (TextView) findViewById(R.id.testclick);
        textViewTime = (TextView) findViewById(R.id.openText);
        rest_name = (TextView) findViewById(R.id.restName);

        toolbar = (Toolbar) findViewById(R.id.toolbarClick);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        contact = i.getStringExtra("ContactNo");
        lat = i.getStringExtra("Latitude");
        lng = i.getStringExtra("Longitude");
        timing = i.getStringExtra("Timing");
        imgPass = i.getStringExtra("Img");
        rewards = i.getStringExtra("Reward");
        id = i.getStringExtra("BusinessID");
        name = i.getStringExtra("BusinessName");
        menu = i.getStringExtra("MenuLink");
        order = i.getStringExtra("OrderLink");

        rest_name.setText(name);

        //  rest_name.setGravity(Gravity.CENTER);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Sorry you cannot make a call right now", Toast.LENGTH_LONG).show();
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact));
                    startActivity(callIntent);
                }
            }
        });
        navigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable()){
                    new AlertDialog.Builder(OnClickRestaurant.this)
                            .setMessage("Internet connection is required to view our location on map")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = getIntent();
                                    //  finish();
                                    startActivity(intent);
                                    // finish();
                                }
                            })
                            .setCancelable(true)
                            .create()
                            .show();
                }
                else {
                    double lt = 0;
                    double ln = 0;
                    lt = Double.parseDouble(lat);
                    ln = Double.parseDouble(lng);
                    Intent intent = new Intent(OnClickRestaurant.this, MapsActivity.class);
                    intent.putExtra("lat", lt);
                    intent.putExtra("lon", ln);
                    startActivity(intent);
                }
            }
        });



        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable()){
                    new AlertDialog.Builder(OnClickRestaurant.this)
                            .setMessage("Internet connection is required to view the menu")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = getIntent();
                                    //  finish();
                                    startActivity(intent);
                                    // finish();
                                }
                            })
                            .setCancelable(true)
                            .create()
                            .show();
                }
                else {
                    if (menu.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Sorry, no menu for now", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(OnClickRestaurant.this, MenuActivity.class);
                        intent.putExtra("MenuLink", menu);
                        startActivity(intent);
                        //Intent intent = new Intent(Intent.ACTION_VIEW);
                        //  intent.setData(Uri.parse(menu));
                        // startActivity(intent);
                    }
                }
            }
        });
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable()){
                    new AlertDialog.Builder(OnClickRestaurant.this)
                            .setMessage("Internet connection is required to place your order")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = getIntent();
                                    //  finish();
                                    startActivity(intent);
                                    // finish();
                                }
                            })
                            .setCancelable(true)
                            .create()
                            .show();
                }
                else {
                    if (order.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Sorry, you cannot order currently", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(OnClickRestaurant.this, OrderActivity.class);
                        intent.putExtra("OrderLink", order);
                        startActivity(intent);
                    }
                }

            }
        });

        timingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timing.equals("[]")) {
                    Toast.makeText(getApplicationContext(), "Sorry, timing array is empty for now", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(OnClickRestaurant.this, WeekTiming.class);
                    intent.putExtra("Timing", timing);
                    startActivity(intent);
                }

            }
            //builder.setMessage(array1.toString()+"").show();

        });


        getCurrentDayAndTime();
        getImageRestraurant();
//
        //    if(arrImgName.size()!=0){
        for (int k = 0; k < arrImgName.size(); k++) {
            images.put(arrImgName.get(k), arrImgLink.get(k));
            Log.e(TAG, "Image name " + arrImgName + ", Image link " + arrImgLink);
        }
        //  images.put(imgName,imgLink);
        for (String name : images.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(OnClickRestaurant.this);
            textSliderView
                    .image(images.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            // .setOnSliderClickListener(this);
            sliderLayout.addSlider(textSliderView);
            //}
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(3000);
        }

        new ActionBarTask().execute();
        new ActionBarPoints().execute();

    }

    }




    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null;
    }


    @SuppressLint("ResourceType")
    private void getRewards(int userPoints) {
        String jsonObj;

        try {
            JSONArray jsonArray=new JSONArray(rewards);
            SharedPreferences preferences=getApplicationContext().getSharedPreferences("saveRewardsArray",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            for(int i=0;i<jsonArray.length();i++){
             //   editor.p
                 jsonObj=jsonArray.toString();
                editor.putString("rewards",jsonObj);
            }

          //  editor.putString("rewardsArray", String.valueOf(jsonArray));
           // editor.putInt("rewardArrayLength",jsonArray.length());

            editor.commit();
            int count=0;



                loadRewardstable(jsonArray,userPoints);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("ResourceType")
    private void loadRewardstable(JSONArray jsonArray, int userPoints) {
        if(jsonArray.length()==0 ){
       //     Toast.makeText(getApplicationContext(), "No rewards available", Toast.LENGTH_LONG).show();

        }
else{
        int count=0;
        TableRow tr_head=new TableRow(this);
        tr_head.setId(50);
        //    tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView headItem=new TextView(this);
        headItem.setText("Rewards");
        headItem.setPadding(30,5,5,5);
        headItem.setTypeface(headItem.getTypeface(),Typeface.BOLD);
        headItem.setTextSize(18);
        headItem.setTextColor(Color.BLACK);
        tr_head.addView(headItem);

        TextView headPoint=new TextView(this);
        headPoint.setId(99);
        headPoint.setTypeface(headItem.getTypeface(),Typeface.BOLD);
        headPoint.setTextSize(18);
        headPoint.setGravity(Gravity.RIGHT);
        headPoint.setPadding(30,5,20,15);
        headPoint.setText("Points");
        headPoint.setTextColor(Color.BLACK);
        tr_head.addView(headPoint);

        t1.addView(tr_head, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        arrRewardName.clear();
        arrRewardItem.clear();
        arrPoints.clear();
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject= null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
                rewardN=jsonObject.getString("RewardName");
                rewardI=jsonObject.getString("RewardItem");
                point=jsonObject.getString("Point");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            arrRewardName.add(rewardN);
            arrRewardItem.add(rewardI);
            arrPoints.add(point);
//                rewardName.setText(arrRewardName.get(0));
            //              rewardItem.setText(arrRewardItem.get(0));
            //             points.setText(arrPoints.get(0));

            if(count<arrRewardItem.size()){
                TableRow tr=new TableRow(this);

                tr.setId(100+count);
                tr.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView labelItem=new TextView(this);
                labelItem.setText(arrRewardItem.get(i));
                labelItem.setPadding(30,5,5,5);//left,top,right,bottom
                //  labelItem.setTypeface(labelItem.getTypeface(),Typeface.ITALIC);
                labelItem.setTextSize(18);
                /**  if(count%2!=0)
                 {
                 tr.setBackgroundColor(Color.GRAY);
                 labelItem.setTextColor(Color .WHITE);
                 }
                 else{**/
                //     tr.setBackgroundColor(Color.WHITE);
                labelItem.setTextColor(Color.BLACK);
                //  }

                tr.addView(labelItem);

                TextView labelPoint=new TextView(this);
                labelPoint.setId(200+count);
                // labelPoint.setTypeface(labelItem.getTypeface(),Typeface.ITALIC);
                labelPoint.setGravity(Gravity.RIGHT);
                labelPoint.setPadding(20,5,20,15);
                labelPoint.setTextSize(18);
                labelPoint.setText(arrPoints.get(i));
                /**     if(count%2!=0)
                 {
                 tr.setBackgroundColor(Color.GRAY);
                 labelPoint.setTextColor(Color.WHITE);
                 }
                 else{**/
                //  tr.setBackgroundColor(Color.WHITE);
                // labelPoint.setTextColor(Color.BLACK);
                labelPoint.setTextColor(Color.parseColor("#1e90ff"));
                if((Integer.parseInt(arrPoints.get(i)))>userPoints){
                    labelItem.setTextColor(Color.parseColor("#b0b0b0"));
                    labelPoint.setTextColor(Color.parseColor("#b0b0b0"));
                }
                //  }
                //     labelPoint.setTextColor(Color.BLACK);
                tr.addView(labelPoint);

                t1.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                count++;
            }

        }
        }
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    private void getImageRestraurant() {
        try {
            JSONArray jsonArray=new JSONArray(imgPass);
            if(jsonArray.length()>0){
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    imgName=jsonObject.getString("ImgName");
                    imgLink=jsonObject.getString("Link");
                    arrImgName.add(imgName);
                    arrImgLink.add(imgLink);
                    Log.e(TAG,"Image name and links: "+imgName+", "+imgLink);
                }
            }
            else{
                arrImgName.clear();
                arrImgLink.clear();
                images.put("Food1","http://www.metropolisgrill.com/img/gallery/img1.jpg");
                images.put("Food2","http://www.metropolisgrill.com/img/gallery/img2.jpg");
                images.put("Food3","http://www.metropolisgrill.com/img/gallery/img4.jpg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent intent=new Intent(OnClickRestaurant.this, MainActivity.class);
        startActivity(intent);
    }


    private void getCurrentDayAndTime() {
        try {
            JSONArray jsonArray = new JSONArray(timing);
            String timings ="";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Day = jsonObject.getString("Day");
                    OpenTime = jsonObject.getString("OpenTime");
                    CloseTime = jsonObject.getString("CloseTime");
                    arr.add(OpenTime);
                    arr2.add(CloseTime);
                    arrDay.add(Day);
                   array1.add(Day+"    "+OpenTime + " - " + CloseTime +"\n");
                    timings  = timings + Day +" "+OpenTime + " - " + CloseTime +"\n";

                }
            //temp.setText(arrDay.toString()+"  "+arr+"   "+arr2);



           Calendar calendar = Calendar.getInstance();
            DateFormat timeFormat=new SimpleDateFormat("HH:mm");


            int day = calendar.get(Calendar.DAY_OF_WEEK);
            //int seconds=calendar.get(Calendar.SECOND);
            //Log.e("Time","TIme is: "+seconds);
            switch (day) {
                case Calendar.MONDAY:
                    String ot=arr.get(0);
                    String ct=arr2.get(0);
                    DateFormat dateFormat=new SimpleDateFormat("HH:mm");
                    Date timeOpen=dateFormat.parse(ot);
                    Date timeClose=dateFormat.parse(ct);
                    String localTime=timeFormat.format(calendar.getTime());
                    Date currentTime=timeFormat.parse(localTime);
                    Log.e("Time","Time is: "+localTime);

                    if(currentTime.after(timeOpen) && currentTime.before(timeClose)){
                        textViewTime.setText("Open now");
                        textViewTime.setTextColor(Color.parseColor("#3ec731"));
                    }
                    else {
                        textViewTime.setText("Closed now");
                        textViewTime.setTextColor(Color.RED);
                    }
                   // textViewTime.setText(arr.get(0));
                    //   Toast.makeText(getApplicationContext(),"Today is Monday",Toast.LENGTH_LONG).show();
                    break;
                case Calendar.TUESDAY:
                    ot=arr.get(1);
                    ct=arr2.get(1);
                    dateFormat=new SimpleDateFormat("HH:mm");
                    timeOpen=dateFormat.parse(ot);
                    timeClose=dateFormat.parse(ct);
                    localTime=timeFormat.format(calendar.getTime());
                    currentTime=timeFormat.parse(localTime);
                    Log.e("Time","Time is: "+localTime);

                    if(currentTime.after(timeOpen) && currentTime.before(timeClose)){
                        textViewTime.setText("Open now");
                        textViewTime.setTextColor(Color.parseColor("#3ec731"));
                    }
                    else {
                        textViewTime.setText("Closed now");
                        textViewTime.setTextColor(Color.RED);
                    }
                    //textViewTime.setText(arr.get(1));
                    // Toast.makeText(getApplicationContext(),"Today is Tuesday",Toast.LENGTH_LONG).show();
                    break;
                case Calendar.WEDNESDAY:
                    ot=arr.get(2);
                    ct=arr2.get(2);
                    dateFormat=new SimpleDateFormat("HH:mm");
                    timeOpen=dateFormat.parse(ot);
                    timeClose=dateFormat.parse(ct);
                    localTime=timeFormat.format(calendar.getTime());
                    currentTime=timeFormat.parse(localTime);
                    Log.e("Time","Time is: "+localTime);

                    if(currentTime.after(timeOpen) && currentTime.before(timeClose)){
                        textViewTime.setText("Open now");
                        textViewTime.setTextColor(Color.parseColor("#3ec731"));
                    }
                    else {
                        textViewTime.setText("Closed now");
                        textViewTime.setTextColor(Color.RED);
                    }
                 //   textViewTime.setText(arr.get(2));
                    //Toast.makeText(getApplicationContext(),"Today is Wednesday",Toast.LENGTH_LONG).show();
                    break;
                case Calendar.THURSDAY:
                    ot=arr.get(3);
                    ct=arr2.get(3);
                    dateFormat=new SimpleDateFormat("HH:mm");
                    timeOpen=dateFormat.parse(ot);
                    timeClose=dateFormat.parse(ct);
                    localTime=timeFormat.format(calendar.getTime());
                    currentTime=timeFormat.parse(localTime);
                    Log.e("Time","Time is: "+localTime);

                    if(currentTime.after(timeOpen) && currentTime.before(timeClose)){
                        textViewTime.setText("Open now");
                        textViewTime.setTextColor(Color.parseColor("#3ec731"));
                    }
                    else {
                        textViewTime.setText("Closed now");
                        textViewTime.setTextColor(Color.RED);
                    }
                    // textViewTime.setText(arr.get(3));
                    //Toast.makeText(getApplicationContext(),"Today is Thursday",Toast.LENGTH_LONG).show();
                    break;
                case Calendar.FRIDAY:
                    ot=arr.get(4);
                    ct=arr2.get(4);
                    dateFormat=new SimpleDateFormat("HH:mm");
                    timeOpen=dateFormat.parse(ot);
                    timeClose=dateFormat.parse(ct);
                    localTime=timeFormat.format(calendar.getTime());
                    currentTime=timeFormat.parse(localTime);
                    Log.e("Time","Time is: "+localTime);

                    if(currentTime.after(timeOpen) && currentTime.before(timeClose)){
                        textViewTime.setText("Open now");
                        textViewTime.setTextColor(Color.parseColor("#3ec731"));
                    }
                    else {
                        textViewTime.setText("Closed now");
                        textViewTime.setTextColor(Color.RED);
                    }
                   // textViewTime.setText(arr.get(4));
                    //Toast.makeText(getApplicationContext(),"Today is Friday",Toast.LENGTH_LONG).show();
                    break;
                case Calendar.SATURDAY:
                   // textViewTime.setText(arr.get(5));
                   ot=arr.get(5);
                    ct=arr2.get(5);
                     dateFormat=new SimpleDateFormat("HH:mm");
                    timeOpen=dateFormat.parse(ot);
                     timeClose=dateFormat.parse(ct);
                     localTime=timeFormat.format(calendar.getTime());
                    currentTime=timeFormat.parse(localTime);
                    Log.e("Time","Time is: "+localTime);

                    if(currentTime.after(timeOpen) && currentTime.before(timeClose)){
                    textViewTime.setText("Open now");
                   textViewTime.setTextColor(Color.parseColor("#3ec731"));
                        }
                    else {
                        textViewTime.setText("Closed now");
                        textViewTime.setTextColor(Color.RED);
                    }
                    //Toast.makeText(getApplicationContext(),"Today is Saturday",Toast.LENGTH_LONG).show();
                    break;
                case Calendar.SUNDAY:

                    ot=arr.get(6);
                    ct=arr2.get(6);
                    dateFormat=new SimpleDateFormat("HH:mm");
                    timeOpen=dateFormat.parse(ot);
                    timeClose=dateFormat.parse(ct);
                    localTime=timeFormat.format(calendar.getTime());
                    currentTime=timeFormat.parse(localTime);
                    Log.e("Time","Time is: "+localTime);

                    if(currentTime.after(timeOpen) && currentTime.before(timeClose)){
                        textViewTime.setText("Open now");
                        textViewTime.setTextColor(Color.parseColor("#3ec731"));
                    }
                    else {
                        textViewTime.setText("Closed now");
                        textViewTime.setTextColor(Color.RED);
                    }//textViewTime.setText(6);
                    //Toast.makeText(getApplicationContext(),"Today is Sunday",Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    protected void onStop(){
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra")+"",Toast.LENGTH_LONG).show();
    }


    private class ActionBarTask extends AsyncTask<Void, Void, String>{
         String str, url,name,uid,mid;
        protected void onPreExecute(){
            SharedPreferences data=getApplicationContext().getSharedPreferences("saveNumber",MODE_PRIVATE);
             str=data.getString("Number","");
           //  uid=data.getString("userId","");
            Log.e(TAG,"Contact no. is "+str);
            //Log.e(TAG,"user id is "+uid);
            url="http://oneplusrewards.com/app/api.asmx/UserDataByPhone?Appid=123456789&CustomerPhone="+str;
        }
        @Override
        protected String doInBackground(Void... params) {
            HttpHandler actionBarHandler=new HttpHandler();
            String json=actionBarHandler.makeServiceCall(url);
            Log.e(TAG, "Response from url "+json);
            if(json!=null){
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    name=jsonObject.getString("FirstName");
                 //   mid=jsonObject.getString("MID");
                   // Log.e(TAG,"Mid inside onclickrestaurant is"+mid);
                  //  getSupportActionBar().setTitle("Hello "+name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return name;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(name!=null) {
                if(getSupportActionBar()!=null) {
                    getSupportActionBar().setTitle("Hi " + name + ",");
                }          //     getSupportActionBar().setSubtitle("Your points: 100 pts");
               // getSupportActionBar().set
            }
        }
    }

    private class ActionBarPoints extends AsyncTask<Void, Void, String> {
        String uid, urlPoint, points;
        HashMap<String,String> hmap;

        protected void onPreExecute(){
        SharedPreferences data=getApplicationContext().getSharedPreferences("saveUserId",MODE_PRIVATE);
        uid=data.getString("userId","");
        Log.e(TAG,"User id is "+uid);
        urlPoint="http://oneplusrewards.com/app/api.asmx/UserPointByPhone?Appid=123456789&MID="+uid;
        }
        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler pointHandler=new HttpHandler();
            String pointJson=pointHandler.makeServiceCall(urlPoint);
            Log.e(TAG,"Response from json "+pointJson);
            if(pointJson!=null){
                try {
                    String bussId;
                    JSONArray jsonArray=new JSONArray(pointJson);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        bussId=jsonObject.getString("BusinessID");
                        if(bussId.equals(id)) {
                        points=jsonObject.getString("TotalPoint");

                           // hmap = new HashMap<>();
                           // hmap.put("BusinessID", uid);
                           // hmap.put("TotalPoint", points);
                        }// pointsList.add(hmap);
                    }

                //Log.e(TAG,"Points list is "+pointsList);
                }
                    catch (JSONException e) {
                    e.printStackTrace();
                }
            }
           return points;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            //for(int i=0;i<pointsList.size();i++){
          //  if(pointsList[i].equals(id)) {
            String data;

                if (getSupportActionBar() != null) {
                    if(points!=null) {
                        getSupportActionBar().setSubtitle("Your points: " + points);
                    }
                    else{
                        getSupportActionBar().setSubtitle("Your points: 0");
                    }
            //    }
            }
                if (points != null) {
                    getRewards(Integer.parseInt(points));
                   // loadRewardstable(data);
                } else {
                    getRewards(0);
                }
         /**   }
            else{
                loadRewardstable();
            }**/
        // }

        }
    }
}
