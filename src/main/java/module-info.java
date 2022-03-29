module com.example.hw5 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hw5 to javafx.fxml;
    exports com.example.hw5;
}