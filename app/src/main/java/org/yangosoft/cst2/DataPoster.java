package org.yangosoft.cst2;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;

public class DataPoster implements Runnable{

    private volatile boolean do_work = true;
    private Activity activity;
    public DataPoster(Activity act)
    {
        this.activity = act;
    }

    @Override
    public void run() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        while(do_work)
        {
            try {
                //InetAddress serverAddr = InetAddress.getByAddress(192,168,1,137);

                Socket socket = new Socket("192.168.1.37" , 9999);
                // get the output stream from the socket.
                OutputStream outputStream = socket.getOutputStream();
                // create a data output stream from the output stream so we can send data through it

                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                System.out.println("Sending string to the ServerSocket");

                // write the message we want to send
                dataOutputStream.writeUTF("Hello from the other side!");

                /*fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            try {
                                dataOutputStream.writeUTF(location.toString());
                                dataOutputStream.flush(); // send the message
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else
                        {

                        }
                    }
                });
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

    public void stop()
    {
        do_work = false;
    }
}
