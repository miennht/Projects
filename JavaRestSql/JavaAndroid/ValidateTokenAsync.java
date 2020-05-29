package fantasticmassage.com;

import android.content.Context;
import android.os.AsyncTask;
import android.text.PrecomputedText;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dto.User;

public class ValidateTokenAsync extends AsyncTask<String,Void, Boolean> {
    public static final String URL_VALIDATE_TOKEN = "http://10.0.2.2:8080/com.fms.FMSRestfulWS/login/istokenvalid";
    Context mContext;
    public ValidateTokenAsyncResponseInterface delegate = null;
    public ValidateTokenAsync(Context context){
        mContext = context;
    }
    @Override
    public Boolean doInBackground(String... params){
        String userId = params[0];
        String token = params[1];
        Boolean isTokenValid = false;
        String finalUrl = this.URL_VALIDATE_TOKEN;
        finalUrl += "?userid=" + userId;
        finalUrl += "&token=" + token;
        System.out.println("Inside ValidateTokenAsync, doInBackGround, finalUrl: " + finalUrl);
        try {
            System.out.println("Inside ValidateTokenAsync, doInBackground, inside try, finalUrl: " + finalUrl);
            URL url = new URL(finalUrl);
            System.out.println("Inside ValidateTokenAsync, doInBackground, inside try, url: " + url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            String result = IOUtils.toString(httpURLConnection.getInputStream(), "UTF-8");
            JSONObject json;
            try {
                json = new JSONObject(result);
                if (json.getBoolean("status")) {
                    isTokenValid = true;
                } else {
                    isTokenValid = false;
                }
            } catch (JSONException e) {
                isTokenValid = false;
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTokenValid;
    }
    @Override
    public void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        delegate.processValidateTokenFinish(result);
    }
}
