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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView requiredMoneyView;
    private Button crashPiggyButton;
    private ImageView piggyImageView;

    private SensorManager sensorManager;
    private Piggy piggy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_piggy, null);

        piggyAmountView = (TextView) rootView.findViewById(R.id.piggyAmountView);
        piggyImageView = (ImageView) rootView.findViewById(R.id.piggyImage);
        crashPiggyButton = (Button) rootView.findViewById(R.id.crashPiggyButton);
        requiredMoneyView = (TextView) rootView.findViewById(R.id.requiredMoneyView);

        FloatingActionButton addMoneyToPiggy = (FloatingActionButton) rootView.findViewById(R.id.addMoneyToPiggy);
        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        piggy = new Piggy(piggyImageView, getContext());


        crashPiggyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.emptyPiggy();
                onResume();
                Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(eventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
                requiredMoneyView.setText("Flipped Portrait");
            } else
                requiredMoneyView.setText(String.valueOf(event.values[0]) + ":::" + String.valueOf(event.values[1]));

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

    @Override
    public void onResume() {
        super.onResume();

        float requiredMoney = MainActivity.mSettings.getFloat(MainActivity.REQUIRED_MONEY, 0);
        float remainingMoney = MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0);
        float piggyMoney = MainActivity.mSettings.getFloat(MainActivity.PIGGY_MONEY, 0);

        if (!piggyAmountView.getText().equals("")) {
            float piggyAmountValue = Float.parseFloat(piggyAmountView.getText().toString());

            if (piggyMoney > piggyAmountValue) {
                piggy.startAddingMoney();
            } else {
                piggy.startBlinking();
            }
        }


        piggyAmountView.setText(String.valueOf(piggyMoney));

    }

}
