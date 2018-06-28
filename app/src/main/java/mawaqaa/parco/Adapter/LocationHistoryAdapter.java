package mawaqaa.parco.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mawaqaa.parco.R;

public class LocationHistoryAdapter extends RecyclerView.Adapter<LocationHistoryAdapter.ItemViewHolder> {
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_history_item,parent,false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
    }


    @Override
    public int getItemCount() {
        return 5;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView locationName;
        public ItemViewHolder(View itemView) {
            super(itemView);
            locationName = (TextView) itemView.findViewById(R.id.location_name);
        }
    }
}
