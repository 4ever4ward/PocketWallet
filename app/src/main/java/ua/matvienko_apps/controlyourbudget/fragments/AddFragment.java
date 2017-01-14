package ua.matvienko_apps.controlyourbudget.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;
import ua.matvienko_apps.controlyourbudget.activity.AddGroupDialogActivity;
import ua.matvienko_apps.controlyourbudget.activity.MainActivity;
import ua.matvienko_apps.controlyourbudget.adapters.GroupAdapter;
import ua.matvienko_apps.controlyourbudget.classes.Expense;
import ua.matvienko_apps.controlyourbudget.classes.Group;
import ua.matvienko_apps.controlyourbudget.classes.Income;
import ua.matvienko_apps.controlyourbudget.classes.ShopItem;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;

import static ua.matvienko_apps.controlyourbudget.Utility.CARD;
import static ua.matvienko_apps.controlyourbudget.Utility.CASH;

/**
 * Created by alex_ on 09-Sep-16.
 */

public class AddFragment extends Fragment {

    private GridView groupGridView;
    private EditText itemNameView;
    private EditText itemCostView;
    private DateTime mDateTimeNow;
    AppDBHelper appDBHelper;
    private String expenseGroup;
    private String incomeGroup;
    private String itemGroup;
    private SharedPreferences.Editor editor;

