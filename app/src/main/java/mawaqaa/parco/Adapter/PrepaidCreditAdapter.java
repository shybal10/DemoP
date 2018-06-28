package mawaqaa.parco.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mawaqaa.parco.Model.PrepaidCreditModel;
import mawaqaa.parco.R;

/**
 * Created by HP on 4/16/2018.
 */

public class PrepaidCreditAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;

    List<PrepaidCreditModel> prepaidCreditList;

    public PrepaidCreditAdapter(Context context, List<PrepaidCreditModel> prepaidCreditList) {
        this.context = context;
        this.prepaidCreditList = prepaidCreditList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return prepaidCreditList.size();
    }

    @Override
    public Object getItem(int i) {
        PrepaidCreditModel prepaidCreditModel = prepaidCreditList.get(i);
        return prepaidCreditModel;
    }

    @Override
    public long getItemId(int i) {
        return  i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder vh;


        if (view == null) {
            view = inflater.inflate(R.layout.prepaid_credit_list_item, null);
            vh = new ViewHolder();
            view.setTag(vh);

        } else {
            vh = (ViewHolder) view.getTag();
        }
        return view;
    }

    public static class ViewHolder {
         TextView textView_sportIPlay;
    }
}
