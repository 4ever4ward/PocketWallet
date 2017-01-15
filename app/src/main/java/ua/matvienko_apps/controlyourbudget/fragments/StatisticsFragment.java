package ua.matvienko_apps.controlyourbudget.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;
import ua.matvienko_apps.controlyourbudget.adapters.StatisticsGroupAdapter;
import ua.matvienko_apps.controlyourbudget.classes.Group;

/**
 * Created by alex_ on 12-Sep-16.
 */

public class StatisticsFragment extends Fragment {

    PieChart circleChart;
    ArrayList<Group> groupSum;
    StatisticsGroupAdapter groupRecyclerViewAdapter;

    TextView groupNameText;
    TextView groupCostText;
    TextView allAddedText;
    TextView allSpentText;

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

        groupNameText = (TextView) rootView.findViewById(R.id.groupName);
        groupCostText = (TextView) rootView.findViewById(R.id.groupCost);
        allAddedText = (TextView) rootView.findViewById(R.id.all_of_added_moneys);
        allSpentText = (TextView) rootView.findViewById(R.id.all_of_spent_moneys);

        ArrayList<PieEntry> entries = new ArrayList<>();

        groupSum = Utility.getGroupsCashSum(getContext());

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
                groupNameText.setText(((PieEntry) e).getLabel());
                groupCostText.setText(Utility.formatMoney(((PieEntry) e).getValue()));
            }

            @Override
            public void onNothingSelected() {

            }
        });

        groupRecyclerViewAdapter = new StatisticsGroupAdapter(groupSum);

        allAddedText.setText(Utility.formatMoney(Utility.getAllAddedMoney(getContext())));
        allSpentText.setText(Utility.formatMoney(Utility.getAllSpentMoney(getContext())));

        return rootView;

    }

}
