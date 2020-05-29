package fantasticmassage.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import dto.User;


public class GetClientTokenAsync extends AsyncTask<String, Void, String>{
    private static final String URL_BRAINTREE_CLIENTTOKEN = "http://10.0.2.2:8080/com.fms.FMSRestfulWS/braintree/clienttoken";
    android.content.Context mContext;
    ProgressDialog progressDialog;
    public GetClientTokenAsync(Context _mContext) {
        mContext = _mContext;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mContext, android.R.style.Theme_DeviceDefault_Dialog);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String[] customerIds) {
        String customerId = customerIds[0];
        String token = null;
        String finalUrl = this.URL_BRAINTREE_CLIENTTOKEN;
        System.out.println ("GetClientTokenAsync, doInBackground, customerId: " + customerId);
        finalUrl += "?customerId=" + customerId;
        System.out.println ("GetClientTokenAsync, doInBackground, finalUrl: " + finalUrl);
        try {
            URL url = new URL(finalUrl);
            System.out.println("GetClientTokenAsync, doInBackground, inside try, url: " + url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            String result = IOUtils.toString(httpURLConnection.getInputStream(), "UTF-8");
            System.out.println("GetClientTokenAsync, doInBackground, inside try, result: " + result);
            try {
                JSONObject json = new JSONObject(result);
                System.out.println("Inside GetClientTokenAsync, doInBackground, json: " + json);
                //String tokenTemp = json.toString();
                //System.out.println("Inside GetClientTokenAsync, doInBackground, userTemp: " + tokenTemp);
                if (json.getBoolean("status")) {
                    //When the status = true, server returns the token which is stored in "error_msg"
                    // since "error_msg" is a string, this can be improved in Utility class in server side ^^
                    token = json.getString("error_msg");
                    System.out.println("Inside GetClientTokenAsync, doInBackground, token: " + token);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    @Override
    public void onPostExecute(String token){
        super.onPostExecute(token);
        progressDialog.dismiss();
    }
}
