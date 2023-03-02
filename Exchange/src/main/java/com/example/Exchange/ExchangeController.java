package com.example.Exchange;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ExchangeController {
    private ExchangeRepository exchangeRepository;


    @Autowired
    public ExchangeController(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;

    }

    @GetMapping(value = "/exchange")
    public BigDecimal addProject(@RequestParam String currencyFrom, String currencyTo, Date date) {
        if (currencyFrom.equals("XXX") && currencyTo.equals("XXX")) {
            return new BigDecimal("0");
        } else {
            int day = date.getDay();
            if (day == 6) {
                date = Date.valueOf(date.toLocalDate().minusDays(1));
            }
            if (day == 0) {
                date = Date.valueOf(date.toLocalDate().minusDays(2));
            }
            if (currencyTo.equals("EUR")) {
                Exchange result = exchangeRepository.findByCurrencyFromAndCurrencyToAndDate(currencyTo, currencyFrom, date);
                if(result == null){
                    return new BigDecimal("0");
                } else {
                    BigDecimal rate = new BigDecimal("1").divide(result.getRate(), 2, RoundingMode.HALF_UP);
                    return rate;
                }
            }
            if (currencyFrom.equals(currencyTo)) {
                return new BigDecimal("1");
            }
            if (!currencyFrom.equals("EUR") && !currencyTo.equals("EUR")) {
                Exchange numeral = exchangeRepository.findByCurrencyFromAndCurrencyToAndDate("EUR", currencyTo, date);
                Exchange denominator = exchangeRepository.findByCurrencyFromAndCurrencyToAndDate("EUR", currencyFrom, date);
                if(denominator == null || numeral == null){
                    return new BigDecimal("0");
                } else {
                    return new BigDecimal(String.valueOf(numeral.getRate())).divide(denominator.getRate(), 2, RoundingMode.HALF_UP);
                }
            } else {
                return exchangeRepository.findByCurrencyFromAndCurrencyToAndDate(currencyFrom, currencyTo, date).getRate();
            }
        }
    }

    @GetMapping(value = "/from")
    public ArrayList to() {
        Iterable<Exchange> all = exchangeRepository.findAll();
        ArrayList currency = new ArrayList<>();
        currency.add("EUR");
        for (Exchange i : all) {
            if(!currency.contains(i.getCurrencyTo()))
            {
                currency.add(i.getCurrencyTo());
            }
        }

       return currency;
    }

    @GetMapping(value = "/check")
    public String check(@RequestParam String currencyFrom, String currencyTo, Date date) {
        if (currencyFrom.equals("XXX") && currencyTo.equals("XXX")) {
            return "Good";
        } else {
            Date fromDateLast = exchangeRepository.findTopByCurrencyTo(currencyFrom).getDate();
            Date toDateLast = exchangeRepository.findTopByCurrencyTo(currencyTo).getDate();

            Date fromDateFirst = exchangeRepository.findFirstByCurrencyToOrderByDateAsc(currencyFrom).getDate();
            Date toDateFirst = exchangeRepository.findFirstByCurrencyToOrderByDateAsc(currencyTo).getDate();


            if (date.compareTo(fromDateLast) > 0) {
                return "Currency from doesn't exist already";
            }
            if (date.compareTo(toDateLast) > 0) {
                return "Currency to doesn't exist already";
            }
            if (fromDateFirst.compareTo(date) > 0) {
                return "Currency from doesn't exist yet";
            }
            if (toDateFirst.compareTo(date) > 0) {
                return "Currency to doesn't exist yet";
            }
            return "Good";
        }
    }




}