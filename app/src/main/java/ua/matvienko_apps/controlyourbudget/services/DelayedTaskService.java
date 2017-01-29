package ua.matvienko_apps.controlyourbudget.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.joda.time.DateTime;

import java.util.List;

import ua.matvienko_apps.controlyourbudget.R;
import ua.matvienko_apps.controlyourbudget.Utility;
import ua.matvienko_apps.controlyourbudget.activity.AddActivity;
import ua.matvienko_apps.controlyourbudget.classes.Expense;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;
import ua.matvienko_apps.controlyourbudget.fragments.ExpenseFragment;

public class DelayedTaskService extends IntentService {

    private static final String TAG = DelayedTaskService.class.getSimpleName();

    // Interval for repeat service
    private static final int INTERVAL = 1000 * 60 * 10;// * 10; // millis * sec * min

    public DelayedTaskService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        AppDBHelper dbHelper = new AppDBHelper(getApplicationContext(), AppDBContract.DB_NAME, null, AppDBHelper.DB_VERSION);
        List<Expense> repeatedExpensesList = dbHelper.getAllRepeatingExpenseAsList();

        for (Expense expense : repeatedExpensesList) {
            DateTime now = new DateTime();

//            // You can change this code for test different type of notification
//            now = now.plusDays(1);

            DateTime expenseStartTime = new DateTime(expense.getDate());

            switch (expense.getRepeat()) {

                case Expense.REPEAT_DAILY: {
                    if (!isEqualsDate(now, expenseStartTime)
                            && (now.getHourOfDay() == expenseStartTime.getHourOfDay())
                            && (now.getMinuteOfHour() / 10 * 10 == expenseStartTime.getMinuteOfHour() / 10 * 10)) {

                        showNotification(expense);
                    }
                    break;
                }
                case Expense.REPEAT_WEEKLY: {
                    if (isEqualsDate(now, expenseStartTime.plusDays(7))
                            && (now.getHourOfDay() == expenseStartTime.getHourOfDay())
                            && (now.getMinuteOfHour() / 10 * 10 == expenseStartTime.getMinuteOfHour() / 10 * 10)) {

                        showNotification(expense);
                    }
                    break;
                }
                case Expense.REPEAT_MONTHLY: {
                    if (!isEqualsDate(now, expenseStartTime)
                            && (now.getMonthOfYear() != expenseStartTime.getMonthOfYear()
                            || now.getYear() != expenseStartTime.getYear())
                            && (now.getHourOfDay() == expenseStartTime.getHourOfDay())
                            && (now.getMinuteOfHour() / 10 * 10 == expenseStartTime.getMinuteOfHour() / 10 * 10)) {

                        showNotification(expense);
                    }
                    break;
                }
                case Expense.REPEAT_ANNUALLY: {
                    if (now.getDayOfMonth() == expenseStartTime.getDayOfMonth()
                            && now.getMonthOfYear() == expenseStartTime.getMonthOfYear()
                            && now.getYear() != expenseStartTime.getYear()
                            && (now.getHourOfDay() == expenseStartTime.getHourOfDay())
                            && (now.getMinuteOfHour() / 10 * 10 == expenseStartTime.getMinuteOfHour() / 10 * 10)) {

                        showNotification(expense);
                    }
                    break;
                }

            }
        }
    }

    private void showNotification(Expense expense) {

        /** TODO: set 2 button's for notification
         * 1-st - for add this expense (Open AddFragment, when u're check intent
         * and parse intent data from it, then use this data for fields auto-fill
         * 2-st - for cancel
         */


        int requestID = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("ExpenseID", expense.getId());
        intent.putExtra("FragmentName", ExpenseFragment.class.getSimpleName());
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, 0);


        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(getString(R.string.app_name) + ": " + "Expense notification")
                .setSmallIcon(R.drawable.ic_expense)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(expense.getName() + "   " + Utility.formatMoney((float) expense.getCost()))
                .setContentIntent(pIntent)
                .setAutoCancel(false)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(expense.getId(), notification);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent intent = new Intent(context, DelayedTaskService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }


    private boolean isEqualsDate(DateTime firstDate, DateTime secondDate) {

        return firstDate.getYear() == secondDate.getYear()
                && firstDate.getMonthOfYear() == secondDate.getMonthOfYear()
                && firstDate.getDayOfMonth() == secondDate.getDayOfMonth();
    }


}