    private int ADD_TYPE;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, null);

        editor = MainActivity.mSettings.edit();

        itemNameView = (EditText) rootView.findViewById(R.id.addExpenseName);
        itemCostView = (EditText) rootView.findViewById(R.id.addExpenseCost);
        groupGridView = (GridView) rootView.findViewById(R.id.groupGridView);

        groupGridView.setNumColumns(3);


        appDBHelper = new AppDBHelper(getContext(),
                AppDBContract.DB_NAME, null, 1);

        // Submit add expense and cancel from fragment
        Button btnSubmitAdd = (Button) rootView.findViewById(R.id.btnSubmitAdd);
        Button btnCancelAdd = (Button) rootView.findViewById(R.id.btnCancelAdd);

        // ImageView for select from what user take money
        final ImageView cardImageView = (ImageView) rootView.findViewById(R.id.card_button);
        final ImageView cashImageView = (ImageView) rootView.findViewById(R.id.cash_button);


        cashImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ADD_TYPE != CASH) {
                    v.setBackgroundColor(Color.CYAN);
                    ADD_TYPE = CASH;
                    cardImageView.setBackgroundColor(Color.TRANSPARENT);
                    v.setBackground(getContext().getResources().getDrawable(R.drawable.group_item_selection));
                }
            }
        });
        cardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ADD_TYPE != CARD) {
                    v.setBackgroundColor(Color.CYAN);
                    ADD_TYPE = CARD;
                    cashImageView.setBackgroundColor(Color.TRANSPARENT);
                    v.setBackground(getResources().getDrawable(R.drawable.group_item_selection));
                }
            }
        });

        //At the begin user take money from cash
        //next he can change it
        cashImageView.callOnClick();


        // Init standard groups
        expenseGroup = getString(R.string.expense_group_type);
        if (!appDBHelper.getAllGroupCursor(expenseGroup).moveToFirst()) {

            appDBHelper.addGroup(new Group(getString(R.string.new_group_name), expenseGroup));
            appDBHelper.addGroup(new Group(getString(R.string.eat_group_name), expenseGroup, Utility.HIGH_PRIORITY));
            appDBHelper.addGroup(new Group(getString(R.string.fastfood_group_name), expenseGroup, Utility.LOW_PRIORITY));
            appDBHelper.addGroup(new Group(getString(R.string.drinks_group_name), expenseGroup, Utility.LOW_PRIORITY));
        }

        incomeGroup = getString(R.string.income_group_type);
        if (!appDBHelper.getAllGroupCursor(incomeGroup).moveToFirst()) {

            appDBHelper.addGroup(new Group(getString(R.string.new_group_name), incomeGroup));
            appDBHelper.addGroup(new Group(getString(R.string.salary_group_name), incomeGroup));
            appDBHelper.addGroup(new Group(getString(R.string.bank_group_name), incomeGroup));
            appDBHelper.addGroup(new Group(getString(R.string.other_group_name), incomeGroup));
        }


        final String fragmentName = getActivity().getIntent().getStringExtra("FragmentName");


        btnSubmitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int addType = ADD_TYPE;
                String name = itemNameView.getText().toString();
                String group = itemGroup;

                AddItem(fragmentName, addType, name, group);
            }
        });

        btnCancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        groupGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // if pressed group_item for create new group
                if (i == 0) {
                    Intent intent = new Intent(getActivity(), AddGroupDialogActivity.class);
                    startActivity(intent);
                    // if pressed another group_item
                } else {
                    TextView text = (TextView) view.findViewById(R.id.groupNameText);
                    itemGroup = text.getText().toString();
                }
            }
        });

        return rootView;
    }


    public void AddItem(String FragmentName, int addType, String name, String type) {

        if (!name.equals("") && type != null && !itemCostView.getText().toString().equals("")) {

            if (FragmentName.equals(ExpenseFragment.class.getSimpleName())) {

                float rMoney = getRemainingMoneyBy(addType);
                double cost = Double.parseDouble(itemCostView.getText().toString());

                if (rMoney > (float) cost) {
                    addExpense(addType,
                            name,
                            type,
                            cost);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "not enough money", Toast.LENGTH_SHORT).show();
                }

            } else if (FragmentName.equals(IncomeFragment.class.getSimpleName())) {

                addIncome(addType,
                        name,
                        type,
                        Double.parseDouble(itemCostView.getText().toString()));
                getActivity().finish();
            }
        } else {
            Toast.makeText(getContext(), "please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    public float getRemainingMoneyBy(int moneyTAG) {
        switch (moneyTAG) {
            case CASH:
                return MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0);
            case CARD:
                return MainActivity.mSettings.getFloat(MainActivity.CARD_REMAINING_MONEY, 0);
        }

        return -1;
    }

    public void updateGridViewFromDB(String groupType) {

        Cursor allGroupsCursor = appDBHelper.getAllGroupCursor(groupType);

        GroupAdapter groupAdapter = new GroupAdapter(getContext(), allGroupsCursor, 0);
        groupGridView.setAdapter(groupAdapter);

    }

    public void addExpense(int addType, String name, String type, double cost) {
        if (addType == CASH) {
            float reMoney = MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0);
            float newReMoney = reMoney - (float) cost;
            editor.putFloat(MainActivity.CASH_REMAINING_MONEY, newReMoney);
            editor.apply();

        } else if (addType == CARD) {
            float reMoney = MainActivity.mSettings.getFloat(MainActivity.CARD_REMAINING_MONEY, 0);
            float newReMoney = reMoney - (float) cost;
            editor.putFloat(MainActivity.CARD_REMAINING_MONEY, newReMoney);
            editor.apply();

        } else return;

        mDateTimeNow = new DateTime();
        Expense expense = new Expense(mDateTimeNow.getMillis(),
                type,
                name,
                cost);
        appDBHelper.addExpense(expense);
    }
    public void addIncome(int addType, String name, String type, double cost) {
        if (addType == CASH) {
            float reMoney = MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0);
            float newReMoney = reMoney + (float) cost;
            editor.putFloat(MainActivity.CASH_REMAINING_MONEY, newReMoney);
            editor.apply();

        } else if (addType == CARD) {
            float reMoney = MainActivity.mSettings.getFloat(MainActivity.CARD_REMAINING_MONEY, 0);
            float newReMoney = reMoney + (float) cost;
            editor.putFloat(MainActivity.CARD_REMAINING_MONEY, newReMoney);
            editor.apply();

        } else return;

        mDateTimeNow = new DateTime();
        Income income = new Income(mDateTimeNow.getMillis(),
                type,
                name,
                cost);
        appDBHelper.addIncome(income);


    }
    public void addShopItem(String name, String type, double cost) {
        mDateTimeNow = new DateTime();
        ShopItem shopItem = new ShopItem(mDateTimeNow.getMillis(),
                type,
                name,
                cost,
                0);
        appDBHelper.addShopListItem(shopItem);
    }

    @Override
    public void onResume() {
        super.onResume();

        String frName = getActivity().getIntent().getStringExtra("FragmentName");
        String groupType;

        if (frName.equals(IncomeFragment.class.getSimpleName())) {
            groupType = incomeGroup;
        } else {
            groupType = expenseGroup;
        }

        updateGridViewFromDB(groupType);
    }

    @Override
    public void onPause() {
        super.onPause();
        appDBHelper.close();
    }
}
