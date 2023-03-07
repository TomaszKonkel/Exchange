package com.example.Exchange;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.sql.Date;

public class Fetch {
    public static void Download(ExchangeRepository exchangeRepository) {
        if (exchangeRepository.count() == 0) {
            System.out.println("Table is empty");
            try {
                //Getting xml file from ecb
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.xml");
                doc.getDocumentElement().normalize();

                //Elements which contains dates (is used to define number of dates)
                NodeList dataList = (NodeList) doc.getElementsByTagName("Cube").item(0);

                //For loop through every date starting with the oldest
                for (int i = dataList.getLength()-1 ; i >= 0; i--) {
                    // Elements which contains course (currency and rate)
                    NodeList kursList = (NodeList) doc.getElementsByTagName("Cube").item(0).getChildNodes().item(i);

                    //For loop through every course
                    for (int j = 0; j < kursList.getLength(); j++) {

                        Node dataNode = dataList.item(i);
                        Node kursNode = kursList.item(j);

                        // Checking that is still element of course
                        if (kursNode.getNodeType() == Node.ELEMENT_NODE) {

                            // Getting actual value of looking parameters
                            Element kursElement = (Element) kursNode;
                            Element dataElement = (Element) dataNode;

                            // Creating new istance of exchange class
                            Exchange rates = new Exchange();

                            // Setting to instance a new value for parameters and saving it
                            rates.setCurrencyFrom("EUR");
                            rates.setCurrencyTo(kursElement.getAttribute("currency"));
                            rates.setDate(Date.valueOf(dataElement.getAttribute("time")));
                            rates.setRate(new BigDecimal(kursElement.getAttribute("rate")));
                            exchangeRepository.save(rates);
                        }
                    }
                }
                System.out.println("Saving data was ended");
            } catch (Exception e) {
                System.out.println("Error occurs while fetching");
            }
        }
        else{
            System.out.println("Data is present in table");
        }
    }
}

