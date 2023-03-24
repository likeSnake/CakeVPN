package com.lazycoder.cakevpn.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownTask extends AsyncTask<String,String,String> {

    private OnDownloadCompleteListener mListener;

    public DownTask(OnDownloadCompleteListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        return downloadCity(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        if (mListener!=null){
            mListener.onDownloadComplete(s);
        }
    }

    public String downloadCity(String imageUrl){
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                return inputStreamToString(inputStream);
            } else {
                throw new IOException("Error response code: " + connection.getResponseCode());
            }
        }catch (IOException e){
            Log.e("IOException", String.valueOf(e));
            return null;
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
    public static String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append('\n');
        }
        return stringBuilder.toString();
    }
    public interface OnDownloadCompleteListener {
        void onDownloadComplete(String s);
    }
}
