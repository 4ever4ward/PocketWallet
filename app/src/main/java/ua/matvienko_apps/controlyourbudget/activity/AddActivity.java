package ua.matvienko_apps.controlyourbudget.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.fragments.ShopListFragment;
import ua.matvienko_apps.controlyourbudget.fragments.AddFragment;
import ua.matvienko_apps.controlyourbudget.fragments.ExpenseFragment;
import ua.matvienko_apps.controlyourbudget.fragments.IncomeFragment;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        String fragmentName = getIntent().getStringExtra("FragmentName");

        if (fragmentName.equals(ExpenseFragment.class.getSimpleName()) ||
                fragmentName.equals(IncomeFragment.class.getSimpleName()) ||
                fragmentName.equals(ShopListFragment.class.getSimpleName())) {

            AddFragment addFragment = new AddFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.addContainer, addFragment).commit();

        }

    }

}
