package com.ua.viktor.stormy.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ua.viktor.stormy.R;
import com.ua.viktor.stormy.ui.weather.CurrentWeather;
import com.ua.viktor.stormy.ui.weather.Day;
import com.ua.viktor.stormy.ui.weather.Forecast;
import com.ua.viktor.stormy.ui.weather.Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private static final String TAG =MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST="DAILY_FORECAST";
    public static final String HORLY_FORECAST="HORLY_FORECAST";

    private CurrentWeather weather;
    private Forecast mForecast;
    ProgressBar progressBar;
    TextView mHumidyti;
    TextView temperature;
    ImageButton imageButton;
    ImageView imageView;
    TextView time;
    TextView city;
    TextView rainText;
    Button days;
    Button hourly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hourly=(Button)findViewById(R.id.hourlyButton);
        days=(Button)findViewById(R.id.dailyButton);
        time=(TextView)findViewById(R.id.textView);
        city=(TextView)findViewById(R.id.cityTextView);
        imageView=(ImageView)findViewById(R.id.iconImageView);
        temperature=(TextView)findViewById(R.id.temperature);
        progressBar=(ProgressBar)findViewById(R.id.progressBarRefresh);
        mHumidyti=(TextView)findViewById(R.id.humidityTextView);
        imageButton=(ImageButton)findViewById(R.id.imageButton);
        rainText=(TextView)findViewById(R.id.rainTextView);
        progressBar.setVisibility(View.INVISIBLE);

        final double latitude = 50.45;
        final double loung = 30.523333;
        days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailForecast.class);
                intent.putExtra(DAILY_FORECAST, mForecast.getDays());
                startActivity(intent);
            }
        });
        hourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HourlyForecastActivity.class);
                intent.putExtra(HORLY_FORECAST, mForecast.getHours());
                startActivity(intent);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude,loung);
            }
        });
        getForecast(latitude,loung);
    }

    private void getForecast(double latitude,double loung) {
        String apiKey = "b23514b9598ff927c2a83c76b2c9e908";

        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + loung;

        if(isNetworkAvaible()){
            toggleRefresh();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(forecastUrl)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toggleRefresh();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toggleRefresh();
                    }
                });
                try {
                    String json = response.body().string();
                    Log.v(TAG, json);
                    if (response.isSuccessful()) {
                        mForecast = parseForecastDetails(json);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateWhether();
                            }
                        });

                    } else {
                        alertUserAboutError();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "EROR", e);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        }else{
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleRefresh() {
        if(progressBar.getVisibility()==View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateWhether() {
        CurrentWeather mweather=mForecast.getWeather();
        String humadity=""+mweather.getmHumadity();
          temperature.setText(mweather.getMtemperature()+"");
        Drawable drawable=getResources().getDrawable(mweather.getIconId());
         imageView.setImageDrawable(drawable);
         mHumidyti.setText(humadity);
         city.setText(mweather.getTimeZone());
         time.setText(mweather.getFormattedTime());
         rainText.setText("" + mweather.getmPreceipChance());
    }

  private Forecast parseForecastDetails(String json) throws JSONException {
    Forecast forecast=new Forecast();
      forecast.setWeather(getCurrentDetail(json));
      forecast.setHours(getHourlyForecast(json));
      forecast.setDays(getDailyForecast(json));
         return forecast;
   }

    private Day[] getDailyForecast(String json) throws JSONException {
        JSONObject forecast=new JSONObject(json);
        String timeZone=forecast.getString("timezone");
        JSONObject daily=forecast.getJSONObject("daily");
        JSONArray data=daily.getJSONArray("data");

        Day [] days=new Day[data.length()];
        for (int i=0;i<data.length();i++){
            JSONObject jsonDay=data.getJSONObject(i);
            Day myDay=new Day();
            myDay.setSummary(jsonDay.getString("summary"));
            myDay.setTime(jsonDay.getLong("time"));
            myDay.setIcon(jsonDay.getString("icon"));
            myDay.setTempratureMax(jsonDay.getDouble("temperatureMax"));
            myDay.setTimeZone(timeZone);
            days[i]=myDay;
        }
        return days;

    }

    private Hour[] getHourlyForecast(String json) throws JSONException {
        JSONObject forecast=new JSONObject(json);
        String timeZone=forecast.getString("timezone");
        JSONObject hourly=forecast.getJSONObject("hourly");
        JSONArray data=hourly.getJSONArray("data");

        Hour [] hour=new Hour[data.length()];
    for (int i=0;i<data.length();i++){
     JSONObject jsonHour=data.getJSONObject(i);
     Hour myHour=new Hour();
     myHour.setTimeZone(timeZone);
        myHour.setSummary(jsonHour.getString("summary"));
     myHour.setMtemoerature(jsonHour.getDouble("temperature"));
     myHour.setMtime(jsonHour.getLong("time"));
     myHour.setIcon(jsonHour.getString("icon"));

    hour[i]=myHour;
    }
        return hour;
    }

    private CurrentWeather getCurrentDetail(String json) throws JSONException {
        JSONObject forecast=new JSONObject(json);
        String timeZone=forecast.getString("timezone");
        Log.v(TAG,""+timeZone);
        JSONObject currently=forecast.getJSONObject("currently");

        CurrentWeather currentWeather=new CurrentWeather();
        currentWeather.setmHumadity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setmPreceipChance(currently.getDouble("precipProbability"));
        currentWeather.setmSummary(currently.getString("summary"));
        currentWeather.setMtemperature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timeZone);
        Log.v(TAG,""+currentWeather.getFormattedTime());
        return currentWeather;
    }

    private boolean isNetworkAvaible() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isAvailble=false;
        if(networkInfo!=null&&networkInfo.isConnected()){
         isAvailble=true;
        }
        return isAvailble;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void alertUserAboutError() {
        AlertDialogFragment dialog= new AlertDialogFragment();
        dialog.show(getFragmentManager(),"ERROr");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
