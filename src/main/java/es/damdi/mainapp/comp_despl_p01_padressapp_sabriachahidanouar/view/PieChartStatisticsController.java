package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view;

import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.util.HashMap;
import java.util.Map;

public class PieChartStatisticsController {

    @FXML
    private PieChart pieChart;

    // Guardamos la referencia a la lista para poder vigilarla
    private ObservableList<Person> personData;

    /**
     * Recibe los datos y les pone "espías" (Listeners) para que sea dinámico.
     */
    public void setPersonData(ObservableList<Person> personData) {
        this.personData = personData;

        // 1. ESPÍA DE LISTA: Vigila si se añade o borra a alguien nuevo
        this.personData.addListener((ListChangeListener<Person>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Person p : c.getAddedSubList()) {
                        // Si se añade alguien nuevo, le ponemos un espía a su cumpleaños
                        p.birthdayProperty().addListener((obs, oldVal, newVal) -> updateChart());
                    }
                }
            }
            // Actualizar la gráfica si hay altas o bajas
            updateChart();
        });

        // 2. ESPÍA DE EDICIÓN: Vigila si a alguien que ya existe le cambian la fecha de nacimiento
        for (Person p : personData) {
            p.birthdayProperty().addListener((obs, oldVal, newVal) -> updateChart());
        }

        // 3. Dibujar la gráfica por primera vez
        updateChart();
    }

    /**
     * Este método calcula los porcentajes y redibuja el gráfico.
     * Se ejecuta solo automáticamente cada vez que alguien edita una fecha.
     */
    private void updateChart() {
        Map<String, Integer> counts = new HashMap<>();
        String[] categorias = {
                "Silent Gen (1928-1945)",
                "Baby Boomers (1946-1964)",
                "Gen X (1965-1980)",
                "Millennials (1981-1996)",
                "Gen Z (1997-2012)",
                "Gen Alpha (>2013)"
        };

        for (String cat : categorias) counts.put(cat, 0);

        int totalPersons = 0;

        for (Person p : personData) {
            if (p.getBirthday() != null) {
                int year = p.getBirthday().getYear();
                totalPersons++;

                if (year >= 1928 && year <= 1945) counts.put(categorias[0], counts.get(categorias[0]) + 1);
                else if (year >= 1946 && year <= 1964) counts.put(categorias[1], counts.get(categorias[1]) + 1);
                else if (year >= 1965 && year <= 1980) counts.put(categorias[2], counts.get(categorias[2]) + 1);
                else if (year >= 1981 && year <= 1996) counts.put(categorias[3], counts.get(categorias[3]) + 1);
                else if (year >= 1997 && year <= 2012) counts.put(categorias[4], counts.get(categorias[4]) + 1);
                else if (year >= 2013) counts.put(categorias[5], counts.get(categorias[5]) + 1);
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (String cat : categorias) {
            int cantidad = counts.get(cat);
            if (cantidad > 0) {
                // Cálculo del porcentaje exacto
                double porcentaje = (double) cantidad / totalPersons * 100;
                String etiqueta = String.format("%s (%.1f%%)", cat, porcentaje);

                pieChartData.add(new PieChart.Data(etiqueta, cantidad));
            }
        }

        pieChart.setData(pieChartData);
    }
}