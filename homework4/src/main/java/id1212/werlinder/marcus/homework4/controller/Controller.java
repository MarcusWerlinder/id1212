package id1212.werlinder.marcus.homework4.controller;

import id1212.werlinder.marcus.homework4.integration.CurrencyDAO;
import id1212.werlinder.marcus.homework4.integration.RateDAO;
import id1212.werlinder.marcus.homework4.model.Currency;
import id1212.werlinder.marcus.homework4.model.CurrencyDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class Controller {
    @EJB CurrencyDAO currencyDAO;
    @EJB RateDAO rateDAO;

    public List<? extends CurrencyDTO> getCurrencies() {
        return currencyDAO.getCurrencies();
    }

    /**
     *
     * @param fromId the id of the currency we want to exchange from
     * @param toId the id of the currency we want to exchange to
     * @param conversionAmnt the amount of money we want to convert
     * @return we get the exchange rate and return the converted amount
     */
    public float convert(long fromId, long toId, float conversionAmnt) {
        Currency from = currencyDAO.getCurrencyById(fromId);
        Currency to = currencyDAO.getCurrencyById(toId);

        return rateDAO.getRate(from, to).getRate() * conversionAmnt;
    }
}
