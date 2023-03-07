package com.example.Exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.sql.Date;

public class Update {
    public static void checkCompatibility(ExchangeRepository exchangeRepository) {
            try {
                // Getting xml file from ecb
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.xml");
                doc.getDocumentElement().normalize();

                // Collecting first date from doc
                NodeList dataList = (NodeList) doc.getElementsByTagName("Cube").item(0);
                Node dataNode = dataList.item(0);
                Element dataElement = (Element) dataNode;
                Date lastDataECB = Date.valueOf(dataElement.getAttribute("time"));

                // The most recent date from repo
                Date lastDateRepo = exchangeRepository.findTopByOrderByDateDesc().getDate();

                // If date from ECB is newer than date from repo, start updating
                if(lastDataECB.compareTo(lastDateRepo) > 0){
                    System.out.println("Dates doesn't match");
                    for (int i = dataList.getLength() - 1; i >= 0; i--) {
                        dataNode = dataList.item(i);
                        dataElement = (Element) dataNode;
                        Date dateInFor = Date.valueOf(dataElement.getAttribute("time"));

                        if (dateInFor.compareTo(lastDateRepo) > 0) {
                            System.out.println("Updating is in progress for date: " +" "+ dateInFor);
                            NodeList kursList = (NodeList) doc.getElementsByTagName("Cube").item(0).getChildNodes().item(i);
                            for (int j = 0; j < kursList.getLength(); j++) {

                                Node kursNode = kursList.item(j);

                                if (kursNode.getNodeType() == Node.ELEMENT_NODE) {

                                    Element kursElement = (Element) kursNode;

                                    Exchange update = new Exchange();

                                    update.setCurrencyFrom("EUR");
                                    update.setCurrencyTo(kursElement.getAttribute("currency"));
                                    update.setDate(Date.valueOf(dataElement.getAttribute("time")));
                                    update.setRate(new BigDecimal(kursElement.getAttribute("rate")));
                                    exchangeRepository.save(update);
                                }
                            }
                        }
                    }
                    System.out.println("Update is Over");
                }
                else{
                    System.out.println("Dates is match");
                }

            } catch (Exception e) {
                System.out.println("Error occurs while updating");
            }
    }
}

