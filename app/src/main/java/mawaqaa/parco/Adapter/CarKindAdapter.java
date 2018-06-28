package mawaqaa.parco.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mawaqaa.parco.Model.CarKindModel;
import mawaqaa.parco.R;

/**
 * Created by Ayadi on 4/11/2018.
 */

public class CarKindAdapter extends RecyclerView.Adapter<CarKindAdapter.CarKindHolder> {

    static OnItemClickListener mItemClickListener;
    Context context;
    List<CarKindModel> carKindList;

    public CarKindAdapter(Context context, List<CarKindModel> resourcelList) {
        this.context = context;
        carKindList = resourcelList;
    }

    @Override
    public CarKindAdapter.CarKindHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_model_row, parent, false);
        CarKindAdapter.CarKindHolder viewholder = new CarKindAdapter.CarKindHolder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(CarKindAdapter.CarKindHolder holder, final int position) {
        CarKindModel info = carKindList.get(position);
        holder.name_model.setText(info.getNameModel());

    }

    @Override
    public int getItemCount() {
        return carKindList.size();
    }

    public void updateList(List<CarKindModel> temp) {
        carKindList = temp;
        notifyDataSetChanged();
    }

    public static class CarKindHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_model;

        public CarKindHolder(View itemView) {
            super(itemView);
            name_model = itemView.findViewById(R.id.name_model);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());

            }
        }
    }

    public void setOnItemClickListener(final CarKindAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}