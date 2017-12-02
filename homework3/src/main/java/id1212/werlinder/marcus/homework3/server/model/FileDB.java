package id1212.werlinder.marcus.homework3.server.model;

import id1212.werlinder.marcus.homework3.server.integration.HibernateStarter;

import org.hibernate.annotations.NamedNativeQuery;

import javax.persistence.*;

@Entity(name = "file")
@NamedNativeQuery(name = "checkFile", query = "select * from file where name = ?", resultClass = FileDB.class)
public class FileDB extends HibernateStarter{
    private long id;
    private String name;
    private long size;
    private UserDB owner;
    //Just setting some initial values
    private boolean publicAccess = false;
    private boolean writable = false;
    private boolean readable = false;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public long getSize() {
        return size;
    }

    @Column(nullable = false)
    public void setSize(long size) {
        this.size = size;
    }

    public boolean isPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(boolean publicAccess) {
        this.publicAccess = publicAccess;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean write) {
        this.writable = write;
    }

    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean read) {
        this.readable = read;
    }

    @ManyToOne(optional = false)
    public UserDB getOwner() {
        return owner;
    }

    public void setOwner(UserDB owner) {
        this.owner = owner;
    }
}
