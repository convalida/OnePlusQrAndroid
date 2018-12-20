package com.convalida.user.jsonparsing;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    //{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private View navHeader;
    static int tabPosition;
    private DrawerLayout mDrawerLayout;
    ProgressDialog progressDialog;
    static double curLatitude;
    static double curLongitude;
    static double currLatitude;
    static double currLongitude;
    static String ID = "BusinessID";
    static String NAME = "BusinessName";
    static String LATITUDE = "Latitude";
    static String LONGITUDE = "Longitude";
    static String LOGO = "Logo";
    static String ADDRESS1 = "Address1";
    static String ADDRESS2 = "Address2";
    static String ADDRESS3 = "Address3";
    static String CONTACT = "ContactNo";
    static String ZIP = "ZipCode";
    static String WEBSITE = "Website";
    static String TIMING = "Timing";
    static String IMAGES = "Img";
    static String REWARDS = "Reward";
    static String MENU = "MenuLink";
    static String POINT = "TotalPoint";
    static String ORDER = "OrderLink";
    static String DAY = "Day";
    static String OPENTIME = "OpenTime";
    static String CLOSETIME = "CloseTime";
    static String DISTANCE = "Distance";
    private static final int REQUEST_APP_SEETINGS=168;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    TextView userName, userMail, userLName;
    String uName, uMail;
    //   final GlobalClass globalVariable;
    ImageView userPhoto;
    Context context;
    ListView listView;
    NavigationView navigationView;
    static int current;
    //SearchAdapter searchAdapter;
    private static String urlMain = "http://demo.oneplusrewards.com/app/api.asmx/GetBusinessData?Appid=123456789";
    //  private static String urlMain="http://localhost:10945/App/Api.asmx/GetBusinessData?Appid=123456789";
    private static final String TAG = "MainActivity";
 //   static ListViewAdapter listViewAdapter;
    static ArrayList<HashMap<String, String>> mainArrayList;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final String requiredPermission=Manifest.permission.ACCESS_FINE_LOCATION;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;

    private boolean mRequestLocationUpdates = false;
    private LocationRequest mLocationRequest;
    //CoordinatorLayout coordinatorLayout;
    LinearLayout linearLayout;
    HashMap<String, String> result = new HashMap<>();
    ArrayList<HashMap<String, String>> data;
    private String[] columns = new String[]{"_id", "text"};
    private SearchView searchView;
    private SearchManager searchManager;
    String provider;
    //  private SuggestionsAdapter suggestionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  new GetDataMain().execute();
        linearLayout= (LinearLayout) findViewById(R.id.main);
        if(!isNetworkAvailable()){

      /**   Snackbar snackbar=Snackbar.make(linearLayout,"Internet connection is required",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=getIntent();
                    finish();
                    startActivity(intent);
                }
            });
         snackbar.show();**/
        /**    AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setMessage("Internet connection is required");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            AlertDialog dialog=alertDialog.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();**/
            new AlertDialog.Builder(this)
                    .setTitle("Internet connection is required")
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
        else{
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mainArrayList = new ArrayList<>();
        // globalVariable= (GlobalClass)getBaseContext();
        checkLocationPermission();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "permission granted");
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location l = locationManager.getLastKnownLocation(provider);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10,this);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation != null) {
            onLocationChanged(bestLocation);
        }
        /**  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
         locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
         //   locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5,this);
         //  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
         // Define the criteria how to select the locatioin provider -> use
         // default
         Criteria criteria = new Criteria();
         provider = locationManager.getBestProvider(criteria, false);
         Location location = locationManager.getLastKnownLocation(provider);
         if(location!=null){
         onLocationChanged(location);
         }
         else{
         Toast.makeText(getApplicationContext(),"Could'nt get location",Toast.LENGTH_LONG).show();
         }**/


        //  navigationView= (NavigationView) findViewById(R.id.navigation_view);
        // ActionBar actionBar=getSupportActionBar();
        //  actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        // actionBar.setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

       viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
