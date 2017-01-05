package ua.matvienko_apps.controlyourbudget.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
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
import ua.matvienko_apps.controlyourbudget.classes.CustomAnimationDrawable;

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


    View dialogView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_piggy, null);
        dialogView = inflater.inflate(R.layout.dialog_piggy_view, null);

        piggyImageView = (ImageView) rootView.findViewById(R.id.piggyImage);
        crashPiggyButton = (Button) rootView.findViewById(R.id.crashPiggyButton);
        piggyAmountView = (TextView) rootView.findViewById(R.id.piggyAmountView);
        requiredMoneyView = (TextView) rootView.findViewById(R.id.requiredMoneyView);
        FloatingActionButton addMoneyToPiggy = (FloatingActionButton) rootView.findViewById(R.id.addMoneyToPiggy);

        piggyJoyAnimation = new CustomAnimationDrawable((AnimationDrawable) getResources().getDrawable(R.drawable.piggy_joy_animation)) {
            @Override
            public void onAnimationFinish() {
                startBlinking();
            }
        };


        piggyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                piggyImageView.setBackgroundDrawable(piggyJoyAnimation);
                piggyJoyAnimation.start();

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

                } else Log.e("PiggyFragment", "App not contains PIGGY_MONEY or CASH_REMAINING_MONEY");
            }
        });

        return rootView;
    }


    public void startBlinking() {
        piggyImageView.setBackgroundResource(R.drawable.piggy_blinking_animation);
        piggyBlinkingAnimation = (AnimationDrawable) piggyImageView.getBackground();

        piggyBlinkingAnimation.start();

    }

//    public void startJoy() {

//
//        piggyBlingkinAnimation.start();
//    }


    @Override
    public void onResume() {
        super.onResume();

        startBlinking();

        float requiredMoney = MainActivity.mSettings.getFloat(MainActivity.REQUIRED_MONEY, 0);
        float remainingMoney = MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0);

        if (remainingMoney == 0) {
            requiredMoneyView.setTextColor(Color.WHITE);
        } else if (requiredMoney == remainingMoney) {
            requiredMoneyView.setTextColor(Color.DKGRAY);
        } else if (requiredMoney >= remainingMoney) {
            requiredMoneyView.setTextColor(Color.GREEN);
        } else {
            requiredMoneyView.setTextColor(Color.RED);
        }

        piggyAmountView.setText(Float.toString(MainActivity.mSettings.getFloat(MainActivity.PIGGY_MONEY, 0)));
    }
}
