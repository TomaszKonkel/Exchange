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
		if (exchangeRepository.count() == 0) {


			try {
				//Użycie DocumentBuilderFactory do pobrania pliku z ECB
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.xml");
				doc.getDocumentElement().normalize();

				//Stworzenie NodeList który przechowuje element z pliku z datami
				NodeList dataList = (NodeList) doc.getElementsByTagName("Cube").item(0);
				//Zapytanie sprawdzające zawartość bazy


				//Pętla od dni w pliku od ECB (od ostatniego do pierwszego)
				for (int i = 0; i < dataList.getLength(); i++) {
					// Pobieranie walut i kursów na dany dzień i potem w pętli wpisywanie jej do zmiennej
					NodeList kursList = (NodeList) doc.getElementsByTagName("Cube").item(0).getChildNodes().item(i);


					//Pętla od walut na dany dzien
					for (int j = 0; j < kursList.getLength(); j++) {

						Node dataNode = dataList.item(i);
						Node kursNode = kursList.item(j);

						// Sprawdzenie kiedy kończą się waluty na dany dzień
						if (kursNode.getNodeType() == Node.ELEMENT_NODE) {

							// Pobieranie danych do bazy
							Element kursElement = (Element) kursNode;
							Element dataElement = (Element) dataNode;

							System.out.println(dataElement.getAttribute("time"));
							System.out.println(kursElement.getAttribute("currency"));
							System.out.println(kursElement.getAttribute("rate"));

							Exchange rates = new Exchange();


							rates.setCurrencyFrom("EUR");
							rates.setCurrencyTo(kursElement.getAttribute("currency"));
							rates.setDate(Date.valueOf(dataElement.getAttribute("time")));
							rates.setRate(new BigDecimal(kursElement.getAttribute("rate")));
							exchangeRepository.save(rates);
						}


					}
				}


			} catch (
					Exception e) {

			}

		}
	}
}




