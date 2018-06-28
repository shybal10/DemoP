package mawaqaa.parco.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import mawaqaa.parco.Activity.MyCArActivity;
import mawaqaa.parco.Model.CarPickUpModel;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.R;

/**
 * Created by Ayadi on 5/7/2018.
 */

public class CarPickUpFargAdpater extends RecyclerView.Adapter<CarPickUpFargAdpater.CarPickUpViewHolder> {

    Context context;
    List<CarPickUpModel> ResourcelList;
    static OnItemClickListener mItemClickListener;

    public CarPickUpFargAdpater(Context context, List<CarPickUpModel> resourcelList) {
        this.context = context;
        ResourcelList = resourcelList;
    }

    @Override
    public CarPickUpFargAdpater.CarPickUpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_pickup_frag_row, parent, false);
        CarPickUpFargAdpater.CarPickUpViewHolder viewholder = new CarPickUpFargAdpater.CarPickUpViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final CarPickUpFargAdpater.CarPickUpViewHolder holder, final int position) {
        final CarPickUpModel info = ResourcelList.get(position);
        holder.car_name.setText(info.getCarName());



        Glide.with(context)
                .load(UrlClass.ImageURL + info.getCarType())
                .into(holder.type_img);
    }

    @Override
    public int getItemCount() {
        return ResourcelList.size();
    }



    public static class CarPickUpViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView car_name;
        ImageView type_img , delete;


        public CarPickUpViewHolder(View itemView) {
            super(itemView);
            car_name = itemView.findViewById(R.id.car_name);
            type_img = itemView.findViewById(R.id.type_img);
            delete = itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getPosition());
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