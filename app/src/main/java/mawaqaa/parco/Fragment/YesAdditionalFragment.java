package mawaqaa.parco.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import mawaqaa.parco.Adapter.FillAdapter;
import mawaqaa.parco.Adapter.FuelAdapter;
import mawaqaa.parco.Model.FuelModel;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

public class YesAdditionalFragment extends MainBaseFragment implements View.OnClickListener {

    private static String TAG = "YesAdditionalFrag";
    ImageView previous_fuel_img, next_fuel_img, previous_fill_img, next_fill_img;
    RecyclerView fuel_rec, fill_rec;
    View v;
    LinearLayoutManager mLinearLayoutManager, LinearLayoutManager;
    static List<FuelModel> fuelList = new ArrayList<FuelModel>();
    List<FuelModel> fillList = new ArrayList<FuelModel>();

    String[] fuelID = {"0", "0", "0"};
    String[] fuelName = {"91", "95", "98"};
    TextView another_txt, yes_txt, no_txt;
    JSONArray lstFuelType, lstFuelAmount;
    FuelAdapter fuelAdapter;
    FillAdapter fillAdapter;
    public static int fuelSelected = 0;
    public static int fillSelected = 0;
    private ProgressDialog pDialog;
    Button done_btn;
    String fuelTypeId, fuelAmpuntID;
    boolean isWashCar = false;

    String  ServiceName, ServiceFees;
    int carWashID ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.yes_additional_fragment, container, false);
        defineView();

        GetAdditionalServices();
        return v;
    }

    private void GetAdditionalServices() {
        if (Activity.isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put(appConstant.LanguageID, 1);


                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetAdditionalServices, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {

                                    JSONObject oAdditionalServicesList = new JSONObject();
                                    oAdditionalServicesList = response.getJSONObject("oAdditionalServicesList");
                                    lstFuelAmount = new JSONArray();
                                    lstFuelType = new JSONArray();
                                    JSONArray lstOtherSevices = new JSONArray();
                                    lstOtherSevices = oAdditionalServicesList.getJSONArray("lstOtherSevices");
                                    lstFuelType = oAdditionalServicesList.getJSONArray("lstFuelType");
                                    lstFuelAmount = oAdditionalServicesList.getJSONArray("lstFuelAmount");

                                    JSONObject jo = lstOtherSevices.getJSONObject(0);
                                    carWashID = jo.getInt("ID");
                                    ServiceName = jo.getString("ServiceName");
                                    ServiceFees = jo.getString("ServiceFees");

                                    another_txt.setText(ServiceName + " ( " + ServiceFees + " KD)");

                                    fillFuel(lstFuelType);
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

    private void defineView() {
        pDialog = new ProgressDialog(Activity);
        pDialog.setMessage(Activity.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);
        another_txt = v.findViewById(R.id.another_txt);
        fill_rec = v.findViewById(R.id.fill_rec);
        done_btn = v.findViewById(R.id.done_btn);
        yes_txt = v.findViewById(R.id.yes_txt);
        no_txt = v.findViewById(R.id.no_txt);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //fuel_rec.setLayoutManager(mLinearLayoutManager);
        fill_rec.setLayoutManager(LinearLayoutManager);

        yes_txt.setOnClickListener(this);
        no_txt.setOnClickListener(this);
//        fillFuel();
//        fillFill();

        done_btn.setOnClickListener(this);

    }

    private void fillFill(JSONArray lstFuelAmount) {
        fillList.clear();


        for (int i = 1; i < lstFuelAmount.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = lstFuelAmount.getJSONObject(i);
                fillList.add(new FuelModel(jo.getString("ID"), jo.getString("ServiceFees") + " KD"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fillAdapter = new FillAdapter(Activity, fillList);
        fill_rec.setAdapter(fillAdapter);

        fuelAmpuntID = fillList.get(0).getFuelId();

        fillAdapter.setOnItemClickListener(new FillAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                fuelAmpuntID = fillList.get(position).getFuelId();
            }
        });
    }

    private void fillFuel(JSONArray lstFuelType) {
        fuelList.clear();

        for (int i = 0; i < lstFuelType.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = lstFuelType.getJSONObject(i);
                fuelList.add(new FuelModel(jo.getString("ID"), jo.getString("ServiceName")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //fuelAdapter = new FuelAdapter(Activity, fuelList);
        //fuel_rec.setAdapter(fuelAdapter);

        //fuelTypeId = fuelList.get(0).getFuelId();

/*        fuelAdapter.setOnItemClickListener(new FuelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                fuelTypeId = fuelList.get(position).getFuelId();
            }
        });*/
        fillFill(lstFuelAmount);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.done_btn:
                MainActivity.LsAdditionalService = new JSONArray();
                int amount = Integer.parseInt(fuelAmpuntID);
                MainActivity.LsAdditionalService.put(amount);
                if (isWashCar){
                    MainActivity.LsAdditionalService.put(carWashID);
                }

                Activity.pushFragments(new PaymentFragment(), false, true);
                break;

            case R.id.yes_txt:
                isWashCar = true;
                break;

            case R.id.no_txt:
                isWashCar = false;
                break;
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
