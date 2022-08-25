package org.yangosoft.cst2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class DataPoster implements Runnable{

    private volatile boolean do_work = true;

    @Override
    public void run() {

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
                dataOutputStream.flush(); // send the message
                dataOutputStream.close(); // close the output stream when we're done.
                socket.close();

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void stop()
    {
        do_work = false;
    }
}
