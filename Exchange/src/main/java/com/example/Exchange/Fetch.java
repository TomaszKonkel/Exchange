package com.example.Exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.sql.Date;

public class Fetch {

    private static ExchangeRepository exchangeRepository;

    @Autowired
    public Fetch(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;

    }



}

