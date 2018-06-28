package mawaqaa.parco.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import mawaqaa.parco.Activity.MyCArActivity;
import mawaqaa.parco.Model.CarPickUpModel;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.R;

/**
 * Created by Ayadi on 4/15/2018.
 */

public class CarPickUpAdapter extends RecyclerSwipeAdapter<CarPickUpAdapter.CarPickUpViewHolder> {

    Context context;
    List<CarPickUpModel> ResourcelList;
    static OnItemClickListener mItemClickListener;

    public CarPickUpAdapter(Context context, List<CarPickUpModel> resourcelList) {
        this.context = context;
        ResourcelList = resourcelList;
    }

    @Override
    public CarPickUpAdapter.CarPickUpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_pickup_row, parent, false);
        CarPickUpViewHolder viewholder = new CarPickUpViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final CarPickUpAdapter.CarPickUpViewHolder holder, final int position) {
        final CarPickUpModel info = ResourcelList.get(position);
        holder.car_name.setText(info.getCarName());

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.delete));
            }
        });
        holder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(holder.swipeLayout);

                MyCArActivity.removeFromCart(position);
            }
        });

        Glide.with(context)
                .load(UrlClass.ImageURL + info.getCarType())
                .into(holder.type_img);
    }

    @Override
    public int getItemCount() {
        return ResourcelList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {

        return R.id.swipe;
    }

    public static class CarPickUpViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SwipeLayout swipeLayout;
        TextView car_name;
        ImageView type_img , delete;


        public CarPickUpViewHolder(View itemView) {
            super(itemView);
            car_name = itemView.findViewById(R.id.car_name);
            type_img = itemView.findViewById(R.id.type_img);
            swipeLayout = itemView.findViewById(R.id.swipe);
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