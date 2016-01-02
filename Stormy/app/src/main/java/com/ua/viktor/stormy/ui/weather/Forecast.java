package com.ua.viktor.stormy.ui.weather;

import com.ua.viktor.stormy.R;

/**
 * Created by viktor on 17.07.15.
 */
public class Forecast {
    CurrentWeather mWeather;
    Day[]mDays;
    Hour[]mHours;

    public CurrentWeather getWeather() {
        return mWeather;
    }

    public void setWeather(CurrentWeather weather) {
        mWeather = weather;
    }

    public Day[] getDays() {
        return mDays;
    }

    public void setDays(Day[] days) {
        mDays = days;
    }

    public Hour[] getHours() {
        return mHours;
    }

    public void setHours(Hour[] hours) {
        mHours = hours;
    }
    public static int getIconId(String icon)
    {
        int iconId = R.drawable.clear_day;
        if (icon.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (icon.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (icon.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (icon.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (icon.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (icon.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (icon.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (icon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (icon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (icon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }
        return iconId;
    }

}
