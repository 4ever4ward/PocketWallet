package ua.matvienko_apps.controlyourbudget.classes;

/**
 * Created by alex_ on 27-Sep-16.
 */

public abstract class MoneyManager {

    private long date;
    private String group;
    private String name;
    private double cost;


    public MoneyManager(long _date, String _group,
                        String _name, Double _cost) {

        date = _date;
        group = _group;
        name = _name;
        cost = _cost;
    }

    public long getDate() {return date;}
    public String getGroup() {return group;}
    public String getName() {return name;}
    public double getCost() {return cost;}
}
