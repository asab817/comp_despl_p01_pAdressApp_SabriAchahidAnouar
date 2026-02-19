package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view.help;

import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import javax.swing.JPanel;
import java.net.URL;

// 1. QUITAMOS el "extends Application"
public class PDFHelpViewer {

    // 2. CREAMOS el método show()
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Ayuda (Manual PDF)");

        // Configurar IcePDF (Visor)
        SwingController controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        JPanel viewerComponentPanel = factory.buildViewerPanel();

        // Incrustar el panel de Swing dentro de JavaFX
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(viewerComponentPanel);
        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);

        stage.setScene(new Scene(pane, 900, 700));
        stage.show(); // Mostrar la ventana

        // 3. Cargar el PDF asegurando la ruta correcta
        URL url = getClass().getResource("pdf/ayuda.pdf");

        // Si la ruta corta falla, probamos con la ruta absoluta
        if (url == null) {
            url = getClass().getResource("/es/damdi/mainapp/comp_despl_p01_padressapp_sabriachahidanouar/view/help/pdf/ayuda.pdf");
        }

        if (url != null) {
            controller.openDocument(url);
        } else {
            System.err.println("⚠️ ERROR: No se encontró el archivo PDF en los recursos.");
            System.err.println("Revisa que exista la carpeta 'pdf' y dentro 'ayuda.pdf'");
        }
    }
}