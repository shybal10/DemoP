package mawaqaa.parco.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mawaqaa.parco.R;

import static mawaqaa.parco.Activity.MainActivity.main_container;

/**
 * Created by HP on 4/15/2018.
 */

public class LanguageFragment extends MainBaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.language_fragment, container, false);

        defineView(v);

        return v;
    }

    private void defineView(View v) {
    }
}