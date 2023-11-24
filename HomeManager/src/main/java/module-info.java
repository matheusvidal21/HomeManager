module br.com.homemanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens br.com.homemanager to javafx.fxml;
    exports br.com.homemanager;
}