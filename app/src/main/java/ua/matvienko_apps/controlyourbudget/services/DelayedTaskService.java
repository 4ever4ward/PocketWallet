package ua.matvienko_apps.controlyourbudget.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.List;

import ua.matvienko_apps.controlyourbudget.classes.Expense;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;

public class DelayedTaskService extends IntentService {

    private static final String TAG = DelayedTaskService.class.getSimpleName();

    private static final int INTERVAL = 1000 * 10; // millis * sec * min

    public DelayedTaskService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        AppDBHelper dbHelper = new AppDBHelper(getApplicationContext(), AppDBContract.DB_NAME, null, AppDBHelper.DB_VERSION);
        List<Expense> repeatedExpensesList = dbHelper.getAllRepeatingExpenseAsList();

        Log.e(TAG, "REPEAT:");

        for (Expense expense : repeatedExpensesList) {
            DateTime now = new DateTime();

            DateTime expenseStartTime = new DateTime(expense.getDate());

            switch (expense.getRepeat()) {

                case Expense.REPEAT_DAILY: {
                    if (now.getHourOfDay() == expenseStartTime.getHourOfDay()
                            && now.getMinuteOfHour() % 10 * 10 == expenseStartTime.getMinuteOfHour() % 10 * 10)
                        Log.e(TAG, "onHandleIntent: ");

                }
            }


//            Log.d(TAG, "onHandleIntent: " + expense.getName());
//
//            if (getNotificationRunTime(expense) != null) {
//
//                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                Intent i = new Intent(this, ExpenseNotification.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
//                        i, PendingIntent.FLAG_CANCEL_CURRENT);
//
//
//                am.cancel(pendingIntent);
//                am.set(AlarmManager.RTC_WAKEUP, getNotificationRunTime(expense).getMillis(), pendingIntent);

//                Notification notification = new NotificationCompat.Builder(this)
//                        .setTicker("New Expense")
//                        .setSmallIcon(R.drawable.ic_expense_group_transport)
//                        .setContentTitle("Title")
//                        .setContentText("Hello World")
//                        .setAutoCancel(true)
//                        .build();
//
//                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                notificationManager.notify(0,notification);


        }
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

    private DateTime getNotificationRunTime(Expense expense) {
        DateTime now = new DateTime();
        DateTime expenseStartTime = new DateTime(expense.getDate());

        switch (expense.getRepeat()) {
            case Expense.REPEAT_DAILY: {
                if (now.getHourOfDay() == expenseStartTime.getHourOfDay()) {

                }

            }
//            case Expense.REPEAT_WEEKLY: {
//                if (now.equals(new DateTime(expenseStartTime.plusWeeks(1))))
//                    return true;
//            }
//            case Expense.REPEAT_MONTHLY: {
//                if (now.equals(new DateTime(expenseStartTime.plusMonths(1))))
//                    return true;
//            }
//            case Expense.REPEAT_ANNUALLY: {
//                if (now.equals(new DateTime(expenseStartTime.plusYears(1))))
//                    return true;
//            }
        }
        return null;
    }

}
