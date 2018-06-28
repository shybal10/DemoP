package mawaqaa.parco.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.CommandFactory;
import mawaqaa.parco.Volley.VolleyUtils;

public class RegisterActivity extends MainBaseActivity implements View.OnClickListener {

    private static String TAG = "RegisterActivity";
    Button next_btn;
    Intent i;
    TextInputLayout input_layout_fname, input_layout_lname, input_layout_email, input_layout_password, input_layout_repassword, input_layout_mobile;
    EditText input_fname, input_lname, input_email, input_password, input_repassword, input_mobile;
    LinearLayout date_relative;
    Calendar cal;
    int year;
    int month;
    int day;
    String dateToServer = "";

    RadioGroup radio_group;
    RadioButton male, female;
    int gender = 0;
    SharedPrefsUtils myPref;

    TextView day_txt, month_txt, year_txt,information;
    Typeface bold, normal;

    private ProgressDialog pDialog;

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (myPref.getStringPreference(RegisterActivity.this, appConstant.language_selected).equals(
                appConstant.HP_ENGLISH) || myPref.getStringPreference(RegisterActivity.this, appConstant.language_selected).isEmpty()) {
            myPref.setStringPreference(getApplicationContext(), appConstant.language_selected,
                    appConstant.HP_ENGLISH);
            Log.e(TAG, "Set locale to Arabic");
            setLanguage(appConstant.HP_ENGLISH);
        } else {
            myPref.setStringPreference(getApplicationContext(), appConstant.language_selected,
                    appConstant.HP_ARABIC);
            setLanguage(appConstant.HP_ARABIC);
            Log.e(TAG, "Set locale to English");
        }

        setContentView(R.layout.register_activity);
        information = (TextView) findViewById(R.id.information);

        bold = Typeface.createFromAsset(getAssets(),"corbelbold.ttf");
        normal = Typeface.createFromAsset(getAssets(),"corbel.ttf");
        information.setTypeface(bold);

        defineView();

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearP, int monthP, int dayP) {
                int monthS = monthP + 1;
                day_txt.setText("" + dayP);
                day_txt.setTypeface(normal);
                month_txt.setText("" + monthS);
                month_txt.setTypeface(normal);
                year_txt.setText("" + yearP);
                year_txt.setTypeface(normal);

                dateToServer = "" + yearP + "-" + monthS + "-" + dayP;
            }
        };
    }

    private void defineView() {
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage(RegisterActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        radio_group = findViewById(R.id.radio_group);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);


        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        next_btn = findViewById(R.id.next_btn);
        next_btn.setTypeface(bold);
        next_btn.setOnClickListener(this);
        date_relative = findViewById(R.id.date_relative);
        date_relative.setOnClickListener(this);
        input_layout_fname = findViewById(R.id.input_layout_fname);
        input_layout_fname.setTypeface(normal);
        input_layout_lname = findViewById(R.id.input_layout_lname);
        input_layout_lname.setTypeface(normal);
        input_layout_email = findViewById(R.id.input_layout_email);
        input_layout_email.setTypeface(normal);
        input_layout_password = findViewById(R.id.input_layout_password);
        input_layout_password.setTypeface(normal);
        input_layout_repassword = findViewById(R.id.input_layout_repassword);
        input_layout_repassword.setTypeface(normal);
        input_layout_mobile = findViewById(R.id.input_layout_mobile);
        input_layout_mobile.setTypeface(normal);

        input_fname = findViewById(R.id.input_fname);
        input_fname.setTypeface(normal);
        input_lname = findViewById(R.id.input_lname);
        input_lname.setTypeface(normal);
        input_email = findViewById(R.id.input_email);
        input_email.setTypeface(normal);

        input_password = findViewById(R.id.input_password);
        input_password.setTypeface(normal);

        input_repassword = findViewById(R.id.input_repassword);
        input_repassword.setTypeface(normal);

        input_mobile = findViewById(R.id.input_mobile);
        input_mobile.setTypeface(normal);
        day_txt = findViewById(R.id.day_txt);
        day_txt.setTypeface(normal);
        month_txt = findViewById(R.id.month_txt);
        month_txt.setTypeface(normal);
        year_txt = findViewById(R.id.year_txt);
        year_txt.setTypeface(normal);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.male) {
                    gender = 1;
                } else if (i == R.id.female) {
                    gender = 2;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:

                checkValue();
                break;

            case R.id.date_relative:
                DatePickerDialog dialog = new DatePickerDialog(
                        this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                break;
        }
    }

    private void checkValue() {
        if (!validateFName()) {
            return;
        }
        if (!validateLName()) {
            return;
        }
        if (dateToServer.isEmpty() || dateToServer.equalsIgnoreCase("")) {
            Toast.makeText(this, getString(R.string.err_msg_dob), Toast.LENGTH_SHORT).show();
            return;
        }
        if (gender == 0) {
            Toast.makeText(this, getString(R.string.err_msg_gender), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (!validateRePassword()) {
            return;
        }
        if (!validateMobile()) {
            return;
        }

        Register();
    }

    private void Register() {
        String FirstName = input_fname.getText().toString();
        String LastName = input_lname.getText().toString();
        String Email = input_email.getText().toString();
        String Password = input_password.getText().toString();
        String Mobile = input_mobile.getText().toString();

        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put("FirstName", FirstName);
                jo.put("LastName", LastName);
                jo.put("Email", Email);
                jo.put("Password", Password);
                jo.put("DateOfBirth", dateToServer);
                jo.put("Gender", gender);
                jo.put("Mobile", Mobile);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());

//                if (VolleyUtils.volleyEnabled) {
//                    showDialog();
//                    CommandFactory commandFactory = new CommandFactory();
//                    commandFactory.sendPostCommand(UrlClass.Register, jo);
//                }
                appConstant.registerJson = jo;
                appConstant.emailRegister = Email;
                i = new Intent(RegisterActivity.this, RegisterCarActivity.class);
                startActivity(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(RegisterActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateMobile() {
        if (input_mobile.getText().toString().trim().isEmpty()) {
            input_layout_mobile.setError(getString(R.string.err_msg_mobile));
            requestFocus(input_mobile);
            return false;
        } else {
            input_layout_mobile.setErrorEnabled(false);
        }

        return true;
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

    private boolean validateRePassword() {
        String pass = input_password.getText().toString();
        String rePass = input_repassword.getText().toString();

        if (rePass.isEmpty() || !rePass.equalsIgnoreCase(pass)) {
            input_layout_repassword.setError(getString(R.string.err_msg_repassword));
            requestFocus(input_repassword);
            return false;
        } else {
            input_layout_repassword.setErrorEnabled(false);
        }

        return true;
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

    private boolean validateLName() {
        if (input_lname.getText().toString().trim().isEmpty()) {
            input_layout_lname.setError(getString(R.string.err_msg_lname));
            requestFocus(input_lname);
            return false;
        } else {
            input_layout_lname.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateFName() {
        if (input_fname.getText().toString().trim().isEmpty()) {
            input_layout_fname.setError(getString(R.string.err_msg_fname));

            requestFocus(input_fname);
            return false;
        } else {
            input_layout_fname.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //    private void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }
    private void setLanguage(String lang) {
       /* String languageToLoad = lang;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());*/
        String languageToLoad = lang;
        Locale locale = new Locale(languageToLoad);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);

        onConfigurationChanged(conf);
//        this.getResources().updateConfiguration(conf, this.getResources().getDisplayMetrics());

    }

}