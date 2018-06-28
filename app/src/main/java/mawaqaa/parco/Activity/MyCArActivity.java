package mawaqaa.parco.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Adapter.CarPickUpAdapter;
import mawaqaa.parco.Model.CarPickUpModel;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

public class MyCArActivity extends MainBaseActivity implements View.OnClickListener {

    private static String TAG = "MyCArActivity";
    Button add_car_btn;
    Intent i;
    TextView title;
    static LinearLayout no_car_linear;
    private static ProgressDialog pDialog;
    static SharedPrefsUtils myPref;
    static RecyclerView car_recy;
    private LinearLayoutManager linearLayoutManager;
    static List<CarPickUpModel> carList = new ArrayList<CarPickUpModel>();
    static CarPickUpAdapter carPickUpAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_car_activity);
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "corbelbold.ttf"));

        defineView();

        GetCustomerCars();
    }

    private static void GetCustomerCars() {
        String CustomerID = myPref.getStringPreference(BaseActivity, appConstant.CustomerID);
        JSONObject jo = new JSONObject();
        try {
            jo.put(appConstant.APIKey, 123456);
            jo.put("CustomerID", CustomerID);
            jo.put(appConstant.LanguageID, 1);

            Log.d(TAG, jo.toString());

            if (VolleyUtils.volleyEnabled) {
                showDialog();

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetCustomerCars, jo, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        Log.d(TAG, response.toString());

                        try {
                            int APIStatus = response.getInt("APIStatus");
                            String APIMessage = response.getString("APIMessage");

                            if (APIStatus == 1) {
                                JSONArray jsonArray = response.getJSONArray("lstCustomerCars");
                                if (jsonArray.length() > 0) {
                                    no_car_linear.setVisibility(View.GONE);
                                    fillCar(jsonArray);
                                } else {
                                    no_car_linear.setVisibility(View.VISIBLE);
                                }

                            } else {
                                Toast.makeText(BaseActivity, APIMessage, Toast.LENGTH_SHORT).show();
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


                RequestQueue requestQueue = Volley.newRequestQueue(BaseActivity);
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

    private static void fillCar(JSONArray jsonArray) {
        carList.clear();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = jsonArray.getJSONObject(i);
                carList.add(new CarPickUpModel(
                        jo.getInt("ID"),
                        jo.getString("CarTypeImage"),
                        jo.getString("CarModelName")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        carPickUpAdapter = new CarPickUpAdapter(BaseActivity, carList);
        car_recy.setAdapter(carPickUpAdapter);
    }

    private void defineView() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.wait));
        pDialog.setCancelable(true);
        car_recy = findViewById(R.id.car_recy);
        linearLayoutManager = new LinearLayoutManager(this);
        car_recy.setLayoutManager(linearLayoutManager);

        add_car_btn = findViewById(R.id.add_car_btn);
        no_car_linear = findViewById(R.id.no_car_linear);
        add_car_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_car_btn:
                i = new Intent(this, AddNewCarActivity.class);
                startActivity(i);
                break;
        }
    }

    private static void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private static void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public static void removeFromCart(int position) {
        int car_id = carList.get(position).getId();
        String CustomerID = myPref.getStringPreference(BaseActivity, appConstant.CustomerID);
        JSONObject jo = new JSONObject();
        try {
            jo.put(appConstant.APIKey, 123456);
            jo.put("CustomerID", CustomerID);
            jo.put("CarID", car_id);

            Log.d(TAG, jo.toString());

            if (VolleyUtils.volleyEnabled) {
                showDialog();

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.DeleteCustomerCar, jo, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        Log.d(TAG, response.toString());

                        try {
                            int APIStatus = response.getInt("APIStatus");
                            String APIMessage = response.getString("APIMessage");

                            if (APIStatus == 1) {
                                GetCustomerCars();

                            } else {
                                Toast.makeText(BaseActivity, APIMessage, Toast.LENGTH_SHORT).show();
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


                RequestQueue requestQueue = Volley.newRequestQueue(BaseActivity);
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
}
