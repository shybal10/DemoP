package mawaqaa.parco.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Activity.MainBaseActivity;
import mawaqaa.parco.Model.packageModel;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

public class PackagesActiveFragment extends MainBaseFragment {
    private static String TAG = "PackagesParcoFrag";
    private ProgressDialog pDialog;
    SharedPrefsUtils myPref;
    View v;
    TextView parking, fual, wash, credit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.packages_active_fragment, container, false);

        defineView();
        GetActivePackages();
        return v;
    }

    private void defineView() {
        pDialog = new ProgressDialog(Activity);
        pDialog.setMessage(Activity.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);
        parking = v.findViewById(R.id.parking);
        fual = v.findViewById(R.id.fual);
        wash = v.findViewById(R.id.wash);
        credit = v.findViewById(R.id.credit);

    }

    private void GetActivePackages() {
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

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetActivePackages, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {
                                    JSONObject oUserPackagesAPI = response.getJSONObject("oUserPackagesAPI");
                                    parking.setText(oUserPackagesAPI.getString("ParkingCount"));
                                    fual.setText(oUserPackagesAPI.getString("FuelingCount"));
                                    wash.setText(oUserPackagesAPI.getString("CarWashCount"));
                                    credit.setText(oUserPackagesAPI.getString("CreditAmount"));
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
