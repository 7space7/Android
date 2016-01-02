package com.ua.viktor.game2048.game;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;



public class Card extends FrameLayout {
    private LayoutParams mLayoutParams;
    private int num;
    private TextView label ;
    public Card(Context context) {
        super(context);
        label = new TextView(getContext());
        mLayoutParams = new LayoutParams(-1, -1);


        View background = new View(getContext());

        mLayoutParams.setMargins(3, 3, 0, 0);
        background.setBackgroundColor(Color.parseColor("#E0DDD7"));
        addView(background, mLayoutParams);

        label = new TextView(getContext());
        label.setTextSize(28);
        label.setTextColor(Color.parseColor("#FFFFFF"));
        label.setGravity(Gravity.CENTER);

        mLayoutParams.setMargins(3, 3, 0, 0);
        addView(label, mLayoutParams);

        setNum(0);
    }


    public void setNum(int num) {
        this.num = num;
        if(num<=0){
            label.setText("");
        }else{
            label.setText(num+"");
        }
        switch (num) {
            case 0:
                label.setBackgroundColor(Color.parseColor("#696969"));
                break;
            case 2:
                label.setBackgroundColor(Color.parseColor("#34B4E3"));
                break;
            case 4:
                label.setBackgroundColor(Color.parseColor("#0099CC"));
                break;
            case 8:
                label.setBackgroundColor(Color.parseColor("#AA66CC"));
                break;
            case 16:
                label.setBackgroundColor(Color.parseColor("#9933CC"));
                break;
            case 32:
                label.setBackgroundColor(Color.parseColor("#99CC00"));
                break;
            case 64:
                label.setBackgroundColor(Color.parseColor("#669900"));
                break;
            case 128:
                label.setBackgroundColor(Color.parseColor("#FFBB33"));
                break;
            case 256:
                label.setBackgroundColor(Color.parseColor("#FF8800"));
                break;
            case 512:
                label.setBackgroundColor(Color.parseColor("#FF4545"));
                break;
            case 1024:
                label.setBackgroundColor(Color.parseColor("#EA1F00"));
                break;
            case 2048:
                label.setBackgroundColor(Color.parseColor("#000000"));
                break;
            default:
                label.setBackgroundColor(0xff3c3a32);
                break;
        }
    }


}

