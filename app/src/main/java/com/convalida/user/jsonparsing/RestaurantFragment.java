package com.convalida.user.jsonparsing;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;



public class RestaurantFragment extends Fragment implements SearchView.OnQueryTextListener {
    static double currentLatitude,currentLongitude;
    View  view;
    private static String TAG="RestaurantFragment";
    private ProgressDialog progressDialog;
   ListView listView;

    static String bussType;
    static double dist;
    static int flag;
    static double kmdist;
    static double roundoff;
    static float round;
  //  static float roundoff;

    static String latitude,longitude,businessName,businessId,address1,address2,address3,zipCode,contactNo,website,logo,menuLink,orderLink,
            timing, image,reward,bussId,points;
    // ListViewAdapter adapterRest;
    //Context context;
    static double mileDist;
    static String distString;
    static int distanceInt;
    private static final int PLAY_SERVICES_REQUEST=1000;
  //  static ArrayList<HashMap<String,String>> businessList;
    static ArrayList<HashMap<String,String>> restaurantList;
 //  static ArrayList<HashMap<String,String>> bussList ;
    private SearchManager searchManager;
    String name,add1,add2,add3;

 // private static String url="http://localhost:10945/App/Api.asmx/GetBusinessData?Appid=123456789";
    private ArrayList<HashMap<String,String>> mainList;
    static ArrayList<HashMap<String,String>> timingList=new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String,String>> finalList=new ArrayList<HashMap<String, String>>();
    ArrayList<String> pointsList;
    private static Context context;
    static boolean isUserActiveRestaurtant;
    //public List<String> timingList;
    static String NAME="BusinessName";
    static String LATITUDE="Latitude";
    static String LONGITUDE="Longitude";
    static String LOGO="Logo";
    static String ADDRESS1="Address1";
    static String ADDRESS2="Address2";
    static String ADDRESS3="Address3";
    static String CONTACT="ContactNo";
    static String ZIP="ZipCode";
    static String WEBSITE="Website";
    static String TIMING="Timing";
    static String DAY="Day";
    static String OPENTIME="OpenTime";
    static String CLOSETIME="CloseTime";
    static JSONArray jsonArray2;
    static ArrayList<HashMap<String,String>> restArrayList;
    static ArrayList<HashMap<String,String>> rangeRestArrayList;
    static ArrayList<ArrayList<HashMap<String,String>>> newList;
    static ArrayList<HashMap<String,String>> pointslist;
   // static HashMap<String,String> pointsMap;
    static ArrayList<Double> distList;
    static double currLatitude, currLongitude;
    private Location lastLocation;
    private GoogleApiClient googleApiClient;
    static DataAdapter dataAdapter;
  //  PointsAdapter pointsAdapter;
   static RecyclerView recyclerView;
    HashMap<String,String> result=new HashMap<String, String>();
    LocationManager locationManager;
    private boolean isViewShown=false;
    TextView emptyView;
    Context ctx;
    public static String empty;


    ListAdapter timingAdapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.e(TAG,"Rest frag oncreate");
        setUserVisibleHint(false);
    //    setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.restaurant_fragment,container,false);
        Log.e(TAG,"Rest frag oncreateview");
        context=getActivity();
    //    businessList=new ArrayList<>();
        restaurantList=new ArrayList<>();
       // bussList=new ArrayList<>();
        restArrayList=new ArrayList<>();
        newList=new ArrayList<>();
        pointslist=new ArrayList<>();
     //   pointsMap=new HashMap<>();
        rangeRestArrayList=new ArrayList<>();
        distList=new ArrayList<>();
        ctx=container.getContext();
      //  dataAdapter=new DataAdapter(RestaurantFragment.this,restaurantList)
       /** locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"Permission not granted");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);**/
        //listView= (ListView) view.findViewById(R.id.listView);
        //listView.setDivider(null);

