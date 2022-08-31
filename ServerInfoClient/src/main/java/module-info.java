module com.github.coryrobertson.serverinfoclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.github.coryrobertson.serverinfoclient to javafx.fxml;
    exports com.github.coryrobertson.serverinfoclient;
}