package mawaqaa.parco.Volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Ayadi on 4/23/2018.
 */

public class VolleyUtils {
    private static RequestQueue mRequestQueue;
    public static final boolean volleyEnabled = true;
    private static ServerConnectionChannel mServerConnectionChannel;

    public static void init(Context context) {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(context);
        if (mServerConnectionChannel == null)
            mServerConnectionChannel = new ServerConnectionChannel();
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static ServerConnectionChannel getServerConnectionChannel() {
        if (mServerConnectionChannel != null) {
            return mServerConnectionChannel;
        } else {
            throw new IllegalStateException("mServerConnectionChannel not initialized");
        }
    }
}
