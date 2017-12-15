package id1212.werlinder.marcus.homework4.view;

import id1212.werlinder.marcus.homework4.controller.Controller;
import id1212.werlinder.marcus.homework4.model.CurrencyDTO;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("manager")
@RequestScoped
public class Manager implements Serializable {
    @EJB
    private Controller controller;
    private float amntToConvert = 0;
    private float amntConverted = 0;
    private long fromId = 1;
    private long toId = 1;

    /**
     *
     * @return All the currencies on the database as a list
     */
    public List<? extends CurrencyDTO> getCurrencies() {
        return controller.getCurrencies();
    }

    public void convert() {
        amntConverted = controller.convert(fromId, toId, amntToConvert);
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public float getAmntToConvert() {
        return amntToConvert;
    }

    public void setAmntToConvert(float amntToConvert) {
        this.amntToConvert = amntToConvert;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long id) {
        this.fromId = id;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long id) {
        this.toId = id;
    }
}
