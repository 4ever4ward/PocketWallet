package ua.matvienko_apps.controlyourbudget.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLineSeries;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;
import ua.matvienko_apps.controlyourbudget.activity.AddActivity;
import ua.matvienko_apps.controlyourbudget.adapters.IncomeCursorAdapter;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;

/**
 * Created by alex_ on 11-Sep-16.
 */

public class IncomeFragment extends Fragment {


    private ListView incomeListView;
    private AppDBHelper incomeDbHelper;
    private ValueLineChart lineChart;
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater(savedInstanceState);

        rootView = inflater.inflate(R.layout.fragment_incomes, null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final FloatingActionButton addElementButton = (FloatingActionButton) rootView.findViewById(R.id.addIncomeButton);
        incomeListView = (ListView) rootView.findViewById(R.id.incomesListView);

        lineChart = (ValueLineChart) rootView.findViewById(R.id.lineChart);

        incomeListView.setVerticalScrollBarEnabled(false);
        incomeListView.setDivider(null);
//        incomeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                int _id = incomeListView.getCount() - position;
//                Toast.makeText(getContext(), "position = " + position + " id = " + id, Toast.LENGTH_LONG).show();
//                incomeDbHelper.delete(AppDBContract.IncomeEntry.TABLE_NAME, _id);
//                update();
//                return false;
//            }
//        });

        incomeListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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


        incomeDbHelper = new AppDBHelper(getContext(),
                AppDBContract.IncomeEntry.TABLE_NAME, null, 1);

//        update();

        addElementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra("FragmentName", IncomeFragment.class.getSimpleName());
                startActivity(intent);
            }

        });

        return rootView;
    }

    private void update() {
        Cursor allCursorForIncomeTable = incomeDbHelper
                .getAllCursor(AppDBContract.IncomeEntry.TABLE_NAME);

        IncomeCursorAdapter incomeAdapter = new IncomeCursorAdapter(getContext(),
                allCursorForIncomeTable, 0, R.layout.list_income_item);

        incomeListView.setAdapter(incomeAdapter);

        ValueLineSeries series = Utility.createIncomeLineChartSeries(getContext(), AppDBContract.IncomeEntry.TABLE_NAME);
        lineChart.clearChart();
        lineChart.addSeries(series);
        lineChart.startAnimation();

    }


    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onPause() {
        super.onPause();
        incomeDbHelper.close();
    }
}
