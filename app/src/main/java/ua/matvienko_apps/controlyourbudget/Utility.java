package ua.matvienko_apps.controlyourbudget;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ua.matvienko_apps.controlyourbudget.activity.MainActivity;
import ua.matvienko_apps.controlyourbudget.adapters.GroupAdapter;
import ua.matvienko_apps.controlyourbudget.classes.Expense;
import ua.matvienko_apps.controlyourbudget.classes.Group;
import ua.matvienko_apps.controlyourbudget.classes.Income;
import ua.matvienko_apps.controlyourbudget.classes.ShopItem;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;
import ua.matvienko_apps.controlyourbudget.fragments.ExpenseFragment;

import static ua.matvienko_apps.controlyourbudget.data.AppDBHelper.DB_VERSION;


public class Utility {


    public static final int LOW_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 2;
    public static final int HIGH_PRIORITY = 3;

    public static final int CASH = 1;
    public static final int CARD = 2;
    public static final int PIGGY = 3;


    public static float cashMemoSum(List<Expense> expenseIncomeList) {
        float mCashMemo = 0;

        for (int i = 0; i < expenseIncomeList.size(); i++) {
            mCashMemo += expenseIncomeList.get(i).getCost();
        }

        return mCashMemo;
    }

    public static ValueLineSeries createIncomeLineChartSeries(Context context, String tableName) {

        AppDBHelper appDBHelper = new AppDBHelper(context, tableName, null, AppDBHelper.DB_VERSION);
        ValueLineSeries series = new ValueLineSeries();
        DateTime dateTime;
        DateTime firstExpDate;
        DateTime secondExpDate;
        float daySum = 0.0f;

        List<Income> incomeList = appDBHelper.getAllIncomeAsList();
        incomeList.add(new Income(0,"","",0.0));

        series.addPoint(new ValueLinePoint("", 0f));


        if (incomeList.size() != 1) {

            for (int i = 0; i < incomeList.size() - 1; i++) {

                firstExpDate = new DateTime(incomeList.get(i).getDate());
                secondExpDate = new DateTime(incomeList.get(i + 1).getDate());

                if (firstExpDate.getYear() == secondExpDate.getYear()
                        && firstExpDate.getMonthOfYear() == secondExpDate.getMonthOfYear()
                        && firstExpDate.getDayOfMonth() == secondExpDate.getDayOfMonth()) {

                    if (daySum == 0.0) {
                        daySum += incomeList.get(i).getCost();
                    }
                    daySum += incomeList.get(i + 1).getCost();

                } else {
                    if (daySum == 0.0) {
                        daySum += incomeList.get(i).getCost();
                    }
                    dateTime = new DateTime(incomeList.get(i).getDate());
                    series.addPoint(new ValueLinePoint(dateTime.toString("dd.MM.yy"), daySum));
                    daySum = 0.0f;
                }


            }
        } else {daySum = (float) incomeList.get(0).getCost();}

        series.addPoint(new ValueLinePoint("", 0f));

        series.setColor(context.getResources().getColor(R.color.colorAccent));

        appDBHelper.close();
        return series;
    }

    /**
     *
     *
     * @param context Context
     * @return List of Group with sum of all spent moneys
     */
    public static ArrayList<Group> getGroupsCashSum(Context context) {
        AppDBHelper expenseDBHelper = new AppDBHelper(context, AppDBContract.ExpensesEntry.TABLE_NAME, null, 1);
        AppDBHelper groupDBHelper = new AppDBHelper(context, AppDBContract.GroupsEntry.TABLE_NAME, null, 1);
        Cursor expensesCursor;
        Cursor cursor;
        String groupName;
        ArrayList<Group> groupSum = new ArrayList<Group>();
        Float sum = 0.f;
        int i = 0;
        int groupPriority;

        cursor = groupDBHelper.getAllGroupCursor(context.getString(R.string.expense_group_type)); // All groups

        if (cursor.moveToFirst()) {
            do {

                groupName = cursor.getString(GroupAdapter.COL_GROUP_NAME); // find name of group by column index
                groupPriority = cursor.getInt(GroupAdapter.COL_GROUP_PRIORITY); // find priority of group by column index

                expensesCursor = expenseDBHelper.
                        getAllCursor(AppDBContract.ExpensesEntry.TABLE_NAME,
                                AppDBContract.ExpensesEntry.COLUMN_EXPENSE_GROUP,
                                groupName); // find all expenses where group = groupName

                if (expensesCursor.moveToFirst()) {
                    do {


                        sum += (float) Double.parseDouble(
                                expensesCursor.getString(ExpenseFragment.COL_EXPENSE_COST)); // calc sum of expenses cost


                    } while (expensesCursor.moveToNext());

                }
                if (sum != 0) { // if group sum not 0 then add it to list
                    groupSum.add(new Group(groupName, sum, groupPriority));
                }

                sum = 0.f; // set sum to 0.0 for next use
                i++;
            } while (cursor.moveToNext());

            expensesCursor.close();
        }

        cursor.close();

        if (groupSum.isEmpty()) return null;

        expenseDBHelper.close();
        groupDBHelper.close();
        return groupSum;
    }

