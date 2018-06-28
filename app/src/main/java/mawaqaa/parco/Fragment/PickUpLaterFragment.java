package mawaqaa.parco.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mawaqaa.parco.R;


public class PickUpLaterFragment extends MainBaseFragment {

    private static String TAG = "PickUpLaterFragment";
    View v;

    NumberPicker datePicker, hourPicker, minutePicker, custom_picker_ampm;
    String dates[];
    Calendar rightNow = Calendar.getInstance();
    Button done_btn ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dates = getDatesFromCalender();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.pick_up_later_fragment, container, false);
        datePicker = v.findViewById(R.id.custom_picker_date_full);
        hourPicker = v.findViewById(R.id.custom_picker_hour);
        minutePicker = v.findViewById(R.id.custom_picker_minute);
        custom_picker_ampm = v.findViewById(R.id.custom_picker_ampm);
        done_btn = v.findViewById(R.id.done_btn);

        datePicker.setMinValue(0);
        datePicker.setMaxValue(dates.length - 1);
        datePicker.setFormatter(new NumberPicker.Formatter() {

            @Override
            public String format(int value) {
                return dates[value];
            }
        });
        String[] amPm = {"am", "pm"};


        datePicker.setDisplayedValues(dates);
        hourPicker.setMinValue(1);
        hourPicker.setMaxValue(12);
        hourPicker.setValue(rightNow.get(Calendar.HOUR_OF_DAY));
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(rightNow.get(Calendar.MINUTE));

        custom_picker_ampm.setMinValue(1);
        custom_picker_ampm.setMaxValue(amPm.length);
        custom_picker_ampm.setDisplayedValues(amPm);


        datePicker.setWrapSelectorWheel(false);
        hourPicker.setWrapSelectorWheel(false);
        minutePicker.setWrapSelectorWheel(false);
        custom_picker_ampm.setWrapSelectorWheel(false);


        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity.pushFragments(new CarPickUpFragment() , false , true);
            }
        });
        return v;
    }

    private String[] getDatesFromCalender() {
        Calendar c1 = Calendar.getInstance();

        List<String> dates = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM");
        dates.add(dateFormat.format(c1.getTime()));

        for (int i = 0; i < 7; i++) {
            c1.add(Calendar.DATE, 1);
            dates.add(dateFormat.format(c1.getTime()));
        }

        return dates.toArray(new String[dates.size() - 1]);
    }
}