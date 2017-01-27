package ua.matvienko_apps.controlyourbudget.classes;

/**
 * Created by alex_ on 15-Sep-16.
 */

public class Group {

    public static final int LOW_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 2;
    public static final int HIGH_PRIORITY = 3;

    private String groupName;
    private String groupType;
    private float groupPrice;
    private int groupPriority;

    public Group(String groupName, float groupPrice, int groupPriority) {
        this.groupName = groupName;
        this.groupPrice = groupPrice;
        this.groupPriority = groupPriority;
    }

    public  Group(String groupName, String groupType) {
        this.groupName = groupName;
        this.groupType = groupType;
    }

    public Group(String groupName, String groupType, int groupPriority) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupPriority = groupPriority;
    }

    public Group(String groupName, String groupType, float groupPrice, int groupPriority) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupPrice = groupPrice;
        this.groupPriority = groupPriority;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public void setGroupPrice(float groupPrice) {
        this.groupPrice = groupPrice;
    }

    public void setGroupPriority(int groupPriority) {
        this.groupPriority = groupPriority;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public float getGroupPrice() {
        return groupPrice;
    }

    public int getGroupPriority() {
        return groupPriority;
    }
}
