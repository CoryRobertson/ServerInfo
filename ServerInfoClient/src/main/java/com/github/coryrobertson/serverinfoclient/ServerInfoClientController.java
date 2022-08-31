package com.github.coryrobertson.serverinfoclient;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;


import java.net.URL;
import java.util.ResourceBundle;

public class ServerInfoClientController implements Initializable
{

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ServerInfoClientApplication.UPDATE_RATE), e -> updateInfo()));
        timeline.setCycleCount(Animation.INDEFINITE); // loop forever
        timeline.play();
    }

    /**
     * Called every ServerInfoClientApplication.UPDATE_RATE milis (probably 1000 milis)
     */
    public void updateInfo()
    {
        this.info.setText(ServerInfoClientApplication.info.toString());

    }

    @FXML
    private Label info;


}