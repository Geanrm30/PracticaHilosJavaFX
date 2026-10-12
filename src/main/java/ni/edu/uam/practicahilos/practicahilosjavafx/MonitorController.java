package ni.edu.uam.practicahilos.practicahilosjavafx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class MonitorController {

    @FXML private Label lblEstadoClientes;
    @FXML private ProgressBar barraClientes;
    @FXML private Label lblPorcentajeClientes;
    @FXML private Button btnIniciarClientes;
    @FXML private Button btnDetenerClientes;

    @FXML private Label lblEstadoProductos;
    @FXML private ProgressBar barraProductos;
    @FXML private Label lblPorcentajeProductos;
    @FXML private Button btnIniciarProductos;
    @FXML private Button btnDetenerProductos;

    private Thread hiloClientes;
    private Thread hiloProductos;

    @FXML
    private void iniciarClientes() {
        if (hiloClientes != null && hiloClientes.isAlive()) {
            return;
        }
        hiloClientes = crearHilo("Hilo-Clientes", 100, 100,
                lblEstadoClientes, barraClientes, lblPorcentajeClientes,
                btnIniciarClientes, btnDetenerClientes);
        hiloClientes.start();
    }

    @FXML
    private void iniciarProductos() {
        if (hiloProductos != null && hiloProductos.isAlive()) {
            return;
        }
        hiloProductos = crearHilo("Hilo-Productos", 50, 250,
                lblEstadoProductos, barraProductos, lblPorcentajeProductos,
                btnIniciarProductos, btnDetenerProductos);
        hiloProductos.start();
    }

    @FXML
    private void detenerClientes() {
        detener(hiloClientes, btnDetenerClientes, lblEstadoClientes);
    }

    @FXML
    private void detenerProductos() {
        detener(hiloProductos, btnDetenerProductos, lblEstadoProductos);
    }

    /**
     * Prepara la interfaz y crea (sin iniciar) un hilo que procesa
     * "total" registros, esperando "msPorRegistro" por cada uno.
     */
    private Thread crearHilo(String nombre, int total, int msPorRegistro,
                             Label estado, ProgressBar barra, Label porcentaje,
                             Button iniciar, Button detener) {

        barra.setProgress(0);
        porcentaje.setText("0%");
        estado.setText("Procesando registros...");
        iniciar.setDisable(true);
        detener.setDisable(false);

        Runnable tarea = () -> {
            try {
                for (int i = 1; i <= total; i++) {
                    Thread.sleep(msPorRegistro);

                    final int avance = i;
                    Platform.runLater(() -> {
                        barra.setProgress(avance / (double) total);
                        porcentaje.setText((avance * 100 / total) + "%");
                    });
                }
                Platform.runLater(() -> {
                    estado.setText("Proceso finalizado");
                    iniciar.setDisable(false);
                    detener.setDisable(true);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    estado.setText("Proceso detenido");
                    iniciar.setDisable(false);
                    detener.setDisable(true);
                });
            }
        };

        Thread hilo = new Thread(tarea, nombre);
        hilo.setDaemon(true);
        return hilo;
    }

    private void detener(Thread hilo, Button btnDetener, Label estado) {
        if (hilo != null && hilo.isAlive()) {
            btnDetener.setDisable(true);
            estado.setText("Solicitando detención...");
            hilo.interrupt();
        }
    }

    public void detenerAlCerrar() {
        if (hiloClientes != null) hiloClientes.interrupt();
        if (hiloProductos != null) hiloProductos.interrupt();
    }
}
