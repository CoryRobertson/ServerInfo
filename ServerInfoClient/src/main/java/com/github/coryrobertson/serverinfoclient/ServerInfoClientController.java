package com.github.coryrobertson.serverinfoclient;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


import java.net.URL;
import java.util.ResourceBundle;

public class ServerInfoClientController implements Initializable
{

    @FXML
    private Label info;

    @FXML
    private Label systemDate;

    @FXML
    private Label systemCPUUsage;

    @FXML
    private Label RAMUsage;

    @FXML
    private TextField hostName;

    @FXML
    private Rectangle connectionDisplayShape;

    @FXML
    private Rectangle cpuUsageIndicator;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ServerInfoClientApplication.UPDATE_RATE), e -> updateInfo()));
        timeline.setCycleCount(Animation.INDEFINITE); // loop forever
        timeline.play();
    }


    @FXML
    public void connectButtonPressed()
    {
        ServerInfoClientApplication.dataThread = new DataRetrievalThread(hostName.getText());
        ServerInfoClientApplication.dataThread.start();
    }

    /**
     * Called every ServerInfoClientApplication.UPDATE_RATE milis (probably 1000 milis)
     */
    public void updateInfo()
    {
        if(ServerInfoClientApplication.info == null)
        {
            this.info.setText("disconnected...");
            this.connectionDisplayShape.setFill(Color.RED);
        }
        else
        {
            double cpuLoad = ServerInfoClientApplication.info.CPU_LOAD();
            this.info.setText(ServerInfoClientApplication.info.toString());
            this.connectionDisplayShape.setFill(Color.GREEN);
            this.systemDate.setText(ServerInfoClientApplication.info.date().toString());
            this.RAMUsage.setText(ServerInfoClientApplication.info.RAM_USAGE() + "");
            this.systemCPUUsage.setText(cpuLoad + "");
            this.cpuUsageIndicator.setHeight(cpuLoad);
            this.cpuUsageIndicator.setLayoutY(235 - cpuLoad);
        }
    }




}