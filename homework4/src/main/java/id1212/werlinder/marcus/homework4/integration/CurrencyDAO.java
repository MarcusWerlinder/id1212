package id1212.werlinder.marcus.homework4.integration;

import id1212.werlinder.marcus.homework4.model.Currency;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class CurrencyDAO {
    @PersistenceContext(unitName = "HibernatePU")
    private EntityManager em;

    /**
     * @return all the currencies in the database
     */
    public List<Currency> getCurrencies() {
        return em.createNamedQuery("getAllCurrencies", Currency.class).getResultList();
    }

    public Currency getCurrencyById(long id) {
        Currency currency = em.find(Currency.class, id);

        if (currency == null)
            throw new EntityNotFoundException(String.format("There is no currency with that id"));

        return currency;
    }
}
