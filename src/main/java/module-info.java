module com.example.hw5 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.hse.homework.fifth to javafx.fxml;
    exports ru.hse.homework.fifth;
    exports ru.hse.homework.fifth.controller;
    opens ru.hse.homework.fifth.controller to javafx.fxml;
}