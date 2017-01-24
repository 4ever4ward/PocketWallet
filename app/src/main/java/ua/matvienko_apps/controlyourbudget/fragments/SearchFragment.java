package ua.matvienko_apps.controlyourbudget.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.adapters.IncomeCursorAdapter;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;

public class SearchFragment extends Fragment {

    EditText columnTypeEdt;
    EditText columnValueEdt;
    ImageView searchBtn;
    ListView searchResultListView;

    AppDBHelper dbHelper;
    IncomeCursorAdapter cursorAdapter;

    View rootView;

    private String SEARCH_TYPE = "";
    private final String EXPENSE = AppDBContract.ExpensesEntry.TABLE_NAME;
    private final String INCOME = AppDBContract.IncomeEntry.TABLE_NAME;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_search, null);

//        columnTypeEdt = (EditText) rootView.findViewById(R.id.column_type);
//        columnValueEdt = (EditText) rootView.findViewById(R.id.column_value);
//
//        dbHelper = new AppDBHelper(getContext(), AppDBContract.DB_NAME, null, AppDBHelper.DB_VERSION);
//
//        searchResultListView = (ListView) rootView.findViewById(R.id.search_res_list);
//
//        // ImageView for select from what user take money
//        final Button expenseButton = (Button) rootView.findViewById(R.id.expense_button);
//        final Button incomeButton = (Button) rootView.findViewById(R.id.income_button);
//
//
//        expenseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (SEARCH_TYPE != EXPENSE) {
//                    v.setBackgroundColor(Color.CYAN);
//                    SEARCH_TYPE = EXPENSE;
//                    Toast.makeText(getContext(), EXPENSE, Toast.LENGTH_SHORT).show();
//                    expenseButton.setBackgroundColor(Color.TRANSPARENT);
//                }
//            }
//        });
//        incomeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (SEARCH_TYPE != INCOME) {
//                    v.setBackgroundColor(Color.CYAN);
//                    SEARCH_TYPE = INCOME;
//                    Toast.makeText(getContext(), INCOME, Toast.LENGTH_SHORT).show();
//                    incomeButton.setBackgroundColor(Color.TRANSPARENT);
//                }
//            }
//        });
//
//
//        searchBtn = (ImageButton) rootView.findViewById(R.id.button_search);
//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String columnType = columnTypeEdt.getText().toString();
//                String columnValue = columnValueEdt.getText().toString();
//
//                Toast.makeText(getContext(), SEARCH_TYPE, Toast.LENGTH_SHORT).show();
//                cursorAdapter = new IncomeCursorAdapter(getContext(),
//                        dbHelper.getAllCursor(SEARCH_TYPE, columnType, columnValue),
//                        0,
//                        R.layout.list_search_res_item);
//
//                searchResultListView.setAdapter(cursorAdapter);
//
//                }
//        });


        return rootView;
    }
}
