package mawaqaa.parco.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import mawaqaa.parco.Fragment.PinLocationFragment;
import mawaqaa.parco.InterFace.PinButtonChange;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;


public class MainActivity extends MainBaseActivity implements OnMapReadyCallback, View.OnClickListener, PinButtonChange {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static String TAG = "MainActivity";
    SupportMapFragment mapFrag;
    public static GoogleMap mGoogleMap;
    LocationManager locationManager;
    ImageView myLocationImage;
    static final int REQUEST_LOCATION = 99;
    public static double CustomerLatitude = 29.367706;
    public static double CustomerLongitude = 47.979637;
    public static double PickupLatitude;
    public static double PickupLongitude;
    Fragment fragment;
    public static boolean isPinSelected = false;
    ImageView menu_btn;
    public static LinearLayout main_container;
    Intent i;
    RequestQueue requestQueue;
    private ProgressDialog pDialog;
    PinButtonChange clicked;
    public static String AddressString;
    Marker myMarker;
    SharedPrefsUtils myPref;
    public static int AreaID;
    public static LatLng middle;
    public static int PickupID;
    public static int CarID;
    public static boolean IstAdditionalService;
    public static JSONArray LsAdditionalService;

    private final static String LINE = "elfjD~a}uNOnFN~Em@fJv@tEMhGDjDe@hG^nF??@lA?n@IvAC`Ay@A{@DwCA{CF_EC{CEi@PBTFDJBJ?V?n@?D@?A@?@?F?F?LAf@?n@@`@@T@~@FpA?fA?p@?r@?vAH`@OR@^ETFJCLD?JA^?J?P?fAC`B@d@?b@A\\@`@Ad@@\\?`@?f@?V?H?DD@DDBBDBD?D?B?B@B@@@B@B@B@D?D?JAF@H@FCLADBDBDCFAN?b@Af@@x@@";
    private final static String OVAL_POLYGON = "}wgjDxw_vNuAd@}AN{A]w@_Au@kAUaA?{@Ke@@_@C]D[FULWFOLSNMTOVOXO\\I\\CX?VJXJTDTNXTVVLVJ`@FXA\\AVLZBTATBZ@ZAT?\\?VFT@XGZAP";
    private final static int ALPHA_ADJUSTMENT = 0x77000000;
    public static JSONArray LstAreaList;
    public static ArrayList<LatLng> triangle = new ArrayList<>();

    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (myPref.getStringPreference(MainActivity.this, appConstant.language_selected).equals(
                appConstant.HP_ENGLISH) || myPref.getStringPreference(MainActivity.this, appConstant.language_selected).isEmpty()) {
            myPref.setStringPreference(getApplicationContext(), appConstant.language_selected,
                    appConstant.HP_ENGLISH);
            Log.e(TAG, "Set locale to Arabic");
            setLanguage(appConstant.HP_ENGLISH);
        } else {
            myPref.setStringPreference(getApplicationContext(), appConstant.language_selected,
                    appConstant.HP_ARABIC);
            setLanguage(appConstant.HP_ARABIC);
            Log.e(TAG, "Set locale to English");
        }

        setContentView(R.layout.main_activity);
        LstAreaList = new JSONArray();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        myLocationImage = (ImageView) findViewById(R.id.map_marker);
        clicked = (PinButtonChange) this;
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        menu_btn = findViewById(R.id.menu_btn);
        main_container = findViewById(R.id.main_container);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        requestQueue = Volley.newRequestQueue(this);
        GetAreas();
        fragment = new PinLocationFragment();
        pushFragments(fragment, false, false);

        menu_btn.setOnClickListener(this);

        showDialogWelcome();

