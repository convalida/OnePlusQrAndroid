package com.convalida.user.jsonparsing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Convalida on 7/7/2017.
 */

public class BarFragment extends Fragment implements SearchView.OnQueryTextListener {
    private String TAG="BarFragment";
    private ProgressDialog progressDialog;
    ListView listView;
    static ArrayList<HashMap<String,String>> pointslist;
  //  ListViewAdapter adapterBar;
   DataAdapter dataAdapter;
    static Context context;
    static RecyclerView recyclerView;
    double roundoff;
    static boolean isUserActiveBar;
    String bussType;
    static int distanceInt;
    private static String url="http://oneplusrewards.com/app/api.asmx/GetBusinessData?Appid=123456789";
    private ArrayList<HashMap<String,String>> barList;
    static ArrayList<HashMap<String,String>> rangeBarList;
    ArrayList<HashMap<String,String>> arrList;
    static String NAME="BusinessName";
    static String LATITUDE="Latitude";
    static String LONGITUDE="Longitude";
    static String LOGO="Logo";
    TextView emptyView;
    View view;
   static double mileDist;
    float dist;
    String distString;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bar_fragment, container, false);
        context=getActivity();
        barList=new ArrayList<>();
        arrList=new ArrayList<>();
        pointslist=new ArrayList<>();
        rangeBarList=new ArrayList<>();
     //   listView= (ListView) view.findViewById(R.id.listBar);
       recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
       emptyView= (TextView) view.findViewById(R.id.emptyView);
