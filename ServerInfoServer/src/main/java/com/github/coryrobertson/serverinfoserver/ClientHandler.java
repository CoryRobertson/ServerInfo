package com.github.coryrobertson.serverinfoserver;

import com.github.coryrobertson.serverinfo.ServerInfoPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler extends Thread
{
    private int id;
    private Socket clientSocket;
    private final AtomicBoolean serving = new AtomicBoolean(false);

    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    public ClientHandler(Socket clientSocket, int id)
    {
        this.clientSocket = clientSocket;
        this.id = id;

    }

    public void disconnect()
    {
        this.serving.set(false);
    }

    @Override
    public void run()
    {
        this.serving.set(true);

        while(serving.get())
        {
            try
            {
                //ois = new ObjectInputStream(clientSocket.getInputStream());
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.writeObject(new ServerInfoPacket(new Date()));
                Thread.sleep(ServerInfoServer.UPDATE_RATE);
            }
            catch (IOException e)
            {
                System.out.println("Client disconnected: " + this.id + ": " + this.clientSocket.toString());
                disconnect();
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }


        }
    }
}
