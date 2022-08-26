package org.yangosoft.cst2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DataPoster implements Runnable, OnSuccessListener<Location> {

    private volatile boolean do_work = true;
    private Location location;
    private volatile boolean locationFound = false;
    private final Activity activity;

    public DataPoster(Activity act) {
        this.activity = act;
    }

    @Override
    public void run() {

        while (do_work) {
            try {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
                //InetAddress serverAddr = InetAddress.getByAddress(192,168,1,137);
                locationFound = false;
                Socket socket = new Socket("192.168.1.37", 9999);
                // get the output stream from the socket.
                OutputStream outputStream = socket.getOutputStream();
                // create a data output stream from the output stream so we can send data through it

                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                System.out.println("Sending string to the ServerSocket");

                // write the message we want to send
                dataOutputStream.writeUTF("Hello from the other side!");


                int totalRetries = 10;
                location = null;
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationClient.getLastLocation().addOnSuccessListener(activity, this);
                try {
                    while ((totalRetries > 0) && (locationFound == false)) {
                        dataOutputStream.writeUTF("Stll waiting " + totalRetries + "\n");
                        dataOutputStream.flush();
                        Thread.sleep(1000);
                        totalRetries--;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if ((locationFound) &&(location != null)) {
                    dataOutputStream.writeUTF("Location found!\n");
                    dataOutputStream.writeUTF(location.toString());
                    dataOutputStream.flush(); // send the message
                }
                dataOutputStream.flush(); // send the message
                dataOutputStream.close(); // close the output stream when we're done.
                socket.close();


            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


        }

    }

    public void stop() {
        do_work = false;
    }

    @Override
    public void onSuccess(Location location) {
        // Got last known location. In some rare situations this can be null.
        if (location != null) {
            // Logic to handle location object

            this.location = location;

            locationFound = true;

        } else {


            locationFound = true;
        }
    }


}
