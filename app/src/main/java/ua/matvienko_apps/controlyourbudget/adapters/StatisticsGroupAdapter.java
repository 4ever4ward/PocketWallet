package ua.matvienko_apps.controlyourbudget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;
import ua.matvienko_apps.controlyourbudget.classes.Group;

/**
 * Created by alex_ on 13-Sep-16.
 */

public class StatisticsGroupAdapter extends RecyclerView.Adapter<StatisticsGroupAdapter.GroupViewHolder> {

    Context context;

    public class GroupViewHolder extends RecyclerView.ViewHolder {

        ImageView groupTypeImage;
        TextView groupNameText;
        TextView groupPriceText;

        public GroupViewHolder(View itemView) {
            super(itemView);

            groupTypeImage = (ImageView) itemView.findViewById(R.id.groupImageView);
            groupNameText = (TextView) itemView.findViewById(R.id.groupNameText);
            groupPriceText = (TextView) itemView.findViewById(R.id.groupPriceText);

        }
    }

    List<Group> groupList = new ArrayList<Group>();

    public StatisticsGroupAdapter(Context context, List<Group> groupList) {
        this.groupList = groupList;
        this.context = context;
    }


    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_statistic_group_item, null);
        GroupViewHolder viewHolder = new GroupViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int pos) {
        String groupName = groupList.get(pos).getGroupName();

        holder.groupTypeImage.setImageResource(Utility.getIconIdByGroupName(groupName, context));
        holder.groupNameText.setText(groupName);
        holder.groupPriceText.setText(Float.toString(groupList.get(pos).getGroupPrice()) + " " + context.getString(R.string.currency));
    }

    @Override
    public int getItemCount() {
        if (groupList == null) {
            return 0;
        }
        return groupList.size();
    }
}
