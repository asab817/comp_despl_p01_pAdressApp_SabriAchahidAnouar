package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view.help;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;

public class HelpViewer extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear un componente WebView para mostrar el contenido HTML
        WebView webView = new WebView();

        // Obtener la URL del archivo HTML dentro de los recursos
        URL url = getClass().getResource("help/html/index.html");

        if (url != null) {
            // Cargar el archivo HTML en el WebView
            webView.getEngine().load(url.toExternalForm());
        } else {
            System.err.println("⚠️ No se encontró el archivo HTML en el classpath.");
            webView.getEngine().loadContent("<html><body><h2>Error: No se pudo cargar la ayuda.</h2></body></html>");
        }

        // Configurar la escena y mostrar la ventana de ayuda
        primaryStage.setScene(new Scene(webView, 800, 600));
        primaryStage.setTitle("Ayuda - Documentación");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
