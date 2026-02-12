package es.damdi.mainapp.comp_despl_p01_padressapp_sabriachahidanouar.model;

import java.time.LocalDate;

public class PersonPOJO {

    private String firstName;
    private String lastName;
    private String street;
    private int postalCode;
    private String city;
    private LocalDate birthday;

    // --- 1. CONSTRUCTOR VACÍO (Obligatorio para XML/JAXB) ---
    public PersonPOJO() {
    }

    // --- 2. CONSTRUCTOR CON ARGUMENTOS (Para facilitar la creación) ---
    public PersonPOJO(String firstName, String lastName, String street, int postalCode, String city, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.birthday = birthday;
    }

    // --- 3. GETTERS Y SETTERS ---

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // Importante: La anotación para que el XML entienda la fecha
//    @XmlJavaTypeAdapter(LocalDateAdapter.class)
//    public LocalDate getBirthday() {
//        return birthday;
//    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getBirthday() {
        return birthday;
    }
}