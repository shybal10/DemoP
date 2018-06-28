package mawaqaa.parco.Activity;

import android.app.Activity;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Adapter.CarTypeAdapter;
import mawaqaa.parco.Adapter.CarTypeAddCarAdapter;
import mawaqaa.parco.Model.CarTypeModel;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.CommandFactory;
import mawaqaa.parco.Volley.VolleyUtils;

public class AddNewCarActivity extends MainBaseActivity implements View.OnClickListener {
    private static String TAG = "AddNewCarActivity";
    LinearLayout car_model_linear, color_linear, fuel_type_linear;
    Intent i;
    RecyclerView car_type_rec;
    private LinearLayoutManager linearLayoutManager;
    private ProgressDialog pDialog;
    static List<CarTypeModel> itemtList = new ArrayList<CarTypeModel>();
    CarTypeAddCarAdapter carTypeAdapter;
    Button add_car;
    Typeface normal;
    static String car_model_id = "";
    static String car_color_id = "";
    public static String car_type_id = "";
    static String fuel_type_id = "";
    ImageView img_car_model, color_car_img, fuelTypeImage;
    TextView carModel, carColor, fuelType, carType;

    SharedPrefsUtils myPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_car_activity);

        definView();
    }

    private void definView() {
        pDialog = new ProgressDialog(AddNewCarActivity.this);
        pDialog.setMessage(AddNewCarActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);
        add_car = findViewById(R.id.add_car);

        carModel = (TextView) findViewById(R.id.car_model_text_view);
        carColor = (TextView) findViewById(R.id.color_text_view);
        fuelType = (TextView) findViewById(R.id.fuel_type_text_view);
        carType = (TextView) findViewById(R.id.title_type);

        normal = Typeface.createFromAsset(getAssets(), "corbel.ttf");

        carModel.setTypeface(normal);
        carColor.setTypeface(normal);
        fuelType.setTypeface(normal);
        carType.setTypeface(normal);


        fuelTypeImage = (ImageView) findViewById(R.id.fuel_type_img);

        car_type_rec = findViewById(R.id.car_type_rec);
        linearLayoutManager = new LinearLayoutManager(this);
        car_type_rec.setLayoutManager(linearLayoutManager);

        car_model_linear = findViewById(R.id.car_model_linear);
        color_linear = findViewById(R.id.color_linear);

        img_car_model = findViewById(R.id.img_car_model);
        color_car_img = findViewById(R.id.color_car_img);

        fuel_type_linear = findViewById(R.id.fuel_type_linear);
        fuelTypeImage = findViewById(R.id.fuel_type_img);

        car_model_linear.setOnClickListener(this);
        color_linear.setOnClickListener(this);
        fuel_type_linear.setOnClickListener(this);
        add_car.setOnClickListener(this);
        if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty()) {
            add_car.setBackgroundResource(R.drawable.btn_gray);
        } else {
            add_car.setBackgroundResource(R.drawable.btn_blue);
        }

        GetCarTypes();
    }

    private void GetCarTypes() {
        if (isNetworkAvailable()) {
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
                                    Toast.makeText(AddNewCarActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
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


                    RequestQueue requestQueue = Volley.newRequestQueue(this);
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
            Toast.makeText(AddNewCarActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
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
        carTypeAdapter = new CarTypeAddCarAdapter(this, itemtList);
        car_type_rec.setAdapter(carTypeAdapter);

        carTypeAdapter.setOnItemClickListener(new CarTypeAddCarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                car_type_id = itemtList.get(position).getTypeID();

                if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty() || fuel_type_id.isEmpty()) {
                    add_car.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    add_car.setBackgroundResource(R.drawable.btn_blue);
                }
                carTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_model_linear:
                i = new Intent(this, CarModelActivity.class);
                startActivityForResult(i, 1);
                break;

            case R.id.color_linear:
                i = new Intent(this, CarColorActivity.class);
                startActivityForResult(i, 2);
                break;
            case R.id.fuel_type_linear:
                i = new Intent(this, FuelTypeActivity.class);
                startActivityForResult(i, 3);
                break;

            case R.id.add_car:
                if (car_model_id.isEmpty()) {
                    Toast.makeText(this, getString(R.string.err_car_model), Toast.LENGTH_SHORT).show();
                } else if (car_color_id.isEmpty()) {
                    Toast.makeText(this, getString(R.string.err_car_color), Toast.LENGTH_SHORT).show();
                } else if (car_type_id.isEmpty()) {
                    Toast.makeText(this, getString(R.string.err_car_type), Toast.LENGTH_SHORT).show();
                } else if (fuel_type_id.isEmpty()) {
                    Toast.makeText(this, getString(R.string.err_fuel_type), Toast.LENGTH_SHORT).show();
                } else {
                    AddCustomerCar();
                }
                break;
        }
    }

    private void AddCustomerCar() {
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                String customerId = myPref.getStringPreference(this, appConstant.CustomerID);
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
                                    Toast.makeText(AddNewCarActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                    finish();
                                } else {
                                    Toast.makeText(AddNewCarActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
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


                    RequestQueue requestQueue = Volley.newRequestQueue(this);
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
            Toast.makeText(AddNewCarActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                car_model_id = data.getStringExtra("car_model_id");
                img_car_model.setImageResource(R.drawable.ic_selected_blue);
                if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty() || fuel_type_id.isEmpty()) {
                    add_car.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    add_car.setBackgroundResource(R.drawable.btn_blue);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                car_color_id = data.getStringExtra("car_color_id");
                color_car_img.setImageResource(R.drawable.ic_selected_blue);
                if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty() || fuel_type_id.isEmpty()) {
                    add_car.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    add_car.setBackgroundResource(R.drawable.btn_blue);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                fuel_type_id = data.getStringExtra("fuel_type_id");
                Toast.makeText(this, fuel_type_id, Toast.LENGTH_LONG).show();
                fuelTypeImage.setImageResource(R.drawable.ic_selected_blue);
                if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty() || fuel_type_id.isEmpty()) {
                    add_car.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    add_car.setBackgroundResource(R.drawable.btn_blue);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
