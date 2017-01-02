package ua.matvienko_apps.controlyourbudget.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ua.matvienko_apps.controlyourbudget.R;

/**
 * Created by alex_ on 17-Oct-16.
 */

public class ShopListItemAdapter extends CursorAdapter{


    private static class ViewHolder {
        TextView itemName;
        TextView itemCost;
        CheckBox itemChecked;

        public ViewHolder(View view) {
            itemName = (TextView) view.findViewById(R.id.list_item_name);
            itemCost = (TextView) view.findViewById(R.id.list_item_cost);
            itemChecked = (CheckBox) view.findViewById(R.id.list_item_checked);
        }
    }


    public ShopListItemAdapter(Context context, Cursor c, int flags) {super(context, c, flags);}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.list_shoplist_item, null);
        ViewHolder viewHolder = new ViewHolder(rootView);
        rootView.setTag(viewHolder);

        return rootView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = new ViewHolder(view);//(ViewHolder) view.getTag();

        String name = cursor.getString(3);
        viewHolder.itemName.setText(name);

        double cost = cursor.getDouble(4);
        viewHolder.itemCost.setText(Double.toString(cost));

        int checked = cursor.getInt(5);
        if (checked != 0) {
            viewHolder.itemChecked.setChecked(true);
        } else {
            viewHolder.itemChecked.setChecked(false);
        }

    }
}