        DeviceRegistration();
    }


    private void GetAreas() {
        String UserId = myPref.getStringPreference(MainActivity.this, appConstant.CustomerID);

        JSONObject jo = new JSONObject();
        try {
            jo.put(appConstant.APIKey, 123456);
            jo.put("UserID", UserId);
            jo.put("LanguageID", 1);

            Log.d(TAG, jo.toString());

            showDialog();
            JsonObjectRequest postRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    UrlClass.GetAreas,

                    jo,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                int IsSuccess = response.getInt("APIStatus");
                                String msg = response.getString("APIMessage");
                                hideDialog();
                                if (IsSuccess == 1) {
                                    LstAreaList = response.getJSONArray("LstAreaList");
                                    fillArea(LstAreaList);
                                } else {
                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //   Handle Error
                            hideDialog();
                            Log.e("Wt", error.toString());
                        }
                    });
            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                    2,
                    2));
            requestQueue.add(postRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void DeviceRegistration() {
        String CustomerID = myPref.getStringPreference(this, appConstant.CustomerID);
        String DeviceToken = myPref.getStringPreference(this, appConstant.deviceToken_KEY);
        String IMEI = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put("CustomerID", CustomerID);
                jo.put("DeviceToken", DeviceToken);
                jo.put("DeviceID", IMEI);
                jo.put("DevicePlatform", 0);
                jo.put("UserTypeID", 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.DeviceRegistration, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideDialog();
                        }
                    });


                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                            6000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(jsonRequest);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }

    }

    private void showDialogWelcome() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.wlc_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button lets_go = dialog.findViewById(R.id.lets_go);
        TextView user_name = dialog.findViewById(R.id.user_name);
        String UserName = myPref.getStringPreference(this, appConstant.UserName);
        user_name.setText(getString(R.string.congrats) + " " + UserName);
        lets_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//            ??
