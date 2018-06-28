package mawaqaa.parco.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mawaqaa.parco.Model.PrepaidCreditModel;
import mawaqaa.parco.R;

import static mawaqaa.parco.Activity.PrepaidCreditActivity.postionSelected;

/**
 * Created by Ayadi on 4/29/2018.
 */

public class PrepaidAdapter extends RecyclerView.Adapter<PrepaidAdapter.PrepaidHolder> {

    static OnItemClickListener mItemClickListener;
    Context context;
    List<PrepaidCreditModel> PrepaidList;

    public PrepaidAdapter(Context context, List<PrepaidCreditModel> resourcelList) {
        this.context = context;
        PrepaidList = resourcelList;
    }

    @Override
    public PrepaidAdapter.PrepaidHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prepaid_credit_list_item, parent, false);
        PrepaidAdapter.PrepaidHolder viewholder = new PrepaidAdapter.PrepaidHolder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(PrepaidAdapter.PrepaidHolder holder, final int position) {
        PrepaidCreditModel info = PrepaidList.get(position);
        holder.name.setText(info.getCreditAmount());

        if (position == postionSelected) {
            holder.name.setBackgroundResource(R.drawable.blue_circle_2x);
        } else {
            holder.name.setBackgroundResource(R.drawable.gray_circle_2x);
        }
    }

    @Override
    public int getItemCount() {
        return PrepaidList.size();
    }


    public static class PrepaidHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;

        public PrepaidHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());

            }
        }
    }

    public void setOnItemClickListener(final PrepaidAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
