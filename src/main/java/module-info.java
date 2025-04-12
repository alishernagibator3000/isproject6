module com.example.isproject2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.isproject2 to javafx.fxml;
    exports com.example.isproject2;
}