//            if (location != null) {
//                CustomerLatitude = location.getLatitude();
//                CustomerLongitude = location.getLongitude();
//                getCurrentLocation();
//            } else {
//            }

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                CustomerLatitude = location.getLatitude();
                                CustomerLongitude = location.getLongitude();
                                getCurrentLocation(CustomerLatitude,CustomerLongitude);
                            }
                        }
                    });
        }
    }

    public void getCurrentLocation(double lat,double lon) {
        if (mGoogleMap != null) {
            fillArea(LstAreaList);
            isPinSelected = false;
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(CustomerLatitude, CustomerLongitude)).zoom(14).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    middle = mGoogleMap.getCameraPosition().target;

                    myLocationImage.setImageDrawable(getResources().getDrawable(R.drawable.pointerloc));
                    myLocationImage.requestLayout();
                    myLocationImage.getLayoutParams().height = 20;
                    myLocationImage.getLayoutParams().width = 20;
                    for (int i = 0; i < LstAreaList.length(); i++) {
                        JSONObject jo = new JSONObject();
                        try {
                            triangle.clear();
                            jo = LstAreaList.getJSONObject(i);

                            JSONArray oCoordinaties = jo.getJSONArray("oCoordinaties");
                            for (int s = 0; s < oCoordinaties.length(); s++) {
                                JSONObject XY = oCoordinaties.getJSONObject(s);
                                triangle.add(new LatLng(XY.getDouble("Latitude"), XY.getDouble("Longitude")));
                            }
//                            mGoogleMap.addPolygon(new PolygonOptions().addAll(triangle).fillColor(Color.BLUE - ALPHA_ADJUSTMENT).strokeColor(Color.BLUE).strokeWidth(15));

                            if (PolyUtil.containsLocation(middle, triangle, false)) {
                                AreaID = jo.getInt("AreaID");
                                AddressString = jo.getString("AreaName");
                                myLocationImage.setImageDrawable(getResources().getDrawable(R.drawable.newmarker));
                                myLocationImage.requestLayout();
                                myLocationImage.getLayoutParams().height = 125;
                                myLocationImage.getLayoutParams().width = 85;
                                isPinSelected = true;
                                clicked.changed(isPinSelected);
                                PickupLatitude = middle.latitude;
                                PickupLongitude = middle.longitude;
                                break;
                            }else {
                                isPinSelected = false;
                                clicked.changed(isPinSelected);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }

    private void fillArea(JSONArray lstAreaList) {
        for (int i = 0; i < lstAreaList.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                triangle.clear();
                jo = lstAreaList.getJSONObject(i);
                JSONArray oCoordinaties = jo.getJSONArray("oCoordinaties");
                for (int s = 0; s < oCoordinaties.length(); s++) {
                    JSONObject XY = oCoordinaties.getJSONObject(s);
                    triangle.add(new LatLng(XY.getDouble("Latitude"), XY.getDouble("Longitude")));
                }
                mGoogleMap.addPolygon(new PolygonOptions().addAll(triangle).fillColor(R.color.mapcolor - ALPHA_ADJUSTMENT).strokeColor(Color.BLUE).strokeWidth(5));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        getLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_btn:
                showMenuDialog();
                break;
        }
    }

    private void showMenuDialog() {
        Holder holder;
        holder = new ViewHolder(R.layout.menu_layout);


        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setCancelable(true)
                .setGravity(Gravity.TOP)
                .setBackgroundColorResId(Color.TRANSPARENT)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.languge_txt:
                                dialog.dismiss();
//                                i = new Intent(MainActivity.this, LanguageActivity.class);
//                                startActivity(i);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                alertDialogBuilder.setTitle(R.string.language);
                                alertDialogBuilder
                                        .setMessage(R.string.language_flip)
                                        .setCancelable(false)
                                        .setPositiveButton("Yes",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int id) {
                                                        setLanguageSwitcher();
                                                    }
                                                })
                                        .setNegativeButton("No",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int id) {

                                                        dialog.cancel();
                                                    }
                                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                break;

                            case R.id.account_txt:
                                dialog.dismiss();
                                i = new Intent(MainActivity.this, AccountActivity.class);
                                startActivity(i);
                                break;

                            case R.id.about_txt:
                                dialog.dismiss();
//                              pushFragments(new AboutParco(), false, true);
                                i = new Intent(MainActivity.this, AboutParcoActivity.class);
                                startActivity(i);
                                break;

                            case R.id.packages_txt:
                                dialog.dismiss();
                                i = new Intent(MainActivity.this, PackagesActivity.class);
                                startActivity(i);
                                break;

                            case R.id.noti_txt:
                                dialog.dismiss();
                                i = new Intent(MainActivity.this, NotificationActivity.class);
                                startActivity(i);
                                break;

                            case R.id.logout:
//                                myPref.setBooleanPreference(MainActivity.this, appConstant.isLoggedIn, false);
//                                i = new Intent(MainActivity.this, LoginOrRegisterActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(i);
//                                finish();
                                Logout();
                                break;
                        }
                    }
                })
                .create();

        dialog.show();
    }

    private void setLanguageSwitcher() {
        Fragment fr = new Fragment();
        String str = "";
        if (myPref.getStringPreference(MainActivity.this, appConstant.language_selected).equals(
                appConstant.HP_ENGLISH)) {
            myPref.setStringPreference(getApplicationContext(), appConstant.language_selected,
                    appConstant.HP_ARABIC);
            Log.e(TAG, "Set locale to Arabic");
            setLanguage(appConstant.HP_ARABIC);
        } else {
            myPref.setStringPreference(getApplicationContext(), appConstant.language_selected,
                    appConstant.HP_ENGLISH);
            setLanguage(appConstant.HP_ENGLISH);
            Log.e(TAG, "Set locale to English");
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.e(TAG, "BackStack Count Zero");
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager()
                    .getBackStackEntryAt(
                            getSupportFragmentManager()
                                    .getBackStackEntryCount() - 1);
            str = backEntry.getName();
            Log.e(TAG, "Fragment Name: " + str);
            fr = getSupportFragmentManager().findFragmentByTag(str);
        } else {
            fr = getSupportFragmentManager().findFragmentByTag("mawaqaa.parco.Fragment.PinLocationFragment");
            Log.e(TAG, "BackStack Count Zero");
        }
        if (fr != null) {
            Log.e(TAG, "Not Null Fragment");
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            ft.detach(fr);
            ft.attach(fr);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
            //recreate();
            // popFragments(fr);
            // pushFragments(fr, false, true,str);
            // pushFragments4LanSwitch(fr, false);
        } else
            Log.e(TAG, "Null Fragment Return");
    }

    private void setLanguage(String lang) {
       /* String languageToLoad = lang;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());*/
        String languageToLoad = lang;
        Locale locale = new Locale(languageToLoad);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);

        onConfigurationChanged(conf);
