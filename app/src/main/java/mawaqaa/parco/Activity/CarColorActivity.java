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

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Adapter.CarColorAdapter;
import mawaqaa.parco.Adapter.CarKindAdapter;
import mawaqaa.parco.Model.CarKindModel;
import mawaqaa.parco.Model.ColorModel;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.CommandFactory;
import mawaqaa.parco.Volley.VolleyUtils;

public class CarColorActivity extends MainBaseActivity {

    private static String TAG = "CarColorActivity";
    RecyclerView car_color_rec;
    private LinearLayoutManager linearLayoutManager;
    static List<ColorModel> itemtList = new ArrayList<ColorModel>();
    String[] carModelName = {"Green", "Red", "Pink", "Orange"};
    String[] carModelId = {"1", "2", "3", "4"};
    String[] colorHash = {"#13f62d", "#e31c1c", "#ff0074", "#ffcb0d"};
    CarColorAdapter carColorAdapter;
    TextView color;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_color_activity);

        pDialog = new ProgressDialog(CarColorActivity.this);
        pDialog.setMessage(CarColorActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        car_color_rec = findViewById(R.id.car_color_rec);
        color = (TextView) findViewById(R.id.color_text);
        color.setTypeface(Typeface.createFromAsset(getAssets(),"corbelbold.ttf"));
        linearLayoutManager = new LinearLayoutManager(this);
        car_color_rec.setLayoutManager(linearLayoutManager);

        GetCarColors();
    }

    private void GetCarColors() {
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();
                    CommandFactory commandFactory = new CommandFactory();
                    commandFactory.sendPostCommand(UrlClass.GetCarColors, jo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(CarColorActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void fillCarColor(JSONArray jArray) {
        itemtList.clear();

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = jArray.getJSONObject(i);
                itemtList.add(new ColorModel(jo.getString("ID"), jo.getString("ColorName"), jo.getString("ColorImage")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        carColorAdapter = new CarColorAdapter(this, itemtList);
        car_color_rec.setAdapter(carColorAdapter);

        carColorAdapter.setOnItemClickListener(new CarColorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("car_color_id", itemtList.get(position).getIdColor());
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onGetCarColorsSucessfully(JSONObject jsonObject) {
        super.onGetCarColorsSucessfully(jsonObject);
        Log.d(TAG, jsonObject.toString());
        hideDialog();

        try {
            int APIStatus = jsonObject.getInt("APIStatus");
            String APIMessage = jsonObject.getString("APIMessage");

            if (APIStatus == 1) {
                JSONArray jArray = jsonObject.getJSONArray("lstCarColorList");

                fillCarColor(jArray);
            } else {
                Toast.makeText(this, APIMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetCarColorsFailed(JSONObject jsonObject) {
        super.onGetCarColorsFailed(jsonObject);
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