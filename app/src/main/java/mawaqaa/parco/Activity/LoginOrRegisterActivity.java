package mawaqaa.parco.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import mawaqaa.parco.R;

public class LoginOrRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView login , register;
    Intent i ;
    Typeface typBold,typBoldItalic,typeItalic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_or_register_activity);
        typBold = Typeface.createFromAsset(getAssets(),"corbelbold.ttf");
        typBoldItalic = Typeface.createFromAsset(getAssets(),"corbelbolditalic.ttf");
        typeItalic = Typeface.createFromAsset(getAssets(),"corbelitalic.ttf");
        Typeface typenormal= Typeface.createFromAsset(getAssets(),"corbel.ttf");

        login = findViewById(R.id.login);
        login.setTypeface(typBold);
        register = findViewById(R.id.register);
        register.setTypeface(typBold);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                i = new Intent(LoginOrRegisterActivity.this , LoginActivity.class);
                startActivity(i);
                break;

            case R.id.register :
                i = new Intent(LoginOrRegisterActivity.this , RegisterActivity.class);
                startActivity(i);
                break;
        }
    }
}