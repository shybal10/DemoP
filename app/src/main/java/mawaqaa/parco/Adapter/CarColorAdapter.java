package mawaqaa.parco.Adapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import mawaqaa.parco.Model.ColorModel;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.R;
/**
 * Created by Ayadi on 4/11/2018.
 */
public class CarColorAdapter extends RecyclerView.Adapter<CarColorAdapter.CarColorHolder> {

    static OnItemClickListener mItemClickListener;
    Context context;
    List<ColorModel> carColorList;

    public CarColorAdapter(Context context, List<ColorModel> resourcelList) {
        this.context = context;
        carColorList = resourcelList;
    }

    @Override
    public CarColorAdapter.CarColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_color_row, parent, false);
        CarColorAdapter.CarColorHolder viewholder = new CarColorAdapter.CarColorHolder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(CarColorAdapter.CarColorHolder holder, final int position) {
        ColorModel info = carColorList.get(position);
        Typeface bold = Typeface.createFromAsset(context.getAssets(),"corbelbold.ttf");
        holder.name_model.setText(info.getNameColor());
        holder.name_model.setTypeface(bold);
//        holder.color_img.setBackgroundColor(Color.parseColor(info.getHashColor()));

        Glide.with(context)
                .load(UrlClass.ImageURL+ info.getHashColor())
//                .apply(RequestOptions.circleCropTransform())
                .into(holder.color_img);
    }

    @Override
    public int getItemCount() {
        return carColorList.size();
    }


    public static class CarColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_model;
        ImageView color_img;

        public CarColorHolder(View itemView) {
            super(itemView);
            name_model = itemView.findViewById(R.id.name_model);
            color_img = itemView.findViewById(R.id.color_img);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());

            }
        }
    }

    public void setOnItemClickListener(final CarColorAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
