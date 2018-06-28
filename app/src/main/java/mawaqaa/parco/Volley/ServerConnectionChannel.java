package mawaqaa.parco.Volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mawaqaa.parco.Activity.MainBaseActivity;

/**
 * Created by Ayadi on 4/23/2018.
 */

public class ServerConnectionChannel {
    private static final String TAG = "ServerConnectionChannel";
    private int BABTAIN_BACKOFF_MULT = 2;
    private int BABTAIN_MAX_RETRIES = 2;

    public ServerConnectionChannel() {
    }

    public void doSendJsonRequest(ParcoRequest parcoRequest) {
        RequestQueue queue = VolleyUtils.getRequestQueue();
        JSONObject jsonObject = parcoRequest.jsonObject;

        ParcoJsonRequest myReq = new ParcoJsonRequest(parcoRequest.method, parcoRequest.mReqUrl, jsonObject,
                createReqSuccessListener(parcoRequest), createReqErrorListener(parcoRequest)) {
            protected Map<String, String> getParams()
                    throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                BABTAIN_MAX_RETRIES,
                BABTAIN_BACKOFF_MULT));
//        myReq.setHeader("Cache-Control", "no-cache");
//        myReq.setHeader("Content-Type", "application/x-www-form-urlencoded");
        queue.add(myReq);
    }

    private Response.ErrorListener createReqErrorListener(final ParcoRequest parcoRequest) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ReqErrorListener" + error.toString());
                ParcoResponse batainResponse = new ParcoResponse();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e(TAG, String.valueOf(networkResponse.statusCode));
                }
                batainResponse.mReqUrl = parcoRequest.mReqUrl;
                MainBaseActivity.getMainBaseActivity().serviceResponseError(batainResponse);
            }
        };
    }

    private Response.Listener<JSONObject> createReqSuccessListener(final ParcoRequest icleanPlusRequest) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "ReqSuccessListener :" + icleanPlusRequest.mReqUrl);
                ParcoResponse batainResponse = new ParcoResponse();

                batainResponse.mReqUrl = icleanPlusRequest.mReqUrl;
                batainResponse.jsonObject = response;
                MainBaseActivity.getMainBaseActivity().serviceResponseSuccess(batainResponse);
            }
        };
    }
}