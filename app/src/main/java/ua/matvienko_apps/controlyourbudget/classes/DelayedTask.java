package ua.matvienko_apps.controlyourbudget.classes;

/**
 * Created by Alexandr on 25/01/2017.
 */

public class DelayedTask extends MoneyManager {

    int repeat;

    public DelayedTask(long _date, String _group, String _name, Double _cost, int _repeat) {
        super(_date, _group, _name, _cost);

        this.repeat = _repeat;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
