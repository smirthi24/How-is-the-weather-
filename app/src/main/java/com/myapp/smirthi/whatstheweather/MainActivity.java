package com.myapp.smirthi.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView ResultTextView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       editText=findViewById(R.id.editText);
       ResultTextView=findViewById(R.id.ResultTextView);

    }

    public void getWeather(View view){
       try {
           DownloadTask task = new DownloadTask();

           String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");
           task.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=439d4b804bc8187953eb36d2a8c26a02");

           InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
           mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
       }catch (Exception e){
           e.printStackTrace();
           Toast.makeText(getApplicationContext(),"Could not find Weather :(",Toast.LENGTH_SHORT).show();
       }
    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection;


            try{
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in =urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();

                while(data !=-1){
                    char current =(char)data;
                    result +=current;
                    data=reader.read();

                }
                return result;

            }catch (Exception e){
                e.printStackTrace();

                Toast.makeText(getApplicationContext(),"Could not find Weather :(",Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherinfo=jsonObject.getString("weather");
                Log.i("weather content",weatherinfo);
                JSONArray arr=new JSONArray(weatherinfo);

                String message="";

                for(int i=0;i<arr.length();i++){
                    JSONObject jsonpart=arr.getJSONObject(i);
                    String main=jsonpart.getString("main");
                    String description=jsonpart.getString("description");

                    //Log.i("main",jsonpart.getString("main"));
                    //Log.i("description",jsonpart.getString("description"));
                    if(!main.equals("")&&!description.equals("")){
                        message+=main+ ":" + description + "\r\n";
                    }
                }
                if(!message.equals("")){
                    ResultTextView.setText(message);
                }else{
                    Toast.makeText(getApplicationContext(),"Could not find Weather :(",Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            // Log.i("json",s);
        }
    }

}
