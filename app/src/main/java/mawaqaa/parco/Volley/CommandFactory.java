package mawaqaa.parco.Volley;

import com.android.volley.Request;

import org.json.JSONObject;

/**
 * Created by Ayadi on 4/23/2018.
 */

public class CommandFactory {
    public void sendgetCommand(String url) {

        ServerConnectionChannel serverConnectionChannel = VolleyUtils
                .getServerConnectionChannel();
        serverConnectionChannel.doSendJsonRequest(createGetRequest(url));

    }

    public void sendPostCommand(String url, JSONObject jsonObject) {
        ServerConnectionChannel serverConnectionChannel = VolleyUtils
                .getServerConnectionChannel();
        serverConnectionChannel.doSendJsonRequest(createPostRequest(url, jsonObject));
    }

    /*method for get method*/
    private ParcoRequest createGetRequest(String url) {
        ParcoRequest babtainRequest = new ParcoRequest();
        babtainRequest.method = Request.Method.GET;
        babtainRequest.mReqUrl = url;
        return babtainRequest;

    }

    /*method for post method*/
    private ParcoRequest createPostRequest(String url, JSONObject jsonObject) {
        ParcoRequest babtainRequest = new ParcoRequest();
        babtainRequest.method = Request.Method.POST;
        babtainRequest.mReqUrl = url;
        babtainRequest.jsonObject = jsonObject;
        return babtainRequest;

    }
}
