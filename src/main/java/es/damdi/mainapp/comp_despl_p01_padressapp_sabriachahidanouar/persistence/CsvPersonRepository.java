package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.persistence;

import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model.Person;
import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.util.DateUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvPersonRepository implements PersonRepository {

    private static final String SEPARATOR = ";";

    @Override
    public List<Person> load(File file) throws IOException {
        System.out.println("--- INICIANDO CARGA CSV ---");
        List<Person> personas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            int contador = 0;
            while ((linea = br.readLine()) != null) {
                contador++;
                // Ignorar líneas vacías
                if (linea.trim().isEmpty()) continue;

                // Separar por punto y coma
                String[] campos = linea.split(SEPARATOR);

                System.out.println("Línea " + contador + ": " + linea);
                System.out.println("  -> Columnas detectadas: " + campos.length);

                if (campos.length >= 6) { // Aceptamos 6 o más columnas
                    try {
                        Person persona = new Person();
                        persona.setFirstName(campos[0].trim());
                        persona.setLastName(campos[1].trim());
                        persona.setStreet(campos[2].trim());

                        // Parsear código postal con seguridad
                        try {
                            persona.setPostalCode(Integer.parseInt(campos[3].trim()));
                        } catch (NumberFormatException e) {
                            persona.setPostalCode(0);
                        }

                        persona.setCity(campos[4].trim());

                        // Parsear fecha
                        persona.setBirthday(DateUtil.parse(campos[5].trim()));

                        personas.add(persona);
                        System.out.println("  -> Persona añadida OK: " + persona.getFirstName());
                    } catch (Exception e) {
                        System.out.println("  -> ERROR procesando datos de la persona: " + e.getMessage());
                    }
                } else {
                    System.out.println("  -> ERROR: La línea no tiene 6 columnas separadas por '" + SEPARATOR + "'");
                }
            }
        }
        System.out.println("--- FIN CARGA. Total personas: " + personas.size() + " ---");
        return personas;
    }

    @Override
    public void save(File file, List<Person> persons) throws IOException {
        System.out.println("--- INICIANDO GUARDADO CSV ---");
        System.out.println("Intentando guardar " + persons.size() + " personas.");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Person persona : persons) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(persona.getFirstName()).append(SEPARATOR);
                    sb.append(persona.getLastName()).append(SEPARATOR);
                    sb.append(persona.getStreet()).append(SEPARATOR);
                    sb.append(persona.getPostalCode()).append(SEPARATOR);
                    sb.append(persona.getCity()).append(SEPARATOR);

                    // Fecha
                    String fechaStr = DateUtil.format(persona.getBirthday());
                    sb.append(fechaStr); // No añadimos separador final para evitar columnas extra vacías

                    bw.write(sb.toString());
                    bw.newLine();
                } catch (Exception e) {
                    System.out.println("ERROR al escribir una persona: " + e.getMessage());
                }
            }
        }
        System.out.println("--- GUARDADO COMPLETADO ---");
    }
}