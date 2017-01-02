package ua.matvienko_apps.controlyourbudget.classes;

/**
 * Created by alex_ on 27-Sep-16.
 */

public class ShopItem extends MoneyManager {

    private int checked;

    public ShopItem(long _date, String _group, String _name, Double _cost, int checked) {
        super(_date, _group, _name, _cost);

        this.checked = checked;
    }

    public int getChecked() {
        return checked;
    }

}
