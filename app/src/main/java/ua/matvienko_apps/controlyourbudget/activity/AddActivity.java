package ua.matvienko_apps.controlyourbudget.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.fragments.AddFragment;
import ua.matvienko_apps.controlyourbudget.fragments.ExpenseFragment;
import ua.matvienko_apps.controlyourbudget.fragments.IncomeFragment;
import ua.matvienko_apps.controlyourbudget.fragments.ShopListFragment;

public class AddActivity extends AppCompatActivity {

    private String TAG = AddActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        String fragmentName = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fragmentName = extras.getString("FragmentName");
        }


        if (fragmentName != null) {
            if (fragmentName.equals(ExpenseFragment.class.getSimpleName()) ||
                    fragmentName.equals(IncomeFragment.class.getSimpleName()) ||
                    fragmentName.equals(ShopListFragment.class.getSimpleName())) {

                AddFragment addFragment = new AddFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.addContainer, addFragment).commit();

            }
        }

    }

}
