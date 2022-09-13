package com.github.coryrobertson.serverinfoserver;

import com.github.coryrobertson.Logger.LogLevels;
import com.github.coryrobertson.Logger.Logger;
import com.github.coryrobertson.serverinfo.ServerInfoPacket;

import javax.management.*;
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
    private final AtomicBoolean serving = new AtomicBoolean(true);

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

    public boolean isConnected()
    {
        return serving.get();
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
                oos.writeObject(new ServerInfoPacket(new Date(), ServerInfoServer.getProcessCpuLoad(), ServerInfoServer.getRamUsage(),ServerInfoServer.getTotalRam(), ServerInfoServer.getTotalSpace(), ServerInfoServer.getFreeSpace()));
                Thread.sleep(ServerInfoServer.UPDATE_RATE);
            }
            catch (IOException e)
            {
                Logger.log("Client disconnected: " + this.id + ": " + this.clientSocket.toString(), LogLevels.LOG);
                disconnect();
            }
            catch (IllegalArgumentException | InterruptedException | ReflectionException | MalformedObjectNameException | InstanceNotFoundException | AttributeNotFoundException | MBeanException e)
            {
                Logger.log(e.toString(), LogLevels.ERROR);
                throw new RuntimeException(e);
            }
        }
    }
}
