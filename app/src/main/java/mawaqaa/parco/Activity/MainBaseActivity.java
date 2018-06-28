package mawaqaa.parco.Activity;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONObject;

import mawaqaa.parco.Fragment.MainBaseFragment;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.ParcoResponse;
import mawaqaa.parco.Volley.VolleyUtils;
/**
 * Created by Ayadi on 4/15/2018.
 */
public class MainBaseActivity extends AppCompatActivity {
    protected static MainBaseActivity BaseActivity;
    public MainBaseFragment BaseFragment;
    private static final String TAG = "MainBaseActivity";
    private Dialog spinWheelDialog;
    Handler spinWheelTimer = new Handler(); // Handler to post a runnable that

    public static final int SPINWHEEL_LIFE_TIME = 700;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "On create");
        BaseActivity = this;

        VolleyUtils.init(BaseActivity);
    }

    public void startSpinwheel(boolean setDefaultLifetime, boolean isCancelable) {
        // Log.d(TAG, "startSpinwheel"+getCurrentActivity().getClass() );
        // If already showing no need to create.
        if (spinWheelDialog != null && spinWheelDialog.isShowing())
            return;

        spinWheelDialog = new Dialog(BaseActivity, R.style.wait_spinner_style);
        ProgressBar progressBar = new ProgressBar(BaseActivity);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        spinWheelDialog.addContentView(progressBar, layoutParams);
        spinWheelDialog.setCancelable(isCancelable);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinWheelDialog.show();
            }
        });

        // start timer for SPINWHEEL_LIFE_TIME
        spinWheelTimer.removeCallbacks(dismissSpinner);
        if (setDefaultLifetime) // If requested for default dismiss time.
            spinWheelTimer.postAtTime(dismissSpinner, SystemClock.uptimeMillis() + SPINWHEEL_LIFE_TIME);

        spinWheelDialog.setCanceledOnTouchOutside(false);
    }

    public void stopSpinWheel() {
        // Log.d(TAG, "stopSpinWheel"+getCurrentActivity().getClass());
        if (spinWheelDialog != null)
            try {
                spinWheelDialog.dismiss();
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Parent is died while tryingto dismiss spin wheel dialog ");
                e.printStackTrace();
            }
        spinWheelDialog = null;
    }

    Runnable dismissSpinner = new Runnable() {

        @Override
        public void run() {
            stopSpinWheel();
        }

    };

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseActivity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static MainBaseActivity getMainBaseActivity() {
        return BaseActivity;
    }

    private boolean isNeedTransaction(String backStateName) {
        boolean needTransaction = true;
        if (BaseFragment != null) {
            String baseFrag = BaseFragment.getClass().getName();
            if (baseFrag.equals(backStateName)) {
                needTransaction = false;
            } else
                needTransaction = true;
        }
        return needTransaction;
    }

    public void pushFragments(Fragment fragment, boolean shouldAnimate,
                              boolean shouldAdd) {
        FragmentManager manager = getSupportFragmentManager();
        String backStateName = fragment.getClass().getName();

        if (isNeedTransaction(backStateName)) {
            boolean fragmentPopped = manager.popBackStackImmediate(
                    backStateName, 0);

            if (!fragmentPopped) { // fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                if (shouldAnimate)
                    ft.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                ft.replace(R.id.main_container, fragment, backStateName);
                if (shouldAdd)
                    ft.addToBackStack(backStateName);
                ft.commit();
                manager.executePendingTransactions();
            }
        }
    }

    public void pushFragments4LanSwitch(Fragment fragment, boolean shouldAnimate) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate)
            // ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            ft.replace(R.id.main_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
        manager.executePendingTransactions();
    }

    public void serviceResponseSuccess(ParcoResponse response) {
        if (response != null) {
            String reqUrl = response.mReqUrl;
            Log.d(TAG, "serviceResponseSuccess!!!" + reqUrl);
            switch (reqUrl) {
                case UrlClass.GetCarModels:
                    BaseActivity.onGetCarModelsSucessfully(response.jsonObject);
                    break;

                case UrlClass.GetCarColors:
                    BaseActivity.onGetCarColorsSucessfully(response.jsonObject);
                    break;

                case UrlClass.GetFuelType:
                    BaseActivity.onGetFuelTypesSucessfully(response.jsonObject);
                    break;

                case UrlClass.GetCarTypes:
                    BaseActivity.onGetCarTypesSucessfully(response.jsonObject);
                    break;

                case UrlClass.Register:
                    BaseActivity.onRegisterSucessfully(response.jsonObject);
                    break;
                case UrlClass.Login:
                    BaseActivity.onLoginSucessfully(response.jsonObject);
                    break;

                case UrlClass.ForgotPassword:
                    BaseActivity.onForgotPasswordSucessfully(response.jsonObject);
                    break;

                case UrlClass.GetCustomerCars:
                    BaseFragment.onGetCustomerCarsSucessfully(response.jsonObject);
                    break;

                case UrlClass.GetCustomerProfile:
                    BaseActivity.onGetCustomerProfileSucessfully(response.jsonObject);
                    break;

            }
        }
    }

    public void onGetCustomerProfileSucessfully(JSONObject jsonObject) {
    }


    public void serviceResponseError(ParcoResponse response) {
        if (response != null) {
            String reqUrl = response.mReqUrl;
            Log.d(TAG, "serviceResponseError" + reqUrl);
            switch (reqUrl) {
                case UrlClass.GetCarModels:
                    BaseActivity.onGetCarModelsFailed(response.jsonObject);
                    break;

                case UrlClass.GetCarColors:
                    BaseActivity.onGetCarColorsFailed(response.jsonObject);
                    break;

                case UrlClass.GetFuelType:
                    BaseActivity.onGetFuelTypesFailed(response.jsonObject);
                    break;
                case UrlClass.GetCarTypes:
                    BaseActivity.onGetCarTypesFailed(response.jsonObject);
                    break;

                case UrlClass.Register:
                    BaseActivity.onRegisterFailed(response.jsonObject);
                    break;

                case UrlClass.Login:
                    BaseActivity.onLoginFailed(response.jsonObject);
                    break;

                case UrlClass.ForgotPassword:
                    BaseActivity.onForgotPasswordFailed(response.jsonObject);
                    break;

                case UrlClass.GetCustomerCars:
                    BaseFragment.onGetCustomerCarsFailed(response.jsonObject);
                    break;

                case UrlClass.GetCustomerProfile:
                    BaseActivity.onGetCustomerProfileFailed(response.jsonObject);
                    break;


            }
        }
    }

    public void onGetCustomerProfileFailed(JSONObject jsonObject) {
    }

    public void onForgotPasswordFailed(JSONObject jsonObject) {
    }

    public void onLoginFailed(JSONObject jsonObject) {
    }

    public void onRegisterSucessfully(JSONObject jsonObject) {
    }

    public void onRegisterFailed(JSONObject jsonObject) {
    }

    public void onGetCarTypesSucessfully(JSONObject jsonObject) {
    }


    public void onGetCarTypesFailed(JSONObject jsonObject) {
    }

    public void onGetCarColorsFailed(JSONObject jsonObject) {
    }


    public void onGetCarModelsSucessfully(JSONObject jsonObject) {
    }

    public void onGetCarModelsFailed(JSONObject jsonObject) {
    }

    public void onGetCarColorsSucessfully(JSONObject jsonObject) {
    }


    public void onGetFuelTypesSucessfully(JSONObject jsonObject) {

    }

    public void onGetFuelTypesFailed(JSONObject jsonObject) {

    }

    public void onLoginSucessfully(JSONObject jsonObject) {
    }

    public void onForgotPasswordSucessfully(JSONObject jsonObject) {
    }
}
