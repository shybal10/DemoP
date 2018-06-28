package mawaqaa.parco.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mawaqaa.parco.Model.ScheduledPickupModel;
import mawaqaa.parco.R;

/**
 * Created by HP on 4/15/2018.
 */

public class ScheduledPickupsAdapter extends RecyclerView.Adapter<ScheduledPickupsAdapter.ViewHolder>{

    Context context;
    List<ScheduledPickupModel> scheduledPickupList;
    static OnItemClickListener mItemClickListener;

    public ScheduledPickupsAdapter(Context context, List<ScheduledPickupModel> scheduledPickupList) {
        this.context = context;
        this.scheduledPickupList = scheduledPickupList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheduled_pickups_item_row, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.scheduled_TV_city.setText(scheduledPickupList.get(position).getScheduledCity());
        holder.scheduled_TV__status.setText(scheduledPickupList.get(position).getScheduledStatus());
    }

    @Override
    public int getItemCount() {
        return scheduledPickupList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView scheduled_TV_city, scheduled_TV__status;
        ImageView scheduled_ImV__arrow;


        public ViewHolder(View itemView) {
            super(itemView);
            scheduled_TV_city = itemView.findViewById(R.id.scheduled_TV_city);
            scheduled_TV__status = itemView.findViewById(R.id.scheduled_TV__status);
            scheduled_ImV__arrow = itemView.findViewById(R.id.scheduled_ImV__arrow);

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
