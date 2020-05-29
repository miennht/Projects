package fantasticmassage.com;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by mien.nguyen on 6/01/2020.
 */
public class TokenGenerateHashStoreAsync extends AsyncTask<String, Void, String> {
    private static final String URL_SAVE_HASH = "http://10.0.2.2:8080/com.fms.FMSRestfulWS/login/savetoken";
    private static final String HASH_EXCEPTION_MESSAGE = "HASH_EXCEPTION! Please contact administrator!";
    public TokenGenerateHashStoreAsyncResponseInterface delegate = null;
    android.content.Context mContext;
    public TokenGenerateHashStoreAsync(Context _mContext) throws ParseException {
        mContext = _mContext;
    }
    @Override
    protected String doInBackground(String... params) {
        String resultString = null;
        String username = params[0];
        String finalUrl = this.URL_SAVE_HASH;
        finalUrl += "?userid=" + username;
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            String result = IOUtils.toString(httpURLConnection.getInputStream(), "UTF-8");
            JSONObject json;
            try {
                json = new JSONObject(result);
                if (json.getBoolean("status")) {
                    resultString = json.getString("error_msg");//error_message in this case contains hash
                } else {
                    resultString = HASH_EXCEPTION_MESSAGE + json.getString("error_msg");
                }
            } catch (JSONException e) {
                resultString = HASH_EXCEPTION_MESSAGE;
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
    @Override
    protected void onPostExecute(String result) {
        System.out.println("Inside TokenGenerateHashStoreAsync, onPostExecute: " + result);
        delegate.processSaveHashFinish(result);
    }
    @Override
    protected void onPreExecute() {
    }
    @Override
    protected void onProgressUpdate(Void... values) {
    }
}

