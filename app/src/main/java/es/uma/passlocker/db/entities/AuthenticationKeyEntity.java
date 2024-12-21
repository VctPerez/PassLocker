package es.uma.passlocker.db.entities;

public class AuthenticationKeyEntity {
    private int id;
    private String authKey;
    private UserEntity user;
    private SiteEntity site;

    public AuthenticationKeyEntity(int id, String key, UserEntity user, SiteEntity site) {
        setId(id);
        setAuthKey(key);
        setUser(user);
        setSite(site);
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

    public String getAuthKey() {
        return authKey;
    }

    private void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public SiteEntity getSite() {
        return site;
    }

    private void setSite(SiteEntity site) {
        this.site = site;
    }

    public UserEntity getUser() {
        return user;
    }

    private void setUser(UserEntity user) {
        this.user = user;
    }
}
