package ua.matvienko_apps.controlyourbudget.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;
import ua.matvienko_apps.controlyourbudget.activity.AddActivity;
import ua.matvienko_apps.controlyourbudget.activity.MainActivity;
import ua.matvienko_apps.controlyourbudget.adapters.GroupShopListAdapter;
import ua.matvienko_apps.controlyourbudget.classes.Expense;
import ua.matvienko_apps.controlyourbudget.classes.ShopItem;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;

/**
 * Created by alex_ on 22-Sep-16.
 */

public class ShopListFragment extends Fragment {

    private ListView shopListView;
    private AppDBHelper appDBHelper;
    private Button endShoppingButton;
    private List<ShopItem> checkedItemsList;
    private SharedPreferences.Editor editor;
    private float rMoney;


    public final int ITEM_CHECKED = 1;
    public final int ITEM_NOT_CHECKED = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shoplist, null);

        appDBHelper = new AppDBHelper(getActivity(),
                AppDBContract.DB_NAME, null, 1);

        shopListView = (ListView) rootView.findViewById(R.id.shopListView);
        final FloatingActionButton addShopListItem = (FloatingActionButton) rootView.findViewById(R.id.addShopListItem);
        endShoppingButton = (Button) rootView.findViewById(R.id.endShoppingButton);

        rMoney = MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0);


        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                int checked;

                if (!((CheckBox)(view.findViewById(R.id.list_item_checked))).isChecked()) {
                    checked = ITEM_CHECKED;
                } else checked = ITEM_NOT_CHECKED;

                ShopItem shopItem = new ShopItem(cursor.getLong(1), cursor.getString(2),
                        cursor.getString(3), cursor.getDouble(4), checked);

                appDBHelper.updateShopListItem(shopItem);


                // TODO: Change color button to grey if not have an element and do it unClickable
                // Change color if all items is checked to Green
                // and to Red if not all items is checked
                if (Utility.isAllShopListItemChecked(getContext()) == shopListView.getCount()) {
                    endShoppingButton.setBackgroundColor(Color.GREEN);
                } else if (Utility.isAllShopListItemChecked(getContext()) < shopListView.getCount()) {
                    endShoppingButton.setBackgroundColor(Color.RED);
                } else {
                    endShoppingButton.setBackgroundColor(Color.DKGRAY);
                }

                updateListFromDB();
            }
        });

        // ShPref editor for calculate spent money
        editor = MainActivity.mSettings.edit();

        updateListFromDB();

        addShopListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra("FragmentName", ShopListFragment.class.getSimpleName());
                startActivity(intent);
            }
        });


        endShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedItemsList = appDBHelper.getAllCheckedShopItem();

                float checkedItemsSum = 0;
                for (int i = 0; i < checkedItemsList.size(); i++) {
                    checkedItemsSum += checkedItemsList.get(i).getCost();
                }


                if (checkedItemsList.size() == shopListView.getCount()) {
                    if (checkedItemsSum <= rMoney ) {
                            // Move all of checked items to expense table
                            moveCheckedItems();
                    } else Toast.makeText(getActivity(), "Not enough remaining money. You cannot buy all of this items", Toast.LENGTH_SHORT).show();

                } else {
                    final float itemsSum = checkedItemsSum;
                    // TODO: Change Message and Title to String resource
                    AlertDialog.Builder attentionDialog = new AlertDialog.Builder(getContext());
                    attentionDialog.setCancelable(false)
                            .setTitle("Attention")
                            .setMessage("Are you want to finish buying?/n" +
                                    "All of your unchecked items not be added to" +
                                    "your expense list")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (itemsSum <= rMoney ) {
                                        // Move all of checked items to expense table
                                        moveCheckedItems();
                                    } else
                                        Toast.makeText(getActivity(), "Not enough remaining money. You cannot buy all of this items", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create();
                    attentionDialog.show();
                }
                updateListFromDB();
            }
        });

        return rootView;
    }

    public void updateListFromDB() {

        GroupShopListAdapter listCursorAdapter = new GroupShopListAdapter(getContext(),
                appDBHelper.getAllUsedGroup());

        shopListView.setAdapter(listCursorAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (shopListView.getCount() == 0) {
            endShoppingButton.setBackgroundColor(Color.DKGRAY);
        }
        else if (Utility.isAllShopListItemChecked(getContext()) == shopListView.getCount()) {
            endShoppingButton.setBackgroundColor(Color.GREEN);
        } else endShoppingButton.setBackgroundColor(Color.RED);

        updateListFromDB();
    }

    public void moveCheckedItems() {

        // Add all checked items to expenseList
        for (int pos = 0; pos < checkedItemsList.size(); pos++) {
            appDBHelper.addExpense(new Expense(checkedItemsList.get(pos).getDate(), checkedItemsList.get(pos).getGroup(),
                    checkedItemsList.get(pos).getName(), checkedItemsList.get(pos).getCost(), 0));

            // Change value of CASH_REMAINING_MONEY
            Float rMoney = MainActivity.mSettings.getFloat(MainActivity.CASH_REMAINING_MONEY, 0)
                    - (float) checkedItemsList.get(pos).getCost();

            editor.putFloat(MainActivity.CASH_REMAINING_MONEY, rMoney);
            editor.apply();

        }
        // EMPTY ShopList table
        appDBHelper.delete(AppDBContract.ShopListEntry.TABLE_NAME);
        this.onResume();

    }


}
