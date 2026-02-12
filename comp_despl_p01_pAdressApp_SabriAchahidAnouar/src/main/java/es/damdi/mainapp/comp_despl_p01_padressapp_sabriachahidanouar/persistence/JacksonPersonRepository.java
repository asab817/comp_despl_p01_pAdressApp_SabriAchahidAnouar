package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model.Person;
import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model.PersonListWrapper;
import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model.PersonPOJO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class JacksonPersonRepository implements PersonRepository {

    private final ObjectMapper mapper;

    public JacksonPersonRepository() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Para LocalDate
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // JSON bonito
    }

    @Override
    public List<Person> load(File file) throws IOException {
        if (file == null || !file.exists()) return List.of();

        PersonListWrapper wrapper = mapper.readValue(file, PersonListWrapper.class);
        List<Person> out = new ArrayList<>();
        if (wrapper != null && wrapper.persons != null) {
            for (PersonPOJO dto : wrapper.persons) {
                out.add(Person.fromPOJO(dto));
            }
        }
        return out;
    }

    @Override
    public void save(File file, List<Person> persons) throws IOException {
        if (file == null) return;
        if (file.getParentFile() != null) {
            Files.createDirectories(file.toPath().getParent());
        }

        PersonListWrapper wrapper = new PersonListWrapper();
        for (Person p : persons) {
            wrapper.persons.add(p.toPOJO());
        }
        mapper.writeValue(file, wrapper);
    }
}