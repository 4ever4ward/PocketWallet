package ua.matvienko_apps.controlyourbudget.classes;

/**
 * Created by alex_ on 27-Sep-16.
 */

public class Expense extends MoneyManager {

    private int priority;

    private int repeat;

    public Expense(long _date, String _group, String _name, Double _cost, int _repeat) {
        super(_date, _group, _name, _cost);

        this.repeat = _repeat;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
