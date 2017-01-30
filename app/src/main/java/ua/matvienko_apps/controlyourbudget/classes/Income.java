package ua.matvienko_apps.controlyourbudget.classes;

/**
 * Created by alex_ on 27-Sep-16.
 */

public class Income extends MoneyManager {

    public static final int REPEAT_DAILY = 0;
    public static final int REPEAT_WEEKLY = 1;
    public static final int REPEAT_MONTHLY = 2;
    public static final int REPEAT_ANNUALLY = 3;

    private int id;
    private int repeat;

    public Income(long _date, String _group, String _name, Double _cost, int _repeat) {
        super(_date, _group, _name, _cost);
    }

    public Income(long _date, String _group, String _name, Double _cost, int _repeat, int _id) {
        super(_date, _group, _name, _cost);

        this.id = _id;
        this.repeat = _repeat;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getRepeat() {
        return repeat;
    }
}
