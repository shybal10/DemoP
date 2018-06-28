

package mawaqaa.parco.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import mawaqaa.parco.Activity.AccountActivity;
import mawaqaa.parco.Activity.LoginActivity;
import mawaqaa.parco.Activity.RegisterCarActivity;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.CommandFactory;
import mawaqaa.parco.Volley.VolleyUtils;

/**
 * Created by HP on 4/9/2018.
 */

public class AboutParco extends MainBaseFragment {

    private static String TAG = "AboutParco";
    View v;
    TextView about_txt, email_text, phone_txt, contact_us, tutorial_txt, website_txt;
    Typeface bold, normal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.about_fragment, container, false);
        about_txt = v.findViewById(R.id.about_txt);
        email_text = v.findViewById(R.id.email_text);
        phone_txt = v.findViewById(R.id.phone_txt);
        contact_us = v.findViewById(R.id.Contact_us_text);
        tutorial_txt = v.findViewById(R.id.tutorial_text);
        website_txt = v.findViewById(R.id.website_txt);
        bold = Typeface.createFromAsset(getActivity().getAssets(), "corbelbold.ttf");
        normal = Typeface.createFromAsset(getActivity().getAssets(), "corbelbold.ttf");
        website_txt.setTypeface(normal);
        contact_us.setTypeface(bold);
        about_txt.setTypeface(normal);
        email_text.setTypeface(normal);
        tutorial_txt.setTypeface(normal);
        phone_txt.setTypeface(normal);
        GetAboutApp();
        return v;
    }

    private void GetAboutApp() {
        if (Activity.isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    Activity.startSpinwheel(false, true);

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetAboutApp, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Activity.stopSpinWheel();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {
                                    about_txt.setText(Html.fromHtml(response.getString("Content")));
                                    email_text.setText(getString(R.string.email_info) + response.getString("Email"));
                                    phone_txt.setText(getString(R.string.phone_us));
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

}