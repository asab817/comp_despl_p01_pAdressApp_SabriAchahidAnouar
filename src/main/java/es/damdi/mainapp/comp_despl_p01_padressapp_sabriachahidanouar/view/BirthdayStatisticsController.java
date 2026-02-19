package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view;

import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

public class BirthdayStatisticsController {

    @FXML private BarChart<String, Integer> barChart;
    @FXML private CategoryAxis xAxis;

    private ObservableList<String> monthNames = FXCollections.observableArrayList();
    private ObservableList<Person> personData;

    @FXML
    private void initialize() {
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        monthNames.addAll(Arrays.asList(months));
        xAxis.setCategories(monthNames);
    }

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

    // Lógica que redibuja el gráfico al instante
    private void updateChart() {
        int[] monthCounter = new int[12];
        for (Person p : personData) {
            if (p.getBirthday() != null) {
                int month = p.getBirthday().getMonthValue() - 1;
                monthCounter[month]++;
            }
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Cumpleaños por mes");
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
        }

        barChart.getData().clear();
        barChart.getData().add(series);
    }
}