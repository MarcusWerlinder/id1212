package id1212.werlinder.marcus.homework4.model;

import javax.persistence.*;
import java.io.Serializable;

@NamedQueries({
    @NamedQuery(
            name = "getAllCurrencies",
            query = "SELECT currency FROM Currency currency"
    )
})

@Entity(name = "Currency")
public class Currency implements CurrencyDTO, Serializable{
    @Id
    @GeneratedValue
    private  long id;
    @Column(unique = true, nullable = false)
    private String name;

    public Currency() {
    }

    public Currency(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
