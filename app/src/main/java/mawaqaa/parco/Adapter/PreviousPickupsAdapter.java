package mawaqaa.parco.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mawaqaa.parco.Model.PreviousPickupsModel;
import mawaqaa.parco.R;

/**
 * Created by HP on 4/12/2018.
 */

public class PreviousPickupsAdapter extends RecyclerView.Adapter<PreviousPickupsAdapter.ViewHolder> {

    Context context;
    List<PreviousPickupsModel> previousPickupsModels;
    static OnItemClickListener mItemClickListener;

    public PreviousPickupsAdapter(Context context, List<PreviousPickupsModel> previousPickupsModels) {
        this.context = context;
        this.previousPickupsModels = previousPickupsModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_pickups_item_row, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView_city.setText(previousPickupsModels.get(position).getCity());
        holder.textView_city.setText(previousPickupsModels.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return previousPickupsModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView_city, textView_status;
        ImageView imageView_arrow;


        public ViewHolder(View itemView) {
            super(itemView);
            textView_city = itemView.findViewById(R.id.textView_city);
            textView_status = itemView.findViewById(R.id.textView_status);
            imageView_arrow = itemView.findViewById(R.id.imageView_arrow);
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
