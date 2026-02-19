package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view.help;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

// 1. QUITAMOS el "extends Application"
public class MarkdownHelp {

    // 2. CAMBIAMOS el método "start" por "show()"
    public void show() {
        // Creamos la ventana (Stage) aquí dentro
        Stage primaryStage = new Stage();

        // Cargar el contenido del archivo Markdown
        String markdownContent = loadMarkdown();

        // Convertir Markdown a HTML utilizando Flexmark
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        Node document = parser.parse(markdownContent);
        String htmlContent = renderer.render(document);

        // Crear un WebView para mostrar el contenido HTML generado
        WebView webView = new WebView();
        webView.getEngine().loadContent(htmlContent);

        // Configurar la escena y mostrar la ventana
        primaryStage.setScene(new Scene(webView, 800, 600));
        primaryStage.setTitle("Ayuda en Markdown");
        primaryStage.show();
    }

    /**
     * Método para cargar el contenido del archivo Markdown desde los recursos del classpath.
     *
     * @return Contenido del archivo Markdown como String, o un mensaje de error si ocurre un problema.
     */
    private String loadMarkdown() {
        // 3. CORREGIMOS LA RUTA (quitamos el "help/" inicial porque ya estamos en la carpeta help)
        try (InputStream inputStream = getClass().getResourceAsStream("markdown/README.md")) {
            if (inputStream == null) {
                System.err.println("⚠️ El archivo Markdown no se encontró dentro del JAR.");
                return "Error: No se pudo encontrar el archivo Markdown.";
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("❌ Error al cargar el archivo Markdown: " + e.getMessage());
            return "Error al cargar el archivo Markdown.";
        }
    }

    // 4. BORRAMOS el método main() porque ya no es necesario
}