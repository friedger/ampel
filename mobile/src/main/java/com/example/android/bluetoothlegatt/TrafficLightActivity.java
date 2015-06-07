package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by Maxime on 6/06/15.
 */
public class TrafficLightActivity extends Activity{

    private ImageView imageGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffice);
        imageGo = (ImageView)findViewById(R.id.go_img);
        Long go = getIntent().getLongExtra("go_signal",1);
        if (go == 0){
            imageGo.setImageResource(R.drawable.ic_thumb_up_green_800_24dp);
        } else {
            imageGo.setImageResource(R.drawable.ic_thumb_down_red_800_24dp);
        }
    }


}
