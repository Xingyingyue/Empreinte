package example.com.empreinte.Util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by huanghaojian on 17/6/23.
 */

public class OkHttpUtil {
    public static void sendOkHttpRequest(String address, Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendOkhttpRequestByPost(String address, RequestBody requestBody,Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
}
