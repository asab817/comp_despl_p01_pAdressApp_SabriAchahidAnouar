package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.persistence;

import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model.Person;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvPersonRepository implements PersonRepository {

    // Usamos punto y coma para que Excel lo reconozca bien en español
    private static final String SEPARATOR = ";";
    // Formato de fecha solicitado: DD-MM-YYYY
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public List<Person> load(File file) throws IOException {
        List<Person> persons = new ArrayList<>();

        // Leemos el archivo usando UTF-8
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Ignorar líneas vacías
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(SEPARATOR);

                // Aseguramos que tenga los 6 campos necesarios
                if (parts.length >= 6) {
                    Person p = new Person();
                    p.setFirstName(parts[0].trim());
                    p.setLastName(parts[1].trim());
                    p.setStreet(parts[2].trim());
                    p.setPostalCode(Integer.parseInt(parts[3].trim()));
                    p.setCity(parts[4].trim());

                    // Convertir el String de fecha a LocalDate
                    try {
                        p.setBirthday(LocalDate.parse(parts[5].trim(), DATE_FORMATTER));
                    } catch (Exception e) {
                        // Si la fecha falla, usamos la actual por seguridad
                        p.setBirthday(LocalDate.now());
                    }
                    persons.add(p);
                }
            }
        }
        return persons;
    }

    @Override
    public void save(File file, List<Person> persons) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            for (Person p : persons) {
                StringBuilder sb = new StringBuilder();
                sb.append(p.getFirstName()).append(SEPARATOR);
                sb.append(p.getLastName()).append(SEPARATOR);
                sb.append(p.getStreet()).append(SEPARATOR);
                sb.append(p.getPostalCode()).append(SEPARATOR);
                sb.append(p.getCity()).append(SEPARATOR);

                // Guardar fecha formateada
                if (p.getBirthday() != null) {
                    sb.append(DATE_FORMATTER.format(p.getBirthday()));
                } else {
                    sb.append("");
                }

                bw.write(sb.toString());
                bw.newLine(); // Salto de línea
            }
        }
    }
}