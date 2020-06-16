package com.example.amans.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {

    EditText et;
    String weather_info;
    String main_info;
    String description_info;
    TextView final_tv;
    //String temp_info;

    String temp_far;
    //int temp_far_int;
    //String temp_cel;
    //int temp_cel_int;
    String pressure;
    String Humidity;
   // String wind_info;
    String wind_Speed;
    String final_message="";
    ProgressBar pb;
    int progressStatus=0;
    Handler handler;

    public void clear_edittext(View view)
    {
        final_tv.setText("");
        final_message="";
    }

    /*public String convert(String s)
    {
        String ss;
        temp_far_int=Integer.parseInt(s);
        temp_cel_int=((temp_far_int-32)*5)/9;
        ss=Integer.toString(temp_cel_int);
        return ss;

    }*/

    /*public void progressBar_func()
    {

        handler=new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(progressStatus<100)
                {
                    progressStatus++;
                    if(progressStatus==99){
                        progressStatus=0;
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setProgress(progressStatus);
                            pb.setSecondaryProgress(progressStatus+15);

                        }
                    });
                }
            }
        }).start();

    }*/

    public void findWeather(View view) {


        //progressBar_func();


        if(et.getText().toString()=="")
        {
            Toast.makeText(getApplicationContext(),"SORRY!!!REQUEST CANNOT BE COMPLETED!!",Toast.LENGTH_LONG).show();
        }

        final_tv.setText("");
        final_message="";

        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(et.getWindowToken(),0);


        try {
            String encodedcityName = URLEncoder.encode(et.getText().toString(),"UTF-8");
            downloadTask task = new downloadTask();

            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedcityName+"&APPID=3c5db8ebca2d4bb922604f74c808e01a");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }





    }


    public class downloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            String result = " ";

            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"SORRY!!!REQUEST CANNOT BE COMPLETED!!",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"SORRY!!!REQUEST CANNOT BE COMPLETED!!",Toast.LENGTH_LONG).show();
            }

            return result;
        }


        @Override
        protected void onPostExecute(String Result) {
            super.onPostExecute(Result);

            String message="";
            String message1="";
            String message2="";

            try {
                JSONObject j_obj = new JSONObject(Result);
                JSONObject j_obj1 = new JSONObject(Result);

                weather_info = j_obj.getString("weather");


                JSONArray arr = new JSONArray(weather_info);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    main_info = jsonPart.getString("main");
                    //Log.i("MAIN:", main_info);
                    description_info = jsonPart.getString("description");
                    //Log.i("DESCRIPTION:", description_info);




                    if (main_info != " " && description_info != " ") {
                        message += main_info + ":" + description_info + "\r\n";
                    }

                }


                JSONObject main=j_obj1.getJSONObject("main");




                    temp_far=main.getString("temp");
                    //temp_cel=convert(temp_far);
                    pressure=main.getString("pressure");
                    Humidity=main.getString("humidity");

                    if (temp_far!= " " && pressure!= " " && Humidity!=" ") {
                        message1 += "Temperature:" + temp_far + " Fahrenhiet"+"\r\n"+"Pressure:"+pressure+" Pa"+"\r\n"+"Humidity:"+Humidity+"%"+"\r\n";
                    }






               JSONObject wind=j_obj1.getJSONObject("wind");
                    wind_Speed=wind.getString("speed");


                    if (wind_Speed != " " ) {
                        message2 += "Wind" + ":" +wind_Speed+" mph" + "\r\n";
                    }











            }
            catch (JSONException e)
            {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"SORRY!!!REQUEST CANNOT BE COMPLETED!!",Toast.LENGTH_LONG).show();
            }

            Log.i("JSON CODE:", Result);

            final_message+=message+message1+message2;

            Log.i("FINAL MESSAGE:",final_message);

            if(final_message!=" ")
            {
                final_tv.setText(final_message);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"SORRY!!!REQUEST CANNOT BE COMPLETED!!",Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.editText);
        final_tv=findViewById(R.id.textView3);
        //pb=findViewById(R.id.progressBar);


    }
}
