package ua.matvienko_apps.controlyourbudget.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;
import ua.matvienko_apps.controlyourbudget.activity.MainActivity;
import ua.matvienko_apps.controlyourbudget.activity.PiggyDialogActivity;

/**
 * Created by alex_ on 16-Sep-16.
 */

public class PiggyFragment extends Fragment {

    public TextView piggyAmountView;
    public TextView requiredMoneyView;
    public Button crashPiggyButton;


    View dialogView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_piggy, null);
        dialogView = inflater.inflate(R.layout.dialog_piggy_view, null);

        crashPiggyButton = (Button) rootView.findViewById(R.id.crashPiggyButton);
        piggyAmountView = (TextView) rootView.findViewById(R.id.piggyAmountView);
        requiredMoneyView = (TextView) rootView.findViewById(R.id.requiredMoneyView);
        FloatingActionButton addMoneyToPiggy = (FloatingActionButton) rootView.findViewById(R.id.addMoneyToPiggy);


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



    @Override
    public void onResume() {
        super.onResume();

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
