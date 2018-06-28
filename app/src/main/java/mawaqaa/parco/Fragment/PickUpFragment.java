package mawaqaa.parco.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mawaqaa.parco.Activity.MainActivity;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

import static mawaqaa.parco.Activity.MainActivity.AddressString;
import static mawaqaa.parco.Activity.MainActivity.mGoogleMap;

public class PickUpFragment extends MainBaseFragment implements View.OnClickListener {

    private static String TAG = "PickUpFragment";
    View v;
    LinearLayout pick_up_later_btn, pick_up_now_btn;
    TextView addrees_txt;
    SharedPrefsUtils myPref;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.pick_up_fragment, container, false);
        addrees_txt = v.findViewById(R.id.addrees_txt);
        addrees_txt.setText(AddressString);
        if (mGoogleMap != null) {

            mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);

            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                }
            });

        }

        pDialog = new ProgressDialog(Activity);
        pDialog.setMessage(Activity.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        pick_up_later_btn = v.findViewById(R.id.pick_up_later_btn);
        pick_up_now_btn = v.findViewById(R.id.pick_up_now_btn);

        pick_up_later_btn.setOnClickListener(this);
        pick_up_now_btn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pick_up_now_btn:
//                Activity.pushFragments(new CarPickUpFragment(), false, true);
                PickupDriverNow();
                break;

            case R.id.pick_up_later_btn:
                Activity.pushFragments(new PickUpLaterFragment(), false, true);
                break;
        }

    }

    private void PickupDriverNow() {
        double PickupLatitude = MainActivity.PickupLatitude;
        double PickupLongitude = MainActivity.PickupLongitude;
        String CustomerID = myPref.getStringPreference(Activity, appConstant.CustomerID);
        double CustomerLatitude = MainActivity.CustomerLatitude;
        double CustomerLongitude = MainActivity.CustomerLongitude;
        int AreaID = MainActivity.AreaID;
        if (Activity.isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put("PickupLatitude", PickupLatitude);
                jo.put("PickupLongitude", PickupLongitude);
                jo.put("CustomerID", CustomerID);
                jo.put("CustomerLatitude", CustomerLatitude);
                jo.put("CustomerLongitude", CustomerLongitude);
                jo.put("AreaID", AreaID);
                Log.d(TAG, jo.toString());
                Log.d(TAG, UrlClass.PickupDriverNow);
                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.PickupDriverNow, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                           hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {
                                    MainActivity.PickupID = response.getInt("PickUpID");
                                    Activity.pushFragments(new CarPickUpFragment(), false, true);
                                } else {
                                    showDialogNoDriver();
//                                    Toast.makeText(Activity, APIMessage, Toast.LENGTH_SHORT).show();
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


                    RequestQueue requestQueue = Volley.newRequestQueue(Activity);
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
            Toast.makeText(Activity, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void showDialogNoDriver() {
        final Dialog dialog = new Dialog(Activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.no_driver_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button ok = dialog.findViewById(R.id.ok_btn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
