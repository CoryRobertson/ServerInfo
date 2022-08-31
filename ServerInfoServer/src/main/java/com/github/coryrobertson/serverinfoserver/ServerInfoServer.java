package com.github.coryrobertson.serverinfoserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerInfoServer {
    private static final int MAX_CLIENTS = 5;
    public static final int UPDATE_RATE = 1000;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    public static void main(String[] args) throws IOException
    {
        int clientCount = 0;

        System.out.println("Waiting on port 8123!");

        while(true)
        {
            try(ServerSocket serverSocket = new ServerSocket(8123))
            {
                Socket clientSocket = serverSocket.accept();
                if(clientCount <= MAX_CLIENTS)
                {
                    System.out.println("Caught client: " + clientCount + ": " + clientSocket.toString());

                    clients.add(new ClientHandler(clientSocket, clientCount));
                    clients.get(clientCount).start();
                    clientCount++;
                }
            }
//            for(ClientHandler client : clients)
//            {
//
//            }
        }
    }
}