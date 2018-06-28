package mawaqaa.parco.Activity;


import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import mawaqaa.parco.Adapter.NotificationAdapter;
import mawaqaa.parco.Model.NotificationModel;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

public class NotificationActivity extends MainBaseActivity {

    private static String TAG = "NotificationActivity";

    RecyclerView noti_rec;
    static List<NotificationModel> itemtList = new ArrayList<NotificationModel>();
    private LinearLayoutManager linearLayoutManager;
    String[] title = {"Notification 1", "Notification 2 ", " Notification 3 ", "Notification 4 "};
    String[] msg = {" msg 1", " msg 2", "msg 3", "msg 4"};
    String[] date = {" 14/2", " 21/8", "5/6", "30/12"};

    NotificationAdapter notiAdapter;
    private ProgressDialog pDialog;
    LinearLayout no_notification;
    TextView heading;

    SharedPrefsUtils myPref ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);
        heading = (TextView) findViewById(R.id.notification_heading);
        heading.setTypeface(Typeface.createFromAsset(getAssets(),"corbelbold.ttf"));

        noti_rec = findViewById(R.id.noti_rec);
        linearLayoutManager = new LinearLayoutManager(this);
        noti_rec.setLayoutManager(linearLayoutManager);
        no_notification = findViewById(R.id.no_notification);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);


        GetNotificationsByUserID();
    }

    private void GetNotificationsByUserID() {
        String CustomerID = myPref.getStringPreference(this, appConstant.CustomerID);
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put("UserID", CustomerID);
                jo.put("UserTypeID", 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetNotificationsByUserID, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {

                                    JSONArray ListNotification = new JSONArray();
                                    ListNotification = response.getJSONArray("ListNotification");

                                    if (ListNotification.length() == 0) {
                                        no_notification.setVisibility(View.VISIBLE);
                                    } else {
                                        no_notification.setVisibility(View.GONE);
                                        fillNoti(ListNotification);
                                    }

                                } else {
                                    Toast.makeText(NotificationActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(NotificationActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void fillNoti(JSONArray listNotification) {
        itemtList.clear();

        for (int i = 0; i < listNotification.length(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo = listNotification.getJSONObject(i);
                itemtList.add(new NotificationModel(
                        jo.getString("Title"),
                        jo.getString("Message"),
                        jo.getString("Date")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        notiAdapter = new NotificationAdapter(this, itemtList);
        noti_rec.setAdapter(notiAdapter);

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