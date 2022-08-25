package org.yangosoft.cst2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class ConnectorSrv extends Service {

        public static final String CHANNEL_ID = "ForegroundServiceChannel";
        @Override
        public void onCreate() {
            super.onCreate();
        }
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            String input = intent.getStringExtra("inputExtra");
            createNotificationChannel();
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("CST Service")
                    .setContentText(input)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
            //do heavy work on a background thread
            Toast.makeText(this,"STARTED",Toast.LENGTH_LONG);
            try {
                DataPoster poster = new DataPoster(MainActivity.instance);

                Thread t = new Thread(poster);

                t.start();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            //stopSelf();
            return START_NOT_STICKY;
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
        private void createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel serviceChannel = new NotificationChannel(
                        CHANNEL_ID,
                        "Foreground Service Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
        }

}