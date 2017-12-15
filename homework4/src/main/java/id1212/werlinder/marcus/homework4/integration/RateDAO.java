package id1212.werlinder.marcus.homework4.integration;

import id1212.werlinder.marcus.homework4.model.Currency;
import id1212.werlinder.marcus.homework4.model.Rate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class RateDAO {
    @PersistenceContext(unitName = "HibernatePU")
    private EntityManager em;

    public Rate getRate(Currency from, Currency to) {
        try {
            return em.createNamedQuery("getRateByCurrency", Rate.class)
                    .setParameter("fromCurr", from)
                    .setParameter("toCurr", to)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
