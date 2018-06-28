package mawaqaa.parco.Fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Adapter.CarColorAdapter;
import mawaqaa.parco.Model.ColorModel;
import mawaqaa.parco.R;

public class CarColorFragment extends MainBaseFragment {

    private static String TAG = "CarColorFragment";
    View v ;
    TextView colorText;
    RecyclerView car_color_rec ;
    private LinearLayoutManager linearLayoutManager;
    static List<ColorModel> itemtList = new ArrayList<ColorModel>();
    String[] carModelName = {"Green", "Red", "Pink", "Orange"};
    String[] carModelId = {"1", "2", "3", "4"};
    String[] colorHash = {"#13f62d","#e31c1c","#ff0074","#ffcb0d"} ;
    CarColorAdapter carColorAdapter ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.car_color_fragment, container, false);

        car_color_rec = v.findViewById(R.id.car_color_rec);
        linearLayoutManager = new LinearLayoutManager(Activity);
        car_color_rec.setLayoutManager(linearLayoutManager);
        colorText = (TextView) v.findViewById(R.id.textColor);
        colorText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"corbelbold.ttf"));


        fillCarColor();

        return v ;
    }

    private void fillCarColor() {
        itemtList.clear();

        for (int i = 0; i < carModelName.length; i++) {
            itemtList.add(new ColorModel(carModelId[i], carModelName[i] , colorHash[i]));
        }

        carColorAdapter = new CarColorAdapter(Activity , itemtList);
        car_color_rec.setAdapter(carColorAdapter);
    }


}
