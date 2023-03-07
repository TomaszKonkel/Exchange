package com.example.Exchange;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ExchangeController {
    private ExchangeRepository exchangeRepository;


    @Autowired
    public ExchangeController(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;

    }

    @GetMapping(value = "/exchange")
    public ResponseEntity addProject(@RequestParam String currencyFrom, String currencyTo, Date date) {
        Date lastDate = exchangeRepository.findTopByOrderByDateDesc().getDate();
        if(date.compareTo(lastDate) > 0){
            return ResponseEntity.ok("Given date doesn't exist in database ");
        }
        if (currencyFrom.equals("XXX") && currencyTo.equals("XXX")) {
            return check(currencyFrom, currencyTo, date);
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
                    return check(currencyFrom, currencyTo, date);
                } else {
                    BigDecimal rate = new BigDecimal("1").divide(result.getRate(), 2, RoundingMode.HALF_UP);
                    return ResponseEntity.ok(rate);
                }
            }
            if (currencyFrom.equals(currencyTo)) {
                return ResponseEntity.ok(1);
            }
            if (!currencyFrom.equals("EUR") && !currencyTo.equals("EUR")) {
                Exchange numeral = exchangeRepository.findByCurrencyFromAndCurrencyToAndDate("EUR", currencyTo, date);
                Exchange denominator = exchangeRepository.findByCurrencyFromAndCurrencyToAndDate("EUR", currencyFrom, date);
                if(denominator == null || numeral == null){
                    return check(currencyFrom, currencyTo, date);
                } else {
                    return ResponseEntity.ok(numeral.getRate().divide(denominator.getRate(), 2, RoundingMode.HALF_UP));
                }
            } else {
                Exchange result = exchangeRepository.findByCurrencyFromAndCurrencyToAndDate(currencyFrom, currencyTo, date);
                if(result == null){
                    return check(currencyFrom, currencyTo, date);
                }else {
                    return ResponseEntity.ok(exchangeRepository.findByCurrencyFromAndCurrencyToAndDate(currencyFrom, currencyTo, date).getRate());
                }

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
    public ResponseEntity check(String currencyFrom, String currencyTo, Date date) {
        Date fromDateLast = null;
        Date fromDateFirst = null;
        Date toDateLast = null;
        Date toDateFirst = null;

        if (currencyFrom.equals("XXX") && currencyTo.equals("XXX")) {
            return ResponseEntity.ok("Not choose ");

        } else {
            if(!currencyFrom.equals("EUR")) {
                fromDateLast = exchangeRepository.findTopByCurrencyTo(currencyFrom).getDate();
                fromDateFirst = exchangeRepository.findFirstByCurrencyToOrderByDateAsc(currencyFrom).getDate();
            }
            if(!currencyTo.equals("EUR")) {
                toDateLast = exchangeRepository.findTopByCurrencyTo(currencyTo).getDate();
                toDateFirst = exchangeRepository.findFirstByCurrencyToOrderByDateAsc(currencyTo).getDate();
            }

            if  (fromDateLast != null && date.compareTo(fromDateLast) > 0) {
                return ResponseEntity.ok("Currency from doesn't exist already ");

            }
            if (toDateLast != null && date.compareTo(toDateLast) > 0 ) {
                return ResponseEntity.ok("Currency to doesn't exist already ");

            }
            if (fromDateFirst !=null && fromDateFirst.compareTo(date) > 0 ) {
                return ResponseEntity.ok("Currency from doesn't exist yet ");
            }
            if (toDateFirst !=null && toDateFirst.compareTo(date) > 0 ) {
                return ResponseEntity.ok("Currency to doesn't exist yet ");

            }
            return ResponseEntity.ok("Good ");
        }
    }




}