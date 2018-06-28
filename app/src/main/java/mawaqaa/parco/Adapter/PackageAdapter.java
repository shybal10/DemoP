package mawaqaa.parco.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mawaqaa.parco.Model.packageModel;
import mawaqaa.parco.R;

/**
 * Created by Ayadi on 4/19/2018.
 */

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageHolder> {

    static OnItemClickListener mItemClickListener;
    Context context;
    List<packageModel> packagesList;

    public PackageAdapter(Context context, List<packageModel> resourcelList) {
        this.context = context;
        packagesList = resourcelList;
    }

    @Override
    public PackageAdapter.PackageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_row, parent, false);
        PackageAdapter.PackageHolder viewholder = new PackageAdapter.PackageHolder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(PackageAdapter.PackageHolder holder, final int position) {
        packageModel info = packagesList.get(position);
        holder.name_package.setText(info.getPackageName());

        holder.parking.setText(info.getParkingCount());
        holder.credit.setText(info.getCreditAmount());
        holder.wash.setText(info.getCarWashCount());
        holder.fual.setText(info.getFuelingCount());
    }

    @Override
    public int getItemCount() {
        return packagesList.size();
    }


    public static class PackageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_package , credit , wash , fual , parking;

        public PackageHolder(View itemView) {
            super(itemView);
            name_package = itemView.findViewById(R.id.name_package);
            credit = itemView.findViewById(R.id.credit);
            wash = itemView.findViewById(R.id.wash);
            fual = itemView.findViewById(R.id.fual);
            parking = itemView.findViewById(R.id.parking);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public void setOnItemClickListener(final PackageAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
