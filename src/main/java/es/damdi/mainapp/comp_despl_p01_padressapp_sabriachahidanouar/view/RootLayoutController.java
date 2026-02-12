package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.MainApp;

import java.io.IOException;
import java.util.Optional;

/**
 * Controlador para el menú superior (File, Edit, Help).
 * Gestiona la apertura, guardado y cierre de la aplicación controlando cambios sin guardar.
 */
public class RootLayoutController {

    // Referencia a la aplicación principal
    private MainApp mainApp;

    /**
     * Es llamado por la aplicación principal para tener una referencia de vuelta.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Crea una libreta de direcciones vacía.
     */
    @FXML
    private void handleNew() {
        // Verifica si hay cambios pendientes antes de limpiar
        if (!confirmSaveIfDirty("New", "Create a new address book?")) {
            return; // Cancelado por el usuario
        }

        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null);
        mainApp.setDirty(false); // Resetear flag
    }

    /**
     * Abre un FileChooser para seleccionar una libreta de direcciones JSON.
     */
    @FXML
    private void handleOpen() {
        if (!confirmSaveIfDirty("Open", "Open another address book?")) {
            return;
        }

        FileChooser fileChooser = createJsonFileChooser("Open Address Book (JSON)");
        setInitialDirectory(fileChooser);

        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            try {
                mainApp.loadPersonDataFromJson(file);
            } catch (IOException e) {
                showError("Could not load data", "Could not load data from file:\n" + file.getPath(), e);
            }
        }
    }

    /**
     * Guarda el archivo en la persona abierta actualmente.
     * Si no hay archivo abierto, se muestra el diálogo "guardar como".
     */
    @FXML
    private void handleSave() {
        saveOrSaveAs();
    }

    /**
     * Abre un FileChooser para dejar al usuario seleccionar un archivo para guardar.
     */
    @FXML
    private void handleSaveAs() {
        saveAs();
    }

    /**
     * Abre un diálogo de "Acerca de".
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("AddressApp");
        alert.setHeaderText("About");
        alert.setContentText("Author: Anouar Sabri Achahid\nWebsite: http://damdi.es");
        alert.showAndWait();
    }

    /**
     * Cierra la aplicación.
     */
    @FXML
    private void handleExit() {
        if (!confirmSaveIfDirty("Exit", "Exit application?")) {
            return;
        }
        System.exit(0);
    }

    // ========================================================================
    // MÉTODOS AUXILIARES (LÓGICA DEL PDF)
    // ========================================================================

    /**
     * Intenta guardar. Si no hay archivo, llama a Save As.
     * @return true si se guardó correctamente.
     */
    private boolean saveOrSaveAs() {
        File file = mainApp.getPersonFilePath();
        if (file != null) {
            try {
                mainApp.savePersonDataToJson(file);
                return true;
            } catch (IOException e) {
                showError("Could not save data", "Could not save data to file:\n" + file.getPath(), e);
                return false;
            }
        } else {
            return saveAs();
        }
    }

    /**
     * Lógica de Guardar Como.
     */
    private boolean saveAs() {
        FileChooser fileChooser = createJsonFileChooser("Save Address Book (JSON)");
        setInitialDirectory(fileChooser);

        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Asegurar extensión .json
            if (!file.getPath().toLowerCase().endsWith(".json")) {
                file = new File(file.getPath() + ".json");
            }
            try {
                mainApp.savePersonDataToJson(file);
                return true;
            } catch (IOException e) {
                showError("Could not save data", "Could not save data to file:\n" + file.getPath(), e);
                return false;
            }
        }
        return false; // Usuario canceló
    }

    /**
     * Muestra una alerta si hay cambios sin guardar (dirty).
     * Opciones: Save, Don't Save, Cancel.
     * @return true si se puede continuar, false si se cancela.
     */
    private boolean confirmSaveIfDirty(String title, String header) {
        if (!mainApp.isDirty()) {
            return true; // No hay cambios, continuar
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText("You have unsaved changes. What do you want to do?");

        ButtonType buttonTypeSave = new ButtonType("Save");
        ButtonType buttonTypeDontSave = new ButtonType("Don't Save");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeDontSave, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == buttonTypeSave) {
                return saveOrSaveAs(); // Intentar guardar
            } else if (result.get() == buttonTypeDontSave) {
                return true; // Continuar sin guardar
            }
        }
        return false; // Cancelar
    }

    /**
     * Configura el FileChooser para archivos JSON.
     */
    private FileChooser createJsonFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        return fileChooser;
    }

    /**
     * Establece el directorio inicial del FileChooser (último usado o home).
     */
    private void setInitialDirectory(FileChooser fileChooser) {
        File currentFile = mainApp.getPersonFilePath();
        if (currentFile != null && currentFile.getParentFile() != null && currentFile.getParentFile().exists()) {
            fileChooser.setInitialDirectory(currentFile.getParentFile());
        } else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }
    }

    private void showError(String header, String content, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content + "\n\n" + e.getMessage());
        alert.showAndWait();
    }
}