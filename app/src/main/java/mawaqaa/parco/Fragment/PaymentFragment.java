package mawaqaa.parco.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mawaqaa.parco.Activity.HistoryActivity;
import mawaqaa.parco.Activity.MainActivity;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

public class PaymentFragment extends MainBaseFragment implements View.OnClickListener {
    private static String TAG = "PaymentFragment";
    View v;
    TextView title, knet;
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
        v = inflater.inflate(R.layout.payment_fragment, container, false);
        defineView();
        return v;
    }

    private void defineView() {
        pDialog = new ProgressDialog(Activity);
        pDialog.setMessage(Activity.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        title = v.findViewById(R.id.title);


        knet = v.findViewById(R.id.knet);

        knet.setOnClickListener(this);

        CalculateFees();
    }

    private void CalculateFees() {
        JSONObject jo = new JSONObject();
        int PickupID = MainActivity.PickupID;
        try {
            jo.put(appConstant.APIKey, 123456);
            jo.put("PickupID", PickupID);

            Log.d(TAG, jo.toString());

            if (VolleyUtils.volleyEnabled) {
                showDialog();

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.CalculateFees, jo, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        Log.d(TAG, response.toString());

                        try {
                            int APIStatus = response.getInt("APIStatus");
                            String APIMessage = response.getString("APIMessage");

                            if (APIStatus == 1) {
                                String PickUpFees = response.getString("PickUpFees");
                                title.setText(getString(R.string.the_fare_is) + " " + PickUpFees + " KD");

                            } else {
                                Toast.makeText(Activity, APIMessage, Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.knet:
                PostPickUp();
                break;
        }
    }

    private void PostPickUp() {
        String CustomerID = myPref.getStringPreference(Activity, appConstant.CustomerID);
        int PickupID = MainActivity.PickupID;
        int CarID = MainActivity.CarID;
        JSONObject jo = new JSONObject();

        try {
            jo.put(appConstant.APIKey, 123456);
            jo.put("CustomerID", CustomerID);
            jo.put("PickupID", PickupID);
            jo.put("CarID", CarID);
            jo.put("PaymentMethod", 1);

            if (MainActivity.IstAdditionalService) {
                jo.put("lstAdditionalService", MainActivity.LsAdditionalService);
            }
            Log.d(TAG, jo.toString());

            if (VolleyUtils.volleyEnabled) {
                showDialog();

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.PostPickUp, jo, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        Log.d(TAG, response.toString());

                        try {
                            int APIStatus = response.getInt("APIStatus");
                            String APIMessage = response.getString("APIMessage");

                            if (APIStatus == 1) {
                                Toast.makeText(Activity, APIMessage, Toast.LENGTH_SHORT).show();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                    fm.popBackStack();
                                }

                                Intent i = new Intent(Activity, HistoryActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(Activity, APIMessage, Toast.LENGTH_SHORT).show();
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