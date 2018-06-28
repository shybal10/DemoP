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

import static mawaqaa.parco.Fragment.YesAdditionalFragment.fillSelected;

/**
 * Created by Ayadi on 4/17/2018.
 */

public class FillAdapter extends RecyclerView.Adapter<FillAdapter.FillViewHolder> {

    static OnItemClickListener mItemClickListener;
    Context context;
    List<FuelModel> fuelList;
    private final int screenWidth;

    public FillAdapter(Context context, List<FuelModel> resourcelList) {
        this.context = context;
        fuelList = resourcelList;
        screenWidth = getScreenWidth(context);
    }

    @Override
    public FillAdapter.FillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fuel_item, parent, false);
        view.getLayoutParams().width = screenWidth / 4;
        FillAdapter.FillViewHolder viewholder = new FillAdapter.FillViewHolder(view, context);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(FillAdapter.FillViewHolder holder, final int position) {
        FuelModel info = fuelList.get(position);
        holder.fuel_name.setText(info.getFuelName());

        if (fillSelected == position) {
            holder.fuel_name.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            holder.fuel_name.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.fuel_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fillSelected == position) {

                } else {
                    fillSelected = position;

                    notifyDataSetChanged();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return fuelList.size();
    }

    public static class FillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView fuel_name;
        LinearLayout llImg;
        View v;

        public FillViewHolder(View itemView, Context context) {
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

    public void setOnItemClickListener(final FillAdapter.OnItemClickListener mItemClickListener) {
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