package ua.matvienko_apps.controlyourbudget.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;
import ua.matvienko_apps.controlyourbudget.adapters.StatisticsGroupAdapter;
import ua.matvienko_apps.controlyourbudget.classes.Group;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;

/**
 * Created by alex_ on 12-Sep-16.
 */

public class StatisticsFragment extends Fragment {

    private final String TAG = StatisticsFragment.class.getSimpleName();

    private PieChart circleChart;
    private ArrayList<Group> groupSum;
    private StatisticsGroupAdapter groupRecyclerViewAdapter;

    private TextView allAddedText;
    private TextView allSpentText;

    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_statistics, null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        circleChart = (PieChart) rootView.findViewById(R.id.circleChart);

        AppDBHelper appDBHelper = new AppDBHelper(getContext(), AppDBContract.DB_NAME, null, AppDBHelper.DB_VERSION);

        List<Group> expenseGroupSum = appDBHelper.getAllGroupAsList(getString(R.string.expense_group_type));
        if (expenseGroupSum.size() > 0)
            expenseGroupSum.remove(0);
        groupRecyclerViewAdapter = new StatisticsGroupAdapter(getContext(), Utility.getAllExpenseGroupsCashSum(getContext()));

        final RecyclerView expenseGroupsList = (RecyclerView) rootView.findViewById(R.id.expenseGroups);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        expenseGroupsList.setAdapter(groupRecyclerViewAdapter);
        expenseGroupsList.setLayoutManager(linearLayoutManager);


        ArrayList<PieEntry> entries = new ArrayList<>();
        groupSum = Utility.getUsedGroupsCashSum(getContext());

        if (groupSum != null) {

            for (int i = 0; i < groupSum.size(); i++) {
                entries.add(new PieEntry(groupSum.get(i).getGroupPrice(), groupSum.get(i).getGroupName()));
            }
        }



        PieDataSet dataset = new PieDataSet(entries, "# of Calls");
        dataset.setValueTextColor(Color.TRANSPARENT);
        dataset.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataset);
        circleChart.setData(data);
        circleChart.setDescription("");
        circleChart.setEntryLabelTextSize(12);
        circleChart.setEntryLabelColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        circleChart.animateXY(2000,2000);
        circleChart.setHoleColor(Color.TRANSPARENT);

        circleChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                String groupName = ((PieEntry) e).getLabel();
                List<Group> groupList = Utility.getAllExpenseGroupsCashSum(getContext());

                for (int i = 0; i < groupList.size(); i++) {
                    if (groupList.get(i).getGroupName().equals(groupName)) {
                        expenseGroupsList.setScrollingTouchSlop(1);
                        expenseGroupsList.smoothScrollToPosition(i);
                    }
                }

            }

            @Override
            public void onNothingSelected() {
                expenseGroupsList.smoothScrollToPosition(0);
            }
        });

//        allAddedText.setText(Utility.formatMoney(Utility.getAllAddedMoney(getContext())) + " " + getContext().getString(R.string.currency));
//        allSpentText.setText(Utility.formatMoney(Utility.getAllSpentMoney(getContext())) + " " + getContext().getString(R.string.currency));

        return rootView;

    }

}
