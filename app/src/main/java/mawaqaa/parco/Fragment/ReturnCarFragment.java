package mawaqaa.parco.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import mawaqaa.parco.Activity.MainBaseActivity;
import mawaqaa.parco.R;

public class ReturnCarFragment extends MainBaseFragment {

    private static String TAG = "ReturnCarFragment";
    View v;
    LinearLayout return_my_car, return_my_key;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.return_car_fragment, container, false);
        return_my_car = v.findViewById(R.id.return_my_car);
        return_my_key = v.findViewById(R.id.return_my_key);

        return_my_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity.pushFragments(new AdditionalServiceFragment(), false, true);
            }
        });

        return_my_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity.pushFragments(new AdditionalServiceFragment(), false, true);
            }
        });
        return v;
    }

}
