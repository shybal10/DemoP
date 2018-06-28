package mawaqaa.parco.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mawaqaa.parco.Activity.MainActivity;
import mawaqaa.parco.R;


public class AdditionalServiceFragment extends MainBaseFragment implements View.OnClickListener {

    private static String TAG = "AdditionalServiceF";
    View v;
    TextView servicesText;
    Button yes_btn, no_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.additional_service_fragment, container, false);
        servicesText = (TextView) v.findViewById(R.id.additional_services_text);
        servicesText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "corbel.ttf"));

        yes_btn = v.findViewById(R.id.yes_btn);
        no_btn = v.findViewById(R.id.no_btn);
        yes_btn.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "corbel.ttf"));
        no_btn.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "corbel.ttf"));
        yes_btn.setOnClickListener(this);
        no_btn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yes_btn:
                yes_btn.setBackgroundResource(R.drawable.btn_gray);
                MainActivity.IstAdditionalService = true;
                Activity.pushFragments(new YesAdditionalFragment(), false, true);
                break;

            case R.id.no_btn:
                no_btn.setBackgroundResource(R.drawable.btn_gray);
                MainActivity.IstAdditionalService = false;
                Activity.pushFragments(new PaymentFragment(), false, true);
                break;
        }
    }
}