package mawaqaa.parco.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.CommandFactory;
import mawaqaa.parco.Volley.VolleyUtils;

/**
 * Created by HP on 4/10/2018.
 */

public class ForgetPasswordActivity extends MainBaseActivity {
    private static String TAG = "ForgetPassword";
    EditText input_email;
    Button forget_btn;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pass_activity);

        pDialog = new ProgressDialog(ForgetPasswordActivity.this);
        pDialog.setMessage(ForgetPasswordActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        input_email = findViewById(R.id.input_email);
        forget_btn = findViewById(R.id.forget_btn);
        forget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail()) {
                    return;
                }
                forget_btn.setBackgroundColor(getResources().getColor(R.color.gray));
                ForgotPassword();
            }
        });
    }

    private void ForgotPassword() {
        String Email = input_email.getText().toString();
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put("Email", Email);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();
                    CommandFactory commandFactory = new CommandFactory();
                    commandFactory.sendPostCommand(UrlClass.ForgotPassword, jo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(ForgetPasswordActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateEmail() {
        String emailS = input_email.getText().toString().trim();

        if (emailS.isEmpty() || !isValidEmail(emailS)) {
            Toast.makeText(this ,getString(R.string.err_msg_email) , Toast.LENGTH_SHORT ).show();
            requestFocus(input_email);
            return false;
        } else {
        }
        return true;
    }

    private boolean isValidEmail(String emailS) {
        return !TextUtils.isEmpty(emailS) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailS).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    @Override
    public void onForgotPasswordFailed(JSONObject jsonObject) {
        super.onForgotPasswordFailed(jsonObject);
        hideDialog();
    }

    @Override
    public void onForgotPasswordSucessfully(JSONObject jsonObject) {
        super.onForgotPasswordSucessfully(jsonObject);
        hideDialog();
        Log.d(TAG,jsonObject.toString());

        String APIMessage = null;
        try {
            int APIStatus = jsonObject.getInt("APIStatus");
            APIMessage = jsonObject.getString("APIMessage");

            if (APIStatus == 1) {
                String emailS = input_email.getText().toString().trim();
                Toast.makeText(ForgetPasswordActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ForgetPasswordActivity.this,RestPassWordActivity.class);
                i.putExtra("UserEmail",emailS);
                startActivity(i);
            }else{
                Toast.makeText(ForgetPasswordActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}