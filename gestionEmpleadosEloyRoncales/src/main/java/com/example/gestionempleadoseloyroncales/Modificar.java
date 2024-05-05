package com.example.gestionempleadoseloyroncales;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class Modificar {
    @FXML
    private TextField modificarID;
    @FXML
    private TextField modificarNombre;
    @FXML
    private TextField modificarPuesto;
    @FXML
    private TextField modificarSalario;
    @FXML
    private Button botonCancelar;
    @FXML
    private Button botonEditar;

    private Trabajador trabajadorSeleccionado;
    private Stage stage;

    public void init(Trabajador trabajador) {
        this.trabajadorSeleccionado = trabajador;
        this.stage = stage;

        // Mostrar los datos del trabajador en los campos de texto
        modificarID.setText(String.valueOf(trabajador.getId()));
        modificarNombre.setText(trabajador.getNombre());
        modificarPuesto.setText(trabajador.getPuesto());
        modificarSalario.setText(String.valueOf(trabajador.getSalario()));
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) botonCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void editar() {
        if (trabajadorSeleccionado != null) {
            String nuevoNombre = modificarNombre.getText();
            String nuevoPuesto = modificarPuesto.getText();
            int nuevoSalario = Integer.parseInt(modificarSalario.getText());
            trabajadorSeleccionado.setNombre(nuevoNombre);
            trabajadorSeleccionado.setPuesto(nuevoPuesto);
            trabajadorSeleccionado.setSalario(nuevoSalario);
            Stage stage = (Stage) botonEditar.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("No se ha seleccionado ningún trabajador para editar.");
        }
    }

}

