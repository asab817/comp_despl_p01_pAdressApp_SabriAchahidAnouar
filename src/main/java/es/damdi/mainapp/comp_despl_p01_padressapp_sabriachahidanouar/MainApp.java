package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// IMPORTS DE TUS CLASES (Ajusta si los paquetes varían ligeramente)
import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model.Person;
import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view.PersonEditDialogController;
import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view.PersonOverviewController;
import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.persistence.JacksonPersonRepository;
import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.persistence.PersonRepository;
import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.settings.AppPreferences;

import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    // --- NUEVO: Repositorio para JSON ---
    private final PersonRepository personRepository = new JacksonPersonRepository();

    // --- NUEVO: Estado del archivo y cambios (dirty flag) ---
    private File personFilePath;
    private boolean dirty = false;

    /**
     * Los datos como una lista observable de Personas.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    /**
     * Constructor: Añade datos de ejemplo.
     */
    public MainApp() {
        // Datos de ejemplo
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }

    /**
     * Devuelve la lista de personas.
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp - Anouar Sabri Achahid");

        // --- NUEVO: Escuchar cambios en la lista para activar el flag 'dirty' ---
        personData.addListener((ListChangeListener<Person>) c -> setDirty(true));

        initRootLayout();
        showPersonOverview();

        // --- NUEVO: Cargar último archivo al arrancar ---
        loadOnStartup();
    }

    /**
     * Inicializa el diseño raíz y conecta el controlador del menú.
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

            // Cargar icono
            this.primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/address_book_32.png")));

            // --- NUEVO: Dar acceso al RootLayoutController a MainApp ---
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la vista de resumen de personas dentro del diseño raíz.
     */
    public void showPersonOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(personOverview);

            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre un diálogo para editar los detalles de la persona especificada.
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/address_book_32.png")));

            Scene scene = new Scene(page);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            dialogStage.setScene(scene);

            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =====================================================================
    //  NUEVOS MÉTODOS DE PERSISTENCIA Y PREFERENCIAS (PARTE 5 - PDF)
    // =====================================================================

    public File getPersonFilePath() {
        return personFilePath;
    }

    /**
     * Establece la ruta del archivo y actualiza las preferencias y el título.
     */
    public void setPersonFilePath(File file) {
        this.personFilePath = file;

        // Guardar en preferencias (si es null, se borra)
        AppPreferences.setPersonFile(file == null ? null : file.getAbsolutePath());

        // Actualizar título de la ventana
        if (primaryStage != null) {
            String name = (file == null) ? "AddressApp" : "AddressApp - " + file.getName();
            primaryStage.setTitle(name);
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        // Opcional: Podrías añadir un asterisco al título si hay cambios
    }

    /**
     * Carga datos de personas desde el archivo JSON especificado.
     */
    public void loadPersonDataFromJson(File file) throws IOException {
        List<Person> loaded = personRepository.load(file);

        // setAll reemplaza el contenido manteniendo la referencia de la lista
        personData.setAll(loaded);

        setPersonFilePath(file);
        setDirty(false); // Recién cargado, no hay cambios pendientes
    }

    /**
     * Guarda los datos actuales en el archivo JSON especificado.
     */
    public void savePersonDataToJson(File file) throws IOException {
        personRepository.save(file, new ArrayList<>(personData));

        setPersonFilePath(file);
        setDirty(false); // Guardado, no hay cambios pendientes
    }

    /**
     * Intenta cargar el último archivo abierto al iniciar la app.
     */
    private void loadOnStartup() {
        AppPreferences.getPersonFile().ifPresent(path -> {
            File f = new File(path);
            if (f.exists()) {
                try {
                    loadPersonDataFromJson(f);
                } catch (IOException e) {
                    e.printStackTrace(); // Podrías mostrar una alerta aquí
                }
            }
        });
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}