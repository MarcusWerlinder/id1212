package id1212.werlinder.marcus.homework3.server.model;

import id1212.werlinder.marcus.homework3.server.integration.HibernateStarter;
import org.hibernate.annotations.NamedNativeQuery;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "user")
@NamedNativeQuery(name = "getUser", query = "select * from user where username = ? and password = ?", resultClass = UserDB.class)
@NamedNativeQuery(name = "checkDouble", query = "select * from user where username = ?", resultClass = UserDB.class)
public class UserDB extends HibernateStarter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
