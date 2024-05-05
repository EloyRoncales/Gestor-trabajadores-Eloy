package com.example.gestionempleadoseloyroncales;

import com.example.gestionempleadoseloyroncales.Trabajador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class HelloController  {
    @FXML
    private Label welcomeText;

    @FXML private TextField txtFieldNombre;

    @FXML private TextField FieldtextSalario;

    @FXML private Button ButtonnAnyadirEmpleado;

    @FXML private Label ConsultarID;

    @FXML private Label ConsultarNombre;

    @FXML private Label ConsultarPuesto;

    @FXML private Label ConsultarSueldo;

    @FXML private Label ConsultarFecha;

    @FXML private Button ButtonRefrescar;

    @FXML private static Button buttonModificar;

    @FXML private Button buttonEliminar;

    @FXML private ListView<String> listViewTrabajadores;

    @FXML private ComboBox Puesto;

    private  ArrayList<Trabajador> ListaTrabajadores=new ArrayList<>();



    @FXML
    public Trabajador crearTrabajador() {
        String nombre = txtFieldNombre.getText();
        int salario = Integer.parseInt(FieldtextSalario.getText());
        String puesto = (String)Puesto.getValue();
        System.out.println("Nombre: " + nombre);
        System.out.println("Puesto: " + puesto);
        System.out.println("Salario: " + salario);
        return new Trabajador(nombre, puesto, salario);
    }

    public void anyadirEmpleados(Trabajador t){
        ListaTrabajadores.add(t);
    }
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        ButtonType botonAceptar = new ButtonType("Aceptar");
        alerta.getButtonTypes().setAll(botonAceptar);
        alerta.setOnCloseRequest(event -> {
            txtFieldNombre.clear();
            FieldtextSalario.clear();
            Puesto.setValue(null);

        });
        alerta.showAndWait();
    }


    @FXML
    public void clickAnyadirEmpleados() {
        if (txtFieldNombre.getText().isEmpty() || FieldtextSalario.getText().isEmpty() || Puesto.getValue() == null) {
            System.out.println("Todos los campos deben estar rellenados.");
            return;
        }
        Trabajador nuevoTrabajador = crearTrabajador();
        nuevoTrabajador.setFechaAlta(LocalDate.now());
        anyadirEmpleados(nuevoTrabajador);
        ConexionSQL.insertarTrabajador(nuevoTrabajador);
        mostrarAlerta("Trabajador creado correctamente. Presiona 'Aceptar' para volver a la pantalla principal.");
    }
    public void anyadirTrabajador() {
        InputStream inputStream = getClass().getResourceAsStream("/trabajadores.txt");
        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String lineaTrabajador;
                while ((lineaTrabajador = reader.readLine()) != null) {
                    Trabajador trabajador = parsearLinea(lineaTrabajador);
                    if (trabajador != null) {
                        ListaTrabajadores.add(trabajador);
                        System.out.println("Trabajador cargado: " + trabajador.getNombre());
                    } else {
                        System.out.println("Error al parsear línea: " + lineaTrabajador);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al cargar trabajadores: " + e.getMessage());
            }
        } else {
            System.err.println("No se encontró el archivo de trabajadores.");
        }
    }

    public Trabajador parsearLinea(String trabajadorLinea) {
        System.out.println("Línea recibida: " + trabajadorLinea);
        String[] tokens = trabajadorLinea.split(";");
        System.out.println("Número de tokens: " + tokens.length);
        try {
            if (tokens.length >= 3) { // Verificar si hay al menos tres tokens
                String nombre = tokens[0];
                String cargo = tokens[1];
                int salario = Integer.parseInt(tokens[2]); // Convertir salario a entero
                return new Trabajador(nombre, cargo, salario);

            } else {
                System.err.println("Error al parsear línea: No hay suficientes elementos");
                return null;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear línea: Salario no válido");
            return null;
        }
    }
    @FXML
    public void clickrefrescar(){
        listViewTrabajadores.getItems().clear();
        anyadirTrabajador();
        for (Trabajador t : ListaTrabajadores) {
            listViewTrabajadores.getItems().add(t.getNombre());
            System.out.println(t.getNombre()+" Ha sido añadido a listViewTrabajadores");

        }
    }
    @FXML
    public void initialize() {
        listViewTrabajadores.setOnMouseClicked(event -> {
            int indiceSeleccionado = listViewTrabajadores.getSelectionModel().getSelectedIndex();
            if (indiceSeleccionado >= 0) {
                Trabajador trabajadorSeleccionado = ListaTrabajadores.get(indiceSeleccionado);
                ConsultarID.setText(String.valueOf(trabajadorSeleccionado.getId()));
                ConsultarNombre.setText(trabajadorSeleccionado.getNombre());
                ConsultarPuesto.setText(trabajadorSeleccionado.getPuesto());
                ConsultarSueldo.setText(String.valueOf(trabajadorSeleccionado.getSalario()));
                LocalDate fechaAlta = trabajadorSeleccionado.getFechaAlta();
                if (fechaAlta != null) {
                    ConsultarFecha.setText(fechaAlta.toString());
                } else {
                    ConsultarFecha.setText("Fecha no disponible");
                }
            }
        });
    }
    public void eliminarTrabajador(){
        buttonEliminar.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro de que quieres eliminar este trabajador?");
            ButtonType buttonTypeSi = new ButtonType("Sí");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeSi, buttonTypeNo);
            Optional<ButtonType> resultado = alert.showAndWait();
            if (resultado.isPresent() && resultado.get() == buttonTypeSi) {
                confirmarEliminacion();
            }
        });
    }
    private void confirmarEliminacion() {
        int indiceSeleccionado = listViewTrabajadores.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado >= 0) {
            Trabajador trabajadorSeleccionado = ListaTrabajadores.get(indiceSeleccionado);
            ListaTrabajadores.remove(trabajadorSeleccionado);
            ConsultarID.setText("");
            ConsultarNombre.setText("");
            ConsultarPuesto.setText("");
            ConsultarSueldo.setText("");
            ConsultarFecha.setText("");
            listViewTrabajadores.getItems().remove(indiceSeleccionado);
            System.out.println("El trabajador ha sido eliminado correctamente.");
        } else {
            System.out.println("No se ha seleccionado ningún trabajador para eliminar.");
        }
    }

    @FXML
    private void modificarButton() {
        String nombreSeleccionado = listViewTrabajadores.getSelectionModel().getSelectedItem();
        Trabajador trabajadorSeleccionado = null;
        for (Trabajador trabajador : ListaTrabajadores) {
            if (trabajador.getNombre().equals(nombreSeleccionado)) {
                trabajadorSeleccionado = trabajador;
                break;
            }
        }
        if (trabajadorSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("modificar.fxml"));
                Parent root = loader.load();

                Modificar modificarController = loader.getController();
                modificarController.init(trabajadorSeleccionado);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se ha seleccionado ningún trabajador.");
        }
    }

}


