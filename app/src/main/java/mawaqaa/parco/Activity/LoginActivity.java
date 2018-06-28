package mawaqaa.parco.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.CommandFactory;
import mawaqaa.parco.Volley.VolleyUtils;

public class LoginActivity extends MainBaseActivity implements View.OnClickListener {
    private static String TAG = "LoginActivity";

    TextView login_btn, forgetpass_txt;
    Intent i;
    TextInputLayout input_layout_email, input_layout_password;
    EditText input_email, input_password;
    private ProgressDialog pDialog;
    SharedPrefsUtils myPref;
    Typeface typBold,typBoldItalic,typeItalic,typenormal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        typBold = Typeface.createFromAsset(getAssets(),"corbelbold.ttf");
        typBoldItalic = Typeface.createFromAsset(getAssets(),"corbelbolditalic.ttf");
        typeItalic = Typeface.createFromAsset(getAssets(),"corbelitalic.ttf");
        typenormal = Typeface.createFromAsset(getAssets(),"corbel.ttf");
        defindView();
    }

    private void defindView() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage(LoginActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);
        login_btn = findViewById(R.id.login_btn);
        login_btn.setTypeface(typBold);
        forgetpass_txt = findViewById(R.id.forgetpass_txt);
        forgetpass_txt.setTypeface(typBold);
        input_layout_email = findViewById(R.id.input_layout_email);
        input_layout_email.setTypeface(typenormal);
        input_layout_password = findViewById(R.id.input_layout_password);
        input_layout_password.setTypeface(typenormal);
        input_email = findViewById(R.id.input_email);
        input_email.setTypeface(typenormal);
        input_password = findViewById(R.id.input_password);
        input_password.setTypeface(typenormal);

        login_btn.setOnClickListener(this);
        forgetpass_txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                checkValue();
                break;

            case R.id.forgetpass_txt:
                i = new Intent(this, ForgetPasswordActivity.class);
                startActivity(i);
                break;
        }
    }

    private void checkValue() {
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        Login();
    }

    private void Login() {
        String Email = input_email.getText().toString();
        String Password = input_password.getText().toString();
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put("Email", Email);
                jo.put("Password", Password);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());
                if (VolleyUtils.volleyEnabled) {
                    showDialog();
                    CommandFactory commandFactory = new CommandFactory();
                    commandFactory.sendPostCommand(UrlClass.Login, jo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateEmail() {
        String emailS = input_email.getText().toString().trim();
        if (emailS.isEmpty() || !isValidEmail(emailS)) {
            input_layout_email.setError(getString(R.string.err_msg_email));
            requestFocus(input_email);
            return false;
        } else {
            input_layout_email.setErrorEnabled(false);
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

    private boolean validatePassword() {
        if (input_password.getText().toString().trim().isEmpty()) {
            input_layout_password.setError(getString(R.string.err_msg_password));
            requestFocus(input_password);
            return false;
        } else {
            input_layout_password.setErrorEnabled(false);
        }

        return true;
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
    public void onLoginFailed(JSONObject jsonObject) {
        super.onLoginFailed(jsonObject);
        hideDialog();
    }


    @Override
    public void onLoginSucessfully(JSONObject jsonObject) {
        super.onLoginSucessfully(jsonObject);
        hideDialog();
        Log.d(TAG, jsonObject.toString());
        try {
            int APIStatus = jsonObject.getInt("APIStatus");
            String APIMessage = jsonObject.getString("APIMessage");

            if (APIStatus == 1) {
                String CustomerID = jsonObject.getString("CustomerID");
                String FirstName = jsonObject.getString("FirstName");
                myPref.setStringPreference(this, appConstant.CustomerID, CustomerID);
                myPref.setBooleanPreference(this, appConstant.isLoggedIn, true);
                myPref.setStringPreference(this, appConstant.UserName, FirstName);

                if (jsonObject.getString("Image")!=null){
                    myPref.setStringPreference(this, appConstant.CustomerImage, jsonObject.getString("Image"));
                }
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}