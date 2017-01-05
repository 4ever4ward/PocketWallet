package ua.matvienko_apps.controlyourbudget.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.fragments.ExpenseFragment;
import ua.matvienko_apps.controlyourbudget.fragments.IncomeFragment;
import ua.matvienko_apps.controlyourbudget.fragments.PiggyFragment;
import ua.matvienko_apps.controlyourbudget.fragments.StatisticsFragment;


public class MainActivity extends AppCompatActivity {

    public static SharedPreferences mSettings;
    public final String APP_PREFERENCES = "WalletPreferences";
    public static final String CASH_REMAINING_MONEY = "CashRemainingMoney";
    public static final String CARD_REMAINING_MONEY = "CardRemainingMoney";
    public static final String PIGGY_MONEY = "PiggyMoney";
    public static final String REQUIRED_MONEY = "RequiredMoney";

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
            e.putBoolean("hasVisited", true);
            e.apply();
        }

        e.putBoolean("hasVisited", true);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_expenses: {
                        ExpenseFragment expenseFragment = new ExpenseFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, expenseFragment).commit();
                        break;
                    }
                    case R.id.tab_incomes: {
                        IncomeFragment incomeFragment = new IncomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, incomeFragment).commit();
                        break;
                    }
                    case R.id.tab_piggy: {
                        PiggyFragment piggyFragment = new PiggyFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, piggyFragment).commit();
//                        SearchFragment searchFragment = new SearchFragment();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, searchFragment).commit();
                        break;
                    }
                    case R.id.tab_statistics: {
                        StatisticsFragment statisticsFragment = new StatisticsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, statisticsFragment).commit();
                        break;
                    }
//                    case R.id.tab_shopList: {
//                        ShopListFragment shopListFragment = new ShopListFragment();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, shopListFragment).commit();
//                        break;
//                    }
                }
            }
        });

    }

}
