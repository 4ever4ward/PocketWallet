package ua.matvienko_apps.controlyourbudget.classes;

/**
 * Created by alex_ on 27-Sep-16.
 */

public class Expense extends MoneyManager {

    private int priority;

    public Expense(long _date, String _group, String _name, Double _cost) {
        super(_date, _group, _name, _cost);
    }
}
