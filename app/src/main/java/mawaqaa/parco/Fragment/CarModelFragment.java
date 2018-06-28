package mawaqaa.parco.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Adapter.CarKindAdapter;
import mawaqaa.parco.Model.CarKindModel;
import mawaqaa.parco.R;

public class CarModelFragment extends MainBaseFragment {

    private static String TAG = "CarModelFragment";
    View v;

    TextView carModel;
    RecyclerView car_model_rec;
    static List<CarKindModel> itemtList = new ArrayList<CarKindModel>();
    private LinearLayoutManager linearLayoutManager;
    String[] carModelName = {"Aston Martin", "GMC", "Mitsubishi", "Mercedes"};
    String[] carModelId = {"1", "2", "3", "4"};

    CarKindAdapter carKindAdapter;

    EditText search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.car_model_fragment, container, false);
        //carModel = (TextView) v.findViewById(R.id.car_model_text);
        //carModel.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "corbelbold.ttf"));
        defineView();
        return v;
    }

    private void defineView() {
        car_model_rec = v.findViewById(R.id.car_model_rec);
        linearLayoutManager = new LinearLayoutManager(Activity);
        car_model_rec.setLayoutManager(linearLayoutManager);
        search = v.findViewById(R.id.search);
        //search.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "corbel.ttf"));


        fillCarModel();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
    }

    private void fillCarModel() {
        itemtList.clear();


        for (int i = 0; i < carModelName.length; i++) {
            itemtList.add(new CarKindModel(carModelId[i], carModelName[i]));
        }

        carKindAdapter = new CarKindAdapter(Activity, itemtList);
        car_model_rec.setAdapter(carKindAdapter);


    }

    void filter(String text) {
        List<CarKindModel> temp = new ArrayList();
        for (CarKindModel d : itemtList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getNameModel().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        carKindAdapter.updateList(temp);
    }

}
