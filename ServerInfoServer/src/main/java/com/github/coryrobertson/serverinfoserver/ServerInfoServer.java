package com.github.coryrobertson.serverinfoserver;

import com.github.coryrobertson.Logger.LogLevels;
import com.github.coryrobertson.Logger.Logger;

import javax.management.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class ServerInfoServer {
    private static final int MAX_CLIENTS = 5;
    public static final int UPDATE_RATE = 1000;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        int clientCount = 0;
        Logger.setLogFileName("log.txt");
        Logger.setLevel(LogLevels.LOG);

        Logger.log("Waiting on port 8123!",LogLevels.LOG);

        while(true)
        {
            Logger.log("Client count: " + clientCount,LogLevels.LOG);

            try(ServerSocket serverSocket = new ServerSocket(8123))
            {
                serverSocket.setSoTimeout(3000); // recheck with a timeout of 3 secs
                try
                {
                    Socket clientSocket = serverSocket.accept();

                    if (clientCount < MAX_CLIENTS)
                    {
                        Logger.log("Caught client: " + clientCount + ": " + clientSocket.toString(),LogLevels.LOG);

                        clients.add(new ClientHandler(clientSocket, clientCount));
                        clients.get(clientCount).start();
                        clientCount++;
                    }
                    else
                    {
                        Logger.log("Client tried to connect but it would exceed the maximum allowed number of clients: " + clientSocket.toString(), LogLevels.WARN);
                        clientSocket.close();
                    }
                }
                catch (SocketTimeoutException ignored)
                {

                }
            }

            for (int i = 0; i < clients.size(); i++)
            {

                ClientHandler client = clients.get(i);

                if (!client.isConnected()) {
                    clients.remove(i);
                    clientCount--;
                }
            }
        }
    }

    public static double getProcessCpuLoad() throws MalformedObjectNameException, ReflectionException, InstanceNotFoundException {

        MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
        ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
//        AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });
        AttributeList list = mbs.getAttributes(name, new String[]{ "SystemCpuLoad" });

        if (list.isEmpty())     return Double.NaN;

        Attribute att = (Attribute)list.get(0);
        Double value  = (Double)att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0)      return Double.NaN;
        // returns a percentage value with 1 decimal point precision
        return ((int)(value * 1000) / 10.0);
    }

    public static long getRamUsage() throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, InstanceNotFoundException, MBeanException {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Object attribute = mBeanServer.getAttribute(new ObjectName("java.lang","type","OperatingSystem"), "TotalPhysicalMemorySize");
        Object attribute2 = mBeanServer.getAttribute(new ObjectName("java.lang","type","OperatingSystem"), "FreePhysicalMemorySize");
        long total = Long.parseLong(attribute.toString()) / 1024;
        long free = Long.parseLong(attribute2.toString()) / 1024;
        return total - free;
    }

    public static long getTotalRam() throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, InstanceNotFoundException, MBeanException {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Object attribute = mBeanServer.getAttribute(new ObjectName("java.lang","type","OperatingSystem"), "TotalPhysicalMemorySize");
        return Long.parseLong(attribute.toString()) / 1024;
    }

    public static double getTotalSpace()
    {
        File root = new File("/");
        return (double)root.getTotalSpace() / 1073741824;
    }

    public static double getFreeSpace()
    {
        File root = new File("/");
        return  (double)root.getFreeSpace() / 1073741824;
    }

    public static double getUsableSpace()
    {
        File root = new File("/");
        return (double)root.getUsableSpace() / 1073741824;
    }

}