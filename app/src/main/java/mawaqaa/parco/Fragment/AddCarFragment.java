package mawaqaa.parco.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import mawaqaa.parco.Activity.CarColorActivity;
import mawaqaa.parco.Activity.CarModelActivity;
import mawaqaa.parco.Activity.MainActivity;
import mawaqaa.parco.Adapter.CarTypeAddCarAdapter;
import mawaqaa.parco.Adapter.CarTypeFragmentAdapter;
import mawaqaa.parco.Model.CarTypeModel;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

public class AddCarFragment extends MainBaseFragment implements View.OnClickListener {

    private static String TAG = "AddCarFragment";
    View v;
    LinearLayout car_model_linear, color_linear;
    RecyclerView car_type_rec;
    private LinearLayoutManager linearLayoutManager;
    static List<CarTypeModel> itemtList = new ArrayList<CarTypeModel>();
    CarTypeFragmentAdapter carTypeAdapter;
    static String car_model_id = "";
    static String car_color_id = "";
    public static String car_type_id = "";
    Button add_car_btn;
    TextView userinfo, carModel, carType, carColor, fuelType;

    ImageView color_car_img, img_car_model;
    Typeface bold, normal;
    SharedPrefsUtils myPref;
    private ProgressDialog pDialog;
    private MainActivity myContext;
//    TODO: fetch data and integrate fuel type

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.add_car_fragment, container, false);
        definView();
        return v;
    }
    private void definView() {
        carColor = (TextView) v.findViewById(R.id.car_color_text);
        carModel = (TextView) v.findViewById(R.id.car_model_text);
        carType = (TextView) v.findViewById(R.id.car_type_text);
        userinfo = (TextView) v.findViewById(R.id.user_info_textView);
        fuelType = (TextView) v.findViewById(R.id.fuel_type_text_view);


        pDialog = new ProgressDialog(Activity);
        pDialog.setMessage(Activity.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);
        color_car_img = v.findViewById(R.id.color_car_img);
        img_car_model = v.findViewById(R.id.img_car_model);
        car_model_linear = v.findViewById(R.id.car_model_linear);
        color_linear = v.findViewById(R.id.color_linear);
        add_car_btn = v.findViewById(R.id.add_car_btn);

        car_type_rec = v.findViewById(R.id.car_type_rec);
        linearLayoutManager = new LinearLayoutManager(Activity);
        car_type_rec.setLayoutManager(linearLayoutManager);

        car_model_linear.setOnClickListener(this);
        color_linear.setOnClickListener(this);
        add_car_btn.setOnClickListener(this);
        GetCarTypes();

        if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty()) {
            add_car_btn.setBackgroundResource(R.drawable.btn_gray);
        } else {
            add_car_btn.setBackgroundResource(R.drawable.btn_blue);
        }
    }

    private void GetCarTypes() {
        if (Activity.isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetCarTypes, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {
                                    JSONArray jArray = response.getJSONArray("LstCarTypeList");
                                    fillCarType(jArray);
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.color_linear:
//                Activity.pushFragments(new CarColorFragment(), false, true);
                Intent i = new Intent(myContext, CarColorActivity.class);
                startActivityForResult(i, 2);
                break;

            case R.id.car_model_linear:
//                Activity.pushFragments(new CarModelFragment(), false, true);
                i = new Intent(myContext, CarModelActivity.class);
                startActivityForResult(i, 1);
                break;

            case R.id.add_car_btn:
                if (car_model_id.isEmpty()) {
                    Toast.makeText(Activity, getString(R.string.err_car_model), Toast.LENGTH_SHORT).show();
                } else if (car_color_id.isEmpty()) {
                    Toast.makeText(Activity, getString(R.string.err_car_color), Toast.LENGTH_SHORT).show();
                } else if (car_type_id.isEmpty()) {
                    Toast.makeText(Activity, getString(R.string.err_car_type), Toast.LENGTH_SHORT).show();
                } else {
                    AddCustomerCar();
                }
                break;
        }
    }

    private void AddCustomerCar() {
        if (Activity.isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                String customerId = myPref.getStringPreference(Activity, appConstant.CustomerID);
                jo.put(appConstant.APIKey, 123456);
                jo.put("CustomerID", customerId);
                jo.put("CarModelID", car_model_id);
                jo.put("CarTypeID", car_type_id);
                jo.put("CarColorID", car_color_id);

                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.AddCustomerCar, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {
                                    Toast.makeText(Activity, APIMessage, Toast.LENGTH_SHORT).show();
                                    Activity.pushFragments(new CarPickUpFragment(), false, false);
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

    private void fillCarType(JSONArray jArray) {
        itemtList.clear();

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = jArray.getJSONObject(i);
                itemtList.add(new CarTypeModel(jo.getString("ID"), jo.getString("TypeName"), jo.getString("TypeImage")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        carTypeAdapter = new CarTypeFragmentAdapter(Activity, itemtList);
        car_type_rec.setAdapter(carTypeAdapter);

        carTypeAdapter.setOnItemClickListener(new CarTypeFragmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                car_type_id = itemtList.get(position).getTypeID();

                if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty()) {
                    add_car_btn.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    add_car_btn.setBackgroundResource(R.drawable.btn_blue);
                }
                carTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        myContext = (MainActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                car_color_id = data.getStringExtra("car_color_id");
                color_car_img.setImageResource(R.drawable.ic_selected_blue);
                if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty()) {
                    add_car_btn.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    add_car_btn.setBackgroundResource(R.drawable.btn_blue);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {
                car_model_id = data.getStringExtra("car_model_id");
                img_car_model.setImageResource(R.drawable.ic_selected_blue);
                if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty()) {
                    add_car_btn.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    add_car_btn.setBackgroundResource(R.drawable.btn_blue);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
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