if(position==0){
    if(RestaurantFragment.restaurantList.isEmpty()){
        Log.e(TAG,"arraylist1 is empty");
    }
}

}
         //   }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
      current= viewPager.getCurrentItem();
 /**    if(current==0){
         if(RestaurantFragment.restaurantList.isEmpty()){
             Log.e(TAG,"arraylist1 is empty");
         }
     }
     if(current==1){
         if(BarFragment.rangeBarList.isEmpty()){
             Log.e(TAG,"arraylist2 is empty");
         }
     }**/

        // mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        //navHeader=navigationView.getHeaderView(0);
        //userName= (TextView) navHeader.findViewById(R.id.userName);
        //userLName= (TextView) navHeader.findViewById(R.id.userLastName);
        //userMail= (TextView) navHeader.findViewById(R.id.userId);
        //loadNavHeader();
        //if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //}
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        /**    if (checkPlayServices()) {
         buildGoogleApiClient();
         }**/
        //     displayLocation();

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        data = RestaurantFragment.rangeRestArrayList;

    }}


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
               // new AlertDialog.Builder(this)

                 //       .setTitle("Location permission required")
                   //     .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                     //       @Override
                       //     public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                         //   }
                        //})
                        //.create()
                        //.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    //  locationManager.requestLocationUpdates(provider,400,1,this);
                      //return;
                      finish();
                      startActivity(getIntent());
                  }

                      //   Toast.makeText(MainActivity.this,"Location permission is required",Toast.LENGTH_SHORT).show();
                      // finish();
                      // startActivity(getIntent());
                //    else{
              //        Intent intent =new Intent(MainActivity.this,LocationPermission.class);
                  //    startActivity(intent);
                  //}


                               }
                               else {
                  /**  if(isFirstTimeAskingPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){

                        firstTimeAskingPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, false);
                        new AlertDialog.Builder(this)
                                //  .setTitle("Location permission required")
                                .setTitle("User's current location is required")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                                    }
                                })
                                .create()
                                .show();
                    }
                    else {**
                       // Intent intent=new Intent(MainActivity.this,Login.class);
                        //startActivity(intent);
                     /**   new AlertDialog.Builder(this)
                                //  .setTitle("Location permission required")
                                .setTitle("User's current location is required")
                                .setMessage("Please add them in Settings -> Installed Apps -> Permissions -> Location ")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                     //   ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                                   Intent intent=new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri=Uri.fromParts("package",getPackageName(),null);
                                        intent.setData(uri);
                                       // startActivity(intent);
                                        startActivityForResult(intent,REQUEST_APP_SEETINGS);
                                  //      startActivity(getIntent());

                              /**    Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package"+getPackageName()));
                                  intent.addCategory(Intent.CATEGORY_DEFAULT);
                                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                  startActivityForResult(intent, REQUEST_APP_SEETINGS);**/
                                    /**}
                                })
                                .create()
                                .show();**/
                                    Intent intent =new Intent(MainActivity.this,LocationPermission.class);
                                    startActivity(intent);




                   /**     if(grantResults.length>0 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                            finish();
                            startActivity(getIntent());

                        }}**/
                     // startActivity(getIntent());
                   // if(ActivityCompat.requestPermissions(MainActivity.this,);)


                    }

                   // if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                     //   finish();
                       // startActivity(getIntent());
                    //}
                }

               // return;
            }
        }

 //   }
   /** protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==REQUEST_APP_SEETINGS){
            Intent in=new Intent(this,MainActivity.class);
            startActivity(in);
        }
    }**/

  /**  @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean hasPermissions(String permisssion){
       // for(String permission:permisssions){
            if(PackageManager.PERMISSION_GRANTED!=checkSelfPermission(permisssion)){
                return false;
           }
            return true;
        //}
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==REQUEST_APP_SEETINGS){
            if(hasPermissions(requiredPermission)){
                Toast.makeText(getApplicationContext(),"Permission granted",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Permission not granted",Toast.LENGTH_SHORT).show();
            }
        }
    }**/

    public static void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("PERMISSION_FIRST_TIME",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(permission,isFirstTime).apply();
    }

    public static boolean isFirstTimeAskingPermission(Context context,String permission){
        return context.getSharedPreferences("PERMISSION_FIRST_TIME",MODE_PRIVATE).getBoolean(permission,true);
    }


    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           Toast.makeText(getApplicationContext(),"Couldn't accesslocation",Toast.LENGTH_LONG).show();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation!=null){
           curLatitude=mLastLocation.getLatitude();
            curLongitude=mLastLocation.getLongitude();
           // curLatitude=0.0;
           // curLongitude=0.0;
          //  Toast.makeText(MainActivity.this,""+curLatitude+", "+curLongitude,Toast.LENGTH_LONG).show();
            Log.e(TAG,"Latitudes and longitudes are: "+curLatitude+", "+curLongitude);
        }
        else{
            Toast.makeText(MainActivity.this,"Couldn't get location. Make sure that location is enabled on device",Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI=GoogleApiAvailability.getInstance();
        int result=googleAPI.isGooglePlayServicesAvailable(this);
        if(result!=ConnectionResult.SUCCESS){
            if(googleAPI.isUserResolvableError(result)){
                googleAPI.getErrorDialog(this,result,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"This device is not supported",Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
   // int resultCode= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
     //   if(resultCode!=ConnectionResult.SUCCESS){
       //     if()
        //}
    }

    protected void onStart(){
        super.onStart();
        if(mGoogleApiClient!=null){
            mGoogleApiClient.connect();
        }
    }
    protected void onResume(){
        super.onResume();
       // if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
         //   locationManager.requestLocationUpdates(provider,400,1,this);
        //}
        checkPlayServices();
    }
 /**   protected void onPause(){
        super.onPause();
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            locationManager.removeUpdates(this);
        }
    }**/

  /**  protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }**/

 /**   private void loadNavHeader() {
        String loginurl="http://demo.oneplusrewards.com/app/api.asmx/UserDataByPhone?Appid=123456789&CustomerPhone="+Register.mobile;

        JsonObjectRequest jsonRequest=new JsonObjectRequest(Request.Method.GET, loginurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    userName.setText(response.getString("FirstName"));
                    userLName.setText(response.getString("LastName"));
                    userMail.setText(response.getString("EmailID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("tag",error.getMessage());
                Toast.makeText(getApplicationContext(),"Error getting data",Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance(this).addToRequestQueue(jsonRequest);
    }**/


    public boolean onOptionsItemSelected(MenuItem item){

       // super.onCreateOptionsMenu((Menu) item);
        switch (item.getItemId()){
            case R.id.navigation_profile:
             //   Toast.makeText(getApplicationContext(),"Open Profile activity",Toast.LENGTH_LONG).show();
                Intent intent1=new Intent(MainActivity.this,EditProfile.class);
                startActivity(intent1);
                break;
            case R.id.navigation_search:
                Intent intent=new Intent(MainActivity.this, SelectRange.class);
                startActivity(intent);
              //  Toast.makeText(getApplicationContext(),"Open seerkbar",Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
               SharedPreferences preferences=getSharedPreferences("saveUserId",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.remove("userId");
                editor.commit();

             //   Message myMessage=new Message();
               // myMessage.obj="NOTSUCCESS";

             Intent logoutIntent=new Intent(MainActivity.this,Login.class);
                startActivity(logoutIntent);
                 Toast.makeText(getApplicationContext(),"User successfully logged out",Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

       /** int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }**/
        return super.onOptionsItemSelected(item);
    }
    ViewPagerAdapter adapter;
    private void setupViewPager(ViewPager viewPager) {
         adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RestaurantFragment(),"Restaurants");
        adapter.addFrag(new BarFragment(),"Bar");
        adapter.addFrag(new GroceryFragment(),"Groceries");
        adapter.addFrag(new GasFragment(),"Gas Station");
        viewPager.setAdapter(adapter);

    }

    boolean doubleBackToExitPressedOnce=false;
  //  public void onBackPressed(){
    //    moveTaskToBack(true);
   // }
  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  public void onBackPressed(){
      if(doubleBackToExitPressedOnce){
          finishAffinity();
         // super.onBackPressed();
     //     finish();
        /**moveTaskToBack(true);
          android.os.Process.killProcess(android.os.Process.myPid());
          System.exit(1);**/
/**moveTaskToBack(true);
 // finish();
 // Intent intent= getIntent(Intent.ACTION_MAIN)
     Intent intent=new Intent(Intent.ACTION_MAIN);
        //  intent.addCategory(Intent.CATEGORY_HOME);
        //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
     // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      finish();
      //intent.addCategory(Intent.CATEGORY_HOME);
      //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
      //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);**/

          //    finishAndRemoveTask();

          return;

         // this.finish();
      /**    Intent a=new Intent(Intent.ACTION_MAIN);
       //   a.addCategory(Intent.CATEGORY_HOME);
          a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(a);**/

          //finish();
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
  public void onDestroy(){
      super.onDestroy();
  }
     /**   if(getFragmentManager().getBackStackEntryCount()==0){
            this.finish();
        }
        else{
            getFragmentManager().popBackStack();
        }**/
     //finish();

      /**  new AlertDialog.Builder(this)
                .setTitle("Really exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton("No",null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });**/


    

   /** @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }**/

 /**   @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG,"Connection failed: ConnectionResult.getErrorCode = "+connectionResult.getErrorCode());
    }**/

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

            currLatitude = location.getLatitude();
            currLongitude = location.getLongitude();
            final GlobalClass globalVariable= (GlobalClass) getApplicationContext();
            globalVariable.setLatitude(location.getLatitude());
            globalVariable.setLongitude(location.getLongitude());
            Log.e(TAG, "Current Latitude is: " + currLatitude);
            Log.e(TAG, "Current Longitude is: " + currLongitude);
        }
        else{
            Toast.makeText(getApplicationContext(),"Waiting for current location",Toast.LENGTH_LONG).show();
        }
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


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList=new ArrayList<>();
        private final List<String> mFragmentTitleList=new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFrag(Fragment fragment, String resaurants) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(resaurants);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }

   /** public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }**/

  /** public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.drawer,menu);
             MenuItem item=menu.findItem(R.id.search);
         searchView= (SearchView) MenuItemCompat.getActionView(item);
       // setSearchTextColor(searchView);
        //setSearchIcons(searchView);
        searchView.getQuery();
       // searchView.setQueryHint(Html.fromHtml("<font color= #ffffff>"+getResources().getString(R.string.search_here) + "</font>"));
        //final SearchView searchView=MenuItemCompat.getActionView()
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
       /** Object[] temp=new Object[] {0,"default"};
        final MatrixCursor cursor=new MatrixCursor(columns);
        for(int i=0;i<RestaurantFragment.rangeRestArrayList.size();i++){
            temp[0]=i;
            temp[1]=RestaurantFragment.rangeRestArrayList.get(i).get("BusinessName");
            cursor.addRow(temp);
        }
        suggestionsAdapter=new SuggestionsAdapter(MainActivity.this,cursor,RestaurantFragment.rangeRestArrayList);
        searchView.setSuggestionsAdapter(suggestionsAdapter);**/
    /**    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG,"Query submitted: "+query);
              /**  if(!TextUtils.isEmpty(query)){
                    //suggestionsAdapter.getFilter().filter(query.toString());
                }**/
         //       return true;
              /**  Log.e(TAG,"Inside query submit");
                if(MainActivity.tabPosition==0){
                    Log.e(TAG,"Active tab is 0");

                   // ArrayList<String> indexArrayList = new ArrayList<String>();
                    for(HashMap<String,String> map:RestaurantFragment.rangeRestArrayList){
                        for(Map.Entry<String,String> mapEntry:map.entrySet()){

                            String value=mapEntry.getValue();
                            if(value.equals(query)){
                                Log.e(TAG,"Yes such restaurant exists");
                                Log.e(TAG,"Value: "+value);
                                int index=RestaurantFragment.rangeRestArrayList.indexOf(query);
                             //   RestaurantFragment.rangeRestArrayList.get(value).get
                                Log.e(TAG,"Index of value is: "+index);
                           //     searchAdapter=new SearchAdapter(MainActivity.this,RestaurantFragment.rangeRestArrayList);
                                Intent intent=new Intent(MainActivity.this,OnClickRestaurant.class);
                              //  intent.putExtra("BusinessName",)
                                startActivity(intent);
                            }
                            else {
                              //  Log.e(TAG,"No such restaurant exists");
                            }
                          //  Log.e(TAG," "+value);
                        }
                    }
                }
                return false;**/
       /**     }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG,"Query change "+newText);
//                RestaurantFragment.adapterRest.getFilter().filter(newText);
               // Intent intent=new Intent(MainActivity.this,ListViewAdapter.class);
                //startActivity(intent);
                  //  loadHistory(newText);
               /** Object[] temp=new Object[] {0,"default"};
                final MatrixCursor cursor=new MatrixCursor(columns);
                Log.e(TAG,"Size of fragment list"+RestaurantFragment.rangeRestArrayList.size());
                for(int i=0;i<RestaurantFragment.rangeRestArrayList.size();i++){
                    temp[0]=i;
                    temp[1]=RestaurantFragment.rangeRestArrayList.get(i).get("BusinessName");
                    cursor.addRow(temp);
                }
                suggestionsAdapter=new SuggestionsAdapter(MainActivity.this,cursor,RestaurantFragment.rangeRestArrayList);
                searchView.setSuggestionsAdapter(suggestionsAdapter);**/
/**
              RestaurantFragment.adapterRest.getFilter().filter(newText);
           //     RestaurantFragment.listView.invalidate();
               /** if(TextUtils.isEmpty(newText)){
                    RestaurantFragment.listView.clearTextFilter();
                }
                else {
                    RestaurantFragment.listView.setFilterText(newText.toString());
                }**/

         /**       Log.e(TAG,"Size of Restaurant Fragment "+RestaurantFragment.rangeRestArrayList.size());
                return true;
            }
        });
      /**  searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                result=data.get(position);
                String suggestion=getSuggestion(position);
                Intent intent=new Intent(MainActivity.this,OnClickRestaurant.class);
                intent.putExtra("BusinessName",result.get(NAME));
                intent.putExtra("ContactNo",result.get(CONTACT));
                intent.putExtra("Website",result.get(WEBSITE));
                intent.putExtra("Timing",result.get(TIMING));
                intent.putExtra("Latitude",result.get(LATITUDE));
                intent.putExtra("Longitude",result.get(LONGITUDE));
                startActivity(intent);
                return true;
            }
        });**/
   //     return true;
    //}

   /** private String getSuggestion(int position) {
        Cursor cursor= (Cursor) searchView.getSuggestionsAdapter().getItem(position);
        String suggest=cursor.getString(cursor.getColumnIndex("text"));
        return suggest;
    }

    private void setSearchIcons(SearchView searchView) {

    }

    private void setSearchTextColor(SearchView searchView) {
    }

    private void loadHistory(String newText) {
    }

    private void search(SearchView searchView) {
    }**/


}