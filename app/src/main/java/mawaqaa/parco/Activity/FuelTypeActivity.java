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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Adapter.CarColorAdapter;
import mawaqaa.parco.Adapter.FuelAdapter;
import mawaqaa.parco.Adapter.FuelTypeAdapter;
import mawaqaa.parco.Model.ColorModel;
import mawaqaa.parco.Model.FuelModel;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.CommandFactory;
import mawaqaa.parco.Volley.VolleyUtils;

public class FuelTypeActivity extends MainBaseActivity {
    private static String TAG = "FuelTypeActivity";

    RecyclerView recyclerView;
    FuelTypeAdapter fuelTypeAdapter;
    private ProgressDialog pDialog;
    TextView fuelType;
    static List<FuelModel> itemtList = new ArrayList<FuelModel>();
    Typeface bold;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_type);

        fuelType = (TextView) findViewById(R.id.fuel_type_heading_textview);
        bold = Typeface.createFromAsset(getAssets(), "corbelbold.ttf");
        fuelType.setTypeface(bold);
        pDialog = new ProgressDialog(FuelTypeActivity.this);
        pDialog.setMessage(FuelTypeActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);


        getFuelTypes();

    }

    private void getFuelTypes() {
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();
                    CommandFactory commandFactory = new CommandFactory();
                    commandFactory.sendPostCommand(UrlClass.GetFuelType, jo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(FuelTypeActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetFuelTypesFailed(JSONObject jsonObject) {
        super.onGetFuelTypesFailed(jsonObject);
        hideDialog();


    }

    @Override
    public void onGetFuelTypesSucessfully(JSONObject jsonObject) {
        super.onGetFuelTypesSucessfully(jsonObject);
        Log.d(TAG, jsonObject.toString());
        hideDialog();

        try {
            int APIStatus = jsonObject.getInt("APIStatus");
            String APIMessage = jsonObject.getString("APIMessage");

            if (APIStatus == 1) {
                JSONArray jArray = jsonObject.getJSONArray("ListFuelType");
                Toast.makeText(this, "" + jArray, Toast.LENGTH_LONG).show();


                fillFuelTypes(jArray);
            } else {
                Toast.makeText(this, APIMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillFuelTypes(JSONArray jArray) {
        itemtList.clear();
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = jArray.getJSONObject(i);
                itemtList.add(new FuelModel(jo.getString("ID"), jo.getString("FuelType")));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        recyclerView = (RecyclerView) findViewById(R.id.fuel_type_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        fuelTypeAdapter = new FuelTypeAdapter(this, itemtList);
        recyclerView.setAdapter(fuelTypeAdapter);

        fuelTypeAdapter.setOnItemClickListener(new FuelTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("fuel_type_id", itemtList.get(position).getFuelId());
                setResult(Activity.RESULT_OK, intent);
                finish();
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
