package com.example.dandi.latihan1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;

import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.charts.BarChart;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GarbageActivity extends AppCompatActivity {

    public static final String URL_GET = "http://things.ubidots.com/api/v1.6/devices/esp8266new/58f08f6476254207a65869e1/values?page_size=1&token=USLXcRYPRkL6ECHQLublgREtNcg0kk";
    public static final String URL_GET2 = "http://things.ubidots.com/api/v1.6/devices/esp8266new/58f5aad476254254aae25ea7/values?page_size=1&token=USLXcRYPRkL6ECHQLublgREtNcg0kk";
    public static final String URL_GET3 = "http://things.ubidots.com/api/v1.6/devices/esp8266new/humidity/values?page_size=1&token=USLXcRYPRkL6ECHQLublgREtNcg0kk";

    TextView sensor1;
    TextView sensor2;
    TextView sensor3;
    TextView connect;
    protected BarChart mChart;
    private Notification myNotification;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect  = (Button)findViewById(R.id.connect);
        sensor1 = (TextView)findViewById(R.id.sensor1);
        sensor2 = (TextView)findViewById(R.id.sensor2);
        sensor3 = (TextView)findViewById(R.id.sensor3);
        mChart = (BarChart) findViewById(R.id.chart1);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Handler mHandler = new Handler();

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true){
                                try{
                                    Thread.sleep(10);
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            getData gd = new getData();
                                            gd.execute();

                                        }
                                    });
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    public  class getData extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPostExecute(String data){
            super.onPostExecute(data);

            String[] arrayData = new String[3];
            for(int index = 1; index <= arrayData.length; index++){
                String[] ubidotData = data.split("@");

                String dummy1 = ubidotData[0];
                String dummy2 = ubidotData[1];
                String dummy3 = ubidotData[2];

                String fixData1 = receivedData(dummy1);
                String fixData2 = receivedData(dummy2);
                String fixData3 = receivedData(dummy3);

                Float floatData1 = Float.parseFloat(fixData1);
                Float floatData2 = Float.parseFloat(fixData2);
                Float floatData3 = Float.parseFloat(fixData3);

                sensor1.setText(fixData1);
                sensor2.setText(fixData2);
                sensor3.setText(fixData3);

                buildGraph(35f, 0f, floatData1, floatData2, floatData3);
                if(floatData1 < 10.0){
                    buildNotif(1,"Kampus","http://www.google.com/maps/place/-7.130337,112.723691/@49.46800006494457,17.11514008755796,17z/data=!3m1!1e3");
                }
                if(floatData2 < 10.0){
                    buildNotif(2,"Salak","http://www.google.com/maps/place/-7.157217,112.722543/@49.46800006494457,17.11514008755796,17z/data=!3m1!1e3");
                }
                if(floatData3 < 10.0){
                    buildNotif(3,"Kamal","http://www.google.com/maps/place/-7.171832,112.722490/@49.46800006494457,17.11514008755796,17z/data=!3m1!1e3");
                }
            }
    }

        @Override
        protected String doInBackground(Void... v){
            String data1 = sendGetRequest(URL_GET);
            String data2 = sendGetRequest(URL_GET2);
            String data3 = sendGetRequest(URL_GET3);
            String res = data1 + "@" + data2 + "@" + data3;
            return res;
        }
    }

    public String sendGetRequest (String requestURL) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s + "\n");
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }
    public String receivedData(String dummy){
        String[] remove_dummy1 = dummy.split("value");
        String[] remove_label1 = remove_dummy1[3].split("timestamp");
        String fixData = remove_label1[0].replaceAll("\"","").replaceAll(":","").replaceAll(",","");
        return fixData;
    }

    public void buildGraph(float max, float min, float val1, float val2, float val3){

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, max-val1));
        entries.add(new BarEntry(1f, max-val2));
        entries.add(new BarEntry(2f, max-val3));


        BarDataSet dataSet = new BarDataSet(entries, "BarDataSet");
        BarData barData = new BarData(dataSet);

        barData.setBarWidth(0.9f); // set custom bar width
        YAxis yAxisL = mChart.getAxisLeft();
        yAxisL.setAxisMaximum(max);
        yAxisL.setAxisMinimum(min);
        yAxisL.setGranularity(5f);
        YAxis yAxisR = mChart.getAxisRight();
        yAxisR.setAxisMaximum(max);
        yAxisR.setAxisMinimum(min);
        yAxisR.setGranularity(5f);
        mChart.setFitBars(true); // make the x-axis fit exactly all bars

        mChart.setData(barData);
        mChart.invalidate(); // refresh
    }

    public void buildNotif(int ID, String tempat, String url){
        myNotification = new Notification(R.drawable.boxing, "Notification!", System.currentTimeMillis());
        String MyNotificationTitle = "Tempat Sampah " + tempat + "Penuh !!";
        String myText = Integer.toString(ID);
        Uri URL  = Uri.parse(url);

        Context ctx = GarbageActivity.this;

        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, URL); //manggil GMAP
        PendingIntent contentIntent = PendingIntent.getActivity(ctx,
                ID, notificationIntent,
                Intent.FILL_IN_ACTION);

        NotificationManager nm = (NotificationManager) GarbageActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        Notification.Builder builder = new Notification.Builder(GarbageActivity.this);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.boxing)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(MyNotificationTitle)
                .setContentText(myText);
        Notification n = builder.build();

        nm.notify(ID, n);
    }





}
