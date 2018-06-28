package mawaqaa.parco.FirbaseServices;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.appConstant;


/**
 * Created by Ayadi on 3/20/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    SharedPrefsUtils sharedPref;

    @Override
    public void onTokenRefresh()
    {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sharedPref.setStringPreference(getApplicationContext(), appConstant.deviceToken_KEY,refreshedToken);

        Log.d("tokenDevice", refreshedToken);

        Intent registrationComplete = new Intent(ConfigFCM.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}