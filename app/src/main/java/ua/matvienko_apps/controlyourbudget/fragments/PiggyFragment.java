package ua.matvienko_apps.controlyourbudget.fragments;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;
import ua.matvienko_apps.controlyourbudget.activity.MainActivity;
import ua.matvienko_apps.controlyourbudget.activity.PiggyDialogActivity;
import ua.matvienko_apps.controlyourbudget.classes.Piggy;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by alex_ on 16-Sep-16.
 */

public class PiggyFragment extends Fragment {

    private final String LOG_TAG = PiggyFragment.class.getSimpleName();

    private TextView piggyAmountView;

    private SensorManager sensorManager;
    private Piggy piggy;
    private float piggyMoney;
    ImageView crashPiggyButton;

    private boolean piggyIsEmptied;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_piggy, null);

        piggyAmountView = (TextView) rootView.findViewById(R.id.piggyAmountView);
        ImageView piggyImageView = (ImageView) rootView.findViewById(R.id.piggyImage);
        crashPiggyButton = (ImageView) rootView.findViewById(R.id.crashPiggyButton);

        FloatingActionButton addMoneyToPiggy = (FloatingActionButton) rootView.findViewById(R.id.addMoneyToPiggy);
        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);

        piggy = new Piggy(piggyImageView, getContext());

        crashPiggyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (piggyMoney != 0) {
                    piggyIsEmptied = false;
                    Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    sensorManager.registerListener(eventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    Toast.makeText(getContext(), "Your piggy is already empty", Toast.LENGTH_SHORT).show();
                }
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
                    Log.e(LOG_TAG, "App not contains PIGGY_MONEY or CASH_REMAINING_MONEY");
            }
        });

        return rootView;
    }

    SensorEventListener eventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if ((event.values[0] > 0 && event.values[0] < 5 || event.values[0] < 0 && event.values[0] > -5)
                    && (event.values[1] < -6 && event.values[1] > -10)) {
                if (!piggyIsEmptied) {
                    piggy.startGettingMoney();
                    Utility.emptyPiggy();
                    changePiggyValue();
                    piggyIsEmptied = true;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(eventListener);

    }

    public void changePiggyValue() {
        piggyMoney = MainActivity.mSettings.getFloat(MainActivity.PIGGY_MONEY, 0);
        piggyAmountView.setText(String.valueOf(piggyMoney));
    }

    @Override
    public void onResume() {
        super.onResume();

        float requiredMoney = MainActivity.mSettings.getFloat(MainActivity.REQUIRED_MONEY, 0);
        float remainingMoney = MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0);

        piggyMoney = MainActivity.mSettings.getFloat(MainActivity.PIGGY_MONEY, 0);
        TranslateAnimation animation = new TranslateAnimation(-200, 0, 0, 0);
        animation.setDuration(600);
        crashPiggyButton.startAnimation(animation);
//        crashPiggyButton.startAnimation(new TranslateAnimation(-500, 0, 0,0));


        if (!piggyAmountView.getText().equals("")) {
            float piggyAmountValue = Float.parseFloat(piggyAmountView.getText().toString());

            if (piggyMoney > piggyAmountValue) {
                piggy.startAddingMoney();
            } else {
                piggy.startBlinking();
            }
        }

        changePiggyValue();
    }

}