        initViews();
 //       new PointsTask().execute();
  //      if(getUserVisibleHint()) {
      //  if(!isViewShown) {
            new GetDataRestuarant().execute();
          //  checkForFirstFragment();
        //}
    //    }
        return view;

    }

  /**  private void checkForFirstFragment() {
      //  if(getView()!=null) {
        //    isViewShown = true;
            if (MainActivity.current == 0 && restaurantList.isEmpty()) {
                Toast.makeText(context, "Sorry, no restaurants within this range", Toast.LENGTH_SHORT).show();
            }
        //}
        //else
          //  isViewShown=false;
    }**/

    private void initViews() {
        emptyView= (TextView) view.findViewById(R.id.emptyView);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dataAdapter);
       recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
           @Override
           public void onItemClick(View view, int position) {
               HashMap<String,String> result=new HashMap<String, String>();
               result=restaurantList.get(position);
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
    }


    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        setHasOptionsMenu(true);
      //  listView.setAdapter(adapterRest);
        recyclerView.setAdapter(dataAdapter);

    }


    private boolean checkPlayServices(){
        GoogleApiAvailability googleApiAvailability=GoogleApiAvailability.getInstance();
        int result=googleApiAvailability.isGooglePlayServicesAvailable(context);
        if(result!=ConnectionResult.SUCCESS){
            if(googleApiAvailability.isUserResolvableError(result)){
                googleApiAvailability.getErrorDialog(getActivity(),result,PLAY_SERVICES_REQUEST).show();
            }
            else {
                Toast.makeText(context,"This device is not supported",Toast.LENGTH_LONG).show();

            }
            return false;
        }
        return true;
    }
    public void onStart(){
        super.onStart();
        if(googleApiClient!=null){
            googleApiClient.connect();
        }
    }
    public void onResume(){
        super.onResume();
        checkPlayServices();
        Log.e("RestaurantFragment","onResume of RestaurantFragment called");
    }
public void onPause(){
        super.onPause();
        Log.e(TAG,"onPause of RestaurantFragment called");
}


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

            inflater.inflate(R.menu.drawer, menu);
     //   if (restaurantList.size()!=0){
        Log.e(TAG,"ArrayList size is "+restaurantList.size());
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        //   MaterialSearchView searchView= (MaterialSearchView) view.findViewById(R.id.searchView);
        //  searchView.setMenuItem(item);
        searchView.setQueryHint(getResources().getString(R.string.search_here));
        int searchPlateId = android.support.v7.appcompat.R.id.search_plate;
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.parseColor("#1E88E5"));
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText != null) {
                searchText.setTextColor(Color.WHITE);
                searchText.setHintTextColor(Color.WHITE);
            }
        }
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

            searchView.setOnQueryTextListener(this);

        /**    MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
        @Override public boolean onMenuItemActionExpand(MenuItem item) {

        return true;
        }

        @Override public boolean onMenuItemActionCollapse(MenuItem item) {
        adapterRest.setmFilter(rangeRestArrayList);
        return true;
        }
        });**/
   // }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
      //  final ArrayList<HashMap<String,String>> filteredList=filter(rangeRestArrayList,newText);
        final ArrayList<HashMap<String, String>> filteredList=filter(restaurantList,newText);
