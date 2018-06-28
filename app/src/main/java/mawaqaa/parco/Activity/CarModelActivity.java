package mawaqaa.parco.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Adapter.CarKindAdapter;
import mawaqaa.parco.Model.CarKindModel;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.CommandFactory;
import mawaqaa.parco.Volley.VolleyUtils;

public class CarModelActivity extends MainBaseActivity {

    private static String TAG = "CarModelActivity";
    RecyclerView car_model_rec;
    static List<CarKindModel> itemtList = new ArrayList<CarKindModel>();
    List<CarKindModel> temp;
    private LinearLayoutManager linearLayoutManager;
    String[] carModelName = {"Aston Martin", "GMC", "Mitsubishi", "Mercedes"};
    String[] carModelId = {"1", "2", "3", "4"};
    TextView car_model;

    CarKindAdapter carKindAdapter;

    EditText search;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        car_model = (TextView) findViewById(R.id.carModelText);
 //       car_model.setTypeface(Typeface.createFromAsset(getAssets(), "corbelbold.ttf"));
        setContentView(R.layout.car_model_activity);
        defineView();
    }

    private void defineView() {
        pDialog = new ProgressDialog(CarModelActivity.this);
        pDialog.setMessage(CarModelActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        car_model_rec = findViewById(R.id.car_model_rec);
        linearLayoutManager = new LinearLayoutManager(this);
        car_model_rec.setLayoutManager(linearLayoutManager);
        search = findViewById(R.id.search);
        search.setTypeface(Typeface.createFromAsset(getAssets(), "corbel.ttf"));
        search.setImeOptions(EditorInfo.IME_ACTION_DONE);
        if (isNetworkAvailable()) {
            GetCarModels();
        } else {
            Toast.makeText(CarModelActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
    }

    private void GetCarModels() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("APIKey", 123456);

            Log.d(TAG, jo.toString());

            if (VolleyUtils.volleyEnabled) {
                showDialog();
                CommandFactory commandFactory = new CommandFactory();
                commandFactory.sendPostCommand(UrlClass.GetCarModels, jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillCarModel(JSONArray jArray) {
        itemtList.clear();
        temp = new ArrayList<>();
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = jArray.getJSONObject(i);
                itemtList.add(new CarKindModel(jo.getString("ID"), jo.getString("CarModelName")));
                temp.add(new CarKindModel(jo.getString("ID"), jo.getString("CarModelName")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        carKindAdapter = new CarKindAdapter(this, temp);
        car_model_rec.setAdapter(carKindAdapter);

        carKindAdapter.setOnItemClickListener(new CarKindAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(CarModelActivity.this ,temp.get(position).getNameModel(),Toast.LENGTH_SHORT ).show();

                Intent intent = new Intent();
                intent.putExtra("car_model_id", temp.get(position).getIdModel());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    void filter(String text) {
        temp.clear();
        for (CarKindModel d : itemtList) {

            if (d.getNameModel().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        carKindAdapter.updateList(temp);
    }


    @Override
    public void onGetCarModelsSucessfully(JSONObject jsonObject) {
        super.onGetCarModelsSucessfully(jsonObject);
        Log.d(TAG, jsonObject.toString());
        hideDialog();

        try {
            int APIStatus = jsonObject.getInt("APIStatus");
            String APIMessage = jsonObject.getString("APIMessage");

            if (APIStatus == 1) {
                JSONArray jArray = jsonObject.getJSONArray("lstCarsModelList");

                fillCarModel(jArray);
            } else {
                Toast.makeText(this, APIMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onGetCarModelsFailed(JSONObject jsonObject) {
        super.onGetCarModelsFailed(jsonObject);
        hideDialog();
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