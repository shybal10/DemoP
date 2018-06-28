package mawaqaa.parco.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import mawaqaa.parco.Activity.PackageDetailsActivity;
import mawaqaa.parco.Activity.PrepaidCreditActivity;
import mawaqaa.parco.Adapter.PackageAdapter;
import mawaqaa.parco.Model.packageModel;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

public class PackagesParcoFragment extends MainBaseFragment {
    private static String TAG = "PackagesParcoFrag";

    View v;
    RecyclerView package_rec;
    private LinearLayoutManager linearLayoutManager;
    static List<packageModel> itemtList = new ArrayList<packageModel>();

    PackageAdapter packageAdapter;
    private ProgressDialog pDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.packages_parco_fragment, container, false);
        package_rec = v.findViewById(R.id.package_rec);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        package_rec.setLayoutManager(linearLayoutManager);
        pDialog = new ProgressDialog(Activity);
        pDialog.setMessage(Activity.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);


        GetPackages();
        
        return v;
    }

    private void GetPackages() {
        if (Activity.isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetPackages, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {
                                    JSONArray lstPackagesList = new JSONArray();
                                    lstPackagesList = response.getJSONArray("lstPackagesList");

                                    fillPackages(lstPackagesList);

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

    private void fillPackages(JSONArray lstPackagesList) {
        itemtList.clear();

        for (int i = 0; i < lstPackagesList.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = lstPackagesList.getJSONObject(i);
                itemtList.add(new packageModel(
                        jo.getString("ID"),
                        jo.getString("PackageName"),
                        jo.getString("PackageDescription"),
                        jo.getString("ParkingCount"),
                        jo.getString("CarWashCount"),
                        jo.getString("FuelingAmountRate"),
                        jo.getString("FuelingCount"),
                        jo.getString("CreditAmount"),
                        jo.getString("PackagePrice")
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        packageAdapter = new PackageAdapter(getActivity(), itemtList);
        package_rec.setAdapter(packageAdapter);

//        packageAdapter.setOnItemClickListener(new PackageAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
////                Intent i = new Intent(getActivity(), PackageDetailsActivity.class);
////                startActivity(i);
//            }
//        });
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