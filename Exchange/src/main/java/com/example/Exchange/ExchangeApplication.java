package com.example.Exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.sql.Date;


@SpringBootApplication
public class ExchangeApplication {

	private static ExchangeRepository exchangeRepository;

	@Autowired
	public ExchangeApplication(ExchangeRepository exchangeRepository) {
		this.exchangeRepository = exchangeRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(ExchangeApplication.class, args);
		Fetch.Download(exchangeRepository);
	}
}




