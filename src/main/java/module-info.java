module com.example.dots_and_boxes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.dots_and_boxes to javafx.fxml;
    exports com.example.dots_and_boxes;
}