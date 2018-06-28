package mawaqaa.parco.Fragment;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import mawaqaa.parco.Activity.MainActivity;
import mawaqaa.parco.Activity.MainBaseActivity;
import mawaqaa.parco.Adapter.CarPickUpAdapter;
import mawaqaa.parco.Adapter.CarPickUpFargAdpater;
import mawaqaa.parco.Model.CarPickUpModel;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.CommandFactory;
import mawaqaa.parco.Volley.VolleyUtils;

public class CarPickUpFragment extends MainBaseFragment implements View.OnClickListener {

    private static String TAG = "CarPickUpFragment";
    View v;
    RecyclerView car_recy;
    private LinearLayoutManager linearLayoutManager;
    static List<CarPickUpModel> carList = new ArrayList<CarPickUpModel>();
    CarPickUpFargAdpater carPickUpAdapter;
    Button add_car_btn;
    SharedPrefsUtils myPref;
    TextView heading;

    private ProgressDialog pDialog;
    private MainActivity myContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.car_pick_up_fragment, container, false);

        add_car_btn = v.findViewById(R.id.add_car_btn);

        car_recy = v.findViewById(R.id.car_recy);
        linearLayoutManager = new LinearLayoutManager(Activity);
        car_recy.setLayoutManager(linearLayoutManager);
        heading = (TextView) v.findViewById(R.id.heading);
        heading.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"corbel.ttf"));
        pDialog = new ProgressDialog(Activity);
        pDialog.setMessage(Activity.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        add_car_btn.setOnClickListener(this);
        add_car_btn.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"corbelbold.ttf"));

        GetCustomerCars();
        return v;
    }

    private void GetCustomerCars() {
        String CustomerID = myPref.getStringPreference(Activity, appConstant.CustomerID);
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
                                fillCar(jsonArray);
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

    private void fillCar(JSONArray jsonArray) {
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

        carPickUpAdapter = new CarPickUpFargAdpater(Activity, carList);
        car_recy.setAdapter(carPickUpAdapter);

        carPickUpAdapter.setOnItemClickListener(new CarPickUpFargAdpater.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MainActivity.CarID = carList.get(position).getId();
                Activity.pushFragments(new AdditionalServiceFragment(), false, true);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_car_btn:
                Activity.pushFragments(new AddCarFragment(), false, true);
                break;
        }
    }


    @Override
    public void onAttach(android.app.Activity activity) {
        myContext = (MainActivity) activity;
        super.onAttach(activity);
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