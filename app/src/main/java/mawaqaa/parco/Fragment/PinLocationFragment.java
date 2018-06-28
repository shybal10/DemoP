package mawaqaa.parco.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mawaqaa.parco.Activity.MainActivity;
import mawaqaa.parco.Adapter.LocationHistoryAdapter;
import mawaqaa.parco.Dialog.SearchLocationsDialog;
import mawaqaa.parco.InterFace.PinButtonChange;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;

import static mawaqaa.parco.Activity.MainActivity.CustomerLatitude;
import static mawaqaa.parco.Activity.MainActivity.CustomerLongitude;
import static mawaqaa.parco.Activity.MainActivity.LstAreaList;
import static mawaqaa.parco.Activity.MainActivity.isPinSelected;

import static mawaqaa.parco.Activity.MainActivity.mGoogleMap;
import static mawaqaa.parco.Activity.MainActivity.triangle;
import static mawaqaa.parco.Activity.MainBaseActivity.getMainBaseActivity;

public class PinLocationFragment extends MainBaseFragment implements View.OnClickListener {
    private final static int ALPHA_ADJUSTMENT = 0x77000000;
    private static String TAG = "PinLocationFragment";
    View v;
    public static int AreaID;
    public static String AddressString;
    PinButtonChange clicked;
    public static LatLng middle;
    Button let_parco_btn, searchLocations;
    ImageButton userPresentLocationButton;
    public static double PickupLatitude;
    public static double PickupLongitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.pin_location_fragment, container, false);
        searchLocations = v.findViewById(R.id.search_location_button);
        searchLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchDialog();
            }
        });
        let_parco_btn = v.findViewById(R.id.let_parco_btn);
        if (mGoogleMap != null) {
            mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

 /*           mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mGoogleMap.clear();
                    fillArea(LstAreaList);
                    isPinSelected = false;
                    changeStatus(isPinSelected);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(CustomerLatitude, CustomerLongitude)).zoom(15).build();
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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

                            if (PolyUtil.containsLocation(latLng, triangle, false)) {

                                MainActivity.AreaID = jo.getInt("AreaID");
                                MainActivity.AddressString = jo.getString("AreaName");
                                mGoogleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer2)));

                                isPinSelected = true;
                                changeStatus(isPinSelected);

                                MainActivity.PickupLatitude = latLng.latitude;
                                MainActivity.PickupLongitude = latLng.longitude;
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });*/
        }

        if (isPinSelected) {
            let_parco_btn.setBackgroundResource(R.drawable.btn_blue);

        } else {
            let_parco_btn.setBackgroundResource(R.drawable.btn_gray);
        }

        let_parco_btn.setOnClickListener(this);

        userPresentLocationButton = v.findViewById(R.id.user_location_button);
        userPresentLocationButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                FusedLocationProviderClient mFusedLocationClient;
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    CustomerLatitude = location.getLatitude();
                                    CustomerLongitude = location.getLongitude();
                                    ((MainActivity)getActivity()).getCurrentLocation(CustomerLatitude,CustomerLongitude);                                }
                            }
                        });
            }
        });
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.let_parco_btn:
                if (isPinSelected) {
                    let_parco_btn.setBackgroundResource(R.color.gray);
                    Activity.pushFragments(new PickUpFragment(), false, true);
                } else {
                    Toast.makeText(Activity, getString(R.string.err_slct_pin), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void changeStatus(boolean status) {
        if (status) {
            let_parco_btn.setBackgroundResource(R.drawable.btn_blue);
        } else {
            let_parco_btn.setBackgroundResource(R.drawable.btn_gray);
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
                mGoogleMap.addPolygon(new PolygonOptions().addAll(triangle).fillColor(Color.BLUE - ALPHA_ADJUSTMENT).strokeColor(Color.BLUE).strokeWidth(5));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void openSearchDialog() {
        SearchLocationsDialog searchLocationsDialog = new SearchLocationsDialog(getActivity());
        searchLocationsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        searchLocationsDialog.setCancelable(true);
        Window window = searchLocationsDialog.getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);
        searchLocationsDialog.show();
    }

/*
    private void getCurrentLocation(double customerLatitude,double customerLongitude) {
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

   */
/*         mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mGoogleMap.clear();
                    fillArea(LstAreaList);
                    isPinSelected = false;
                    clicked.changed(isPinSelected);
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(CustomerLatitude, CustomerLongitude))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer))
                            .title("Me"));

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

                            if (PolyUtil.containsLocation(latLng, triangle, false)) {

                                AreaID = jo.getInt("AreaID");
                                AddressString = jo.getString("AreaName");
                                mGoogleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer2)));

                                isPinSelected = true;
                                clicked.changed(isPinSelected);

                                PickupLatitude = latLng.latitude;
                                PickupLongitude = latLng.longitude;

                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!isPinSelected) {
                        Toast.makeText(MainActivity.this, "Out Parking", Toast.LENGTH_SHORT).show();
                    }
                }
            });*//*

                }
            });
        }
*/



/*    @Override
    public void changed(boolean status) {
        changeStatus(status);
    }*/
}
