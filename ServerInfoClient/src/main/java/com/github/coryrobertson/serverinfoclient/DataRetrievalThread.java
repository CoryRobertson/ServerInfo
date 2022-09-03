package com.github.coryrobertson.serverinfoclient;

import com.github.coryrobertson.serverinfo.ServerInfoPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataRetrievalThread extends Thread
{

    public static ObjectInputStream ois = null;
    public static ObjectOutputStream oos = null;

    private String host;

    private final AtomicBoolean running = new AtomicBoolean(false);

    public void disconnect()
    {
        this.running.set(false);
    }

    public DataRetrievalThread(String host)
    {
        this.host = host;
    }

    @Override
    public void run()
    {
        running.set(true);
        //Socket socket;


        try(Socket socket = new Socket(host, 8123))
        {


            while(running.get())
            {
                //            oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                ServerInfoPacket info = (ServerInfoPacket) ois.readObject();
                ServerInfoClientApplication.info = info;
                System.out.println(ServerInfoClientApplication.info.toString());
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