//        adapterRest.setmFilter(filteredList);
        dataAdapter.setmFilter(filteredList);
        //  adapterRest.getFilter().filter(newText);
        return false;
    }

    private ArrayList<HashMap<String,String>> filter(ArrayList<HashMap<String, String>> restaurantList, String newText) {
        newText=newText.toLowerCase();
        final ArrayList<HashMap<String,String>> filteredList=new ArrayList<>();
        for(HashMap<String,String> data:restaurantList){
            //for(HashMap<String,String> data:arr){
           //for(HashMap.Entry<String,String> data:hmap.entrySet()) {
               final String zip = data.get("ZipCode");
               final String text = data.get("BusinessName").toLowerCase();
               if (zip.contains(newText)) {
                   filteredList.add(data);
               } else if (text.contains(newText)) {
                   filteredList.add(data);
               }
           }//}
        return filteredList;
    }

  /**  @Override
    public void onLocationChanged(Location location) {
        currentLatitude=location.getLatitude();
        currentLongitude=location.getLongitude();
        Log.e(TAG,"Current latitude is "+currentLatitude);
        Log.e(TAG,"Current longitude is "+currentLongitude);
        new GetDataRestuarant().execute();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG,"Status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG,"Enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG,"Disable");
    }
**/

    private class GetDataRestuarant extends AsyncTask<Void, Void, Void> {
        //ArrayList<ArrayList<HashMap<String,String>>> newList=new ArrayList<>();
        String uid;

        ArrayList<String> testList;
        ArrayList<String> test2List;
       // HashMap<String,String> hMap;
        HashMap<String,String> newMap;
        ArrayList<HashMap<String,String>> arrList;
        String key,value;
        String urlMainList;
        String urlPointsList;
        protected void onPreExecute() {
            super.onPreExecute();
              progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            SharedPreferences preferences=context.getSharedPreferences("saveUserId",Context.MODE_PRIVATE);
            uid=preferences.getString("userId","");
            Log.e(TAG, "User id is "+uid);
             urlMainList="http://demo.oneplusrewards.com/app/api.asmx/GetBusinessData?Appid=123456789";
            urlPointsList="http://demo.oneplusrewards.com/app/api.asmx/UserPointByPhone?Appid=123456789&MID="+uid;
        }

        @Override
        protected Void doInBackground(Void... params) {
           // parseJsonForMainList(urlMainList);
            //parseJsonForPointsList(urlPointsList);

            parseJsonInOneList();

/**Log.e(TAG,"BusinessList within inner class: "+restaurantList);

          //  ArrayList<ArrayList<HashMap<String,String>>> newList=new ArrayList<>();

          //  if()
          //  newList.add(rangeRestArrayList);
            for(HashMap<String,String> hashMap:pointslist){
               // for(Map.Entry<String,String> entry:hashMap.entrySet()){
                  //  key=entry.getKey();
                 //    value=entry.getValue();
                //}
              //  if(pointslist.contains(value)){

                //}
                if(hashMap.containsKey("BusinessID")){
                    newList.add(pointslist);
                 //   rangeRestArrayList.
                  // newList.
                }
            }**/



            return null;
        }

        private void parseJsonInOneList() {
            testList=new ArrayList<>();
            test2List=new ArrayList<>();
            float rangeRound;

            urlMainList="http://demo.oneplusrewards.com/app/api.asmx/GetBusinessData?Appid=123456789";
            urlPointsList="http://demo.oneplusrewards.com/app/api.asmx/UserPointByPhone?Appid=123456789&MID="+uid;
            HttpHandler pointsHandler=new HttpHandler();
            arrList=new ArrayList<>();
            String jsonPoints=pointsHandler.makeServiceCall(urlPointsList);
            Log.e(TAG,"Response from url "+jsonPoints);
            if(jsonPoints!=null){
                try {
                    JSONArray jsonArray=new JSONArray(jsonPoints);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        bussId=jsonObject.getString("BusinessID");
                        points=jsonObject.getString("TotalPoint");
                        test2List.add(bussId);


                        newMap=new HashMap<>();
                        newMap.put(bussId,points);
                        rangeRestArrayList.add(newMap);

                      /**  if(testList.contains(businessId)){
                            points=jsonObject.getString("TotalPoint");
                            newMap.put("TotalPoint",points);
                            newMap.put("BusinessID",bussId);

                            arrList.add(newMap);
                            //    businessList.add(newMap);

                        }
                        else{
                            newMap.put("TotalPoint","0");
                        }**/
                        /**      for(HashMap<String,String> hm:businessList){
                         if(hm.containsValue(bussId)){
                         hMap.put("TotalPoint",points);
                         //  break;
                         }
                         //   else{
                         //     hMap.put("TotalPoint","0");
                         //  break;
                         //}
                         //   businessList.add(hMap);

                         Log.e(TAG,"Final arrayList is "+businessList);

                         }
                         Log.e(TAG,"Final arrayList is "+businessList);**/
                        for(int k=0;k<testList.size();k++){
                            if(testList.contains(bussId)){
                                //      hMap.put("TotalPoint",points);
                                //   hMap.put("BusinessID",bussId);
                            //    hMap.put(bussId,points);
                                //   HashMap<String, String> value=businessList.get(Integer.parseInt(businessId));
                                /** if(businessList.size()>0) {
                                 businessList.add(Integer.parseInt(businessId), hMap);
                                 } **/
                                break;
                            }

                            Log.e(TAG,"Final List is "+restaurantList);
                        }


                        /** if(bussId.contains(businessId)){
                         hMap.put("TotalPoint",points);
                         }**/
                        /**   for(HashMap<String,String> hashMap:businessList){
                         if(hashMap.containsValue(bussId)){
                         hMap.put("TotalPoint",points);
                         }
                         else{
                         hMap.put("TotalPoint","0");
                         }}
                         businessList.add(hMap);
                         Log.e(TAG,"Main ArrayList is "+businessList);**/
                        //  HashMap<String,String> pointsMap=new HashMap<>();
                        //pointsMap.put("BusinessID",bussId);
                        //pointsMap.put("TotalPoint",points);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            HttpHandler mainHandler=new HttpHandler();
            String jsonRestraunt=mainHandler.makeServiceCall(urlMainList);
            Log.e(TAG,"Response from url "+jsonRestraunt);
            if(jsonRestraunt!=null){
                try {
                    JSONArray jsonArray=new JSONArray(jsonRestraunt);
                    JSONObject jobj=jsonArray.getJSONObject(0);
                    bussType=jobj.getString("BusinessType");
                    Log.e(TAG,"BussType "+bussType);
                    if(bussType.equals("Reataurant")){
                        Log.e(TAG,"BussType is rest");
                        JSONArray business=jobj.getJSONArray("Business");
                        for(int j=0;j<business.length();j++){
                            JSONObject objBusiness=business.getJSONObject(j);
                            businessId=objBusiness.getString("BusinessID");
                            businessName=objBusiness.getString("BusinessName");
                            address1=objBusiness.getString("Address1");
                            address2=objBusiness.getString("Address2");
                            address3=objBusiness.getString("Address3");
                            contactNo=objBusiness.getString("ContactNo");
                            zipCode=objBusiness.getString("ZipCode");
                            latitude=objBusiness.getString("Latitude");
                            longitude=objBusiness.getString("Longitude");
                            website=objBusiness.getString("Website");
                            logo=objBusiness.getString("Logo");
                            menuLink=objBusiness.getString("MenuLink");
                            orderLink=objBusiness.getString("OrderLink");
                            timing=objBusiness.getString("Timing");
                            image=objBusiness.getString("Img");
                            reward=objBusiness.getString("Reward");
                          //  if(j==0||j==1){
                                distance(latitude,longitude);
                                //   distList.add(mileDist);
                            //}
                            testList.add(businessId);
                     //       HashMap<String,String> hashMap=new HashMap<>();
                        HashMap<String,String> hMap=new HashMap<>();
                           hMap.put("BusinessID",businessId);
                            hMap.put("BusinessName",businessName);
                            hMap.put("Address1",address1);
                            hMap.put("Address2",address2);
                            hMap.put("Address3",address3);
                            hMap.put("ContactNo",contactNo);
                            hMap.put("ZipCode",zipCode);
                            hMap.put("Latitude",latitude);
                            hMap.put("Longitude",longitude);
                            hMap.put("Website",website);
                            hMap.put("Logo",logo);
                            hMap.put("MenuLink",menuLink);
                            hMap.put("OrderLink",orderLink);
                            hMap.put("Timing",timing);
                            hMap.put("Img",image);
                            hMap.put("Reward",reward);
                            hMap.put("Distance",distString);
                            //rangeRestArrayList.add(hMap);
                          //  for(int i=0;i<test2List.size();i++){
                            if(test2List.contains(businessId)){
                                bussId=businessId;
                                String uPoint=rangeRestArrayList.get(j).get(bussId);
                                hMap.put("TotalPoint",uPoint);
                              //  break;
                            }
                            //}
                            else {
                               hMap.put("TotalPoint","0");
                            }
                     /**    for(HashMap<String,String> hm:rangeRestArrayList) {
                             if (hm.containsKey(businessId)) {

                                 String key = bussId;
                                 String uPoint = newMap.get(key);
                                 hMap.put("TotalPoint", uPoint);
                             }
                         }**/
                  /**   for(HashMap<String,String> hm:rangeRestArrayList ){
                         hm.get(bussId);
                         /**if(bid.equals(businessId)){
                             String uPoint=newMap.get(bid);
                             hMap.put("TotalPoint",uPoint);
                         }**/
                     //}
                       /**   if(businessId.equals(bussId)){
                              String uPoint=newMap.get(bussId);
                              hMap.put("TotalPoint",uPoint);
                          }**/
                           if(SelectRange.seekBProgress>=roundoff) {
                                restaurantList.add(hMap);

                            }
                     /**       SharedPreferences range=getContext().getSharedPreferences("mypref",Context.MODE_PRIVATE);
                            rangeRound= range.getFloat("progressKey", 0.0f);
                            if(rangeRound==0.0f){
                                if(SelectRange.seekBaProgress>=round){
                                    restaurantList.add(hMap);
                                }
                            }
                            else{
                                if(rangeRound>=round){
                                    restaurantList.add(hMap);
                                }
                            }**/
                         //   if(SelectRange.seekBaProgress>=round){
                           //     restaurantList.add(hMap);
                            //}
                         /**   if(SelectRange.seekBarProgess>distanceInt){
                                restaurantList.add(hMap);
                            }**/


                        }
                      /**  if(businessList.size()==0){
                            Toast.makeText(context,"Sorry, no reataurants within this range",Toast.LENGTH_LONG).show();
                        }**/
                    }
                   /** if(restaurantList.isEmpty()){
//                      Toast.makeText(context,"Sorry...",Toast.LENGTH_SHORT).show();
                       // Log.e(TAG,"No rest found");

                          isCancelled();
                         // isViewShown=true;
                      //  getView()!=null;
                   //     setUserVisibleHint(true);


                    }**/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }


        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            //   if(progressDialog.isShowing()) {
            //     progressDialog.dismiss();


            // adapterRest = new ListViewAdapter(getActivity(), restArrayList);
         //   adapterRest=new ListViewAdapter(getActivity(),rangeRestArrayList);
         /**   if(pointslist!=null){
            dataAdapter=new DataAdapter(context,rangeRestArrayList,pointslist);}
            else {
                dataAdapter=new DataAdapter(context,rangeRestArrayList);
            }**/
          /**  if(businessList.size()==0){
                Toast.makeText(context,"Sorry, no reataurants within this range",Toast.LENGTH_LONG).show();
            }**/
          //  flag=0;
         // bussList= new ArrayList<>();
       //   bussList=businessList;
       //   if(businessList.size()!=0){
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
        //      if(restaurantList.isEmpty()){
          //          Log.e(TAG,"Sorry, no restaurants in given range");

             //       dataAdapter=new DataAdapter(context);
                 //  Intent intent=new Intent(context, EmptyResults.class);
                   //startActivity(intent);
                 /**  new AlertDialog.Builder(context)
                           .setTitle("No restaurants in this range")
                           .setMessage("Please increase the range")
                           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                               }
                           })
                           .create()
                           .show();**/
            //    dataAdapter = new DataAdapter(context,empty);
                //  cancel(true);
                 // GetDataRestuarant.this.cancel(false);
            //      isCancelled();
                  //return;
              //  }
            //else {
                if(restaurantList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    dataAdapter=new DataAdapter(context, (String) null);
                }
                else {
                    dataAdapter = new DataAdapter(context, restaurantList);
                    //  Log.e(TAG, "User points are " + pointslist);
                    Log.e(TAG, "Restraunt arraylist " + restaurantList);

                     sort();

                    //  }//   Fragment fragmen
                    //   if(SelectRange.seekProgress>=distanceInt){
                    //  listView.setAdapter(adapterRest);
                    //listView.setTextFilterEnabled(true);
                    recyclerView.setAdapter(dataAdapter);
                }   //  Log.e(TAG,"businesslist size in inner class: "+RestaurantFragment.businessList.size());//}
              // }

               /**   if (restaurantList.isEmpty() && MainActivity.current==0) {
                        Toast.makeText(ctx, "Sorry, no restaurants in this range", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Sorry, no restaurants in this range");
                    }**/

                    //else {
                    //      flag=1;
                    //   Toast.makeText(context,"No restaurants in this list",Toast.LENGTH_SHORT).show();
                    //    setUserVisibleHint(true);
                    //}
                    /**   if(businessList.isEmpty() && RestaurantFragment.businessList.isEmpty()){
                     //    Log.e(TAG,"BusinessList inside inner class is "+businessList);

                     Toast.makeText(context,"Sorry, no rest within this range",Toast.LENGTH_SHORT).show();
                     // if()
                     }**/

                    // if(RestaurantFragment.isUserActiveRestaurtant && businessList.isEmpty()){
                    //   Toast.makeText(context,"Sorry, no restaurants within this range",Toast.LENGTH_SHORT).show();
                    //}
                    /**  if(bussList.isEmpty()){
                     Fragment fragment
                     }**/
                }
        }
        }

   // }//}
 //   GetDataRestuarant getDataRestuarant=new GetDataRestuarant();
    private boolean isFragmentLoaded=false;

 /**   public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG,"Arraylist inside visible hint "+restaurantList);
        //GetDataRestuarant getData=new GetDataRestuarant();

      // Log.e(TAG,"BusinessList outside inner class is "+RestaurantFragment.bussList);
       // isUserActiveRestaurtant=isVisibleToUser;
   /**     if(isVisibleToUser && isResumed()){

            new GetDataRestuarant().execute();
          //  Toast.makeText(context,"Sorry, no restaurants within this range",Toast.LENGTH_SHORT).show();
        }**/
  /** if(getView()!=null){
      isViewShown=true;
      // new GetDataRestuarant().execute();
       if(isVisibleToUser && restaurantList.isEmpty()){
           //   if(businessList.isEmpty()){
           Toast.makeText(context,"Sorry, no restaurants within this range",Toast.LENGTH_SHORT).show();
           Log.e(TAG,"Arraylist inside isvisibletouser "+restaurantList);
         //   isFragmentLoaded=true;
          // Log.e(TAG,"Sorry no rest found");
       //     Intent intent=new Intent(context,EmptyResults.class);
         //   startActivity(intent);
           //   if(restaurantList.isEmpty()) {
       //    Log.e(TAG, "Sorry, no restaurants within this range");
           // Log.e(TAG,"Checking arraylist is empty or not "+restaurantList.size());
           // }// }



       }

   }
   else {
       isViewShown=false;

   }

    }**/
    //RestaurantFragment fragment= (RestaurantFragment) this.getFragmentManager().findFragmentByTag("Restaurants");
    //if()

    //Fragment page=ViewPager.getCur
   /**public void onHiddenChanged(boolean hidden){
       super.onHiddenChanged(hidden);
       if(hidden){

       }
       else {
           if(businessList.size()==0){
               Toast.makeText(context,"No restaurants",Toast.LENGTH_SHORT).show();
           }

       }
   }**/

    // }
    private static String distance(String lati,String longi){
        Log.e(TAG,"Inside distance method");
        double lat=Double.parseDouble(lati);
        double lon=Double.parseDouble(longi);
        Location loc1=new Location("point A");
        loc1.setLatitude(lat);
        loc1.setLongitude(lon);
        Location loc2=new Location("point B");
      //  loc2.setLatitude(32.500000);//Kathleen, GA 31047
      //  loc2.setLongitude(-83.600000);
        loc2.setLatitude(28.518153);//iOS Test
        loc2.setLongitude(76.20569);
        //  loc2.setLatitude(33.000000);
        // loc2.setLongitude(-83.200000);
        // loc2.setLatitude(MainActivity.curLatitude);
         //loc2.setLongitude(MainActivity.curLongitude);
        //loc2.setLatitude(currLatitude);
        //loc2.setLongitude(currLongitude);
        final GlobalClass globalVariable= (GlobalClass) context.getApplicationContext();
        final double latit=globalVariable.getLatitude();
        final double longit=globalVariable.getLongitude();
     //   loc2.setLatitude(latit);
       // loc2.setLongitude(longit);
  //    loc2.setLatitude(32.621719); // saket metro station
  //    loc2.setLongitude(-85.455593);
        //loc2.setLatitude(currLatitude);
        //loc2.setLongitude(currLongitude);
       //loc2.setLatitude(33.08228);
        //loc2.setLongitude(-83.227685);
        dist=loc1.distanceTo(loc2);
        kmdist=dist/1000;
        mileDist=dist*0.000621371;
         roundoff=  (Math.round(mileDist*10.0)/10.0);
         round= (float) (Math.round(mileDist*10.0)/10.0);
        Log.e(TAG,"Distance is "+mileDist);
        distanceInt=(int)mileDist;
        distString=Double.toString(roundoff);
        // distString=Double.toString(mileDist);
        //   distanceInt=Integer.parseInt(distString);
        Log.e(TAG,"Integer type distance "+distanceInt);
        return distString;
    }
/**private String distance(String lati, String longi){
 Log.e(TAG,"Inside distance method");
 double lat=Double.parseDouble(lati);
 double lon=Double.parseDouble(longi);
 double theta=lon-currLongitude;
 double kmdist=Math.sin(deg2Rad(lat))*Math.sin(deg2Rad(currLatitude))
 +Math.cos(Math.cos(deg2Rad(lat))*Math.cos(deg2Rad(currLatitude)))*Math.cos(deg2Rad(theta));
 kmdist=Math.acos(kmdist);
 kmdist=rad2Deg(kmdist);
 kmdist=kmdist*60*1.1515;
 mileDist=kmdist*0.621371;
 double roundoff=Math.round(mileDist*100.0)/100.0;
 Log.e(TAG,"Distance is "+mileDist);
 distanceInt=(int)mileDist;
 distString=Double.toString(roundoff);
 return distString;

 }

 private double rad2Deg(double rad) {
 return (rad*180.0/Math.PI);
 }

 private double deg2Rad(double deg) {
 return (deg*Math.PI/180.0);
 }**/

    /**    private String distance(String lati, String longi) {
     Log.e(TAG,"Inside distance method");
     double lat=Double.parseDouble(lati);
     double lon=Double.parseDouble(longi);
     LatLng latLngA=new LatLng(lat,lon);
     LatLng latLngB=new LatLng(MainActivity.curLatitude,MainActivity.curLongitude);
     // LatLng latLngB=new LatLng(currLatitude,currLongitude);
     Location locationA=new Location("point A");
     locationA.setLatitude(latLngA.latitude);
     locationA.setLongitude(latLngA.longitude);
     // locationA.setLatitude(lat);
     // locationA.setLongitude(lon);
     Location locationB=new Location("point B");
     locationB.setLatitude(latLngB.latitude);
     locationB.setLatitude(latLngB.longitude);
     //locationB.setLatitude(MainActivity.curLatitude);
     //locationB.setLongitude(MainActivity.curLongitude);
     dist=locationA.distanceTo(locationB);
     kmdist=dist/1000;
     mileDist=dist*0.000621371;
     double roundoff=Math.round(mileDist*100.0)/100.0;
     Log.e(TAG,"Distance is "+mileDist);
     distanceInt=(int)mileDist;
     distString=Double.toString(roundoff);
     // distString=Double.toString(mileDist);
     //   distanceInt=Integer.parseInt(distString);
     Log.e(TAG,"Integer type distance "+distanceInt);
     return distString;

     }**/

    public void sort() {
        Collections.sort(restaurantList, new Comparator<HashMap<String,String>>() {
            @Override
            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                //return Double.parseDouble(o1.get("Distance")).compareTo(Double.parseDouble(o2.get("Distance")));
                //return o1.get("Distance").compareTo(o2.get("Distance"));
          //  return o1.getDistance().compareTo(o2.getDistance());
                double distance1= Double.parseDouble(o1.get("Distance"));
                double distance2=Double.parseDouble(o2.get("Distance"));
                Comparable v1=distance1;
                Comparable v2=distance2;
               return v1.compareTo(v2);
            }


        });

    }




}

