package ua.matvienko_apps.controlyourbudget.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ua.matvienko_apps.controlyourbudget.R;

/**
 * Created by alex_ on 20-Sep-16.
 */

public class PiggyDialogActivity extends AppCompatActivity {

    private EditText piggyValueView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_piggy_view);

        piggyValueView = (EditText) findViewById(R.id.piggyValueEditText);
        Button submitAddButton = (Button) findViewById(R.id.submitAddButton);
        Button cancelAddButton = (Button) findViewById(R.id.cancelAddButton);



        submitAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor mEditor = MainActivity.mSettings.edit();
                Float moneyBuffer = Float.parseFloat(piggyValueView.getText().toString());
                Float rMoney = MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0);

                if (MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0) != 0
                        || MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0) >= moneyBuffer) {

                    if (!piggyValueView.getText().toString().equals("")) {

                        Float pMoney = MainActivity.mSettings.getFloat(MainActivity.PIGGY_MONEY, 0)
                                + moneyBuffer;
                        Float resMoney = rMoney - moneyBuffer;

                        mEditor.putFloat(MainActivity.CASH_REMAINING_MONEY, resMoney);
                        mEditor.putFloat(MainActivity.PIGGY_MONEY, pMoney);
                        mEditor.apply();
                        finish();
                    } else
                        Toast.makeText(PiggyDialogActivity.this, "Field is empty", Toast.LENGTH_SHORT).show();
                } else
                //TODO: change text to string resource and display dialog with this resource
                    Toast.makeText(PiggyDialogActivity.this, "U're have not enough money", Toast.LENGTH_SHORT).show();
            }
        });

        cancelAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
