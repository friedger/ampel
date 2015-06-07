package com.example.android.bluetoothlegatt.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import lejos.hardware.Audio;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.RegulatedMotor;

public class Control extends AsyncTask<String, Integer, Long> {

    public static RemoteRequestEV3 ev3;
    public static RegulatedMotor left, right;
    public static Audio audio;

    protected Long doInBackground(String... cmd) {

        if (cmd[0].startsWith("connect")) {
            try {
                ev3 = new RemoteRequestEV3(cmd[1]);
                left = ev3.createRegulatedMotor("A", 'L');
                right = ev3.createRegulatedMotor("B", 'L');
                audio = ev3.getAudio();
                audio.systemSound(3);
                if (cmd[0].equals("connectStop")){
                    left.stop();
                    right.stop();
                }
                if (cmd[0].equals("connectForward")) {
                    left.forward();
                    right.forward();
                }
                return 0l;
            } catch (IOException e) {
                return 1l;
            }
        } else if (cmd[0].equals("disconnect") && ev3 != null) {
            audio.systemSound(2);
            left.close();
            right.close();
            ev3.disConnect();
            ev3 = null;
            return 0l;
        }

        if (ev3 == null) return 2l;

        ev3.getAudio().systemSound(1);

        if (cmd[0].equals("stop")) {
            left.stop(true);
            right.stop(true);
        } else if (cmd[0].equals("forward")) {
            left.forward();
            right.forward();
        } else if (cmd[0].equals("backward")) {
            left.backward();
            right.backward();
        } else if (cmd[0].equals("rotate left")) {
            left.backward();
            right.forward();
        } else if (cmd[0].equals("rotate right")) {
            left.forward();
            right.backward();
        }

        return 0l;
    }

    protected void onPostExecute(Long result) {
        Log.d("ControlTask", "execute " + result);
    }
}
