package mawaqaa.parco.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Adapter.PrepaidCreditAdapter;
import mawaqaa.parco.Model.PrepaidCreditModel;
import mawaqaa.parco.R;

/**
 * Created by HP on 4/15/2018.
 */

public class PrepaidCreditFragment extends Fragment {

    PrepaidCreditAdapter prepaidCreditAdapter;
    List<PrepaidCreditModel> prepaidCreditList;
    GridView gridview_prepaidCards;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.prepaid_credit_fragment, container, false);


        defineView(v);

        getPrepaidCreditList();

        return v;
    }

    private void defineView(View v) {
        gridview_prepaidCards = v.findViewById(R.id.gridview_prepaidCards);


    }

    private void getPrepaidCreditList() {

        prepaidCreditList = new ArrayList<PrepaidCreditModel>();

        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));
        prepaidCreditList.add(new PrepaidCreditModel("0","20 KD"));

        prepaidCreditAdapter = new PrepaidCreditAdapter(getActivity(),prepaidCreditList);
        gridview_prepaidCards.setAdapter(prepaidCreditAdapter);


    }
}
