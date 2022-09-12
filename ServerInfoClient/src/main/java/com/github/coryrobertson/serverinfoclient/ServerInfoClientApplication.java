package com.github.coryrobertson.serverinfoclient;

import com.github.coryrobertson.serverinfo.ServerInfoPacket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerInfoClientApplication extends Application
{
    public static DataRetrievalThread dataThread;


    public static ServerInfoPacket info;

    public static final int UPDATE_RATE = 1000;


    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerInfoClientApplication.class.getResource("ServerInfoClient-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        stage.setTitle("ServerInfoClient!");
        stage.setScene(scene);
        stage.show();

        //dataThread.disconnect();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if(dataThread != null)
        {
            dataThread.disconnect();
        }
    }

    public static void main(String[] args)
    {
        //dataThread = new DataRetrievalThread();
        //dataThread.start();
        launch();
        //dataThread.disconnect();
    }
}