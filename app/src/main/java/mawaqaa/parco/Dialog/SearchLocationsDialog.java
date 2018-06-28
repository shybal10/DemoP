package mawaqaa.parco.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import mawaqaa.parco.R;

public class SearchLocationsDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    Button cancel,typeToSearch;


    public SearchLocationsDialog(Activity myActivity) {
        super(myActivity);
        this.activity = myActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.search_locations_layout);
        typeToSearch = (Button) findViewById(R.id.open_location_auto_complete_btn);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        typeToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(activity);
                    activity.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Toast.makeText(getContext(),"GooglePlayServicesRepairableException",Toast.LENGTH_LONG).show();
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(getContext(),"GooglePlayServicesNotAvailableException",Toast.LENGTH_LONG).show();
                    // TODO: Handle the error.
                }

                dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
