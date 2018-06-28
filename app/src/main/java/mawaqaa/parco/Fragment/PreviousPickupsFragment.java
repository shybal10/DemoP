package mawaqaa.parco.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import mawaqaa.parco.Adapter.PreviousPickupsAdapter;
import mawaqaa.parco.Model.PreviousPickupsModel;
import mawaqaa.parco.Model.ScheduledPickupModel;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

/**
 * Created by HP on 4/12/2018.
 */

public class PreviousPickupsFragment extends MainBaseFragment {
    private static String TAG = "PreviousPickupsFragment";
    PreviousPickupsAdapter previousPickupsAdapter;
    List<PreviousPickupsModel> pickupsModelList;
    RecyclerView PreviousPickups_recylerView;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    SharedPrefsUtils myPref;
    private ProgressDialog pDialog;
    LinearLayout no_pickup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.previous_pickups_fragment, container, false);

        defineView(v);

        GetCustomerPreviousPickups();
        return v;
    }

    private void GetCustomerPreviousPickups() {
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

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetCustomerPreviousPickups, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {

                                    JSONArray listCustomerPreviousPickup = new JSONArray();
                                    listCustomerPreviousPickup = response.getJSONArray("listCustomerPreviousPickup");

                                    if (listCustomerPreviousPickup.length() == 0) {
                                        no_pickup.setVisibility(View.VISIBLE);
                                    } else {
                                        no_pickup.setVisibility(View.GONE);
                                        getPreviousPickupsList(listCustomerPreviousPickup);
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

    private void defineView(View v) {
        no_pickup = v.findViewById(R.id.no_pickup);
        pDialog = new ProgressDialog(Activity);
        pDialog.setMessage(Activity.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        PreviousPickups_recylerView = v.findViewById(R.id.PreviousPickups_recylerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        PreviousPickups_recylerView.setLayoutManager(linearLayoutManager);


    }

    private void getPreviousPickupsList(JSONArray listCustomerPreviousPickup) {

        pickupsModelList = new ArrayList<PreviousPickupsModel>();

        for (int i = 0; i < listCustomerPreviousPickup.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = listCustomerPreviousPickup.getJSONObject(i);
                pickupsModelList.add(new PreviousPickupsModel(
                        jo.getString("ID"),
                        jo.getString("AreaName"),
                        jo.getString("sOrderType")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        pickupsModelList.add(new PreviousPickupsModel("1", "swww", "3ref"));
//        pickupsModelList.add(new PreviousPickupsModel("1", "swww", "3ref"));
//        pickupsModelList.add(new PreviousPickupsModel("1", "swww", "3ref"));

        previousPickupsAdapter = new PreviousPickupsAdapter(getActivity(), pickupsModelList);
        PreviousPickups_recylerView.setAdapter(previousPickupsAdapter);

        previousPickupsAdapter.setOnItemClickListener(new PreviousPickupsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), OrderDetailsActivity.class);
                Bundle b = new Bundle();
                b.putString("pickupId", pickupsModelList.get(position).getId());
                i.putExtras(b);
                startActivity(i);
            }
        });
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
