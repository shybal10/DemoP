package mawaqaa.parco.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mawaqaa.parco.Model.CurrentPickupModel;
import mawaqaa.parco.R;

/**
 * Created by HP on 4/15/2018.
 */

public class CurrentPickupsAdapter extends RecyclerView.Adapter<CurrentPickupsAdapter.ViewHolder> {

    Context context;
    static OnItemClickListener mItemClickListener;

    List<CurrentPickupModel> currentPickupModelList;

    public CurrentPickupsAdapter(Context context, List<CurrentPickupModel> currentPickupModels) {
        this.context = context;
        this.currentPickupModelList = currentPickupModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_pickups_item_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.current_TV_city.setText(currentPickupModelList.get(position).getCurrentCity());
        holder.current_TV__status.setText(currentPickupModelList.get(position).getCurrentStatus());
    }

    @Override
    public int getItemCount() {
        return currentPickupModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView current_TV_city, current_TV__status;
        ImageView current_ImV__arrow;


        public ViewHolder(View itemView) {
            super(itemView);
            current_TV_city = itemView.findViewById(R.id.current_TV_city);
            current_TV__status = itemView.findViewById(R.id.current_TV__status);
            current_ImV__arrow = itemView.findViewById(R.id.current_ImV__arrow);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

}
