module com.example.gestionempleadoseloyroncales {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.gestionempleadoseloyroncales to javafx.fxml;
    exports com.example.gestionempleadoseloyroncales;
}