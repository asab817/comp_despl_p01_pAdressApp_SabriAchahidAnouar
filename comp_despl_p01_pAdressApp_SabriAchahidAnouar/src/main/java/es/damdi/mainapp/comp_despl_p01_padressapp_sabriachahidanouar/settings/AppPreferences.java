package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.settings;

import java.util.Optional;
import java.util.prefs.Preferences;

public final class AppPreferences {
    // Usa una ruta única para tu nodo de preferencias
    private static final Preferences PREFS = Preferences.userRoot().node("es.damdi.addressapp.sabri");
    private static final String KEY_PERSON_FILE = "personFile";

    private AppPreferences() {}

    public static Optional<String> getPersonFile() {
        String v = PREFS.get(KEY_PERSON_FILE, null);
        return (v == null || v.isBlank()) ? Optional.empty() : Optional.of(v);
    }

    public static void setPersonFile(String absolutePathOrNull) {
        if (absolutePathOrNull == null || absolutePathOrNull.isBlank()) {
            PREFS.remove(KEY_PERSON_FILE);
        } else {
            PREFS.put(KEY_PERSON_FILE, absolutePathOrNull);
        }
    }
}