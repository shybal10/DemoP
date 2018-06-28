package mawaqaa.parco.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mawaqaa.parco.Model.FaqModels;
import mawaqaa.parco.R;

/**
 * Created by HP on 4/10/2018.
 */

public class FaqAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context context;
    ArrayList<FaqModels> faqModelsArrayList;
    FaqModels faqModels;

    public FaqAdapter(Context context, ArrayList<FaqModels> faqModelsArrayList) {
        this.context = context;
        this.faqModelsArrayList = faqModelsArrayList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return faqModelsArrayList.size();
    }

    @Override
    public FaqModels getItem(int i) {
        FaqModels faqModels = faqModelsArrayList.get(i);

        return faqModels;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewholder;


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_item_faq, viewGroup, false);

            viewholder = new ViewHolder();

            viewholder.faqNum = convertView.findViewById(R.id.faqNum);
            viewholder.faqQuestion = convertView.findViewById(R.id.faqQuestion);
            viewholder.faqAnswer = convertView.findViewById(R.id.faqAnswer);


            convertView.setTag(viewholder);

        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        viewholder.faqNum.setText(faqModelsArrayList.get(i).getFaqId());
        viewholder.faqQuestion.setText(faqModelsArrayList.get(i).getFaqQuestion());
        viewholder.faqAnswer.setText(faqModelsArrayList.get(i).getFaqAnswer());


        return convertView;
    }

    public static class ViewHolder {
        TextView faqNum, faqQuestion, faqAnswer;

    }
}
