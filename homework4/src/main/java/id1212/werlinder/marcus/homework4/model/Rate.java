package id1212.werlinder.marcus.homework4.model;

import javax.persistence.*;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(
                name = "getRateByCurrency",
                query = "SELECT rate FROM Rate rate WHERE rate.from=:fromCurr AND rate.to=:toCurr"
        )
})

@Entity(name = "Rate")
public class Rate implements RateDTO, Serializable {
    @Id @ManyToOne
    private  Currency from;
    @Id @ManyToOne
    private Currency to;
    private  float rate;

    public Rate(){
    }

    public Rate(Currency from, Currency to, float rate){
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    public Currency getFrom() {
        return from;
    }

    public void setFrom(Currency from) {
        this.from = from;
    }

    public Currency getTo() {
        return to;
    }

    public void setTo(Currency to) {
        this.to = to;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
