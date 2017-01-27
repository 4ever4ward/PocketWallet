package ua.matvienko_apps.controlyourbudget.classes;

/**
 * Created by alex_ on 27-Sep-16.
 */

public class Expense extends MoneyManager {

    public static final int REPEAT_DAILY = 0;
    public static final int REPEAT_WEEKLY = 1;
    public static final int REPEAT_MONTHLY = 2;
    public static final int REPEAT_ANNUALLY = 3;

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
