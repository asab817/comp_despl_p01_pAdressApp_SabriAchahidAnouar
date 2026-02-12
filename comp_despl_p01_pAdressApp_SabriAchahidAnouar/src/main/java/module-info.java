module es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar {

    requires javafx.controls;
    requires javafx.fxml;

    // Librerías de Jackson (Asegúrate de tener estas también)
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    // Librería de Bootstrap (si la usas)
    requires org.kordamp.bootstrapfx.core;
    requires java.prefs;

    // Permisos para JavaFX (Vista)
    opens es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view to javafx.fxml;

    // Permisos para JavaFX (Main)
    opens es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar to javafx.fxml;

    // --- ESTA ES LA LÍNEA QUE TE FALTA (Permiso para Jackson) ---
    opens es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model to com.fasterxml.jackson.databind;

    exports es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar;
}