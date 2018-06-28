package mawaqaa.parco.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Adapter.CarTypeAdapter;
import mawaqaa.parco.Model.CarTypeModel;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.CommandFactory;
import mawaqaa.parco.Volley.VolleyUtils;

public class RegisterCarActivity extends MainBaseActivity implements View.OnClickListener {

    private static String TAG = "RegisterCarActivity";
    LinearLayout car_model_linear, color_linear;
    Intent i;
    static String car_model_id = "";
    static String car_color_id = "";
    public static String car_type_id = "";
    ImageView img_car_model, color_car_img;
    RecyclerView car_type_rec;
    private LinearLayoutManager linearLayoutManager;
    private ProgressDialog pDialog;
    static List<CarTypeModel> itemtList = new ArrayList<CarTypeModel>();
    CarTypeAdapter carTypeAdapter;
    Button create_account;
    TextView terms_and_condition ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_car_activity);

        definView();
    }

    private void definView() {
        pDialog = new ProgressDialog(RegisterCarActivity.this);
        pDialog.setMessage(RegisterCarActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        car_model_linear = findViewById(R.id.car_model_linear);
        color_linear = findViewById(R.id.color_linear);
        img_car_model = findViewById(R.id.img_car_model);
        color_car_img = findViewById(R.id.color_car_img);
        create_account = findViewById(R.id.create_account);
        terms_and_condition = findViewById(R.id.terms_and_condition);

        car_type_rec = findViewById(R.id.car_type_rec);
        linearLayoutManager = new LinearLayoutManager(this);
        car_type_rec.setLayoutManager(linearLayoutManager);

        car_model_linear.setOnClickListener(this);
        color_linear.setOnClickListener(this);
        create_account.setOnClickListener(this);
        terms_and_condition.setOnClickListener(this);
        GetCarTypes();

        if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty()) {
            create_account.setBackgroundResource(R.drawable.btn_gray);
        } else {
            create_account.setBackgroundResource(R.drawable.btn_blue);
        }
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
                    CommandFactory commandFactory = new CommandFactory();
                    commandFactory.sendPostCommand(UrlClass.GetCarTypes, jo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(RegisterCarActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
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

            case R.id.create_account:
                if (car_model_id.isEmpty()) {
                    Toast.makeText(this, getString(R.string.err_car_model), Toast.LENGTH_SHORT).show();
                } else if (car_color_id.isEmpty()) {
                    Toast.makeText(this, getString(R.string.err_car_color), Toast.LENGTH_SHORT).show();
                } else if (car_type_id.isEmpty()) {
                    Toast.makeText(this, getString(R.string.err_car_type), Toast.LENGTH_SHORT).show();
                } else {
                    Register();
                }
                break;

            case R.id.terms_and_condition:
                i = new Intent(this, TermsAndCondActivity.class);
                startActivity(i);
                break;
        }
    }

    private void Register() {
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo = appConstant.registerJson;
                jo.put("CarModelID", car_model_id);
                jo.put("CarTypeID", car_type_id);
                jo.put("CarColorID", car_color_id);

                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.Register, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {
                                    Toast.makeText(RegisterCarActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(RegisterCarActivity.this, VerficationCodeActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(RegisterCarActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(RegisterCarActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                car_model_id = data.getStringExtra("car_model_id");
                img_car_model.setImageResource(R.drawable.ic_selected_blue);
                if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty()) {
                    create_account.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    create_account.setBackgroundResource(R.drawable.btn_blue);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                car_color_id = data.getStringExtra("car_color_id");
                color_car_img.setImageResource(R.drawable.ic_selected_blue);
                if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty()) {
                    create_account.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    create_account.setBackgroundResource(R.drawable.btn_blue);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onGetCarTypesFailed(JSONObject jsonObject) {
        super.onGetCarTypesFailed(jsonObject);
        hideDialog();
    }

    @Override
    public void onGetCarTypesSucessfully(JSONObject jsonObject) {
        super.onGetCarTypesSucessfully(jsonObject);
        hideDialog();
        Log.d(TAG, jsonObject.toString());
        try {
            int APIStatus = jsonObject.getInt("APIStatus");
            String APIMessage = jsonObject.getString("APIMessage");

            if (APIStatus == 1) {
                JSONArray jArray = jsonObject.getJSONArray("LstCarTypeList");

                fillCarType(jArray);
            } else {
                Toast.makeText(this, APIMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        carTypeAdapter = new CarTypeAdapter(this, itemtList);
        car_type_rec.setAdapter(carTypeAdapter);

        carTypeAdapter.setOnItemClickListener(new CarTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                car_type_id = itemtList.get(position).getTypeID();

                if (car_model_id.isEmpty() || car_color_id.isEmpty() || car_type_id.isEmpty()) {
                    create_account.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    create_account.setBackgroundResource(R.drawable.btn_blue);
                }
                carTypeAdapter.notifyDataSetChanged();
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



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("CarModelID", car_model_id);
        savedInstanceState.putString("CarTypeID", car_type_id);
        savedInstanceState.putString("CarColorID", car_color_id);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        car_model_id = savedInstanceState.getString("CarModelID");
        car_type_id = savedInstanceState.getString("CarTypeID");
        car_color_id = savedInstanceState.getString("CarColorID");
    }
}