//        listView.setDivider(null);
    //    new PointsTask().execute();
      //if(getUserVisibleHint()) {
            new GetDataBar().execute();
        //}


        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        setHasOptionsMenu(true);
        recyclerView.hasFixedSize();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
       // recyclerView.setAdapter(adapterBar);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HashMap<String,String> result=new HashMap<String, String>();
                result=rangeBarList.get(position);
                Intent intent=new Intent(context,OnClickRestaurant.class);
                intent.putExtra("BusinessID",result.get(MainActivity.ID));
                intent.putExtra("BusinessName",result.get(MainActivity.NAME));
                intent.putExtra("ContactNo",result.get(MainActivity.CONTACT));
                intent.putExtra("Website",result.get(MainActivity.WEBSITE));
                intent.putExtra("Timing",result.get(MainActivity.TIMING));
                intent.putExtra("Img",result.get(MainActivity.IMAGES));
                intent.putExtra("Reward",result.get(MainActivity.REWARDS));
                intent.putExtra("Latitude",result.get(MainActivity.LATITUDE));
                intent.putExtra("Longitude",result.get(MainActivity.LONGITUDE));
                intent.putExtra("MenuLink",result.get(MainActivity.MENU));
                intent.putExtra("OrderLink",result.get(MainActivity.ORDER));
                //   intent.putExtra("TotalPoint",result.get(MainActivity.POINT));
                context.startActivity(intent);
            }
        }));
    //    listView.setAdapter(adapterBar);
    /**    if(getUserVisibleHint()){
            new GetDataBar().execute();
        }**/
           }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menuInflater.inflate(R.menu.drawer,menu);
        MenuItem item=menu.findItem(R.id.search);
      SearchView searchView= (SearchView) item.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_bar));
        int searchPlateId=android.support.v7.appcompat.R.id.search_plate;
        View searchPlate=searchView.findViewById(searchPlateId);
        if(searchPlate!=null){
            searchPlate.setBackgroundColor(Color.parseColor("#1E88E5"));
            int searchTextId=searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
            TextView searchText= (TextView) searchPlate.findViewById(searchTextId);
            if(searchText!=null){
                searchText.setTextColor(Color.WHITE);
                searchText.setHintTextColor(Color.WHITE);
            }
        }



        AutoCompleteTextView searchTextView= (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes= TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView,R.drawable.cursor);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        searchView.setOnQueryTextListener(this);
    }
    public void onResume(){
        super.onResume();
        Log.e(TAG,"onResume of BarFragment");
    }
    public void onPause(){
        super.onPause();
        Log.e(TAG,"onPause of BarFragment");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<HashMap<String,String>> filteredList=filter(rangeBarList,newText);
        //adapterBar.setmFilter(filteredList);
        dataAdapter.setmFilter(filteredList);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HashMap<String,String> result=new HashMap<String, String>();
                if(filteredList.size()>0) {
                    result = filteredList.get(position);
                    Intent intent = new Intent(context, OnClickRestaurant.class);
                    intent.putExtra("BusinessID", result.get(MainActivity.ID));
                    intent.putExtra("BusinessName", result.get(MainActivity.NAME));
                    intent.putExtra("ContactNo", result.get(MainActivity.CONTACT));
                    intent.putExtra("Website", result.get(MainActivity.WEBSITE));
                    intent.putExtra("Timing", result.get(MainActivity.TIMING));
                    intent.putExtra("Img", result.get(MainActivity.IMAGES));
                    intent.putExtra("Reward", result.get(MainActivity.REWARDS));
                    intent.putExtra("Latitude", result.get(MainActivity.LATITUDE));
                    intent.putExtra("Longitude", result.get(MainActivity.LONGITUDE));
                    intent.putExtra("MenuLink", result.get(MainActivity.MENU));
                    intent.putExtra("OrderLink", result.get(MainActivity.ORDER));
                    //   intent.putExtra("TotalPoint",result.get(MainActivity.POINT));
                    context.startActivity(intent);
                }
            }
        }));
        return false;
    }

    private ArrayList<HashMap<String,String>> filter(ArrayList<HashMap<String, String>> rangeBarList, String newText) {
        newText=newText.toLowerCase();
        final ArrayList<HashMap<String,String>> filteredList=new ArrayList<>();
        for(HashMap<String,String> data:rangeBarList){
            final String text=data.get("BusinessName").toLowerCase();
            final String zip=data.get("ZipCode").toLowerCase();
            if(text.contains(newText)){
                filteredList.add(data);
            }
            else if(zip.contains(newText)){
                filteredList.add(data);
            }
        }
        return filteredList;
    }


    private class GetDataBar extends AsyncTask<Void,Void,Void>{
        String uid, urlPoints,points,urlMain,bussId;
        ArrayList<String> testList;
        HashMap<String,String> hmap;
        ArrayList<String> test2List;


        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            SharedPreferences preferences=context.getSharedPreferences("saveUserId",Context.MODE_PRIVATE);
            uid=preferences.getString("userId","");
            Log.e(TAG, "User id is "+uid);
            urlMain="http://oneplusrewards.com/app/api.asmx/GetBusinessData?Appid=123456789";
            urlPoints="http://oneplusrewards.com/app/api.asmx/UserPointByPhone?Appid=123456789&MID="+uid;
        }

        @Override
        protected Void doInBackground(Void... params) {
            test2List=new ArrayList<>();
            HttpHandler handler=new HttpHandler();
            // String bussId;
            String json=handler.makeServiceCall(urlPoints);
            Log.e(TAG,"Response from json "+json);
            if(json!=null){
                try {
                    // String bussId;
                    JSONArray jsonArray=new JSONArray(json);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                           bussId=jsonObject.getString("BusinessID");
                        // if(bussId.equals(id)){
                        //   while(bussId!=null) {
                         points = jsonObject.getString("TotalPoint");
                        // pointsList.add(points);
                        test2List.add(bussId);
                       /** HashMap<String,String> pointsMap=new HashMap<>();
                        pointsMap.put("BusinessID",bussId);
                        pointsMap.put("TotalPoint",points);

                        pointslist.add(pointsMap);
**/
                       hmap=new HashMap<>();
                       hmap.put(bussId,points);
                       arrList.add(hmap);


                       

                        // }// }
                    }
                    Log.e(TAG,"All points are "+pointslist);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            HttpHandler mainHandler=new HttpHandler();
            String jstr=mainHandler.makeServiceCall(url);
            Log.e(TAG,"Response from url "+jstr);
            if(jstr!=null){
                try{
                    JSONArray jsonArray=new JSONArray(jstr);
                    JSONObject objBar=jsonArray.getJSONObject(1);
                    bussType=objBar.getString("BusinessType");
                    Log.e(TAG,"Buss type "+bussType);
                    if(bussType.equals("Bar")){
                        Log.e(TAG,"Buss type is bar");
                    }
               //     for(int i=1;i<jsonArray.length();i++){
                    //    JSONObject obj1 = jsonArray.getJSONObject(i);
                      //  String bussType = obj1.getString("BusinessType");
                        //String buss = obj1.getString("Business");

                        JSONArray business = objBar.getJSONArray("Business");
                        for (int j = 0; j < business.length(); j++) {
                            JSONObject obj2 = business.getJSONObject(j);
                            String businessId=obj2.getString("BusinessID");
                            String BusinessName = obj2.getString("BusinessName");
                            String add1 = obj2.getString("Address1");
                            String add2 = obj2.getString("Address2");
                            String add3 = obj2.getString("Address3");
                            String contact = obj2.getString("ContactNo");
                            String zip = obj2.getString("ZipCode");
                            String Latitude = obj2.getString("Latitude");
                            String Longitude = obj2.getString("Longitude");
                            String website = obj2.getString("Website");
                            String Logo = obj2.getString("Logo");
                            String menuLink=obj2.getString("MenuLink");
                            String orderLink=obj2.getString("OrderLink");
                            String timing = obj2.getString("Timing");
                            String image=obj2.getString("Img");
                            String reward=obj2.getString("Reward");
                            distance(Latitude,Longitude);
                          /**  JSONArray jsonArray2 = obj2.getJSONArray("Timing");
                            for (int k = 0; k < jsonArray2.length(); k++) {
                                JSONObject obj3 = jsonArray2.getJSONObject(k);
                                String day = obj3.getString("Day");
                                String openTime = obj3.getString("OpenTime");
                                String closeTime = obj3.getString("CloseTime");
                            }**/
                            HashMap<String, String> map = new HashMap<>();
                            map.put("BusinessID",businessId);
                            map.put("BusinessName", BusinessName);
                            map.put("Latitude", Latitude);
                            map.put("Longitude", Longitude);
                            map.put("Logo", Logo);
                            map.put("Address1",add1);
                            map.put("Address2",add2);
                            map.put("Address3",add3);
                            map.put("ContactNo",contact);
                            map.put("ZipCode",zip);
                            map.put("Website",website);
                            map.put("Logo",Logo);
                            map.put("MenuLink",menuLink);
                            map.put("OrderLink",orderLink);
                            map.put("Timing",timing);
                            map.put("Img",image);
                            map.put("Reward",reward);
                            map.put("Distance",distString);

                            if(test2List.contains(businessId)){
                               bussId=businessId;
                               String uPoint=arrList.get(j).get(bussId);
                               map.put("TotalPoint",uPoint);
                            }
                            else {
                                map.put("TotalPoint","0");
                            }

                            if(SelectRange.seekBProgress>=roundoff){
                                rangeBarList.add(map);
                            }
                        }
                    //}
                } catch (JSONException e) {
                   Log.e(TAG,"Json parsing error");
                }
            }
            else{
                Log.e(TAG,"Couldn't get json from server");
            }
            return null;
        }
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
              //  if(pointslist!=null) {
                //    adapterBar = new ListViewAdapter(context, rangeBarList, pointslist);
                //}
                //else{
            /**    if(rangeBarList.size()==0){
                    Toast.makeText(context,"Sorry, no bars within this range",Toast.LENGTH_LONG).show();
                }**/

                  //  adapterBar=new ListViewAdapter(context,rangeBarList);
                if(rangeBarList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                   emptyView.setVisibility(View.VISIBLE);
                }
                dataAdapter=new DataAdapter(context,rangeBarList);
               // }
                sort();
              //  listView.setAdapter(adapterBar);
                recyclerView.setAdapter(dataAdapter);
                //recyclerView.setAdapter(adapterBar);
         //     if(rangeBarList.isEmpty() && BarFragment.isUserActiveBar){
           //         Toast.makeText(BarFragment.context,"Sorry, no bars within this range",Toast.LENGTH_SHORT).show();
             //   }
            }
        }
    }
 /**   public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
      //  if(isVisibleToUser && rangeBarList.isEmpty()){
        //    Toast.makeText(context,"Sorry, no restaurants within this range",Toast.LENGTH_SHORT).show();
        //}
      //  isUserActiveBar=isVisibleToUser;
        if(isVisibleToUser){
         //   new GetDataBar().execute();
            if(rangeBarList.isEmpty()){
                Toast.makeText(context,"Sorry, no bars in this range", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"Sorry, no bars in this range");
            }
        }
    }**/

    private void sort() {
        Collections.sort(rangeBarList, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                return o2.get("Distance").compareTo(o1.get("Distance"));
            }
        });
    }

    private String distance(String lati, String longi) {
        Log.e(TAG,"Inside distance method");
        double lat=Double.parseDouble(lati);
        double lon=Double.parseDouble(longi);
      //  LatLng latLngA=new LatLng(lat,lon);
        //LatLng latLngB=new LatLng(MainActivity.curLatitude,MainActivity.curLongitude);
        Location locationA=new Location("point A");
        //locationA.setLatitude(latLngA.latitude);
        //locationA.setLongitude(latLngA.longitude);
        locationA.setLatitude(lat);
        locationA.setLongitude(lon);
        Location locationB=new Location("point B");
        //locationB.setLatitude(latLngB.latitude);
        //locationB.setLatitude(latLngB.longitude);
      /**  final GlobalClass globalVariable= (GlobalClass) context.getApplicationContext();
        final double latit=globalVariable.getLatitude();
        final double longit=globalVariable.getLongitude();
      locationB.setLatitude(latit);
      locationB.setLongitude(longit);**/
        //  locationB.setLatitude(32.500000);
       // locationB.setLongitude(-83.600000);
        locationB.setLatitude(28.520487); // saket metro station
        locationB.setLongitude(77.201531);
        dist=locationA.distanceTo(locationB);
        mileDist=dist*0.000621371192;
        Log.e(TAG,"Distance is "+mileDist);

        roundoff=Math.round(mileDist*10.0)/10.0;
        distanceInt=(int)mileDist;
        distString=Double.toString(roundoff);
        return distString;

    }


}
