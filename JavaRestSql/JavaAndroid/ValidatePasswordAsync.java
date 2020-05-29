package fantasticmassage.com;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;


public class ValidatePasswordAsync extends AsyncTask<String,Void, Boolean> {
    public static final String URL_VALIDATE_PASSWORD = "http://10.0.2.2:8080/com.fms.FMSRestfulWS/login/dologin";
    Context mContext;
    public ValidatePasswordAsync(Context context){
        mContext = context;
    }
    @Override
    public Boolean doInBackground(String... params){
        String username = params[0];
        String password = params[1];
        Boolean isPasswordValid = false;
        String finalUrl = this.URL_VALIDATE_PASSWORD;
        finalUrl += "?username=" + username;
        finalUrl += "&password=" + password;
        System.out.println("Inside fantasticmassage.com.ValidatePasswordAsync, doInBackGround, finalUrl: " + finalUrl);
        try {
            System.out.println("Inside fantasticmassage.com.ValidatePasswordAsync, doInBackground, inside try, finalUrl: " + finalUrl);
            URL url = new URL(finalUrl);
            System.out.println("Inside fantasticmassage.com.ValidatePasswordAsync, doInBackground, inside try, url: " + url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            String result = IOUtils.toString(httpURLConnection.getInputStream(), "UTF-8");
            JSONObject json;
            try {
                json = new JSONObject(result);
                if (json.getBoolean("status")) {
                    isPasswordValid = true;
                } else {
                    isPasswordValid = false;
                }
            } catch (JSONException e) {
                isPasswordValid = false;
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPasswordValid;
    }
    @Override
    public void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }
}
