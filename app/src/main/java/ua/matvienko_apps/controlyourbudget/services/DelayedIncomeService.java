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
import ua.matvienko_apps.controlyourbudget.classes.Income;
import ua.matvienko_apps.controlyourbudget.data.AppDBContract;
import ua.matvienko_apps.controlyourbudget.data.AppDBHelper;
import ua.matvienko_apps.controlyourbudget.fragments.IncomeFragment;

public class DelayedIncomeService extends IntentService {

    private static final String TAG = DelayedExpenseService.class.getSimpleName();

    // Interval for repeat service
    private static final int INTERVAL = 1000 * 60 * 10;// * 10; // millis * sec * min

    public DelayedIncomeService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        AppDBHelper dbHelper = new AppDBHelper(getApplicationContext(), AppDBContract.DB_NAME, null, AppDBHelper.DB_VERSION);
        List<Income> repeatedIncomesList = dbHelper.getAllRepeatingIncomeAsList();

        for (Income income : repeatedIncomesList) {
            DateTime now = new DateTime();

            // You can change this code for test different type of notification
//            now = now.plusDays(1);

            DateTime incomeStartTime = new DateTime(income.getDate());

            switch (income.getRepeat()) {

                case Income.REPEAT_DAILY: {
                    if (!isEqualsDate(now, incomeStartTime)
                            && (now.getHourOfDay() == incomeStartTime.getHourOfDay())
                            && (now.getMinuteOfHour() / 10 * 10 == incomeStartTime.getMinuteOfHour() / 10 * 10)) {

                        showNotification(income);
                    }
                    break;
                }
                case Income.REPEAT_WEEKLY: {
                    if (isEqualsDate(now, incomeStartTime.plusDays(7))
                            && (now.getHourOfDay() == incomeStartTime.getHourOfDay())
                            && (now.getMinuteOfHour() / 10 * 10 == incomeStartTime.getMinuteOfHour() / 10 * 10)) {

                        showNotification(income);
                    }
                    break;
                }
                case Income.REPEAT_MONTHLY: {
                    if (!isEqualsDate(now, incomeStartTime)
                            && (now.getMonthOfYear() != incomeStartTime.getMonthOfYear()
                            || now.getYear() != incomeStartTime.getYear())
                            && (now.getHourOfDay() == incomeStartTime.getHourOfDay())
                            && (now.getMinuteOfHour() / 10 * 10 == incomeStartTime.getMinuteOfHour() / 10 * 10)) {

                        showNotification(income);
                    }
                    break;
                }
                case Income.REPEAT_ANNUALLY: {
                    if (now.getDayOfMonth() == incomeStartTime.getDayOfMonth()
                            && now.getMonthOfYear() == incomeStartTime.getMonthOfYear()
                            && now.getYear() != incomeStartTime.getYear()
                            && (now.getHourOfDay() == incomeStartTime.getHourOfDay())
                            && (now.getMinuteOfHour() / 10 * 10 == incomeStartTime.getMinuteOfHour() / 10 * 10)) {

                        showNotification(income);
                    }
                    break;
                }

            }
        }
    }

    private void showNotification(Income income) {

        /** TODO: set 2 button's for notification
         * 1-st - for add this income (Open AddFragment, when u're check intent
         * and parse intent data from it, then use this data for fields auto-fill
         * 2-st - for cancel
         */
        int requestID = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("IncomeID", income.getId());
        intent.putExtra("FragmentName", IncomeFragment.class.getSimpleName());
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, 0);


        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(getString(R.string.app_name) + ": " + "Income notification")
                .setSmallIcon(R.drawable.ic_income)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(income.getName() + "   " + Utility.formatMoney((float) income.getCost()))
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(income.getId(), notification);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent intent = new Intent(context, DelayedIncomeService.class);
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
