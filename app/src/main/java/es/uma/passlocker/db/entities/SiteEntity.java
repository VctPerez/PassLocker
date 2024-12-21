package es.uma.passlocker.db.entities;

public class SiteEntity {
    private int id;
    private String name;

    public SiteEntity(int id, String name) {
        setId(id);
        setName(name);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Id must be greater than 0");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}
