package com.couriertracking.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.couriertracking.app.utility.DbHelper;
import com.couriertracking.app.utility.MyAppUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anilkukreti on 10/12/15.
 */
public class CourierService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        new GetConfigDataTask(this).execute();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public class GetConfigDataTask extends AsyncTask<Void, Integer, Boolean> {


        StringBuffer responseString = new StringBuffer("");
        int mConnectionCode;
        private Context mContext;

        public GetConfigDataTask(Context ctx) {
            this.mContext = ctx;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                URL url = new URL("http://www.couriertrack.in/app-config/courier_list_sample.txt");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.setDoInput(true);


                mConnectionCode = connection.getResponseCode();

                if (mConnectionCode == HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));

                    String line = "";

                    while ((line = rd.readLine()) != null) {
                        responseString.append(line+"\n");
                    }

                    return true;
                }
            } catch (IOException e) {
                Log.v("Message", e.getMessage());
                e.printStackTrace();
                return false;

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                if (responseString != null && !responseString.toString().equalsIgnoreCase("")) {

                    DbHelper db = new DbHelper(mContext);
                    db.open();

                    db.emptyTables();

                    String[] arrCourierLine = responseString.toString().split("\n");



                    for (int i = 0; i < arrCourierLine.length; i++) {

                        String[] arrCourierData = arrCourierLine[i].split(";;");
                        String strCourierName = arrCourierData[0];
                        String strCourierUrl = arrCourierData[1];
                        db.insert_courier_data("", strCourierName, strCourierUrl);


                    }
                    db.close();

                    if (MyAppUtility.getInstance().getmMainActivityCallback() != null) {
                        MyAppUtility.getInstance().getmMainActivityCallback().mainActivityHandler.sendEmptyMessage(0);
                    }

                } else if (mConnectionCode >= HttpURLConnection.HTTP_BAD_REQUEST && mConnectionCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {


                } else if (mConnectionCode >= HttpURLConnection.HTTP_MULT_CHOICE && mConnectionCode < HttpURLConnection.HTTP_BAD_REQUEST) {


                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


        }
    }

}
