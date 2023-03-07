package com.example.Exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
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
		Update.checkCompatibility(exchangeRepository);
	}
	@Scheduled(cron ="0 */5 16 ? * MON,TUE,WED,THU,FRI")
	public static void scheduledUpdate(){
		Update.checkCompatibility(exchangeRepository);
	}
}




