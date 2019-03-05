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
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
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
    private static final int REQUEST_APP_SEETINGS = 168;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int MY_PERMISSION_CAMERA = 98;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 23;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
    TextView userName, userMail, userLName;
    String uName, uMail;
    //   final GlobalClass globalVariable;
    ImageView userPhoto;
    Context context;
    ListView listView;
    NavigationView navigationView;
    static int current;
    //SearchAdapter searchAdapter;

    //  private static String urlMain="http://localhost:10945/App/Api.asmx/GetBusinessData?Appid=123456789";
    private static final String TAG = "MainActivity";
    //   static ListViewAdapter listViewAdapter;
    static ArrayList<HashMap<String, String>> mainArrayList;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final String requiredPermission = Manifest.permission.ACCESS_FINE_LOCATION;
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
        linearLayout = (LinearLayout) findViewById(R.id.main);
        if (!isNetworkAvailable()) {

            new AlertDialog.Builder(this)
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


        } else {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mainArrayList = new ArrayList<>();
            // globalVariable= (GlobalClass)getBaseContext();
            checkLocationPermission();
            try {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                String locationProvider=LocationManager.NETWORK_PROVIDER;
                assert locationManager != null;
                  Location lastLocation = locationManager.getLastKnownLocation(locationProvider);
                    onLocationChanged(lastLocation);

            }
            catch (SecurityException e){
                e.printStackTrace();
            }
          /**  if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            String locationProvider=LocationManager.NETWORK_PROVIDER;
            assert locationManager != null;
            if(locationManager.getLastKnownLocation(locationProvider)!=null) {
                Location lastLocation = locationManager.getLastKnownLocation(locationProvider);
                onLocationChanged(lastLocation);
            }
            else{
                Toast.makeText(getApplicationContext(),"Last known location is null",Toast.LENGTH_LONG).show();
            }**/



            // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            /**List<String> providers = locationManager.getProviders(true);
             Location bestLocation = null;
             for (String provider : providers) {
             Location l = locationManager.getLastKnownLocation(provider);
             //            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10,this);
             if (l == null) {
             continue;}
             if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
             bestLocation = l;
             }
             }
             if (bestLocation != null) {
             onLocationChanged(bestLocation);
             }**/

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
       /**     assert locationManager != null;
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                Log.e(TAG, location.toString());
                this.onLocationChanged(location);
            }**/

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
                    if (position == 0) {
                        if (RestaurantFragment.restaurantList.isEmpty()) {
                            Log.e(TAG, "arraylist1 is empty");
                        }
                    }

                }


                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            current = viewPager.getCurrentItem();
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

        }

    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {

            return true;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        finish();
                        startActivity(getIntent());
                        /**  Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                         if (location != null) {
                         Log.e(TAG, location.toString());
                         this.onLocationChanged(location);
                         }**/

                    }

                } else {
                    Intent intent = new Intent(MainActivity.this, LocationPermission.class);
                    startActivity(intent);


                }

            }
            case MY_PERMISSION_CAMERA:
                if (MY_PERMISSION_CAMERA == requestCode) {
                    for (int i = 0, len = permissions.length; i < len; i++) {
                        String permission = permissions[i];
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            boolean showRationale = shouldShowRequestPermissionRationale(permission);
                            if (!showRationale) {
                            //    displayNeverAskDialog();
                                Intent intent=new Intent(MainActivity.this,CameraPermission.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                            startActivity(intent);
                        }
                    }
                  /**  istyleyourf(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                            //   finish();
                            //  startActivity(getIntent());
                            //  launchScanner();
                            Intent intent=new Intent(EditProfile.this, ScannerActivity.class );
                            startActivity(intent);

                        }
                    }
                    else{
                        Intent intent = new Intent(EditProfile.this,CameraPermission.class);
                        startActivity(intent);
                    }**/
                }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
            startActivity(intent);
        }
    }


    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Couldn't accesslocation", Toast.LENGTH_LONG).show();
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
        if (mLastLocation != null) {
            curLatitude = mLastLocation.getLatitude();
            curLongitude = mLastLocation.getLongitude();
            // curLatitude=0.0;
            // curLongitude=0.0;
            //  Toast.makeText(MainActivity.this,""+curLatitude+", "+curLongitude,Toast.LENGTH_LONG).show();
            Log.e(TAG, "Latitudes and longitudes are: " + curLatitude + ", " + curLongitude);
        } else {
            Toast.makeText(MainActivity.this, "Couldn't get location. Make sure that location is enabled on device", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "Google Play services required", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;

    }

    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
      //  assert locationManager != null;
        //  if(checkLocationPermission()) {
        // if (Manifest.permission.ACCESS_FINE_LOCATION.equals(PackageManager.PERMISSION_GRANTED)) {
      /**  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }**/
     /**   Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.e(TAG, location.toString());
            this.onLocationChanged(location);
        }**/
    }

    protected void onResume() {
        super.onResume();

        checkPlayServices();


     // checkLocationPermission();
       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
    // if(checkLocationPermission()) {
      //  checkLocationPermission();
        if(locationManager!=null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 10, this);
        }

    }


  /**  protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }**/


protected void onPause(){
    super.onPause();
    if(locationManager!=null) {
        locationManager.removeUpdates(this);
    }
}

    @RequiresApi(api = Build.VERSION_CODES.M)
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

            case R.id.scanQRCode:
                checkForCameraPermission();


            default:
                return super.onOptionsItemSelected(item);
        }


        return super.onOptionsItemSelected(item);
    }

    private boolean checkForCameraPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }

             return false;
        }
        else {
           Intent intent=new Intent(MainActivity.this,ScannerActivity.class);
            startActivity(intent);
            return true;
        }
    }

    private void displayNeverAskDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("We need permission to access your camera to scan the QR code. Please permit the permission through " +
                                "Settings screen.\n\nSelect Permissions -> Enable permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
           dialogInterface.dismiss();
           Intent intent=new Intent();
           intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri=Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
              //  startActivity(intent);
                startActivityForResult(intent,3);
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }



    ViewPagerAdapter adapter;
    private void setupViewPager(ViewPager viewPager) {
         adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RestaurantFragment(),"Restaurants");
        adapter.addFrag(new BarFragment(),"Bars");
        adapter.addFrag(new GroceryFragment(),"Groceries");
        adapter.addFrag(new GasFragment(),"Gas Stations");
        viewPager.setAdapter(adapter);

    }

    boolean doubleBackToExitPressedOnce=false;

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  public void onBackPressed(){
      if(doubleBackToExitPressedOnce){
          finishAffinity();
          return;

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
        //    Toast.makeText(getApplicationContext(),"Waiting for current location",Toast.LENGTH_LONG).show();
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





}