package mawaqaa.parco.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Activity.OrderDetailsActivity;
import mawaqaa.parco.Adapter.CurrentPickupsAdapter;
import mawaqaa.parco.Adapter.ScheduledPickupsAdapter;

import mawaqaa.parco.Model.CurrentPickupModel;
import mawaqaa.parco.Model.ScheduledPickupModel;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

/**
 * Created by HP on 4/15/2018.
 */

public class CurrentScheduledPickupFragment extends MainBaseFragment {
    private static String TAG = "CurrentScheduledPickup";
    CurrentPickupsAdapter currentPickupsAdapter;
    ScheduledPickupsAdapter scheduledPickupsAdapter;
    List<CurrentPickupModel> currentPickupModelList;
    List<ScheduledPickupModel> scheduledPickupList;

    RecyclerView currentPickups_recylerView, scheduledPickups_recylerView;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    private ProgressDialog pDialog;

    SharedPrefsUtils myPref;
    LinearLayout no_pickup;
    TextView current_txt, schedule_txt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.current_scheduled_pickups_fragment, container, false);

        current_txt = (TextView) v.findViewById(R.id.current_txt);
        schedule_txt = (TextView) v.findViewById(R.id.schedule_txt);

        current_txt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"corbel.ttf"));
        schedule_txt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"corbel.ttf"));



        defineView(v);
//        getCurrentPickupsList();
//        getScheduledPickupsList();

        GetCustomerCurrentPickups();

        return v;
    }

    private void GetCustomerCurrentPickups() {
        String CustomerID = myPref.getStringPreference(Activity, appConstant.CustomerID);
        if (Activity.isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put("CustomerID", CustomerID);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetCustomerCurrentPickups, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {
                                    JSONObject oCustomerCurrentPickup = new JSONObject();
                                    oCustomerCurrentPickup = response.getJSONObject("oCustomerCurrentPickup");

                                    JSONArray listCustomerSchedulePickup = new JSONArray();
                                    listCustomerSchedulePickup = response.getJSONArray("listCustomerSchedulePickup");

                                    if (oCustomerCurrentPickup == null && listCustomerSchedulePickup.length() == 0) {
                                        no_pickup.setVisibility(View.VISIBLE);
                                    } else {
                                        if (oCustomerCurrentPickup == null) {
                                            current_txt.setVisibility(View.GONE);
                                        } else {
                                            current_txt.setVisibility(View.VISIBLE);
                                            getCurrentPickupsList(oCustomerCurrentPickup);
                                        }

                                        if (listCustomerSchedulePickup.length() == 0) {
                                            schedule_txt.setVisibility(View.GONE);
                                        } else {
                                            schedule_txt.setVisibility(View.VISIBLE);
                                            getScheduledPickupsList(listCustomerSchedulePickup);
                                        }
                                    }

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
        } else {
            Toast.makeText(Activity, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void getScheduledPickupsList(JSONArray listCustomerSchedulePickup) {

        scheduledPickupList = new ArrayList<ScheduledPickupModel>();

        for (int i = 0; i < listCustomerSchedulePickup.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = listCustomerSchedulePickup.getJSONObject(i);
                scheduledPickupList.add(new ScheduledPickupModel(
                        jo.getString("ID"),
                        jo.getString("AreaName"),
                        jo.getString("sOrderType")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        scheduledPickupsAdapter = new ScheduledPickupsAdapter(getActivity(), scheduledPickupList);
        scheduledPickups_recylerView.setAdapter(scheduledPickupsAdapter);

        scheduledPickupsAdapter.setOnItemClickListener(new ScheduledPickupsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), OrderDetailsActivity.class);
                Bundle b = new Bundle();
                b.putString("pickupId", currentPickupModelList.get(position).getCurrentId());
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    private void getCurrentPickupsList(JSONObject oCustomerCurrentPickup) {

        currentPickupModelList = new ArrayList<CurrentPickupModel>();

        try {
            currentPickupModelList.add(new CurrentPickupModel(
                    oCustomerCurrentPickup.getString("ID"),
                    oCustomerCurrentPickup.getString("AreaName"),
                    oCustomerCurrentPickup.getString("sOrderType")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        currentPickupModelList.add(new CurrentPickupModel("1", "pickup at kuwait", "on the way"));

        currentPickupsAdapter = new CurrentPickupsAdapter(getActivity(), currentPickupModelList);
        currentPickups_recylerView.setAdapter(currentPickupsAdapter);

        currentPickupsAdapter.setOnItemClickListener(new CurrentPickupsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = new Intent(getActivity(), OrderDetailsActivity.class);
                Bundle b = new Bundle();
                b.putString("pickupId", currentPickupModelList.get(position).getCurrentId());
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    private void defineView(View v) {
        no_pickup = v.findViewById(R.id.no_pickup);
        current_txt = v.findViewById(R.id.current_txt);
        schedule_txt = v.findViewById(R.id.schedule_txt);

        pDialog = new ProgressDialog(Activity);
        pDialog.setMessage(Activity.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        currentPickups_recylerView = v.findViewById(R.id.currentPickups_recylerView);
        scheduledPickups_recylerView = v.findViewById(R.id.scheduledPickups_recylerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager2 = new LinearLayoutManager(getActivity());
        currentPickups_recylerView.setLayoutManager(linearLayoutManager);
        scheduledPickups_recylerView.setLayoutManager(linearLayoutManager2);


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
