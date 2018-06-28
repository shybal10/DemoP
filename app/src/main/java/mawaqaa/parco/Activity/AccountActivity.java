package mawaqaa.parco.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;

public class AccountActivity extends MainBaseActivity implements View.OnClickListener {

    LinearLayout user_info_btn, history_btn, prepaid_btn, my_car_btn;
    TextView user_info,history,prepaid,my_car,accountText;
    ImageView user_img;
    Intent i;
    SharedPrefsUtils myPref ;
    Typeface typBold,typBoldItalic,typeItalic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        typBold = Typeface.createFromAsset(getAssets(),"corbelbold.ttf");
        typBoldItalic = Typeface.createFromAsset(getAssets(),"corbelbolditalic.ttf");
        typeItalic = Typeface.createFromAsset(getAssets(),"corbelitalic.ttf");
        defineView();
    }

    private void defineView() {
        accountText = (TextView) findViewById(R.id.account);
        user_info = (TextView) findViewById(R.id.user_info_textView);
        history = (TextView) findViewById(R.id.history_text_view);
        my_car = (TextView) findViewById(R.id.my_car_text_view);
        prepaid = (TextView) findViewById(R.id.prepaid_text_view);

        accountText.setTypeface(typBold);
        user_info.setTypeface(typBold);
        history.setTypeface(typBold);
        my_car.setTypeface(typBold);
        prepaid.setTypeface(typBold);



        user_info_btn = findViewById(R.id.user_info_btn);
        history_btn = findViewById(R.id.history_btn);
        prepaid_btn = findViewById(R.id.prepaid_btn);
        my_car_btn = findViewById(R.id.my_car_btn);
        user_img = findViewById(R.id.user_img);
        user_info_btn.setOnClickListener(this);
        history_btn.setOnClickListener(this);
        prepaid_btn.setOnClickListener(this);
        my_car_btn.setOnClickListener(this);

        String CustomerImage = myPref.getStringPreference(this, appConstant.CustomerImage );
        if (!CustomerImage.equals("") || !CustomerImage.isEmpty()){
            Glide.with(this)
                    .load(UrlClass.ImageURL + CustomerImage)
                    .apply(RequestOptions.circleCropTransform())
                    .into(user_img);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_btn:
                i = new Intent(AccountActivity.this, UserInfoActivity.class);
                startActivity(i);
                break;

            case R.id.history_btn:
                i = new Intent(AccountActivity.this, HistoryActivity.class);
                startActivity(i);
                break;

            case R.id.prepaid_btn:
                i = new Intent(AccountActivity.this, PrepaidCreditActivity.class);
                startActivity(i);
                break;

            case R.id.my_car_btn:
                i = new Intent(AccountActivity.this, MyCArActivity.class);
                startActivity(i);
                break;
        }
    }
}
