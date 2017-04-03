package com.couriertracking.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.couriertracking.app.utility.DbHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent 
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class CourierAlarmReceiver extends WakefulBroadcastReceiver {
    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;

    private Intent mSvcIntent;
  
    @Override
    public void onReceive(Context context, Intent intent) {   
        // BEGIN_INCLUDE(alarm_onreceive)
        /* 
         * If your receiver intent includes extras that need to be passed along to the
         * service, use setComponent() to indicate that the service should handle the
         * receiver's intent. For example:
         * 
         * ComponentName comp = new ComponentName(context.getPackageName(), 
         *      MyService.class.getName());
         *
         * // This intent passed in this call will include the wake lock extra as well as 
         * // the receiver intent contents.
         * startWakefulService(context, (intent.setComponent(comp)));
         * 
         * In this example, we simply create a new intent to deliver to the service.
         * This intent holds an extra identifying the wake lock.
         */

        mSvcIntent = new Intent(context, CourierService.class);
        context.startService(mSvcIntent);

        //Intent service = new Intent(context, SampleSchedulingService.class);
        
        // Start the service, keeping the device awake while it is launching.
        //startWakefulService(context, service);
        // END_INCLUDE(alarm_onreceive)
    }

    // BEGIN_INCLUDE(set_alarm)
    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     * @param context
     */
    public void setAlarm(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CourierAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();

        /*Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm:ss a");
        String localTime = date.format(currentLocalTime);*/

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);




       /* long currentMiliSec = System.currentTimeMillis();
        calendar.setTimeInMillis(currentMiliSec);
        long decade = currentMiliSec / (1000 * 60 * 60 * 24 ) ;*/
        //long min = sec / 60;
       // long hour = min / 60;

        // Set the alarm's trigger time to 13:30 pm.
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        int modmintue = minute + 1;
        calendar.set(Calendar.MINUTE,modmintue);
  
        /* 
         * If you don't have precise time requirements, use an inexact repeating alarm
         * the minimize the drain on the device battery.
         * 
         * The call below specifies the alarm type, the trigger time, the interval at
         * which the alarm is fired, and the alarm's associated PendingIntent.
         * It uses the alarm type RTC_WAKEUP ("Real Time Clock" wake up), which wakes up 
         * the device and triggers the alarm according to the time of the device's clock. 
         * 
         * Alternatively, you can use the alarm type ELAPSED_REALTIME_WAKEUP to trigger 
         * an alarm based on how much time has elapsed since the device was booted. This 
         * is the preferred choice if your alarm is based on elapsed time--for example, if 
         * you simply want your alarm to fire every 60 minutes. You only need to use 
         * RTC_WAKEUP if you want your alarm to fire at a particular date/time. Remember 
         * that clock-based time may not translate well to other locales, and that your 
         * app's behavior could be affected by the user changing the device's time setting.
         * 
         * Here are some examples of ELAPSED_REALTIME_WAKEUP:
         * 
         * // Wake up the device to fire a one-time alarm in one minute.
         * alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
         *         SystemClock.elapsedRealtime() +
         *         60*1000, alarmIntent);
         *        
         * // Wake up the device to fire the alarm in 30 minutes, and every 30 minutes
         * // after that.
         * alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
         *         AlarmManager.INTERVAL_HALF_HOUR, 
         *         AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
         */
        
        // Set the alarm to fire at approximately 8:30 a.m., according to the device's
        // clock, and to repeat once a day.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
                calendar.getTimeInMillis(), 60 * 1000, alarmIntent);
        
        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);           
    }
    // END_INCLUDE(set_alarm)

    /**
     * Cancels the alarm.
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
        if(mSvcIntent != null) {
            context.stopService(mSvcIntent);
        }
        
        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(cancel_alarm)




}
