package mawaqaa.parco.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import mawaqaa.parco.Volley.VolleyUtils;

public class VerficationCodeActivity extends MainBaseActivity {

    private static String TAG = "VerficationCode";
    Button send ;
    EditText num1,num2,num3,num4 ;
    String codeS ;
    View first,second,third,fourth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verfication_code_activity);
        send  = findViewById(R.id.send);

        num1 = (EditText) findViewById(R.id.first_digit);
        num2 = (EditText) findViewById(R.id.second_digit);
        num3 = (EditText) findViewById(R.id.third_digit);
        num4 = (EditText) findViewById(R.id.fourth_digit);

        first = (View) findViewById(R.id.first_digit_view);
        second = (View) findViewById(R.id.second_digit_view);
        third = (View) findViewById(R.id.third_digit_view);
        fourth = (View) findViewById(R.id.fourth_digit_view);
        num1.setTextColor(getResources().getColor(R.color.blue));
        first.setBackgroundColor(getResources().getColor(R.color.blue));

        send.setEnabled(false);
        num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (num1.length()==1) {
                    num2.getText().clear();
                    num2.requestFocus();
                }
            }
        });

        num2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (num2.length()==1) {
                    num2.setTextColor(getResources().getColor(R.color.blue));
                    second.setBackgroundColor(getResources().getColor(R.color.blue));
                    num3.getText().clear();
                    num3.requestFocus();
                }
            }
        });
        num3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (num3.length()==1) {
                    num3.setTextColor(getResources().getColor(R.color.blue));
                    third.setBackgroundColor(getResources().getColor(R.color.blue));
                    num4.getText().clear();
                    num4.requestFocus();
                }
            }
        });

        num4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (num4.length()==1) {
                    num4.setTextColor(getResources().getColor(R.color.blue));
                    fourth.setBackgroundColor(getResources().getColor(R.color.blue));
                    codeS = num1.getText().toString() + num2.getText().toString() + num3.getText().toString() + num4.getText().toString();
                    checkValue();
                    send.setEnabled(true);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValue();
            }
        });
    }

    private void checkValue() {
        if (codeS.isEmpty()){
            Toast.makeText(this , getString(R.string.err_empty_code),Toast.LENGTH_SHORT).show();
        }else{
            if (isNetworkAvailable()) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put(appConstant.APIKey, 123456);
                    jo.put("Email", appConstant.emailRegister);
                    jo.put("Code", codeS);

                    Log.d(TAG, jo.toString());

                    if (VolleyUtils.volleyEnabled) {
                       startSpinwheel(false , true);
                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.CheckVerficationCode, jo, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                stopSpinWheel();
                                Log.d(TAG, response.toString());

                                try {
                                    int APIStatus = response.getInt("APIStatus");
                                    String APIMessage = response.getString("APIMessage");

                                    if (APIStatus == 1) {
                                        Toast.makeText(VerficationCodeActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(VerficationCodeActivity.this, LoginActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(VerficationCodeActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                stopSpinWheel();
                            }
                        });


                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                                5000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        requestQueue.add(jsonRequest);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(VerficationCodeActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
            }
        }
    }
}
