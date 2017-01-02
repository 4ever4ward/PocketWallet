package ua.matvienko_apps.controlyourbudget.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ua.matvienko_apps.controlyourbudget.classes.Group;
import ua.matvienko_apps.controlyourbudget.R;

/**
 * Created by alex_ on 13-Sep-16.
 */

public class StatisticsGroupAdapter extends RecyclerView.Adapter<StatisticsGroupAdapter.GroupViewHolder> {


    public class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView groupNameText;
        TextView groupPriceText;

        public GroupViewHolder(View itemView) {
            super(itemView);

            groupNameText = (TextView) itemView.findViewById(R.id.groupNameText);
            groupPriceText = (TextView) itemView.findViewById(R.id.groupPriceText);

        }
    }

    ArrayList<Group> groupList = new ArrayList<Group>();
    public StatisticsGroupAdapter(ArrayList<Group> groupList) {
        this.groupList = groupList;
    }


    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_statistic_group_item, null);
        GroupViewHolder viewHolder = new GroupViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int pos) {
        holder.groupNameText.setText(groupList.get(pos).getGroupName());
        holder.groupPriceText.setText(Float.toString(groupList.get(pos).getGroupPrice()));
    }

    @Override
    public int getItemCount() {
        if (groupList == null) {
            return 0;
        }
        return groupList.size();
    }
}
