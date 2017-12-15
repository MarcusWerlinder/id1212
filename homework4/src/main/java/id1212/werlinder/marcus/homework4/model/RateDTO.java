package id1212.werlinder.marcus.homework4.model;


/**
 * The interface for rate objects
 */
public interface RateDTO {
    Currency getFrom();

    void setFrom(Currency from);

    Currency getTo();

    void setTo(Currency to);

    float getRate();

    void setRate(float rate);
}
