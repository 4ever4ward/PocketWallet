package ua.matvienko_apps.controlyourbudget.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.fragments.ExpenseFragment;
import ua.matvienko_apps.controlyourbudget.fragments.IncomeFragment;
import ua.matvienko_apps.controlyourbudget.fragments.PiggyFragment;
import ua.matvienko_apps.controlyourbudget.fragments.StatisticsFragment;
import ua.matvienko_apps.controlyourbudget.services.DelayedTaskService;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static SharedPreferences mSettings;
    public final String APP_PREFERENCES = "WalletPreferences";
    public static final String CASH_REMAINING_MONEY = "CashRemainingMoney";
    public static final String CARD_REMAINING_MONEY = "CardRemainingMoney";
    public static final String PIGGY_MONEY = "PiggyMoney";
    public static final String REQUIRED_MONEY = "RequiredMoney";

    public static final String CURRENCY = "Currency";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor e = mSettings.edit();

        if (!mSettings.getBoolean("hasVisited", false)) {
            e.putFloat(REQUIRED_MONEY, 0);
            e.putFloat(CASH_REMAINING_MONEY, 0);
            e.putFloat(CARD_REMAINING_MONEY, 0);
            e.putFloat(PIGGY_MONEY, 0);
            e.putString(CURRENCY, getString(R.string.currency));
            e.putBoolean("hasVisited", true);
            e.apply();
        }

        e.putBoolean("hasVisited", true);

        // Start service for search delayed tasks and remind about it
        DelayedTaskService.setServiceAlarm(getApplicationContext(), true);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new ExpenseFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_expense: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new ExpenseFragment()).commit();
                        break;
                    }
                    case R.id.action_income: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new IncomeFragment()).commit();
                        break;
                    }
                    case R.id.action_piggy: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new PiggyFragment()).commit();
                        break;
                    }
                    case R.id.action_settings: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new StatisticsFragment()).commit();
                        break;
                    }
                }

                return true;
            }
        });
    }

}
