package ua.matvienko_apps.controlyourbudget.data;

import android.provider.BaseColumns;

public class AppDBContract {
    public static final String DB_NAME = "expensesDB.db";

    public static final class ExpensesEntry implements BaseColumns {

        public static final String TABLE_NAME = "expenses";

        public static final String COLUMN_EXPENSE_ID = "_id";
        public static final String COLUMN_EXPENSE_DATE = "date";
        public static final String COLUMN_EXPENSE_GROUP = "type";
        public static final String COLUMN_EXPENSE_NAME = "name";
        public static final String COLUMN_EXPENSE_COST = "cost";
        public static final String COLUMN_EXPENSE_REPEAT = "repeat";
    }
    public static final class IncomeEntry implements BaseColumns {

        public static final String TABLE_NAME = "incomes";

        public static final String COLUMN_INCOME_ID = "_id";
        public static final String COLUMN_INCOME_DATE = "date";
        public static final String COLUMN_INCOME_GROUP = "type";
        public static final String COLUMN_INCOME_NAME = "name";
        public static final String COLUMN_INCOME_COST = "cost";
    }

    public static final class GroupsEntry implements BaseColumns {

        public static final String TABLE_NAME = "groups";

        public static final String COLUMN_GROUP_ID = "_id";
        public static final String COLUMN_GROUP_NAME = "name";
        public static final String COLUMN_GROUP_TYPE = "type";
        public static final String COLUMN_GROUP_PRIORITY = "priority";

    }

    public static final class ShopListEntry implements BaseColumns {

        public static final String TABLE_NAME = "shopList";

        public static final String COLUMN_SHOPLIST_ID = "_id";
        public static final String COLUMN_SHOPLIST_DATE = "date";
        public static final String COLUMN_SHOPLIST_NAME = "name";
        public static final String COLUMN_SHOPLIST_GROUP = "type";
        public static final String COLUMN_SHOPLIST_COST = "cost";
        public static final String COLUMN_SHOPLIST_CHECKED = "checked";
    }

}
