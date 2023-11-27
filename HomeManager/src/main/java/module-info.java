module br.com.homemanager {
    requires javafx.controls;
    requires javafx.fxml;

    opens br.com.homemanager to javafx.fxml;
    opens br.com.homemanager.controller to javafx.fxml;
    opens br.com.homemanager.application to javafx.fxml;
    exports br.com.homemanager.controller;
    exports br.com.homemanager.application;
    exports br.com.homemanager.event;
    opens br.com.homemanager.event to javafx.fxml;
}
