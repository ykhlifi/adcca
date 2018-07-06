package fr.sqli.paja.adccadnconfigapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;


@SpringBootApplication //(exclude = { RabbitAutoConfiguration.class })
@EnableConfigServer // pour lui donner le privilege d'un server de configuration 

public class DeclarationNaissanceConfigAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeclarationNaissanceConfigAppApplication.class, args);
	}
}
