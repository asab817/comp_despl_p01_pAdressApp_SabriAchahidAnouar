package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view;

import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model.Person;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.util.Map;
import java.util.TreeMap;

public class LineChartStatisticsController {

    @FXML private LineChart<String, Integer> lineChart;
    @FXML private CategoryAxis xAxis;

    private ObservableList<Person> personData;

    public void setPersonData(ObservableList<Person> personData) {
        this.personData = personData;

        // Escuchar si se añade/borra gente a la tabla
        this.personData.addListener((ListChangeListener<Person>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Person p : c.getAddedSubList()) {
                        p.birthdayProperty().addListener((obs, oldVal, newVal) -> updateChart());
                    }
                }
            }
            updateChart();
        });

        // Escuchar si a alguien existente le cambian la fecha
        for (Person p : personData) {
            p.birthdayProperty().addListener((obs, oldVal, newVal) -> updateChart());
        }

        // Dibujar el gráfico por primera vez
        updateChart();
    }

    // Lógica que redibuja el gráfico
    private void updateChart() {
        Map<String, Integer> yearCounts = new TreeMap<>();

        for (Person p : personData) {
            if (p.getBirthday() != null) {
                String year = String.valueOf(p.getBirthday().getYear());
                yearCounts.put(year, yearCounts.getOrDefault(year, 0) + 1);
            }
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Nacimientos");

        for (Map.Entry<String, Integer> entry : yearCounts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }
}