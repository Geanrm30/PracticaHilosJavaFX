package ni.edu.uam.practicahilos.practicahilosjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MonitorApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                MonitorApp.class.getResource("monitor-view.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("Reto final - Monitor de procesos");
        stage.setScene(scene);
        stage.show();

        MonitorController controller = loader.getController();
        stage.setOnCloseRequest(event -> controller.detenerAlCerrar());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
