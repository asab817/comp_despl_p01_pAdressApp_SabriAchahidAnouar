package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.view;

import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model.Person;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class GenerationsDonutController {

    @FXML
    private StackPane pane;

    private ObservableList<Person> personData;

    // Usamos ChartData oficial de TilesFX
    private ChartData silentGen;
    private ChartData babyBoomers;
    private ChartData genX;
    private ChartData millennials;
    private ChartData genZ;
    private ChartData genAlpha;

    @FXML
    private void initialize() {
        // 1. Usamos los colores NATIVOS de TilesFX para que quede idéntico a la demo
        silentGen   = new ChartData("Silent Gen", 0, Tile.BLUE);
        babyBoomers = new ChartData("Baby Boomers", 0, Tile.GREEN);
        genX        = new ChartData("Gen X", 0, Tile.MAGENTA);
        millennials = new ChartData("Millennials", 0, Tile.RED);
        genZ        = new ChartData("Gen Z", 0, Tile.YELLOW);
        genAlpha    = new ChartData("Gen Alpha", 0, Tile.ORANGE);

        // 2. Creamos el Tile sin forzar fondos raros, dejando el estilo por defecto
        Tile donutTile = TileBuilder.create()
                .skinType(Tile.SkinType.DONUT_CHART)
                .prefSize(500, 500)
                .title("Generaciones")
                .text("Componente DonutChart TilesFX")
                .chartData(silentGen, babyBoomers, genX, millennials, genZ, genAlpha)
                .animated(true) // Animación fluida de la demo
                .build();

        // 3. Lo centramos y añadimos
        pane.getChildren().clear();
        pane.getChildren().add(donutTile);
    }

    public void setPersonData(ObservableList<Person> personData) {
        this.personData = personData;

        // ESPÍA 1: Detecta altas y bajas en la tabla
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

        // ESPÍA 2: Detecta cuando editas una fecha de alguien que ya existía
        for (Person p : personData) {
            p.birthdayProperty().addListener((obs, oldVal, newVal) -> updateChart());
        }

        // Carga inicial
        updateChart();
    }

    private void updateChart() {
        int countSilent = 0, countBoomers = 0, countX = 0;
        int countMillennials = 0, countZ = 0, countAlpha = 0;

        for (Person p : personData) {
            if (p.getBirthday() != null) {
                int year = p.getBirthday().getYear();
                if (year >= 1928 && year <= 1945) countSilent++;
                else if (year >= 1946 && year <= 1964) countBoomers++;
                else if (year >= 1965 && year <= 1980) countX++;
                else if (year >= 1981 && year <= 1996) countMillennials++;
                else if (year >= 1997 && year <= 2012) countZ++;
                else if (year >= 2013) countAlpha++;
            }
        }

        // Al cambiar solo los "Values", TilesFX hace su animación mágica
        silentGen.setValue(countSilent);
        babyBoomers.setValue(countBoomers);
        genX.setValue(countX);
        millennials.setValue(countMillennials);
        genZ.setValue(countZ);
        genAlpha.setValue(countAlpha);
    }
}