package mawaqaa.parco.Adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mawaqaa.parco.Model.FuelModel;
import mawaqaa.parco.R;

import static mawaqaa.parco.Fragment.YesAdditionalFragment.fuelSelected;

/**
 * Created by Ayadi on 4/17/2018.
 */

public class FuelAdapter extends RecyclerView.Adapter<FuelAdapter.FuelViewHolder> {

    static OnItemClickListener mItemClickListener;
    Context context;
    List<FuelModel> fuelList;
    private final int screenWidth;

    public FuelAdapter(Context context, List<FuelModel> resourcelList) {
        this.context = context;
        fuelList = resourcelList;
        screenWidth = getScreenWidth(context);
    }
    //public static void main String args[]

    @Override
    public FuelAdapter.FuelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fuel_item, parent, false);
        view.getLayoutParams().width = screenWidth / 4;
        FuelAdapter.FuelViewHolder viewholder = new FuelAdapter.FuelViewHolder(view, context);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(FuelAdapter.FuelViewHolder holder, final int position) {
        FuelModel info = fuelList.get(position);
        holder.fuel_name.setText(info.getFuelName());

        if (fuelSelected == position) {
            holder.v.setVisibility(View.VISIBLE);
            holder.fuel_name.setTextColor(context.getResources().getColor(R.color.white));
            holder.fuel_name.setBackgroundResource(R.drawable.blue_circle_2x);
        } else {
            holder.v.setVisibility(View.GONE);
            holder.fuel_name.setTextColor(context.getResources().getColor(R.color.black));
            holder.fuel_name.setBackgroundResource(R.drawable.gray_circle_2x);

        }

        holder.fuel_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fuelSelected == position) {

                } else {
                    fuelSelected = position;

                    notifyDataSetChanged();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return fuelList.size();
    }

    public static class FuelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView fuel_name;
        LinearLayout llImg;
        View v;

        public FuelViewHolder(View itemView, Context context) {
            super(itemView);

            fuel_name = (TextView) itemView.findViewById(R.id.fuel_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public void setOnItemClickListener(final FuelAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    // Display#getSize(Point)
    @SuppressWarnings("deprecation")
    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}