package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.persistence;

import es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;
public interface PersonRepository {
    List<Person> load(File file) throws IOException;
    void save(File file, List<Person> persons) throws IOException;
}