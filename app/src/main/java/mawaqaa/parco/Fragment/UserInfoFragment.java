package mawaqaa.parco.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mawaqaa.parco.Activity.ForgetPasswordActivity;
import mawaqaa.parco.R;

/**
 * Created by HP on 4/10/2018.
 */

public class UserInfoFragment extends Fragment implements View.OnClickListener {

    Button bunChangePass;

    TextView userInfo_update, fName, lName, email_userinfo, textView_edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.user_info_fragment, container, false);

        defineView(v);


        return v;
    }

    private void defineView(View v) {
        bunChangePass = v.findViewById(R.id.bunChangePass);
        userInfo_update = v.findViewById(R.id.userInfo_update);
        textView_edit = v.findViewById(R.id.textView_edit);


        textView_edit.setOnClickListener(this);

        userInfo_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        bunChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_edit:

                enableEdit();

                break;

            case R.id.userInfo_update:

                break;


            default:
                break;
        }
    }

    private void enableEdit() {



    }
}
