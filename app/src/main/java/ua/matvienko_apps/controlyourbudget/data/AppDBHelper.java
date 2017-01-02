package ua.matvienko_apps.controlyourbudget.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ua.matvienko_apps.controlyourbudget.classes.Expense;
import ua.matvienko_apps.controlyourbudget.classes.Group;
import ua.matvienko_apps.controlyourbudget.classes.Income;
import ua.matvienko_apps.controlyourbudget.classes.ShopItem;

import static ua.matvienko_apps.controlyourbudget.data.AppDBContract.ExpensesEntry.COLUMN_EXPENSE_DATE;

/**
 * Created by alex_ on 04-Sep-16.
 */
public class AppDBHelper extends SQLiteOpenHelper {

    public final static int DB_VERSION = 1;

    Context mContext;

    public AppDBHelper(Context context, String db_name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, AppDBContract.DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table with six columns for expenses
        String createExpensesTable = "CREATE TABLE "
                + AppDBContract.ExpensesEntry.TABLE_NAME + " ("
                + AppDBContract.ExpensesEntry.COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AppDBContract.ExpensesEntry.COLUMN_EXPENSE_DATE + " INTEGER NOT NULL, "
                + AppDBContract.ExpensesEntry.COLUMN_EXPENSE_GROUP + " TEXT NOT NULL, "
                + AppDBContract.ExpensesEntry.COLUMN_EXPENSE_NAME + " TEXT NOT NULL, "
                + AppDBContract.ExpensesEntry.COLUMN_EXPENSE_COST + " DOUBLE NOT NULL " + " );";
        // Create table with six columns for incomes
        String createIncomesTable = "CREATE TABLE "
                + AppDBContract.IncomeEntry.TABLE_NAME + " ("
                + AppDBContract.IncomeEntry.COLUMN_INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AppDBContract.IncomeEntry.COLUMN_INCOME_DATE + " INTEGER NOT NULL, "
                + AppDBContract.IncomeEntry.COLUMN_INCOME_GROUP + " TEXT NOT NULL, "
                + AppDBContract.IncomeEntry.COLUMN_INCOME_NAME + " TEXT NOT NULL, "
                + AppDBContract.IncomeEntry.COLUMN_INCOME_COST + " DOUBLE NOT NULL " + " );";
        // Create table with five columns for income and expense groups
        String createGroupTable = "CREATE TABLE "
                + AppDBContract.GroupsEntry.TABLE_NAME + " ("
                + AppDBContract.GroupsEntry.COLUMN_GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AppDBContract.GroupsEntry.COLUMN_GROUP_NAME + " TEXT NOT NULL, "
                + AppDBContract.GroupsEntry.COLUMN_GROUP_TYPE + " TEXT NOT NULL, "
                + AppDBContract.GroupsEntry.COLUMN_GROUP_PRIORITY + " INTEGER NOT NULL " + " );";

        db.execSQL(createExpensesTable);
        db.execSQL(createIncomesTable);
        db.execSQL(createGroupTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + AppDBContract.ExpensesEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AppDBContract.IncomeEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AppDBContract.GroupsEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AppDBContract.ShopListEntry.TABLE_NAME);
            onCreate(db);
        }
    }

    public void addExpense (Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Expense data, which we insert to database
        contentValues.put(COLUMN_EXPENSE_DATE, expense.getDate());
        contentValues.put(AppDBContract.ExpensesEntry.COLUMN_EXPENSE_GROUP, expense.getGroup());
        contentValues.put(AppDBContract.ExpensesEntry.COLUMN_EXPENSE_NAME, expense.getName());
        contentValues.put(AppDBContract.ExpensesEntry.COLUMN_EXPENSE_COST, expense.getCost());

        //Insert expense data to our database
        db.insert(AppDBContract.ExpensesEntry.TABLE_NAME, null, contentValues);
        db.close();
    }

    public void addShopListItem (ShopItem shopItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Expense data, which we insert to database
        contentValues.put(AppDBContract.ShopListEntry.COLUMN_SHOPLIST_DATE, shopItem.getDate());
        contentValues.put(AppDBContract.ShopListEntry.COLUMN_SHOPLIST_GROUP, shopItem.getGroup());
        contentValues.put(AppDBContract.ShopListEntry.COLUMN_SHOPLIST_NAME, shopItem.getName());
        contentValues.put(AppDBContract.ShopListEntry.COLUMN_SHOPLIST_COST, shopItem.getCost());

        //Insert expense data to our database
        db.insert(AppDBContract.ShopListEntry.TABLE_NAME, null, contentValues);
        db.close();
    }

    public void addIncome (Income income) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Income data, which we insert to database
        contentValues.put(AppDBContract.IncomeEntry.COLUMN_INCOME_DATE, income.getDate());
        contentValues.put(AppDBContract.IncomeEntry.COLUMN_INCOME_GROUP, income.getGroup());
        contentValues.put(AppDBContract.IncomeEntry.COLUMN_INCOME_NAME, income.getName());
        contentValues.put(AppDBContract.IncomeEntry.COLUMN_INCOME_COST, income.getCost());

        //Insert income data to our database
        db.insert(AppDBContract.IncomeEntry.TABLE_NAME, null, contentValues);
        db.close();
    }

    public void addGroup (Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Groups data, which we insert to database
        contentValues.put(AppDBContract.GroupsEntry.COLUMN_GROUP_NAME, group.getGroupName());
        contentValues.put(AppDBContract.GroupsEntry.COLUMN_GROUP_TYPE, group.getGroupType());
        contentValues.put(AppDBContract.GroupsEntry.COLUMN_GROUP_PRIORITY, group.getGroupPriority());


        //Insert groups data to our database
        db.insert(AppDBContract.GroupsEntry.TABLE_NAME, null, contentValues);
        db.close();
    }

    public void updateShopListItem (ShopItem shopItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppDBContract.ShopListEntry.COLUMN_SHOPLIST_CHECKED, shopItem.getChecked());

        db.update(AppDBContract.ShopListEntry.TABLE_NAME, values, AppDBContract.ShopListEntry.COLUMN_SHOPLIST_DATE + " = ?",
                new String[] { String.valueOf(shopItem.getDate()) });
        db.close();
    }

    public List<Expense> getAllExpenseAsList(String tableName) {
        List<Expense> expenseList = new ArrayList<Expense>();

        String query = "SELECT * FROM "
                + tableName
                +" ORDER BY "
                + " date" +" ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense(Long.parseLong(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        Double.parseDouble(cursor.getString(4)));

                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        db.close();
        return expenseList;
    }

    public List<Group> getAllGroupAsList(String tableName) {
        List<Group> groupList = new ArrayList<Group>();

        String query = "SELECT * FROM "
                + tableName
                +" ORDER BY "
                + " _id" +" DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Group group = new Group(cursor.getString(1),
                        cursor.getString(2));

                groupList.add(group);
            } while (cursor.moveToNext());
        }

        db.close();
        return groupList;

    }

    public List<Income> getAllIncomeAsList(String tableName) {
        List<Income> incomesList = new ArrayList<Income>();

        String query = "SELECT * FROM "
                + tableName
                +" ORDER BY "
                + " date" +" ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Income income = new Income(Long.parseLong(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        Double.parseDouble(cursor.getString(4)));

                incomesList.add(income);
            } while (cursor.moveToNext());
        }

        db.close();
        return incomesList;
    }


    public List<Expense> getAllExpenseAsList(String tableName, String columnName, String from, String to) {
        List<Expense> expenseIncomeList = new ArrayList<Expense>();

        String query = "SELECT * FROM " + tableName
                + " WHERE "
                + columnName + " >= " + from + " AND "
                + columnName + " <= " + to
                + " ORDER BY "
                + COLUMN_EXPENSE_DATE + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Expense expenseIncome = new Expense(Long.parseLong(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        Double.parseDouble(cursor.getString(4)));

                expenseIncomeList.add(expenseIncome);
            } while (cursor.moveToNext());
        }

        return expenseIncomeList;
    }

    public ArrayList<String> getAllUsedGroup(String tableName) {
        ArrayList<String> usedGroupName = new ArrayList<String>();

        String groupName;
        int i = 0;

        String query = "SELECT * FROM "
                + tableName
                +" ORDER BY "
                + AppDBContract.ShopListEntry.COLUMN_SHOPLIST_GROUP + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            groupName = cursor.getString(2);
            usedGroupName.add(groupName);
            do {
                if (!usedGroupName.get(i).equals(cursor.getString(2))) {
                    usedGroupName.add(cursor.getString(2));
                    i++;
                }
            } while (cursor.moveToNext());
        }

        db.close();
        return usedGroupName;
    }


    public List<ShopItem> getAllShopItemAsList(String tableName) {
        List<ShopItem> shopItems = new ArrayList<ShopItem>();

        String query = "SELECT * FROM "
                + tableName
                +" ORDER BY "
                + " date" +" ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ShopItem shopItem = new ShopItem(Long.parseLong(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        Double.parseDouble(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(4)));

                shopItems.add(shopItem);
            } while (cursor.moveToNext());
        }

        return shopItems;
    }

    public List<ShopItem> getAllCheckedShopItem(String tableName) {
        List<ShopItem> shopItems = new ArrayList<ShopItem>();

        String query = "SELECT * FROM "
                + tableName
                + " WHERE "
                + AppDBContract.ShopListEntry.COLUMN_SHOPLIST_CHECKED + " = 1 "
                + " ORDER BY "
                + " date" +" ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ShopItem shopItem = new ShopItem(Long.parseLong(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        Double.parseDouble(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(4)));

                shopItems.add(shopItem);
            } while (cursor.moveToNext());
        }

        return shopItems;
    }

    public Cursor getAllGroupCursor(String groupType) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(AppDBContract.GroupsEntry.TABLE_NAME,
                null,
                AppDBContract.GroupsEntry.COLUMN_GROUP_TYPE + " =?",
                new String[] {groupType},
                null,
                null,
                "_id" + " ASC");
    }


    public Cursor getAllCursor(String tableName, String col, String val, long startDate, long endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tableName,
                null,
                AppDBContract.ExpensesEntry.COLUMN_EXPENSE_DATE + ">=? AND " +
                        AppDBContract.ExpensesEntry.COLUMN_EXPENSE_DATE + "<=? AND " +
                        col + " = " + val,
                new String[] {Long.toString(startDate), Long.toString(endDate)},
                null,
                null,
                AppDBContract.ExpensesEntry.COLUMN_EXPENSE_DATE + " DESC");
    }


    public Cursor getAllCursor(String tableName, String col, String val) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tableName,
                null,
                col + " =?",
                new String[] {val},
                null,
                null,
                AppDBContract.ExpensesEntry.COLUMN_EXPENSE_DATE + " DESC");
    }


    public Cursor getAllCursor(long startDate, long endDate, String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tableName,
                null,
                "date" + " >=? AND " + "date" + " <=?",
                new String[] {Long.toString(startDate), Long.toString(endDate)},
                null,
                null,
                "date" + " DESC");
    }

    public Cursor getAllCursor(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tableName,
                null,
                null,
                null,
                null,
                null,
                "date" + " DESC");
    }

    public void delete(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName,
                null,
                null);
    }

    public void delete(String tableName, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, AppDBContract.IncomeEntry.COLUMN_INCOME_ID + " = " + id, null);
    }


    public int getGroupsCount(String groupType) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(AppDBContract.GroupsEntry.TABLE_NAME,
                null,
                AppDBContract.GroupsEntry.COLUMN_GROUP_TYPE + " =?",
                new String[] {groupType},
                null,
                null,
                "_id DESC").getCount();
    }

    public Cursor getGroupById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(AppDBContract.GroupsEntry.TABLE_NAME,
                null,
                AppDBContract.GroupsEntry.COLUMN_GROUP_ID + " =?",
                new String[] {Integer.toString(id)},
                null,
                null,
                null);
    }
}
