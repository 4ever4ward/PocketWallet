package ua.matvienko_apps.controlyourbudget.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;
import ua.matvienko_apps.controlyourbudget.activity.AddActivity;
import ua.matvienko_apps.controlyourbudget.activity.MainActivity;
import ua.matvienko_apps.controlyourbudget.adapters.ExpenseCursorAdapter;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;


/**
 * Created by alex_ on 04-Sep-16.
 */
public class ExpenseFragment extends Fragment {

    private ListView expenseListView;
    private AppDBHelper expensesDbHelper;
    private TextView footerText;
    private TextView rCashMoneyTextView;
    private TextView rCardMoneyTextView;


    public static final int COL_EXPENSE_DATE = 1;
    public static final int COL_EXPENSE_GROUP = 2;
    public static final int COL_EXPENSE_NAME = 3;
    public static final int COL_EXPENSE_COST = 4;


    View rootView;
    View listviewHeader;
    View listviewFooter;

    DateTime currentDayStart;
    DateTime currentDayEnd;
    LinearLayout bookmarksLayout;
    DateTime now;
    LayoutInflater inflater;

    Resources resources;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflater = getLayoutInflater(savedInstanceState);

        resources = getResources();

        rootView = inflater.inflate(R.layout.fragment_expenses, null);
        listviewHeader = inflater.inflate(R.layout.list_expense_header, null);
        listviewFooter = inflater.inflate(R.layout.list_expense_footer, null);

        bookmarksLayout = (LinearLayout) rootView.findViewById(R.id.bookmarksLayout);

        now = new DateTime();
        currentDayStart = now.minusHours(now.getHourOfDay());

        currentDayEnd = currentDayStart.plusHours(24);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final FloatingActionButton addElementButton = (FloatingActionButton) rootView.findViewById(R.id.addElementButton);
        TextView dayOfMonth = (TextView) rootView.findViewById(R.id.date_textView);
        TextView monthName = (TextView) rootView.findViewById(R.id.month_textView);
        expenseListView = (ListView) rootView.findViewById(R.id.expenseListView);
        rCashMoneyTextView = (TextView) rootView.findViewById(R.id.rCashMoneyTextView);
        rCardMoneyTextView = (TextView) rootView.findViewById(R.id.rCardMoneyTextView);
        footerText = (TextView) listviewFooter.findViewById(R.id.cashMemoSum);

        dayOfMonth.setText(Integer.toString(now.getDayOfMonth()));
        monthName.setText(now.monthOfYear().getAsShortText());
        rCashMoneyTextView.setText("Осталось: " + Float.toString(MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, -5)));


        expenseListView.setVerticalScrollBarEnabled(false);
        expenseListView.setHeaderDividersEnabled(false);
        expenseListView.setFooterDividersEnabled(false);
        expenseListView.setDivider(null);
        expenseListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem != 0) {
                    addElementButton.hide();
                } else {
                    addElementButton.show();
                }
            }
        });

//        expenseListView.addHeaderView(listviewHeader, null, false);
        expenseListView.addFooterView(listviewFooter, null, false);


        expensesDbHelper = new AppDBHelper(getContext(),
                AppDBContract.ExpensesEntry.TABLE_NAME, null, 1);

//        update();


        addElementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra("FragmentName", ExpenseFragment.class.getSimpleName());
                startActivity(intent);
            }

        });

        return rootView;
    }

    private void update() {

        // All cursor expense, that added at this day
        Cursor dayCursorForExpenseTable = expensesDbHelper
                .getAllCursor(
                        currentDayStart.getMillis(),
                        currentDayEnd.getMillis(),
                        AppDBContract.ExpensesEntry.TABLE_NAME
                );

        // Update list from database
        ExpenseCursorAdapter expenseAdapter = new ExpenseCursorAdapter(getContext(),
                dayCursorForExpenseTable,
                0,
                R.layout.list_expense_item);
        expenseListView.setAdapter(expenseAdapter);

        // Update textView
        rCashMoneyTextView.setText(Utility.formatMoney(MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0)) + "  UAH");
        rCardMoneyTextView.setText(Utility.formatMoney(MainActivity.mSettings.getFloat(MainActivity.CARD_REMAINING_MONEY, 0)) + "  UAH");


        // Update sum of daily expense list
        double totalSum = Utility.cashMemoSum(expensesDbHelper.getAllExpenseAsList(
                AppDBContract.ExpensesEntry.TABLE_NAME,
                AppDBContract.ExpensesEntry.COLUMN_EXPENSE_DATE,
                Long.toString(currentDayStart.getMillis()),
                Long.toString(currentDayEnd.getMillis())
        ));


        footerText.setText(String.format(resources.getString(R.string.total_spending_cost), Utility.formatMoney((float) totalSum)) + "  UAH");

    }


    @Override
    public void onResume() {
        super.onResume();
        bookmarksLayout.startLayoutAnimation();
        update();
    }


}


