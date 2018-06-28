package mawaqaa.parco.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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

import mawaqaa.parco.Adapter.PrepaidAdapter;
import mawaqaa.parco.Adapter.PrepaidCreditAdapter;
import mawaqaa.parco.Model.PrepaidCreditModel;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

public class PrepaidCreditActivity extends MainBaseActivity {
    private static String TAG = "PrepaidCreditActivity";
    TextView total_credit;
    PrepaidCreditAdapter prepaidCreditAdapter;
    PrepaidAdapter prepaidAdapter;
    List<PrepaidCreditModel> prepaidCreditList;
    //    GridView gridview_prepaidCards;
    RecyclerView prepaid_rec;
    private ProgressDialog pDialog;
    public static int postionSelected = -1;
    Button purchase ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prepaid_credit_activity);

        defineView();

        GetParcoCredit();
    }

    private void GetParcoCredit() {
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);

                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetParcoCredit, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {
                                    JSONObject oCustomerParcoCredit = new JSONObject();
                                    oCustomerParcoCredit = response.getJSONObject("oCustomerParcoCredit");
                                    total_credit.setText(oCustomerParcoCredit.getString("TotolParcoCredit") + " KD");

                                    JSONArray lstNewParcoCredit = oCustomerParcoCredit.getJSONArray("lstNewParcoCredit");
                                    fillPrepaidCreditList(lstNewParcoCredit);

                                } else {
                                    Toast.makeText(PrepaidCreditActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(PrepaidCreditActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void fillPrepaidCreditList(JSONArray lstNewParcoCredit) {
        prepaidCreditList = new ArrayList<PrepaidCreditModel>();

        for (int i = 0; i < lstNewParcoCredit.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = lstNewParcoCredit.getJSONObject(i);
                prepaidCreditList.add(new PrepaidCreditModel(jo.getString("ID"), jo.getString("Amount")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prepaid_rec.setLayoutManager(new GridLayoutManager(this, 4));
        prepaidAdapter = new PrepaidAdapter(this ,prepaidCreditList);
        prepaid_rec.setAdapter(prepaidAdapter);

        prepaidAdapter.setOnItemClickListener(new PrepaidAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                postionSelected = position;
                prepaidAdapter.notifyDataSetChanged();
            }
        });
    }

    private void defineView() {
//        gridview_prepaidCards = findViewById(R.id.gridview_prepaidCards);
        prepaid_rec = findViewById(R.id.prepaid_rec);

        total_credit = findViewById(R.id.total_credit);

        pDialog = new ProgressDialog(PrepaidCreditActivity.this);
        pDialog.setMessage(PrepaidCreditActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

    }

    private void getPrepaidCreditList() {
        prepaidCreditList = new ArrayList<PrepaidCreditModel>();

//        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
//        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
//        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
//        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
//        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
//        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
//        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
//        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
//
//        prepaidCreditAdapter = new PrepaidCreditAdapter(this,prepaidCreditList);
//        gridview_prepaidCards.setAdapter(prepaidCreditAdapter);
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
