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

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;


/**
 * Created by alex_ on 22-Sep-16.
 */

public class ExpenseCursorAdapter extends CursorAdapter {

    final int COL_EXPENSE_DATE = 1;
    final int COL_EXPENSE_GROUP = 2;
    final int COL_EXPENSE_NAME = 3;
    final int COL_EXPENSE_COST = 4;

    int viewId;

    public static class ViewHolder {

        ImageView itemImage;
        TextView itemName;
        TextView itemCost;

        public ViewHolder(View itemView) {
            itemImage = (ImageView) itemView.findViewById(R.id.list_item_image);
            itemName = (TextView) itemView.findViewById(R.id.list_item_name);
            itemCost = (TextView) itemView.findViewById(R.id.list_item_cost);

        }
    }

    public ExpenseCursorAdapter(Context context, Cursor c, int flags, int id) {
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

        String group = cursor.getString(COL_EXPENSE_GROUP);

        String name = cursor.getString(COL_EXPENSE_NAME);
        viewHolder.itemName.setText(name);

        float cost = cursor.getFloat(COL_EXPENSE_COST);
        viewHolder.itemCost.setText(Utility.formatMoney(cost) + " " + context.getString(R.string.currency));

        try {
            viewHolder.itemImage.setImageResource(Utility.getIconIdByGroupName(group, context));
        } catch (Resources.NotFoundException ex) {

        }

    }
}