//        this.getResources().updateConfiguration(conf, this.getResources().getDisplayMetrics());

    }

    private void Logout() {
        String CustomerID = myPref.getStringPreference(this, appConstant.CustomerID);
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put("CustomerID", CustomerID);
                jo.put("UserTypeID", 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.Logout, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {

                                    myPref.setBooleanPreference(MainActivity.this, appConstant.isLoggedIn, false);
                                    i = new Intent(MainActivity.this, LoginOrRegisterActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();

                                } else {
                                    Toast.makeText(MainActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideDialog();
                        }
                    });


                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                            6000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(jsonRequest);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void changed(boolean status) {
        PinLocationFragment pinLocationFragment = (PinLocationFragment)
                getSupportFragmentManager().findFragmentById(R.id.main_container);
        pinLocationFragment.changeStatus(status);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                setSearchLocationOnMap(place.getLatLng(),place.getName().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(MainActivity.this,"Error: "+status.getStatusMessage(),Toast.LENGTH_LONG).show();
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void setSearchLocationOnMap(LatLng latLng,String name) {
/*        mGoogleMap.clear();
        fillArea(LstAreaList);
        if (mGoogleMap != null) {
*//*            mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer))
                    .title(name));*//*
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }*/
        getCurrentLocation(latLng);
    }


    private void getCurrentLocation(LatLng latLng){
        if (mGoogleMap != null) {
            fillArea(LstAreaList);
            isPinSelected = false;
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    middle = mGoogleMap.getCameraPosition().target;

                    myLocationImage.setImageDrawable(getResources().getDrawable(R.drawable.pointerloc));
                    myLocationImage.requestLayout();
                    myLocationImage.getLayoutParams().height = 20;
                    myLocationImage.getLayoutParams().width = 20;
                    for (int i = 0; i < LstAreaList.length(); i++) {
                        JSONObject jo = new JSONObject();
                        try {
                            triangle.clear();
                            jo = LstAreaList.getJSONObject(i);

                            JSONArray oCoordinaties = jo.getJSONArray("oCoordinaties");
                            for (int s = 0; s < oCoordinaties.length(); s++) {
                                JSONObject XY = oCoordinaties.getJSONObject(s);
                                triangle.add(new LatLng(XY.getDouble("Latitude"), XY.getDouble("Longitude")));
                            }
//                            mGoogleMap.addPolygon(new PolygonOptions().addAll(triangle).fillColor(Color.BLUE - ALPHA_ADJUSTMENT).strokeColor(Color.BLUE).strokeWidth(15));

                            if (PolyUtil.containsLocation(middle, triangle, false)) {
                                AreaID = jo.getInt("AreaID");
                                AddressString = jo.getString("AreaName");
                                myLocationImage.setImageDrawable(getResources().getDrawable(R.drawable.newmarker));
                                myLocationImage.requestLayout();
                                myLocationImage.getLayoutParams().height = 125;
                                myLocationImage.getLayoutParams().width = 85;
                                isPinSelected = true;
                                clicked.changed(isPinSelected);
                                PickupLatitude = middle.latitude;
                                PickupLongitude = middle.longitude;
                                break;
                            }else {
                                isPinSelected = false;
                                clicked.changed(isPinSelected);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }


}