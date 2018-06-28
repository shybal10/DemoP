package mawaqaa.parco.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class RestPassWordActivity extends MainBaseActivity implements View.OnClickListener {

    private static String TAG = "RestPassWordActivity";
    LinearLayout new_pass_linear, check_linear;
    EditText code, new_pass_edit;
    Button check_btn, rest_btn;
    RequestQueue requestQueue;
    String code_s, pass_s;
    int IsSuccess;
    String msg, UserEmail, UserId;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_pass_word_activity);

        definView();
    }

    private void definView() {
        pDialog = new ProgressDialog(RestPassWordActivity.this);
        pDialog.setMessage(RestPassWordActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        UserEmail = getIntent().getStringExtra("UserEmail");
        requestQueue = Volley.newRequestQueue(this);

        new_pass_linear = findViewById(R.id.new_pass_linear);
        check_linear = findViewById(R.id.check_linear);

        code = findViewById(R.id.code);
        new_pass_edit = findViewById(R.id.new_pass_edit);

        check_btn = findViewById(R.id.check_btn);
        rest_btn = findViewById(R.id.rest_btn);

        new_pass_linear.setVisibility(View.GONE);

        check_btn.setOnClickListener(this);
        rest_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_btn:
                code_s = code.getText().toString();
                if (code_s.matches("")) {
                    Toast.makeText(this, getString(R.string.code_empty), Toast.LENGTH_SHORT).show();
                } else {
                    CheckCode();
                }
                break;

            case R.id.rest_btn:
                pass_s = new_pass_edit.getText().toString();
                if (pass_s.matches("")) {
                    Toast.makeText(this, getString(R.string.new_pass_empty), Toast.LENGTH_SHORT).show();
                } else {
                    ResetPassword();
                }
                break;
        }
    }

    private void ResetPassword() {
        JSONObject jo = new JSONObject();
        try {
            jo.put(appConstant.APIKey, 123456);
            jo.put("CustomerID", UserId);
            jo.put("NewPassword", pass_s);

            Log.d(TAG, jo.toString());

            showDialog();
            JsonObjectRequest postRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    UrlClass.ResetPassword,

                    jo,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                IsSuccess = response.getInt("APIStatus");
                                msg = response.getString("APIMessage");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            hideDialog();
                            if (IsSuccess == 1) {

                                Toast.makeText(RestPassWordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RestPassWordActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();

                            } else {
                                Toast.makeText(RestPassWordActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
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

    private void CheckCode() {
        JSONObject jo = new JSONObject();
        try {
            jo.put(appConstant.APIKey, 123456);
            jo.put("Email", UserEmail);
            jo.put("Code", code_s);
            jo.put("LanguageID", 1);

            Log.d(TAG, jo.toString());

            showDialog();
            JsonObjectRequest postRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    UrlClass.CheckCode,

                    jo,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                IsSuccess = response.getInt("APIStatus");
                                msg = response.getString("APIMessage");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hideDialog();
                            if (IsSuccess == 1) {
                                Toast.makeText(RestPassWordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                try {
                                    UserId = response.getString("CustomerID");
                                    new_pass_linear.setVisibility(View.VISIBLE);
                                    check_linear.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(RestPassWordActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
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