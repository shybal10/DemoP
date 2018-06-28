package mawaqaa.parco.Activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.net.ParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

public class OrderDetailsActivity extends MainBaseActivity implements OnMapReadyCallback {

    private static String TAG = "OrderDetailsActivity";
    SupportMapFragment mapFrag;
    public static GoogleMap mGoogleMap;

    public static double latti = 29.367706;
    public static double longi = 47.979637;
    String pickupId = "";
    private ProgressDialog pDialog;
    SharedPrefsUtils myPref;

    double CustomerLatitude;
    double CustomerLongitude;
    Typeface normal, bold;

    TextView status_txt, price_txt, date_txt, car_text;
    TextView statusLabel, priceLabel, dateLabel, carLabel, detailsHeading;
    ImageView car_color;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_activity);

        Bundle b = getIntent().getExtras();
        if (b != null)
            pickupId = b.getString("pickupId");

        defineView();
        GetPickUpDetails(pickupId);

    }

    private void defineView() {
        normal = Typeface.createFromAsset(getAssets(), "corbel.ttf");
        bold = Typeface.createFromAsset(getAssets(), "corbelbold.ttf");

        car_text = findViewById(R.id.car_text);
        car_text.setTypeface(normal);
        car_color = findViewById(R.id.car_color);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        detailsHeading = findViewById(R.id.details_heading);
        detailsHeading.setTypeface(bold);
        carLabel = (TextView) findViewById(R.id.textView2);
        carLabel.setTypeface(normal);
        dateLabel = findViewById(R.id.date_label);
        dateLabel.setTypeface(normal);
        priceLabel = findViewById(R.id.price_label);
        priceLabel.setTypeface(normal);
        statusLabel = findViewById(R.id.status_label);
        statusLabel.setTypeface(normal);


        status_txt = findViewById(R.id.status_txt);
        status_txt.setTypeface(normal);
        date_txt = findViewById(R.id.date_txt);
        date_txt.setTypeface(normal);
        price_txt = findViewById(R.id.price_txt);
        price_txt.setTypeface(normal);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    private void GetPickUpDetails(String pickupId) {
        String CustomerID = myPref.getStringPreference(this, appConstant.CustomerID);
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put("PickUpID", pickupId);
                jo.put("CustomerID", CustomerID);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetPickUpDetails, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                JSONObject oOrderDetails = new JSONObject();
                                oOrderDetails = response.getJSONObject("oOrderDetails");

                                int APIStatus = oOrderDetails.getInt("APIStatus");
                                String APIMessage = oOrderDetails.getString("APIMessage");

                                if (APIStatus == 1) {

                                    CustomerLatitude = oOrderDetails.getDouble("CustomerLatitude");
                                    CustomerLongitude = oOrderDetails.getDouble("CustomerLongitude");

                                    if (mGoogleMap != null) {
                                        mGoogleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(CustomerLatitude, CustomerLongitude))
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.loc_parco))
                                        );

                                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(CustomerLatitude, CustomerLongitude)).zoom(18).build();
                                        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    }

                                    status_txt.setText(oOrderDetails.getString("sOrderStatus"));
                                    price_txt.setText(oOrderDetails.getString("Price") + " KD");

                                    String dateS = oOrderDetails.getString("OrderDateTime");
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                                    Date date = null;
                                    try {
//                                        date = format.parse(dateS);
//                                        System.out.println(date);

                                        String dateString = dateS.substring(0, 10);
                                        date = format.parse(dateString);

                                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
//                                      use UTC as timezone
                                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                                        Log.i("DATE", sdf.format(date));
                                        String dateToShow = sdf.format(date);

                                        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                                        sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
                                        System.out.println(sdf2.format(date));
                                        String timeToShow = sdf2.format(date);

                                        date_txt.setText(dateToShow + " " + timeToShow);
                                    } catch (java.text.ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (!oOrderDetails.getString("CarModel").isEmpty()) {
                                        car_text.setText(oOrderDetails.getString("CarModel"));
                                    }

                                    if (!oOrderDetails.getString("CarColorImage").isEmpty()) {
                                        Glide.with(OrderDetailsActivity.this)
                                                .load(UrlClass.ImageURL + oOrderDetails.getString("CarColorImage"))
                                                .into(car_color);
                                    }
                                } else {
                                    Toast.makeText(OrderDetailsActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
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


                    RequestQueue requestQueue = Volley.newRequestQueue(OrderDetailsActivity.this);
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
            Toast.makeText(OrderDetailsActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mGoogleMap != null) {
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latti, longi))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.loc_parco))
            );

            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latti, longi)).zoom(18).build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
}
