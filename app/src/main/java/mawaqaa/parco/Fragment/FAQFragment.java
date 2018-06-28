package mawaqaa.parco.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

import mawaqaa.parco.Adapter.FaqAdapter;
import mawaqaa.parco.Model.FaqModels;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

/**
 * Created by HP on 4/10/2018.
 */

public class FAQFragment extends MainBaseFragment {

    private static String TAG = "FAQFragment";
    ListView faq_list;
    ArrayList<FaqModels> faqModelsArrayList;
    FaqAdapter faqAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.faq_fragment, container, false);

        faq_list = v.findViewById(R.id.faq_list);


        GetFAQ();

        return v;
    }

    private void GetFAQ() {
        if (Activity.isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    Activity.startSpinwheel(false, true);

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetFAQ, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Activity.stopSpinWheel();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {
                                    JSONArray jArray = new JSONArray();
                                    jArray = response.getJSONArray("lstFAQ");
                                    getFAQList(jArray);
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
                            Activity.stopSpinWheel();
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

    private void getFAQList(JSONArray jArray) {
        faqModelsArrayList = new ArrayList<FaqModels>();

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = jArray.getJSONObject(i);
                int s = i + 1;
                faqModelsArrayList.add(new FaqModels(
                        "" + s,
                        jo.getString("Question"),
                        jo.getString("Answer")
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
//        faqModelsArrayList.add(new FaqModels("1", "Are Parco proces fixed ? ", "Looong answer ........................"));
//        faqModelsArrayList.add(new FaqModels("2", "Are Parco proces fixed ? ", "Looong answer 2......................."));
//        faqModelsArrayList.add(new FaqModels("3", "Are Parco proces fixed ? ", "Looong answer 3......................."));

        faqAdapter = new FaqAdapter(getActivity(), faqModelsArrayList);
        faq_list.setAdapter(faqAdapter);

    }
}
