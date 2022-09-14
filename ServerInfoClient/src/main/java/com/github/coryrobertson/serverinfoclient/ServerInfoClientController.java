package com.github.coryrobertson.serverinfoclient;

import com.github.coryrobertson.serverinfo.ServerInfoPacket;
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
import java.util.Date;
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

    @FXML
    private Rectangle ramUsageIndicator;

    @FXML
    private Label totalRam;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        if(ServerInfoClientApplication.previousSession != null)
        {
            hostName.setText(ServerInfoClientApplication.previousSession.hostname());
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ServerInfoClientApplication.UPDATE_RATE), e -> updateInfo()));
        timeline.setCycleCount(Animation.INDEFINITE); // loop forever
        timeline.play();
    }

    @FXML
    public void disconnectButtonPressed()
    {
        clearLabels();
        ServerInfoClientApplication.dataThread.disconnect();
    }


    @FXML
    public void connectButtonPressed()
    {
        ServerInfoClientApplication.dataThread = new DataRetrievalThread(hostName.getText());
        ServerInfoClientApplication.dataThread.start();
    }

    /**
     * Called when we need to clear out every label on screen for the display
     * Also clears graph indicators!
     */
    public void clearLabels()
    {
        this.info.setText("disconnected...");
        this.systemDate.setText("");
        this.systemCPUUsage.setText("");
        this.totalRam.setText("");
        this.RAMUsage.setText("");
        this.cpuUsageIndicator.setHeight(0);
        this.ramUsageIndicator.setHeight(0);
        this.connectionDisplayShape.setFill(Color.RED);

    }

    /**
     * Called every ServerInfoClientApplication.UPDATE_RATE milis (probably 1000 milis)
     */
    public void updateInfo()
    {

        if(ServerInfoClientApplication.info == null) // if we have no info, we are disconnected, and should just end updating all the info
        {
            clearLabels();
            return;
        }

        if(!ServerInfoClientApplication.dataThread.getRunning())
        {
            clearLabels();
        }
        else
        {
            ServerInfoPacket info = ServerInfoClientApplication.info;

            Date date = info.date();
            double CPULoad = info.CPU_LOAD();
            double totalRAM = info.TOTAL_RAM();
            double RAMUsage = (info.RAM_USAGE() / totalRAM) * 100;

            this.info.setText(info.toString());
            this.connectionDisplayShape.setFill(Color.GREEN);
            this.systemDate.setText(date.toString());
            this.RAMUsage.setText(RAMUsage + "");
            this.systemCPUUsage.setText(CPULoad + "");
            this.cpuUsageIndicator.setHeight(CPULoad);
            this.cpuUsageIndicator.setLayoutY(235 - CPULoad);
            this.ramUsageIndicator.setHeight(RAMUsage);
            this.ramUsageIndicator.setLayoutY(235 - RAMUsage);
            this.totalRam.setText(totalRAM + "");
        }
    }

}