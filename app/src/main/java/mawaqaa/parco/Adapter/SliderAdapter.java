package mawaqaa.parco.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.R;

/**
 * Created by Ayadi on 4/9/2018.
 */

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutinflater;
    String [] imgTutorial ;
    Typeface typeNormal;


    public SliderAdapter(Context context, String[] imgTutorial) {
        this.context = context;
        this.imgTutorial = imgTutorial ;
    }

//    public int[] slider_img = {
//            R.drawable.stop,
//            R.drawable.stop,
//            R.drawable.stop,
//            R.drawable.stop
//    };

//    public String[] slider_header_txt = {
//            "Parking can be hassle",
//            "Parking can be hassle",
//            "Parking can be hassle",
//            "Parking can be hassle"
//    };


    public String[] textInfo = {
            "Parking can be a hassle, We are here to make it hassle-free",
            "simply lets us know where your are by picking the pickup location",
            "Let us know when you would like your car returned and we will be there"
    };

    @Override
    public int getCount() {
        return imgTutorial.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutinflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutinflater.inflate(R.layout.slide_layout, container, false);

        ImageView slider_image = view.findViewById(R.id.slider_image);
//        TextView slider_header = view.findViewById(R.id.slider_header);

        //typeNormal = Typeface.createFromAsset(context.getAssets(),"corbel.ttf");
        TextView infoTextView = view.findViewById(R.id.textView2);
        infoTextView.setText(textInfo[position]);
       // infoTextView.setTypeface(typeNormal);

        Glide.with(context)
                .load(UrlClass.ImageURL+ imgTutorial[position])
//                .apply(RequestOptions.circleCropTransform())
                .into(slider_image);


//        slider_image.setImageResource(slider_img[position]);
//        slider_header.setText(slider_header_txt[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
