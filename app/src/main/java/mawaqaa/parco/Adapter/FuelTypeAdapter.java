package mawaqaa.parco.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mawaqaa.parco.Model.FuelModel;
import mawaqaa.parco.R;

public class FuelTypeAdapter extends RecyclerView.Adapter<FuelTypeAdapter.ItemViewHolder> {

    static OnItemClickListener mItemClickListener;

    Context context;
    List<FuelModel> fuelModelList;

    public FuelTypeAdapter(Context context, List<FuelModel> resourcelList) {
        this.context = context;
        fuelModelList = resourcelList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fuel_types_list,parent,false);
       return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.fuelName.setText(fuelModelList.get(position).getFuelName());
    }

    @Override
    public int getItemCount() {
        return fuelModelList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView fuelName;
        public ItemViewHolder(View itemView) {
            super(itemView);
            fuelName = itemView.findViewById(R.id.fuel_types_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());

            }
        }
    }

    public void setOnItemClickListener(final FuelTypeAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
