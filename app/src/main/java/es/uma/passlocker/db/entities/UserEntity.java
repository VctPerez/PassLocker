package es.uma.passlocker.db.entities;

public class UserEntity {
    private int id;
    private String username;

    public UserEntity(int id, String username) {
        setId(id);
        setUsername(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(id < 0) {
            throw new IllegalArgumentException("Id must be greater than 0");
        }
        this.id = id;
    }
}
