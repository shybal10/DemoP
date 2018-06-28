package mawaqaa.parco.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;

public class TermsAndCondActivity extends AppCompatActivity {

    private static String TAG = "TermsAndCond";
    TextView content, done;
    RequestQueue requestQueue;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_cond_activity);
        content = findViewById(R.id.content);
        done = findViewById(R.id.done);

        requestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(TermsAndCondActivity.this);
        pDialog.setMessage(TermsAndCondActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        GetTermsConditions();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void GetTermsConditions() {
        JSONObject jo = new JSONObject();
        try {
            jo.put(appConstant.APIKey, 123456);
            jo.put(appConstant.LanguageID, 1);

            JsonObjectRequest postRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    UrlClass.GetTermsConditions,

                    jo,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            hideDialog();
                            try {
                                int  IsSuccess = response.getInt("APIStatus");
                                String  msg = response.getString("APIMessage");
                                if (IsSuccess == 1) {

                                    content.setText(Html.fromHtml(response.getString("Content")));
                                } else {
                                    Toast.makeText(TermsAndCondActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //  Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //   Handle Error
                            hideDialog();
                            Log.e("Wt", error.toString());
                        }
                    });
            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                    2,
                    2));
            requestQueue.add(postRequest);
        } catch (JSONException e) {
            e.printStackTrace();
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
