package mawaqaa.parco.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import mawaqaa.parco.Fragment.AddCarFragment;
import mawaqaa.parco.Model.CarTypeModel;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.R;

/**
 * Created by Ayadi on 4/28/2018.
 */

public class CarTypeFragmentAdapter extends RecyclerView.Adapter<CarTypeFragmentAdapter.CarColorHolder> {

    static OnItemClickListener mItemClickListener;
    Context context;
    List<CarTypeModel> cartypeList;

    public CarTypeFragmentAdapter(Context context, List<CarTypeModel> resourcelList) {
        this.context = context;
        cartypeList = resourcelList;
    }

    @Override
    public CarTypeFragmentAdapter.CarColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_type_row, parent, false);
        CarTypeFragmentAdapter.CarColorHolder viewholder = new CarTypeFragmentAdapter.CarColorHolder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(CarTypeFragmentAdapter.CarColorHolder holder, final int position) {
        CarTypeModel info = cartypeList.get(position);
        holder.name_model.setText(info.getTypeName());

        Glide.with(context)
                .load(UrlClass.ImageURL + info.getTypeImage())
                .into(holder.color_img);


        if (info.getTypeID().equalsIgnoreCase(AddCarFragment.car_type_id)) {
            holder.parent_linear.setBackgroundColor(context.getResources().getColor(R.color.blue));
        } else {
            holder.parent_linear.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return cartypeList.size();
    }


    public static class CarColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_model;
        ImageView color_img;
        LinearLayout parent_linear ;

        public CarColorHolder(View itemView) {
            super(itemView);
            name_model = itemView.findViewById(R.id.name_model);
            color_img = itemView.findViewById(R.id.color_img);
            parent_linear = itemView.findViewById(R.id.parent_linear);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());

            }
        }
    }

    public void setOnItemClickListener(final CarTypeFragmentAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
