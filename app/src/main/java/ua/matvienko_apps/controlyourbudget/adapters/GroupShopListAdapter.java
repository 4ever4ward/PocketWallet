package ua.matvienko_apps.controlyourbudget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;


/**
 * Created by alex_ on 22-Sep-16.
 */

public class GroupShopListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> usedGroupName;
    LayoutInflater inflater;

    public class ShopListViewHolder {
        ListView itemShopList;
        TextView itemType;

        public ShopListViewHolder(View itemView) {
            itemType = (TextView) itemView.findViewById(R.id.list_item_group);
            itemShopList = (ListView) itemView.findViewById(R.id.list_item_shopList);
        }

    }


    public GroupShopListAdapter(Context context, ArrayList<String> usedGroupName) {
        this.usedGroupName = usedGroupName;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return usedGroupName.size();
    }

    @Override
    public Object getItem(int position) {
        return usedGroupName.get(position);
    }

    public String getGroupName(int position) {
        return ((String) getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AppDBHelper dbHelper = new AppDBHelper(context, AppDBContract.DB_NAME, null, AppDBHelper.DB_VERSION);
        View rootView = inflater.inflate(R.layout.list_shoplist_group_item, parent, false);

        ShopListViewHolder viewHolder = new ShopListViewHolder(rootView);

        String groupName = getGroupName(position);
        viewHolder.itemType.setText(groupName);

        viewHolder.itemShopList.setAdapter(new ShopListItemAdapter(context,
                dbHelper.getAllCursor(AppDBContract.ShopListEntry.TABLE_NAME,
                        AppDBContract.ShopListEntry.COLUMN_SHOPLIST_GROUP,
                        groupName),
                0));

        return rootView;
    }


}


//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//        View rootView = LayoutInflater.from(context).inflate(R.layout.list_shoplist_group_item, viewGroup, false);
//
//        ShopListViewHolder viewHolder = new ShopListViewHolder(rootView);
//        rootView.setTag(viewHolder);
//
//        return rootView;
//    }
//
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        ShopListViewHolder viewHolder = (ShopListViewHolder) view.getTag();
//
//        String groupName = cursor.getString(2);
//        viewHolder.itemType.setText(groupName);


//        DateTime dateTime;
//
//        long dateMillis = cursor.getLong(1);
//        dateTime = new DateTime(dateMillis);
//        viewHolder.itemDate.setText(dateTime.dayOfMonth().getAsText() + " " + dateTime.monthOfYear().getAsShortText());
//
//        String group = cursor.getString(2);
//        viewHolder.itemType.setText(group);
//
//        String name = cursor.getString(3);
//        viewHolder.itemName.setText(name);
//
//        double cost = cursor.getDouble(4);
//        viewHolder.itemCost.setText(Double.toString(cost));
//
//        int checked = cursor.getInt(5);
//        if (checked != 0) {
//            viewHolder.itemChecked.setChecked(true);
//        } else {
//            viewHolder.itemChecked.setChecked(false);
//        }
//    }


