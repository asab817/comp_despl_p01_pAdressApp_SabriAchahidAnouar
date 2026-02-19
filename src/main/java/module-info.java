module es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar {
    requires javafx.controls;
    requires javafx.fxml;

    // Librerías de ayuda (IcePDF, Markdown, Web)
    requires javafx.web;
    requires javafx.swing;
    requires java.desktop;
    requires icepdf.viewer;
    requires icepdf.core;
    requires flexmark;
    requires flexmark.util.ast;
    requires flexmark.util.data;

    // Librerías de TilesFX (Gráficos)
    requires eu.hansolo.tilesfx;
    requires eu.hansolo.toolbox;
    requires eu.hansolo.toolboxfx;
    requires eu.hansolo.fx.heatmap;
    requires eu.hansolo.fx.countries;

    // Librerías anteriores (Bootstrap, Jackson, Prefs)
    requires org.kordamp.bootstrapfx.core;
    requires java.prefs;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    // Apertura de paquetes a JavaFX y Jackson
    opens es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view to javafx.fxml;
    opens es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar to javafx.fxml;

    // Importante para guardar JSON
    opens es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model to com.fasterxml.jackson.databind;

    exports es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar;
}