package mawaqaa.parco.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import java.util.Locale;

import mawaqaa.parco.Adapter.SliderAdapter;
import mawaqaa.parco.Fragment.MainBaseFragment;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.VolleyUtils;

public class SplashActivity extends MainBaseActivity implements View.OnClickListener {
    private static String TAG = "SplashActivity";
    TextView skip;
    ViewPager viewpager;
    private LinearLayout myDotLinear;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private int currentPage;
    SharedPrefsUtils myPref;
    boolean isLogin;
    Intent i;
    private ProgressDialog pDialog;
    int numberImage;
    Typeface typBold,typBoldItalic,typeItalic,typenormal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        typBold = Typeface.createFromAsset(getAssets(),"corbelbold.ttf");
        typBoldItalic = Typeface.createFromAsset(getAssets(),"corbelbolditalic.ttf");
        typeItalic = Typeface.createFromAsset(getAssets(),"corbelitalic.ttf");
        typenormal = Typeface.createFromAsset(getAssets(),"corbel.ttf");

        if (myPref.getStringPreference(SplashActivity.this, appConstant.language_selected).equals(
                appConstant.HP_ENGLISH) || myPref.getStringPreference(SplashActivity.this, appConstant.language_selected).isEmpty()) {
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

        setContentView(R.layout.splash_activity);

        definView();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void definView() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);

        isLogin = myPref.getBooleanPreference(this, appConstant.isLoggedIn, false);
        skip = findViewById(R.id.skip);
        skip.setTypeface(typBold);
        viewpager = findViewById(R.id.viewpager);
        myDotLinear = findViewById(R.id.dot_linear);

//        sliderAdapter = new SliderAdapter(this);
//
//        viewpager.setAdapter(sliderAdapter);
//        addDotsIndicator(0);
//        viewpager.addOnPageChangeListener(viewListner);

        skip.setOnClickListener(this);

        GetAppTutorial();

    }

    private void GetAppTutorial() {
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put(appConstant.LanguageID, 1);
                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UrlClass.GetAppTutorial, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Log.d(TAG, response.toString());

                            try {
                                int APIStatus = response.getInt("APIStatus");
                                String APIMessage = response.getString("APIMessage");

                                if (APIStatus == 1) {
                                    JSONArray lstAppTutorialList = new JSONArray();
                                    lstAppTutorialList = response.getJSONArray("lstAppTutorialList");
                                    String[] ImgTutorial = new String[lstAppTutorialList.length()];
                                    numberImage = lstAppTutorialList.length();
                                    for (int i = 0; i < lstAppTutorialList.length(); i++) {
                                        JSONObject jo = new JSONObject();
                                        jo = lstAppTutorialList.getJSONObject(i);
                                        ImgTutorial[i] = jo.getString("ImagePath");
                                    }

                                    sliderAdapter = new SliderAdapter(SplashActivity.this, ImgTutorial);

                                    viewpager.setAdapter(sliderAdapter);
                                    addDotsIndicator(0);
                                    viewpager.addOnPageChangeListener(viewListner);

                                } else {
                                    Toast.makeText(SplashActivity.this, APIMessage, Toast.LENGTH_SHORT).show();
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


                    RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
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
            Toast.makeText(SplashActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public void addDotsIndicator(int postion) {
        mDots = new TextView[numberImage];
        myDotLinear.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.gray));
            myDotLinear.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[postion].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip:
                if (isLogin) {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    i = new Intent(this, LoginOrRegisterActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
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