package com.example.Exchange;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;


@Repository
public interface ExchangeRepository extends CrudRepository<Exchange, Integer> {

    Exchange findByCurrencyFromAndCurrencyToAndDate(String currencyFrom, String currencyTo, Date date);

    ArrayList findDistinctByCurrencyFrom(String eur);


    Iterable<Exchange> findByCurrencyTo(String eek);


    Exchange findTopByCurrencyTo(String currency);



    Exchange findLastByCurrencyTo(String currencyTo);
    Exchange findFirstByCurrencyToOrderByDateAsc(String currency);
}
