package ua.matvienko_apps.controlyourbudget.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;
import ua.matvienko_apps.controlyourbudget.activity.MainActivity;
import ua.matvienko_apps.controlyourbudget.activity.PiggyDialogActivity;
import ua.matvienko_apps.controlyourbudget.classes.CustomAnimationDrawable;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by alex_ on 16-Sep-16.
 */

public class PiggyFragment extends Fragment {

    private TextView piggyAmountView;
    private TextView requiredMoneyView;
    private Button crashPiggyButton;
    private ImageView piggyImageView;


    private AnimationDrawable piggyBlinkingAnimation;
    private CustomAnimationDrawable piggyJoyAnimation;
    private CustomAnimationDrawable piggyCryAnimation;


    View dialogView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_piggy, null);
        dialogView = inflater.inflate(R.layout.dialog_piggy_view, null);

        piggyAmountView = (TextView) rootView.findViewById(R.id.piggyAmountView);
        piggyImageView = (ImageView) rootView.findViewById(R.id.piggyImage);
        crashPiggyButton = (Button) rootView.findViewById(R.id.crashPiggyButton);
        requiredMoneyView = (TextView) rootView.findViewById(R.id.requiredMoneyView);

        FloatingActionButton addMoneyToPiggy = (FloatingActionButton) rootView.findViewById(R.id.addMoneyToPiggy);

        piggyJoyAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.piggy_joy_animation)) {
            @Override
            public void onAnimationFinish() {
                piggyJoyAnimation.stop();
                startBlinking();
            }
        };

        piggyCryAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.piggy_cry_animation)) {
            @Override
            public void onAnimationFinish() {
                piggyCryAnimation.stop();
                startBlinking();
            }
        };


        SensorManager sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        Sensor accelerometr = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        sensorManager.registerListener(eventListener, accelerometr, SensorManager.SENSOR_DELAY_NORMAL);

        piggyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!piggyJoyAnimation.isRunning())
                    if (!piggyCryAnimation.isRunning())
                        startCryAnimation();
            }
        });

        piggyImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!piggyCryAnimation.isRunning())
                    if (!piggyJoyAnimation.isRunning())
                        startJoyAnimation();
                return true;
            }
        });

        crashPiggyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.emptyPiggy();
                onResume();
            }
        });

        addMoneyToPiggy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MainActivity.mSettings.contains(MainActivity.PIGGY_MONEY)
                        && MainActivity.mSettings.contains(MainActivity.CASH_REMAINING_MONEY)) {

                    Intent intent = new Intent(getContext(), PiggyDialogActivity.class);
                    startActivity(intent);

                } else
                    Log.e("PiggyFragment", "App not contains PIGGY_MONEY or CASH_REMAINING_MONEY");
            }
        });

        return rootView;
    }


    public void startBlinking() {
        piggyImageView.setBackgroundResource(R.drawable.piggy_blinking_animation);
        piggyBlinkingAnimation = (AnimationDrawable) piggyImageView.getBackground();

        piggyBlinkingAnimation.start();

    }

    public void startJoyAnimation() {
        piggyImageView.setBackground(piggyJoyAnimation);
        piggyBlinkingAnimation.stop();
        piggyJoyAnimation.start();

        new Thread() {
            public void run() {
                MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.piggy_joy_sound);
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(0.4f, 0.4f);
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                }

            }
        }.start();
    }

    public void startCryAnimation() {
        piggyImageView.setBackground(piggyCryAnimation);
        piggyBlinkingAnimation.stop();
        piggyCryAnimation.start();
    }


    @Override
    public void onResume() {
        super.onResume();

        float requiredMoney = MainActivity.mSettings.getFloat(MainActivity.REQUIRED_MONEY, 0);
        float remainingMoney = MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0);
        float piggyMoney = MainActivity.mSettings.getFloat(MainActivity.PIGGY_MONEY, 0);

        float piggyAmountValue = Float.parseFloat(piggyAmountView.getText().toString());

        if (piggyMoney > piggyAmountValue && piggyBlinkingAnimation != null) {
            startJoyAnimation();
            Log.e("TAG", piggyMoney + ":      :" + Float.parseFloat(piggyAmountView.getText().toString()));
        } else {
            startBlinking();
        }

        piggyAmountView.setText(Float.toString(piggyMoney));

    }

    SensorEventListener eventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
                return;
            float mSensorX, mSensorY;
            switch (display.getRotation()) {
                case Surface.ROTATION_0:
                    mSensorX = event.values[0];
                    mSensorY = event.values[1];
                    break;
                case Surface.ROTATION_90:
                    mSensorX = -event.values[1];
                    mSensorY = event.values[0];
                    break;
                case Surface.ROTATION_180:
                    mSensorX = -event.values[0];
                    mSensorY = -event.values[1];
                    Toast.makeText(getContext(), "lkdjfklsjdf " + mSensorX, Toast.LENGTH_SHORT).show();
                    break;
                case Surface.ROTATION_270:
                    mSensorX = event.values[1];
                    mSensorY = -event.values[0];
            }
            requiredMoneyView.setText(String.valueOf(event.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