    public static int isAllShopListItemChecked(Context context) {
        AppDBHelper appDBHelper = new AppDBHelper(context, AppDBContract.ShopListEntry.TABLE_NAME,
                null, DB_VERSION);

        List<ShopItem> shopList = appDBHelper.getAllCheckedShopItem();
        appDBHelper.close();
        return shopList.size();
    }

    public static float getAllSpentMoney(Context context) {
        float allSpent = 0;
        AppDBHelper appDBHelper = new AppDBHelper(context,
                AppDBContract.DB_NAME,
                null,
                AppDBHelper.DB_VERSION);

        List<Expense> expenseList = appDBHelper.getAllExpenseAsList();

        for (int i = 0; i < expenseList.size(); i++) {
            allSpent += expenseList.get(i).getCost();
        }

        appDBHelper.close();
        return allSpent;
    }

    public static float getAllAddedMoney(Context context) {
        float allAdded = 0;
        AppDBHelper appDBHelper = new AppDBHelper(context,
                AppDBContract.DB_NAME,
                null,
                AppDBHelper.DB_VERSION);

        List<Income> incomeList = appDBHelper.getAllIncomeAsList();

        for (int i = 0; i < incomeList.size(); i++) {
            allAdded += incomeList.get(i).getCost();
        }

        appDBHelper.close();
        return allAdded;
    }

    public static void emptyPiggy() {
        SharedPreferences.Editor editor = MainActivity.mSettings.edit();
        editor.putFloat(MainActivity.PIGGY_MONEY, 0.0f);
        editor.apply();
    }

    // TODO: change uses of this function to moveMoney()
    public static float takeMoneyFromPiggy(float amount) {
        SharedPreferences.Editor editor = MainActivity.mSettings.edit();
        float newPiggyValue = MainActivity.mSettings.getFloat(MainActivity.PIGGY_MONEY, 0) - amount;

        editor.putFloat(MainActivity.PIGGY_MONEY, newPiggyValue);
        editor.apply();

        return amount;
    }

    /**
     *
     * @param groupName The name of the group
     * @param context Context
     * @return Returns the drawable, which depends from groupName
     */
    public static int getIconIdByGroupName (String groupName, Context context) {

        if (groupName.equals(context.getString(R.string.fastfood_group_name))) {
            return R.drawable.ic_expense_group_fastfood;
        } else if (groupName.equals(context.getString(R.string.drinks_group_name))) {
            return R.drawable.ic_expense_group_drinks;
        } else if (groupName.equals(context.getString(R.string.eat_group_name))) {
            return R.drawable.ic_expense_group_eat;
        } else if (groupName.equals(context.getString(R.string.new_group_name))) {
            return R.drawable.ic_add_white;
        } else if (groupName.equals(context.getString(R.string.fruits_group_name))) {
            return R.drawable.ic_expense_group_fruits;
        }
        return -1;
    }

    public static String formatMoney (float money) {
        DecimalFormat dFormat = new DecimalFormat("####,###,##0.00");
        return dFormat.format(money);
    }

    public static int getColorByGroupPriority (int groupPriority, Context context) {
        if (groupPriority == Utility.LOW_PRIORITY) {
            return ContextCompat.getColor(context, R.color.colorCyan);
        } else if (groupPriority == Utility.NORMAL_PRIORITY) {
            return ContextCompat.getColor(context, R.color.colorGreen);
        } else if (groupPriority == Utility.HIGH_PRIORITY) {
            return ContextCompat.getColor(context, R.color.colorRed);
        }
        return Color.TRANSPARENT;
    }


    /**
     * Move money between money types
     *
     * @param from String value, which contain name of from money type in SharedPreferences
     * @param to String value, which contain name of to money type in SharedPreferences
     * @param value float value, which contain quantity of money
     */
    public static void moveMoney(String from, String to, float value) {
        SharedPreferences.Editor editor = MainActivity.mSettings.edit();

        float from_value = MainActivity.mSettings.getFloat(from, 0f);
        float to_value = MainActivity.mSettings.getFloat(to, 0f);

        if (value <= from_value) {
            editor.putFloat(from, from_value - value);
            editor.putFloat(to, to_value + value);
        }


        editor.apply();

    }

    /**
     * Get all spent money by group priority
     *
     * @param context Context
     * @param groupPriority that contain priority num
     * @return All spent money with this priority
     */
    public static float getAllSpentMoneyByGroupPriority(Context context, int groupPriority) {
        ArrayList<Group> groupSumList = getGroupsCashSum(context);
        float result_sum = 0;

        assert groupSumList != null;
        for (int i = 0; i < groupSumList.size(); i++) {
            if (groupSumList.get(i).getGroupPriority() == groupPriority) {
                result_sum += groupSumList.get(i).getGroupPrice();
            }
        }

        return result_sum;
    }
}