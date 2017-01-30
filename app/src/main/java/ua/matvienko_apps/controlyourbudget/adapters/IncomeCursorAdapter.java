package ua.matvienko_apps.controlyourbudget.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;

import static ua.matvienko_apps.controlyourbudget.fragments.ExpenseFragment.COL_EXPENSE_COST;

/**
 * Created by alex_ on 09-Oct-16.
 */

public class IncomeCursorAdapter extends CursorAdapter {

    final int COL_INCOME_DATE = 1;
    final int COL_INCOME_GROUP = 2;
    final int COL_INCOME_NAME = 3;
    final int COL_INCOME_COST = 4;

    int viewId;

    public static class ViewHolder {

        ImageView itemImage;
        TextView itemDate;
        TextView itemName;
        TextView itemType;
        TextView itemCost;

        public ViewHolder(View itemView) {
            itemImage = (ImageView) itemView.findViewById(R.id.list_item_image);
            itemName = (TextView) itemView.findViewById(R.id.list_item_name);
            itemDate = (TextView) itemView.findViewById(R.id.list_item_date);
            itemType = (TextView) itemView.findViewById(R.id.list_item_group);
            itemCost = (TextView) itemView.findViewById(R.id.list_item_cost);

        }
    }

    public IncomeCursorAdapter(Context context, Cursor c, int flags, int id) {
        super(context, c, flags);
        this.viewId = id;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(viewId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        DateTime dateTime;

        try {
            viewHolder.itemImage.setImageResource(Utility.getIconIdByGroupName(cursor.getString(COL_INCOME_GROUP), context));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        long dateMillis = cursor.getLong(COL_INCOME_DATE);
        dateTime = new DateTime(dateMillis);
        viewHolder.itemDate.setText(dateTime.dayOfMonth().getAsText() + " " + dateTime.monthOfYear().getAsShortText());

        String group = cursor.getString(COL_INCOME_GROUP);
        viewHolder.itemType.setText(group);

        String name = cursor.getString(COL_INCOME_NAME);
        viewHolder.itemName.setText(name);

        float cost = cursor.getFloat(COL_EXPENSE_COST);
        viewHolder.itemCost.setText(Utility.formatMoney(cost) + " " + context.getString(R.string.currency));

        String groupName = cursor.getString(COL_INCOME_NAME);
        viewHolder.itemName.setText(groupName);

        if (groupName == "Fastfood") {
            viewHolder.itemImage.setImageResource(Utility.getIconIdByGroupName(groupName, context));

        }

    }
}
