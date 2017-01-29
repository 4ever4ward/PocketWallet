package ua.matvienko_apps.controlyourbudget.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;

/**
 * Created by alex_ on 10-Sep-16.
 */

public class GroupAdapter extends CursorAdapter {

    private final String TAG = "GroupAdapter";

    private Cursor cursor;

    public static int COL_GROUP_NAME = 1;
    public static int COL_GROUP_TYPE = 2;
    public static int COL_GROUP_PRIORITY = 3;

    private class ViewHolder {
        final TextView groupNameView;
        final ImageView groupImageView;

        ViewHolder(View view) {
            groupNameView = (TextView) view.findViewById(R.id.groupNameText);
            groupImageView = (ImageView) view.findViewById(R.id.groupImageView);
        }
    }


    public GroupAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        this.cursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_group, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String groupName = cursor.getString(COL_GROUP_NAME);
        viewHolder.groupNameView.setText(groupName);

        try {
            int groupPriority = cursor.getInt(COL_GROUP_PRIORITY);
            view.findViewById(R.id.groupNameContainer).setBackgroundColor(Utility.getColorByGroupPriority(groupPriority, context));
        } catch (Resources.NotFoundException ex) {
            Log.e(TAG, "No image resources for this group");
        }
        try {
            viewHolder.groupImageView.setImageResource(Utility.getIconIdByGroupName(groupName, context));
        } catch (Resources.NotFoundException ex) {
            Log.e(TAG, "No image resources for this group");
        }
    }

}
