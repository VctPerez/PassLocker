package es.uma.passlocker.db.entities;

import androidx.annotation.NonNull;

public class PasswordInfoEntity {
    private int id;
    private String siteName;
    private String siteUrl;
    private String notes;
    private UserEntity user;
    private String password;

    public PasswordInfoEntity(int id, UserEntity user, String siteName, String siteUrl, String notes, String password) {
        setId(id);
        setNotes(notes);
        setUser(user);
        setSiteName(siteName);
        setSiteUrl(siteUrl);
        setPassword(password);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public UserEntity getUser() {
        return user;
    }

    private void setUser(UserEntity user) {
        this.user = user;
    }

    @NonNull
    @Override
    public String toString() {
        return "";
    }
}
